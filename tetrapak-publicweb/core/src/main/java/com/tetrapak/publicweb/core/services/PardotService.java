package com.tetrapak.publicweb.core.services;

import java.util.Map;

import com.google.gson.JsonObject;

/**
 * API GEE Service class.
 */
public interface PardotService {

    /**
     * Gets the busines inquiry service URL.
     *
     * @return the busines inquiry service URL
     */
    String getBusinesInquiryServiceURL();

    /**
     * Gets the subscription form pardot URL.
     *
     * @return the subscription form pardot URL
     */
    String getSubscriptionFormPardotURL();

    /**
     * Submit pardot post respose.
     *
     * @param parameters
     *            the parameters
     * @param url
     *            the url
     */
    void submitPardotPostRespose(Map<String, String[]> parameters, String url);

    /**
     * Submit pardot post respose.
     *
     * @param parameterMap
     *            the parameter map
     */
    void submitPardotPostRespose(Map<String, String[]> parameterMap);
  
    /**
     * Gets the manage pref api url.
     *
     * @return the manage pref api url
     */
    String getManagePrefApiUrl();
   
    /**
     * Gets the manage pref api credentials.
     *
     * @return the manage pref api credentials
     */
    String getManagePrefApiCredentials();
    
    /**
     * Gets the manage pref json.
     *
     * @return the manage pref json
     */
	JsonObject getManagePrefJson(String emailToCheck);
}
