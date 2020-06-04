package com.tetrapak.publicweb.core.models.multifield;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * The Class ThemeModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ThemeModel {

    /** The theme label. */
    @ValueMapValue
    private String themeLabel;

    /** The tag. */
    @ValueMapValue
    private String tag;

    /**
     * Gets the theme label.
     *
     * @return the theme label
     */
    public String getThemeLabel() {
        return themeLabel;
    }

    /**
     * Sets the theme label.
     *
     * @param themeLabel
     *            the new theme label
     */
    public void setThemeLabel(String themeLabel) {
        this.themeLabel = themeLabel;
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
     * @param tag
     *            the new tag
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

}
