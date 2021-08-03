package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ExporterConstants;
import com.tetrapak.publicweb.core.beans.DeviceTypeBean;
import com.tetrapak.publicweb.core.beans.StoriesBean;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.models.multifield.SemiAutomaticModel;
import com.tetrapak.publicweb.core.services.AggregatorService;
import com.tetrapak.publicweb.core.services.DynamicMediaService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;
import com.tetrapak.publicweb.core.utils.LinkUtils;

/**
 * The Class StoriesModel.
 */
@Model(
        adaptables = SlingHttpServletRequest.class,
        resourceType = "publicweb/components/content/stories",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class StoriesModel {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(StoriesModel.class);

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The resource. */
    private Resource resource;

    /** The content type. */
    @ValueMapValue
    private String contentType;

    /** The tags. */
    @ValueMapValue
    private String[] tags;

    /** The max tabs. */
    @ValueMapValue
    private int maxTeasers;

    /** The logical operator. */
    @ValueMapValue
    private String logicalOperator;

    /** The enable black gradient. */
    @ValueMapValue
    private boolean enableBlackGradient;

    /** The link label. */
    @ValueMapValue
    private String linkLabel;

    /** The anchor id. */
    @ValueMapValue
    private String anchorId;

    /** The anchor title. */
    @ValueMapValue
    private String anchorTitle;

    /** The pw theme. */
    @ValueMapValue
    private String pwTheme;
    
    /** The semi automatic list. */
    @Inject
    @Via("resource")
    private List<SemiAutomaticModel> semiAutomaticList;

    /** The story list. */
    private List<StoriesBean> storyList = new ArrayList<>();

    /** The aggregator service. */
    @OSGiService
    private AggregatorService aggregatorService;

    /** The dynamic media service. */
    @OSGiService
    private DynamicMediaService dynamicMediaService;

    /** The dynamic image model. */
    @Inject
    @Self
    private DynamicImageModel dynamicImageModel;

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        resource = request.getResource();
        if (StringUtils.isNotBlank(contentType)) {
            switch (contentType) {
                case PWConstants.AUTOMATIC:
                    generateListAutomaticWay();
                    break;
                case PWConstants.SEMI_AUTOMATIC:
                    generateListSemiAutomatically();
                    break;
                case PWConstants.MANUAL:
                    getStoriesManualList();
                    break;
                default:
                    LOGGER.info("Not a valid content-type");
            }
        }
    }

    /**
     * Gets the dynamic media params.
     *
     * @param imagePath
     *            the image path
     * @return the dynamic media params
     */
    private DeviceTypeBean getDynamicMediaParams(String imagePath) {
        String dynamicMediaUrl = dynamicMediaService.getImageServiceUrl();
        if (Objects.nonNull(dynamicMediaUrl) && Objects.nonNull(imagePath)) {
            dynamicMediaUrl = StringUtils.removeEndIgnoreCase(dynamicMediaUrl, PWConstants.SLASH) + PWConstants.SLASH
                    + GlobalUtil.getScene7FileName(request.getResourceResolver(), imagePath);
        }
        final DeviceTypeBean deviceTypeBean = new DeviceTypeBean();
        if (StringUtils.isNotBlank(dynamicMediaUrl)) {
            deviceTypeBean.setDesktop(dynamicImageModel.createDynamicMediaUrl(PWConstants.DESKTOP, dynamicMediaUrl));
            deviceTypeBean.setDesktopLarge(
                    dynamicImageModel.createDynamicMediaUrl(PWConstants.DESKTOP_LARGE, dynamicMediaUrl));
            deviceTypeBean.setMobilePortrait(
                    dynamicImageModel.createDynamicMediaUrl(PWConstants.MOBILEPORTRAIT, dynamicMediaUrl));
            deviceTypeBean.setMobileLandscape(
                    dynamicImageModel.createDynamicMediaUrl(PWConstants.MOBILELANDSCAPE, dynamicMediaUrl));
        }
        return deviceTypeBean;
    }

    /**
     * Generate list automatic way.
     */
    private void generateListAutomaticWay() {
        if (tags != null && tags.length > 0) {
            List<AggregatorModel> aggregatorList = aggregatorService.getAggregatorList(resource, tags, maxTeasers,
                    logicalOperator);
            if (!aggregatorList.isEmpty()) {
                setTabListfromAggregator(aggregatorList);
            }
        }
    }

    /**
     * Gets the stories manual list.
     *
     * @return the stories manual list
     */
    public void getStoriesManualList() {
        final ResourceResolver resourceResolver = request.getResourceResolver();
        final Resource manualStoriesResource = resourceResolver
                .getResource(resource.getPath().concat(PWConstants.SLASH).concat(PWConstants.MANUAL_LIST));
        if (Objects.nonNull(manualStoriesResource)) {
            final Iterator<Resource> rootIterator = manualStoriesResource.listChildren();
            while (rootIterator.hasNext()) {
                final Resource storyResource = rootIterator.next();
                StoriesBean stories = new StoriesBean();
                final List<DeviceTypeBean> dynamicMediaParameters = new ArrayList<>();              
                stories.setHeading(storyResource.getValueMap().get(PWConstants.PARAM_HEADING, StringUtils.EMPTY));
                stories.setFileReference(
                        storyResource.getValueMap().get(PWConstants.FILE_REFERENCE, StringUtils.EMPTY));
                if (Objects.nonNull(storyResource.getValueMap().get(PWConstants.FILE_REFERENCE))) {
                    dynamicMediaParameters.add(getDynamicMediaParams(
                            storyResource.getValueMap().get(PWConstants.FILE_REFERENCE).toString()));
                    stories.setDynamicMediaUrlList(dynamicMediaParameters);
                }
                stories.setAlt(storyResource.getValueMap().get(PWConstants.PARAM_ALT, StringUtils.EMPTY));
                stories.setLinkPath(LinkUtils
                        .sanitizeLink(storyResource.getValueMap().get(PWConstants.PARAM_LINK, StringUtils.EMPTY), request));
                storyList.add(stories);
            }
        }
    }

    /**
     * Generate list semi automatically.
     */
    private void generateListSemiAutomatically() {
        List<AggregatorModel> aggregatorList = aggregatorService.getAggregatorList(resource, semiAutomaticList);
        if (!aggregatorList.isEmpty()) {
            setTabListfromAggregator(aggregatorList);
        }
    }

    /**
     * Sets the tab list from aggregator.
     *
     * @param aggregatorList
     *            the new tab list from aggregator
     */
    private void setTabListfromAggregator(List<AggregatorModel> aggregatorList) {
        for (AggregatorModel aggregator : aggregatorList) {
            StoriesBean stories = new StoriesBean();
            final List<DeviceTypeBean> dynamicMediaParameters = new ArrayList<>();
            stories.setHeading(aggregator.getTitle());
            stories.setFileReference(aggregator.getImagePath());
            if (Objects.nonNull(aggregator.getImagePath())) {
                dynamicMediaParameters.add(getDynamicMediaParams(aggregator.getImagePath()));
                stories.setDynamicMediaUrlList(dynamicMediaParameters);
            }
            stories.setAlt(aggregator.getAltText());
            stories.setLinkPath(LinkUtils.sanitizeLink(aggregator.getLinkPath(), request));
            storyList.add(stories);
        }
    }
    
    /**
     * Checks if is enable black gradient.
     *
     * @return true, if is enable black gradient
     */
    public boolean isEnableBlackGradient() {
        return enableBlackGradient;
    }

    /**
     * Gets the link label.
     *
     * @return the link label
     */
    public String getLinkLabel() {
        return linkLabel;
    }

    /**
     * Gets the anchor id.
     *
     * @return the anchor id
     */
    public String getAnchorId() {
        return anchorId;
    }

    /**
     * Gets the anchor title.
     *
     * @return the anchor title
     */
    public String getAnchorTitle() {
        return anchorTitle;
    }

    /**
     * Gets the pw theme.
     *
     * @return the pw theme
     */
    public String getPwTheme() {
        return pwTheme;
    }

    /**
     * Gets the story list.
     *
     * @return the story list
     */
    public List<StoriesBean> getStoryList() {
        return new ArrayList<>(storyList);
    }
}