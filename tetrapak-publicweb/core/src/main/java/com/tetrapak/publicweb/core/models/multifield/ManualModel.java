package com.tetrapak.publicweb.core.models.multifield;

import com.tetrapak.publicweb.core.utils.GlobalUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

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
    private String fileReference;

    /** The alt. */
    @ValueMapValue
    private String alt;

    /** The pw button theme. */
    @ValueMapValue
    private String pwButtonTheme;
    
    /** The asset name. */
    private String assetName;

     /** The article date. */
     @ValueMapValue
    private String articleDate;
    
    /** The Constant FORWARD_SLASH. */
    private static final String FORWARD_SLASH = "/";

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
     * Gets the article date.
     *
     * @return the article date
     */
    public String getArticleDate() {
        return articleDate;
    }    

     /**
     * Sets the articleDate.
     *
     * @param articleDate
     *            the articleDate
     */
    public void setArticleDate(String articleDate) {
        this.articleDate = articleDate;
    }
    
    /**
     * Gets the asset name.
     *
     * @return the asset name
     */
    public String getAssetName() {
        assetName = StringUtils.EMPTY;
        if (StringUtils.isNotEmpty(linkPath)) {
            assetName = StringUtils.substringAfterLast(linkPath, FORWARD_SLASH);
        }
        return assetName;
    }

    @PostConstruct
    protected void init() {
        articleDate = GlobalUtil.formatDate(articleDate);
    }

}
