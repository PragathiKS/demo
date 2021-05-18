package com.tetrapak.customerhub.core.models;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;

import com.google.gson.Gson;
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
	
	/** The sling settings service. */
	@OSGiService
    private SlingSettingsService slingSettingsService;
	
	/** The is publish environment. */
	private boolean isPublishEnvironment= Boolean.FALSE;

	/** The i18n keys. */
	private String i18nKeys;
	
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
		return equipStatToolTip;
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
        i18KeyMap.put("last", getCountry());
        i18KeyMap.put("customizeTable", getCustomizeTable());
        i18KeyMap.put("equipStatToolTip", getEquipStatToolTip());
        i18KeyMap.put("apiErrorCodes", GlobalUtil.getApiErrorCodes(resource));
  
        if (slingSettingsService.getRunModes().contains("publish")) {
            isPublishEnvironment = Boolean.TRUE;
        }
        Gson gson = new Gson();
        i18nKeys = gson.toJson(i18KeyMap);
	}
}
