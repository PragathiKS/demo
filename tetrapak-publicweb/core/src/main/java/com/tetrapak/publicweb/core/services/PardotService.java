package com.tetrapak.publicweb.core.services;

import java.util.List;
import java.util.Map;

import org.json.JSONException;

import com.google.gson.JsonObject;
import com.tetrapak.publicweb.core.beans.pxp.BearerToken;

/**
 * The Interface PardotService.
 */
public interface PardotService {

    /**
	 * Gets the subscription form pardot URL.
	 *
	 * @return the subscription form pardot URL
	 */
    String getSubscriptionFormPardotURL();

    /**
	 * Submit pardot post respose.
	 *
	 * @param parameters the parameters
	 * @param url        the url
	 */
    void submitPardotPostRespose(Map<String, String[]> parameters, String url);

    /**
	 * Submit pardot post respose.
	 *
	 * @param parameterMap the parameter map
	 */
    void submitPardotPostRespose(Map<String, String[]> parameterMap);
  
	/**
	 * Gets the manage pref json.
	 *
	 * @param emailToCheck the email to check
	 * @return the manage pref json
	 */
	JsonObject getManagePrefJson(String emailToCheck);
    
    /**
	 * Gets the pardot subscriber api url.
	 *
	 * @return the pardot subscriber api url
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
	 * @param locale        the locale
	 * @param interestAreas the interest areas
	 * @return the subscriber mail addresses
     * @throws JSONException 
	 */
    List<String> getSubscriberMailAddresses(String locale, List<String> interestAreas) throws JSONException;

    /**
     * Gets the bearer token for custom form service.
     *
     * @return the bearer token for custom form service
     */
    BearerToken getBearerTokenForCustomFormService();

    /**
     * Submit custom form service post response.
     *
     * @param parameters
     *            the parameters
     */
    void submitcustomFormServicePostResponse(Map<String, String[]> parameters) throws Exception;
}
