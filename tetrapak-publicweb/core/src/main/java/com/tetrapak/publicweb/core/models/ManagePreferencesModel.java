package com.tetrapak.publicweb.core.models;

import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.tetrapak.publicweb.core.beans.DropdownOption;
import com.tetrapak.publicweb.core.constants.FormConstants;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.ManagePrefContentFragService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;

/**
 * @author ojaswarn
 * The Class ManagePreferencesModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ManagePreferencesModel extends FormModel {

	/** The resource. */
	@Self
	@Via("resource")
	private Resource resource;

	/** The cf service. */
	@OSGiService
	private ManagePrefContentFragService cfService;

	/** The country options. */
	private List<DropdownOption> countryOptions;

	/** The language options. */
	private List<DropdownOption> languageOptions;

	/** The email. */
	private String email;

	/** The config items. */
	private ManagePreferencesConfigModel configItems;
	
	/** The request. */
	@Self
	private SlingHttpServletRequest request;

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
		if (Objects.nonNull(request.getRequestParameter(FormConstants.EMAIL))
				&& StringUtils.isNotBlank(request.getRequestParameter(FormConstants.EMAIL).getString())) {
			this.email = maskEmailAddress(request.getRequestParameter(FormConstants.EMAIL).getString(), '*');
		} else {
			this.email = maskEmailAddress("ojaswa.swarnkar@publicissapient.com", '*');
		}
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	protected void init() {
		setCountryOptions();
		setLanguageOptions();
		setConfigItems();
		setEmail();
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

		String[] parts = strEmail.split("@");

		String firstPartMask;
		
		if (parts[0].length() < PWConstants.NUMBER_FOUR) {
			firstPartMask = maskString(parts[0], 0, parts[0].length(), maskChar);
		} else {
			firstPartMask = maskString(parts[0], 1, parts[0].length() - 1, maskChar);
		}
		String secondPartMask = parts[1].split("\\.")[0];
		for (int i = 0; i < secondPartMask.length(); i++) {
			if (i % PWConstants.NUMBER_TWO != 0) {
				secondPartMask = secondPartMask.substring(0, i - 1) + maskChar + secondPartMask.substring(i, secondPartMask.length());
			}
		}
		secondPartMask = secondPartMask +"." +parts[1].split("\\.")[1];
		
		return firstPartMask + "@" + secondPartMask;
	}

	/**
	 * Mask string.
	 *
	 * @param strText the str text
	 * @param start the start
	 * @param end the end
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
		if(start > end) {
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

		return strText.substring(0, start) + sbMaskString.toString() + strText.substring(start+maskLength);
	}
}
