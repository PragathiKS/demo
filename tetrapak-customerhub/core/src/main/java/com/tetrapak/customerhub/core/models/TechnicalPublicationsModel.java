package com.tetrapak.customerhub.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.settings.SlingSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TechnicalPublicationsModel {

    /* The Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(TechnicalPublicationsModel.class);

    /** The Countries API. */
    private String allCountriesApi;

    /** The Customers API List API. */
    private String customerListApi;

    /** The Equipment List API. */
    private String equipmentListApi;

    /** The i18n Keys */
    private String i18nKeys;

    /** The publish environment. */
    private boolean isPublishEnvironment = Boolean.FALSE;

    /** The Line API. */
    private String lineListApi;

    private String locale;

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    @Inject
    private Resource resource;

    /** The APIGEE service. */
    @OSGiService
    private APIGEEService service;

    /** The sling settings service. */
    @OSGiService
    private SlingSettingsService slingSettingsService;

    /** The Technical Publications API. */
    private String technicalPublicationsApi;

    private String title;
    
    private TechnicalPublicationsKeysModel i18nKeysModel;

    @PostConstruct
    protected void init() {

	if (slingSettingsService.getRunModes().contains("publish")) {
	    isPublishEnvironment = Boolean.TRUE;
	}
	LOGGER.debug("isPublishEnvironment: {}", isPublishEnvironment);

	allCountriesApi = service.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR
		+ GlobalUtil.getSelectedApiMapping(service, CustomerHubConstants.ALL_COUNTRY_LIST_API);
	LOGGER.debug("allCountriesApi: {}", allCountriesApi);

	equipmentListApi = service.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR
		+ GlobalUtil.getSelectedApiMapping(service, CustomerHubConstants.EQUIPMENT_LIST_API);
	LOGGER.debug("equipmentListApi: {}", equipmentListApi);

	lineListApi = service.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR
		+ GlobalUtil.getSelectedApiMapping(service, CustomerHubConstants.EQUIPMENT_LINESLIST_API);
	LOGGER.debug("lineListApi: {}", lineListApi);

	customerListApi = service.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR
		+ GlobalUtil.getSelectedApiMapping(service, CustomerHubConstants.EQUIPMENT_CUSTOMERLIST_API);
	LOGGER.debug("customerListApi: {}", customerListApi);

	technicalPublicationsApi = service.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR
		+ GlobalUtil.getSelectedApiMapping(service, CustomerHubConstants.TECHNICAL_PUBLICATIONS_API);
	LOGGER.debug("technicalPublicationsApi: {}", technicalPublicationsApi);

	Gson gson = new Gson();
	i18nKeysModel = resource.adaptTo(TechnicalPublicationsKeysModel.class);
	i18nKeys = gson.toJson(i18nKeysModel, TechnicalPublicationsKeysModel.class);
	LOGGER.debug("i18nKeys {}:", i18nKeys);

	title = i18nKeysModel.getTechnicalPublications();

	locale = GlobalUtil.getLanguage(request);

    }

    public String getAllCountriesApi() {
	return allCountriesApi;
    }

    public String getCustomerListApi() {
	return customerListApi;
    }

    public String getEquipmentListApi() {
	return equipmentListApi;
    }

    public String getI18nKeys() {
	return i18nKeys;
    }

    public String getLineListApi() {
	return lineListApi;
    }

    public String getLocale() {
	return locale;
    }

    public String getTechnicalPublicationsApi() {
	return technicalPublicationsApi;
    }

    public String getTitle() {
	return title;
    }

    public boolean isPublishEnvironment() {
	return isPublishEnvironment;
    }
    
    public TechnicalPublicationsKeysModel getI18nKeysModel() {
        return i18nKeysModel;
    }

}
