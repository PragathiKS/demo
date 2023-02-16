package com.tetrapak.customerhub.core.servlets;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import com.tetrapak.customerhub.core.utils.HttpUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.apache.sling.xss.XSSAPI;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
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
    private transient APIGEEService apigeeService;

    /** The XSSAPI  */
    @Reference
    private transient XSSAPI xssAPI;

    private static final long serialVersionUID = 1;

    private static final Logger LOGGER = LoggerFactory.getLogger(APIGEETokenGeneratorServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {

        LOGGER.debug("HTTP GET request from APIGEETokenGeneratorServlet");
        JsonObject jsonResponse = new JsonObject();
        final String apiURL = apigeeService.getApigeeServiceUrl() + GlobalUtil.getSelectedApiMapping(apigeeService, "auth-token");
        String acctkn = StringUtils.EMPTY;
        if (ObjectUtils.notEqual(null, request.getCookie("acctoken"))) {
            acctkn = xssAPI.encodeForHTML(request.getCookie("acctoken").getValue());
        }
        String authString = apigeeService.getApigeeClientID() + ":" + apigeeService.getApigeeClientSecret();
        String encodedAuthString = Base64.getEncoder().encodeToString(authString.getBytes());

        HttpPost postRequest = new HttpPost(apiURL);
        postRequest.addHeader("Authorization", "Basic " + encodedAuthString);
        postRequest.addHeader("Content-Type", "application/x-www-form-urlencoded");
        postRequest.addHeader("Accept", "application/json");
        ArrayList<NameValuePair> postParameters = new ArrayList<>();
        postParameters.add(new BasicNameValuePair("grant_type", "urn:ietf:params:oauth:grant-type:token-exchange"));
        postParameters.add(new BasicNameValuePair("token", acctkn));
        postRequest.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));

        HttpClient httpClient = HttpClientBuilder.create().build();
        int statusCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;
        try {
            HttpResponse httpResponse = httpClient.execute(postRequest);
            statusCode = httpResponse.getStatusLine().getStatusCode();
            response.setStatus(statusCode);
            LOGGER.debug("Http Post request status code: {}", statusCode);

            jsonResponse = HttpUtil.setJsonResponse(jsonResponse, httpResponse);
            jsonResponse.addProperty(CustomerHubConstants.STATUS, CustomerHubConstants.RESPONSE_STATUS_SUCCESS);
            HttpUtil.writeJsonResponse(response, jsonResponse);
        } catch (IOException e) {
            LOGGER.error("Unable to connect to the url {}", apiURL, e);
            response.setStatus(statusCode);
            jsonResponse.addProperty(CustomerHubConstants.STATUS, CustomerHubConstants.RESPONSE_STATUS_FAILURE);
            jsonResponse.addProperty(CustomerHubConstants.RESULT, e.getMessage());
            HttpUtil.writeJsonResponse(response, jsonResponse);
        }
    }
}
