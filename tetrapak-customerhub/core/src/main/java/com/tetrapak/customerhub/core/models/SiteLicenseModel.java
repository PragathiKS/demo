package com.tetrapak.customerhub.core.models;

import com.google.gson.annotations.Expose;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;

/**
 * Model class for Site licenses child node of PlantMaster Licenses component.
 */
@Model(adaptables = {
        Resource.class, SlingHttpServletRequest.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SiteLicenseModel {
    
    public static final String NAME_OF_SITE_KEY = "nameOfSite";
    public static final String LOCATION_OF_SITE_KEY = "locationOfSite";
    public static final String APPLICATION_KEY = "application";
    public static final String PLC_TYPE_KEY = "plcType";
    public static final String HMI_TYPE_KEY = "hmiType";
    public static final String MES_TYPE_KEY = "mesType";
    public static final String NUMBER_OF_BASIC_UNIT_KEY = "numberOfBasicUnit";
    public static final String NUMBER_OF_ADVANCED_UNIT_KEY = "numberOfAdvancedUnit";

    @SlingObject
    private Resource resource;
    
    @SlingObject
    private SlingHttpServletRequest request;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String title;

    @ValueMapValue
    @Expose(serialize = true)
    private String description;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String nameOfSite;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String locationOfSite;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String application;
    
    @Expose(serialize = true)
    @ChildResource
    private List<SiteLicenseApplication> applicationDropdown;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String applicationDropdownPlaceholder;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String plcType;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String hmiType;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String mesType;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String numberOfBasicUnit;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String basicUnitToolTipHeading;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String basicUnitToolTipDescription;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String numberOfAdvancedUnit;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String advancedUnitToolTipHeading;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String advancedUnitToolTipDescription;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String submitButtonLabel;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String successMessageHeading;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String successMessageDescription;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String impInformationTitle;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String impInformationDescription;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String siteLicensesDescriptionHeading;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String inputFieldError;
	
	@ValueMapValue
    @Expose(serialize = true)
    private String numberFieldError;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String selectFieldError;
    
    @ValueMapValue
    private String subject;
    
    @ValueMapValue
    private String salutation;
    
    @ValueMapValue
    private String body;

    @ValueMapValue
    private String plcTypeEmail;

    @ValueMapValue
    private String hmiTypeEmail;

    @ValueMapValue
    private String mesTypeEmail;
    
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
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
    
    public String getSuccessMessageHeading() {
        return successMessageHeading;
    }
    
    public String getSuccessMessageDescription() {
        return successMessageDescription;
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
    
    public String getApplication() {
        return application;
    }
    
    public String getInputFieldError() {
        return inputFieldError;
    }

    public String getNumberFieldError() {
        return numberFieldError;
    }
    
    public String getSelectFieldError() {
        return selectFieldError;
    }
    public String getPlcTypeEmail() {
        return plcTypeEmail;
    }

    public String getHmiTypeEmail() {
        return hmiTypeEmail;
    }

    public String getMesTypeEmail() {
        return mesTypeEmail;
    }
}
