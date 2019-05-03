package com.tetrapak.publicweb.core.models;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;
import com.day.cq.wcm.api.Page;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContactFooterFormModel {
	
	private static final Logger log = LoggerFactory.getLogger(ContactFooterFormModel.class);

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
	
	private String stepLabel;
	
    private Page currentPage;
	
	private Boolean hideContactFooterForm;

    @PostConstruct
    protected void init() {
    	currentPage = resource.getParent().getParent().adaptTo(Page.class);
    	log.info("Current Page path : {}", currentPage.getPath());
        if (currentPage != null) {
            Resource jcrContentResource = currentPage.getContentResource();
            BasePageModel basePageModel = jcrContentResource.adaptTo(BasePageModel.class);
            hideContactFooterForm = basePageModel.getPageContent().getHideContactFooterForm();
            log.info("Value of hideContactFooterForm : {}", hideContactFooterForm);
        }
        
        InheritanceValueMap inheritanceValueMap1 = new HierarchyNodeInheritanceValueMap(resource);
        titleI18n = inheritanceValueMap1.getInherited("titleI18n", String.class);
        description = inheritanceValueMap1.getInherited("description", String.class);
        image = inheritanceValueMap1.getInherited("image", String.class);
        imageAltI18n = inheritanceValueMap1.getInherited("imageAltI18n", String.class);
        helpText = inheritanceValueMap1.getInherited("helpText", String.class);
        privacyPolicyText = inheritanceValueMap1.getInherited("privacyPolicyText", String.class);
        thankYouHeadline = inheritanceValueMap1.getInherited("thankYouHeadline", String.class);
        thankYouMessage = inheritanceValueMap1.getInherited("thankYouMessage", String.class);
        linkTextI18n = inheritanceValueMap1.getInherited("linkTextI18n", String.class);
        linkPath = inheritanceValueMap1.getInherited("linkPath", String.class);
        firstNameLabel = inheritanceValueMap1.getInherited("firstNameLabel", String.class);
        lastNameLabel = inheritanceValueMap1.getInherited("lastNameLabel", String.class);
        phoneNumberLabel = inheritanceValueMap1.getInherited("phoneNumberLabel", String.class);
        emailAddressLabel = inheritanceValueMap1.getInherited("emailAddressLabel", String.class);
        positionLabel = inheritanceValueMap1.getInherited("positionLabel", String.class);
        companyLabel = inheritanceValueMap1.getInherited("companyLabel", String.class);
        contactUsLabel = inheritanceValueMap1.getInherited("contactUsLabel", String.class);
        messageLabel = inheritanceValueMap1.getInherited("messageLabel", String.class);
        previousButtonLabel = inheritanceValueMap1.getInherited("previousButtonLabel", String.class);
        nextButtonLabel = inheritanceValueMap1.getInherited("nextButtonLabel", String.class);
        submitButtonLabel = inheritanceValueMap1.getInherited("submitButtonLabel", String.class);
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

}
