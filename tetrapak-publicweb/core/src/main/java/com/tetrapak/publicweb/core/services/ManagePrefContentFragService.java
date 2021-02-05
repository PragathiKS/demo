package com.tetrapak.publicweb.core.services;

import java.util.List;
import org.apache.sling.api.resource.ResourceResolver;
import com.tetrapak.publicweb.core.beans.DropdownOption;

/**
 * @author ojaswarn
 * The Interface ManagePrefContentFragService.
 */
public interface ManagePrefContentFragService {

    /**
     * Fetch country list.
     *
     * @param resourceResolver
     *            the resource resolver
     * @return the list
     */
    List<DropdownOption> fetchCountryList(ResourceResolver resourceResolver);
    
    /**
     * Fetch language list.
     *
     * @param resourceResolver
     *            the resource resolver
     * @return the list
     */
    List<DropdownOption> fetchLanguageList(ResourceResolver resourceResolver);

    /**
     * Gets the country cf root path.
     *
     * @return the country cf root path
     */
    String getCountryCfRootPath();
    
    /**
     * Gets the language cf root path.
     *
     * @return the language cf root path
     */
    String getLanguageCfRootPath();
}
