package com.tetrapak.publicweb.core.models.multifield;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.tetrapak.publicweb.core.beans.DeviceTypeBean;

/**
 * The Class StoriesManualModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class StoriesManualModel {

    /** The resource. */
    @Self
    private Resource resource;

    /** The title. */
    @ValueMapValue
    private String title;

    /** The file reference. */
    @ValueMapValue
    private String fileReference;

    /** The alt. */
    @ValueMapValue
    private String alt;

    /** The dynamic media url list. */
    private List<DeviceTypeBean> dynamicMediaUrlList;

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title
     *            the new title
     */
    public void setTitle(String title) {
        this.title = title;
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
     * Sets the file reference.
     *
     * @param fileReference
     *            the new file reference
     */
    public void setFileReference(String fileReference) {
        this.fileReference = fileReference;
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
     * Sets the alt.
     *
     * @param alt
     *            the new alt
     */
    public void setAlt(String alt) {
        this.alt = alt;
    }

    /**
     * Gets the dynamic media url list.
     *
     * @return the dynamic media url list
     */
    public List<DeviceTypeBean> getDynamicMediaUrlList() {
        return dynamicMediaUrlList;
    }

    /**
     * Sets the dynamic media url list.
     *
     * @param dynamicMediaUrlList
     *            the new dynamic media url list
     */
    public void setDynamicMediaUrlList(List<DeviceTypeBean> dynamicMediaUrlList) {
        this.dynamicMediaUrlList = dynamicMediaUrlList;
    }

}
