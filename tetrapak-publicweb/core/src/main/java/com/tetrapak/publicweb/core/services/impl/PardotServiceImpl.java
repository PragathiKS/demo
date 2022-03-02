package com.tetrapak.publicweb.core.services.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tetrapak.publicweb.core.beans.pxp.BearerToken;
import com.tetrapak.publicweb.core.constants.FormConstants;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.PardotService;
import com.tetrapak.publicweb.core.services.config.PardotServiceConfig;

/**
 * Impl class for PardotService.
 */
@Component(immediate = true, service = PardotService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = PardotServiceConfig.class)
public class PardotServiceImpl implements PardotService {

    /** The config. */
    private PardotServiceConfig config;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PardotServiceImpl.class);

    /** The success. */
    private static final int SUCESSS = 200;
    
    /** The success. */
    private static final int CREATED = 201;
    
    /** The Constant DATA_FIELD. */
    private static final String DATA_FIELD = "data";

    /**
     * Activate.
     *
     * @param config the config
     */
    @Activate
    public void activate(final PardotServiceConfig config) {
        this.config = config;
    }

    /**
     * Submit pardot post respose.
     *
     * @param parameters the parameters
     * @param url the url
     */
    @Override
    public void submitPardotPostRespose(final Map<String, String[]> parameters, final String url) {
        LOGGER.info("Inside service response");
        final HttpPost postRequest = new HttpPost(url);
        postRequest.addHeader(PWConstants.CONTENT_TYPE, PWConstants.APPLICATION_ENCODING);
        postRequest.addHeader(PWConstants.ACCEPT, PWConstants.APPLICATION_JSON);
        final ArrayList<NameValuePair> postParameters = new ArrayList<>();

        for (final Map.Entry<String, String[]> entry : parameters.entrySet()) {
            if (!"pardotUrl".equalsIgnoreCase(entry.getKey())) {
                if("types-communication".equalsIgnoreCase(entry.getKey()) 
                        || "interestArea".equalsIgnoreCase(entry.getKey())){
                   for(String value :entry.getValue()) {
                       postParameters.add(new BasicNameValuePair(entry.getKey(), value));
                   }
                }
                else {
                    postParameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()[0]));
                }
            }
        }

        final HttpClient httpClient = HttpClientBuilder.create().build();
        int statusCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;
        try {
            postRequest.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));
            final HttpResponse httpResponse = httpClient.execute(postRequest);
            statusCode = httpResponse.getStatusLine().getStatusCode();
            LOGGER.info("status code {}", statusCode);
        } catch (final IOException e) {
            LOGGER.error("Error while submitting pardot post request", e);
        }
    }

    /**
     * Submit pardot post respose.
     *
     * @param parameterMap the parameter map
     */
    @Override
    public void submitPardotPostRespose(final Map<String, String[]> parameterMap) {

        final String url = parameterMap.get(FormConstants.PARDOT_URL_PROPERTY)[0];
        submitPardotPostRespose(parameterMap, url);

    }

    /**
     * Gets the subscription form pardot URL.
     *
     * @return the subscription form pardot URL
     */
    @Override
    public String getSubscriptionFormPardotURL() {
        return config.pardotSubscriptionFormURL();
    }

    /**
     * Gets the manage pref json.
     *
     * @param emailToCheck the email to check
     * @return the manage pref json
     */
    @Override
    public JsonObject getManagePrefJson(String emailToCheck) {
    	JsonObject jsonData = null;
        LOGGER.debug("Inside getManagePrefJson method");
        String apiUrl = config.pardotSubscribersApiURL() + "/subscriberpreferences/" + emailToCheck;
        BearerToken token = getBearerToken();
        String jsonResponse = getPardotApiGetRespose(PWConstants.BEARER, apiUrl, token.getAccessToken());
        if (StringUtils.isNotBlank(jsonResponse)) {
        	jsonData = new JsonParser().parse(jsonResponse).getAsJsonObject();
    		return jsonData.get("data").getAsJsonArray().get(0).getAsJsonObject();
        }
        return jsonData;
    }

    /**
     * Gets the subscriber mail addresses.
     *
     * @param locale the locale
     * @param interestAreas the interest areas
     * @return the subscriber mail addresses
     * @throws JSONException 
     */
    public List<String> getSubscriberMailAddresses(String locale, List<String> interestAreas) throws JSONException {
        LOGGER.debug("Inside Get SubscriberMailAddresses method");
        List<String> mailAddresses = null;
            String apiUrl = config.pardotSubscribersApiURL() + "/subscribers?";
            BearerToken token = getBearerToken();
            String areas = StringUtils.EMPTY;
            if (Objects.nonNull(interestAreas) && !interestAreas.isEmpty()) {
                for (String interest : interestAreas) {
                    areas = areas.concat(interest) + PWConstants.COMMA;
                }
                if (areas.endsWith(PWConstants.COMMA)) {
                    areas = areas.substring(0, areas.length() - 1);
                }
                apiUrl = apiUrl + "markets=" + locale;
                if (!interestAreas.isEmpty()) {
                    apiUrl = apiUrl + "&areas=" + areas;
                }
                final String jsonResponse = getPardotApiGetRespose(PWConstants.BEARER, apiUrl, token.getAccessToken());
                if (!jsonResponse.isEmpty()) {
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    mailAddresses = getMailAddresses(jsonObject);
                }
            }
        return mailAddresses;
    }

    /**
     * Gets the mail addresses.
     *
     * @param json the json
     * @return the mail addresses
     * @throws JSONException the JSON exception
     */
    private List<String> getMailAddresses(JSONObject json) throws JSONException {
        List<String> mailAddresses = null;
        if (!json.isNull(DATA_FIELD) && json.getJSONArray(DATA_FIELD).length() > 0) {
            mailAddresses = new ArrayList<>();
            JSONArray jsonArray = json.getJSONArray(DATA_FIELD);
            JSONObject[] data = new JSONObject[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                data[i] = jsonArray.getJSONObject(i);
                mailAddresses.add(data[i].getString("email"));
            }
        }
        return mailAddresses;
    }

    /**
     * Gets the pardot subscriber api url.
     *
     * @return the pardot subscriber api url
     */
    @Override
    public String getPardotSubscriberApiUrl() {
        return config.pardotSubscribersApiURL();
    }

    /**
     * Gets the pardot token generation url.
     *
     * @return the pardot token generation url
     */
    @Override
    public String getPardotTokenGenerationUrl() {
        return config.pardotTokenGenerationUrl();
    }

    /**
     * Gets the bearer token.
     *
     * @return the bearer token
     */
    @Override
    public BearerToken getBearerToken() {
        BearerToken bearerToken = new BearerToken();
        String jsonResponse = null;
        final String authString = config.pardotTokenGenerationApiClientId() + ":"
                + config.pardotTokenGenerationApiClientSecret();
        final String encodedAuthString = Base64.getEncoder()
                .encodeToString(authString.getBytes(StandardCharsets.UTF_8));
        final String apiURL = config.pardotTokenGenerationUrl();
        jsonResponse = getPardotApiPostRespose(PWConstants.BASIC, apiURL, encodedAuthString);
        if (StringUtils.isNotBlank(jsonResponse)) {
            try {
                JSONObject json = new JSONObject(jsonResponse);
                bearerToken.setAccessToken(json.getString("access_token"));
                bearerToken.setTokenType(json.getString("token_type"));
                bearerToken.setExpiresIn(json.getString("expires_in"));
            } catch (final JSONException e) {
                LOGGER.error("Unable to convert json to pojo for the bearer token response {}", e.getMessage());
            }
        }
        return bearerToken;
    }

    /**
     * Gets the pardot api post respose.
     *
     * @param authType the auth type
     * @param apiURL the api URL
     * @param encodedAuthString the encoded auth string
     * @return the pardot api post respose
     */
    private String getPardotApiPostRespose(final String authType, final String apiURL, final String encodedAuthString) {
        String jsonResponse = StringUtils.EMPTY;
        LOGGER.debug("Http Post request URL : {}", apiURL);
        final HttpPost postRequest = new HttpPost(apiURL);
        postRequest.addHeader(PWConstants.AUTHORIZATION, authType + PWConstants.SPACE + encodedAuthString);
        postRequest.addHeader(PWConstants.CONTENT_TYPE, PWConstants.APPLICATION_ENCODING);
        postRequest.addHeader(PWConstants.ACCEPT, PWConstants.APPLICATION_JSON);
        final ArrayList<NameValuePair> postParameters = new ArrayList<>();
        postParameters.add(new BasicNameValuePair(PWConstants.GRANT_TYPE, PWConstants.CLIENT_CREDENTIALS));

        final HttpClient httpClient = HttpClientBuilder.create().build();
        int statusCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;
        try {
            postRequest.setEntity(new UrlEncodedFormEntity(postParameters, StandardCharsets.UTF_8));
            final HttpResponse httpResponse = httpClient.execute(postRequest);
            statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == SUCESSS) {
                jsonResponse = EntityUtils.toString(httpResponse.getEntity());
            }
            LOGGER.debug("getPardotApiPostRespose :: HTTP POST request status code: {}", statusCode);
            LOGGER.debug("getPardotApiPostRespose :: HTTP Post request Jsonresponse {}", jsonResponse);
        } catch (final IOException e) {
            LOGGER.error("Unable to connect to the URL {}", e.getMessage());
        }
        return jsonResponse;
    }

    /**
     * Gets the pardot api get respose.
     *
     * @param authType the auth type
     * @param apiURL the api URL
     * @param encodedAuthString the encoded auth string
     * @return the pardot api get respose
     */
    private String getPardotApiGetRespose(final String authType, final String apiURL, final String encodedAuthString) {
        String jsonResponse = StringUtils.EMPTY;
        LOGGER.debug("Http Get request URL : {}", apiURL);
        final HttpGet getRequest = new HttpGet(apiURL);
        getRequest.addHeader(PWConstants.AUTHORIZATION, authType + PWConstants.SPACE + encodedAuthString);
        getRequest.addHeader(PWConstants.ACCEPT, PWConstants.APPLICATION_JSON);

        final HttpClient httpClient = HttpClientBuilder.create().build();
        int statusCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;
        try {
            final HttpResponse httpResponse = httpClient.execute(getRequest);
            statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == SUCESSS) {
                jsonResponse = EntityUtils.toString(httpResponse.getEntity());
            }
            LOGGER.debug("Http Get request status code: {}", statusCode);
            LOGGER.debug("HTTP GET request Jsonresponse {}", jsonResponse);
        } catch (final IOException e) {
            LOGGER.error("Unable to connect to the url {}", e.getMessage());
        }
        return jsonResponse;
    }
    
    /**
     * Gets the bearer token.
     *
     * @return the bearer token
     */
    @Override
    public BearerToken getBearerTokenForCustomFormService() {
        BearerToken bearerToken = new BearerToken();
        String jsonResponse = null;
        final String authString = config.customFormServiceClientID() + ":" + config.customFormServiceClientSecret();
        final String encodedAuthString = Base64.getEncoder()
                .encodeToString(authString.getBytes(StandardCharsets.UTF_8));
        final String apiURL = config.customTokenGenerationUrl();
        jsonResponse = getPardotApiPostRespose(PWConstants.BASIC, apiURL, encodedAuthString);
        if (StringUtils.isNotBlank(jsonResponse)) {
            try {                
                bearerToken = new ObjectMapper().readValue(jsonResponse, BearerToken.class);
            } catch (final IOException e) {
                LOGGER.error("Unable to convert json to pojo for the bearer token response {}", e.getMessage());
            }
        }
        return bearerToken;
    }

    /**
     * Submit custom form service post response.
     *
     * @param parameters
     *            the parameters
     */
    @Override
    public void submitcustomFormServicePostResponse(final Map<String, String[]> parameters)
            throws Exception {
        LOGGER.info("Inside submitcustomFormServicePostResponse");

        final String apiURL = config.customFormServiceUrl();
        final HttpPost postRequest = new HttpPost(apiURL);
        postRequest.addHeader(PWConstants.CONTENT_TYPE, PWConstants.APPLICATION_ENCODING);
        postRequest.addHeader(PWConstants.AUTHORIZATION,
                PWConstants.BEARER + PWConstants.SPACE + getBearerTokenForCustomFormService().getAccessToken());

        final ArrayList<NameValuePair> postParameters = new ArrayList<>();
        for (final Map.Entry<String, String[]> entry : parameters.entrySet()) {
            postParameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()[0]));
        }

        final HttpClient httpClient = HttpClientBuilder.create().build();
        postRequest.setEntity(new UrlEncodedFormEntity(postParameters, StandardCharsets.UTF_8));
        final HttpResponse httpResponse = httpClient.execute(postRequest);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        LOGGER.info("submitcustomFormServicePostResponse :: Status code {}", statusCode);     
            switch (statusCode) {
            case CREATED:
                LOGGER.info("Custom form service data submitted successfully to APIGEE {}", statusCode);
                break;
            case SUCESSS:
                LOGGER.info("Custom form service data submitted successfully to APIGEE {}", statusCode);
                break;
            default:
                throw new HttpException("Error occurred while submitting custom form service data to APIGEE");
            }      
    }
}
