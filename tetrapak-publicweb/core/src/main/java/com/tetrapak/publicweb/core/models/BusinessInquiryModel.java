package com.tetrapak.publicweb.core.models;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Arrays;
import com.tetrapak.publicweb.core.constants.FormConstants;
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
import com.tetrapak.publicweb.core.utils.LinkUtils;
import com.tetrapak.publicweb.core.utils.PageUtil;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * The Class BusinessInquiryModel.
 *
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BusinessInquiryModel extends FormModel {

    /** The resource. */
    @Self
    private Resource resource;

    /** Business Enquiry Form Pardot URL. */
    @ValueMapValue
    private String befPardotURL;

    /** Business Enquiry China Pardot URL. */
    @ValueMapValue
    private String befChinaPardotURL;

    /** The pardot service. */
    @OSGiService
    private PardotService pardotService;

    /** The form config. */
    private FormConfigModel formConfig;

    /** The consent config. */
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
        if (Objects.nonNull(formConfigResource)) {
            this.formConfig = formConfigResource.adaptTo(FormConfigModel.class);
        }

        final Resource consentConfigResource = GlobalUtil.fetchConfigResource(resource,
                "/jcr:content/root/responsivegrid/formconsenttextsconf");
        if (Objects.nonNull(consentConfigResource)) {
            this.consentConfig = consentConfigResource.adaptTo(FormConsentConfigModel.class);
        }
    }

    /**
     * Get the tags Value for displaying Processing Roles.
     *
     * @return the tag processing roles
     */
    public Map<String, String> getTagProcessingRoles() {
        return getChildTags(FormConstants.PROCESSING_ROLES_TAGS);
    }

    /**
     * Get the tags Value for displaying Function.
     *
     * @return the tag functions
     */
    public Map<String, String> getTagFunctions() {
        return getChildTags(FormConstants.FUNCTION_TAGS);
    }

    /**
     * Get the tags Value for displaying Position.
     *
     * @return the tag titles
     */
    public Map<String, String> getTagTitles() {
        return getChildTags(FormConstants.JOB_TITLE_TAGS);
    }

    /**
     * Gets the child tags.
     * 
     * @param firstLevelTag
     *            first level tag :: ardot-system-config:job-title - job-title
     * @return tag values
     */
    private Map<String, String> getChildTags(final String firstLevelTag) {
        final String[] pardotFieldTags = formConfig.getPardotSystemConfigTags();
        final int firstLevelTagIndex = Arrays.asList(pardotFieldTags).indexOf(firstLevelTag);
        final String rootTag = pardotFieldTags[firstLevelTagIndex];
        final ResourceResolver resolver = resource.getResourceResolver();
        final TagManager tagManager = resolver.adaptTo(TagManager.class);
        final Tag tag = Objects.requireNonNull(tagManager).resolve(rootTag);
        final Iterator<Tag> tagIterator = tag.listChildren();
        final Map<String, String> tagsValues = new LinkedHashMap<>();
        String otherTagName = StringUtils.EMPTY;
        String otherTagTitle = StringUtils.EMPTY;
        while (tagIterator.hasNext()) {
            final Tag childtag = tagIterator.next();
            final String defaultTagTitle = childtag.getTitle();
            final String tagName = childtag.getTitle();
            final String localizedTagTitle = childtag
                    .getLocalizedTitle(PageUtil.getPageLocale(PageUtil.getCurrentPage(resource)));
            if (tagName.equalsIgnoreCase(FormConstants.OTHER)) {
                if (null != localizedTagTitle) {
                    otherTagTitle = localizedTagTitle;
                } else {
                    otherTagTitle = defaultTagTitle;
                }
                otherTagName = childtag.getTitle();
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
     * Gets the api url.
     *
     * @return the api url
     */
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
     * @return countryOptions the country options
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

    /**
     * Get Pardot Service Url.
     *
     * @return befPardotURL
     */
    public String getBefPardotURL() {
        return befPardotURL;
    }

    /**
     * Gets the bef china pardot URL.
     *
     * @return the bef china pardot URL
     */
    public String getBefChinaPardotURL() {
        return LinkUtils.getUrlWithoutProtocol(befChinaPardotURL);
    }

}