package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.publicweb.core.models.multifield.ManualModel;
import com.tetrapak.publicweb.core.models.multifield.SemiAutomaticModel;
import com.tetrapak.publicweb.core.services.AggregatorService;

/**
 * The Class TeaserModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TeaserModel {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(TeaserModel.class);

    /** The resource. */
    @Self
    private Resource resource;

    /** The heading. */
    @ValueMapValue
    private String heading;

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

    /** The anchor id. */
    @ValueMapValue
    private String anchorId;

    /** The anchor title. */
    @ValueMapValue
    private String anchorTitle;

    /** The pw theme. */
    @ValueMapValue
    private String pwTheme;

    /** The manual list. */
    @Inject
    private List<ManualModel> manualList;

    /** The semi automatic list. */
    @Inject
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
        if (StringUtils.isNotBlank(contentType)) {
            switch (contentType) {
                case "automatic":
                    generateListAutomaticWay();
                    break;
                case "semi-automatic":
                    generateListSemiAutomatically();
                    break;
                case "manual":
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
        teaserList.addAll(manualList);
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
            ManualModel teaser = new ManualModel();
            teaser.setTitle(aggregator.getTitle());
            teaser.setDescription(aggregator.getDescription());
            teaser.setFileReference(aggregator.getImagePath());
            teaser.setAlt(aggregator.getAltText());
            teaser.setLinkText(aggregator.getLinkText());
            teaser.setLinkPath(aggregator.getLinkPath());
            teaser.setPwButtonTheme(aggregator.getPwButtonTheme());
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
     * Gets the teaser list.
     *
     * @return the teaser list
     */
    public List<ManualModel> getTeaserList() {
        return new ArrayList<>(teaserList);
    }
}
