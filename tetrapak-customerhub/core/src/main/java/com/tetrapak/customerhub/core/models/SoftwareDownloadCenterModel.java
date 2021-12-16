package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.utils.LinkUtil;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.inject.Inject;

/**
 * Model class for Software download center component
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SoftwareDownloadCenterModel {

	/**
     * The resource.
     */
    @Self
    private Resource resource;

    @Inject
    private String linkPath;

    /**
     * Process the path to fetch the valid link path.
     *
     * @return processed path
     */
    public String getLinkPath() {
        return LinkUtil.getValidLink(resource, linkPath);
    }
}
