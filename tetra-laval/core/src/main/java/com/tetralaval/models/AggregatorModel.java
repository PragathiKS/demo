package com.tetralaval.models;

import com.tetralaval.utils.GlobalUtil;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Named;

/**
 * The Class AggregatorModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AggregatorModel {    

    /** The title. */
    @Named(value = "jcr:title")
    @ValueMapValue
    private String title;

    /** The tags. */
    @ValueMapValue
    @Named(value = "cq:tags")
    private String[] tags;

    /** The description. */
    @ValueMapValue
    @Named(value = "jcr:description")
    private String description;

    /** The imagePath. */
    @ValueMapValue
    private String imagePath;

    /** The altText. */
    @ValueMapValue
    private String altText;

    /** The linkText. */
    @ValueMapValue
    private String linkText;

    /** The linkPath. */
    @ValueMapValue
    private String linkPath;

    /** The pwButtonTheme. */
    @ValueMapValue
    private String pwButtonTheme;

    /** The articleDate. */
    @ValueMapValue
    private String articleDate;

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the tags.
     *
     * @return the tags
     */
    public String[] getTags() {
        return tags;
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
     * Gets the image path.
     *
     * @return the image path
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Gets the alt text.
     *
     * @return the alt text
     */
    public String getAltText() {
        return altText;
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
     * Gets the link path.
     *
     * @return the link path
     */
    public String getLinkPath() {
        return linkPath;
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
     * Gets the article date.
     *
     * @return the article date
     */
    public String getArticleDate() {
        return articleDate;
    }

    @PostConstruct
    protected void init() {
        articleDate = GlobalUtil.formatDate(articleDate);
    }

}
