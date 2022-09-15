package com.tetrapak.customerhub.core.models;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
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
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RebuildingKitDetailsModel {

    /**
     * The resource.
     */
    @Self
    private Resource resource;
    
    /**
     * The i 18 n keys.
     */
    private String i18nKeys;

	/**
	 * The RK And Equipment Information
	 */
	@Inject
    private String rkAndEquipmentInformation;
	
	/**
	 * The Country Location
	 */
	@Inject
    private String countryLocation;

	/**
	 * The Functional Location
	 */
    @Inject
    private String functionalLocation;

    /**
	 * The Equipment Material
	 */
    @Inject
    private String equipmentMaterial;

    /**
	 * The Equipment Status Type
	 */
    @Inject
    private String statusEquipmentType;

    /**
	 * The Implementation Status Date
	 */
    @Inject
    private String implementationStatusDate;

    /**
	 * The Equipment Structure
	 */
    @Inject
    private String equipmentStructure;

    /**
	 * The Report Implementation Status
	 */
    @Inject
    private String reportImplementationStatus;

    /**
	 * The RK Note
	 */
    @Inject
    private String rkNote;

    /**
	 * The RK Note Value
	 */
    @Inject
    private String rkNoteValue;

    /**
	 * The RK Files
	 */
    @Inject
    private String rkFiles;
    
    /**
	 * The RK CTI
	 */
    @Inject
    private String rkCTI;

    /**
	 * The More Language
	 */
    @Inject
    private String moreLanguage;

    /**
	 * The RK Information
	 */
    @Inject
    private String rkInformation;

    /**
	 * The Ref Release Date
	 */
    @Inject
    private String refReleaseDate;

    /**
	 * The RK Type
	 */
    @Inject
    private String rkType;

    /**
	 * The RK Status
	 */
    @Inject
    private String rkStatus;

    /**
	 * The RK Handling
	 */
    @Inject
    private String rkHandling;

    /**
	 * The RK Planning Information
	 */
    @Inject
    private String rkPlanningInformation;

    /**
	 * The Implementation Deadline
	 */
    @Inject
    private String implDeadline;

    /**
	 * The Service Order
	 */
    @Inject
    private String serviceOrder;
    
	/**
	 * The rebuildingKitDetailsApi
	 */
    private String rebuildingKitDetailsApi;

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
     * Gets the Rebuilding Kit Details API.
     *
     * @return the rebuildingKitDetailsApi
     */
	public String getRebuildingKitDetailsApi() {
		return rebuildingKitDetailsApi;
	}
	
    /**
     * Checks if is publish environment.
     *
     * @return true, if is publish environment
     */
    public boolean isPublishEnvironment() {
        return isPublishEnvironment;
    }

    /**
     * init method.
     */
    @PostConstruct protected void init() {
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
        i18KeyMap.put(CustomerHubConstants.RK_HANDLING, getRkHandling());
        i18KeyMap.put(CustomerHubConstants.RK_PLANNING_INFORMATION, getRkPlanningInformation());
        i18KeyMap.put(CustomerHubConstants.IMPL_DEADLINE, getImplDeadline());
        i18KeyMap.put(CustomerHubConstants.SERVICE_ORDER, getServiceOrder());

        if (slingSettingsService.getRunModes().contains("publish")) {
            isPublishEnvironment = Boolean.TRUE;
        }

        Gson gson = new Gson();
        i18nKeys = gson.toJson(i18KeyMap);

        rebuildingKitDetailsApi = service.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR + GlobalUtil
                .getSelectedApiMapping(service, CustomerHubConstants.RK_DETAILS_API);
    }
}
