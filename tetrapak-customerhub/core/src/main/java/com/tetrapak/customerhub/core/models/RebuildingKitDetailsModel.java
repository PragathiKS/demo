package com.tetrapak.customerhub.core.models;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;

import com.tetrapak.customerhub.core.servlets.RebuildingKitsEmailServlet;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;
import com.google.gson.Gson;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;

/**
 * Model class for Rebuilding Kit Details component.
 */
/**
 * @author INNIMBALKARS
 *
 */
@Model(adaptables = {
        Resource.class, SlingHttpServletRequest.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RebuildingKitDetailsModel {

    public static final String EMAIL_ADDRESS = "emailaddress";
    public static final String EMAIL_SALUTATION = "salutation";
    public static final String EMAIL_SUBJECT = "subject";
    public static final String EMAIL_BODY = "body";
    public static final String USERNAME = "username";
    public static final String RK_TB_NUMBER = "rkTbNumber";
    public static final String MCON = "mcon";
    public static final String FUNCTIONAL_LOCATION = "functionalLocation";
    public static final String REQUESTED_CTI_LANGUAGE = "requestedCTILanguage";
    public static final String COMMENTS_JSON_KEY = "comments";

    @SlingObject
    private SlingHttpServletRequest request;

    /** The resource. */
    @SlingObject
    private Resource resource;

    /**
     * The i 18 n keys.
     */
    private String i18nKeys;

	/**
	 * The RK And Equipment Information
	 */
    @ValueMapValue
    private String rkAndEquipmentInformation;
	
	/**
	 * The Country Location
	 */
    @ValueMapValue
    private String countryLocation;

	/**
	 * The Functional Location
	 */
    @ValueMapValue
    private String functionalLocation;

    /**
	 * The Equipment Material
	 */
    @ValueMapValue
    private String equipmentMaterial;

    /**
	 * The Equipment Status Type
	 */
    @ValueMapValue
    private String statusEquipmentType;

    /**
	 * The Implementation Status Date
	 */
    @ValueMapValue
    private String implementationStatusDate;

    /**
	 * The Equipment Structure
	 */
    @ValueMapValue
    private String equipmentStructure;

    /**
	 * The Report Implementation Status
	 */
    @ValueMapValue
    private String reportImplementationStatus;

    /**
	 * The RK Note
	 */
    @ValueMapValue
    private String rkNote;

    /**
	 * The RK Note Value
	 */
    @ValueMapValue
    private String rkNoteValue;

    /**
	 * The RK Files
	 */
    @ValueMapValue
    private String rkFiles;
    
    /**
	 * The RK CTI
	 */
    @ValueMapValue
    private String rkCTI;

    /**
	 * The More Language
	 */
    @ValueMapValue
    private String moreLanguage;

    /**
	 * The RK Information
	 */
    @ValueMapValue
    private String rkInformation;

    /**
	 * The Ref Release Date
	 */
    @ValueMapValue
    private String refReleaseDate;

    /**
	 * The RK Type
	 */
    @ValueMapValue
    private String rkType;

    /**
	 * The RK Status
	 */
    @ValueMapValue
    private String rkStatus;

    /**
     * The RK SubType
     */
    @ValueMapValue
    private String rkSubType;

    /**
	 * The RK Handling
	 */
    @ValueMapValue
    private String rkHandling;

    /**
     * The RK Validation
     */
    @ValueMapValue
    private String rkValidation;
    /**
	 * The RK Planning Information
	 */
    @ValueMapValue
    private String rkPlanningInformation;

    /**
	 * The Implementation Deadline
	 */
    @ValueMapValue
    private String implDeadline;

    /**
	 * The Service Order
	 */
    @ValueMapValue
    private String serviceOrder;

    /**
     * The RK No CTI Text
     */
    @ValueMapValue
    private String rkNoCtiText;

    /**
     * The RK Required CTI Text
     */
    @ValueMapValue
    private String rkReqCtiText;

    @ValueMapValue
    private String rkctisubjecttext;

    @ValueMapValue
    private String rkctisalutationtext;

    @ValueMapValue
    private String rkctibodytext;

    @ValueMapValue
    private String rkctiUsernameText;

    @ValueMapValue
    private String rktbnumbertext;

    @ValueMapValue
    private String rkctimcontext;

    @ValueMapValue
    private String rkcticommenttext;

    @ValueMapValue
    private String rkctifunctionalLocationtext;

    @ValueMapValue
    private String rkctirequestedlanguage;

    @ValueMapValue
    private String rkctiemailaddress;

    @ValueMapValue
    private String rkReqCtiNewTranslation;

    @ValueMapValue
    private String rkReqCtiATranslation;

    @ValueMapValue
    private String rkReqCtiThankyoutext;

    @ValueMapValue
    private String rkReqCtiThankyoumessage;

    @ValueMapValue
    private String rkReqCtiDropdownError;

    @ValueMapValue
    private String rkRequestCTIText;

    @ValueMapValue
    private String rkReqWhatLanguageWantText;

	/**
	 * The rebuildingKitDetailsApi
	 */
    private String rebuildingKitDetailsApi;
    
	/**
	 * The technicalBulletinApi
	 */
    private String technicalBulletinApi;

	/**
     * The sling settings service.
     */
    @OSGiService
    private SlingSettingsService slingSettingsService;

    /**
     * The service.
     */
    @OSGiService
    private APIGEEService service;

    /**
     * The is publish environment.
     */
    private boolean isPublishEnvironment = Boolean.FALSE;

    private String userNameValue;

    private String emailAddressValue;

    private String emailApiUrl;

    /**
     * init method.
     */
    @PostConstruct protected void init() throws UnsupportedEncodingException {
        Map<String, Object> i18KeyMap = new HashMap<>();
        i18KeyMap.put(CustomerHubConstants.RK_AND_EQUIPMENT_INFORMATION, getRkAndEquipmentInformation());
        i18KeyMap.put(CustomerHubConstants.COUNTRY_LOCATION, getCountryLocation());
        i18KeyMap.put(CustomerHubConstants.FUNCTIONAL_LOCATION, getFunctionalLocation());
        i18KeyMap.put(CustomerHubConstants.EQUIPMENT_MATERIAL, getEquipmentMaterial());
        i18KeyMap.put(CustomerHubConstants.STATU_EQUIPMENT_TYPE, getStatusEquipmentType());
        i18KeyMap.put(CustomerHubConstants.IMPLEMENTATION_STATUS_DATE, getImplementationStatusDate());
        i18KeyMap.put(CustomerHubConstants.EQUIPMENT_STRUCTURE, getEquipmentStructure());
        i18KeyMap.put(CustomerHubConstants.REPOST_IMPLEMENTATION_STATUS, getReportImplementationStatus());
        i18KeyMap.put(CustomerHubConstants.RK_NOTE, getRkNote());
        i18KeyMap.put(CustomerHubConstants.RK_NOTE_VALUE, getRkNoteValue());
        i18KeyMap.put(CustomerHubConstants.RK_FILES, getRkFiles());
        i18KeyMap.put(CustomerHubConstants.RK_CTI, getRkCTI());
        i18KeyMap.put(CustomerHubConstants.MORE_LANGUAGE, getMoreLanguage());
        i18KeyMap.put(CustomerHubConstants.RK_INFORMATION, getRkInformation());
        i18KeyMap.put(CustomerHubConstants.REF_RELEASE_DATE, getRefReleaseDate());
        i18KeyMap.put(CustomerHubConstants.RK_TYPE, getRkType());
        i18KeyMap.put(CustomerHubConstants.RK_STATUS, getRkStatus());
        i18KeyMap.put(CustomerHubConstants.RK_SUBTYPE, getRkSubType());
        i18KeyMap.put(CustomerHubConstants.RK_HANDLING, getRkHandling());
        i18KeyMap.put(CustomerHubConstants.RK_VALIDATION, getRkValidation());
        i18KeyMap.put(CustomerHubConstants.RK_PLANNING_INFORMATION, getRkPlanningInformation());
        i18KeyMap.put(CustomerHubConstants.IMPL_DEADLINE, getImplDeadline());
        i18KeyMap.put(CustomerHubConstants.SERVICE_ORDER, getServiceOrder());
        i18KeyMap.put(CustomerHubConstants.RK_NO_CTI_TEXT, getRkNoCtiText());
        i18KeyMap.put(CustomerHubConstants.RK_REQ_CTI_TEXT, getRkReqCtiText());
        i18KeyMap.put(CustomerHubConstants.REQUEST_NEW_CTI_TRANSLATION, getRkReqCtiNewTranslation());
        i18KeyMap.put(CustomerHubConstants.REQUEST_A_CTI_TRANSLATION, getRkReqCtiATranslation());
        i18KeyMap.put(CustomerHubConstants.CTI_REQUEST_THANKYOU_TEXT, getRkReqCtiThankyoutext());
        i18KeyMap.put(CustomerHubConstants.CTI_REQUEST_THANKYOU_MESSAGE, getRkReqCtiThankyoumessage());
        i18KeyMap.put(CustomerHubConstants.CTI_TRANSLATION_DROPDOWN_ERROR, getRkReqCtiDropdownError());
        i18KeyMap.put(CustomerHubConstants.CTI_REQUEST_TEXT, getRkRequestCTIText());
        i18KeyMap.put(CustomerHubConstants.CTI_WHAT_LANGUAGE_REQUIRED_TEXT, getRkReqWhatLanguageWantText());
        if (slingSettingsService.getRunModes().contains("publish")) {
            isPublishEnvironment = Boolean.TRUE;
        }

        Gson gson = new Gson();
        i18nKeys = gson.toJson(i18KeyMap);

        final String componentPath = resource.getResourceResolver().map(this.resource.getPath());
        final String componentPathExtension = CustomerHubConstants.DOT
                + RebuildingKitsEmailServlet.SLING_SERVLET_SELECTOR + CustomerHubConstants.DOT
                + RebuildingKitsEmailServlet.SLING_SERVLET_EXTENSION;
        this.emailApiUrl = componentPath + componentPathExtension;

        rebuildingKitDetailsApi = service.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR + GlobalUtil
                .getSelectedApiMapping(service, CustomerHubConstants.RK_DETAILS_API);

        technicalBulletinApi = service.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR + GlobalUtil
                .getSelectedApiMapping(service, CustomerHubConstants.TECHNICAL_BULLETIN_API);

        this.setEmailAddressValue();
        this.setUserNameValue();
    }

    public void setEmailAddressValue() {
        this.emailAddressValue = GlobalUtil.getCustomerEmailAddress(this.request);
    }

    public void setUserNameValue() throws UnsupportedEncodingException {
        if (Objects.nonNull(request.getCookie(CustomerHubConstants.CUSTOMER_COOKIE_NAME))) {
            this.userNameValue = URLDecoder.decode(
                    request.getCookie(CustomerHubConstants.CUSTOMER_COOKIE_NAME).getValue(), "UTF-8");
        }
    }
    
    /**
     * Gets the i 18 n keys.
     *
     * @return the i 18 n keys
     */
    public String getI18nKeys() {
        return i18nKeys;
    }

    /**
     * Gets the rkAndEquipmentInformation.
     *
     * @return the rkAndEquipmentInformation
     */
	public String getRkAndEquipmentInformation() {
		return rkAndEquipmentInformation;
	}

    /**
     * Gets the functionalLocation.
     *
     * @return the functionalLocation
     */
	public String getFunctionalLocation() {
		return functionalLocation;
	}
	
    /**
     * Gets the Country Location.
     *
     * @return the countryLocation
     */
	public String getCountryLocation() {
		return countryLocation;
	}


    /**
     * Gets the equipmentMaterial.
     *
     * @return the equipmentMaterial
     */
	public String getEquipmentMaterial() {
		return equipmentMaterial;
	}

    /**
     * Gets the statusEquipmentType.
     *
     * @return the statusEquipmentType
     */
	public String getStatusEquipmentType() {
		return statusEquipmentType;
	}

    /**
     * Gets the implementationStatusDate.
     *
     * @return the implementationStatusDate
     */
	public String getImplementationStatusDate() {
		return implementationStatusDate;
	}

    /**
     * Gets the equipmentStructure.
     *
     * @return the equipmentStructure
     */
	public String getEquipmentStructure() {
		return equipmentStructure;
	}

    /**
     * Gets the reportImplementationStatus.
     *
     * @return the reportImplementationStatus
     */
	public String getReportImplementationStatus() {
		return reportImplementationStatus;
	}

    /**
     * Gets the rkNote.
     *
     * @return the rkNote
     */
	public String getRkNote() {
		return rkNote;
	}

    /**
     * Gets the rkNoteValue.
     *
     * @return the rkNoteValue
     */
	public String getRkNoteValue() {
		return rkNoteValue;
	}

    /**
     * Gets the rkFiles.
     *
     * @return the rkFiles
     */
	public String getRkFiles() {
		return rkFiles;
	}

    /**
     * Gets the rkCTI.
     *
     * @return the rkCTI
     */
	public String getRkCTI() {
		return rkCTI;
	}

    /**
     * Gets the moreLanguage.
     *
     * @return the moreLanguage
     */
	public String getMoreLanguage() {
		return moreLanguage;
	}

    /**
     * Gets the rkInformation.
     *
     * @return the rkInformation
     */
	public String getRkInformation() {
		return rkInformation;
	}

    /**
     * Gets the refReleaseDate.
     *
     * @return the refReleaseDate
     */
	public String getRefReleaseDate() {
		return refReleaseDate;
	}

    /**
     * Gets the rkType.
     *
     * @return the rkType
     */
	public String getRkType() {
		return rkType;
	}

    /**
     * Gets the rkStatus.
     *
     * @return the rkStatus
     */
	public String getRkStatus() {
		return rkStatus;
	}

    /**
     * Gets the rkSubType.
     *
     * @return the rkSubType
     */
    public String getRkSubType() {
        return rkSubType;
    }

    /**
     * Gets the rkValidation.
     *
     * @return the rkValidation
     */
    public String getRkValidation() {
        return rkValidation;
    }

    /**
     * Gets the rkHandling.
     *
     * @return the rkHandling
     */
	public String getRkHandling() {
		return rkHandling;
	}

    /**
     * Gets the rkPlanningInformation.
     *
     * @return the rkPlanningInformation
     */
	public String getRkPlanningInformation() {
		return rkPlanningInformation;
	}

    /**
     * Gets the implDeadline.
     *
     * @return the implDeadline
     */
	public String getImplDeadline() {
		return implDeadline;
	}

    /**
     * Gets the serviceOrder.
     *
     * @return the serviceOrder
     */
	public String getServiceOrder() {
		return serviceOrder;
	}

    /**
     * Gets the rkNoCtiText.
     *
     * @return the rkNoCtiText
     */
    public String getRkNoCtiText() {
        return rkNoCtiText;
    }

    /**
     * Gets the rkReqCtiText.
     *
     * @return the rkReqCtiText
     */
    public String getRkReqCtiText() {
        return rkReqCtiText;
    }

    /**
     * Gets the Rebuilding Kit Details API.
     *
     * @return the rebuildingKitDetailsApi
     */
	public String getRebuildingKitDetailsApi() {
		return rebuildingKitDetailsApi;
	}
	
    /**
     * Gets the Technical Bulletin API.
     *
     * @return the technicalBulletinApi
     */  

	public String getTechnicalBulletinApi() {
		return technicalBulletinApi;
	}
    /**
     * Checks if is publish environment.
     *
     * @return true, if is publish environment
     */
    public boolean isPublishEnvironment() {
        return isPublishEnvironment;
    }

    public String getRkctisubjecttext() {
        return rkctisubjecttext;
    }

    public String getRkctisalutationtext() {
        return rkctisalutationtext;
    }

    public String getRkctibodytext() {
        return rkctibodytext;
    }

    public String getRkctiUsernameText() {
        return rkctiUsernameText;
    }

    public String getRktbnumbertext() {
        return rktbnumbertext;
    }

    public String getRkctimcontext() {
        return rkctimcontext;
    }

    public String getRkctifunctionalLocationtext() {
        return rkctifunctionalLocationtext;
    }

    public String getRkctirequestedlanguage() {
        return rkctirequestedlanguage;
    }

    public String getRkctiemailaddress() {
        return rkctiemailaddress;
    }

    public String getEmailAddressValue() {
        return StringUtils.defaultIfEmpty(emailAddressValue, StringUtils.EMPTY);
    }
    public String getUserNameValue() {
        return StringUtils.defaultIfEmpty(userNameValue, StringUtils.EMPTY);
    }

    public String getRkcticommenttext() {
        return rkcticommenttext;
    }

    public String getEmailApiUrl() {
        return emailApiUrl;
    }

    public SlingHttpServletRequest getRequest() {
        return request;
    }

    public String getRkReqCtiNewTranslation() {
        return rkReqCtiNewTranslation;
    }

    public String getRkReqCtiATranslation() {
        return rkReqCtiATranslation;
    }

    public String getRkReqCtiThankyoutext() {
        return rkReqCtiThankyoutext;
    }

    public String getRkReqCtiThankyoumessage() {
        return rkReqCtiThankyoumessage;
    }

    public String getRkReqCtiDropdownError() {
        return rkReqCtiDropdownError;
    }

    public String getRkRequestCTIText() {
        return rkRequestCTIText;
    }

    public String getRkReqWhatLanguageWantText() {
        return rkReqWhatLanguageWantText;
    }
}
