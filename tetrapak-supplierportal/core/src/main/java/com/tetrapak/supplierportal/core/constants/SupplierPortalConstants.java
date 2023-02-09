package com.tetrapak.supplierportal.core.constants;

public final class SupplierPortalConstants {

	/** The Constant DEFAULT_LOCALE. */
	public static final String DEFAULT_LOCALE = "en";

	public static final String SUPPLIER_PATH = "/content/tetrapak/supplierportal/global";

	public static final String CONTENT_ROOT = SUPPLIER_PATH + "/en";

	public static final String SUPPORT_REQUEST_FORM_MAIL_TEMPLATE_PATH = "/etc/notification/email/supplierportal/supportrequestemail.html";
	
	public static final String SEND_EMAIL_JOB_TOPIC = "com/tetrapak/supplierportal/sendemail";

	/**
	 * The Constant HTML_EXTENSION.
	 */
	public static final String HTML_EXTENSION = ".html";

	/**
	 * The Constant HTML_EXTENSION.
	 */
	public static final String HTM_EXTENSION = ".htm";

	/**
	 * The Constant HTML_EXTENSION.
	 */
	public static final String PREVIEW = "preview";

	/**
	 * The Constant HTML_EXTENSION.
	 */
	public static final String HASH = "#";

	/**
	 * The Constant HTML_EXTENSION.
	 */
	public static final String TRUE = "true";

	/**
	 * The Constant HTML_EXTENSION.
	 */
	public static final String PUBLISH = "publish";

	/**
	 * The Constant HTML_EXTENSION.
	 */
	public static final String HREF = "href";
	
	/** The Constant RESULT. */
    public static final String RESULT = "result";

    /** The Constant STATUS. */
    public static final String STATUS = "status";

	/**
	 * The Constant CONTENT_PATH .
	 */
	public static final String CONTENT_PATH = "/content/";

	/** The Constant LANGUAGE_LEVEL. */
	public static final int LANGUAGE_PAGE_LEVEL = 4;

	/** The Constant PARAM_LINK. */
	public static final String PARAM_LINK = "linkPath";

	/**
	 * The Constant CONTENT_PATH .
	 */
	public static final String CONTENT_ROOT_PATH = "/content/tetrapak/supplierportal";

	/**
	 * The Constant HTTP.
	 */
	public static final String HTTP = "http";

	/**
	 * The Constant HTTPS.
	 */
	public static final String HTTPS = "https";

	/**
	 * The Constant WWW.
	 */
	public static final String WWW = "www";

	/**
	 * The Constant CONTENT_DAM_PATH.
	 */
	public static final String CONTENT_DAM_PATH = "/content/dam/";

	/**
	 * The Constant DOWNLOAD_LINK.
	 */
	public static final String DOWNLOAD_LINK = "download";

	/**
	 * The Constant DOWNLOAD_LINK.
	 */
	public static final String EXTERNAL_DOWNLOAD_LINK = "externalDownload";

	/**
	 * The Constant INTERNAL_LINK.
	 */
	public static final String INTERNAL_LINK = "internal";

	/**
	 * The Constant EXTERNAL_LINK.
	 */
	public static final String EXTERNAL_LINK = "external";

	public static final String SAML_REQUEST_PATH = "saml_request_path";

	public static final String COOKIE_NAME = "SP-AEMCustomerName";

	public static final String COOKIE_EMAIL = "SP-AEMCustomerEmail";

	public static final String DOMAIN_NAME = "tetrapak.com";

	public static final String TOKEN_NAME = "acctoken";

	public static final String NAVIGATION_CONFIGURATION_RESOURCE_TYPE = "supplierportal/components/structure/navigationconfiguration";

	/** The Constant SUPPLIER_PORTAL. */
	public static final String SUPPLIER_PORTAL = "supplierportal";

	/** The Constant DOMAINSCRIPT. */
	public static final String DOMAINSCRIPT = "domainScript";

	/**
	 * Instantiates a new customer hub constants.
	 */
	private SupplierPortalConstants() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * The Constant Support Request Form
	 */
	public static final String SUPPORTREQUESTGENERALTITLE = "title";
	public static final String SUPPORTREQUESTGENERALSUBTITLE = "subtitle";
	public static final String SUPPORTREQUESTPURPOSEOFCONTACT = "purposeContactHeading";
	public static final String SUPPORTREQUESTPURPOSEERRORMSG = "purposeContactErrorMsg";
	public static final String SUPPORTREQUESTQUERYTITLE = "queryTitle";
	public static final String SUPPORTREQUESTQUERYSUBTITLE = "querySubtitle";
	public static final String SUPPORTREQUESTQUERYERRORMSG = "queryErrorMsg";
	public static final String SUPPORTREQUESTFILEERRORMSG = "fileErrorMsg";
	public static final String SUPPORTREQUESTQUERYPLACEHOLDER = "queryPlaceholder";
	public static final String SUPPORTREQUESTDRAGANDDROPBUTTONLABEL = "dragAndDropButtonLabel";
	public static final String SUPPORTREQUESTDRAGANDDROPREMOVEFILE = "removeFileLabel";
	public static final String SUPPORTREQUESTDETAILSTITLE = "detailsTitle";
	public static final String SUPPORTREQUESTNAMELABEL = "nameLabel";
	public static final String SUPPORTREQUESTEMAILLABEL = "emailAddressLabel";
	public static final String SUPPORTREQUESTCOMPANYERRORMSG = "companyErrorMsg";
	public static final String SUPPORTREQUESTCOMPANYLABEL = "companyLabel";
	public static final String SUPPORTREQUESTCOUNTRYLABEL = "countryLabel";
	public static final String SUPPORTREQUESTCOUNTRYERRORMSG = "countryErrorMsg";
	public static final String SUPPORTREQUESTCITYLABEL = "cityLabel";
	public static final String SUPPORTREQUESTCITYERRORMSG = "cityErrorMsg";
	public static final String SUPPORTREQUESTPHONELABEL = "phoneLabel";
	public static final String SUPPORTREQUESTPHONEERRORMSG = "phoneErrorMsg";
	public static final String SUPPORTREQUESTOTHERTITLE = "otherTitle";
	public static final String SUPPORTREQUESTOTHERDESCRIPTIONLABEL = "otherSubtitle";
	public static final String SUPPORTREQUESTARIBANETWORKLABEL = "aribaNetworkLabel";
	public static final String SUPPORTREQUESTARIBAEMAILLABEL = "aribaEmailLabel";
	public static final String SUPPORTREQUESTARIBAEMAILERRORMSG = "aribaEmailErrorMsg";
	public static final String SUPPORTREQUESTTPEMAILLABEL = "tpEmailLabel";
	public static final String SUPPORTREQUESTTPEMAILERRORMSG = "tpEmailErrorMsg";
	public static final String SUPPORTREQUESTSENDBUTTONLABEL = "sendButtonLabel";
	public static final String SUPPORTREQUESTTHANKYOUTITLE = "thankyouTitle";
	public static final String SUPPORTREQUESTTHANKYOUSUBTITLE = "thankyouSubtitle";
	public static final String SUPPORTREQUESTHOMEBUTTON = "homeButtonLabel";
	public static final String SUPPORTREQUESTONBOARDINGMAINTAIN = "onboardingMaintanance";
	public static final String SUPPORTREQUESTONBOARDINGMAINTAINSUBTITLE = "onboardingMaintananceSubtitle";
	public static final String SUPPORTREQUESTSOURCING = "sourcingContracting";
	public static final String SUPPORTREQUESTSOURCINGSUBTITLE = "sourcingContractingSubtitle";
	public static final String SUPPORTREQUESTCATALOGUES = "catalogues";
	public static final String SUPPORTREQUESTCATALOUGESSUBTITLE = "cataloguesSubtitle";
	
	public static final String EMAILSUBJECT = "subject";
	public static final String SUPPORTREQUESTUSERNAME= "userName";
	public static final String SUPPORTREQUESTUSEREMAIL= "userEmail";

}
