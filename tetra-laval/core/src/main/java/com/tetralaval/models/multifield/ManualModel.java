package com.tetralaval.models.multifield;

import com.tetralaval.constants.PWConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * The Class ManualModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ManualModel {

    /** The resource. */
    @Self
    private Resource resource;

    /** The title. */
    @ValueMapValue
    private String title;

    /** The description. */
    @ValueMapValue
    private String description;

    /** The link text. */
    @ValueMapValue
    private String linkText;

    /** The link path. */
    @ValueMapValue
    private String linkPath;

    /** The file reference. */
    @ValueMapValue
    private String imagePath;

    /** The alt. */
    @ValueMapValue
    private String imageAltText;

    /** The pw button theme. */
    @ValueMapValue
    private String pwButtonTheme;
    
    /** The asset name. */
    private String assetName;

    private boolean rightbar;
    
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
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description
     *            the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the link text.
     *
     * @return the link text
     */
    public String getLinkText() {
        return linkText;
    }

    /**
     * Sets the link text.
     *
     * @param linkText
     *            the new link text
     */
    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    /**
     * Gets the link path.
     *
     * @return the link path
     */
    public String getLinkPath() {
        return linkPath;
    }

    /**
     * Sets the link path.
     *
     * @param linkPath
     *            the new link path
     */
    public void setLinkPath(String linkPath) {
        this.linkPath = linkPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageAltText() {
        return imageAltText;
    }

    public void setImageAltText(String imageAltText) {
        this.imageAltText = imageAltText;
    }

    /**
     * Gets the pw button theme.
     *
     * @return the pw button theme
     */
    public String getPwButtonTheme() {
        return pwButtonTheme;
    }

    /**
     * Sets the pw button theme.
     *
     * @param pwButtonTheme
     *            the new pw button theme
     */
    public void setPwButtonTheme(String pwButtonTheme) {
        this.pwButtonTheme = pwButtonTheme;
    }
    
    /**
     * Gets the asset name.
     *
     * @return the asset name
     */
    public String getAssetName() {
        assetName = StringUtils.EMPTY;
        if (StringUtils.isNotEmpty(linkPath)) {
            assetName = StringUtils.substringAfterLast(linkPath, PWConstants.SLASH);
        }
        return assetName;
    }

    public boolean isRightbar() {
        return rightbar;
    }

    public void setRightbar(boolean rightbar) {
        this.rightbar = rightbar;
    }
}
