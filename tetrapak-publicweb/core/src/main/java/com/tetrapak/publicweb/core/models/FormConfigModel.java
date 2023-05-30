package com.tetrapak.publicweb.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * The Class FormModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FormConfigModel {

	/** The heading. */
	@ValueMapValue
	private String heading;

	/** The image. */
	@ValueMapValue
	private String image;

	/** The alt. */
	@ValueMapValue
	private String alt;

	/** The description text. */
	@ValueMapValue
	private String descriptionText;

	/** The thankyou heading. */
	@ValueMapValue
	private String thankyouHeading;

	/** The thankyou image. */
	@ValueMapValue
	private String thankyouImage;

	/** The thankyou image alt text. */
	@ValueMapValue
	private String thankyouImageAltText;

	/** The thankyou description text. */
	@ValueMapValue
	private String thankyouDescriptionText;

	/**
	 * Gets the tags.
	 *
	 * @return the tags
	 */
	@ValueMapValue
	private String[] tags;

	public String[] getPardotSystemConfigTags() {
		return tags;
	}

	/**
	 * Gets the heading.
	 *
	 * @return the heading
	 */
	public String getHeading() {
		return heading;
	}

	/**
	 * Gets the image.
	 *
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * Gets the alt.
	 *
	 * @return the alt
	 */
	public String getAlt() {
		return alt;
	}

	/**
	 * Gets the description text.
	 *
	 * @return the description text
	 */
	public String getDescriptionText() {
		return descriptionText;
	}

	/**
	 * Gets the thankyou heading.
	 *
	 * @return the thankyou heading
	 */
	public String getThankyouHeading() {
		return thankyouHeading;
	}

	/**
	 * Gets the thankyou image.
	 *
	 * @return the thankyou image
	 */
	public String getThankyouImage() {
		return thankyouImage;
	}

	/**
	 * Gets the thankyou image alt text.
	 *
	 * @return the thankyou image alt text
	 */
	public String getThankyouImageAltText() {
		return thankyouImageAltText;
	}

	/**
	 * Gets the thankyou description text.
	 *
	 * @return the thankyou description text
	 */
	public String getThankyouDescriptionText() {
		return thankyouDescriptionText;
	}

}