package com.tetrapak.supplierportal.core.utils;

import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletResponse;

import java.io.IOException;

/**
 * Utility class for http methods
 *
 * @author Nitin Kumar
 */
public final class HttpUtil {

    /**
     * private constructor
     */
    private HttpUtil() {
        //adding private constructor
    }

    /**
     * Method to write json response
     *
     * @param response     Http response
     * @param jsonResponse json response
     * @throws IOException IO Exception
     */
    public static void writeJsonResponse(SlingHttpServletResponse response, JsonObject jsonResponse)
            throws IOException {
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse.toString());
    }

}
