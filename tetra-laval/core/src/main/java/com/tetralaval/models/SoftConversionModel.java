package com.tetralaval.models;

import com.tetralaval.beans.DropdownOption;
import com.tetralaval.services.CountryDetailService;
import com.tetralaval.services.PardotService;
import com.tetralaval.utils.GlobalUtil;
import com.tetralaval.utils.LinkUtils;
import com.tetralaval.utils.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;

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
}
