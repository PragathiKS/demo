package com.tetrapak.customerhub.core.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

@Model(adaptables = { Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SiteLicenseModel {
    private static final Logger LOGGER = LoggerFactory.getLogger(SiteLicenseModel.class);

    public static final String TITLE_JSON_KEY = "title";
    public static final String NAME_OF_SITE_JSON_KEY = "nameOfSite";
    public static final String LOCATION_OF_SITE_JSON_KEY = "locationOfSite";
    public static final String APPLICATION_JSON_KEY = "application";
    public static final String APPLICATION_DROPDOWN_JSON_KEY = "applicationDropDown";
    public static final String APPLICATION_DROPDOWN_PLACEHOLDER_JSON_KEY = "dropdownPlaceholder";
    public static final String PLC_TYPE_JSON_KEY = "plcType";
    public static final String HMI_TYPE_JSON_KEY = "HMIType";
    public static final String MES_TYPE_JSON_KEY = "mesType";
    public static final String NUMBER_OF_BASIC_UNIT_JSON_KEY = "numberOfBasicUnit";
    public static final String BASIC_UNIT_TOOLTIP_HEADING_JSON_KEY = "basicUnitToolTipHeading";
    public static final String BASIC_UNIT_TOOLTIP_DESCRIPTION_JSON_KEY = "basicUnitToolTipDescription";
    public static final String NUMBER_OF_ADVANCED_UNIT_JSON_KEY = "numberOfAdvancedUnit";
    public static final String ADVANCED_UNIT_TOOLTIP_HEADING_JSON_KEY = "advancedUnitToolTipHeading";
    public static final String ADVANCED_UNIT_TOOLTIP_DESCRIPTION_JSON_KEY = "advancedUnitToolTipDescription";
    public static final String SUBMIT_BUTTON_LABEL_JSON_KEY = "submitButtonLabel";
    public static final String SUCCESS_MESSAGE_JSON_KEY = "successMessage";
    public static final String IMPORTANT_INFORMATION_TITLE_JSON_KEY = "impInformationTitle";
    public static final String IMPORTANT_INFORMATION_DESCRIPTION_JSON_KEY = "impInformationDescription";
    public static final String SITE_LICENSE_DESCRIPTION_HEADING_JSON_KEY = "siteLicensesDescriptionHeading";

    /** The resource. */
    @SlingObject
    private Resource resource;

    @SlingObject
    private SlingHttpServletRequest request;

    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(TITLE_JSON_KEY)
    private String title;

    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(NAME_OF_SITE_JSON_KEY)
    private String nameOfSite;

    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(LOCATION_OF_SITE_JSON_KEY)
    private String locationOfSite;

    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(APPLICATION_JSON_KEY)
    private String application;

    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(APPLICATION_DROPDOWN_JSON_KEY)
    @Inject @Via("resource")
    private List<SiteLicenseApplication> applicationDropdown;

    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(APPLICATION_DROPDOWN_PLACEHOLDER_JSON_KEY)
    private String applicationDropdownPlaceholder;

    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(PLC_TYPE_JSON_KEY)
    private String plcType;

    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(HMI_TYPE_JSON_KEY)
    private String hmiType;

    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(MES_TYPE_JSON_KEY)
    private String mesType;

    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(NUMBER_OF_BASIC_UNIT_JSON_KEY)
    private String numberOfBasicUnit;

    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(BASIC_UNIT_TOOLTIP_HEADING_JSON_KEY)
    private String basicUnitToolTipHeading;

    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(BASIC_UNIT_TOOLTIP_DESCRIPTION_JSON_KEY)
    private String basicUnitToolTipDescription ;

    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(NUMBER_OF_ADVANCED_UNIT_JSON_KEY)
    private String numberOfAdvancedUnit;

    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(ADVANCED_UNIT_TOOLTIP_HEADING_JSON_KEY)
    private String advancedUnitToolTipHeading;

    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(ADVANCED_UNIT_TOOLTIP_DESCRIPTION_JSON_KEY)
    private String advancedUnitToolTipDescription;

    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(SUBMIT_BUTTON_LABEL_JSON_KEY)
    private String submitButtonLabel;

    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(SUCCESS_MESSAGE_JSON_KEY)
    private String successMessage;

    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(IMPORTANT_INFORMATION_TITLE_JSON_KEY)
    private String impInformationTitle;

    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(IMPORTANT_INFORMATION_DESCRIPTION_JSON_KEY)
    private String impInformationDescription;

    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(SITE_LICENSE_DESCRIPTION_HEADING_JSON_KEY)
    private String siteLicensesDescriptionHeading;

    @ValueMapValue
    private String subject;

    @ValueMapValue
    private String salutation;

    @ValueMapValue
    private String body;

    @ValueMapValue
    private String name;

    @ValueMapValue
    private String email;

    public String getTitle() {
        return title;
    }

    public String getNameOfSite() {
        return nameOfSite;
    }

    public String getLocationOfSite() {
        return locationOfSite;
    }

    public List<SiteLicenseApplication> getApplicationDropdown() {
        return applicationDropdown;
    }

    public String getApplicationDropdownPlaceholder() {
        return applicationDropdownPlaceholder;
    }

    public String getPlcType() {
        return plcType;
    }

    public String getHmiType() {
        return hmiType;
    }

    public String getMesType() {
        return mesType;
    }

    public String getNumberOfBasicUnit() {
        return numberOfBasicUnit;
    }

    public String getBasicUnitToolTipHeading() {
        return basicUnitToolTipHeading;
    }

    public String getBasicUnitToolTipDescription() {
        return basicUnitToolTipDescription;
    }

    public String getNumberOfAdvancedUnit() {
        return numberOfAdvancedUnit;
    }

    public String getAdvancedUnitToolTipHeading() {
        return advancedUnitToolTipHeading;
    }

    public String getAdvancedUnitToolTipDescription() {
        return advancedUnitToolTipDescription;
    }

    public String getSubmitButtonLabel() {
        return submitButtonLabel;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public String getImpInformationTitle() {
        return impInformationTitle;
    }

    public String getImpInformationDescription() {
        return impInformationDescription;
    }

    public String getSiteLicensesDescriptionHeading() {
        return siteLicensesDescriptionHeading;
    }

    public String getSubject() {
        return subject;
    }

    public String getSalutation() {
        return salutation;
    }

    public String getBody() {
        return body;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getApplication() {
        return application;
    }
}
