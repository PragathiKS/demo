package com.tetrapak.customerhub.core.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.SlingHttpServletResponse;

import java.io.IOException;

/**
 * Utility class for http methods
 *
 * @author Nitin Kumar
 */
public class HttpUtil {

    /**
     * private constructor
     */
    private HttpUtil() {
        //adding private constructor
    }


    /**
     * @param jsonResponse json response
     * @param httpResponse http response
     * @return jsonResponse json response
     * @throws IOException IO Exception
     */
    public static JsonObject setJsonResponse(JsonObject jsonResponse, HttpResponse httpResponse) throws IOException {
        String responseString = EntityUtils.toString(httpResponse.getEntity());
        jsonResponse.addProperty(CustomerHubConstants.RESULT, responseString);
        jsonResponse.addProperty("status", httpResponse.getStatusLine().getStatusCode());
        return jsonResponse;
    }

    /**
     * Method to write json response
     *
     * @param response     Http response
     * @param jsonResponse json response
     * @throws IOException IO Exception
     */
    public static void writeJsonResponse(SlingHttpServletResponse response, JsonObject jsonResponse) throws IOException {
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse.toString());
    }

    /**
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
}
