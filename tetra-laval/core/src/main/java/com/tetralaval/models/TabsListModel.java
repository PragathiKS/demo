package com.tetralaval.models;

import java.util.ArrayList;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetralaval.constants.TLConstants;
import com.tetralaval.models.multifield.SemiAutomaticModel;
import com.tetralaval.models.multifield.TabModel;
import com.tetralaval.services.AggregatorService;
import com.tetralaval.utils.LinkUtils;

/**
 * Model class for Tab list component.
 *
 * @author Sandip Kumar
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TabsListModel {

    /** The request */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The resource */
    private Resource resource;

    /** The heading. */
    @ValueMapValue
    private String heading;

    /** The content Type. */
    @ValueMapValue
    private String contentType;

    /** The read More Text. */
    @ValueMapValue
    private String readMoreText;

    /** The read More Path. */
    @ValueMapValue
    private String readMorePath;

    /** The tags. */
    @ValueMapValue
    private String[] tags;

    /** The max teasers. */
    @Default(intValues = 9)
    @ValueMapValue
    private int maxTabs;

    /** The logical operator. */
    @ValueMapValue
    private String logicalOperator;

    /** The pw theme. */
    @ValueMapValue
    private String pwTheme;

    /** The pw display. */
    @ValueMapValue
    private String pwDisplay;

    /** The anchor id. */
    @ValueMapValue
    private String anchorId;

    /** The anchor title. */
    @ValueMapValue
    private String anchorTitle;

    /** The enable display date. */
    @ValueMapValue
    private Boolean displayDate;

    /** The tab List Manual. */
    @Inject
    @Named(value = "tabsManual")
    @Via("resource")
    private List<TabModel> tabListManual = new ArrayList<>();

    /** The tab List Semi Auto. */
    @Inject
    @Named(value = "tabsSemi")
    @Via("resource")
    private List<SemiAutomaticModel> pagePaths = new ArrayList<>();

    /** The aggregator Service. */
    @OSGiService
    private AggregatorService aggregatorService;

    /** The tab List. */
    private List<TabModel> tabs = new ArrayList<>();

    private static final String TAB_LAYOUT_IMAGE = "imageText";

    private static final Logger LOGGER = LoggerFactory.getLogger(TabsListModel.class);

    /**
     * Post construct method to get the tab list
     * saved in CRX for each of the tab.
     */
    @PostConstruct
    protected void init() {
        resource = request.getResource();
        if(StringUtils.isNotBlank(contentType)) {
            switch (contentType) {
                case TLConstants.AUTOMATIC:
                    generateListAutomaticWay();
                    break;
                case TLConstants.SEMI_AUTOMATIC:
                    generateListSemiAutomatically();
                    break;
                case TLConstants.MANUAL:
                    generateListManually();
                    break;
                default:
                    LOGGER.info("Not a valid content-type");
            }
        }
    }

    /**
     * @param resolver
     * @param pageManager
     */
    private void generateListAutomaticWay() {
        if (tags != null && tags.length > 0) {
            List<AggregatorModel> aggregatorList = aggregatorService.getAggregatorList(resource, tags, maxTabs,logicalOperator);
            if (!aggregatorList.isEmpty()) {
                setTabListfromAggregator(aggregatorList);
            }
        }
    }

    /**
     * set list from paths
     *
     * @param pageManager
     */
    private void generateListSemiAutomatically() {
        if (pagePaths != null && !pagePaths.isEmpty()) {
            List<AggregatorModel> aggregatorList = aggregatorService.getAggregatorList(resource, pagePaths);
            if (!aggregatorList.isEmpty()) {
                setTabListfromAggregator(aggregatorList);
            }
        }
    }

    /**
     * @param aggregatorList
     */
    private void setTabListfromAggregator(List<AggregatorModel> aggregatorList) {
        for (AggregatorModel aggregator : aggregatorList) {
            TabModel tabBean = new TabModel();
            tabBean.setTitle(aggregator.getTitle());
            tabBean.setDescription(aggregator.getDescription());
            tabBean.setFileReference(aggregator.getImagePath());
            tabBean.setAlt(aggregator.getAltText());
            tabBean.setLinkText(aggregator.getLinkText());
            tabBean.setLinkURL(LinkUtils.sanitizeLink(aggregator.getLinkPath(), request));
            tabBean.setPwButtonTheme(aggregator.getPwButtonTheme());
            tabBean.setTabType(TAB_LAYOUT_IMAGE);
            tabBean.setArticleDate(aggregator.getArticleDate());




            tabs.add(tabBean);
        }
    }

    /**
     * set list from manual authoring
     */
    private void generateListManually() {
        tabListManual.stream().forEach(model -> model
                .setLinkURL(LinkUtils.sanitizeLink(model.getLinkURL(), request)));
        tabs.addAll(tabListManual);
    }

    /**
     * @return the heading
     */
    public String getHeading() {
        return heading;
    }

    /**
     * @return the readMoreText
     */
    public String getReadMoreText() {
        return readMoreText;
    }

    /**
     * @return the readMorePath
     */
    public String getReadMorePath() {
        return LinkUtils.sanitizeLink(readMorePath, request);
    }

    /**
     * @return the pwTheme
     */
    public String getPwTheme() {
        return pwTheme;
    }

    /**
     * @return the pwDisplay
     */
    public String getPwDisplay() {
        return pwDisplay;
    }

    /**
     * @return the tabs
     */
    public List<TabModel> getTabs() {
        return tabs;
    }

    /**
     * @return the anchorId
     */
    public String getAnchorId() {
        return anchorId;
    }

    /**
     * @return the anchorTitle
     */
    public String getAnchorTitle() {
        return anchorTitle;
    }

    /**
     * Gets the display date.
     *
     * @return the display date
     */
    public Boolean getDisplayDate() {
        return displayDate;
    }
    /**

     /**
     * Gets the asset name.
     *
     * @return the asset name
     */
    public String getAssetName() {
        String assetName = StringUtils.EMPTY;
        if (StringUtils.isNotEmpty(readMorePath)) {
            assetName = getSubstringAfterLast(readMorePath);
        }
        return assetName;
    }

    /**
     * Gets the substring after last.
     *
     * @param path the path
     * @return the substring after last
     */
    private String getSubstringAfterLast(final String path) {
        return StringUtils.substringAfterLast(path, "/");
    }

}
