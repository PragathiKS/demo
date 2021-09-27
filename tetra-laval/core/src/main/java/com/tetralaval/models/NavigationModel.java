package com.tetralaval.models;

import java.util.List;

/**
 * NavigationModel
 */
public class NavigationModel {
    /** label */
    private String label;
    /** link */
    private String link;
    /** children */
    private List<NavigationModel> children;

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

    /**
     * link getter
     * @return link
     */
    public String getLink() {
        return link;
    }

    /**
     * link setter
     * @param link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * children getter
     * @return children
     */
    public List<NavigationModel> getChildren() {
        return children;
    }

    /**
     * children setter
     * @param children
     */
    public void setChildren(List<NavigationModel> children) {
        this.children = children;
    }
}
