package com.tetrapak.customerhub.core.models;

import com.day.cq.commons.Externalizer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.AIPCategoryService;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.servlets.PlantMasterLicensesEmailServlet;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Model class for Plant Master licenses component.
 */
@Model(adaptables = {
        Resource.class, SlingHttpServletRequest.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PlantMasterLicensesModel {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PlantMasterLicensesModel.class);
    private static final String ENGINEERING_LICENSE_CHILD_RESOURCE_NAME = "engineeringLicense";
    private static final String SITE_LICENSE_CHILD_RESOURCE_NAME = "siteLicense";
    public static final String EMAIL_USERNAME = "username";
    public static final String EMAIL_ADDRESS = "emailaddress";
    public static final String EMAIL_SALUTATION = "salutation";
    public static final String EMAIL_SUBJECT = "subject";
    public static final String EMAIL_BODY = "body";
    public static final String GROUP_SERVLET_URL= "/bin/customerhub/plant-master-groups";
    
    /** The resource. */
    @SlingObject
    private Resource resource;
    
    @SlingObject
    private SlingHttpServletRequest request;
    
    @ChildResource(name = ENGINEERING_LICENSE_CHILD_RESOURCE_NAME)
    private EngineeringLicenseModel engineeringLicenseModel;
    
    @ChildResource(name = SITE_LICENSE_CHILD_RESOURCE_NAME)
    private SiteLicenseModel siteLicenseModel;
    
    @ValueMapValue
    private String username;
    
    @ValueMapValue
    private String emailaddress;
    
    @ValueMapValue
    private String heading;
    
    private String i18nKeys;
    
    private String emailApiUrl;
    
    private String engineeringLicenseApiUrl;
    
    private String siteLicenseApiUrl;
    
    private String userNameValue;
    
    private String emailAddressValue;
    
    private Map<String, Object> i18nKeysMap;
    
    private boolean isPublishEnvironment = Boolean.FALSE;
    
    /** The apigee service. */
    @OSGiService
    private APIGEEService apigeeService;
    
    /** The aip category service. */
    @OSGiService
    private AIPCategoryService aipCategoryService;
    
    @OSGiService
    private SlingSettingsService slingSettingsService;
    
    /**
     * init method.
     */
    @PostConstruct
    protected void init() {
        
        i18nKeysMap = new HashMap<>();
        i18nKeysMap.put(ENGINEERING_LICENSE_CHILD_RESOURCE_NAME, engineeringLicenseModel);
        i18nKeysMap.put(SITE_LICENSE_CHILD_RESOURCE_NAME, siteLicenseModel);
        
        Map<String, Object> i18KeyMap = new HashMap<>();
        i18KeyMap.put(ENGINEERING_LICENSE_CHILD_RESOURCE_NAME, engineeringLicenseModel);
        i18KeyMap.put(SITE_LICENSE_CHILD_RESOURCE_NAME, siteLicenseModel);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        i18nKeys = gson.toJson(i18KeyMap);
        LOGGER.debug("i18nKeys : {}", i18nKeys);
        
        String componentPath = resource.getResourceResolver().map(this.resource.getPath());
        String componentPathExtension = CustomerHubConstants.DOT
                + PlantMasterLicensesEmailServlet.SLING_SERVLET_SELECTOR + CustomerHubConstants.DOT
                + PlantMasterLicensesEmailServlet.SLING_SERVLET_EXTENSION;
        this.emailApiUrl = componentPath + componentPathExtension;
        
        if (slingSettingsService.getRunModes().contains(Externalizer.PUBLISH)) {
            isPublishEnvironment = Boolean.TRUE;
            String apiMapping = GlobalUtil.getSelectedApiMapping(apigeeService,
                    CustomerHubConstants.AIP_PRODUCT_DETAILS_API);
            this.engineeringLicenseApiUrl = GlobalUtil.getAIPEndpointURL(apigeeService.getApigeeServiceUrl(),
                    apiMapping, aipCategoryService.getEngineeringLicensesId());
            this.siteLicenseApiUrl = GlobalUtil.getAIPEndpointURL(apigeeService.getApigeeServiceUrl(), apiMapping,
                    aipCategoryService.getSiteLicensesId());
        }
        this.setEmailAddressValue();
        this.setUserNameValue();
        
    }
    
    public void setEmailAddressValue() {
        this.emailAddressValue = GlobalUtil.getCustomerEmailAddress(this.request);
    }

    public void setUserNameValue() {
        if (Objects.nonNull(request.getCookie(CustomerHubConstants.CUSTOMER_COOKIE_NAME))) {
            this.userNameValue = URLDecoder.decode(
                    request.getCookie(CustomerHubConstants.CUSTOMER_COOKIE_NAME).getValue(), StandardCharsets.UTF_8);
        }
    }
    
    public String getI18nKeys() {
        return i18nKeys;
    }
    
    public EngineeringLicenseModel getEngineeringLicenseModel() {
        return engineeringLicenseModel;
    }
    
    public SiteLicenseModel getSiteLicenseModel() {
        return siteLicenseModel;
    }
    
    public String getEmailApiUrl() {
        return emailApiUrl;
    }
    
    public String getEngineeringLicenseApiUrl() {
        return engineeringLicenseApiUrl;
    }
    
    public String getSiteLicenseApiUrl() {
        return siteLicenseApiUrl;
    }
    
    public String getUsername() {
        return username;
    }

    public String getEmailAddressValue() {
        return StringUtils.defaultIfEmpty(emailAddressValue, StringUtils.EMPTY);
    }
    
    public Map<String, Object> getI18nKeysMap() {
        return i18nKeysMap;
    }
    
    public String getHeading() {
        return heading;
    }
    
    public boolean isPublishEnvironment() {
        return isPublishEnvironment;
    }
    
    public String getEmailaddress() {
        return emailaddress;
    }

    public String getUserNameValue() {
        return StringUtils.defaultIfEmpty(userNameValue, StringUtils.EMPTY);
    }

    public String getGroupServletUrl() {
        return GROUP_SERVLET_URL;
    }
}
