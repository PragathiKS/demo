package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
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
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.models.multifield.StoriesManualModel;
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

    /** The component resource path. */
    @ValueMapValue
    private String componentResourcePath;

    /** The manual list. */
    @Inject
    @Via("resource")
    private List<StoriesManualModel> storiesManualList;

    /** The semi automatic list. */
    @Inject
    @Via("resource")
    private List<SemiAutomaticModel> semiAutomaticList;

    /** The teaser list. */
    private List<StoriesManualModel> storyList = new ArrayList<>();

    /** The aggregator service. */
    @OSGiService
    private AggregatorService aggregatorService;

    /**
     * The dynamic media service.
     */
    @OSGiService
    private DynamicMediaService dynamicMediaService;

    /** The dynamic image model. */
    @Inject
    @Self
    private DynamicImageModel dynamicImageModel;

    /**
     * The Constant PATH_SEPARATOR.
     */
    private static final String PATH_SEPARATOR = "/";

    /**
     * The Constant DESKTOP.
     */
    private static final String DESKTOP = "desktop";

    /**
     * The Constant DESKTOP.
     */
    private static final String DESKTOP_LARGE = "desktopL";

    /**
     * The Constant MOBILELANDSCAPE.
     */
    private static final String MOBILELANDSCAPE = "mobileL";

    /**
     * The Constant MOBILEPORTRAIT.
     */
    private static final String MOBILEPORTRAIT = "mobileP";

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        resource = request.getResource();
        componentResourcePath = resource.getPath();
        if (StringUtils.isNotBlank(contentType)) {
            switch (contentType) {
                case "automatic":
                    generateListAutomaticWay();
                    break;
                case "semi-automatic":
                    generateListSemiAutomatically();
                    break;
                case "manual":
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
        String finalPath = StringUtils.EMPTY;
        String dynamicMediaUrl = dynamicMediaService.getImageServiceUrl();
        Resource imageRes = request.getResourceResolver().getResource(imagePath + "/jcr:content/metadata");
        if (Objects.nonNull(imageRes)) {
            final ValueMap vMap = imageRes.getValueMap();
            String fileFormat = vMap.get("dam:Fileformat", StringUtils.EMPTY);
            String scene7Type = vMap.get("dam:scene7Type", StringUtils.EMPTY);
            if (fileFormat.equalsIgnoreCase("GIF") && !scene7Type.equalsIgnoreCase("Image")) {
                dynamicMediaUrl = dynamicMediaService.getVideoServiceUrl();
            }
        }
        if (imagePath != null) {
            finalPath = PWConstants.SLASH + GlobalUtil.getScene7FileName(request.getResourceResolver(), imagePath);
        }

        if (null != dynamicMediaUrl) {
            dynamicMediaUrl = StringUtils.removeEndIgnoreCase(dynamicMediaUrl, PATH_SEPARATOR) + finalPath;
        }
        final DeviceTypeBean deviceTypeBean = new DeviceTypeBean();
        if (StringUtils.isNotBlank(dynamicMediaUrl)) {
            deviceTypeBean.setDesktop(dynamicImageModel.createDynamicMediaUrl(DESKTOP, dynamicMediaUrl));
            deviceTypeBean.setDesktopLarge(dynamicImageModel.createDynamicMediaUrl(DESKTOP_LARGE, dynamicMediaUrl));
            deviceTypeBean.setMobilePortrait(dynamicImageModel.createDynamicMediaUrl(MOBILEPORTRAIT, dynamicMediaUrl));
            deviceTypeBean
                    .setMobileLandscape(dynamicImageModel.createDynamicMediaUrl(MOBILELANDSCAPE, dynamicMediaUrl));
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
        for (StoriesManualModel storiesManualModel : storiesManualList) {
            StoriesManualModel stories = new StoriesManualModel();
            final List<DeviceTypeBean> dynamicMediaParameters = new ArrayList<>();
            stories.setTitle(storiesManualModel.getTitle());
            stories.setFileReference(storiesManualModel.getFileReference());
            if (Objects.nonNull(storiesManualModel.getFileReference())) {
                dynamicMediaParameters.add(getDynamicMediaParams(storiesManualModel.getFileReference()));
                stories.setDynamicMediaUrlList(dynamicMediaParameters);
            }
            stories.setAlt(storiesManualModel.getAlt());
            storiesManualList.add(stories);
            stories.setAlt(storiesManualModel.getAlt());
            stories.setLinkPath(LinkUtils.sanitizeLink(storiesManualModel.getLinkPath(), request));
            storiesManualList.add(stories);
        }
        storyList.addAll(storiesManualList);
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
            StoriesManualModel stories = new StoriesManualModel();
            final List<DeviceTypeBean> dynamicMediaParameters = new ArrayList<>();
            stories.setTitle(aggregator.getTitle());
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
     * Gets the component resource path.
     *
     * @return the component resource path
     */
    public String getComponentResourcePath() {
        return componentResourcePath;
    }

    /**
     * Gets the teaser list.
     *
     * @return the teaser list
     */
    public List<StoriesManualModel> getStoryList() {
        return new ArrayList<>(storyList);
    }
}