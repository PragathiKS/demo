package com.tetrapak.supplierportal.core.models;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;

import com.adobe.acs.commons.models.injectors.annotation.I18N;
import com.tetrapak.supplierportal.core.utils.GlobalUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.google.gson.Gson;
import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;

@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SupportRequestFormModel {

	/**
	 * The site label.
	 */
	//	General Tab
	@ValueMapValue
	private String generalTitleLabel;

	@ValueMapValue
	private String generalSubtitleLabel;

	@I18N("Purpose of contract")
	private String purposeOfContactLabel;

	@I18N("Please select a purpose")
	private String purposeErrorMsgLabel;

	@I18N("How can we help you?")
	private String queryTitle;

	@I18N("Tell us, more in detail, about your query...")
	private String queryPlaceholder;

	@I18N("Describe your query")
	private String querySubtitle;

	@I18N("Please describe your query")
	private String queryErrorMsgLabel;

	@I18N("SAP Ariba - Onboarding and maintenance")
	private String onboardingMaintanance;

	@I18N("Get support with registration, "
			+ "updating supplier information or similar")
	private String onboardingMaintananceSubtitle;

	@I18N("SAP Ariba - Sourcing & Contracting")
	private String sourcingContracting;

	@I18N("Get support with sending and responding to RFI/RFQs, "
			+ "participating in auctions, using electronic signature")
	private String sourcingContractingSubtitle;

	@I18N("SAP Ariba - Catalogues")
	private String catalogues;

	@I18N("Get support with creating or maintaining catalogues")
	private String cataloguesSubtitle;

	@I18N("Other purpose")
	private String otherPurpose;

	@I18N("Get support with something that is not covered in above purposes")
	private String otherPurposeSubtitle;

	// Drag and drop tab
	@I18N("File size cannot exceed 5MB")
	private String fileErrorMsg;

	@I18N("Attach file")
	private String dragAndDropButtonLabel;

	@I18N("Remove file")
	private String dragAndDropRemoveFileLabel;

	// Personal Detail form
	@I18N("Your contact information")
	private String detailsTitle;

	@I18N("Name")
	private String nameLabel;

	@I18N("Email address")
	private String emailLabel;

	@I18N("Please enter your company legal name")
	private String companyErrorMsg;

	@I18N("Company legal name")
	private String companyLabel;

	@I18N("Country")
	private String countryLabel;

	@I18N("Please enter your country")
	private String countryErrorMsg;

	@I18N("City")
	private String cityLabel;

	@I18N("Please enter your city")
	private String cityErrorMsg;

	@I18N("Phone number")
	private String phoneLabel;

	@I18N("Please enter a valid mobile telephone number")
	private String phoneErrorMsg;

	// Other Details
	@I18N("Other information")
	private String otherTitle;

	@I18N("When possible, share a little more information. This may help our support"
			+ " team to help you faster.")
	private String otherDescriptionLabel;

	@I18N("Ariba Network ID(optional)")
	private String aribaNetworkLabel;

	@I18N("Ariba account administrator email(optional)")
	private String aribaEmailLabel;

	@I18N("Please enter a valid email")
	private String aribaEmailErrorMsg;

	@I18N("Tetra Pak contact email(optional)")
	private String tpEmailLabel;

	@I18N("Please enter a valid email")
	private String tpEmailErrorMsg;

	@I18N("Submit")
	private String SendButtonLabel;

	// Thank you form
	@I18N("Thank you!")
	private String thankyouTitleLabel;

	@I18N("We will get back to you via email as soon as possible")
	private String thankyouSubtitleLabel;

	@I18N("Go to Home")
	private String homeButtonLabel;

	@SlingObject
	private SlingHttpServletRequest request;

	public String getGeneralTitleLabel() {
		return generalTitleLabel;
	}

	public void setGeneralTitleLabel(String generalTitleLabel) {
		this.generalTitleLabel = generalTitleLabel;
	}

	public String getGeneralSubtitleLabel() {
		return generalSubtitleLabel;
	}

	public void setGeneralSubtitleLabel(String generalSubtitleLabel) {
		this.generalSubtitleLabel = generalSubtitleLabel;
	}

	public String getPurposeOfContactLabel() {
		return purposeOfContactLabel;
	}

	public void setPurposeOfContactLabel(String purposeOfContactLabel) {
		this.purposeOfContactLabel = purposeOfContactLabel;
	}

	public String getPurposeErrorMsgLabel() {
		return purposeErrorMsgLabel;
	}

	public void setPurposeErrorMsgLabel(String purposeErrorMsgLabel) {
		this.purposeErrorMsgLabel = purposeErrorMsgLabel;
	}

	public String getQueryTitle() {
		return queryTitle;
	}

	public void setQueryTitle(String queryTitle) {
		this.queryTitle = queryTitle;
	}

	public String getQuerySubtitle() {
		return querySubtitle;
	}

	public void setQuerySubtitle(String querySubtitle) {
		this.querySubtitle = querySubtitle;
	}

	public String getQueryErrorMsgLabel() {
		return queryErrorMsgLabel;
	}

	public void setQueryErrorMsgLabel(String queryErrorMsgLabel) {
		this.queryErrorMsgLabel = queryErrorMsgLabel;
	}

	public String getFileErrorMsg() {
		return fileErrorMsg;
	}

	public void setFileErrorMsg(String fileErrorMsg) {
		this.fileErrorMsg = fileErrorMsg;
	}

	public String getQueryPlaceholder() {
		return queryPlaceholder;
	}

	public void setQueryPlaceholder(String queryPlaceholder) {
		this.queryPlaceholder = queryPlaceholder;
	}

	public String getDragAndDropButtonLabel() {
		return dragAndDropButtonLabel;
	}

	public void setDragAndDropButtonLabel(String dragAndDropButtonLabel) {
		this.dragAndDropButtonLabel = dragAndDropButtonLabel;
	}

	public String getDragAndDropRemoveFileLabel() {
		return dragAndDropRemoveFileLabel;
	}

	public void setDragAndDropRemoveFileLabel(String dragAndDropRemoveFileLabel) {
		this.dragAndDropRemoveFileLabel = dragAndDropRemoveFileLabel;
	}

	public String getDetailsTitle() {
		return detailsTitle;
	}

	public void setDetailsTitle(String detailsTitle) {
		this.detailsTitle = detailsTitle;
	}

	public String getNameLabel() {
		return nameLabel;
	}

	public void setNameLabel(String nameLabel) {
		this.nameLabel = nameLabel;
	}

	public String getEmailLabel() {
		return emailLabel;
	}

	public void setEmailLabel(String emailLabel) {
		this.emailLabel = emailLabel;
	}

	public String getCompanyErrorMsg() {
		return companyErrorMsg;
	}

	public void setCompanyErrorMsg(String companyErrorMsg) {
		this.companyErrorMsg = companyErrorMsg;
	}

	public String getCompanyLabel() {
		return companyLabel;
	}

	public void setCompanyLabel(String companyLabel) {
		this.companyLabel = companyLabel;
	}

	public String getCountryLabel() {
		return countryLabel;
	}

	public void setCountryLabel(String countryLabel) {
		this.countryLabel = countryLabel;
	}

	public String getCountryErrorMsg() {
		return countryErrorMsg;
	}

	public void setCountryErrorMsg(String countryErrorMsg) {
		this.countryErrorMsg = countryErrorMsg;
	}

	public String getCityLabel() {
		return cityLabel;
	}

	public void setCityLabel(String cityLabel) {
		this.cityLabel = cityLabel;
	}

	public String getCityErrorMsg() {
		return cityErrorMsg;
	}

	public void setCityErrorMsg(String cityErrorMsg) {
		this.cityErrorMsg = cityErrorMsg;
	}

	public String getPhoneLabel() {
		return phoneLabel;
	}

	public void setPhoneLabel(String phoneLabel) {
		this.phoneLabel = phoneLabel;
	}

	public String getOtherTitle() {
		return otherTitle;
	}

	public void setOtherTitle(String otherTitle) {
		this.otherTitle = otherTitle;
	}

	public String getOtherDescriptionLabel() {
		return otherDescriptionLabel;
	}

	public void setOtherDescriptionLabel(String otherDescriptionLabel) {
		this.otherDescriptionLabel = otherDescriptionLabel;
	}

	public String getAribaNetworkLabel() {
		return aribaNetworkLabel;
	}

	public void setAribaNetworkLabel(String aribaNetworkLabel) {
		this.aribaNetworkLabel = aribaNetworkLabel;
	}

	public String getAribaEmailLabel() {
		return aribaEmailLabel;
	}

	public void setAribaEmailLabel(String aribaEmailLabel) {
		this.aribaEmailLabel = aribaEmailLabel;
	}

	public String getTpEmailLabel() {
		return tpEmailLabel;
	}

	public void setTpEmailLabel(String tpEmailLabel) {
		this.tpEmailLabel = tpEmailLabel;
	}

	public String getSendButtonLabel() {
		return SendButtonLabel;
	}

	public void setSendButtonLabel(String sendButtonLabel) {
		SendButtonLabel = sendButtonLabel;
	}

	public String getThankyouTitleLabel() {
		return thankyouTitleLabel;
	}

	public void setThankyouTitleLabel(String thankyouTitleLabel) {
		this.thankyouTitleLabel = thankyouTitleLabel;
	}

	public String getThankyouSubtitleLabel() {
		return thankyouSubtitleLabel;
	}

	public void setThankyouSubtitleLabel(String thankyouSubtitleLabel) {
		this.thankyouSubtitleLabel = thankyouSubtitleLabel;
	}

	public String getHomeButtonLabel() {
		return homeButtonLabel;
	}

	public void setHomeButtonLabel(String homeButtonLabel) {
		this.homeButtonLabel = homeButtonLabel;
	}

	public String getPhoneErrorMsg() {
		return phoneErrorMsg;
	}

	public void setPhoneErrorMsg(String phoneErrorMsg) {
		this.phoneErrorMsg = phoneErrorMsg;
	}

	public String getAribaEmailErrorMsg() {
		return aribaEmailErrorMsg;
	}

	public void setAribaEmailErrorMsg(String aribaEmailErrorMsg) {
		this.aribaEmailErrorMsg = aribaEmailErrorMsg;
	}

	public String getTpEmailErrorMsg() {
		return tpEmailErrorMsg;
	}

	public void setTpEmailErrorMsg(String tpEmailErrorMsg) {
		this.tpEmailErrorMsg = tpEmailErrorMsg;
	}

	public String getOnboardingMaintanance() {
		return onboardingMaintanance;
	}

	public void setOnboardingMaintanance(String onboardingMaintanance) {
		this.onboardingMaintanance = onboardingMaintanance;
	}

	public String getOnboardingMaintananceSubtitle() {
		return onboardingMaintananceSubtitle;
	}

	public void setOnboardingMaintananceSubtitle(String onboardingMaintananceSubtitle) {
		this.onboardingMaintananceSubtitle = onboardingMaintananceSubtitle;
	}

	public String getSourcingContracting() {
		return sourcingContracting;
	}

	public void setSourcingContracting(String sourcingContracting) {
		this.sourcingContracting = sourcingContracting;
	}

	public String getSourcingContractingSubtitle() {
		return sourcingContractingSubtitle;
	}

	public void setSourcingContractingSubtitle(String sourcingContractingSubtitle) {
		this.sourcingContractingSubtitle = sourcingContractingSubtitle;
	}

	public String getCatalogues() {
		return catalogues;
	}

	public void setCatalogues(String catalogues) {
		this.catalogues = catalogues;
	}

	public String getCataloguesSubtitle() {
		return cataloguesSubtitle;
	}

	public void setCataloguesSubtitle(String cataloguesSubtitle) {
		this.cataloguesSubtitle = cataloguesSubtitle;
	}

	public String getOtherPurpose() {
		return otherPurpose;
	}

	public void setOtherPurpose(String otherPurpose) {
		this.otherPurpose = otherPurpose;
	}

	public String getOtherPurposeSubtitle() {
		return otherPurposeSubtitle;
	}

	public void setOtherPurposeSubtitle(String otherPurposeSubtitle) {
		this.otherPurposeSubtitle = otherPurposeSubtitle;
	}

	/** The i 18 n keys. */
	private String i18nKeys;

	/**
	 * Gets the i 18 n keys.
	 *
	 * @return the i 18 n keys
	 */
	public String getI18nKeys() {
		return i18nKeys;
	}

	/**
	 * init method.
	 */
	@PostConstruct
	protected void init() {
		Map<String, Object> i18KeyMap = new HashMap<>();

		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTGENERALTITLE, getGeneralTitleLabel());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTGENERALSUBTITLE, getGeneralSubtitleLabel());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTPURPOSEOFCONTACT, getPurposeOfContactLabel());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTPURPOSEERRORMSG, getPurposeErrorMsgLabel());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTQUERYTITLE, getQueryTitle());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTQUERYSUBTITLE, getQuerySubtitle());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTQUERYERRORMSG, getQueryErrorMsgLabel());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTFILEERRORMSG, getFileErrorMsg());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTQUERYPLACEHOLDER, getQueryPlaceholder());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTDRAGANDDROPBUTTONLABEL, getDragAndDropButtonLabel());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTDRAGANDDROPREMOVEFILE, getDragAndDropRemoveFileLabel());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTDETAILSTITLE, getDetailsTitle());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTNAMELABEL, getNameLabel());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTEMAILLABEL, getEmailLabel());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTCOMPANYERRORMSG, getCompanyErrorMsg());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTCOMPANYLABEL, getCompanyLabel());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTCOUNTRYLABEL, getCountryLabel());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTCOUNTRYERRORMSG, getCountryErrorMsg());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTCITYLABEL, getCityLabel());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTCITYERRORMSG, getCityErrorMsg());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTPHONELABEL, getPhoneLabel());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTPHONEERRORMSG, getPhoneErrorMsg());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTOTHERTITLE, getOtherTitle());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTOTHERDESCRIPTIONLABEL, getOtherDescriptionLabel());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTARIBANETWORKLABEL, getAribaNetworkLabel());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTARIBAEMAILLABEL, getAribaEmailLabel());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTARIBAEMAILERRORMSG, getAribaEmailErrorMsg());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTTPEMAILLABEL, getTpEmailLabel());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTTPEMAILERRORMSG, getTpEmailErrorMsg());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTSENDBUTTONLABEL, getSendButtonLabel());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTTHANKYOUTITLE, getThankyouTitleLabel());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTTHANKYOUSUBTITLE, getThankyouSubtitleLabel());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTHOMEBUTTON, getHomeButtonLabel());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTONBOARDINGMAINTAIN, getOnboardingMaintanance());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTONBOARDINGMAINTAINSUBTITLE,
				getOnboardingMaintananceSubtitle());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTSOURCING, getSourcingContracting());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTSOURCINGSUBTITLE, getSourcingContractingSubtitle());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTCATALOGUES, getCatalogues());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTCATALOUGESSUBTITLE, getCataloguesSubtitle());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTOTHERPURPOSE, getOtherPurpose());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTOTHERPURPOSESUBTITLE, getOtherPurposeSubtitle());

		Cookie nameCookie = request.getCookie(SupplierPortalConstants.COOKIE_NAME);
		if (nameCookie != null) {
			i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTUSERNAME, nameCookie.getValue());
		}

		String email = GlobalUtil.getCustomerEmailAddress(request);
		if (!StringUtils.isEmpty(email)) {
			i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTUSEREMAIL, email);
		}
		Gson gson = new Gson();
		i18nKeys = gson.toJson(i18KeyMap);
	}
}
