package com.tetralaval.models;

import com.tetralaval.models.multifield.ManualModel;
import com.tetralaval.models.multifield.SemiAutomaticModel;
import com.tetralaval.services.AggregatorService;
import com.tetralaval.utils.LinkUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TeaserModel {
    private final static String SEMI_AUTOMATIC_CONTENT_TYPE = "semi-automatic";
    private final static String MANUAL_CONTENT_TYPE = "manual";

    @SlingObject
    private SlingHttpServletRequest request;

    @OSGiService
    private AggregatorService aggregatorService;

    private Resource resource;

    @ValueMapValue
    private String heading;

    @ValueMapValue
    private String contentType;

    @ValueMapValue
    private String[] tags;

    @ValueMapValue
    private int maxTeasers;

    @ValueMapValue
    private String logicalOperator;

    @ValueMapValue
    private String anchorId;

    @ValueMapValue
    private String anchorTitle;

    @ValueMapValue
    private String pwTheme;

    @ValueMapValue
    private String linkLabel;

    @ValueMapValue
    private String linkPath;

    @Inject
    @Via("resource")
    private List<ManualModel> manualList;

    @Inject
    @Via("resource")
    private List<SemiAutomaticModel> semiAutomaticList;

    private List<ManualModel> teaserList = new ArrayList<>();

    @PostConstruct
    protected void init() {
        resource = request.getResource();
        if (StringUtils.isNotBlank(contentType)) {
            switch (contentType) {
                case SEMI_AUTOMATIC_CONTENT_TYPE:
                    generateListSemiAutomatically();
                    break;
                case MANUAL_CONTENT_TYPE:
                    getManualList();
                    break;
                default:
                    generateListAutomaticWay();
                    break;
            }
        }
    }

    public String getHeading() {
        return heading;
    }

    public String getAnchorId() {
        return anchorId;
    }

    public String getAnchorTitle() {
        return anchorTitle;
    }

    public String getPwTheme() {
        return pwTheme;
    }

    public String getLinkLabel() {
        return linkLabel;
    }

    public String getLinkPath() {
        return LinkUtils.sanitizeLink(linkPath, request);
    }

    public List<ManualModel> getTeaserList() {
        for (int i = 0; i < teaserList.size(); i++) {
            teaserList.get(i).setRightbar((i + 1) != teaserList.size() &&
                    (i + 1) % (teaserList.size() > 2 ? 3 : 2) != 0);
        }
        return teaserList;
    }

    private void getManualList() {
        manualList.stream().forEach(model -> model.setLinkPath(LinkUtils.sanitizeLink(model.getLinkPath(), request)));
        teaserList.addAll(manualList);
    }

    private void generateListSemiAutomatically() {
        List<AggregatorModel> aggregatorList = aggregatorService.getAggregatorList(resource, semiAutomaticList);
        if (!aggregatorList.isEmpty()) {
            setTabListfromAggregator(aggregatorList);
        }
    }

    private void generateListAutomaticWay() {
        if (tags != null && tags.length > 0) {
            List<AggregatorModel> aggregatorList = aggregatorService.getAggregatorList(resource, tags, maxTeasers,
                    logicalOperator);
            if (!aggregatorList.isEmpty()) {
                setTabListfromAggregator(aggregatorList);
            }
        }
    }

    private void setTabListfromAggregator(List<AggregatorModel> aggregatorList) {
        for (AggregatorModel aggregator : aggregatorList) {
            ManualModel teaser = new ManualModel();
            teaser.setTitle(aggregator.getTitle());
            teaser.setDescription(aggregator.getDescription());
            teaser.setImagePath(aggregator.getImagePath());
            teaser.setImageAltText(aggregator.getAltText());
            teaser.setLinkText(aggregator.getLinkText());
            teaser.setLinkPath(LinkUtils.sanitizeLink(aggregator.getLinkPath(), request));
            teaser.setPwButtonTheme(aggregator.getPwButtonTheme());
            teaserList.add(teaser);
        }
    }
}