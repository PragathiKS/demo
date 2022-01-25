package com.tetrapak.customerhub.core.models;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.settings.SlingSettingsService;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Model class for Add Equipment component.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AddEquipmentModel {

    /** The resource. */
    @Self
    private Resource resource;

    /** The is publish environment. */
    private boolean isPublishEnvironment = Boolean.FALSE;

    /** The sling settings service. */
    @OSGiService
    private SlingSettingsService slingSettingsService;

    /** The service. */
    @OSGiService
    private APIGEEService service;

    /** The country api. */
	private String countryApi;

    /** The all countries api. */
    private String allCountriesApi;

    /** The status api. */
    private String statusApi;

    /** The line api. */
    private String lineApi;

    /** The site api. */
    private String siteApi;

    /** The title label. */
    @Inject
    private String title;

    /** The subtitle reference. */
    @Inject
    private String subTitleReference;

    /** The serial number label. */
    @Inject
    private String serialNumberLabel;

    /** The drag and drop description. */
    @Inject
    private String dragAndDropDescription;

    /** The drag and drop title. */
    @Inject
    private String dragAndDropTitle;

    /** The drag and drop subtitle. */
    @Inject
    private String dragAndDropSubtitle;

    /** The file format subtitle. */
    @Inject
    private String fileFormatSubtitle;

    /** The drag and drop button label. */
    @Inject
    private String dragAndDropButtonLabel;

    /** The drag and drop remove file label. */
    @Inject
    private String dragAndDropRemoveFileLabel;

    /** The details title. */
    @Inject
    private String detailsTitle;

    /** The details reference. */
    @Inject
    private String detailsSubtitleReference;

    /** The country label. */
    @Inject
    private String countryLabel;

    /** The site label. */
    @Inject
    private String siteLabel;

    /** The line label. */
    @Inject
    private String lineLabel;

    /** The position label. */
    @Inject
    private String positionLabel;

    /** The equipment status label. */
    @Inject
    private String equipmentStatusLabel;

    /** The machine system label. */
    @Inject
    private String machineSystemLabel;

    /** The equipment description label. */
    @Inject
    private String equipmentDescriptionLabel;

    /** The manufacture model number label. */
    @Inject
    private String manufactureModelNumberLabel;

    /** The manufacture of asset label. */
    @Inject
    private String manufactureOfAssetLabel;

    /** The country of manufacture label. */
    @Inject
    private String countryOfManufactureLabel;

    /** The construction year label. */
    @Inject
    private String constructionYearLabel;

    /** The comments label. */
    @Inject
    private String commentsLabel;

    /** The submit button label. */
    @Inject
    private String submitButtonLabel;

    /** The dropdown placeholder. */
    @Inject
    private String dropdownPlaceholder;

    /** The submit title. */
    @Inject
    private String submitTitle;

    /** The submit subtitle. */
    @Inject
    private String submitSubtitle;

    /** The add another equipment label. */
    @Inject
    private String addAnotherEquipmentLabel;

    /** The serial number error msg. */
    @Inject
    private String serialNumberErrorMsg;

    /** The country error msg. */
    @Inject
    private String countryErrorMsg;

    /** The site error msg. */
    @Inject
    private String siteErrorMsg;

    /** The line error msg. */
    @Inject
    private String lineErrorMsg;

    /** The position error msg. */
    @Inject
    private String positionErrorMsg;

    /** The equipment status error msg. */
    @Inject
    private String equipmentStatusErrorMsg;

    /** The equipment description error msg. */
    @Inject
    private String equipmentDescriptionErrorMsg;

    /** The manufacture of asset error msg. */
    @Inject
    private String manufactureOfAssetErrorMsg;

    /** The country of manufacture error msg. */
    @Inject
    private String countryOfManufactureErrorMsg;

    /** The construction year error msg. */
    @Inject
    private String constructionYearErrorMsg;

    /** The file extension error msg. */
    @Inject
    private String fileExtensionErrorMsg;

    /** The i 18 n keys. */
    private String i18nKeys;

    /**
     * Checks if is publish environment.
     *
     * @return true, if is publish environment
     */
    public boolean isPublishEnvironment() {
        return isPublishEnvironment;
    }

    /**
     * Gets the sling settings service.
     *
     * @return the sling settings service
     */
    public SlingSettingsService getSlingSettingsService() {
        return slingSettingsService;
    }

    /**
     * Gets the country api.
     *
     * @return the country api
     */
    public String getCountryApi() { return countryApi; }

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
    public String getStatusApi() { return statusApi; }

    /**
     * Gets the line api.
     *
     * @return the line api
     */
    public String getLineApi() { return lineApi; }

    /**
     * Gets the site api.
     *
     * @return the site api
     */
    public String getSiteApi() { return siteApi; }

    /**
     * Gets the i 18 n keys.
     *
     * @return the i 18 n keys
     */
    public String getI18nKeys() {
        return i18nKeys;
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the serial number label.
     *
     * @return the serial number label
     */
    public String getSerialNumberLabel() {
        return serialNumberLabel;
    }

    /**
     * Gets the drag and drop description.
     *
     * @return the drag and drop description
     */
    public String getDragAndDropDescription() {
        return dragAndDropDescription;
    }

    /**
     * Gets the drag and drop title.
     *
     * @return the drag and drop title
     */
    public String getDragAndDropTitle() {
        return dragAndDropTitle;
    }

    /**
     * Gets the drag and drop subtitle.
     *
     * @return the drag and drop subtitle
     */
    public String getDragAndDropSubtitle() {
        return dragAndDropSubtitle;
    }

    /**
     * Gets the file format subtitle.
     *
     * @return the file format subtitle
     */
    public String getFileFormatSubtitle() {
        return fileFormatSubtitle;
    }

    /**
     * Gets the drag and drop button label.
     *
     * @return the drag and drop button label
     */
    public String getDragAndDropButtonLabel() {
        return dragAndDropButtonLabel;
    }

    /**
     * Gets the drag and drop remove file label.
     *
     * @return the drag and drop remove file label
     */
    public String getDragAndDropRemoveFileLabel() {
        return dragAndDropRemoveFileLabel;
    }

    /**
     * Gets the details title.
     *
     * @return the details title
     */
    public String getDetailsTitle() { return detailsTitle; }

    /**
     * Gets the country label.
     *
     * @return the country label
     */
    public String getCountryLabel() { return countryLabel; }

    /**
     * Gets the site label.
     *
     * @return the site label
     */
    public String getSiteLabel() { return siteLabel; }

    /**
     * Gets the line label.
     *
     * @return the line label
     */
    public String getLineLabel() { return lineLabel; }

    /**
     * Gets the position label.
     *
     * @return the position label
     */
    public String getPositionLabel() { return positionLabel; }

    /**
     * Gets the equipment status label.
     *
     * @return the equipment status label
     */
    public String getEquipmentStatusLabel() { return equipmentStatusLabel; }

    /**
     * Gets the machine system label.
     *
     * @return the machine system label
     */
    public String getMachineSystemLabel() { return machineSystemLabel; }

    /**
     * Gets the equipment description label.
     *
     * @return the equipment description label
     */
    public String getEquipmentDescriptionLabel() { return equipmentDescriptionLabel; }

    /**
     * Gets the manufacture model number label.
     *
     * @return the manufacture model number label.
     */
    public String getManufactureModelNumberLabel() { return manufactureModelNumberLabel; }

    /**
     * Gets the manufacture of asset label.
     *
     * @return the manufacture of asset label
     */
    public String getManufactureOfAssetLabel() { return manufactureOfAssetLabel; }

    /**
     * Gets the country of manufacture label.
     *
     * @return the country of manufacture label
     */
    public String getCountryOfManufactureLabel() { return countryOfManufactureLabel; }

    /**
     * Gets the construction year label.
     *
     * @return the construction year label
     */
    public String getConstructionYearLabel() { return constructionYearLabel; }

    /**
     * Gets the comments label.
     *
     * @return the comments label
     */
    public String getCommentsLabel() { return commentsLabel; }

    /**
     * Gets the submit button label.
     *
     * @return the submit button label
     */
    public String getSubmitButtonLabel() { return submitButtonLabel; }

    /**
     * Gets the dropdown placeholder.
     *
     * @return the dropdown placeholder
     */
    public String getDropdownPlaceholder() { return dropdownPlaceholder; }

    /**
     * Gets the submit title.
     *
     * @return the submit title
     */
    public String getSubmitTitle() { return submitTitle; }

    /**
     * Gets the submit subtitle.
     *
     * @return the submit subtitle
     */
    public String getSubmitSubtitle() { return submitSubtitle; }

    /**
     * Gets the add another equipment label.
     *
     * @return the add another equipment label
     */
    public String getAddAnotherEquipmentLabel() { return addAnotherEquipmentLabel; }

    /**
     * Gets the serial number error msg.
     *
     * @return the serial number error msg
     */
    public String getSerialNumberErrorMsg() { return serialNumberErrorMsg; }

    /**
     * Gets the country error msg.
     *
     * @return the country error msg
     */
    public String getCountryErrorMsg() { return countryErrorMsg; }

    /**
     * Gets the site error msg.
     *
     * @return the site error msg
     */
    public String getSiteErrorMsg() { return siteErrorMsg; }

    /**
     * Gets the line error msg.
     *
     * @return the line error msg
     */
    public String getLineErrorMsg() { return lineErrorMsg; }

    /**
     * Gets the position error msg.
     *
     * @return the position error msg
     */
    public String getPositionErrorMsg() { return positionErrorMsg; }

    /**
     * Gets the equipment status error msg.
     *
     * @return the equipment status error msg
     */
    public String getEquipmentStatusErrorMsg() { return equipmentStatusErrorMsg; }

    /**
     * Gets the equipment description error msg.
     *
     * @return the equipment description error msg
     */
    public String getEquipmentDescriptionErrorMsg() { return equipmentDescriptionErrorMsg; }

    /**
     * Gets the manufacture of asset error msg.
     *
     * @return the manufacture of asset error msg
     */
    public String getManufactureOfAssetErrorMsg() { return manufactureOfAssetErrorMsg; }

    /**
     * Gets the country of manufacture error msg.
     *
     * @return the country of manufacture error msg
     */
    public String getCountryOfManufactureErrorMsg() { return countryOfManufactureErrorMsg; }

    /**
     * Gets the construction year error msg.
     *
     * @return the construction year error msg
     */
    public String getConstructionYearErrorMsg() { return constructionYearErrorMsg; }

    /**
     * Gets the file extension error msg.
     *
     * @return the file extension error msg
     */
    public String getFileExtensionErrorMsg() { return fileExtensionErrorMsg; }

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
     * Gets String subTitle from content reference.
     *
     * @return subTitle.
     */
    public String getSubTitle() {
        return resolveDescriptionFromReference(subTitleReference);
    }

    /**
     * Gets String detailsSubTitle from content reference.
     *
     * @return detailsSubTitle.
     */
    public String getDetailsSubtitle() {
        return resolveDescriptionFromReference(detailsSubtitleReference);
    }

    private String resolveDescriptionFromReference(String referencePath) {
        if (StringUtils.isNotBlank(referencePath)) {
            GenericDescription description = resource.getResourceResolver().getResource(referencePath)
                    .adaptTo(GenericDescription.class);
            if(description != null) {
                return description.getText();
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * init method.
     */
    @PostConstruct
    protected void init() {
        Map<String, Object> i18KeyMap = new HashMap<>();
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_TITLE, getTitle());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_SERIAL_NUMBER_LABEL, getSerialNumberLabel());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_DRAG_AND_DROP_DESCRIPTION, getDragAndDropDescription());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_DRAG_AND_DROP_TITLE, getDragAndDropTitle());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_DRAG_AND_DROP_SUBTITLE, getDragAndDropSubtitle());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_FILE_FORMAT_SUBTITLE, getFileFormatSubtitle());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_DRAG_AND_DROP_BUTTON, getDragAndDropButtonLabel());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_DRAG_AND_DROP_REMOVE_FILE_LABEL, getDragAndDropRemoveFileLabel());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_DETAILS_TITLE, getDetailsTitle());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_COUNTRY_LABEL, getCountryLabel());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_SITE_LABEL, getSiteLabel());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_LINE_LABEL, getLineLabel());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_POSITION_LABEL, getPositionLabel());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_EQUIPMENT_STATUS_LABEL, getEquipmentStatusLabel());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_MACHINE_SYSTEM_LABEL, getMachineSystemLabel());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_EQUIPMENT_DESCRIPTION_LABEL, getEquipmentDescriptionLabel());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_MANUFACTURE_MODEL_NUMBER_LABEL, getManufactureModelNumberLabel());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_MANUFACTURE_OF_ASSET_LABEL, getManufactureOfAssetLabel());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_COUNTRY_OF_MANUFACTURE_LABEL, getCountryOfManufactureLabel());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_CONTRUCTION_YEAR_LABEL, getConstructionYearLabel());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_COMMENTS_LABEL, getCommentsLabel());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_SUBMIT_BUTTON_LABEL, getSubmitButtonLabel());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_DROPDOWN_PLACEHOLDER, getDropdownPlaceholder());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_SUBMIT_TITLE, getSubmitTitle());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_SUBMIT_SUBTITLE, getSubmitSubtitle());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_ADD_ANOTHER_EQUIPMENT_LABEL, getAddAnotherEquipmentLabel());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_SERIAL_NUMBER_ERROR_MSG, getSerialNumberErrorMsg());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_COUNTRY_ERROR_MSG, getCountryErrorMsg());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_SITE_ERROR_MSG, getSiteErrorMsg());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_LINE_ERROR_MSG, getLineErrorMsg());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_POSITION_ERROR_MSG, getPositionErrorMsg());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_EQUIPMENT_STATUS_ERROR_MSG, getEquipmentStatusErrorMsg());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_EQUIPMENT_DESCRIPTION_ERROR_MSG, getEquipmentDescriptionErrorMsg());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_MANUFACTURE_OF_ASSET_ERROR_MSG, getManufactureOfAssetErrorMsg());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_COUNTRY_OF_MANUFACTURE_ERROR_MSG, getCountryOfManufactureErrorMsg());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_CONTRUCTION_YEAR_ERROR_MSG, getConstructionYearErrorMsg());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_FILE_EXTENSION_ERROR_MSG, getFileExtensionErrorMsg());

        Gson gson = new Gson();
        i18nKeys = gson.toJson(i18KeyMap);

        if (slingSettingsService.getRunModes().contains("publish")) {
            isPublishEnvironment = Boolean.TRUE;
        }

        countryApi = service.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR + GlobalUtil
                .getSelectedApiMapping(service, CustomerHubConstants.EQUIPMENT_COUNTRYLIST_API);

        allCountriesApi = service.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR + GlobalUtil
                .getSelectedApiMapping(service, CustomerHubConstants.ALL_COUNTRY_LIST_API);

        statusApi = service.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR + GlobalUtil
                .getSelectedApiMapping(service, CustomerHubConstants.EQUIPMENT_USERSTATUSLIST_API);

        siteApi = service.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR + GlobalUtil
                .getSelectedApiMapping(service, CustomerHubConstants.EQUIPMENT_SITESLIST_API);

        lineApi = service.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR + GlobalUtil
                .getSelectedApiMapping(service, CustomerHubConstants.EQUIPMENT_LINESLIST_API);
    }

}
