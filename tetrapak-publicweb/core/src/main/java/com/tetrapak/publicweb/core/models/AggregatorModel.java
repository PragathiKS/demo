package com.tetrapak.publicweb.core.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class AggregatorModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AggregatorModel {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AggregatorModel.class);

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
        articleDate = formatDate(articleDate);
    }

/**
     * Format date.
     *
     * @param dateString
     *            the date string
     * @return the string
     */
     private String formatDate(String dateString) {
        if (dateString != null && dateString.length() > 0 && dateString.contains("T")) {
            final String parsedDate = dateString.substring(0, dateString.indexOf('T'));
            if (parsedDate.length() > 0) {
                try {
                    return parsedDate;
                } catch (final Exception e) {
                    LOGGER.error("Error occurred while parsing date: {} ", e.getMessage(), e);
                }
            }
        }
        return StringUtils.EMPTY;
    }

}
