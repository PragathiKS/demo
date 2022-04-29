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

    /** The heading. */
    @ValueMapValue
    private String description;

    /** The link label. */
    @ValueMapValue
    private String linkLabel;

    /** The link path. */
    @ValueMapValue
    private String linkPath;

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
        if (manualList != null && !manualList.isEmpty()) {
            manualList.stream()
                    .forEach(model -> model.setLinkPath(LinkUtils.sanitizeLink(model.getLinkPath(), request)));
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
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
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
     * Gets the teaser list.
     *
     * @return the teaser list
     */
    public List<ManualModel> getTeaserList() {
        return new ArrayList<>(teaserList);
    }
}
