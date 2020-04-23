package com.tetrapak.publicweb.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.tetrapak.publicweb.core.services.impl.FindMyOfficeServiceImpl;

/**
 * The Class FindMyOfficeModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FindMyOfficeModel {

    /** The resource. */
    @Self
    private Resource resource;

    /** The heading. */
    @ValueMapValue
    private String heading;

    /** The anchor id. */
    @ValueMapValue
    private String anchorId;

    /** The anchor title. */
    @ValueMapValue
    private String anchorTitle;

    /** The servlet path. */
    private String servletPath;

    /** The google api key. */
    private String googleApiKey;

    /** The find my office service impl. */
    @Inject
    private FindMyOfficeServiceImpl findMyOfficeServiceImpl;

    /**
     * Inits the.
     */
    @PostConstruct
    public void init() {
        servletPath = resource.getPath() + ".json";
        googleApiKey = findMyOfficeServiceImpl.getGoogleApiKey();
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
     * Gets the servlet path.
     *
     * @return the servlet path
     */
    public String getServletPath() {
        return servletPath;
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
     * Gets the google api key.
     *
     * @return the google api key
     */
    public String getGoogleApiKey() {
        return googleApiKey;
    }

}
