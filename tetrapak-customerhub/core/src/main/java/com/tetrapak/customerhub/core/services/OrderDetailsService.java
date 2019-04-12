package com.tetrapak.customerhub.core.services;

import com.google.gson.JsonObject;

/**
 * Tetra Pak Order Detail Service
 * @author Nitin Kumar
 */
@FunctionalInterface
public interface OrderDetailsService {

    JsonObject getOrderDetails(String orderNumber, String token);
}
