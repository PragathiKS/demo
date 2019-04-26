package com.tetrapak.publicweb.core.models;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SoftConversionFormModel {

	@Inject
	private String firstNameLabel;

	@Inject
	private String lastNameLabel;

	@Inject
	private String emailAddressLabel;

	@Inject
	private String companyLabel;
	
	@Inject
	private String positionLabel;

	@Inject
	private String previousButtonLabel;

	@Inject
	private String nextButtonLabel;
	
	@Inject
	private String submitButtonLabel;

	public String getFirstNameLabel() {
		return firstNameLabel!=null ? firstNameLabel : "First Name";
	}

	public String getLastNameLabel() {
		return lastNameLabel!=null ? lastNameLabel : "Last Name";
	}

	public String getEmailAddressLabel() {
		return emailAddressLabel!=null ? emailAddressLabel : "Email Address";
	}

	public String getCompanyLabel() {
		return companyLabel!=null ? companyLabel : "Company";
	}
	
	public String getPositionLabel() {
		return positionLabel!=null ? positionLabel : "Position";
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

}
