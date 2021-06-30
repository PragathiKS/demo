package com.tetralaval.services;

import com.tetralaval.beans.ContactUs;
import com.tetralaval.beans.DropdownOption;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.List;

/**
 * The Interface CountryDetailService.
 */
public interface CountryDetailService {

    /**
     * Fetch country list.
     *
     * @param resourceResolver
     *            the resource resolver
     * @return the list
     */
    List<DropdownOption> fetchCountryList(ResourceResolver resourceResolver);
    
    /**
     * Fetch pardot country list.
     *
     * @param resourceResolver the resource resolver
     * @return the list
     */
    List<DropdownOption> fetchPardotCountryList(ResourceResolver resourceResolver);

    /**
     * Fetch contact email addresses.
     *
     * @param contactUs
     *            the contact us
     * @param resourceResolver
     *            the resource resolver
     * @return the string[]
     */
    String[] fetchContactEmailAddresses(ContactUs contactUs, ResourceResolver resourceResolver);

    /**
     * Gets the country cf root path.
     *
     * @return the country cf root path
     */
    String getCountryCfRootPath();
    
    /**
     * Gets the pardot country cf root path.
     *
     * @return the pardot country cf root path
     */
    String getPardotCountryCfRootPath();

}
