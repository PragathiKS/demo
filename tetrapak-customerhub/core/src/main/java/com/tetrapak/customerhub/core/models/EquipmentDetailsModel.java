package com.tetrapak.customerhub.core.models;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.sling.api.resource.Resource;
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
 * Model class for Equipment Details component
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class EquipmentDetailsModel {

    @Self
    private Resource resource;

    @Inject
    private String country;

    @Inject
    private String equipmentInformation;

    @Inject
    private String updateEquipmentInformation;

    @Inject
    private String equipmentDetails;

    @Inject
    private String additionalDetails;

    @Inject
    private String location;

    @Inject
    private String site;

    @Inject
    private String line;

    @Inject
    private String equipmentStatus;

    @Inject
    private String position;

    @Inject
    private String equipmentDescription;

    @Inject
    private String equipmentType;

    @Inject
    private String machineSystem;

    @Inject
    private String material;

    @Inject
    private String manufacturerModelNumber;

    @Inject
    private String manufacturerSerialNumber;

    @Inject
    private String superiorEquipment;

    @Inject
    private String functionalLocationDesc;

    @Inject
    private String manufacturer;

    @Inject
    private String manufacturerCountry;

    @Inject
    private String constructionYear;

    @Inject
    private String customerWarrantyEndDate;

    @Inject
    private String customerWarrantyStartDate;

    @Inject
    private String businessType;

    @Inject
    private String equipmentCategory;

    @Inject
    private String eofsConfirmationDate;

    @Inject
    private String eofsValidFromDate;

    @Inject
    private String equipmentListApi;

    private String i18nKeys;

    @OSGiService
    private SlingSettingsService slingSettingsService;

    @OSGiService
    private APIGEEService service;

    private boolean isPublishEnvironment= Boolean.FALSE;

    public String getCountry() {
        return country;
    }

    public String getLocation() {
        return location;
    }

    public String getSite() {
        return site;
    }

    public String getLine() {
        return line;
    }

    public String getEquipmentStatus() {
        return equipmentStatus;
    }

    public String getPosition() {
        return position;
    }

    public String getEquipmentDescription() {
        return equipmentDescription;
    }

    public String getEquipmentInformation() {
        return equipmentInformation;
    }

    public String getUpdateEquipmentInformation() {
        return updateEquipmentInformation;
    }

    public String getEquipmentDetails() {
        return equipmentDetails;
    }

    public String getAdditionalDetails() {
        return additionalDetails;
    }

    public String getI18nKeys() {
        return i18nKeys;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public String getMachineSystem() {
        return machineSystem;
    }

    public String getMaterial() {
        return material;
    }

    public String getManufacturerModelNumber() {
        return manufacturerModelNumber;
    }

    public String getManufacturerSerialNumber() {
        return manufacturerSerialNumber;
    }

    public String getSuperiorEquipment() {
        return superiorEquipment;
    }

    public String getFunctionalLocationDesc() {
        return functionalLocationDesc;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getManufacturerCountry() {
        return manufacturerCountry;
    }

    public String getConstructionYear() {
        return constructionYear;
    }

    public String getCustomerWarrantyEndDate() {
        return customerWarrantyEndDate;
    }

    public String getCustomerWarrantyStartDate() {
        return customerWarrantyStartDate;
    }

    public String getBusinessType() {
        return businessType;
    }

    public String getEquipmentCategory() {
        return equipmentCategory;
    }

    public String getEofsConfirmationDate() {
        return eofsConfirmationDate;
    }

    public String getEofsValidFromDate() {
        return eofsValidFromDate;
    }

    public String getEquipmentListApi() {
        return equipmentListApi;
    }

    public boolean isPublishEnvironment() {
        return isPublishEnvironment;
    }

    /**
     * init method
     */
    @PostConstruct
    protected void init() {
        Map<String, Object> i18KeyMap = new HashMap<>();
        i18KeyMap.put("equipmentInformation", getEquipmentInformation());
        i18KeyMap.put("updateEquipmentInformation", getUpdateEquipmentInformation());
        i18KeyMap.put("equipmentDetails", getEquipmentDetails());
        i18KeyMap.put("additionalDetails", getAdditionalDetails());
        i18KeyMap.put("country", getCountry());
        i18KeyMap.put("location", getLocation());
        i18KeyMap.put("site", getSite());
        i18KeyMap.put("line", getLine());
        i18KeyMap.put("equipmentStatus", getEquipmentStatus());
        i18KeyMap.put("position", getPosition());
        i18KeyMap.put("equipmentDescription", getEquipmentDescription());
        i18KeyMap.put("equipmentType", getEquipmentType());
        i18KeyMap.put("machineSystem", getMachineSystem());
        i18KeyMap.put("material", getMaterial());
        i18KeyMap.put("manufacturerModelNumber", getManufacturerModelNumber());
        i18KeyMap.put("manufacturerSerialNumber", getManufacturerSerialNumber());
        i18KeyMap.put("superiorEquipment", getSuperiorEquipment());
        i18KeyMap.put("functionalLocationDesc", getFunctionalLocationDesc());
        i18KeyMap.put("manufacturer", getManufacturer());
        i18KeyMap.put("manufacturerCountry", getManufacturerCountry());
        i18KeyMap.put("constructionYear", getConstructionYear());
        i18KeyMap.put("customerWarrantyEndDate", getCustomerWarrantyEndDate());
        i18KeyMap.put("customerWarrantyStartDate", getCustomerWarrantyStartDate());
        i18KeyMap.put("businessType", getBusinessType());
        i18KeyMap.put("equipmentCategory", getEquipmentCategory());
        i18KeyMap.put("eofsConfirmationDate", getEofsConfirmationDate());
        i18KeyMap.put("eofsValidFromDate", getEofsValidFromDate());

        if (slingSettingsService.getRunModes().contains("publish")) {
            isPublishEnvironment = Boolean.TRUE;
        }

        Gson gson = new Gson();
        i18nKeys = gson.toJson(i18KeyMap);

        equipmentListApi = service.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR + GlobalUtil
                .getSelectedApiMapping(service, "myequipment-equipmentlist");
    }
}
