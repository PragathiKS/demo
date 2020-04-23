package com.tetrapak.publicweb.core.services.impl;

import java.io.IOException;
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
import com.tetrapak.publicweb.core.beans.pxp.Files;
import com.tetrapak.publicweb.core.beans.pxp.FillingMachine;
import com.tetrapak.publicweb.core.services.APIGEEService;
import com.tetrapak.publicweb.core.services.config.APIGEEServiceConfig;

/**
 * Impl class for API GEE Service
 * @author Sandip Kumar
 */
@Component(immediate = true, service = APIGEEService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = APIGEEServiceConfig.class)
public class APIGEEServiceImpl implements APIGEEService {

    private APIGEEServiceConfig config;
    
    private static final Logger LOGGER =  LoggerFactory.getLogger(APIGEEServiceImpl.class);
    
    private static final String TOKEN_API_URI = "/oauth2/v2/token";
    
    private static final String FULL_FEED_FILES_URI = "/equipment/pxpparameters/files/full";
    
    private static final int SUCESSS = 200;
    
    ObjectMapper mapper; 

    /**
     * activate method
     * @param config API GEE Service configuration
     */
    @Activate
    public void activate(APIGEEServiceConfig config) {

        this.config = config;
    }

    @Override
    public BearerToken getBearerToken() {
        LOGGER.debug("HTTP GET request from APIGEETokenGeneratorServlet");
        BearerToken bearerToken = new BearerToken();
        String authString = config.apigeeClientID() + ":" + config.apigeeClientSecret();
        String encodedAuthString = Base64.getEncoder().encodeToString(authString.getBytes());
        String jsonResponse = getAPIGeeRespose("BASIC", TOKEN_API_URI, encodedAuthString,true);
        if(StringUtils.isNotBlank(jsonResponse)) {
            try {
                bearerToken = new ObjectMapper().readValue(jsonResponse, BearerToken.class);
            } catch (IOException e) {
                LOGGER.error("Unable to convert json to pojo for the bearer token response", jsonResponse, e);
            }
        }
        return bearerToken;
    }

    @Override
    public List<FillingMachine> getFillingMachines(String token, String fileURI) {
        List<FillingMachine> fillingMachines = new ArrayList<FillingMachine>();
        String jsonResponse = getAPIGeeRespose("BEARER", fileURI, token,false);
        if(StringUtils.isNotBlank(jsonResponse)) {
            try {
                fillingMachines = Arrays.asList(new ObjectMapper().readValue(jsonResponse, FillingMachine[].class));
            } catch (IOException e) {
                LOGGER.error("Unable to convert json to pojo for the list of files response", e);
            }
        }
        return fillingMachines;
    }

    @Override
    public Files getListOfFiles(String type, String token) {
        Files listOfFiles = new Files();
        String jsonResponse = getAPIGeeRespose("BEARER", FULL_FEED_FILES_URI, token,false);
        if(StringUtils.isNotBlank(jsonResponse)) {
            try {
                listOfFiles = new ObjectMapper().readValue(jsonResponse, Files.class);
            } catch (IOException e) {
                LOGGER.error("Unable to convert json to pojo for the list of files response", e);
            }
        }
        return listOfFiles;
    }
    
    private String getAPIGeeRespose(String authType,String apiURI,String encodedAuthString,Boolean isGrantType) {
        String jsonResponse = StringUtils.EMPTY;
        
        //API Call to APIGEE End point to get json data
        final String apiURL = config.apigeeServiceUrl() + apiURI;
        HttpPost postRequest = new HttpPost(apiURL);
        postRequest.addHeader("Authorization", authType + " " + encodedAuthString);
        postRequest.addHeader("Content-Type", "application/x-www-form-urlencoded");
        postRequest.addHeader("Accept", "application/json");
     
        HttpClient httpClient = HttpClientBuilder.create().build();
        int statusCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;
        try {
            if(isGrantType) {
                ArrayList<NameValuePair> postParameters = new ArrayList<>();
                postParameters.add(new BasicNameValuePair("grant_type", "client_credentials"));
                postRequest.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));
            }
            HttpResponse httpResponse = httpClient.execute(postRequest);
            statusCode = httpResponse.getStatusLine().getStatusCode();  
            if(statusCode == SUCESSS) {
                jsonResponse = EntityUtils.toString(httpResponse.getEntity());
            }
            LOGGER.debug("Http Post request status code: {}", statusCode);
        } catch (IOException e) {
            LOGGER.error("Unable to connect to the url {}", apiURL, e);
        }
        return jsonResponse;
    }

}
