package com.tetrapak.customerhub.core.models;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.tetrapak.customerhub.core.beans.ImageBean;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import com.tetrapak.customerhub.core.utils.LinkUtil;

/**
 * This is a model class for Full Bleed Image component.
 * 
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FullBleedImageModel {

    /**
     * The resource.
     */
    @Self
    private Resource resource;

    /** The file reference. */
    @ValueMapValue
    private String fileReference;

    /** The alt. */
    @ValueMapValue
    private String alt;

    /** The link URL. */
    @ValueMapValue
    private String linkURL;

    /** The bean for Dynamic Media Image **/
    private ImageBean image;

    /**
     * Inits the Model.
     */
    @PostConstruct
    protected void init() {
	linkURL = LinkUtil.getValidLink(resource, linkURL);
	image = GlobalUtil.getImageBean(resource);
    }

    /**
     * Gets the file reference.
     *
     * @return the file reference
     */
    public String getFileReference() {
	return fileReference;
    }

    /**
     * Gets the alt.
     *
     * @return the alt
     */
    public String getAlt() {
	return alt;
    }

    /**
     * Gets the link URL.
     *
     * @return the link URL
     */
    public String getLinkURL() {
	return linkURL;
    }

    /**
     * Gets the Image for Dynamic Media
     * 
     * @return the Image
     */
    public ImageBean getImage() {
	return image;
    }
}
