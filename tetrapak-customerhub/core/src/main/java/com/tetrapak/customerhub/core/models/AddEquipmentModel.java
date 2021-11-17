package com.tetrapak.customerhub.core.models;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

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

    /** The title label. */
    @Inject
    private String title;

    /** The subtitle label. */
    @Inject
    private String subTitle;

    /** The serial number label. */
    @Inject
    private String serialNumber;

    /** The drag and drop description. */
    @Inject
    private String dragAndDropDescription;

    /** The drag and drop title. */
    @Inject
    private String dragAndDropTitle;

    /** The drag and drop subtitle. */
    @Inject
    private String dragAndDropSubtitle;

    /** The drag and drop button label. */
    @Inject
    private String dragAndDropButtonLabel;

    /** The drag and drop remove file label. */
    @Inject
    private String dragAndDropRemoveFileLabel;

    /** The details title. */
    @Inject
    private String detailsTitle;

    /** The details subtitle. */
    @Inject
    private String detailsSubtitle;

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
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the subtitle.
     *
     * @return the subtitle
     */
    public String getSubTitle() {
        return subTitle;
    }


    /**
     * Gets the serial number label.
     *
     * @return the serial number label
     */
    public String getSerialNumber() {
        return serialNumber;
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
     * @return the drag and drop sybtitle
     */
    public String getDragAndDropSubtitle() {
        return dragAndDropSubtitle;
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
     * Gets the details subtitle.
     *
     * @return the details subtitle
     */
    public String getDetailsSubtitle() { return detailsSubtitle; }

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
     * Gets the details title.
     *
     * @return the details title
     */
    public String getManufactureModelNumberLabel() { return manufactureModelNumberLabel; }

    /**
     * Gets the details title.
     *
     * @return the details title
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
     * init method.
     */
    @PostConstruct
    protected void init() {
        Map<String, Object> i18KeyMap = new HashMap<>();
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_TITLE, getTitle());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_SUBTITLE, getSubTitle());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_SERIAL_NUMBER, getSerialNumber());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_DRAG_AND_DROP_DESCRIPTION, getDragAndDropDescription());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_DRAG_AND_DROP_TITLE, getDragAndDropTitle());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_DRAG_AND_DROP_SUBTITLE, getDragAndDropSubtitle());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_DRAG_AND_DROP_BUTTON, getDragAndDropButtonLabel());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_DRAG_AND_DROP_REMOVE_FILE_LABEL, getDragAndDropRemoveFileLabel());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_DETAILS_TITLE, getDetailsTitle());
        i18KeyMap.put(CustomerHubConstants.NEW_EQUIPMENT_DETAILS_SUBTITLE, getDetailsSubtitle());
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

        Gson gson = new Gson();
        i18nKeys = gson.toJson(i18KeyMap);
    }
}
