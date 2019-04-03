package com.tetrapak.customerhub.core.beans;

/**
 * The RecommendedForYouCardBean Class
 */
public class RecommendedForYouCardBean extends GetStartedBean {

    private String linkUrl;

    private String linkTextI18n;

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getLinkTextI18n() {
        return linkTextI18n;
    }

    public void setLinkTextI18n(String linkTextI18n) {
        this.linkTextI18n = linkTextI18n;
    }
}
