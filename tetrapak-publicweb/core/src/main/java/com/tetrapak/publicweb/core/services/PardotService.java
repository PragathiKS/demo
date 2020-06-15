package com.tetrapak.publicweb.core.services;

import java.util.Map;

/**
 * API GEE Service class
 */
public interface PardotService {

    String getBusinesInquiryServiceURL();

    String getSubscriptionFormPardotURL();

    void submitPardotPostRespose(Map<String, String[]> parameters, String url);

    void submitPardotPostRespose(Map<String, String[]> parameterMap);

}

