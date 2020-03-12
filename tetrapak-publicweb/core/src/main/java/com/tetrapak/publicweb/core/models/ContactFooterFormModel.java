package com.tetrapak.publicweb.core.models;

import com.adobe.acs.commons.genericlists.GenericList;
import com.adobe.acs.commons.genericlists.GenericList.Item;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContactFooterFormModel {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContactFooterFormModel.class);

	@Self
	private Resource resource;

	private String titleI18n;

	private String description;

	private String image;

	private String imageAltI18n;

	private String helpText;

	private String privacyPolicyText;

	private String thankYouHeadline;

	private String thankYouMessage;

	private String linkTextI18n;

	private String linkPath;

	private String firstNameLabel;

	private String lastNameLabel;

	private String phoneNumberLabel;

	private String emailAddressLabel;

	private String positionLabel;

	private String companyLabel;

	private String contactUsLabel;

	private String messageLabel;

	private String previousButtonLabel;

	private String nextButtonLabel;

	private String submitButtonLabel;

	private Boolean hideContactFooterForm = false;

	private List<Item> countryList;

	@PostConstruct
	protected void init() {
		LOGGER.info("Inside init() method.");
		ResourceResolver resourceResolver = resource.getResourceResolver();
		PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
		Page currentPage = pageManager.getContainingPage(resource);
		if (currentPage != null) {
			LOGGER.info("Current Page path : {}", currentPage.getPath());
			ValueMap currentPageProps = currentPage.getContentResource().getValueMap();
			hideContactFooterForm = currentPageProps.get("hideContactFooterForm", Boolean.class);
		}

		ValueMap valueMap = resource.getValueMap();
		titleI18n = valueMap.get("titleI18n", String.class);
		description = valueMap.get("description", String.class);
		image = valueMap.get("image", String.class);
		imageAltI18n = valueMap.get("imageAltI18n", String.class);
		helpText = valueMap.get("helpText", String.class);
		privacyPolicyText = valueMap.get("privacyPolicyText", String.class);
		thankYouHeadline = valueMap.get("thankYouHeadline", String.class);
		thankYouMessage = valueMap.get("thankYouMessage", String.class);
		linkTextI18n = valueMap.get("linkTextI18n", String.class);
		linkPath = valueMap.get("linkPath", String.class);
		firstNameLabel = valueMap.get("firstNameLabel", String.class);
		lastNameLabel = valueMap.get("lastNameLabel", String.class);
		phoneNumberLabel = valueMap.get("phoneNumberLabel", String.class);
		emailAddressLabel = valueMap.get("emailAddressLabel", String.class);
		positionLabel = valueMap.get("positionLabel", String.class);
		companyLabel = valueMap.get("companyLabel", String.class);
		contactUsLabel = valueMap.get("contactUsLabel", String.class);
		messageLabel = valueMap.get("messageLabel", String.class);
		previousButtonLabel = valueMap.get("previousButtonLabel", String.class);
		nextButtonLabel = valueMap.get("nextButtonLabel", String.class);
		submitButtonLabel = valueMap.get("submitButtonLabel", String.class);

		getCountriesList(pageManager);
	}

	private void getCountriesList(PageManager pageManager) {
		Page listPage = pageManager.getPage("/etc/acs-commons/lists/countries");
		GenericList list = listPage.adaptTo(GenericList.class);
		if (Objects.nonNull(list)) {
			countryList = list.getItems();
		} else {
			LOGGER.debug("Generic list is empty");
		}
	}

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

	public Boolean getHideContactFooterForm() {
		return hideContactFooterForm;
	}

	public List<Item> getCountryList() {
		return countryList;
	}

}
