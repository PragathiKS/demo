package com.tetrapak.publicweb.core.models;

import java.util.*;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.tetrapak.publicweb.core.beans.DropdownOption;
import com.tetrapak.publicweb.core.services.CountryDetailService;
import com.tetrapak.publicweb.core.services.PardotService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;
import com.tetrapak.publicweb.core.utils.PageUtil;

/**
 * The Class BusinessInquiryModel.
 *
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BusinessInquiryModel extends FormModel {

	/** The resource. */
	@Self
	private Resource resource;

	/** The pardot service. */
	@OSGiService
	private PardotService pardotService;

	private FormConfigModel formConfig;

	private FormConsentConfigModel consentConfig;

	/** The country options. */
	private List<DropdownOption> countryOptions;

	/** The country detail service. */
	@OSGiService
	private CountryDetailService countryDetailService;

	/**
	 * The init method.
	 */
	@PostConstruct
	protected void init() {
		setCountryOptions();
		setFormConfig();
	}

	/**
	 * Sets the form configs.
	 */
	public void setFormConfig() {

		final Resource formConfigResource = GlobalUtil.fetchConfigResource(resource,
				"/jcr:content/root/responsivegrid/businessinquiryformc");
		if (Objects.nonNull(formConfigResource))
			this.formConfig = formConfigResource.adaptTo(FormConfigModel.class);

		final Resource consentConfigResource = GlobalUtil.fetchConfigResource(resource,
				"/jcr:content/root/responsivegrid/formconsenttextsconf");
		if (Objects.nonNull(consentConfigResource))
			this.consentConfig = consentConfigResource.adaptTo(FormConsentConfigModel.class);
	}

	/**
	 * Get the tags Value for displaying Processing Roles.
	 *
	 */
	public Map<String, String> getTagProcessingRoles() {
		final String[] tagsCheck = formConfig.getProfileTags();
		final String rootTag = tagsCheck[Arrays.asList(tagsCheck).indexOf("pardot-system-config:processing-roles")];
		final ResourceResolver resolver = resource.getResourceResolver();
		final TagManager tagManager = resolver.adaptTo(TagManager.class);
		final Tag tag = tagManager.resolve(rootTag);
		final Iterator<Tag> tagIterator = tag.listChildren();
		final Map<String, String> tagsValues = new LinkedHashMap<>();
		String otherTagName = StringUtils.EMPTY;
		String otherTagTitle = StringUtils.EMPTY;
		while (tagIterator.hasNext()) {
			final Tag childtag = tagIterator.next();
			final String defaultTagTitle = childtag.getTitle();
			final String tagName = childtag.getName();
			final String localizedTagTitle = childtag
					.getLocalizedTitle(PageUtil.getPageLocale(PageUtil.getCurrentPage(resource)));

			if (tagName.equalsIgnoreCase("other")) {
				otherTagTitle = localizedTagTitle != null ? localizedTagTitle : defaultTagTitle;
				otherTagName = childtag.getName();
			} else {
				if (null != localizedTagTitle) {
					tagsValues.put(tagName, localizedTagTitle);
				} else {
					tagsValues.put(tagName, defaultTagTitle);
				}
			}
		}
		tagsValues.put(otherTagName, otherTagTitle);
		return tagsValues;
	}

	/**
	 * Get the tags Value for displaying Function.
	 *
	 */
	public Map<String, String> getTagFunctions() {
		final String[] tagsCheck = formConfig.getProfileTags();
		final String rootTag = tagsCheck[Arrays.asList(tagsCheck).indexOf("pardot-system-config:function")];
		final ResourceResolver resolver = resource.getResourceResolver();
		final TagManager tagManager = resolver.adaptTo(TagManager.class);
		final Tag tag = tagManager.resolve(rootTag);
		final Iterator<Tag> tagIterator = tag.listChildren();
		final Map<String, String> tagsValues = new LinkedHashMap<>();
		String otherTagName = StringUtils.EMPTY;
		String otherTagTitle = StringUtils.EMPTY;
		while (tagIterator.hasNext()) {
			final Tag childtag = tagIterator.next();
			final String defaultTagTitle = childtag.getTitle();
			final String tagName = childtag.getName();
			final String localizedTagTitle = childtag
					.getLocalizedTitle(PageUtil.getPageLocale(PageUtil.getCurrentPage(resource)));

			if (tagName.equalsIgnoreCase("other")) {
				otherTagTitle = localizedTagTitle != null ? localizedTagTitle : defaultTagTitle;
				otherTagName = childtag.getName();
			} else {
				if (null != localizedTagTitle) {
					tagsValues.put(tagName, localizedTagTitle);
				} else {
					tagsValues.put(tagName, defaultTagTitle);
				}
			}
		}
		tagsValues.put(otherTagName, otherTagTitle);
		return tagsValues;
	}


	/**
	 * Get the tags Value for displaying Position.
	 *
	 */
	public Map<String, String> getTagTitles() {
		final String[] tagsCheck = formConfig.getProfileTags();
		final String rootTag = tagsCheck[Arrays.asList(tagsCheck).indexOf("pardot-system-config:job-title")];
		final ResourceResolver resolver = resource.getResourceResolver();
		final TagManager tagManager = resolver.adaptTo(TagManager.class);
		final Tag tag = tagManager.resolve(rootTag);
		final Iterator<Tag> tagIterator = tag.listChildren();
		final Map<String, String> tagsValues = new LinkedHashMap<>();
		String otherTagName = StringUtils.EMPTY;
		String otherTagTitle = StringUtils.EMPTY;
		while (tagIterator.hasNext()) {
			final Tag childtag = tagIterator.next();
			final String defaultTagTitle = childtag.getTitle();
			final String tagName = childtag.getName();
			final String localizedTagTitle = childtag
					.getLocalizedTitle(PageUtil.getPageLocale(PageUtil.getCurrentPage(resource)));

			if (tagName.equalsIgnoreCase("other")) {
				otherTagTitle = localizedTagTitle != null ? localizedTagTitle : defaultTagTitle;
				otherTagName = childtag.getName();
			} else {
				if (null != localizedTagTitle) {
					tagsValues.put(tagName, localizedTagTitle);
				} else {
					tagsValues.put(tagName, defaultTagTitle);
				}
			}
		}
		tagsValues.put(otherTagName, otherTagTitle);
		return tagsValues;
	}

	public String getApiUrl() {
		return resource.getPath() + ".pardotbusinessenquiry.json";
	}

	/**
	 * Gets the site language.
	 *
	 * @return the site language
	 */
	public String getSiteLanguage() {
		return PageUtil.getLanguageCodeFromResource(resource);
	}

	/**
	 * Gets the site country.
	 *
	 * @return the site country
	 */
	public String getSiteCountry() {
		return PageUtil.getMarketCode(resource);
	}

	/**
	 * Gets the form config.
	 *
	 * @return the form config
	 */
	public FormConfigModel getFormConfig() {
		return formConfig;
	}

	/**
	 * Gets the consent config.
	 *
	 * @return the consent config
	 */
	public FormConsentConfigModel getConsentConfig() {
		return consentConfig;
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
	 * Fetches country list from content fragments.
	 *
	 */
	private void setCountryOptions() {
		this.countryOptions = countryDetailService.fetchPardotCountryList(resource.getResourceResolver());

	}

}
