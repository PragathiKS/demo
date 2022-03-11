package com.tetrapak.customerhub.core.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.AIPCategoryService;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.servlets.PlantMasterLicensesEmailServlet;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Model(adaptables = { Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PlantMasterLicensesModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlantMasterLicensesModel.class);
    private static final String ENGINEERING_LICENSE_CHILD_RESOURCE_NAME = "engineeringLicense";
    private static final String SITE_LICENSE_CHILD_RESOURCE_NAME = "siteLicense";
    private static final String PLANT_MASTER_SITE_LICENSE_API_MAPPING_KEY = "";
    private static final String PLANT_MASTER_ENGINEERING_LICENSE_API_MAPPING_KEY = "";


    /** The resource. */
    @SlingObject
    private Resource resource;

    @SlingObject
    private SlingHttpServletRequest request;

    @ChildResource(name = ENGINEERING_LICENSE_CHILD_RESOURCE_NAME)
    EngineeringLicenseModel engineeringLicenseModel;

    @ChildResource(name = SITE_LICENSE_CHILD_RESOURCE_NAME)
    SiteLicenseModel siteLicenseModel;

    private String i18nKeys;

    private String emailApiUrl;

    private String engineeringLicenseApiUrl;

    private String siteLicenseApiUrl;

    private String userName;

    private String userEmailAddress;

    private Map<String, Object> i18nKeysMap;

    /** The apigee service. */
    @OSGiService
    private APIGEEService apigeeService;

    /** The aip category service. */
    @OSGiService
    private AIPCategoryService aipCategoryService;

    /**
     * init method.
     */
    @PostConstruct
    protected void init() {

        i18nKeysMap = new HashMap<>();
        i18nKeysMap.put(ENGINEERING_LICENSE_CHILD_RESOURCE_NAME, engineeringLicenseModel);
        i18nKeysMap.put(SITE_LICENSE_CHILD_RESOURCE_NAME,siteLicenseModel);

        Map<String, Object> i18KeyMap = new HashMap<>();
        i18KeyMap.put(ENGINEERING_LICENSE_CHILD_RESOURCE_NAME, engineeringLicenseModel);
        i18KeyMap.put(SITE_LICENSE_CHILD_RESOURCE_NAME,siteLicenseModel);
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        i18nKeys = gson.toJson(i18KeyMap);
        LOGGER.debug("i18nKeys : {}",i18nKeys);

        String componentPath = resource.getResourceResolver().map(this.resource.getPath());
        String componentPathExtension = CustomerHubConstants.DOT + PlantMasterLicensesEmailServlet.SLING_SERVLET_SELECTOR
                + CustomerHubConstants.DOT + PlantMasterLicensesEmailServlet.SLING_SERVLET_EXTENSION;
        this.emailApiUrl = componentPath + componentPathExtension;

        String apiMapping = GlobalUtil.getSelectedApiMapping(apigeeService,
                CustomerHubConstants.AIP_PRODUCT_DETAILS_API);
        engineeringLicenseApiUrl = GlobalUtil.getAIPEndpointURL(apigeeService.getApigeeServiceUrl(), apiMapping,
                aipCategoryService.getEngineeringLicensesId());
        this.siteLicenseApiUrl = GlobalUtil.getAIPEndpointURL(apigeeService.getApigeeServiceUrl(), apiMapping,
                aipCategoryService.getSiteLicensesId());
        LOGGER.debug(this.siteLicenseApiUrl);
        LOGGER.debug(this.engineeringLicenseApiUrl);

    }

    public void setUserEmailAddress() {
        this.userEmailAddress = GlobalUtil.getCustomerEmailAddress(this.request);
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

    public String getUserName() {
        return userName;
    }

    public String getUserEmailAddress() {
        return userEmailAddress;
    }

    public Map<String, Object> getI18nKeysMap() {
        return i18nKeysMap;
    }
}
