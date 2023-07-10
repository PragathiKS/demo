package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.models.multifield.ManualModel;
import com.tetrapak.publicweb.core.models.multifield.SemiAutomaticModel;
import com.tetrapak.publicweb.core.services.AggregatorService;
import com.tetrapak.publicweb.core.utils.LinkUtils;

/**
 * The Class TeaserModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TeaserModel {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(TeaserModel.class);

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The resource. */
    private Resource resource;

    /** The heading. */
    @ValueMapValue
    private String heading;

    /** The description. */
    @ValueMapValue
    private String description;

    /** The number Of Columns. */
    @ValueMapValue
    private String numberOfColumns;

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

    /** The link path. */
    @ValueMapValue
    private String linkPath;

    /** The anchor id. */
    @ValueMapValue
    private String anchorId;

    /** The anchor title. */
    @ValueMapValue
    private String anchorTitle;

    /** The pw theme. */
    @ValueMapValue
    private String pwTheme;

    /** The enable carousel. */
    @ValueMapValue
    private String enableCarousel;

    /** The enable display date. */
    @ValueMapValue
    private Boolean displayDate;   

    /** The manual list. */
    @Inject
    @Via("resource")
    private List<ManualModel> manualList;

    /** The semi automatic list. */
    @Inject
    @Via("resource")
    private List<SemiAutomaticModel> semiAutomaticList;

    /** The teaser list. */
    private List<ManualModel> teaserList = new ArrayList<>();

    /** The aggregator service. */
    @OSGiService
    private AggregatorService aggregatorService;

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
                    getManualList();
                    break;
                default:
                    LOGGER.info("Not a valid content-type");
            }
        }
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
	 * Gets the manual list.
	 *
	 * @return the manual list
	 */
	public void getManualList() {
		if (manualList != null && !manualList.isEmpty()) {
			manualList.stream()
					.forEach(model -> model.setLinkPath(LinkUtils.sanitizeLink(model.getLinkPath(), request)));
			teaserList.addAll(manualList);
		}
	}

	/**
	 * Generate list semi automatically.
	 */
	private void generateListSemiAutomatically() {
		if (semiAutomaticList != null && !semiAutomaticList.isEmpty()) {
			List<AggregatorModel> aggregatorList = aggregatorService.getAggregatorList(resource, semiAutomaticList);
			if (!aggregatorList.isEmpty()) {
				setTabListfromAggregator(aggregatorList);
			}
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
            ManualModel teaser = new ManualModel();
            teaser.setTitle(aggregator.getTitle());
            teaser.setDescription(aggregator.getDescription());
            teaser.setFileReference(aggregator.getImagePath());
            teaser.setAlt(aggregator.getAltText());
            teaser.setLinkText(aggregator.getLinkText());
            teaser.setLinkPath(LinkUtils.sanitizeLink(aggregator.getLinkPath(), request));
            teaser.setPwButtonTheme(aggregator.getPwButtonTheme());            
            teaser.setArticleDate(aggregator.getArticleDate());                                        
            teaserList.add(teaser);
        }
    }

    /**
     * Gets the heading.
     *
     * @return the heading
     */
    public String getHeading() {
        return heading;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the number of columns.
     *
     * @return the number of columns.
     */
    public String getNumberOfColumns() {
        return numberOfColumns;
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
     * Gets the link path.
     *
     * @return the link path
     */
    public String getLinkPath() {
        return LinkUtils.sanitizeLink(linkPath, request);
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
     * Gets the enable carousel.
     *
     * @return the enable carousel
     */
    public String getEnableCarousel() {
        return enableCarousel;
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
     * Gets the teaser list.
     *
     * @return the teaser list
     */
    public List<ManualModel> getTeaserList() {
        return new ArrayList<>(teaserList);
    }
}
