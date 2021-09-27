package com.tetralaval.models.multifield;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

/**
 * Contact details model
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContactDetailsModel {
    private String anchorId;
    private String anchorTitle;
    private String text;

    /**
     * anchorId getter
     * @return anchorId
     */
    public String getAnchorId() {
        return anchorId;
    }

    /**
     * anchorId setter
     * @param anchorId
     */
    public void setAnchorId(String anchorId) {
        this.anchorId = anchorId;
    }

    /**
     * anchorTitle getter
     * @return anchorTitle
     */
    public String getAnchorTitle() {
        return anchorTitle;
    }

    /**
     * anchorTitle setter
     * @param anchorTitle
     */
    public void setAnchorTitle(String anchorTitle) {
        this.anchorTitle = anchorTitle;
    }

    /**
     * text getter
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * text setter
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }
}
