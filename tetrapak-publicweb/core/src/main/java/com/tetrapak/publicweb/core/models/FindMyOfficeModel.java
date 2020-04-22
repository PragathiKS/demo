package com.tetrapak.publicweb.core.models;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

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

    /** The servlet path. */
    private String servletPath;

    /**
     * Inits the.
     */
    @PostConstruct
    public void init() {
        servletPath = resource.getPath() + ".json";
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

}
