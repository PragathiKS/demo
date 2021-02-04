package com.tetrapak.publicweb.core.services.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

	@Override
	public String getManagePrefApiUrl() {
		return config.pardotManagePrefApiURL();
	}

	@Override
	public String getManagePrefApiCredentials() {
		return config.pardotManagePrefApiCredentials();
	}

	@Override
	public JsonObject getManagePrefJson(String emailToCheck) {
		JsonObject jsonData = null;
		LOGGER.debug("Inside Get getManagePrefJson method");
        try {
            final HttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet();
            request.addHeader("Authorization", "Basic " + getManagePrefApiCredentials());
            request.setURI(new URI(getManagePrefApiUrl()));
            HttpResponse response = httpClient.execute(request);
            InputStream ips = response.getEntity().getContent();
            StringBuilder sb = new StringBuilder();
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(ips, StandardCharsets.UTF_8));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            JsonObject data = new JsonParser().parse(sb.toString()).getAsJsonObject();
            String jsonStr = data.get("jsonData").getAsString();
            JsonObject exactJsonData = new JsonParser().parse(jsonStr).getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : exactJsonData.entrySet()) {
    			if (entry.getKey().equalsIgnoreCase(emailToCheck)) {
    				jsonData = entry.getValue().getAsJsonObject();
    				break;
    			}
            }
        } catch (URISyntaxException | IOException e) {
        	LOGGER.error("Error while fetching manage preference json data: {}", e.getMessage());
        }
        return jsonData;
	}
}
