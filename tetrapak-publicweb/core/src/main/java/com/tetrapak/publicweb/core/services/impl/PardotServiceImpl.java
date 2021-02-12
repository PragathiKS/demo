package com.tetrapak.publicweb.core.services.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
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

import com.google.gson.JsonObject;
import com.tetrapak.publicweb.core.beans.pxp.BearerToken;
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

    /** The bearer. */
    private static final String BEARER = "Bearer";

    /** The Constant DATA_FIELD. */
    private static final String DATA_FIELD = "data";

    /**
     * activate method.
     *
     * @param config
     *            Pardot Service configuration
     */
    @Activate
    public void activate(final PardotServiceConfig config) {

        this.config = config;
    }

    /**
     * Gets the busines inquiry service URL.
     *
     * @return the busines inquiry service URL
     */
    @Override
    public String getBusinesInquiryServiceURL() {
        return config.pardotBusinessInquiryServiceUrl();
    }

    /**
     * Submit pardot post respose.
     *
     * @param parameters
     *            the parameters
     * @param url
     *            the url
     * @return api gee post response
     */
    @Override
    public void submitPardotPostRespose(final Map<String, String[]> parameters, final String url) {

        LOGGER.info("Inside service response");
        final HttpPost postRequest = new HttpPost(url);
        postRequest.addHeader("Content-Type", "application/x-www-form-urlencoded");
        postRequest.addHeader("Accept", "application/json");
        final ArrayList<NameValuePair> postParameters = new ArrayList<>();

        for (final Map.Entry<String, String[]> entry : parameters.entrySet()) {
            if (!"pardotUrl".equalsIgnoreCase(entry.getKey())) {
                postParameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()[0]));
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
     * @param parameterMap
     *            the parameter map
     */
    @Override
    public void submitPardotPostRespose(final Map<String, String[]> parameterMap) {

        final String url = parameterMap.get("pardotUrl")[0];
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
     * @param emailToCheck
     *            the email to check
     * @return the manage pref json
     */
    @Override
    public JsonObject getManagePrefJson(String emailToCheck) {
        JsonObject jsonData = null;
        // LOGGER.debug("Inside Get getManagePrefJson method");
        // try {
        // final HttpClient httpClient = HttpClientBuilder.create().build();
        // HttpGet request = new HttpGet();
        // request.addHeader("Authorization", "Basic " + getManagePrefApiCredentials());
        // request.setURI(new URI(getManagePrefApiUrl()));
        // HttpResponse response = httpClient.execute(request);
        // InputStream ips = response.getEntity().getContent();
        // StringBuilder sb = new StringBuilder();
        // String line;
        // BufferedReader br = new BufferedReader(new InputStreamReader(ips, StandardCharsets.UTF_8));
        // while ((line = br.readLine()) != null) {
        // sb.append(line);
        // }
        // JsonObject data = new JsonParser().parse(sb.toString()).getAsJsonObject();
        // String jsonStr = data.get("jsonData").getAsString();
        // JsonObject exactJsonData = new JsonParser().parse(jsonStr).getAsJsonObject();
        // for (Map.Entry<String, JsonElement> entry : exactJsonData.entrySet()) {
        // if (entry.getKey().equalsIgnoreCase(emailToCheck)) {
        // jsonData = entry.getValue().getAsJsonObject();
        // break;
        // }
        // }
        // } catch (URISyntaxException | IOException e) {
        // LOGGER.error("Error while fetching manage preference json data: {}", e.getMessage());
        // }
        return jsonData;
    }

    /**
     * Gets the subscriber mail addresses.
     *
     * @param locale
     *            the locale
     * @param interestAreas
     *            the interest areas
     * @return the subscriber mail addresses
     */
    public List<String> getSubscriberMailAddresses(String locale, List<String> interestAreas) {
        LOGGER.debug("Inside Get SubscriberMailAddresses method");
        List<String> mailAddresses = null;
        try {
            String apiUrl = config.pardotSubscribersApiURL() + "/subscribers?";
            BearerToken token = getBearerToken();
            String areas = StringUtils.EMPTY;
            if (Objects.nonNull(interestAreas) && !interestAreas.isEmpty()) {
                for (String interest : interestAreas) {
                    areas = areas.concat(interest) + ",";
                }
                if (areas.endsWith(",")) {
                    areas = areas.substring(0, areas.length() - 1);
                }
                String marketCode = locale.split("-")[1];
                apiUrl = apiUrl + "market=" + marketCode;
                if (!interestAreas.isEmpty()) {
                    apiUrl = apiUrl + "&areas=" + areas;
                }
                final String jsonResponse = getPardotApiGetRespose(BEARER, apiUrl, token.getAccessToken());
                if (!jsonResponse.isEmpty()) {
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    mailAddresses = getMailAddresses(jsonObject);
                }
            }
        } catch (JSONException e) {
            LOGGER.error("Error while fetching Subscriber data {}", e.getMessage());
        }
        return mailAddresses;
    }

    /**
     * Gets the mail addresses.
     *
     * @param json
     *            the json
     * @return the mail addresses
     * @throws JSONException
     *             the JSON exception
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
        String jsonResponse = StringUtils.EMPTY;
        final String authString = config.pardotTokenGenerationApiClientId() + ":"
                + config.pardotTokenGenerationApiClientSecret();
        final String encodedAuthString = Base64.getEncoder()
                .encodeToString(authString.getBytes(StandardCharsets.UTF_8));
        final String apiURL = config.pardotTokenGenerationUrl();
        jsonResponse = getPardotApiPostRespose("Basic", apiURL, encodedAuthString);
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
     * @param authType
     *            the auth type
     * @param apiURL
     *            the api URL
     * @param encodedAuthString
     *            the encoded auth string
     * @return the pardot api post respose
     */
    private String getPardotApiPostRespose(final String authType, final String apiURL, final String encodedAuthString) {
        String jsonResponse = StringUtils.EMPTY;
        LOGGER.debug("Http Post request URL : {}", apiURL);
        final HttpPost postRequest = new HttpPost(apiURL);
        postRequest.addHeader("Authorization", authType + " " + encodedAuthString);
        postRequest.addHeader("Content-Type", "application/x-www-form-urlencoded");
        postRequest.addHeader("Accept", "application/json");
        final ArrayList<NameValuePair> postParameters = new ArrayList<>();
        postParameters.add(new BasicNameValuePair("grant_type", "client_credentials"));

        final HttpClient httpClient = HttpClientBuilder.create().build();
        int statusCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;
        try {
            postRequest.setEntity(new UrlEncodedFormEntity(postParameters, StandardCharsets.UTF_8));
            final HttpResponse httpResponse = httpClient.execute(postRequest);
            statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == SUCESSS) {
                jsonResponse = EntityUtils.toString(httpResponse.getEntity());
            }
            LOGGER.debug("Http Post request status code: {}", statusCode);
            LOGGER.debug("HTTP Post request Jsonresponse {}", jsonResponse);
        } catch (final IOException e) {
            LOGGER.error("Unable to connect to the url {}", apiURL, e);
        }
        return jsonResponse;
    }

    /**
     * Gets the pardot api get respose.
     *
     * @param authType
     *            the auth type
     * @param apiURL
     *            the api URL
     * @param encodedAuthString
     *            the encoded auth string
     * @return the pardot api get respose
     */
    private String getPardotApiGetRespose(final String authType, final String apiURL, final String encodedAuthString) {
        String jsonResponse = StringUtils.EMPTY;
        LOGGER.debug("Http Get request URL : {}", apiURL);
        final HttpGet getRequest = new HttpGet(apiURL);
        getRequest.addHeader("Authorization", authType + " " + encodedAuthString);
        getRequest.addHeader("Accept", "application/json");

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
            LOGGER.error("Unable to connect to the url {}", apiURL, e);
        }
        return jsonResponse;
    }

}
