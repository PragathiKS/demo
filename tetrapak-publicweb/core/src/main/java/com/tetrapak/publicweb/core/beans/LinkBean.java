package com.tetrapak.publicweb.core.beans;

import com.tetrapak.publicweb.core.models.SectionMenuModel;

/**
 * The Class LinkBean.
 */
public class LinkBean {

	/** The link text. */
	private String linkText;

	/** The link path. */
	private String linkPath;

    /** The section menu model. */
    private SectionMenuModel navigationConfigurationModel;

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
	public void setLinkText(String linkText) {
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
	public void setLinkPath(String linkPath) {
		this.linkPath = linkPath;
	}

    /**
     * Gets the navigation configuration model.
     *
     * @return the navigation configuration model
     */
    public SectionMenuModel getNavigationConfigurationModel() {
        return navigationConfigurationModel;
    }

    /**
     * Sets the navigation configuration model.
     *
     * @param navigationConfigurationModel the new navigation configuration model
     */
    public void setNavigationConfigurationModel(SectionMenuModel navigationConfigurationModel) {
        this.navigationConfigurationModel = navigationConfigurationModel;
    }
}
