package com.tetrapak.supplierportal.core.models;

import com.google.gson.annotations.SerializedName;

public class SupportRequestFormBean {
	
	@SerializedName("purposeOfContact")
	private String purposeOfContact;
	
	@SerializedName("howHelp")
	private String howHelp;
	
	@SerializedName("name")
	private String name;
	
	@SerializedName("emailAddress")
	private String emailAddress;
	
	@SerializedName("companyLegalName")
	private String companyLegalName;
	
	@SerializedName("country")
	private String country;
	
	@SerializedName("city")
	private String city;
	
	@SerializedName("ownPhoneNumber")
	private String ownPhoneNumber;
	
	@SerializedName("aribaNetworkId")
	private String aribaNetworkId;
	
	@SerializedName("aribaAccountAdminEmail")
	private String aribaAccountAdminEmail;
	
	@SerializedName("tpContactEmail")
	private String tpContactEmail;
	
	public String getPurposeOfContact() {
		return purposeOfContact;
	}

	public String getHowHelp() {
		return howHelp;
	}

	public String getName() {
		return name;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getCompanyLegalName() {
		return companyLegalName;
	}

	public String getCountry() {
		return country;
	}

	public String getCity() {
		return city;
	}

	public String getOwnPhoneNumber() {
		return ownPhoneNumber;
	}

	public String getAribaNetworkId() {
		return aribaNetworkId;
	}

	public String getAribaAccountAdminEmail() {
		return aribaAccountAdminEmail;
	}

	public String getTpContactEmail() {
		return tpContactEmail;
	}

}
