package com.tetrapak.publicweb.core.services;

import java.util.Map;

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

}
