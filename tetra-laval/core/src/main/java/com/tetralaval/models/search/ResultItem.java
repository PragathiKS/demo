package com.tetralaval.models.search;

import java.util.Calendar;

/**
 * Result item
 */
public class ResultItem {
    /** type */
    private String type;
    /** title */
    private String title;
    /** description */
    private String description;
    /** path */
    private String path;
    /** size */
    private String size;
    /** sizeType */
    private String sizeType;
    /** date */
    private String date;
    /** assetType */
    private String assetType;
    /** assetExtension */
    private String assetExtension;
    /** assetThumbnail */
    private String assetThumbnail;
    /** sortDate */
    private Calendar sortDate;

    /**
     * type getter
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * type setter
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * title getter
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * title setter
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * description getter
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * description setter
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * path getter
     * @return path
     */
    public String getPath() {
        return path;
    }

    /**
     * path setter
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * size getter
     * @return size
     */
    public String getSize() {
        return size;
    }

    /**
     * size setter
     * @param size
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * sizeType getter
     * @return sizeType
     */
    public String getSizeType() {
        return sizeType;
    }

    /**
     * sizeType setter
     * @param sizeType
     */
    public void setSizeType(String sizeType) {
        this.sizeType = sizeType;
    }

    /**
     * date getter
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * date setter
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * assetType getter
     * @return assetType
     */
    public String getAssetType() {
        return assetType;
    }

    /**
     * assetType setter
     * @param assetType
     */
    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    /**
     * assetExtension getter
     * @return assetExtension
     */
    public String getAssetExtension() {
        return assetExtension;
    }

    /**
     * assetExtension setter
     * @param assetExtension
     */
    public void setAssetExtension(String assetExtension) {
        this.assetExtension = assetExtension;
    }

    /**
     * assetThumbnail getter
     * @return assetThumbnail
     */
    public String getAssetThumbnail() {
        return assetThumbnail;
    }

    /**
     * assetThumbnail setter
     * @param assetThumbnail
     */
    public void setAssetThumbnail(String assetThumbnail) {
        this.assetThumbnail = assetThumbnail;
    }

    /**
     * sortDate getter
     * @return sortDate
     */
    public Calendar getSortDate() {
        return sortDate;
    }

    /**
     * sortDate setter
     * @param sortDate
     */
    public void setSortDate(Calendar sortDate) {
        this.sortDate = sortDate;
    }
}
