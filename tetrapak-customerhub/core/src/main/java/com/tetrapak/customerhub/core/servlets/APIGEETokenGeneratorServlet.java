package com.tetrapak.customerhub.core.servlets;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.utils.HttpUtil;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

/**
 * API GEE Token Generator Servlet
 *
 * @author Nitin Kumar
 */
@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=API GEE Token Generator Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=" + "/bin/customerhub/token-generator"
        })
public class APIGEETokenGeneratorServlet extends SlingSafeMethodsServlet {

    @Reference
    private APIGEEService apigeeService;

    private static final long serialVersionUID = 1;

    private static final Logger LOGGER = LoggerFactory.getLogger(APIGEETokenGeneratorServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        LOGGER.debug("HTTP GET request from APIGEETokenGeneratorServlet");
        JsonObject jsonResponse = new JsonObject();
        final String apiURL = apigeeService.getApigeeServiceUrl() + "/oauth2/v2/token";
        final String username = apigeeService.getApigeeClientID();
        final String password = apigeeService.getApigeeClientSecret();

        String authString = username + ":" + password;
        String encodedAuthString = Base64.getEncoder().encodeToString(authString.getBytes());

        HttpPost postRequest = new HttpPost(apiURL);
        postRequest.addHeader("Authorization", "Basic " + encodedAuthString);
        postRequest.addHeader("Content-Type", "application/x-www-form-urlencoded");
        postRequest.addHeader("Accept", "application/json");
        ArrayList<NameValuePair> postParameters = new ArrayList<>();
        postParameters.add(new BasicNameValuePair("grant_type", "client_credentials"));
        postRequest.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));

        HttpClient httpClient = HttpClientBuilder.create().build();
        int statusCode = HttpStatus.SC_NOT_FOUND;
        try {
            HttpResponse httpResponse = httpClient.execute(postRequest);
            statusCode = httpResponse.getStatusLine().getStatusCode();
            response.setStatus(statusCode);
            LOGGER.debug("Http Post request status code: {}", statusCode);

            jsonResponse = HttpUtil.setJsonResponse(jsonResponse, httpResponse);

            HttpUtil.writeJsonResponse(response, jsonResponse);
        } catch (FileNotFoundException e) {
            LOGGER.error("Unable to connect to the url {}", apiURL, e);
            response.setStatus(statusCode);
            jsonResponse.addProperty("status", CustomerHubConstants.RESPONSE_STATUS_FAILURE);
            HttpUtil.writeJsonResponse(response, jsonResponse);
        }
    }
}
