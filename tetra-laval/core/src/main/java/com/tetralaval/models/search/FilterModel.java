package com.tetralaval.models.search;

/**
 * Filter model
 */
public class FilterModel {
    private String key;
    private String label;

    /**
     * FilterModel constructor
     */
    public FilterModel() {}

    /**
     * FilterModel constructor
     * @param key
     * @param label
     */
    public FilterModel(String key, String label) {
        this.key = key;
        this.label = label;
    }

    /**
     * key getter
     * @return key
     */
    public String getKey() {
        return key;
    }

    /**
     * key setter
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * label getter
     * @return label
     */
    public String getLabel() {
        return label;
    }

    /**
     * label setter
     * @param label
     */
    public void setLabel(String label) {
        this.label = label;
    }
}
