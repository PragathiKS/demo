package com.tetrapak.supplierportal.core.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.SlingHttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;

/**
 * Utility class for http methods
 *
 * @author Nitin Kumar
 */
public final class HttpUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);
    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String BEARER_COOKIE_VALUE = "Bearer ";
    private static final String FILE_PARAM_NAME = "file";

    /**
     * private constructor
     */
    private HttpUtil() {
        // adding private constructor
    }

    /**
     * Method to set json response with data from API
     *
     * @param jsonResponse json response
     * @param httpResponse http response
     * @return jsonResponse json response
     * @throws IOException IO Exception
     */
    public static JsonObject setJsonResponse(JsonObject jsonResponse, HttpResponse httpResponse) throws IOException {
        String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        jsonResponse.addProperty(SupplierPortalConstants.RESULT, responseString);
        jsonResponse.addProperty(SupplierPortalConstants.STATUS, httpResponse.getStatusLine().getStatusCode());
        return jsonResponse;
    }

    /**
     * Method to set json response with custom info.
     *
     * @param jsonResponse json response
     * @param message      message
     * @return jsonResponse json response
     */
    public static JsonObject setJsonResponse(JsonObject jsonResponse, String message, int code) {
        jsonResponse.addProperty(SupplierPortalConstants.RESULT, message);
        jsonResponse.addProperty(SupplierPortalConstants.STATUS, code);
        return jsonResponse;
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
    
    /**
     * Method to get String data without escape characters \n and \
     *
     * @param result json element
     * @return result string
     */
    public static String getStringFromJsonWithoutEscape(JsonElement result) {
    	String resultString = result.toString();
        resultString = resultString.replaceAll("\\\\n", "");
        resultString = resultString.replaceAll("\\\\", "");
        resultString = StringUtils.substringAfter(resultString, "\"");
        resultString = StringUtils.substringBeforeLast(resultString, "\"");
        return resultString;
    }
    
    /**
     * Method to return send error message
     *
     * @param response response
     */
    public static void sendErrorMessage(SlingHttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        try {
            JsonObject obj = new JsonObject();
            obj.addProperty("errorMsg", "Some internal server error occurred while processing the request!");
            HttpUtil.writeJsonResponse(response, obj);
        } catch (IOException e) {
            LOGGER.error("IOException: ", e);
        }
    }


}