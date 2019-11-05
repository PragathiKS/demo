package com.tetrapak.customerhub.core.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import org.apache.commons.compress.utils.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.SlingHttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Utility class for http methods
 *
 * @author Nitin Kumar
 */
public final class HttpUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * private constructor
     */
    private HttpUtil() {
        //adding private constructor
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

    /**
     * Method to return json object
     *
     * @param token        token
     * @param jsonResponse json response
     * @param url          url
     * @return json object
     */
    public static JsonObject getJsonObject(String token, JsonObject jsonResponse, String url) {
        HttpGet getRequest = new HttpGet(url);
        getRequest.addHeader("Authorization", "Bearer " + token);
        HttpClient httpClient = HttpClientBuilder.create().build();

        try {
            HttpResponse httpResponse = httpClient.execute(getRequest);
            LOGGER.debug("Http Post request status code: {}", httpResponse.getStatusLine().getStatusCode());

            jsonResponse = HttpUtil.setJsonResponse(jsonResponse, httpResponse);

        } catch (ClientProtocolException e) {
            LOGGER.error("ClientProtocolException in OrderDetailsApiServiceImpl", e);
        } catch (IOException e) {
            LOGGER.error("IOException in OrderDetailsApiServiceImpl", e);
        }
        return jsonResponse;
    }

    /**
     * This method would decode the String passed to this method
     *
     * @param encodedStr encoded String
     * @return string decoded String
     */
    public static String decodeStr(String encodedStr) {
        org.apache.commons.codec.binary.Base64 base64Decoder = new org.apache.commons.codec.binary.Base64();
        byte[] base64DecodedByteArray = base64Decoder.decode(encodedStr);
        return new String(base64DecodedByteArray, Charsets.UTF_8);
    }

    /**
     * This method would encode the String passed to this method
     *
     * @param encodeStr String
     * @return encoded String
     */
    public static String encodeStr(String encodeStr) {
        org.apache.commons.codec.binary.Base64 base64Decoder = new org.apache.commons.codec.binary.Base64();
        if (StringUtils.isEmpty(encodeStr)) {
            return StringUtils.EMPTY;
        }
        return base64Decoder.encodeAsString(encodeStr.getBytes());
    }

}
