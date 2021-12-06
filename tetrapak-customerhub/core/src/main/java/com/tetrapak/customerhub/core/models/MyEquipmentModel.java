package com.tetrapak.customerhub.core.models;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;

import com.tetrapak.customerhub.core.utils.LinkUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;
import com.google.gson.Gson;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;

/**
 * The Class MyEquipmentModel.
 *
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MyEquipmentModel {

	public static final String EQUIPMENT_DETAILS_PATH = "/content/tetrapak/customerhub/global/en/equipment-details";

	/** The resource. */
	@Self
	private Resource resource;
	
	/** The country. */
	@ValueMapValue
	private String country;
	
	/** The line. */
	@ValueMapValue
	private String line;
	
	/** The search results. */
	@ValueMapValue
	private String searchResults;
	
	/** The site. */
	@ValueMapValue
	private String site;

	/** The customer. */
	@ValueMapValue
	private String customer;
	
	/** The equipment description. */
	@ValueMapValue
	private String equipmentDescription;
	
	/** The serial number. */
	@ValueMapValue
	private String serialNumber;
	
	/** The equipment status. */
	@ValueMapValue
	private String equipmentStatus;

	/** The equipment type. */
	@ValueMapValue
	private String equipmentType;
	
	/** The my equipment. */
	@ValueMapValue
	private String myEquipment;
	
	/** The apply filter. */
	@ValueMapValue
	private String applyFilter;
	
	/** The remove all. */
	@ValueMapValue
	private String removeAll;
	
	/** The first. */
	@ValueMapValue
	private String first;
	
	/** The last. */
	@ValueMapValue
	private String last;
	
	/** The customize table. */
	@ValueMapValue
	private String customizeTable;
	
	/** The equip stat tool tip. */
	@ValueMapValue
	private String equipStatToolTip;
	
	/** The country tool tip. */
	@ValueMapValue
	private String countryToolTip;

	/** The site tool tip. */
	@ValueMapValue
	private String siteToolTip;
	
	/** The line tool tip. */
	@ValueMapValue
	private String lineToolTip;
	
	/** The equip desc tool tip. */
	@ValueMapValue
	private String equipDescToolTip;
	
	/** The serial num tool tip. */
	@ValueMapValue
	private String serialNumToolTip;

	/** The functional location. */
	@ValueMapValue
	private String functionalLocation;

	/** The functional location tool tip. */
	@ValueMapValue
	private String functionalLocationToolTip;

	/** The site description. */
	@ValueMapValue
	private String siteDescription;

	/** The site description tool tip. */
	@ValueMapValue
	private String siteDescToolTip;

	/** The location. */
	@ValueMapValue
	private String location;

	/** The location tool tip. */
	@ValueMapValue
	private String locationToolTip;

	/** The sling settings service. */
	@OSGiService
    private SlingSettingsService slingSettingsService;
	
	/** The is publish environment. */
	private boolean isPublishEnvironment= Boolean.FALSE;

	/** The i18n keys. */
	private String i18nKeys;
	
	/** The tool tip class. */
	private String toolTipClass;
	
	/** The country api. */
	@ValueMapValue
	private String countryApi;
	
    /** The equipment list api. */
    @ValueMapValue
    private String equipmentListApi;

	/** The service. */
	@OSGiService
	private APIGEEService service;
	
	/** The hide And Show Cta.*/
	@ValueMapValue
	private String hideAndShowCta;

	/** The no Data Found.*/
	@ValueMapValue
	private String noDataFound;

	/** The permanent volume conversion.*/
	@ValueMapValue
	private String permanentVolumeConversion;
	
	/** The show all filters. */
	@ValueMapValue
    private String showAllFilters;
		
	/** The hide all filters. */
	@ValueMapValue
    private String hideAllFilters;
	
	/** The remove all filters. */
	@ValueMapValue
    private String removeAllFilters;
	
	/** The download excel servlet url. */
	private String downloadExcelServletUrl;

	/** The add new equipment label. */
	@ValueMapValue
	private String addNewEquipmentLabel;

	/** The add new equipment Url. */
	@ValueMapValue
	private String addNewEquipmentUrl;

	/**
	 * Gets the noDataFound.
	 *
	 * @return the noDataFound
	 */
	public String getNoDataFound() {
		return noDataFound;
	}

	/**
	 * Gets the Permanent Volume Conversion.
	 *
	 * @return the permanentVolumeConversion
	 */
	public String getPermanentVolumeConversion() {
		return permanentVolumeConversion;
	}

	/**
	 * Gets the hideAndShowCta.
	 *
	 * @return the hideAndShowCta
	 */
	public String getHideAndShowCta() {
		return hideAndShowCta;
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
	 * Gets the equipment list api.
	 *
	 * @return the equipment list api
	 */
	public String getEquipmentListApi() {
		return equipmentListApi;
	}

	/**
	 * Gets the first.
	 *
	 * @return the first
	 */
	public String getFirst() {
		return first;
	}

	/**
	 * Gets the last.
	 *
	 * @return the last
	 */
	public String getLast() {
		return last;
	}

	/**
	 * Gets the customize table.
	 *
	 * @return the customize table
	 */
	public String getCustomizeTable() {
		return customizeTable;
	}

	/**
	 * Gets the equip stat tool tip.
	 *
	 * @return the equip stat tool tip
	 */
	public String getEquipStatToolTip() {
		return evaluateToolTip(equipStatToolTip);
	}

	/**
	 * Gets the country tool tip.
	 *
	 * @return the country tool tip
	 */
	public String getCountryToolTip() {
		return evaluateToolTip(countryToolTip);
	}

	/**
	 * Gets the site tool tip.
	 *
	 * @return the site tool tip
	 */
	public String getSiteToolTip() {
		return evaluateToolTip(siteToolTip);
	}

	/**
	 * Gets the line tool tip.
	 *
	 * @return the line tool tip
	 */
	public String getLineToolTip() {
		return evaluateToolTip(lineToolTip);
	}

	/**
	 * Gets the equip desc tool tip.
	 *
	 * @return the equip desc tool tip
	 */
	public String getEquipDescToolTip() {
		return evaluateToolTip(equipDescToolTip);
	}

	/**
	 * Gets the serial num tool tip.
	 *
	 * @return the serial num tool tip
	 */
	public String getSerialNumToolTip() {
		return evaluateToolTip(serialNumToolTip);
	}


	/**
	 * Gets the functional location.
	 *
	 * @return the functional location
	 */
	public String getFunctionalLocation() {
		return functionalLocation;
	}

	/**
	 * Gets the functional location tool tip.
	 *
	 * @return the functional location tool tip
	 */
	public String getFunctionalLocationToolTip() {
		return functionalLocationToolTip;
	}

	/**
	 * Gets the site description.
	 *
	 * @return the site description.
	 */
	public String getSiteDescription() {
		return siteDescription;
	}

	/**
	 * Gets the site description tool tip.
	 *
	 * @return the site description tool tip
	 */
	public String getSiteDescToolTip() {
		return siteDescToolTip;
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
	 * Gets the location tool tip.
	 *
	 * @return the location tool tip
	 */
	public String getLocationToolTip() {
		return locationToolTip;
	}

	/**
	 * Gets the i18n keys.
	 *
	 * @return the i18n keys
	 */
	public String getI18nKeys() {
        return i18nKeys;
    }

	/**
	 * Gets the country.
	 *
	 * @return the country
	 */
	public String getCountry() {
		return country;
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
	 * Gets the search results.
	 *
	 * @return the search results
	 */
	public String getSearchResults() {
		return searchResults;
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
	 * Gets the customer.
	 *
	 * @return the customer
	 */
	public String getCustomer() {
		return customer;
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
	 * Gets the serial number.
	 *
	 * @return the serial number
	 */
	public String getSerialNumber() {
		return serialNumber;
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
	 * Gets the equipment type.
	 *
	 * @return the equipment type
	 */
	public String getEquipmentType() {
		return equipmentType;
	}

	/**
	 * Gets the my equipment.
	 *
	 * @return the my equipment
	 */
	public String getMyEquipment() {
		return myEquipment;
	}

	/**
	 * Gets the apply filter.
	 *
	 * @return the apply filter
	 */
	public String getApplyFilter() {
		return applyFilter;
	}

	/**
	 * Gets the removes the all.
	 *
	 * @return the removes the all
	 */
	public String getRemoveAll() {
		return removeAll;
	}

	/**
	 * Gets the show all filters.
	 *
	 * @return the show all filters
	 */
	public String getShowAllFilters() {
        return showAllFilters;
    }

    /**
     * Gets the hide all filters.
     *
     * @return the hide all filters
     */
    public String getHideAllFilters() {
        return hideAllFilters;
    }

    /**
     * Gets the removes the all filters.
     *
     * @return the removes the all filters
     */
    public String getRemoveAllFilters() {
        return removeAllFilters;
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
	 * Checks if is publish environment.
	 *
	 * @return true, if is publish environment
	 */
	public boolean isPublishEnvironment() {
		return isPublishEnvironment;
	}


	/**
	 * Gets the tool tip class.
	 *
	 * @return the tool tip class
	 */
	public String getToolTipClass() {
		return toolTipClass;
	}

	/**
	 * Sets the tool tip class.
	 *
	 * @param toolTipClass the new tool tip class
	 */
	public void setToolTipClass(String toolTipClass) {
		this.toolTipClass = toolTipClass;
	}

	/**
	 * Gets the download excel servlet url.
	 *
	 * @return the download excel servlet url
	 */
	public String getDownloadExcelServletUrl() {
		return downloadExcelServletUrl;
	}

	/**
	 * Gets the add new equipment label.
	 *
	 * @return the add new equipment label
	 */
	public String getAddNewEquipmentLabel() {
		return addNewEquipmentLabel;
	}

	/**
	 * Gets the add new equipment label.
	 *
	 * @return the add new equipment url
	 */
	public String getAddNewEquipmentUrl() {
		return addNewEquipmentUrl;
	}
	/**
	 * Inits the.
	 */
	@PostConstruct
    protected void init() {
        Map<String, Object> i18KeyMap = new HashMap<>();
        i18KeyMap.put(CustomerHubConstants.REMOVE_ALL, getRemoveAll());
        i18KeyMap.put(CustomerHubConstants.APPLY_FILTER, getApplyFilter());
        i18KeyMap.put(CustomerHubConstants.MY_EQUIPMENT, getMyEquipment());
        i18KeyMap.put(CustomerHubConstants.EQUIPMENT_STATUS, getEquipmentStatus());
        i18KeyMap.put(CustomerHubConstants.EQUIPMENT_TYPE, getEquipmentType());
        i18KeyMap.put(CustomerHubConstants.SERIAL_NUMBER, getSerialNumber());
        i18KeyMap.put(CustomerHubConstants.EQUIPMENT_DESCRIPTION, getEquipmentDescription());
        i18KeyMap.put(CustomerHubConstants.SITE, getSite());
        i18KeyMap.put(CustomerHubConstants.CUSTOMER, getCustomer());
        i18KeyMap.put(CustomerHubConstants.SEARCH_RESULTS, getSearchResults());
        i18KeyMap.put(CustomerHubConstants.LINE, getLine());
        i18KeyMap.put(CustomerHubConstants.COUNTRY, getCountry());
        i18KeyMap.put(CustomerHubConstants.FIRST, getFirst());
        i18KeyMap.put(CustomerHubConstants.LAST, getLast());
        i18KeyMap.put(CustomerHubConstants.CUSTOMIZE_TABLE, getCustomizeTable());
        i18KeyMap.put(CustomerHubConstants.SERIAL_NUM_TOOL_TIP, getSerialNumToolTip());
        i18KeyMap.put(CustomerHubConstants.FUNCTIONAL_LOCATION, getFunctionalLocation());
        i18KeyMap.put(CustomerHubConstants.SITE_DESCRIPTION, getSiteDescription());
        i18KeyMap.put(CustomerHubConstants.LOCATION, getLocation());
		i18KeyMap.put(CustomerHubConstants.LOCATION_TOOL_TIP, getLocationToolTip());
		i18KeyMap.put(CustomerHubConstants.SITE_DESC_TOOL_TIP, getSiteDescToolTip());
		i18KeyMap.put(CustomerHubConstants.FUNCTIONAL_LOCATION_TOOL_TIP, getFunctionalLocationToolTip());
        i18KeyMap.put(CustomerHubConstants.LINE_TOOL_TIP, getLineToolTip());
        i18KeyMap.put(CustomerHubConstants.SITE_TOOL_TIP, getSiteToolTip());
        i18KeyMap.put(CustomerHubConstants.COUNTRY_TOOL_TIP, getCountryToolTip());
        i18KeyMap.put(CustomerHubConstants.EQUIPMENT_DESC_TOOL_TIP, getEquipDescToolTip());
        i18KeyMap.put(CustomerHubConstants.EQUIP_STAT_TOOL_TIP, getEquipStatToolTip());
        i18KeyMap.put(CustomerHubConstants.API_ERROR_CODES, GlobalUtil.getApiErrorCodes(resource));
		i18KeyMap.put(CustomerHubConstants.HIDE_AND_SHOW_CTA, getHideAndShowCta());
		i18KeyMap.put(CustomerHubConstants.NO_DATA_FOUND, getNoDataFound());
		i18KeyMap.put(CustomerHubConstants.PERMANENT_VOLUME_CONV, getPermanentVolumeConversion());
		i18KeyMap.put(CustomerHubConstants.SHOW_ALL_FILTERS, getShowAllFilters());
		i18KeyMap.put(CustomerHubConstants.HIDE_ALL_FILTERS, getHideAllFilters());
		i18KeyMap.put(CustomerHubConstants.REMOVE_ALL_FILTERS, getRemoveAllFilters());
		
        if (slingSettingsService.getRunModes().contains("publish")) {
            isPublishEnvironment = Boolean.TRUE;
        }
        Gson gson = new Gson();
        i18nKeys = gson.toJson(i18KeyMap);
        downloadExcelServletUrl = resource.getPath() + CustomerHubConstants.EXCEL_DOWNLOAD_EXTENSION;
        
        countryApi = service.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR + GlobalUtil
                .getSelectedApiMapping(service, "myequipment-countrylist");
        
        equipmentListApi = service.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR + GlobalUtil
                .getSelectedApiMapping(service, "myequipment-equipmentlist");
	}
	
	/**
	 * Evaluate tool tip.
	 *
	 * @param toolTipText the tool tip text
	 * @return the string
	 */
	private String evaluateToolTip(String toolTipText) {
		if (StringUtils.isNotBlank(toolTipText) && toolTipText.length() > 0) {
			setToolTipClass("showToolTip");
			return toolTipText;
		}
		return StringUtils.EMPTY;
	}

	/**
	 * Get valid url to Equipment Details
	 * @return mapped url.
	 */
	public String getMappedEquipmentDetailsUrl() {
		return LinkUtil.getValidLink(resource, EQUIPMENT_DETAILS_PATH);
	}


}
