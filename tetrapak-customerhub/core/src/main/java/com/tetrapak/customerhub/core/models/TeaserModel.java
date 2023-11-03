package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.models.multifield.ManualModel;
import com.tetrapak.customerhub.core.utils.LinkUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class TeaserModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TeaserModel {

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The heading. */
    @ValueMapValue
    private String heading;

    /** The link. */
    @Inject
    @Via("resource")
    private LinkModel viewAllLink;

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
    @Via("resource")
    private List<ManualModel> manualList;

    /** The teaser list. */
    private List<ManualModel> teaserList = new ArrayList<>();

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        if(viewAllLink != null) {
            viewAllLink.setLinkUrl(LinkUtils.sanitizeLink(viewAllLink.getLinkUrl(), request));
        }
        if (manualList != null && !manualList.isEmpty()) {
            manualList.stream()
                    .filter(model -> model.getLink() != null)
                    .forEach(
                      model -> model.getLink().setLinkUrl(LinkUtils.sanitizeLink(model.getLink().getLinkUrl(), request)));
            teaserList.addAll(manualList);
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
     * Gets the view all link .
     *
     * @return the vew all link
     */
    public LinkModel getViewAllLink() {
        return viewAllLink;
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