package com.tetrapak.supplierportal.core.models;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;

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

    private static final String SUPPORTREQUESTPURPOSEOFCONTACT = "Purpose of contract";
    private static final String SUPPORTREQUESTPURPOSEERRORMSG = "Please select a purpose";
    private static final String SUPPORTREQUESTQUERYTITLE = "How can we help you?";
    private static final String SUPPORTREQUESTQUERYSUBTITLE = "Describe your query";
    private static final String SUPPORTREQUESTQUERYERRORMSG = "Please describe your query";
    private static final String SUPPORTREQUESTFILEERRORMSG = "File size cannot exceed 5MB";
    private static final String SUPPORTREQUESTQUERYPLACEHOLDER = "Tell us, more in detail, about your query...";
    private static final String SUPPORTREQUESTDRAGANDDROPBUTTONLABEL = "Attach file";
    private static final String SUPPORTREQUESTDRAGANDDROPREMOVEFILE = "Remove file";
    private static final String SUPPORTREQUESTDETAILSTITLE = "Your contact information";
    private static final String SUPPORTREQUESTNAMELABEL = "Name";
    private static final String SUPPORTREQUESTEMAILLABEL = "Email address";
    private static final String SUPPORTREQUESTCOMPANYERRORMSG = "Please enter your company legal name";
    private static final String SUPPORTREQUESTCOMPANYLABEL = "Company legal name";
    private static final String SUPPORTREQUESTCOUNTRYLABEL = "Country";
    private static final String SUPPORTREQUESTCOUNTRYERRORMSG = "Please enter your country";
    private static final String SUPPORTREQUESTCITYLABEL = "City";
    private static final String SUPPORTREQUESTCITYERRORMSG = "Please enter your city";
    private static final String SUPPORTREQUESTPHONELABEL = "Phone number";
    private static final String SUPPORTREQUESTPHONEERRORMSG = "Please enter a valid mobile telephone number";
    private static final String SUPPORTREQUESTOTHERTITLE = "Other information";
    private static final String SUPPORTREQUESTOTHERDESCRIPTIONLABEL = "When possible=share a little more information. This may help our support team to help you faster.";
    private static final String SUPPORTREQUESTARIBANETWORKLABEL = "Ariba Network ID(optional)";
    private static final String SUPPORTREQUESTARIBAEMAILLABEL = "Ariba account administrator email(optional)";
    private static final String SUPPORTREQUESTARIBAEMAILERRORMSG = "Please enter a valid email";
    private static final String SUPPORTREQUESTTPEMAILLABEL = "Tetra Pak contact email(optional)";
    private static final String SUPPORTREQUESTTPEMAILERRORMSG = "Please enter a valid email";
    private static final String SUPPORTREQUESTSENDBUTTONLABEL = "Submit";
    private static final String SUPPORTREQUESTTHANKYOUTITLE = "Thank you!";
    private static final String SUPPORTREQUESTTHANKYOUSUBTITLE = "We will get back to you via email as soon as possible";
    private static final String SUPPORTREQUESTHOMEBUTTON = "Go to Home";
    private static final String SUPPORTREQUESTONBOARDINGMAINTAIN = "SAP Ariba - Onboarding and maintenance";
    private static final String SUPPORTREQUESTONBOARDINGMAINTAINSUBTITLE = "Get support with registration updating supplier information or similar";
    private static final String SUPPORTREQUESTSOURCING = "SAP Ariba - Sourcing & Contracting";
    private static final String SUPPORTREQUESTSOURCINGSUBTITLE = "Get support with sending and responding to RFI/RFQs participating in auctions=using electronic signature";
    private static final String SUPPORTREQUESTCATALOGUES = "SAP Ariba - Catalogues";
    private static final String SUPPORTREQUESTCATALOUGESSUBTITLE = "Get support with creating or maintaining catalogues";
    private static final String SUPPORTREQUESTOTHERPURPOSE = "Other purpose";
    private static final String SUPPORTREQUESTOTHERPURPOSESUBTITLE = "Get support with something that is not covered in above purposes";

    /**
	 * The site label.
	 */
	//	General Tab
	@ValueMapValue
	private String generalTitleLabel;

	@ValueMapValue
	private String generalSubtitleLabel;

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
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTPURPOSEOFCONTACT, SUPPORTREQUESTPURPOSEOFCONTACT);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTPURPOSEERRORMSG, SUPPORTREQUESTPURPOSEERRORMSG);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTQUERYTITLE, SUPPORTREQUESTQUERYTITLE);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTQUERYSUBTITLE, SUPPORTREQUESTQUERYSUBTITLE);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTQUERYERRORMSG, SUPPORTREQUESTQUERYERRORMSG);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTFILEERRORMSG, SUPPORTREQUESTFILEERRORMSG);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTQUERYPLACEHOLDER, SUPPORTREQUESTQUERYPLACEHOLDER);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTDRAGANDDROPBUTTONLABEL,
                SUPPORTREQUESTDRAGANDDROPBUTTONLABEL);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTDRAGANDDROPREMOVEFILE, SUPPORTREQUESTDRAGANDDROPREMOVEFILE);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTDETAILSTITLE, SUPPORTREQUESTDETAILSTITLE);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTNAMELABEL, SUPPORTREQUESTNAMELABEL);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTEMAILLABEL, SUPPORTREQUESTEMAILLABEL);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTCOMPANYERRORMSG, SUPPORTREQUESTCOMPANYERRORMSG);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTCOMPANYLABEL, SUPPORTREQUESTCOMPANYLABEL);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTCOUNTRYLABEL, SUPPORTREQUESTCOUNTRYLABEL);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTCOUNTRYERRORMSG, SUPPORTREQUESTCOUNTRYERRORMSG);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTCITYLABEL, SUPPORTREQUESTCITYLABEL);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTCITYERRORMSG, SUPPORTREQUESTCITYERRORMSG);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTPHONELABEL, SUPPORTREQUESTPHONELABEL);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTPHONEERRORMSG, SUPPORTREQUESTPHONEERRORMSG);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTOTHERTITLE, SUPPORTREQUESTOTHERTITLE);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTOTHERDESCRIPTIONLABEL, SUPPORTREQUESTOTHERDESCRIPTIONLABEL);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTARIBANETWORKLABEL, SUPPORTREQUESTARIBANETWORKLABEL);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTARIBAEMAILLABEL, SUPPORTREQUESTARIBAEMAILLABEL);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTARIBAEMAILERRORMSG, SUPPORTREQUESTARIBAEMAILERRORMSG);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTTPEMAILLABEL, SUPPORTREQUESTTPEMAILLABEL);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTTPEMAILERRORMSG, SUPPORTREQUESTTPEMAILERRORMSG);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTSENDBUTTONLABEL, SUPPORTREQUESTSENDBUTTONLABEL);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTTHANKYOUTITLE, SUPPORTREQUESTTHANKYOUTITLE);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTTHANKYOUSUBTITLE, SUPPORTREQUESTTHANKYOUSUBTITLE);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTHOMEBUTTON, SUPPORTREQUESTHOMEBUTTON);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTONBOARDINGMAINTAIN, SUPPORTREQUESTONBOARDINGMAINTAIN);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTONBOARDINGMAINTAINSUBTITLE,
                SUPPORTREQUESTONBOARDINGMAINTAINSUBTITLE);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTSOURCING, SUPPORTREQUESTSOURCING);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTSOURCINGSUBTITLE, SUPPORTREQUESTSOURCINGSUBTITLE);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTCATALOGUES, SUPPORTREQUESTCATALOGUES);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTCATALOUGESSUBTITLE, SUPPORTREQUESTCATALOUGESSUBTITLE);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTOTHERPURPOSE, SUPPORTREQUESTOTHERPURPOSE);
        i18KeyMap.put(SupplierPortalConstants.SUPPORTREQUESTOTHERPURPOSESUBTITLE, SUPPORTREQUESTOTHERPURPOSESUBTITLE);

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
