package com.tetrapak.customerhub.core.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import org.apache.commons.compress.utils.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.SlingHttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

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
        jsonResponse.addProperty(CustomerHubConstants.STATUS, httpResponse.getStatusLine().getStatusCode());
        return jsonResponse;
    }

    /**
     * Method to set json response with custom info.
     *
     * @param jsonResponse json response
     * @param message message
     * @return jsonResponse json response
     */
    public static JsonObject setJsonResponse(JsonObject jsonResponse, String message, int code) {
        jsonResponse.addProperty(CustomerHubConstants.RESULT, message);
        jsonResponse.addProperty(CustomerHubConstants.STATUS, code);
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
        getRequest.addHeader(AUTHORIZATION_HEADER_NAME, BEARER_COOKIE_VALUE + token);
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
        org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64();
        byte[] base64DecodedByteArray = base64.decode(encodedStr);
        return new String(base64DecodedByteArray, Charsets.UTF_8);
    }

    /**
     * This method would encode the String passed to this method
     *
     * @param str String
     * @return encoded String
     */
    public static String encodeStr(String str) {
        if (StringUtils.isEmpty(str)) {
            return StringUtils.EMPTY;
        }
        org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64();
        byte[] enc = new byte[0];
        try {
            byte[] utf8 = str.getBytes(StandardCharsets.UTF_8);
            Cipher cipher;
            String customerhubKey = "9NrKZiHIMJvjV1Fp";
            Key key = new SecretKeySpec(customerhubKey.getBytes(StandardCharsets.UTF_8), "AES");
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            utf8 = cipher.doFinal(utf8);
            enc = base64.encode(utf8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            LOGGER.error("NoSuchAlgorithmException", e);
            return StringUtils.EMPTY;
        } catch (BadPaddingException e) {
            LOGGER.error("BadPaddingException", e);
        } catch (IllegalBlockSizeException e) {
            LOGGER.error("IllegalBlockSizeException", e);
        } catch (InvalidKeyException e) {
            LOGGER.error("InvalidKeyException", e);
        }
        return org.apache.commons.codec.binary.StringUtils.newStringUtf8(enc);
    }

    public static JsonObject sendAPIGeePostWithEntity(String url, String token, String entity) {

        HttpPost apiRequest = new HttpPost(url);

        apiRequest.addHeader(AUTHORIZATION_HEADER_NAME, BEARER_COOKIE_VALUE + token);
        apiRequest.addHeader("Content-Type", "application/json");
        apiRequest.addHeader("accept", "*/*");
        apiRequest.setEntity(new StringEntity(entity, StandardCharsets.UTF_8));
        return executeRequest(apiRequest);
    }

    public static JsonObject sendAPIGeePostWithFiles(String url, String token, File file) {
        if (file == null) {
            return null;
        }
        HttpPost apiRequest = new HttpPost(url);
        apiRequest.addHeader(AUTHORIZATION_HEADER_NAME, BEARER_COOKIE_VALUE + token);
        apiRequest.addHeader("accept", "*/*");

        FileBody uploadFilePart = new FileBody(file);
        HttpEntity requestEntity = MultipartEntityBuilder.create()
                .addPart(FILE_PARAM_NAME, uploadFilePart).build();
        apiRequest.setEntity(requestEntity);

        return executeRequest(apiRequest);
    }

    private static JsonObject executeRequest(HttpPost apiRequest) {
        JsonObject jsonResponse = new JsonObject();
        HttpClient client = HttpClientBuilder.create().build();
        try {
            HttpResponse res = client.execute(apiRequest);
            jsonResponse = HttpUtil.setJsonResponse(jsonResponse, res);
            LOGGER.debug("request sent: " + res.getStatusLine());
        } catch (UnsupportedEncodingException e) {
            jsonResponse = HttpUtil.setJsonResponse(jsonResponse, "Unsupported Encoding", HttpStatus.SC_INTERNAL_SERVER_ERROR);
            LOGGER.error("Unsupported Encoding while sending post request" + e);
        } catch (ClientProtocolException e) {
            jsonResponse = HttpUtil.setJsonResponse(jsonResponse, "ClientProtocolException Error", HttpStatus.SC_INTERNAL_SERVER_ERROR);
            LOGGER.error("ClientProtocolException Error while sending post request" + e);
        } catch (IOException e) {
            jsonResponse = HttpUtil.setJsonResponse(jsonResponse, "IO Error", HttpStatus.SC_INTERNAL_SERVER_ERROR);
            LOGGER.error("IO Error while sending post request" + e);
        }
        return jsonResponse;
    }

    /**
     *
     * This method is a variation of getJsonObject provided in the same file. This method does not parse the API
     * response into JSON Object; instead it returns the response AS IS in the form of a string.
     *
     * @param token Authorisation TOken
     * @param url HTTP End-point URL
     * @return Map Map containing HTTP call response and HTTP call status code
     */
    public static Map<String, String> executeHttp(String token, String url) {
        Map<String, String> responseMap = new HashMap<>();
        HttpGet getRequest = new HttpGet(url);
        getRequest.addHeader("Authorization", "Bearer " + token);
        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            StopWatch watch = new StopWatch();
            watch.start();
            HttpResponse httpResponse = httpClient.execute(getRequest);
            watch.stop();
            LOGGER.debug("Http Post request status code: {}", httpResponse.getStatusLine().getStatusCode());
            LOGGER.debug("Time taken for the call to URL : {} is {}", url, watch.getTime());
            String responseString = StringUtils.EMPTY;
            if (httpResponse.getEntity() != null) {
                responseString = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
            }
            responseMap.put(CustomerHubConstants.RESULT, responseString);
            responseMap.put(CustomerHubConstants.STATUS, String.valueOf(httpResponse.getStatusLine().getStatusCode()));

        } catch (IOException e) {
            LOGGER.error("Error while making HTTP call for the URL {}", url, e);
        }
        return responseMap;
    }

}
