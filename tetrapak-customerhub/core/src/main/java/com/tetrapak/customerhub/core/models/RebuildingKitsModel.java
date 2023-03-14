package com.tetrapak.customerhub.core.models;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import com.tetrapak.customerhub.core.utils.LinkUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class RebuildingKitsModel.
 *
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RebuildingKitsModel {

	/** The resource. */
	@Self
	private Resource resource;

	/** The rebuilding kits label. */
	@ValueMapValue
	private String rebuildingKitsLabel;
	
	/** The countryLabel. */
	@ValueMapValue
	private String countryLabel;
	
	/** The functionalLocationLabel. */
	@ValueMapValue
	private String functionalLocationLabel;
	
	/** The descriptionLabel. */
	@ValueMapValue
	private String descriptionLabel;
	
	/** The machineSystemLabel. */
	@ValueMapValue
	private String machineSystemLabel;

	/** The serialNumberLabel. */
	@ValueMapValue
	private String serialNumberLabel;
	
	/** The status Label. */
	@ValueMapValue
	private String statusLabel;
	
	/** The RK number. */
	@ValueMapValue
	private String rkNumberLabel;
	
	/** The RK Description Label. */
	@ValueMapValue
	private String rkDescriptionLabel;

	/** The Impl status label. */
	@ValueMapValue
	private String implStatusLabel;
	
	/** The Impl date label. */
	@ValueMapValue
	private String implDateLabel;
	
	/** The Impl status date label. */
	@ValueMapValue
	private String implStatusDateLabel;
	
	/** The RK type label. */
	@ValueMapValue
	private String rkTypeLabel;
	
	/** The RK status label. */
	@ValueMapValue
	private String rkStatusLabel;
	
	/** The RK Handling label. */
	@ValueMapValue
	private String rkHandlingLabel;
	
	/** The Impl deadline label. */
	@ValueMapValue
	private String implDeadlineLabel;
	
	/** The Planned date label. */
	@ValueMapValue
	private String plannedDateLabel;
	
	/** The Release date label. */
	@ValueMapValue
	private String releaseDateLabel;
	
	/** The General RK number label. */
	@ValueMapValue
	private String generalRkNumberLabel;

	/** The Order label. */
	@ValueMapValue
	private String order;
	
	/** The Permanent Volume Conversion label. */
	@ValueMapValue
	private String permanentVolumeConversionLabel;
	
	/** The customize table label. */
	@ValueMapValue
	private String customizeTableLabel;
	
	/** The Export to CSV label. */
	@ValueMapValue
	private String exportToCsvLabel;
	
	/** The Show All filters label. */
	@ValueMapValue
	private String showAllFiltersLabel;
	
	/** The Hide filters label. */
	@ValueMapValue
	private String hideFiltersLabel;
	
	/** The apply filter. */
	@ValueMapValue
	private String applyFilter;
	
	/** The remove all label. */
	@ValueMapValue
	private String removeAllLabel;
	
	/** The no Data Found label.*/
	@ValueMapValue
	private String noDataFoundLabel;
	
	/** The first label. */
	@ValueMapValue
	private String firstLabel;
	
	/** The last label. */
	@ValueMapValue
	private String lastLabel;
	
	/** The search results label. */
	@ValueMapValue
	private String searchResultsLabel;
	
	/** The remove all filters. */
	@ValueMapValue
	private String removeAllFiltersLabel;

	/** The hide And Show Cta.*/
	@ValueMapValue
	private String hideAndShowCta;

	/** The country tool tip. */
	@ValueMapValue
	private String countryToolTip;
	
	/** The functional location tool tip. */
	@ValueMapValue
	private String functionalLocationToolTip;

	/** The description tool tip. */
	@ValueMapValue
	private String descriptionToolTip;
	
	/** The machine system tool tip. */
	@ValueMapValue
	private String machineSystemToolTip;
	
	/** The serial number tool tip. */
	@ValueMapValue
	private String serialNumberToolTip;
	
	/** The status tool tip. */
	@ValueMapValue
	private String statusToolTip;

	/** The RK number tool tip. */
	@ValueMapValue
	private String rkNumberToolTip;

	/** The RK description tool tip. */
	@ValueMapValue
	private String rkDescriptionToolTip;

	/** The Impl status tool tip. */
	@ValueMapValue
	private String implStatusToolTip;
	
	/** The Impl date tool tip. */
	@ValueMapValue
	private String implDateToolTip;
	
	/** The Impl status date tool tip. */
	@ValueMapValue
	private String implStatusDateToolTip;

	/** The RK type tool tip. */
	@ValueMapValue
	private String rkTypeToolTip;

	/** The RK status tool tip. */
	@ValueMapValue
	private String rkStatusToolTip;
	
	/** The RK handling tool tip. */
	@ValueMapValue
	private String rkHandlingToolTip;
	
	/** The Impl deadline tool tip. */
	@ValueMapValue
	private String implDeadlineToolTip;
	
	/** The Planned date tool tip. */
	@ValueMapValue
	private String plannedDateToolTip;
	
	/** The Release date tool tip. */
	@ValueMapValue
	private String releaseDateToolTip;
	
	/** The General RK number tool tip. */
	@ValueMapValue
	private String generalRkNumberToolTip;

	/** The Order tool tip. */
	@ValueMapValue
	private String orderToolTip;

	/** The From Date Text. */
	@ValueMapValue
	private String fromDateText;

	/** The To Date Text. */
	@ValueMapValue
	private String toDateText;

	/** The Invalid date Text. */
	@ValueMapValue
	private String invalidDateText;

	/** The Past Date Error Text. */
	@ValueMapValue
	private String pastDateErrorText;

	/** The Date Later Than error text. */
	@ValueMapValue
	private String dateLaterThanError;

	/** The Date Before Than error text. */
	@ValueMapValue
	private String dateBeforeThanError;
	
	/** The path to rebuilding kits details. */
	@ValueMapValue
	private String rebuldingKitsDetailsPath;

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
	
    /** The rebuilding kits list api. */
    @ValueMapValue
    private String rebuildingKitsListApi;

	/** The service. */
	@OSGiService
	private APIGEEService service;
	
	/** The download csv servlet url. */
	private String downloadCsvServletUrl;

	/** The max filter limit error pre label. */
	@ValueMapValue
	private String maxLimitErrorPre;

	/** The max filter limit error after label. */
	@ValueMapValue
	private String maxLimitErrorAfter;

	/** The filters selected label. */
	@ValueMapValue
	private String filtersSelected;

	/** The of label. */
	@ValueMapValue
	private String filtersOf;

	/**
	 * Sets the tool tip class.
	 *
	 * @param toolTipClass the new tool tip class
	 */
	public void setToolTipClass(String toolTipClass) {
		this.toolTipClass = toolTipClass;
	}

	/**
	 * Gets the rebuildingKitsLabel.
	 *
	 * @return the rebuildingKitsLabel
	 */
	public String getRebuildingKitsLabel() {
		return rebuildingKitsLabel;
	}

	/**
	 * Gets the country label.
	 *
	 * @return the countryLabel
	 */
	public String getCountryLabel() {
		return countryLabel;
	}

	/**
	 * Gets the functional location.
	 *
	 * @return the functionalLocationLabel
	 */
	public String getFunctionalLocationLabel() {
		return functionalLocationLabel;
	}

	/**
	 * Gets the desc label.
	 *
	 * @return the descriptionLabel
	 */
	public String getDescriptionLabel() {
		return descriptionLabel;
	}

	/**
	 * Gets the machine system label.
	 *
	 * @return the machineSystemLabel
	 */
	public String getMachineSystemLabel() {
		return machineSystemLabel;
	}

	/**
	 * Gets the serial number label.
	 *
	 * @return the serialNumberLabel
	 */
	public String getSerialNumberLabel() {
		return serialNumberLabel;
	}

	/**
	 * Gets the status label.
	 *
	 * @return the statusLabel
	 */
	public String getStatusLabel() {
		return statusLabel;
	}

	/**
	 * Gets the rk number label.
	 *
	 * @return the rkNumberLabel
	 */
	public String getRkNumberLabel() {
		return rkNumberLabel;
	}

	/**
	 * Gets the rk desc label.
	 *
	 * @return the rkDescriptionLabel
	 */
	public String getRkDescriptionLabel() {
		return rkDescriptionLabel;
	}

	/**
	 * Gets the impl status label.
	 *
	 * @return the implStatusLabel
	 */
	public String getImplStatusLabel() {
		return implStatusLabel;
	}
	
	/**
	 * Gets the impl date label.
	 *
	 * @return the implDateLabel
	 */
	public String getImplDateLabel() {
		return implDateLabel;
	}

	/**
	 * Gets the impl status date label.
	 *
	 * @return the implStatusDateLabel
	 */
	public String getImplStatusDateLabel() {
		return implStatusDateLabel;
	}

	/**
	 * Gets the rk type label.
	 *
	 * @return the rkTypeLabel
	 */
	public String getRkTypeLabel() {
		return rkTypeLabel;
	}

	/**
	 * Gets the rk status label.
	 *
	 * @return the rkStatusLabel
	 */
	public String getRkStatusLabel() {
		return rkStatusLabel;
	}

	/**
	 * Gets the rk handling label.
	 *
	 * @return the rkHandlingLabel
	 */
	public String getRkHandlingLabel() {
		return rkHandlingLabel;
	}

	/**
	 * Gets the impl deadline label.
	 *
	 * @return the implDeadlineLabel
	 */
	public String getImplDeadlineLabel() {
		return implDeadlineLabel;
	}

	/**
	 * Gets the planned date label.
	 *
	 * @return the plannedDateLabel
	 */
	public String getPlannedDateLabel() {
		return plannedDateLabel;
	}

	/**
	 * Gets the release date label.
	 *
	 * @return the releaseDateLabel
	 */
	public String getReleaseDateLabel() {
		return releaseDateLabel;
	}

	/**
	 * Gets the general rk number label.
	 *
	 * @return the generalRkNumberLabel
	 */
	public String getGeneralRkNumberLabel() {
		return generalRkNumberLabel;
	}

	/**
	 * Gets the order label.
	 *
	 * @return the orderLabel
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * Gets the permanent volumen conversion label.
	 *
	 * @return the permanentVolumeConversionLabel
	 */
	public String getPermanentVolumeConversionLabel() {
		return permanentVolumeConversionLabel;
	}

	/**
	 * Gets the customize table label.
	 *
	 * @return the customizeTableLabel
	 */
	public String getCustomizeTableLabel() {
		return customizeTableLabel;
	}

	/**
	 * Gets the export to csv label.
	 *
	 * @return the exportToCsvLabel
	 */
	public String getExportToCsvLabel() {
		return exportToCsvLabel;
	}

	/**
	 * Gets the show all filters label.
	 *
	 * @return the showAllFiltersLabel
	 */
	public String getShowAllFiltersLabel() {
		return showAllFiltersLabel;
	}

	/**
	 * Gets the hide filters label.
	 *
	 * @return the hideFiltersLabel
	 */
	public String getHideFiltersLabel() {
		return hideFiltersLabel;
	}

	/**
	 * Gets the apply filter label.
	 *
	 * @return the applyFilter
	 */
	public String getApplyFilter() {
		return applyFilter;
	}

	/**
	 * Gets the remove all label.
	 *
	 * @return the removeAllLabel
	 */
	public String getRemoveAllLabel() {
		return removeAllLabel;
	}

	/**
	 * Gets the no data found label.
	 *
	 * @return the noDataFoundLabel
	 */
	public String getNoDataFoundLabel() {
		return noDataFoundLabel;
	}

	/**
	 * Gets the first label.
	 *
	 * @return the firstLabel
	 */
	public String getFirstLabel() {
		return firstLabel;
	}

	/**
	 * Gets the last label.
	 *
	 * @return the lastLabel
	 */
	public String getLastLabel() {
		return lastLabel;
	}

	/**
	 * Gets the search results label.
	 *
	 * @return the searchResultsLabel
	 */
	public String getSearchResultsLabel() {
		return searchResultsLabel;
	}

	/**
	 * Gets the remove all filters label.
	 *
	 * @return the removeAllFiltersLabel
	 */
	public String getRemoveAllFiltersLabel() {
		return removeAllFiltersLabel;
	}

	/**
	 * Gets the hide or show filter label.
	 *
	 * @return the hideAndShowCta
	 */
	public String getHideAndShowCta() {
		return hideAndShowCta;
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
	 * Gets the functional location tool tip.
	 *
	 * @return the functional location tool tip
	 */
	public String getFunctionalLocationToolTip() {
		return evaluateToolTip(functionalLocationToolTip);
	}

	/**
	 * Gets the description tool tip.
	 *
	 * @return the description tool tip
	 */
	public String getDescriptionToolTip() {
		return evaluateToolTip(descriptionToolTip);
	}

	/**
	 * Gets the machine system tool tip.
	 *
	 * @return the machine system tool tip
	 */
	public String getMachineSystemToolTip() {
		return evaluateToolTip(machineSystemToolTip);
	}

	/**
	 * Gets the serial number tool tip.
	 *
	 * @return the serial number tool tip
	 */
	public String getSerialNumberToolTip() {
		return evaluateToolTip(serialNumberToolTip);
	}

	/**
	 * Gets the status tool tip.
	 *
	 * @return the status tool tip
	 */
	public String getStatusToolTip() {
		return evaluateToolTip(statusToolTip);
	}

	/**
	 * Gets the rk number tool tip.
	 *
	 * @return the rk number tool tip
	 */
	public String getRkNumberToolTip() {
		return evaluateToolTip(rkNumberToolTip);
	}

	/**
	 * Gets the order tool tip.
	 *
	 * @return the order tool tip
	 */
	public String getOrderToolTip() {
		return orderToolTip;
	}

	/**
	 * Gets the rk desc tool tip.
	 *
	 * @return the rk desc tool tip
	 */
	public String getRkDescriptionToolTip() {
		return evaluateToolTip(rkDescriptionToolTip);
	}

	/**
	 * Gets the impl status tool tip.
	 *
	 * @return the impl status tool tip
	 */
	public String getImplStatusToolTip() {
		return evaluateToolTip(implStatusToolTip);
	}
	
	/**
	 * Gets the impl date tool tip.
	 *
	 * @return the impl date tool tip
	 */
	public String getImplDateToolTip() {
		return evaluateToolTip(implDateToolTip);
	}

	/**
	 * Gets the impl status date tool tip.
	 *
	 * @return the impl status date tool tip
	 */
	public String getImplStatusDateToolTip() {
		return evaluateToolTip(implStatusDateToolTip);
	}

	/**
	 * Gets the rk type tool tip.
	 *
	 * @return the rk type tool tip
	 */
	public String getRkTypeToolTip() {
		return evaluateToolTip(rkTypeToolTip);
	}

	/**
	 * Gets the rk status tool tip.
	 *
	 * @return the rk status tool tip
	 */
	public String getRkStatusToolTip() {
		return evaluateToolTip(rkStatusToolTip);
	}

	/**
	 * Gets the rk handling tool tip.
	 *
	 * @return the rk handling tool tip
	 */
	public String getRkHandlingToolTip() {
		return evaluateToolTip(rkHandlingToolTip);
	}

	/**
	 * Gets the impl deadline tool tip.
	 *
	 * @return the impl deadline tool tip
	 */
	public String getImplDeadlineToolTip() {
		return evaluateToolTip(implDeadlineToolTip);
	}

	/**
	 * Gets the planned date tool tip.
	 *
	 * @return the planned date tool tip
	 */
	public String getPlannedDateToolTip() {
		return evaluateToolTip(plannedDateToolTip);
	}

	/**
	 * Gets the release date tool tip.
	 *
	 * @return the release date tool tip
	 */
	public String getReleaseDateToolTip() {
		return evaluateToolTip(releaseDateToolTip);
	}

	/**
	 * Gets the general rk number tool tip.
	 *
	 * @return the general rk number tool tip
	 */
	public String getGeneralRkNumberToolTip() {
		return evaluateToolTip(generalRkNumberToolTip);
	}

	/**
	 * Gets the rebuilding kits details path.
	 *
	 * @return the rebuldingKitsDetailsPath
	 */
	public String getRebuldingKitsDetailsPath() {
		return rebuldingKitsDetailsPath;
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
	 * Gets the i18n keys.
	 *
	 * @return the i18n keys
	 */
	public String getI18nKeys() {
		return i18nKeys;
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
	 * Gets the country api.
	 *
	 * @return the country api
	 */
	public String getCountryApi() {
		return countryApi;
	}

	/**
	 * Gets the rebuilding kits list api.
	 *
	 * @return the rebuilding kits list api
	 */
	public String getRebuildingKitsListApi() {
		return rebuildingKitsListApi;
	}

	/**
	 * Gets the download csv servlet url.
	 *
	 * @return the download csv servlet url
	 */
	public String getDownloadCsvServletUrl() {
		return downloadCsvServletUrl;
	}

	/**
	 * Gets the max filter limit error label pre label.
	 *
	 * @return the max filter limit error label pre label
	 */
	public String getMaxLimitErrorPre() {
		return maxLimitErrorPre;
	}

	/**
	 * Gets the max filter limit error label after label.
	 *
	 * @return the max filter limit error label after label
	 */
	public String getMaxLimitErrorAfter() {
		return maxLimitErrorAfter;
	}

	/**
	 * Gets the filters selected label.
	 *
	 * @return the filters selected label
	 */
	public String getFiltersSelected() {
		return filtersSelected;
	}

	/**
	 * Gets the filters OF label.
	 *
	 * @return the filters OF label
	 */
	public String getFiltersOf() {
		return filtersOf;
	}

	/**
	 *Gets the from date text
	 * @return from date text
	 */
	public String getFromDateText() {
		return fromDateText;
	}

	/**
	 * Gets the to date text
	 * @return to date text
	 */
	public String getToDateText() {
		return toDateText;
	}

	/**
	 *Gets the invalid date text
	 * @return invalid date text
	 */
	public String getInvalidDateText() {
		return invalidDateText;
	}

	/**
	 *Gets the past date error text
	 * @return past date error text
	 */
	public String getPastDateErrorText() {
		return pastDateErrorText;
	}

	/**
	 *Gets the date later than error text
	 * @return date later than error text
	 */
	public String getDateLaterThanError() {
		return dateLaterThanError;
	}

	/**
	 *Gets the date before than error text
	 * @return date before than error text
	 */
	public String getDateBeforeThanError() {
		return dateBeforeThanError;
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
    protected void init() {
        Map<String, Object> i18KeyMap = new HashMap<>();
        i18KeyMap.put(CustomerHubConstants.REBUILDING_KITS, getRebuildingKitsLabel());
        i18KeyMap.put(CustomerHubConstants.COUNTRY, getCountryLabel());
        i18KeyMap.put(CustomerHubConstants.FUNCTIONAL_LOCATION, getFunctionalLocationLabel());
        i18KeyMap.put(CustomerHubConstants.EQUIPMENT_DESCRIPTION, getDescriptionLabel());
        i18KeyMap.put(CustomerHubConstants.MACHINE_SYSTEM, getMachineSystemLabel());
        i18KeyMap.put(CustomerHubConstants.SERIAL_NUMBER, getSerialNumberLabel());
        i18KeyMap.put(CustomerHubConstants.EQUIPMENT_STATUS, getStatusLabel());
        i18KeyMap.put(CustomerHubConstants.RK_NUMBER, getRkNumberLabel());
        i18KeyMap.put(CustomerHubConstants.RK_DESC, getRkDescriptionLabel());
        i18KeyMap.put(CustomerHubConstants.IMPL_STATUS, getImplStatusLabel());
        i18KeyMap.put(CustomerHubConstants.IMPL_DATE, getImplDateLabel());
        i18KeyMap.put(CustomerHubConstants.IMPL_STATUS_DATE, getImplStatusDateLabel());
        i18KeyMap.put(CustomerHubConstants.RK_TYPE, getRkTypeLabel());
        i18KeyMap.put(CustomerHubConstants.RK_STATUS, getRkStatusLabel());
        i18KeyMap.put(CustomerHubConstants.RK_HANDLING, getRkHandlingLabel());
        i18KeyMap.put(CustomerHubConstants.IMPL_DEADLINE, getImplDeadlineLabel());
        i18KeyMap.put(CustomerHubConstants.PLANNED_DATE, getPlannedDateLabel());
        i18KeyMap.put(CustomerHubConstants.RELEASE_DATE, getReleaseDateLabel());
        i18KeyMap.put(CustomerHubConstants.GENERAL_RKNUMBER, getGeneralRkNumberLabel());
        i18KeyMap.put(CustomerHubConstants.ORDER, getOrder());
        i18KeyMap.put(CustomerHubConstants.PERMANENT_VOLUME_CONV, getPermanentVolumeConversionLabel());
        i18KeyMap.put(CustomerHubConstants.CUSTOMIZE_TABLE, getCustomizeTableLabel());
        i18KeyMap.put(CustomerHubConstants.EXPORT_TO_CSV, getExportToCsvLabel());
        i18KeyMap.put(CustomerHubConstants.SHOW_ALL_FILTERS, getShowAllFiltersLabel());
        i18KeyMap.put(CustomerHubConstants.HIDE_ALL_FILTERS, getHideFiltersLabel());
        i18KeyMap.put(CustomerHubConstants.REMOVE_ALL, getRemoveAllLabel());
        i18KeyMap.put(CustomerHubConstants.APPLY_FILTER, getApplyFilter());
        i18KeyMap.put(CustomerHubConstants.NO_DATA_FOUND, getNoDataFoundLabel());
        i18KeyMap.put(CustomerHubConstants.FIRST, getFirstLabel());
        i18KeyMap.put(CustomerHubConstants.LAST, getLastLabel());
        i18KeyMap.put(CustomerHubConstants.SEARCH_RESULTS, getSearchResultsLabel());
        i18KeyMap.put(CustomerHubConstants.REMOVE_ALL_FILTERS, getRemoveAllFiltersLabel());
        i18KeyMap.put(CustomerHubConstants.MAX_FILTERS_LIMIT_PRE, getMaxLimitErrorPre());
		i18KeyMap.put(CustomerHubConstants.MAX_FILTERS_LIMIT_AFTER, getMaxLimitErrorAfter());
		i18KeyMap.put(CustomerHubConstants.FILTERS_OF, getFiltersOf());
		i18KeyMap.put(CustomerHubConstants.FILTERS_SELECTED, getFiltersSelected());
		i18KeyMap.put(CustomerHubConstants.HIDE_AND_SHOW_CTA, getHideAndShowCta());
		i18KeyMap.put(CustomerHubConstants.API_ERROR_CODES, GlobalUtil.getApiErrorCodes(resource));
		i18KeyMap.put(CustomerHubConstants.COUNTRY_TOOL_TIP, getCountryToolTip());
		i18KeyMap.put(CustomerHubConstants.FUNCTIONAL_LOCATION_TOOL_TIP, getFunctionalLocationToolTip());
		i18KeyMap.put(CustomerHubConstants.EQUIPMENT_DESC_TOOL_TIP, getDescriptionToolTip());
		i18KeyMap.put(CustomerHubConstants.MACHINE_SYSTEM_TOOL_TIP, getMachineSystemToolTip());
        i18KeyMap.put(CustomerHubConstants.SERIAL_NUM_TOOL_TIP, getSerialNumberToolTip());
        i18KeyMap.put(CustomerHubConstants.EQUIP_STAT_TOOL_TIP, getStatusToolTip());
        i18KeyMap.put(CustomerHubConstants.RK_NUMBER_TOOL_TIP, getRkNumberToolTip());
        i18KeyMap.put(CustomerHubConstants.RK_DESC_TOOL_TIP, getRkDescriptionToolTip());
        i18KeyMap.put(CustomerHubConstants.IMPL_STATUS_TOOL_TIP, getImplStatusToolTip());
        i18KeyMap.put(CustomerHubConstants.IMPL_DATE_TOOL_TIP, getImplDateToolTip());
        i18KeyMap.put(CustomerHubConstants.IMPL_STATUS_DATE_TOOL_TIP, getImplStatusDateToolTip());
        i18KeyMap.put(CustomerHubConstants.RK_TYPE_TOOL_TIP, getRkTypeToolTip());
        i18KeyMap.put(CustomerHubConstants.RK_STATUS_TOOL_TIP, getRkStatusToolTip());
        i18KeyMap.put(CustomerHubConstants.RK_HANDLING_TOOL_TIP, getRkHandlingToolTip());
        i18KeyMap.put(CustomerHubConstants.IMPL_DEADLINE_TOOL_TIP, getImplDeadlineToolTip());
        i18KeyMap.put(CustomerHubConstants.PLANNED_DATE_TOOL_TIP, getPlannedDateToolTip());
        i18KeyMap.put(CustomerHubConstants.RELEASE_DATE_TOOL_TIP, getReleaseDateToolTip());
        i18KeyMap.put(CustomerHubConstants.GENERAL_RKNUMBER_TOOL_TIP, getGeneralRkNumberToolTip());
		i18KeyMap.put(CustomerHubConstants.ORDER_TOOL_TIP, getOrderToolTip());
		i18KeyMap.put(CustomerHubConstants.FROM_DATE_TEXT, getFromDateText());
		i18KeyMap.put(CustomerHubConstants.TO_DATE_TEXT, getToDateText());
		i18KeyMap.put(CustomerHubConstants.INVALID_DATE_TEXT, getInvalidDateText());
		i18KeyMap.put(CustomerHubConstants.PAST_ERROR_TEXT, getPastDateErrorText());
		i18KeyMap.put(CustomerHubConstants.DATE_LATER_THAN_ERROR, getDateLaterThanError());
		i18KeyMap.put(CustomerHubConstants.DATE_BEFORE_THAN_ERROR, getDateBeforeThanError());

        if (slingSettingsService.getRunModes().contains(CustomerHubConstants.PUBLISH)) {
            isPublishEnvironment = Boolean.TRUE;
        }
        Gson gson = new Gson();
        i18nKeys = gson.toJson(i18KeyMap);
        downloadCsvServletUrl = resource.getPath() + CustomerHubConstants.EXCEL_DOWNLOAD_EXTENSION;
        
        countryApi = service.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR + GlobalUtil
                .getSelectedApiMapping(service, CustomerHubConstants.REBUILDINGKITS_COUNTRYLIST_API);
        
        rebuildingKitsListApi = service.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR + GlobalUtil
                .getSelectedApiMapping(service, CustomerHubConstants.REBUILDINGKITS_LIST_API);
	}
	
	/**
	 * Evaluate tool tip.
	 *
	 * @param toolTipText the tool tip text
	 * @return the string
	 */
	private String evaluateToolTip(String toolTipText) {
		if (StringUtils.isNotBlank(toolTipText) && toolTipText.length() > 0) {
			setToolTipClass(CustomerHubConstants.SHOW_TOOLTIP);
			return toolTipText;
		}
		return StringUtils.EMPTY;
	}

	/**
	 * Get valid url to Rebuilding Kits Details
	 * @return mapped url.
	 */
	public String getMappedRebuildingKitsDetailsUrl() {
		return LinkUtil.getValidLink(resource, rebuldingKitsDetailsPath);
	}
}
