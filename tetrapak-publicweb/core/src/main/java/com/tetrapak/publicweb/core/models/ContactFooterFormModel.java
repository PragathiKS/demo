package com.tetrapak.publicweb.core.models;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContactFooterFormModel {

	@Inject
	private String titleI18n;

	@Inject
	private String description;

	@Inject
	private String image;

	@Inject
	private String imageAltI18n;

	@Inject
	private String helpText;
	
	@Inject
	private String privacyPolicyText;
	
	@Inject
	private String thankYouHeadline;
	
	@Inject
	private String thankYouMessage;
	
	@Inject
	private String linkTextI18n;
	
	@Inject
	private String linkPath;

	@Inject
	private String firstNameLabel;

	@Inject
	private String lastNameLabel;
	
	@Inject
	private String phoneNumberLabel;

	@Inject
	private String emailAddressLabel;

	@Inject
	private String positionLabel;

	@Inject
	private String companyLabel;	

	@Inject
	private String contactUsLabel;	

	@Inject
	private String messageLabel;	

	@Inject
	private String previousButtonLabel;

	@Inject
	private String nextButtonLabel;
	
	@Inject
	private String submitButtonLabel;
	
	@Inject
	private String stepLabel;

	public String getTitleI18n() {
		return titleI18n;
	}

	public String getDescription() {
		return description;
	}

	public String getImage() {
		return image;
	}

	public String getImageAltI18n() {
		return imageAltI18n;
	}

	public String getHelpText() {
		return helpText;
	}

	public String getPrivacyPolicyText() {
		return privacyPolicyText;
	}

	public String getThankYouHeadline() {
		return thankYouHeadline;
	}

	public String getThankYouMessage() {
		return thankYouMessage;
	}

	public String getLinkTextI18n() {
		return linkTextI18n;
	}

	public String getLinkPath() {
		return linkPath;
	}

	public String getFirstNameLabel() {
		return firstNameLabel;
	}

	public String getLastNameLabel() {
		return lastNameLabel;
	}

	public String getPhoneNumberLabel() {
		return phoneNumberLabel;
	}

	public String getEmailAddressLabel() {
		return emailAddressLabel;
	}

	public String getPositionLabel() {
		return positionLabel;
	}

	public String getCompanyLabel() {
		return companyLabel;
	}

	public String getContactUsLabel() {
		return contactUsLabel;
	}

	public String getMessageLabel() {
		return messageLabel;
	}

	public String getPreviousButtonLabel() {
		return previousButtonLabel;
	}

	public String getNextButtonLabel() {
		return nextButtonLabel;
	}

	public String getSubmitButtonLabel() {
		return submitButtonLabel;
	}

	public String getStepLabel() {
		return stepLabel;
	}	

}
