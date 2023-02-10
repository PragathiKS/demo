package com.tetrapak.supplierportal.core.models;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;

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

	/** The site label. */
	@ValueMapValue
	private String generalTitleLabel;

	@ValueMapValue
	private String generalSubtitleLabel;

	@ValueMapValue
	private String purposeOfContactLabel;

	@ValueMapValue
	private String purposeErrorMsgLabel;

	@ValueMapValue
	private String queryTitle;

	@ValueMapValue
	private String querySubtitle;

	@ValueMapValue
	private String queryPlaceholder;

	@ValueMapValue
	private String queryErrorMsgLabel;

	@ValueMapValue
	private String fileErrorMsg;

	@ValueMapValue
	private String dragAndDropButtonLabel;

	@ValueMapValue
	private String dragAndDropRemoveFileLabel;

	@ValueMapValue
	private String detailsTitle;

	@ValueMapValue
	private String nameLabel;

	@ValueMapValue
	private String emailLabel;

	@ValueMapValue
	private String companyErrorMsg;

	@ValueMapValue
	private String companyLabel;

	@ValueMapValue
	private String countryLabel;

	@ValueMapValue
	private String countryErrorMsg;

	@ValueMapValue
	private String cityLabel;

	@ValueMapValue
	private String cityErrorMsg;

	@ValueMapValue
	private String phoneLabel;

	@ValueMapValue
	private String phoneErrorMsg;

	@ValueMapValue
	private String otherTitle;

	@ValueMapValue
	private String otherDescriptionLabel;

	@ValueMapValue
	private String aribaNetworkLabel;

	@ValueMapValue
	private String aribaEmailLabel;

	@ValueMapValue
	private String aribaEmailErrorMsg;

	@ValueMapValue
	private String tpEmailLabel;

	@ValueMapValue
	private String tpEmailErrorMsg;

	@ValueMapValue
	private String SendButtonLabel;

	@ValueMapValue
	private String thankyouTitleLabel;

	@ValueMapValue
	private String thankyouSubtitleLabel;

	@ValueMapValue
	private String homeButtonLabel;

	@ValueMapValue
	private String onboardingMaintanance;

	@ValueMapValue
	private String onboardingMaintananceSubtitle;

	@ValueMapValue
	private String sourcingContracting;

	@ValueMapValue
	private String sourcingContractingSubtitle;

	@ValueMapValue
	private String catalogues;

	@ValueMapValue
	private String cataloguesSubtitle;

	@ValueMapValue
	private String otherPurpose;

	@ValueMapValue
	private String otherPurposeSubtitle;

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

		Cookie emailCookie = request.getCookie(SupplierPortalConstants.COOKIE_EMAIL);
		if (emailCookie != null) {
			i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTUSEREMAIL, emailCookie.getValue());
		}

		Gson gson = new Gson();
		i18nKeys = gson.toJson(i18KeyMap);
	}
}
