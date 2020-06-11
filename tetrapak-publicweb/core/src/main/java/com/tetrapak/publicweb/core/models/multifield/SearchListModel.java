package com.tetrapak.publicweb.core.models.multifield;

import javax.inject.Named;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * The Class ThemeModel.
 */
/**
 * @author sankumar28
 *
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SearchListModel {

    /** The theme label. */
    @ValueMapValue
    @Named(value = "themeLabel")
    private String label;

    /** The tag. */
    @ValueMapValue
    @Named(value = "themeKey")
    private String key;
    
    /** The tag. */
    @ValueMapValue
    @Named(value = "tag")
    private String tag;

    /**
     * Gets the theme label.
     *
     * @return the theme label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the theme label.
     *
     * @param label the new label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Gets the tag.
     *
     * @return the tag
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the tag.
     *
     * @param key the new key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Gets the tag.
     *
     * @return the tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * Sets the tag.
     *
     * @param tag the new tag
     */
    public void setTag(String tag) {
        this.tag = tag;
    }
    
    

}
