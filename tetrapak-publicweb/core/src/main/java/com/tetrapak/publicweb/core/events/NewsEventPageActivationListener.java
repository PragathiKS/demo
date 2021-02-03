package com.tetrapak.publicweb.core.events;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ModifiableValueMap;
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
                        + "=(paths=/content/tetrapak/publicweb/**/about-tetra-pak/news-and-events/*)" })
public class NewsEventPageActivationListener implements EventHandler {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(NewsEventPageActivationListener.class);

    /** The resolver factory. */
    @Reference
    private ResourceResolverFactory resolverFactory;

    /** The mail service. */
    @Reference
    private SubscriptionMailService mailService;

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
        try {
            String[] paths = (String[]) event.getProperty("paths");
            final ResourceResolver resourceResolver = GlobalUtil.getResourceResolverFromSubService(resolverFactory);
            if (Objects.nonNull(paths) && Objects.nonNull(resourceResolver)) {
                for (String path : paths) {
                    Resource resource = resourceResolver.getResource(path + "/jcr:content");
                    ValueMap valueMap = resource.getValueMap();
                    if (Objects.isNull(valueMap.get("eventPublished"))) {
                        NewsEventBean bean = getNewsEventBean(valueMap, path, resourceResolver);
                        List<String> emailAddresses = pardotService.getSubscriberMailAddresses(bean);
                        if (Objects.nonNull(emailAddresses) && !emailAddresses.isEmpty()) {
                            String status = mailService.sendSubscriptionEmail(bean, emailAddresses, resourceResolver);
                            if (status.equalsIgnoreCase(PWConstants.STATUS_SUCCESS)) {
                                ModifiableValueMap modifiableMap = resource.adaptTo(ModifiableValueMap.class);
                                modifiableMap.put("eventPublished", "true");
                                resource.getResourceResolver().commit();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Error in NewsEventPageActivationListener {}", ex.getMessage());
        }

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
        String rootPath = LinkUtils.getRootPath(pagePath);
        String newsRoomPagePath = rootPath + "/about-tetra-pak/news-and-events/news-room";
        String legalInfoPagePath = rootPath + "/about-tetra-pak/legal-information";
        String managePreferencePagePath = rootPath + "/about-tetra-pak/manage-preference";
        Page page = pageManager.getPage(LinkUtils.getRootPath(rootPath));
        bean.setLanguage(PageUtil.getLanguageCode(page));
        bean.setLocale(PageUtil.getLocaleFromURL(page));
        bean.setTitle(Objects.nonNull(valueMap.get("jcr:title", String.class)) ? valueMap.get("jcr:title", String.class)
                : StringUtils.EMPTY);
        bean.setDescription(Objects.nonNull(valueMap.get("jcr:description", String.class))
                ? valueMap.get("jcr:description", String.class)
                : StringUtils.EMPTY);
        if (Objects.nonNull(valueMap.get("cq:tags", String[].class))) {
            bean.setPageTags(valueMap.get("cq:tags", String[].class));
        }
        bean.setPageLink(resolver.map(pagePath));
        bean.setNewsroomLink(resolver.map(newsRoomPagePath));
        bean.setLegalInformationLink(resolver.map(legalInfoPagePath));
        bean.setManagePreferenceLink(managePreferencePagePath);
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
        String path = pagePath + "/jcr:content/root/responsivegrid/banner";
        Resource bannerResource = resolver.getResource(path);
        if (Objects.nonNull(bannerResource)) {
            ValueMap valueMap = bannerResource.getValueMap();
            if (Objects.nonNull(valueMap.get("fileReference", String.class))) {
                return valueMap.get("fileReference", String.class);
            }
        }
        return StringUtils.EMPTY;
    }

}
