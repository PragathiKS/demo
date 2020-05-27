package com.tetrapak.publicweb.core.models;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.tetrapak.publicweb.core.beans.DropdownOption;
import com.tetrapak.publicweb.core.services.CountryDetailService;

/**
 * The Class ContactUsModel.
 *
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContactUsModel extends FormModel {

    /** The resource. */
    @Self
    private Resource resource;

    /** The how can we help text. */
    @ValueMapValue
    private String howCanWeHelpText;

    /** The country options. */
    private List<DropdownOption> countryOptions;

    /** The country detail service. */
    @OSGiService
    private CountryDetailService countryDetailService;

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        setCountryOptions();
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
     * Gets the how can we help text.
     *
     * @return the how can we help text
     */
    public String getHowCanWeHelpText() {
        return howCanWeHelpText;
    }

    /**
     * Fetches country list from content fragments.
     *
     */
    private void setCountryOptions() {
        this.countryOptions = countryDetailService.fetchCountryList(resource.getResourceResolver());
    }

    /**
     * Gets the servlet path.
     *
     * @return the servlet path
     */
    public String getServletPath() {
        return resource.getPath() + ".sendmail.json";
    }

}
