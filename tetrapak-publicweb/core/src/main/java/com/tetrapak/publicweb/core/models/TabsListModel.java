package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.publicweb.core.models.multifield.SemiAutomaticModel;
import com.tetrapak.publicweb.core.models.multifield.TabModel;
import com.tetrapak.publicweb.core.services.AggregatorService;
import com.tetrapak.publicweb.core.utils.LinkUtils;

/**
 * Model class for Tab list component.
 *
 * @author Sandip Kumar
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TabsListModel {

    /** The resource */
    @Self
    private Resource resource;

    /** The heading. */
    @ValueMapValue
    private String heading;

    /** The content Type. */
    @ValueMapValue
    private String contentType;

    /** The read More Text I18n. */
    @ValueMapValue
    private String readMoreTextI18n;

    /** The read More Path. */
    @ValueMapValue
    private String readMorePath;

    /** The read More Target. */
    @ValueMapValue
    private String readMoreTarget;

    /** The tags. */
    @ValueMapValue
    private String[] tags;

    /** The max teasers. */
    @Default(intValues = 9)
    @ValueMapValue
    private int maxTabs;

    /** The pw theme. */
    @ValueMapValue
    private String pwTheme;

    /** The pw padding. */
    @ValueMapValue
    private String pwPadding;

    /** The pw display. */
    @ValueMapValue
    private String pwDisplay;

    /** The anchor id. */
    @ValueMapValue
    private String anchorId;

    /** The anchor title. */
    @ValueMapValue
    private String anchorTitle;

    @Inject
    @Named(value = "tabsManual")
    private List<TabModel> tabListManual = new ArrayList<>();

    @Inject
    @Named(value = "tabsSemi")
    private List<SemiAutomaticModel> pagePaths = new ArrayList<>();
    
    @OSGiService
    private AggregatorService aggregatorService;

    private List<TabModel> tabs = new ArrayList<>();
    
    private static final String TAB_LAYOUT_IMAGE = "imageText";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TabsListModel.class);

    /**
     * Post construct method to get the tab content from the multifield property
     * saved in CRX for each of the tab.
     */
    @PostConstruct
    protected void init() {
	switch (contentType) {
	case "automatic":
	    generateListAutomaticWay();
	    break;
	case "semi-automatic":
	    generateListSemiAutomatically();
	    break;
	case "manual":
	    generateListManually();
	    break;
	default:
	    LOGGER.info("Not a valid content-type");
	}
    }

    /**
     * @param resolver
     * @param pageManager
     */
    private void generateListAutomaticWay() {
	List<AggregatorModel> aggregatorList = aggregatorService.getAggregatorList(resource, tags, maxTabs);
	if (!aggregatorList.isEmpty()) {
	    setTabListfromAggregator(aggregatorList);
	}
    }

    /**
     * set list from paths
     * 
     * @param pageManager
     */
    private void generateListSemiAutomatically() {
	List<AggregatorModel> aggregatorList = aggregatorService.getAggregatorList(resource, pagePaths, maxTabs);
	if (!aggregatorList.isEmpty()) {
	    setTabListfromAggregator(aggregatorList);
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
	    tabBean.setLinkTexti18n(aggregator.getLinkText());
	    tabBean.setLinkURL(aggregator.getLinkPath());
	    tabBean.setTargetBlank(aggregator.getLinkTarget());
	    tabBean.setPwLinkTheme(aggregator.getPwLinkTheme());
	    tabBean.setPwButtonTheme(aggregator.getPwButtonTheme());
	    tabBean.setTabType(TAB_LAYOUT_IMAGE);
	    tabs.add(tabBean);
	}
    }

    /**
     * set list from manual authoring
     */
    private void generateListManually() {
	tabs.addAll(tabListManual);
    }

    public String getHeading() {
	return heading;
    }

    public String getReadMoreTextI18n() {
	return readMoreTextI18n;
    }

    public String getReadMorePath() {
	return LinkUtils.sanitizeLink(readMorePath);
    }

    public String getReadMoreTarget() {
	return readMoreTarget;
    }

    public String getPwTheme() {
	return pwTheme;
    }

    public String getPwPadding() {
	return pwPadding;
    }

    public String getPwDisplay() {
	return pwDisplay;
    }

    public List<TabModel> getTabs() {
	return tabs;
    }

    public String getAnchorId() {
	return anchorId;
    }

    public String getAnchorTitle() {
	return anchorTitle;
    }

}
