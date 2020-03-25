package com.tetrapak.publicweb.core.models;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.tetrapak.publicweb.core.utils.LinkUtils;

/**
 * The Class HeaderConfigurationModel.
 */
@Model(adaptables = Resource.class)
public class HeaderConfigurationModel {

	/** The resource. */
	@Self
	private Resource resource;

	/** The logo link. */
	@Inject
	private String logoLink;

	/** The logo link target. */
	@Inject
	private String logoLinkTarget;

	/** The logo alt. */
	@Inject
	private String logoAlt;

	/** The login link. */
	@Inject
	private String loginLink;

	/** The login label. */
	@Inject
	private String loginLabel;

	/** The contact link. */
	@Inject
	private String contactLink;

	/** The contact text. */
	@Inject
	private String contactText;

	/** The contact link target. */
	@Inject
	private String contactLinkTarget;

	/**
	 * Gets the logo link.
	 *
	 * @return the logo link
	 */
	public String getLogoLink() {
		return LinkUtils.sanitizeLink(logoLink);
	}

	/**
	 * Gets the logo link target.
	 *
	 * @return the logo link target
	 */
	public String getLogoLinkTarget() {
		return logoLinkTarget;
	}

	/**
	 * Gets the logo alt.
	 *
	 * @return the logo alt
	 */
	public String getLogoAlt() {
		return logoAlt;
	}

	/**
	 * Gets the login link.
	 *
	 * @return the login link
	 */
	public String getLoginLink() {
		return LinkUtils.sanitizeLink(loginLink);
	}

	/**
	 * Gets the login label.
	 *
	 * @return the login label
	 */
	public String getLoginLabel() {
		return loginLabel;
	}

	/**
	 * Gets the contact link.
	 *
	 * @return the contact link
	 */
	public String getContactLink() {
		return LinkUtils.sanitizeLink(contactLink);
	}

	/**
	 * Gets the contact text.
	 *
	 * @return the contact text
	 */
	public String getContactText() {
		return contactText;
	}

	/**
	 * Gets the contact link target.
	 *
	 * @return the contact link target
	 */
	public String getContactLinkTarget() {
		return contactLinkTarget;
	}

}
