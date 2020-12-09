package com.tetrapak.publicweb.core.events;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
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
                        + "glob:/content/tetrapak/publicweb/lang-masters/sv_se**/jcr:content",
                ResourceChangeListener.PATHS + "=" + "glob:/content/tetrapak/publicweb/lang-masters/ru**/jcr:content",
                ResourceChangeListener.PATHS + "=" + "glob:/content/tetrapak/publicweb/lang-masters/zh/**/jcr:content",
                ResourceChangeListener.PATHS + "=" + "glob:/content/tetrapak/publicweb/lang-masters/tr/**/jcr:content",
                ResourceChangeListener.PATHS + "=" + "glob:/content/tetrapak/publicweb/lang-masters/ja/**/jcr:content",
                ResourceChangeListener.PATHS + "=" + "glob:/content/tetrapak/publicweb/lang-masters/pt/**/jcr:content",
                ResourceChangeListener.CHANGES + "=" + "CHANGED",
                ResourceChangeListener.PROPERTY_NAMES_HINT + "=cq:ctTranslated"

        })
public class LionBridgeTranslationListner implements ResourceChangeListener {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LionBridgeTranslationListner.class);

    /** The Constant CQ_CT_TRANSLATED. */
    private static final String CQ_CT_TRANSLATED = "cq:ctTranslated";

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
     */
    private void processChange(final ResourceChange change, final ResourceResolver resolver) {
        if (change.getPath().contains(JcrConstants.JCR_CONTENT)) {
            final String jcrContentPath = StringUtils.substringBefore(change.getPath(),
                    PWConstants.SLASH + JcrConstants.JCR_CONTENT);
            LOGGER.info("LionBridgeTranslationListener Listenering on :: {}", jcrContentPath);
            LOGGER.info("LionBridgeTranslationListener change on1");
            final Resource jcrResource = resolver.getResource(jcrContentPath);
            final ValueMap valueMap = jcrResource.getValueMap();
            if (valueMap.containsKey(CQ_CT_TRANSLATED) && valueMap.containsKey(PWConstants.CQ_LAST_MODIFIED)) {
                LOGGER.info("LionBridgeTranslationListener change 1");
                Calendar lastModified = valueMap.get(PWConstants.CQ_LAST_MODIFIED, Calendar.class);
                Calendar ctTranslated = valueMap.get(CQ_CT_TRANSLATED, Calendar.class);
                LOGGER.info("time comparision :: {}", ctTranslated.before(lastModified));
                if (!ctTranslated.before(lastModified)) {
                    LOGGER.info("LionBridgeTranslationListener change 2");
                    String language = PageUtil.getLanguageCodeFromResource(resolver.getResource(jcrContentPath));
                    LOGGER.info("Payload path  jcrContentPath:: {}", jcrContentPath);
                    LOGGER.info("language:: {}", language);
                    createLiveCopyService.createLiveCopy(resolver, jcrContentPath, rolloutManager, liveRelManager,
                            language);
                }
            }
        }
    }
}
