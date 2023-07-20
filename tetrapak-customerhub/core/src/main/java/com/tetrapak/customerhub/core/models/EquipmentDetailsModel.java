package com.tetrapak.customerhub.core.models;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import com.tetrapak.customerhub.core.utils.LinkUtil;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.settings.SlingSettingsService;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Model class for Equipment Details component.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class EquipmentDetailsModel {

    /**
     * The resource.
     */
    @Self
    private Resource resource;

    /**
     * The path to my equipment.
     */
    @Inject
    private String myEqipmentPath;

    /**
     * The country.
     */
    @Inject
    private String country;

    /**
     * The equipment information.
     */
    @Inject
    private String equipmentInformation;

    /**
     * The update equipment information.
     */
    @Inject
    private String updateEquipmentInformation;

    /**
     * The equipment details.
     */
    @Inject
    private String equipmentDetails;

    /**
     * The additional details.
     */
    @Inject
    private String additionalDetails;

    /**
     * The location.
     */
    @Inject
    private String location;

    /**
     * The site.
     */
    @Inject
    private String site;

    /**
     * The line.
     */
    @Inject
    private String line;

    /**
     * The equipment status.
     */
    @Inject
    private String equipmentStatus;

    /**
     * The position.
     */
    @Inject
    private String position;

    /**
     * The equipment description.
     */
    @Inject
    private String equipmentDescription;

    /**
     * The equipment type.
     */
    @Inject
    private String equipmentType;

    /**
     * The machine system.
     */
    @Inject
    private String machineSystem;

    /**
     * The material.
     */
    @Inject
    private String material;

    /**
     * The manufacturer model number.
     */
    @Inject
    private String manufacturerModelNumber;

    /**
     * The manufacturer serial number.
     */
    @Inject
    private String manufacturerSerialNumber;

    /**
     * The superior equipment.
     */
    @Inject
    private String superiorEquipment;

    /**
     * The functional location desc.
     */
    @Inject
    private String functionalLocationDesc;

    /**
     * The manufacturer.
     */
    @Inject
    private String manufacturer;

    /**
     * The manufacturer country.
     */
    @Inject
    private String manufacturerCountry;

    /**
     * The construction year.
     */
    @Inject
    private String constructionYear;

    /**
     * The customer warranty end date.
     */
    @Inject
    private String customerWarrantyEndDate;

    /**
     * The customer warranty start date.
     */
    @Inject
    private String customerWarrantyStartDate;

    /**
     * The business type.
     */
    @Inject
    private String businessType;

    /**
     * The equipment category.
     */
    @Inject
    private String equipmentCategory;

    /**
     * The eofs confirmation date.
     */
    @Inject
    private String eofsConfirmationDate;

    /**
     * The eofs valid from date.
     */
    @Inject
    private String eofsValidFromDate;

    /**
     * The country api.
     */
    @Inject
    private String countryApi;

    /** The all countries api. */
    private String allCountriesApi;

    /**
     * The equipment list api.
     */
    @Inject
    private String equipmentListApi;

    /**
     * The status api.
     */
    @Inject
    private String statusApi;

    /**
     * The cancel.
     */
    @Inject
    private String cancel;

    /**
     * The request updates.
     */
    @Inject
    private String requestUpdates;

    /**
     * The make updates.
     */
    @Inject
    private String makeUpdates;

    /**
     * The i 18 n keys.
     */
    private String i18nKeys;

    /**
     * The thank you label.
     */
    @Inject
    private String thankYouLabel;

    /**
     * The thank you text.
     */
    @Inject
    private String thankYouDescription;

    /**
     * The Modal Confirm Header.
     */
    @Inject
    private String modalConfirmHeader;

    /**
     * The Modal Confirm Text.
     */
    @Inject
    private String modalConfirmText;

    /**
     * The Status Update Heading.
     */
    @Inject
    private String statusUpdateHeading;

    /**
     * The Status Update Description.
     */
    @Inject
    private String statusUpdateDescription;

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
     * The path to Technical Publication page.
     */
    @Inject
    private String techPubPath;

    /**
     * Gets the country.
     *
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Gets the location.
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets the site.
     *
     * @return the site
     */
    public String getSite() {
        return site;
    }

    /**
     * Gets the line.
     *
     * @return the line
     */
    public String getLine() {
        return line;
    }

    /**
     * Gets the equipment status.
     *
     * @return the equipment status
     */
    public String getEquipmentStatus() {
        return equipmentStatus;
    }

    /**
     * Gets the position.
     *
     * @return the position
     */
    public String getPosition() {
        return position;
    }

    /**
     * Gets the equipment description.
     *
     * @return the equipment description
     */
    public String getEquipmentDescription() {
        return equipmentDescription;
    }

    /**
     * Gets the equipment information.
     *
     * @return the equipment information
     */
    public String getEquipmentInformation() {
        return equipmentInformation;
    }

    /**
     * Gets the update equipment information.
     *
     * @return the update equipment information
     */
    public String getUpdateEquipmentInformation() {
        return updateEquipmentInformation;
    }

    /**
     * Gets the country api.
     *
     * @return the country api
     */
    public String getCountryApi() {
        return countryApi;
    }

    /**
     * Gets the all countries api.
     *
     * @return the all countries api
     */
    public String getAllCountriesApi() {
        return allCountriesApi;
    }

    /**
     * Gets the status api.
     *
     * @return the status api
     */
    public String getStatusApi() {
        return statusApi;
    }

    /**
     * Gets the equipment details.
     *
     * @return the equipment details
     */
    public String getEquipmentDetails() {
        return equipmentDetails;
    }

    /**
     * Gets the additional details.
     *
     * @return the additional details
     */
    public String getAdditionalDetails() {
        return additionalDetails;
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
     * Gets the equipment type.
     *
     * @return the equipment type
     */
    public String getEquipmentType() {
        return equipmentType;
    }

    /**
     * Gets the machine system.
     *
     * @return the machine system
     */
    public String getMachineSystem() {
        return machineSystem;
    }

    /**
     * Gets the material.
     *
     * @return the material
     */
    public String getMaterial() {
        return material;
    }

    /**
     * Gets the manufacturer model number.
     *
     * @return the manufacturer model number
     */
    public String getManufacturerModelNumber() {
        return manufacturerModelNumber;
    }

    /**
     * Gets the manufacturer serial number.
     *
     * @return the manufacturer serial number
     */
    public String getManufacturerSerialNumber() {
        return manufacturerSerialNumber;
    }

    /**
     * Gets the superior equipment.
     *
     * @return the superior equipment
     */
    public String getSuperiorEquipment() {
        return superiorEquipment;
    }

    /**
     * Gets the functional location desc.
     *
     * @return the functional location desc
     */
    public String getFunctionalLocationDesc() {
        return functionalLocationDesc;
    }

    /**
     * Gets the manufacturer.
     *
     * @return the manufacturer
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * Gets the manufacturer country.
     *
     * @return the manufacturer country
     */
    public String getManufacturerCountry() {
        return manufacturerCountry;
    }

    /**
     * Gets the construction year.
     *
     * @return the construction year
     */
    public String getConstructionYear() {
        return constructionYear;
    }

    /**
     * Gets the customer warranty end date.
     *
     * @return the customer warranty end date
     */
    public String getCustomerWarrantyEndDate() {
        return customerWarrantyEndDate;
    }

    /**
     * Gets the customer warranty start date.
     *
     * @return the customer warranty start date
     */
    public String getCustomerWarrantyStartDate() {
        return customerWarrantyStartDate;
    }

    /**
     * Gets the business type.
     *
     * @return the business type
     */
    public String getBusinessType() {
        return businessType;
    }

    /**
     * Gets the equipment category.
     *
     * @return the equipment category
     */
    public String getEquipmentCategory() {
        return equipmentCategory;
    }

    /**
     * Gets the eofs confirmation date.
     *
     * @return the eofs confirmation date
     */
    public String getEofsConfirmationDate() {
        return eofsConfirmationDate;
    }

    /**
     * Gets the eofs valid from date.
     *
     * @return the eofs valid from date
     */
    public String getEofsValidFromDate() {
        return eofsValidFromDate;
    }

    /**
     * Gets the cancel.
     *
     * @return the cancel
     */
    public String getCancel() {
        return cancel;
    }

    /**
     * Gets the request updates.
     *
     * @return the request updates
     */
    public String getRequestUpdates() {
        return requestUpdates;
    }

    /**
     * Gets the make updates.
     *
     * @return the make updates
     */
    public String getMakeUpdates() {
        return makeUpdates;
    }

    /**
     * Gets the equipment list api.
     *
     * @return the equipment list api
     */
    public String getEquipmentListApi() {
        return equipmentListApi;
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
     * Gets the ThankYou label.
     *
     * @return the thankYouLabel
     */
    public String getThankYouLabel() {
        return thankYouLabel;
    }

    /**
     * Gets the ThankYou description.
     *
     * @return the thankYouDescription
     */
    public String getThankYouDescription() {
        return thankYouDescription;
    }

    /**
     * Gets the Modal Confirm Label
     *
     * @return the modalConfirmHeader.
     */
    public String getModalConfirmHeader() {
        return modalConfirmHeader;
    }

    /**
     * Gets the Modal Confirm Text.
     *
     * @return the modalConfirmText
     */
    public String getModalConfirmText() {
        return modalConfirmText;
    }

    /**
     * Gets the Status Update Heading.
     *
     * @return the statusUpdateHeading
     */
    public String getStatusUpdateHeading() {
        return statusUpdateHeading;
    }

    /**
     * Gets the Status Update Description.
     *
     * @return the statusUpdateDescription
     */
    public String getStatusUpdateDescription() {
        return statusUpdateDescription;
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
	 * Get valid url to MyEquipment
	 * @return mapped url.
	 */
	public String getMappedMyEquipmentUrl() {
		return LinkUtil.getValidLink(resource, myEqipmentPath);
	}
	
	/**
	 * Get valid url to Technical Publication
	 * @return mapped url.
	 */
	public String getMappedTechnicalPublicationUrl() {
		return LinkUtil.getValidLink(resource, techPubPath);
	}

    /**
     * init method.
     */
    @PostConstruct protected void init() {
        Map<String, Object> i18KeyMap = new HashMap<>();
        i18KeyMap.put(CustomerHubConstants.EQUIPMENT_INFORMATION, getEquipmentInformation());
        i18KeyMap.put(CustomerHubConstants.UPDATE_EQUIPMENT_INFORMATION, getUpdateEquipmentInformation());
        i18KeyMap.put(CustomerHubConstants.EQUIPMENT_DETAILS, getEquipmentDetails());
        i18KeyMap.put(CustomerHubConstants.ADDITIONAL_DETAILS, getAdditionalDetails());
        i18KeyMap.put(CustomerHubConstants.COUNTRY, getCountry());
        i18KeyMap.put(CustomerHubConstants.LOCATION, getLocation());
        i18KeyMap.put(CustomerHubConstants.SITE, getSite());
        i18KeyMap.put(CustomerHubConstants.LINE, getLine());
        i18KeyMap.put(CustomerHubConstants.EQUIPMENT_STATUS, getEquipmentStatus());
        i18KeyMap.put(CustomerHubConstants.POSITION, getPosition());
        i18KeyMap.put(CustomerHubConstants.EQUIPMENT_DESCRIPTION, getEquipmentDescription());
        i18KeyMap.put(CustomerHubConstants.EQUIPMENT_TYPE, getEquipmentType());
        i18KeyMap.put(CustomerHubConstants.MACHINE_SYSTEM, getMachineSystem());
        i18KeyMap.put(CustomerHubConstants.MATERIAL, getMaterial());
        i18KeyMap.put(CustomerHubConstants.MANUFACTURER_MODEL_NUMBER, getManufacturerModelNumber());
        i18KeyMap.put(CustomerHubConstants.MANUFACTURER_SERIAL_NUMBER, getManufacturerSerialNumber());
        i18KeyMap.put(CustomerHubConstants.SUPERIOR_EQUIPMENT, getSuperiorEquipment());
        i18KeyMap.put(CustomerHubConstants.FUNCTIONAL_LOCATION_DESC, getFunctionalLocationDesc());
        i18KeyMap.put(CustomerHubConstants.MANUFACTURER, getManufacturer());
        i18KeyMap.put(CustomerHubConstants.MANUFACTURER_COUNTRY, getManufacturerCountry());
        i18KeyMap.put(CustomerHubConstants.CONSTRUCTION_YEAR, getConstructionYear());
        i18KeyMap.put(CustomerHubConstants.CUSTOMER_WARRANTY_END_DATE, getCustomerWarrantyEndDate());
        i18KeyMap.put(CustomerHubConstants.CUSTOMER_WARRANTY_START_DATE, getCustomerWarrantyStartDate());
        i18KeyMap.put(CustomerHubConstants.BUSINESS_TYPE, getBusinessType());
        i18KeyMap.put(CustomerHubConstants.EQUIPMENT_CATEGORY, getEquipmentCategory());
        i18KeyMap.put(CustomerHubConstants.CONFIRMATION_DATE, getEofsConfirmationDate());
        i18KeyMap.put(CustomerHubConstants.VALID_FROM_DATE, getEofsValidFromDate());
        i18KeyMap.put(CustomerHubConstants.CANCEL, getCancel());
        i18KeyMap.put(CustomerHubConstants.REQUEST_UPDATES, getRequestUpdates());
        i18KeyMap.put(CustomerHubConstants.MAKE_UPDATES, getMakeUpdates());
        i18KeyMap.put(CustomerHubConstants.EQUIPMENT_THANK_YOU_LABEL, getThankYouLabel());
        i18KeyMap.put(CustomerHubConstants.EQUIPMENT_THANK_YOU_DESCRIPTION, getThankYouDescription());
        i18KeyMap.put(CustomerHubConstants.EQUIPMENT_MODAL_CONFIRM_HEADER, getModalConfirmHeader());
        i18KeyMap.put(CustomerHubConstants.EQUIPMENT_MODAL_CONFIRM_TEXT, getModalConfirmText());
        i18KeyMap.put(CustomerHubConstants.EQUIPMENT_STATUS_UPDATE_HEADING, getStatusUpdateHeading());
        i18KeyMap.put(CustomerHubConstants.EQUIPMENT_STATUS_UPDATE_DESCRIPTION, getStatusUpdateDescription());

        if (slingSettingsService.getRunModes().contains("publish")) {
            isPublishEnvironment = Boolean.TRUE;
        }

        Gson gson = new Gson();
        i18nKeys = gson.toJson(i18KeyMap);

        countryApi = service.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR + GlobalUtil
                .getSelectedApiMapping(service, CustomerHubConstants.EQUIPMENT_COUNTRYLIST_API);

        allCountriesApi = service.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR + GlobalUtil
                .getSelectedApiMapping(service, CustomerHubConstants.ALL_COUNTRY_LIST_API);

        statusApi = service.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR + GlobalUtil
                .getSelectedApiMapping(service, CustomerHubConstants.EQUIPMENT_USERSTATUSLIST_API);

        equipmentListApi = service.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR + GlobalUtil
                .getSelectedApiMapping(service, CustomerHubConstants.EQUIPMENT_LIST_API);
    }
}
