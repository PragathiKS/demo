package com.tetrapak.supplierportal.core.models;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.google.gson.Gson;
import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;

@Model(adaptables = { SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SupportRequestFormModel {

	/** The resource. */
	@Self
	private Resource resource;

	/** The site label. */
	@ValueMapValue
	private String generalTitleLabel;

	@Inject
	private String generalSubtitleLabel;

	@Inject
	private String purposeOfContactLabel;

	@Inject
	private String purposeErrorMsgLabel;

	@Inject
	private String queryTitle;

	@Inject
	private String querySubtitle;

	@Inject
	private String queryErrorMsgLabel;

	@Inject
	private String dragAndDropTitle;

	@Inject
	private String dragAndDropSubtitle;

	@Inject
	private String fileErrorMsg;

	@Inject
	private String fileFormatSubtitle;

	@Inject
	private String dragAndDropButtonLabel;

	@Inject
	private String dragAndDropRemoveFileLabel;

	@Inject
	private String detailsTitle;

	@Inject
	private String nameLabel;

	@Inject
	private String emailLabel;

	@Inject
	private String companyErrorMsg;

	@Inject
	private String companyLabel;

	@Inject
	private String countryLabel;

	@Inject
	private String countryErrorMsg;

	@Inject
	private String cityLabel;

	@Inject
	private String cityErrorMsg;

	@Inject
	private String phoneLabel;
	
	@Inject
	private String phoneErrorMsg;

	@Inject
	private String otherTitle;

	@Inject
	private String otherDescriptionLabel;

	@Inject
	private String aribaNetworkLabel;

	@Inject
	private String aribaEmailLabel;
	
	@Inject
	private String aribaEmailErrorMsg;

	@Inject
	private String tpEmailLabel;

	@Inject
	private String tpEmailErrorMsg;

	@Inject
	private String SendButtonLabel;

	@Inject
	private String thankyouTitleLabel;

	@Inject
	private String thankyouSubtitleLabel;

	@Inject
	private String homeButtonLabel;

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

	public String getDragAndDropTitle() {
		return dragAndDropTitle;
	}

	public void setDragAndDropTitle(String dragAndDropTitle) {
		this.dragAndDropTitle = dragAndDropTitle;
	}

	public String getDragAndDropSubtitle() {
		return dragAndDropSubtitle;
	}

	public void setDragAndDropSubtitle(String dragAndDropSubtitle) {
		this.dragAndDropSubtitle = dragAndDropSubtitle;
	}

	public String getFileErrorMsg() {
		return fileErrorMsg;
	}

	public void setFileErrorMsg(String fileErrorMsg) {
		this.fileErrorMsg = fileErrorMsg;
	}

	public String getFileFormatSubtitle() {
		return fileFormatSubtitle;
	}

	public void setFileFormatSubtitle(String fileFormatSubtitle) {
		this.fileFormatSubtitle = fileFormatSubtitle;
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
     * Gets the Mapped Path of this resource.
     *
     * @return the mappedPath
     */
    public String getMappedResourcePath() {
        ResourceResolver resolver = resource.getResourceResolver();
        return resolver.map(resource.getPath());
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
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTDRAGANDDROPTITLE, getDragAndDropTitle());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTDRAGANDDROPSUBTITLE, getDragAndDropSubtitle());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTFILEERRORMSG, getFileErrorMsg());
		i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTFILEFORMATSUBTITLE, getFileFormatSubtitle());
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

		Gson gson = new Gson();
		i18nKeys = gson.toJson(i18KeyMap);

	}
}
