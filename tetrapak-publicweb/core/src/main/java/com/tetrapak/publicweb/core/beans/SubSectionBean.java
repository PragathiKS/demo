package com.tetrapak.publicweb.core.beans;

/**
 * The Class SubSectionBean.
 */
public class SubSectionBean {
    
    /** The link text. */
    private String linkText;
    
    /** The link path. */
    private String linkPath;
    
    /** The external. */
    private boolean external;

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
     * @param linkText the new link text
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
     * @param linkPath the new link path
     */
    public void setLinkPath(final String linkPath) {
        this.linkPath = linkPath;
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
     * @param external the new external
     */
    public void setExternal(final boolean external) {
        this.external = external;
    }
}
