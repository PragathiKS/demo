package com.tetrapak.publicweb.core.models;

import com.adobe.acs.commons.genericlists.GenericList;
import com.adobe.acs.commons.genericlists.GenericList.Item;
import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;
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
	
	private Boolean hideContactFooterForm = false;
	
	private List<Item> countryList;

    @PostConstruct
    protected void init() {
    	log.info("Inside init() method." );
    	ResourceResolver resourceResolver = resource.getResourceResolver();
    	PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
    	Page currentPage = pageManager.getContainingPage(resource);      	
        if (currentPage != null) {
        	log.info("Current Page path : {}", currentPage.getPath());
        	ValueMap currentPageProps = currentPage.getContentResource().getValueMap();
        	hideContactFooterForm = currentPageProps.get("hideContactFooterForm", Boolean.class);
        }
        
        InheritanceValueMap inheritanceValueMap = new HierarchyNodeInheritanceValueMap(resource);
        titleI18n = inheritanceValueMap.getInherited("titleI18n", String.class);
        description = inheritanceValueMap.getInherited("description", String.class);
        image = inheritanceValueMap.getInherited("image", String.class);
        imageAltI18n = inheritanceValueMap.getInherited("imageAltI18n", String.class);
        helpText = inheritanceValueMap.getInherited("helpText", String.class);
        privacyPolicyText = inheritanceValueMap.getInherited("privacyPolicyText", String.class);
        thankYouHeadline = inheritanceValueMap.getInherited("thankYouHeadline", String.class);
        thankYouMessage = inheritanceValueMap.getInherited("thankYouMessage", String.class);
        linkTextI18n = inheritanceValueMap.getInherited("linkTextI18n", String.class);
        linkPath = inheritanceValueMap.getInherited("linkPath", String.class);
        firstNameLabel = inheritanceValueMap.getInherited("firstNameLabel", String.class);
        lastNameLabel = inheritanceValueMap.getInherited("lastNameLabel", String.class);
        phoneNumberLabel = inheritanceValueMap.getInherited("phoneNumberLabel", String.class);
        emailAddressLabel = inheritanceValueMap.getInherited("emailAddressLabel", String.class);
        positionLabel = inheritanceValueMap.getInherited("positionLabel", String.class);
        companyLabel = inheritanceValueMap.getInherited("companyLabel", String.class);
        contactUsLabel = inheritanceValueMap.getInherited("contactUsLabel", String.class);
        messageLabel = inheritanceValueMap.getInherited("messageLabel", String.class);
        previousButtonLabel = inheritanceValueMap.getInherited("previousButtonLabel", String.class);
        nextButtonLabel = inheritanceValueMap.getInherited("nextButtonLabel", String.class);
        submitButtonLabel = inheritanceValueMap.getInherited("submitButtonLabel", String.class);
        
        getCountriesList(pageManager);
    }

	private void getCountriesList(PageManager pageManager) {				
	    Page listPage = pageManager.getPage("/etc/acs-commons/lists/countries");
	    GenericList list = listPage.adaptTo(GenericList.class);
	    countryList = list.getItems();
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
