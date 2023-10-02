package com.tetrapak.publicweb.core.events;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.msm.api.LiveRelationshipManager;
import com.day.cq.wcm.msm.api.RolloutManager;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.CreateLiveCopyService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;
import com.tetrapak.publicweb.core.utils.PageUtil;

/**
 * The Class LionBridgeTranslationListner.
 */
@Component(
        immediate = true,
        configurationPolicy = ConfigurationPolicy.REQUIRE,
        service = ResourceChangeListener.class,
        property = {
                ResourceChangeListener.PATHS + "=" + "glob:/content/tetrapak/publicweb/lang-masters/de/**/jcr:content",
                ResourceChangeListener.PATHS + "=" + "glob:/content/tetrapak/publicweb/lang-masters/fr/**/jcr:content",
                ResourceChangeListener.PATHS + "=" + "glob:/content/tetrapak/publicweb/lang-masters/it/**/jcr:content",
                ResourceChangeListener.PATHS + "=" + "glob:/content/tetrapak/publicweb/lang-masters/es/**/jcr:content",
                ResourceChangeListener.PATHS + "="
                        + "glob:/content/tetrapak/publicweb/lang-masters/sv_se/**/jcr:content",
                ResourceChangeListener.PATHS + "=" + "glob:/content/tetrapak/publicweb/lang-masters/zh/**/jcr:content",
                ResourceChangeListener.PATHS + "=" + "glob:/content/tetrapak/publicweb/lang-masters/tr/**/jcr:content",
                ResourceChangeListener.PATHS + "=" + "glob:/content/tetrapak/publicweb/lang-masters/ja/**/jcr:content",
                ResourceChangeListener.PATHS + "=" + "glob:/content/tetrapak/publicweb/lang-masters/pt/**/jcr:content",
                ResourceChangeListener.PATHS + "=" + "glob:/content/experience-fragments/publicweb/**/jcr:content",
                ResourceChangeListener.CHANGES + "=" + "CHANGED",
                ResourceChangeListener.PROPERTY_NAMES_HINT + "=cq:ctTranslated",
                ResourceChangeListener.PROPERTY_NAMES_HINT + "=cq:lastModified"

        })
public class LionBridgeTranslationListner implements ResourceChangeListener {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LionBridgeTranslationListner.class);

    /** The Constant CQ_CT_TRANSLATED. */
    private static final String CQ_CT_TRANSLATED = "cq:ctTranslated";

    /** The Constant GL_SERVICE. */
    private static final String GL_SERVICE = "gl-service";

    /** The Constant LB_TRANSLATED_PAGES. */
    public static final String LB_TRANSLATED_PAGES = "lbtranslatedpages";

    /** The resolverFactory. */
    @Reference
    private ResourceResolverFactory resolverFactory;

    /** The create live copy service. */
    @Reference
    private CreateLiveCopyService createLiveCopyService;

    /** The rollout manager. */
    @Reference
    private RolloutManager rolloutManager;

    /** The live rel manager. */
    @Reference
    private LiveRelationshipManager liveRelManager;

    /**
     * On change.
     *
     * @param changes
     *            the changes
     */
    @Override
    public void onChange(final List<ResourceChange> changes) {
        LOGGER.debug("{{LionBridgeTranslationListener started}}");
        try (final ResourceResolver resolver = GlobalUtil.getResourceResolverFromSubService(resolverFactory)) {
            if (Objects.nonNull(resolver)) {
                for (final ResourceChange change : changes) {
                    processChange(change, resolver);
                }
            }
        } catch (PersistenceException e) {
            LOGGER.error("Error :: {}", e.getMessage(), e);
        } finally {
            LOGGER.debug("{{LionBridgeTranslationListener ended}}");
        }
    }

    /**
     * Process change.
     *
     * @param change
     *            the change
     * @param resolver
     *            the resolver
     * @throws PersistenceException
     *             the persistence exception
     */
    private void processChange(final ResourceChange change, final ResourceResolver resolver)
            throws PersistenceException {
        String changePath = change.getPath();
        if (changePath.contains(JcrConstants.JCR_CONTENT)) {
            final String contentPath = StringUtils.substringBefore(changePath,
                    PWConstants.SLASH + JcrConstants.JCR_CONTENT);
            LOGGER.info("LionBridgeTranslationListener Listenering on :: {}", changePath);
            LOGGER.info(" contentPath :: {}", contentPath);
            final Resource jcrResource = resolver.getResource(changePath);
            LOGGER.info("jcr resource path : {}", jcrResource.getPath());
            final ValueMap valueMap = jcrResource.getValueMap();
            if ((valueMap.containsKey(CQ_CT_TRANSLATED) || GL_SERVICE.equalsIgnoreCase(Objects.requireNonNull(change.getUserId()) ))
                    && valueMap.containsKey(PWConstants.CQ_LAST_MODIFIED)) {
                LOGGER.info("LionBridgeTranslationListener inside if");
                Calendar lastModified = valueMap.get(PWConstants.CQ_LAST_MODIFIED, Calendar.class);
                Calendar ctTranslated = valueMap.get(CQ_CT_TRANSLATED, Calendar.class);
                Boolean translationReceived = Boolean.FALSE;
                if(null != ctTranslated && !ctTranslated.before(lastModified)){
                    LOGGER.info("time comparision :: {}", ctTranslated.before(lastModified));
                    translationReceived = Boolean.TRUE;
                }
                if (GL_SERVICE.equalsIgnoreCase(Objects.requireNonNull(change.getUserId()))){
                    LOGGER.info("Translation received by TransPerfect :: ");
                    translationReceived = Boolean.TRUE;
                }

                if (translationReceived) {
                    String language = PageUtil.getLanguageCodeFromResource(resolver.getResource(contentPath));
                    LOGGER.info("language:: {}", language);
                    createOrUpdateLBTraslatedNode(resolver, contentPath);
                }
            }
        }
    }

    /**
     * Creates the or update LB traslated node.
     *
     * @param resolver
     *            the resolver
     * @param contentPath
     *            the content path
     * @throws PersistenceException
     *             the persistence exception
     */
    private void createOrUpdateLBTraslatedNode(final ResourceResolver resolver, final String contentPath)
            throws PersistenceException {
        LOGGER.debug("Inside createOrUpdateLBTraslatedNode");
        Resource lbTraslatedRes = resolver.getResource(PWConstants.LB_TRANSLATED_PATH);
        String nodeName;
        Map<String, Object> properties = new HashMap<>();
        properties.put(PWConstants.JCR_PRIMARY_TYPE, PWConstants.NT_UNSTRUCTURED);
        properties.put(LB_TRANSLATED_PAGES, contentPath);
        nodeName = PWConstants.LB_TRNSLATED_PAGES + PWConstants.HYPHEN + Calendar.getInstance().getTimeInMillis();
        if (Objects.nonNull(lbTraslatedRes)) {
            createNode(resolver, properties, nodeName, lbTraslatedRes);
            LOGGER.debug("Node created with name :{}", nodeName);
        } else {
            Map<String, Object> prop = new HashMap<>();
            prop.put(PWConstants.JCR_PRIMARY_TYPE, PWConstants.NT_UNSTRUCTURED);
            nodeName = PWConstants.LB_TRNSLATED_PAGES;
            Resource res = resolver.getResource(PWConstants.VAR_COMMERCE_PATH);
            createNode(resolver, prop, nodeName, res);
            Resource resource = resolver.getResource(PWConstants.LB_TRANSLATED_PATH);
            nodeName = PWConstants.LB_TRNSLATED_PAGES + PWConstants.HYPHEN + Calendar.getInstance().getTimeInMillis();
            createNode(resolver, properties, nodeName, resource);
            LOGGER.debug("Node created with name :{}", nodeName);
        }
        resolver.commit();
    }

    /**
     * Creates the node.
     *
     * @param resolver
     *            the resolver
     * @param properties
     *            the properties
     * @param nodeName
     *            the node name
     * @param res
     *            the res
     * @throws PersistenceException
     *             the persistence exception
     */
    private void createNode(final ResourceResolver resolver, final Map<String, Object> properties, String nodeName,
            Resource res) throws PersistenceException {
        resolver.create(res, nodeName, properties);
    }
}
