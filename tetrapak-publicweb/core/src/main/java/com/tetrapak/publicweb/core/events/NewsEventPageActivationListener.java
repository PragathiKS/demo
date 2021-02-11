package com.tetrapak.publicweb.core.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.ReplicationAction;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.publicweb.core.beans.NewsEventBean;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.models.FooterConfigurationModel;
import com.tetrapak.publicweb.core.models.HeaderConfigurationModel;
import com.tetrapak.publicweb.core.services.PardotService;
import com.tetrapak.publicweb.core.services.SubscriptionMailService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import com.tetrapak.publicweb.core.utils.PageUtil;

/**
 * The listener interface for receiving newsEventPageActivation events. The class that is interested in processing a
 * newsEventPageActivation event implements this interface, and the object created with that class is registered with a
 * component using the component's <code>addNewsEventPageActivationListener<code> method. When the
 * newsEventPageActivation event occurs, that object's appropriate method is invoked.
 *
 * @see NewsEventPageActivationEvent
 */
@Component(
        immediate = true,
        configurationPolicy = ConfigurationPolicy.REQUIRE,
        service = EventHandler.class,
        property = {
                Constants.SERVICE_DESCRIPTION
                        + "=This event handler listens the event on news and events page activation",
                EventConstants.EVENT_TOPIC + "=" + ReplicationAction.EVENT_TOPIC, EventConstants.EVENT_FILTER
                        + "=(paths=/content/tetrapak/publicweb/**/about-tetra-pak/news-and-events/**)" })
public class NewsEventPageActivationListener implements EventHandler {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(NewsEventPageActivationListener.class);

    /** The resolver factory. */
    @Reference
    private ResourceResolverFactory resolverFactory;

    /** The mail service. */
    @Reference
    private SubscriptionMailService mailService;

    /** The pardot service. */
    @Reference
    private PardotService pardotService;

    /**
     * Handle event.
     *
     * @param event
     *            the event
     */
    @Override
    public void handleEvent(Event event) {
        LOGGER.debug("Event is registered : {}", event.getTopic());
        try (final ResourceResolver resourceResolver = GlobalUtil.getResourceResolverFromSubService(resolverFactory)) {
            String[] paths = (String[]) event.getProperty("paths");
            if (Objects.nonNull(paths) && Objects.nonNull(resourceResolver)) {
                for (String path : paths) {
                    Resource resource = resourceResolver.getResource(path + "/jcr:content");
                    processData(resourceResolver, path, resource);
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Error in NewsEventPageActivationListener {}", ex.getMessage());
        }

    }

    /**
     * Process data.
     *
     * @param resourceResolver
     *            the resource resolver
     * @param path
     *            the path
     * @param resource
     *            the resource
     * @param valueMap
     *            the value map
     * @throws PersistenceException
     *             the persistence exception
     */
    private void processData(final ResourceResolver resourceResolver, String path, Resource resource)
            throws PersistenceException {
        ValueMap valueMap = resource.getValueMap();
        if (Objects.isNull(valueMap.get(PWConstants.EVENT_PUBLISHED_PROPERTY))
                && PWConstants.PRESS_TEMPLATES.contains(valueMap.get(PWConstants.CQ_TEMPLATE, String.class))) {
            NewsEventBean bean = getNewsEventBean(valueMap, path, resourceResolver);
            addPageLinks(bean, path, resourceResolver);
            List<String> emailAddresses = pardotService.getSubscriberMailAddresses(bean);
            if (Objects.nonNull(emailAddresses) && !emailAddresses.isEmpty()) {
                String status = mailService.sendSubscriptionEmail(bean, emailAddresses, resourceResolver);
                if (status.equalsIgnoreCase(PWConstants.STATUS_SUCCESS)) {
                    updatePageActivationProperty(resource);
                }
            }
        }
    }

    /**
     * Update first time page activation property.
     *
     * @param resource
     *            the resource
     * @throws PersistenceException
     *             the persistence exception
     */
    private void updatePageActivationProperty(Resource resource) throws PersistenceException {
        ModifiableValueMap modifiableMap = resource.adaptTo(ModifiableValueMap.class);
        List<String> propertyInheritanceCancelled = new ArrayList<>();
        String[] propertyValues = modifiableMap.get("cq:propertyInheritanceCancelled", String[].class);
        if (Objects.nonNull(propertyValues)) {
            for (String property : propertyValues) {
                propertyInheritanceCancelled.add(property);
            }
        }
        propertyInheritanceCancelled.add(PWConstants.EVENT_PUBLISHED_PROPERTY);
        modifiableMap.put(PWConstants.EVENT_PUBLISHED_PROPERTY, "true");
        modifiableMap.put("cq:propertyInheritanceCancelled", propertyInheritanceCancelled.toArray());
        resource.getResourceResolver().commit();
    }

    /**
     * Gets the news event bean.
     *
     * @param valueMap
     *            the value map
     * @param pagePath
     *            the page path
     * @param resolver
     *            the resolver
     * @return the news event bean
     */
    public NewsEventBean getNewsEventBean(ValueMap valueMap, String pagePath, ResourceResolver resolver) {
        NewsEventBean bean = new NewsEventBean();
        PageManager pageManager = resolver.adaptTo(PageManager.class);
        List<String> interestAreas = null;
        String rootPath = LinkUtils.getRootPath(pagePath);
        Page page = pageManager.getPage(LinkUtils.getRootPath(rootPath));
        bean.setLanguage(PageUtil.getLanguageCode(page));
        bean.setLocale(PageUtil.getLocaleFromURL(page));
        bean.setTitle(Objects.nonNull(valueMap.get("jcr:title", String.class)) ? valueMap.get("jcr:title", String.class)
                : StringUtils.EMPTY);
        bean.setDescription(Objects.nonNull(valueMap.get("jcr:description", String.class))
                ? valueMap.get("jcr:description", String.class)
                : StringUtils.EMPTY);
        if (Objects.nonNull(valueMap.get(PWConstants.CQ_TAGS_PROPERTY, String[].class))) {
            interestAreas = getInterestAreas(valueMap.get(PWConstants.CQ_TAGS_PROPERTY, String[].class));
            if (Objects.nonNull(interestAreas)) {
                bean.setInterestAreas(interestAreas);
            }
        }
        bean.setPageLink(resolver.map(pagePath));
        bean.setNewsroomLink("#");
        bean.setLegalInformationLink("#");
        bean.setManagePreferenceLink("#");
        bean.setHeaderLogo(getHeaderLogo(pagePath, resolver));
        bean.setFooterLogo(getFooterLogo(pagePath, resolver));
        bean.setHeroImage(getBannerImage(pagePath, resolver));
        return bean;
    }

    /**
     * Gets the header logo.
     *
     * @param pagePath
     *            the page path
     * @param resolver
     *            the resolver
     * @return the header logo
     */
    private String getHeaderLogo(String pagePath, ResourceResolver resolver) {
        String rootPath = LinkUtils.getRootPath(pagePath);
        final String path = rootPath + "/jcr:content/root/responsivegrid/headerconfiguration";
        final Resource headerConfigurationResource = resolver.getResource(path);
        if (Objects.nonNull(headerConfigurationResource)) {
            final HeaderConfigurationModel configurationModel = headerConfigurationResource
                    .adaptTo(HeaderConfigurationModel.class);
            if (Objects.nonNull(configurationModel)) {
                return configurationModel.getLogoImagePath();
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * Gets the footer logo.
     *
     * @param pagePath
     *            the page path
     * @param resolver
     *            the resolver
     * @return the footer logo
     */
    private String getFooterLogo(String pagePath, ResourceResolver resolver) {
        String rootPath = LinkUtils.getRootPath(pagePath);
        final String path = rootPath + "/jcr:content/root/responsivegrid/footerconfiguration";
        final Resource footerConfigurationResource = resolver.getResource(path);
        if (Objects.nonNull(footerConfigurationResource)) {
            final FooterConfigurationModel configurationModel = footerConfigurationResource
                    .adaptTo(FooterConfigurationModel.class);
            if (Objects.nonNull(configurationModel)) {
                return configurationModel.getLogoImagePath();
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * Gets the banner image.
     *
     * @param pagePath
     *            the page path
     * @param resolver
     *            the resolver
     * @return the banner image
     */
    private String getBannerImage(String pagePath, ResourceResolver resolver) {
        String path = pagePath + "/jcr:content/root/responsivegrid";
        Resource resource = resolver.getResource(path);
        Iterator<Resource> children = resource.listChildren();
        while (children.hasNext()) {
            Resource child = children.next();
            if (child.getName().startsWith("banner") && child.isResourceType("publicweb/components/content/banner")) {
                ValueMap valueMap = child.getValueMap();
                if (Objects.nonNull(valueMap.get(PWConstants.FILE_REFERENCE, String.class))) {
                    return valueMap.get(PWConstants.FILE_REFERENCE, String.class);
                }
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * Adds the page links.
     *
     * @param bean
     *            the bean
     * @param pagePath
     *            the page path
     * @param resolver
     *            the resolver
     * @return the news event bean
     */
    private NewsEventBean addPageLinks(NewsEventBean bean, String pagePath, ResourceResolver resolver) {
        String rootPath = LinkUtils.getRootPath(pagePath);
        final String path = rootPath + "/jcr:content/root/responsivegrid/subscriptionformconf";
        Resource subcriptionFormConfigResource = resolver.getResource(path);
        if (Objects.nonNull(subcriptionFormConfigResource)) {
            ValueMap valueMap = subcriptionFormConfigResource.getValueMap();
            if (Objects.nonNull(valueMap.get("pressroomLink", String.class))) {
                bean.setNewsroomLink(resolver.map(valueMap.get("pressroomLink", String.class)));
            }
            if (Objects.nonNull(valueMap.get("legalInfoLink", String.class))) {
                bean.setLegalInformationLink(resolver.map(valueMap.get("legalInfoLink", String.class)));
            }
            if (Objects.nonNull(valueMap.get("managePreferenceLink", String.class))) {
                bean.setManagePreferenceLink(resolver.map(valueMap.get("managePreferenceLink", String.class)));
            }
        }
        return bean;
    }

    /**
     * Gets the interest areas.
     *
     * @param tags
     *            the tags
     * @return the interest areas
     */
    private List<String> getInterestAreas(String[] tags) {
        List<String> interestAreas = new ArrayList<>();
        for (String tagId : tags) {
            switch (tagId) {
                case PWConstants.PW_TAG_SERVICES:
                    interestAreas.add("Services");
                    break;
                case PWConstants.PW_TAG_SUSTAINIBILITY:
                    interestAreas.add("Sustainability");
                    break;
                case PWConstants.PW_TAG_PACKAGING:
                    interestAreas.add("Packaging");
                    break;
                case PWConstants.PW_TAG_INNOVATION:
                    interestAreas.add("Innovation");
                    break;
                case PWConstants.PW_TAG_PROCESSING:
                    interestAreas.add("Processing");
                    break;
                case PWConstants.PW_TAG_END_TO_END_SOLUTIONS:
                    interestAreas.add("End-To-End");
                    break;
                default:
                    break;
            }
        }
        return interestAreas;
    }

}
