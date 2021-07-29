package com.tetralaval.models.multifield;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContactDetailsModel {
    private String anchorId;
    private String anchorTitle;
    private String text;

    public String getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(String anchorId) {
        this.anchorId = anchorId;
    }

    public String getAnchorTitle() {
        return anchorTitle;
    }

    public void setAnchorTitle(String anchorTitle) {
        this.anchorTitle = anchorTitle;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
