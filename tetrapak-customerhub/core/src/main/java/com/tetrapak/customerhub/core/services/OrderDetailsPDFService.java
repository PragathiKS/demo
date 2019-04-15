package com.tetrapak.customerhub.core.services;

import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

/**
 * Tetra Pak Order Detail Service
 * @author Nitin Kumar
 */
@FunctionalInterface
public interface OrderDetailsPDFService {

    void generateOrderDetailsPDF(SlingHttpServletRequest request, SlingHttpServletResponse response, JsonObject jsonResponse);
}
