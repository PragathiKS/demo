package com.tetrapak.publicweb.core.models;

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
import org.osgi.service.component.annotations.Reference;

import com.tetrapak.publicweb.core.beans.DropdownOption;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.CountryDetailService;
import com.tetrapak.publicweb.core.services.PardotService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import com.tetrapak.publicweb.core.utils.PageUtil;

/**
 * The Class SubscriptionFormModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SubscriptionFormModel extends FormModel {

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The resource. */
    @Reference
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

    /** The more link action. */
    @ValueMapValue
    private String moreButtonActionSubscription;

    /** The more link label. */
    @ValueMapValue
    private String moreButtonLabelSubscription;

    /** The pardot url. */
    @ValueMapValue
    private String pardotUrlSubscription;
    
    /** The pardot China url. */
    @ValueMapValue
    private String pardotChinaUrlSubscription;

    /** The heading. */
    @ValueMapValue
    private String headingSubscription;

    /** The market site subscribed. */
    private String marketSiteSubscribed;

    /**
     * The init method.
     */
    @PostConstruct
    protected void init() {
        resource = request.getResource();
        if (StringUtils.isNotEmpty(moreButtonActionSubscription)) {
            moreButtonActionSubscription = LinkUtils.sanitizeLink(moreButtonActionSubscription, request);
        }
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
		this.countryOptions = countryService.fetchPardotCountryList(resource.getResourceResolver());
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
     * Gets the pardot url.
     *
     * @return the pardot url
     */
    public String getPardotUrlSubscription() {
        return pardotUrlSubscription;
    }

    /**
     * Gets the pardot china url subscription.
     *
     * @return the pardot china url subscription
     */
    public String getPardotChinaUrlSubscription() {
        return LinkUtils.getUrlWithoutProtocol(pardotChinaUrlSubscription);
    }

    /**
     * Gets the more link action.
     *
     * @return the more link action
     */
    public String getMoreButtonActionSubscription() {
        return moreButtonActionSubscription;
    }

    /**
     * Gets the more button label.
     *
     * @return the more button label
     */
    public String getMoreButtonLabelSubscription() {
        return moreButtonLabelSubscription;
    }

    /**
     * Gets the heading.
     *
     * @return the heading
     */

    public String getHeadingSubscription() {
        return headingSubscription;
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
