package com.tetrapak.publicweb.core.services;

import java.util.List;

import org.apache.sling.api.resource.ResourceResolver;

import com.tetrapak.publicweb.core.beans.ContactUs;
import com.tetrapak.publicweb.core.beans.DropdownOption;

/**
 * The Interface CountryDetailService.
 */
public interface CountryDetailService {
    
    /**
     * Fetch pardot country list.
     *
     * @param resourceResolver
     *            the resource resolver
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
     * Gets the pardot country cf root path.
     *
     * @return the pardot country cf root path
     */
    String getPardotCountryCfRootPath();

}
