package com.tetrapak.customerhub.core.models;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;

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
 * @author ojaswarn
 * The Class MyEquipmentModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MyEquipmentModel {
	
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
	
	/** The equipment description. */
	@ValueMapValue
	private String equipmentDescription;
	
	/** The serial number. */
	@ValueMapValue
	private String serialNumber;
	
	/** The equipment status. */
	@ValueMapValue
	private String equipmentStatus;
	
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
	 * Inits the.
	 */
	@PostConstruct
    protected void init() {
        Map<String, Object> i18KeyMap = new HashMap<>();
        i18KeyMap.put("removeAll", getRemoveAll());
        i18KeyMap.put("applyFilter", getApplyFilter());
        i18KeyMap.put("myEquipment", getMyEquipment());
        i18KeyMap.put("equipmentStatus", getEquipmentStatus());
        i18KeyMap.put("serialNumber", getSerialNumber());
        i18KeyMap.put("equipmentDescription", getEquipmentDescription());
        i18KeyMap.put("site", getSite());
        i18KeyMap.put("searchResults", getSearchResults());
        i18KeyMap.put("line", getLine());
        i18KeyMap.put("country", getCountry());
        i18KeyMap.put("first", getFirst());
        i18KeyMap.put("last", getLast());
        i18KeyMap.put("customizeTable", getCustomizeTable());
        i18KeyMap.put("serialNumToolTip", getSerialNumToolTip());
        i18KeyMap.put("functionalLocation", getFunctionalLocation());
        i18KeyMap.put("siteDescription", getSiteDescription());
        i18KeyMap.put("location", getLocation());
		i18KeyMap.put("locationToolTip", getLocationToolTip());
		i18KeyMap.put("siteDescToolTip", getSiteDescToolTip());
		i18KeyMap.put("functionalLocationToolTip", getFunctionalLocationToolTip());
        i18KeyMap.put("lineToolTip", getLineToolTip());
        i18KeyMap.put("siteToolTip", getSiteToolTip());
        i18KeyMap.put("countryToolTip", getCountryToolTip());
        i18KeyMap.put("equipDescToolTip", getEquipDescToolTip());
        i18KeyMap.put("equipStatToolTip", getEquipStatToolTip());
        i18KeyMap.put("apiErrorCodes", GlobalUtil.getApiErrorCodes(resource));
		i18KeyMap.put("hideAndShowCta", getHideAndShowCta());
		i18KeyMap.put("noDataFound", getNoDataFound());
		i18KeyMap.put("permanentVolumeConversion", getPermanentVolumeConversion());
        if (slingSettingsService.getRunModes().contains("publish")) {
            isPublishEnvironment = Boolean.TRUE;
        }
        Gson gson = new Gson();
        i18nKeys = gson.toJson(i18KeyMap);
        
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
}
