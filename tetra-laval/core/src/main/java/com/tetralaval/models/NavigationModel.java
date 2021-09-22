package com.tetralaval.models;

import java.util.List;

public class NavigationModel {
    private String label;
    private String link;
    private List<NavigationModel> children;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<NavigationModel> getChildren() {
        return children;
    }

    public void setChildren(List<NavigationModel> children) {
        this.children = children;
    }
}
