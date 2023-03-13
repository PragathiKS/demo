package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.tetrapak.publicweb.core.beans.DropdownOption;
import com.tetrapak.publicweb.core.services.CountryDetailService;
import com.tetrapak.publicweb.core.services.PardotService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import com.tetrapak.publicweb.core.utils.PageUtil;

/**
 * The Class SoftConversionModel.
 *
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SoftConversionModel extends FormModel {

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;
   
    /** The resource. */
    private Resource resource;

    /** The pardot service. */
    @OSGiService
    private PardotService pardotService;

    /** The more link action. */
    @ValueMapValue
    private String moreButtonAction;

    /** The more link label. */
    @ValueMapValue
    private String moreButtonLabel;

    /** The pardot url. */
    @ValueMapValue
    private String pardotUrl;
    
    /** The pardot China url. */
    @ValueMapValue
    private String pardotChinaUrl;

    /** The form config. */
    private SoftConversionFormConfigModel formConfig;

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
        resource = request.getResource();
        if (StringUtils.isNotEmpty(moreButtonAction)) {
            moreButtonAction = LinkUtils.sanitizeLink(moreButtonAction, request);
        }
        setFormConfig();
        setCountryOptions();
    }

    /**
     * Sets the form configs.
     */
    public void setFormConfig() {

        final Resource formConfigResource = GlobalUtil.fetchConfigResource(resource,
                "/jcr:content/root/responsivegrid/softconversionformco");
        if (Objects.nonNull(formConfigResource))
            this.formConfig = formConfigResource.adaptTo(SoftConversionFormConfigModel.class);

        final Resource consentConfigResource = GlobalUtil.fetchConfigResource(resource,
                "/jcr:content/root/responsivegrid/formconsenttextsconf");
        if (Objects.nonNull(consentConfigResource))
            this.consentConfig = consentConfigResource.adaptTo(FormConsentConfigModel.class);
    }

    /**
     * Gets the more link action.
     *
     * @return the more link action
     */
    public String getMoreButtonAction() {
        return moreButtonAction;
    }

    /**
     * Gets the more button label.
     *
     * @return the more button label
     */
    public String getMoreButtonLabel() {
        return moreButtonLabel;
    }

    /**
     * Gets the api url.
     *
     * @return the api url
     */
    public String getApiUrl() {
        return resource.getPath() + ".pardotsoftconversion.json";
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
     * Gets the pardot url.
     *
     * @return the pardot url
     */
    public String getPardotUrl() {
        return pardotUrl;
    }

    /**
     * Gets the pardot china url.
     *
     * @return the pardot china url
     */
    public String getPardotChinaUrl() {
        return LinkUtils.getUrlWithoutProtocol(pardotChinaUrl);
    }

    /**
     * Gets the form config.
     *
     * @return the form config
     */
    public SoftConversionFormConfigModel getFormConfig() {
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
    
    /**
     * Gets the position options.
     *
     * @return the position options
     */
    public List<DropdownOption> getPositionOptions(){
    	return fetchTags(formConfig.getPositionTagsPath());
    }
    
    /**
     * Gets the function options.
     *
     * @return the function options
     */
    public List<DropdownOption> getFunctionOptions(){
    	return fetchTags(formConfig.getFunctionTagsPath());
    }
    
    /**
     * Fetch tags.
     *
     * @param tagPath the tag path
     * @return the list
     */
    private List<DropdownOption> fetchTags(String tagPath){
    	final List<DropdownOption> tagOptions = new ArrayList<>();
    	if(StringUtils.isNotEmpty(tagPath)) {
	    	final TagManager tagManager = resource.getResourceResolver().adaptTo(TagManager.class);
	    	final Tag rootTag = tagManager.resolve(tagPath);
	    	if(Objects.nonNull(rootTag)) {
		    	final Iterator<Tag> childTagsIterator = rootTag.listChildren();
		    	while(childTagsIterator.hasNext()) {
		    		DropdownOption option = new DropdownOption();
		    		final Tag tag = childTagsIterator.next();
                    option.setKey(tag.getTitle());
                    option.setValue(tag.getTitle(PageUtil.getPageLocale(PageUtil.getCurrentPage(resource))));
		    		tagOptions.add(option);
		    	}
	    	}	
    	}
    	return tagOptions;
    }
}
