package com.tetrapak.publicweb.core.beans;

/**
 * The Class SubSectionBean.
 */
public class SubSectionBean {

    /** The link text. */
    private String linkText;

    /** The link path. */
    private String linkPath;

    /** The highlighted. */
    private boolean highlighted;

    /** The external. */
    private boolean external;

    /** The mobile overview label. */
    private String mobileOverviewLabel;

    /**
     * Gets the link text.
     *
     * @return the link text
     */
    public String getLinkText() {
        return linkText;
    }

    /**
     * Sets the link text.
     *
     * @param linkText
     *            the new link text
     */
    public void setLinkText(final String linkText) {
        this.linkText = linkText;
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
     * Sets the link path.
     *
     * @param linkPath
     *            the new link path
     */
    public void setLinkPath(final String linkPath) {
        this.linkPath = linkPath;
    }

    /**
     * Checks if is highlighted.
     *
     * @return true, if is highlighted
     */
    public boolean isHighlighted() {
        return highlighted;
    }

    /**
     * Sets the highlighted.
     *
     * @param highlighted
     *            the new highlighted
     */
    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    /**
     * Checks if is external.
     *
     * @return true, if is external
     */
    public boolean isExternal() {
        return external;
    }

    /**
     * Sets the external.
     *
     * @param external
     *            the new external
     */
    public void setExternal(final boolean external) {
        this.external = external;
    }

    /**
     * Gets the mobile overview label.
     *
     * @return the mobile overview label
     */
    public String getMobileOverviewLabel() {
        return mobileOverviewLabel;
    }

    /**
     * Sets the mobile overview label.
     *
     * @param mobileOverviewLabel
     *            the new mobile overview label
     */
    public void setMobileOverviewLabel(String mobileOverviewLabel) {
        this.mobileOverviewLabel = mobileOverviewLabel;
    }
}
