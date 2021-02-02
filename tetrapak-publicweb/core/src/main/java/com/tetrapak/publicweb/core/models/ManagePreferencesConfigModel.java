package com.tetrapak.publicweb.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * @author ojaswarn
 * The Class ManagePreferencesConfigModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ManagePreferencesConfigModel {
	
	/** The headline. */
	@ValueMapValue
	@Default(values="Manage your Preference")
	private String headline;
	
	/** The intro text. */
	@ValueMapValue
	@Default(values="Do you wish to receive other kinds of updates from us?\nPlease update your preferences below.")
	private String introText;
	
	/** The privacy policy. */
	@ValueMapValue
	private String privacyPolicy;
	
	/** The unsubscribe intro text. */
	@ValueMapValue
	private String unsubscribeIntroText;
	
	/**
	 * Gets the headline.
	 *
	 * @return the headline
	 */
	public String getHeadline() {
		return headline;
	}

	/**
	 * Sets the headline.
	 *
	 * @param headline the new headline
	 */
	public void setHeadline(String headline) {
		this.headline = headline;
	}

	/**
	 * Gets the intro text.
	 *
	 * @return the intro text
	 */
	public String getIntroText() {
		return introText;
	}

	/**
	 * Sets the intro text.
	 *
	 * @param introText the new intro text
	 */
	public void setIntroText(String introText) {
		this.introText = introText;
	}

	/**
	 * Gets the privacy policy.
	 *
	 * @return the privacy policy
	 */
	public String getPrivacyPolicy() {
		return privacyPolicy;
	}

	/**
	 * Sets the privacy policy.
	 *
	 * @param privacyPolicy the new privacy policy
	 */
	public void setPrivacyPolicy(String privacyPolicy) {
		this.privacyPolicy = privacyPolicy;
	}

	/**
	 * Gets the unsubscribe intro text.
	 *
	 * @return the unsubscribe intro text
	 */
	public String getUnsubscribeIntroText() {
		return unsubscribeIntroText;
	}

	/**
	 * Sets the unsubscribe intro text.
	 *
	 * @param unsubscribeIntroText the new unsubscribe intro text
	 */
	public void setUnsubscribeIntroText(String unsubscribeIntroText) {
		this.unsubscribeIntroText = unsubscribeIntroText;
	}
}
