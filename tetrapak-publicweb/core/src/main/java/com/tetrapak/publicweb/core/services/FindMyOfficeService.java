package com.tetrapak.publicweb.core.services;

import java.util.Map;

import org.apache.sling.api.resource.ResourceResolver;

import com.tetrapak.publicweb.core.beans.CountryBean;
import com.tetrapak.publicweb.core.models.FindMyOfficeModel;

/**
 * The Interface FindMyOfficeService.
 */
public interface FindMyOfficeService {

    /**
     * Gets the find my office data.
     *
     * @param resourceResolver
     *            the resource resolver
     * @return the find my office data
     */
    Map<String, CountryBean> getFindMyOfficeData(ResourceResolver resourceResolver, FindMyOfficeModel findMyOfficeModel);
    
    /**
     * Gets the corporate office list.
     *
     * @return the corporate office list
     */
    Map<String, CountryBean> getCorporateOfficeList();

    /**
     * Gets the google api key.
     *
     * @return the google api key
     */
    String getGoogleApiKey();

    /**
     * Gets the country cf root path.
     *
     * @return the country cf root path
     */
    String getCountryCfRootPath();

    /**
     * Gets the office cf root path.
     *
     * @return the office cf root path
     */
    String getOfficeCfRootPath();

}
