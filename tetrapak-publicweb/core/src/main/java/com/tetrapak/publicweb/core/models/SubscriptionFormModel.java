package com.tetrapak.publicweb.core.models;

import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.tetrapak.publicweb.core.beans.DropdownOption;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.CountryDetailService;
import com.tetrapak.publicweb.core.services.PardotService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;
import com.tetrapak.publicweb.core.utils.PageUtil;

/**
 * The Class SubscriptionFormModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SubscriptionFormModel extends FormModel {

    /** The resource. */
    @Self
    private Resource resource;

    /** The pardot service. */
    @OSGiService
    private PardotService pardotService;
    
    /** The country service. */
    @OSGiService
    private CountryDetailService countryService;

    /** The form config. */
    private FormConfigModel formConfig;

    /** The consent config. */
    private FormConsentConfigModel consentConfig;
    
    /** The country list. */
    private List<DropdownOption> countryOptions;
    
    /** The market site subscribed. */
    private String marketSiteSubscribed;

    /**
     * The init method.
     */
    @PostConstruct
    protected void init() {
        setFormConfig();
        setCountryOptions();
        if (PageUtil.getCountryCodeFromResource(resource).equalsIgnoreCase(PWConstants.SE_COUNTRY_CODE)) {
            marketSiteSubscribed = PWConstants.SE_MARKET_LOCALE;
        } else {
            marketSiteSubscribed = PageUtil.getLanguageCodeFromResource(resource) + PWConstants.HYPHEN
                    + PageUtil.getCountryCodeFromResource(resource);
        }
    }

    /**
     * Sets the form config.
     */
    public void setFormConfig() {
        final Resource formConfigResource = GlobalUtil.fetchConfigResource(resource,
                "/jcr:content/root/responsivegrid/subscriptionformconf");
        if (Objects.nonNull(formConfigResource))
            this.formConfig = formConfigResource.adaptTo(FormConfigModel.class);

    	final Resource consentConfigResource = GlobalUtil.fetchConfigResource(resource,
                "/jcr:content/root/responsivegrid/formconsenttextsconf");
        if (Objects.nonNull(consentConfigResource))
            this.consentConfig = consentConfigResource.adaptTo(FormConsentConfigModel.class);
    }

    /**
     * Gets the api url.
     *
     * @return the api url
     */
    public String getApiUrl() {
        return resource.getPath() + ".pardotsubscription.json";
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
        return PageUtil.getCountryCodeFromResource(resource);
    }

	/**
	 * Sets the country options.
	 */
	public void setCountryOptions() {
		this.countryOptions = countryService.fetchCountryList(resource.getResourceResolver());
	}

	/**
	 * Gets the consent config.
	 *
	 * @return the consentConfig
	 */
	public FormConsentConfigModel getConsentConfig() {
		return consentConfig;
	}

	/**
	 * Gets the form config.
	 *
	 * @return the formConfig
	 */
	public FormConfigModel getFormConfig() {
		return formConfig;
	}

	/**
	 * Gets the country options.
	 *
	 * @return the countryOptions
	 */
	public List<DropdownOption> getCountryOptions() {
		return countryOptions;
	}

    /**
     * Gets the market site subscribed.
     *
     * @return the market site subscribed
     */
    public String getMarketSiteSubscribed() {
        return marketSiteSubscribed;
    }

}
