package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tetrapak.publicweb.core.beans.DropdownOption;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.DataEncryptionService;
import com.tetrapak.publicweb.core.services.ManagePrefContentFragService;
import com.tetrapak.publicweb.core.services.PardotService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;

/**
 * @author ojaswarn
 * The Class ManagePreferencesModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ManagePreferencesModel {

	/** The resource. */
	@Self
	@Via("resource")
	private Resource resource;

	/** The cf service. */
	@OSGiService
	private ManagePrefContentFragService cfService;
	
	/** The pardot service. */
	@OSGiService
	private PardotService pardotService;
	
	/** The encryption service. */
	@OSGiService
	private DataEncryptionService encryptionService;

	/** The request. */
	@Self
	private SlingHttpServletRequest request;

	/** The resolver. */
	@SlingObject
	private ResourceResolver resolver;

	/** The country options. */
	private List<DropdownOption> countryOptions;

	/** The language options. */
	private List<DropdownOption> languageOptions;

	/** The email. */
	private String email;

	/** The config items. */
	private ManagePreferencesConfigModel configItems;

	/** The selected country. */
	private List<DropdownOption> selectedCountry;

	/** The selected language. */
	private List<DropdownOption> selectedLanguage;

	/** The types of communication. */
	private String typesOfCommunication;

	/** The area of interest. */
	private String areaOfInterest;

	/** The email to check. */
	private String emailToCheck;
	
	/** The fetch data. */
	private Boolean fetchData = false;

	/**
	 * @return the selectedCountry
	 */
	public List<DropdownOption> getSelectedCountry() {
		return selectedCountry;
	}

	/**
	 * @param selectedCountry the selectedCountry to set
	 */
	public void setSelectedCountry(List<DropdownOption> selectedCountry) {
		this.selectedCountry = selectedCountry;
	}

	/**
	 * @return the selectedLanguage
	 */
	public List<DropdownOption> getSelectedLanguage() {
		return selectedLanguage;
	}

	/**
	 * @param selectedLanguage the selectedLanguage to set
	 */
	public void setSelectedLanguage(List<DropdownOption> selectedLanguage) {
		this.selectedLanguage = selectedLanguage;
	}

	/**
	 * @return the typesOfCommunication
	 */
	public String getTypesOfCommunication() {
		return typesOfCommunication;
	}

	/**
	 * @param typesOfCommunication the typesOfCommunication to set
	 */
	public void setTypesOfCommunication(String typesOfCommunication) {
		this.typesOfCommunication = typesOfCommunication;
	}

	/**
	 * @return the areaOfInterest
	 */
	public String getAreaOfInterest() {
		return areaOfInterest;
	}

	/**
	 * @param areaOfInterest the areaOfInterest to set
	 */
	public void setAreaOfInterest(String areaOfInterest) {
		this.areaOfInterest = areaOfInterest;
	}

	/**
	 * Gets the country options.
	 *
	 * @return the country options
	 */
	public List<DropdownOption> getCountryOptions() {
		return countryOptions;
	}

	/**
	 * Sets the country options.
	 */
	private void setCountryOptions() {
		this.countryOptions = cfService.fetchCountryList(resource.getResourceResolver());
	}

	/**
	 * Gets the language options.
	 *
	 * @return the language options
	 */
	public List<DropdownOption> getLanguageOptions() {
		return languageOptions;
	}

	/**
	 * Sets the language options.
	 */
	private void setLanguageOptions() {
		this.languageOptions = cfService.fetchLanguageList(resource.getResourceResolver());
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 */
	public void setEmail() {
		if (Objects.nonNull(request.getRequestParameter(PWConstants.ID))
				&& StringUtils.isNotBlank(request.getRequestParameter(PWConstants.ID).getString())) {
			emailToCheck = encryptionService.decryptText(request.getRequestParameter(PWConstants.ID).getString());
			if (emailToCheck.contains(PWConstants.AT_THE_RATE) && !"error".equalsIgnoreCase(emailToCheck)) {
				fetchData = true;
				this.email = maskEmailAddress(emailToCheck, '*');
			}
		}
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	protected void init() {
		setEmail();
		if (fetchData) {
			String countryFromJson = StringUtils.EMPTY;
			String languageFromJson = StringUtils.EMPTY;
			JsonObject aoi = null;
			JsonObject ct = null;
			setCountryOptions();
			setLanguageOptions();
			setConfigItems();
			JsonObject myJson = pardotService.getManagePrefJson(emailToCheck);
			if (Objects.nonNull(myJson)) {
				if (myJson.has("country")) {
					countryFromJson = myJson.get("country").getAsString();
				}
				if (myJson.has("language")) {
					languageFromJson = myJson.get("language").getAsString();
				}
				if (myJson.has("areaofinterest")) {
					aoi = myJson.get("areaofinterest").getAsJsonObject();
				}
				if (myJson.has("communicationtype")) {
					ct = myJson.get("communicationtype").getAsJsonObject();
				}
				setSelectedCountry(getSpecificData(countryOptions, countryFromJson));
				setSelectedLanguage(getSpecificData(languageOptions, languageFromJson));
				setAreaOfInterest(getAoiCtData(aoi));
				setTypesOfCommunication(getAoiCtData(ct));
			}
		}
	}

	/**
	 * Gets the specific data.
	 *
	 * @param dataList the data list
	 * @param toSearch the to search
	 * @return the specific data
	 */
	private List<DropdownOption> getSpecificData(List<DropdownOption> dataList, String toSearch) {
		List<DropdownOption> specList = new ArrayList<>();
		for (DropdownOption options : dataList) {
			if (options.getKey().equals(toSearch)) {
				final DropdownOption optionList = new DropdownOption();
		        optionList.setKey(options.getKey());
		        optionList.setValue(options.getValue());
		        specList.add(optionList);
		        break;
			}
		}
		return specList;
	}

	/**
	 * Gets the aoi ct data.
	 *
	 * @param obj the obj
	 * @return the aoi ct data
	 */
	private String getAoiCtData(JsonObject obj) {
		String finalData = StringUtils.EMPTY;
		if (Objects.nonNull(obj)) {
			for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
				if ("Y".equalsIgnoreCase(entry.getValue().getAsString())) {
					finalData = finalData.concat(entry.getKey()) + ",";
				}
			}
		}
		if (finalData.endsWith(",")) {
			finalData = finalData.substring(0, finalData.length() - 1);
		}
		return finalData;
	}

	/**
	 * Gets the config items.
	 *
	 * @return the config items
	 */
	public ManagePreferencesConfigModel getConfigItems() {
		return configItems;
	}

	/**
	 * Sets the config items.
	 */
	public void setConfigItems() {
		final Resource formConfigResource = GlobalUtil.fetchConfigResource(resource,
				"/jcr:content/root/responsivegrid/managepreferenceform");
		if (Objects.nonNull(formConfigResource)) {
			this.configItems = formConfigResource.adaptTo(ManagePreferencesConfigModel.class);
		}
	}

	/**
	 * Mask email address.
	 *
	 * @param strEmail the str email
	 * @param maskChar the mask char
	 * @return the string
	 */
	private static String maskEmailAddress(String strEmail, char maskChar) {
		String[] parts = strEmail.split(PWConstants.AT_THE_RATE);

		String firstPartMask;

		if (parts[0].length() < PWConstants.NUMBER_FOUR) {
			firstPartMask = maskString(parts[0], 0, parts[0].length(), maskChar);
		} else {
			firstPartMask = maskString(parts[0], 1, parts[0].length() - 1, maskChar);
		}
		String secondPartMask = parts[1].split("\\.")[0];
		for (int i = 0; i < secondPartMask.length(); i++) {
			if (i % PWConstants.NUMBER_TWO != 0) {
				secondPartMask = secondPartMask.substring(0, i - 1) + maskChar
						+ secondPartMask.substring(i, secondPartMask.length());
			}
		}
		secondPartMask = secondPartMask + "." + parts[1].split("\\.")[1];

		return firstPartMask + PWConstants.AT_THE_RATE + secondPartMask;
	}

	/**
	 * Mask string.
	 *
	 * @param strText  the str text
	 * @param start    the start
	 * @param end      the end
	 * @param maskChar the mask char
	 * @return the string
	 */
	private static String maskString(String strText, int start, int end, char maskChar) {

		if (strText == null || strText.equals(StringUtils.EMPTY)) {
			return StringUtils.EMPTY;
		}
		if (start < 0) {
			start = 0;
		}
		if (end > strText.length()) {
			end = strText.length();
		}
		if (start > end) {
			throw new IllegalArgumentException("End index cannot be greater than start index");
		}
		int maskLength = end - start;

		if (maskLength == 0) {
			return strText;
		}
		StringBuilder sbMaskString = new StringBuilder(maskLength);

		for (int i = 0; i < maskLength; i++) {
			sbMaskString.append(maskChar);
		}

		return strText.substring(0, start) + sbMaskString.toString() + strText.substring(start + maskLength);
	}
}
