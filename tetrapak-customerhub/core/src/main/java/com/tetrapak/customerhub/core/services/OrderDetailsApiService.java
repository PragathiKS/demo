package com.tetrapak.customerhub.core.services;

import com.google.gson.JsonObject;

/**
 * Tetra Pak Order Detail Service
 * @author Nitin Kumar
 */
@FunctionalInterface
public interface OrderDetailsApiService {

    /** Method to get order details from TETRA PAK API
     * @param orderNumber order number
     * @param token token
     * @param orderType order type
     * @return json object
     */
    JsonObject getOrderDetails(String orderNumber, String token, String orderType);
}
