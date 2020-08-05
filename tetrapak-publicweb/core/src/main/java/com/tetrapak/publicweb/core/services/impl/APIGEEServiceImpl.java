package com.tetrapak.publicweb.core.services.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

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
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tetrapak.publicweb.core.beans.pxp.BearerToken;
import com.tetrapak.publicweb.core.beans.pxp.DeltaFillingMachine;
import com.tetrapak.publicweb.core.beans.pxp.DeltaPackageType;
import com.tetrapak.publicweb.core.beans.pxp.DeltaProcessingEquipement;
import com.tetrapak.publicweb.core.beans.pxp.Files;
import com.tetrapak.publicweb.core.beans.pxp.FillingMachine;
import com.tetrapak.publicweb.core.beans.pxp.Packagetype;
import com.tetrapak.publicweb.core.beans.pxp.ProcessingEquipement;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.APIGEEService;
import com.tetrapak.publicweb.core.services.config.APIGEEServiceConfig;

/**
 * Impl class for API GEE Service
 *
 * @author Sandip Kumar
 */
@Component(immediate = true, service = APIGEEService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = APIGEEServiceConfig.class)
public class APIGEEServiceImpl implements APIGEEService {

    /** The config. */
    private APIGEEServiceConfig config;

    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(APIGEEServiceImpl.class);

    /** The token api url. */
    private static String TOKEN_API_URI = "/oauth2/v2/token";

    /** The bearer. */
    private static String BEARER = "Bearer";

    /** The success. */
    private static final int SUCESSS = 200;

    /**
     * activate method
     *
     * @param config
     *            API GEE Service configuration
     */
    @Activate
    public void activate(final APIGEEServiceConfig config) {

        this.config = config;
    }

    /**
     * return bearer token
     */
    @Override
    public BearerToken getBearerToken() {
        LOGGER.debug("HTTP GET request from APIGEETokenGeneratorServlet");
        BearerToken bearerToken = new BearerToken();
        final String authString = config.apigeeClientID() + ":" + config.apigeeClientSecret();
        final String encodedAuthString = Base64.getEncoder()
                .encodeToString(authString.getBytes(StandardCharsets.UTF_8));
        final String jsonResponse = getAPIGeePostRespose("Basic", TOKEN_API_URI, encodedAuthString);
        if (StringUtils.isNotBlank(jsonResponse)) {
            try {
                bearerToken = new ObjectMapper().readValue(jsonResponse, BearerToken.class);
            } catch (final IOException e) {
                LOGGER.error("Unable to convert json to pojo for the bearer token response", e.getMessage(), e);
            }
        }
        return bearerToken;
    }

    /**
     * return filling machines
     */
    @Override
    public List<FillingMachine> getFillingMachines(final String token, final String fileURI) {
        List<FillingMachine> fillingMachines = new ArrayList<>();
        final String jsonResponse = getAPIGeeGetRespose(BEARER, fileURI, token);
        if (StringUtils.isNotBlank(jsonResponse)) {
            try {
                fillingMachines = Arrays.asList(new ObjectMapper().readValue(jsonResponse, FillingMachine[].class));
            } catch (final IOException e) {
                LOGGER.error("Unable to convert filling machine to pojo for the list of files response", e.getMessage(),
                        e);
            }
        }
        return fillingMachines;
    }

    /**
     * return package types
     */
    @Override
    public List<Packagetype> getPackageTypes(final String token, final String fileURI) {
        List<Packagetype> packageType = new ArrayList<>();
        final String jsonResponse = getAPIGeeGetRespose(BEARER, fileURI, token);
        if (StringUtils.isNotBlank(jsonResponse)) {
            try {
                packageType = Arrays.asList(new ObjectMapper().readValue(jsonResponse, Packagetype[].class));
            } catch (final IOException e) {
                LOGGER.error("Unable to convert package types to pojo for the list of files response", e.getMessage(),
                        e);
            }
        }
        return packageType;
    }

    /**
     * return processing equipements
     */
    @Override
    public List<ProcessingEquipement> getProcessingEquipements(final String token, final String fileURI) {
        List<ProcessingEquipement> equipements = new ArrayList<>();
        final String jsonResponse = getAPIGeeGetRespose(BEARER, fileURI, token);
        if (StringUtils.isNotBlank(jsonResponse)) {
            try {
                equipements = Arrays.asList(new ObjectMapper().readValue(jsonResponse, ProcessingEquipement[].class));
            } catch (final IOException e) {
                LOGGER.error("Unable to convert equipements to pojo for the list of files response", e.getMessage(), e);
            }
        }
        return equipements;
    }

    /**
     * return delta filling machines
     */
    @Override
    public DeltaFillingMachine getDeltaFillingMachines(final String token, final String fileURI) {
        DeltaFillingMachine fillingMachines = new DeltaFillingMachine();
        final String jsonResponse = getAPIGeeGetRespose(BEARER, fileURI, token);
        if (StringUtils.isNotBlank(jsonResponse)) {
            try {
                fillingMachines = new ObjectMapper().readValue(jsonResponse, DeltaFillingMachine.class);
            } catch (final IOException e) {
                LOGGER.error("Unable to convert filling machine to pojo for the list of files response", e.getMessage(),
                        e);
            }
        }
        return fillingMachines;
    }

    /**
     * return delta package types
     */
    @Override
    public DeltaPackageType getDeltaPackageTypes(final String token, final String fileURI) {
        DeltaPackageType packageType = new DeltaPackageType();
        final String jsonResponse = getAPIGeeGetRespose(BEARER, fileURI, token);
        if (StringUtils.isNotBlank(jsonResponse)) {
            try {
                packageType = new ObjectMapper().readValue(jsonResponse, DeltaPackageType.class);
            } catch (final IOException e) {
                LOGGER.error("Unable to convert package types to pojo for the list of files response", e.getMessage(),
                        e);
            }
        }
        return packageType;
    }

    /**
     * return delta processing equipements
     */
    @Override
    public DeltaProcessingEquipement getDeltaProcessingEquipements(final String token, final String fileURI) {
        DeltaProcessingEquipement equipements = new DeltaProcessingEquipement();
        final String jsonResponse = getAPIGeeGetRespose(BEARER, fileURI, token);
        if (StringUtils.isNotBlank(jsonResponse)) {
            try {
                equipements = new ObjectMapper().readValue(jsonResponse, DeltaProcessingEquipement.class);
            } catch (final IOException e) {
                LOGGER.error("Unable to convert equipements to pojo for the list of files response", e.getMessage(), e);
            }
        }
        return equipements;
    }

    /**
     * return list of files
     *
     */
    @Override
    public Files getListOfFiles(final String feedType, final String token) {
        Files listOfFiles = new Files();
        final String jsonResponse = getAPIGeeGetRespose(BEARER, PWConstants.FEED_FILES_URI + feedType, token);
        if (StringUtils.isNotBlank(jsonResponse)) {
            try {
                listOfFiles = new ObjectMapper().readValue(jsonResponse, Files.class);
            } catch (final IOException e) {
                LOGGER.error("Unable to convert json to pojo for the list of files response", e);
            }
        }
        return listOfFiles;
    }

    /**
     * @param authType
     * @param apiURI
     * @param encodedAuthString
     * @return api gee post response
     */
    private String getAPIGeePostRespose(final String authType, final String apiURI, final String encodedAuthString) {
        String jsonResponse = StringUtils.EMPTY;

        // POST API Call to APIGEE End point to get json data
        final String apiURL = config.apigeeServiceUrl() + apiURI;
        final HttpPost postRequest = new HttpPost(apiURL);
        postRequest.addHeader("Authorization", authType + " " + encodedAuthString);
        postRequest.addHeader("Content-Type", "application/x-www-form-urlencoded");
        postRequest.addHeader("Accept", "application/json");
        final ArrayList<NameValuePair> postParameters = new ArrayList<>();
        postParameters.add(new BasicNameValuePair("grant_type", "client_credentials"));

        final HttpClient httpClient = HttpClientBuilder.create().build();
        int statusCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;
        try {
            postRequest.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));
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
     * @param authType
     * @param apiURI
     * @param encodedAuthString
     * @return api gee get response
     */
    private String getAPIGeeGetRespose(final String authType, final String apiURI, final String encodedAuthString) {
        String jsonResponse = StringUtils.EMPTY;

        // GET API Call to APIGEE End point to get json data
        final String apiURL = config.apigeeServiceUrl() + apiURI;
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
