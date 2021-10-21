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
import com.tetrapak.publicweb.core.services.CountryDetailService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;

/**
 * The Class ContactUsModel.
 *
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContactUsModel extends FormModel {

    /** The resource. */
    @Self
    private Resource resource;

    /** The country options. */
    private List<DropdownOption> countryOptions;

    /** The country detail service. */
    @OSGiService
    private CountryDetailService countryDetailService;

    /** The consent config. */
    private FormConsentConfigModel consentConfig;

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        setCountryOptions();
        setConsentConfig();
    }

    /**
     * Sets the form configs.
     */
    public void setConsentConfig() {

        final Resource consentConfigResource = GlobalUtil.fetchConfigResource(resource,
                "/jcr:content/root/responsivegrid/formconsenttextsconf");
        if (Objects.nonNull(consentConfigResource))
            this.consentConfig = consentConfigResource.adaptTo(FormConsentConfigModel.class);
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
     * Fetches country list from pardot countries content fragments.
     *
     */
    private void setCountryOptions() {
        this.countryOptions = countryDetailService.fetchPardotCountryList(resource.getResourceResolver());
    }

    /**
     * Gets the api url.
     *
     * @return the api url
     */
    public String getApiUrl() {
        return resource.getPath() + ".sendmail.json";
    }

    /*     * Gets the consent config.
     *
     * @return the consent config
     */
    public FormConsentConfigModel getConsentConfig() {
        return consentConfig;
    }

}
