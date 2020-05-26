package com.tetrapak.publicweb.core.beans;

/**
 * The Class ExternalTemplateBean.
 */
public class ExternalTemplateBean {

    /** The is external. */
    private boolean isExternal;

    /** The external url. */
    private String externalUrl;

    /**
     * Checks if is external.
     *
     * @return true, if is external
     */
    public boolean isExternal() {
        return isExternal;
    }

    /**
     * Sets the external.
     *
     * @param isExternal
     *            the new external
     */
    public void setExternal(final boolean isExternal) {
        this.isExternal = isExternal;
    }

    /**
     * Gets the external url.
     *
     * @return the external url
     */
    public String getExternalUrl() {
        return externalUrl;
    }

    /**
     * Sets the external url.
     *
     * @param externalUrl
     *            the new external url
     */
    public void setExternalUrl(final String externalUrl) {
        this.externalUrl = externalUrl;
    }
}
