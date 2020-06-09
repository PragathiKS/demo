package com.tetrapak.publicweb.core.services;

import java.util.Map;

/**
 * API GEE Service class
 */
public interface PadrotService {

    String getBusinesInquiryServiceURL();

    void submitPadrotPostRespose(Map<String, String[]> parameters, String url);

}

