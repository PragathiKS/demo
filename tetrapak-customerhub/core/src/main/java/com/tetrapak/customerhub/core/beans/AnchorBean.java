package com.tetrapak.customerhub.core.beans;

/**
 * The Class AnchorBean.
 */
public final class AnchorBean {

    /** The anchor id. */
    private String anchorId;

    /** The anchor title. */
    private String anchorTitle;

    /**
     * Create immutable AnchorBean
     * @param anchorId
     * @param anchorTitle
     */
    public AnchorBean(String anchorId, String anchorTitle) {
        this.anchorId = anchorId;
        this.anchorTitle = anchorTitle;
    }

    /**
     * Gets the anchor id.
     *
     * @return the anchor id
     */
    public String getAnchorId() {
        return anchorId;
    }

    /**
     * Gets the anchor title.
     *
     * @return the anchor title
     */
    public String getAnchorTitle() {
        return anchorTitle;
    }
}
