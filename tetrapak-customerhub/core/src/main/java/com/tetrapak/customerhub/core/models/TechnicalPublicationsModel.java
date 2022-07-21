package com.tetrapak.customerhub.core.models;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.settings.SlingSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TechnicalPublicationsModel {

    /* The Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(TechnicalPublicationsModel.class);

    /** The APIGEE service. */
    @OSGiService
    private APIGEEService service;

    /** The sling settings service. */
    @OSGiService
    private SlingSettingsService slingSettingsService;

    /** The publish environment. */
    private boolean isPublishEnvironment = Boolean.FALSE;

    /** The Countries API. */
    private String allCountriesApi;

    /** The Equipment List API. */
    private String equipmentListApi;

    /** The Line API. */
    private String lineListApi;

    /** The Customers API List API. */
    private String customerListApi;

    /** The Technical Publications API. */
    private String technicalPublicationsApi;

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

    }

    public boolean isPublishEnvironment() {
	return isPublishEnvironment;
    }

    public String getAllCountriesApi() {
	return allCountriesApi;
    }

    public String getEquipmentListApi() {
	return equipmentListApi;
    }

    public String getLineListApi() {
	return lineListApi;
    }

    public String getCustomerListApi() {
	return customerListApi;
    }

    public String getTechnicalPublicationsApi() {
	return technicalPublicationsApi;
    }

}
