package com.tetrapak.publicweb.core.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.publicweb.core.services.PadrotService;
import com.tetrapak.publicweb.core.services.config.PadrotServiceConfig;

/**
 * Impl class for PadrotService
 *
 */
@Component(immediate = true, service = PadrotService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = PadrotServiceConfig.class)
public class PadrotServiceImpl implements PadrotService {

    /** The config. */
    private PadrotServiceConfig config;

    private static final Logger LOGGER = LoggerFactory.getLogger(PadrotServiceImpl.class);

    /**
     * activate method
     *
     * @param config
     *            Padrot Service configuration
     */
    @Activate
    public void activate(final PadrotServiceConfig config) {

        this.config = config;
    }

    @Override
    public String getBusinesInquiryServiceURL() {
        return config.padrotBusinessInquiryServiceUrl();
    }

    /**
     * @param authType
     * @param apiURI
     * @param encodedAuthString
     * @return api gee post response
     */
    @Override
    public void submitPadrotPostRespose(final Map<String, String[]> parameters, final String url) {

        LOGGER.info("Inside service response");
        final HttpPost postRequest = new HttpPost(url);
        postRequest.addHeader("Content-Type", "application/x-www-form-urlencoded");
        postRequest.addHeader("Accept", "application/json");
        final ArrayList<NameValuePair> postParameters = new ArrayList<>();

        for (final Map.Entry<String, String[]> entry : parameters.entrySet()) {
            if (!"padrotUrl".equalsIgnoreCase(entry.getKey())) {
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
            LOGGER.error("Error while submitting padrot post request", e);
        }
    }

    @Override
    public void submitPadrotPostRespose(final Map<String, String[]> parameterMap) {

        final String url = parameterMap.get("padrotUrl")[0];
        submitPadrotPostRespose(parameterMap, url);

    }

}
