package com.tetrapak.publicweb.core.services;

import java.util.List;
import java.util.Map;
import com.google.gson.JsonObject;
import com.tetrapak.publicweb.core.beans.NewsEventBean;
import com.tetrapak.publicweb.core.beans.pxp.BearerToken;

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
     * Gets the manage pref json.
     *
     * @param emailToCheck
     *            the email to check
     * @return the manage pref json
     */
	JsonObject getManagePrefJson(String emailToCheck);
    
    /**
     * Gets the pardot subscriber data api url.
     *
     * @return the pardot subscriber data api url
     */
    String getPardotSubscriberApiUrl();
    
    /**
     * Gets the pardot token generation url.
     *
     * @return the pardot token generation url
     */
    String getPardotTokenGenerationUrl();
    
    /**
     * Gets the bearer token.
     *
     * @return the bearer token
     */
    BearerToken getBearerToken();
    
    
    /**
     * Gets the subscriber mail addresses.
     *
     * @param bean
     *            the bean
     * @return the subscriber mail addresses
     */
    List<String> getSubscriberMailAddresses(NewsEventBean bean);
}
