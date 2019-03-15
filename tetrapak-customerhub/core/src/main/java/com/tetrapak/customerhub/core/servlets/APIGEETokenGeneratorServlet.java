package com.tetrapak.customerhub.core.servlets;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=API GEE Token Generator Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=" + "/bin/customerhub/token-generator"
        })
public class APIGEETokenGeneratorServlet extends SlingSafeMethodsServlet {

    @Reference
    APIGEEService apigeeService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        logger.debug("HTTP GET request from APIGEETokenGeneratorServlet");
        final String apiURL = apigeeService.getApigeeServiceUrl();
        final String username = apigeeService.getApigeeClientID();
        final String password = apigeeService.getApigeeClientSecret();

        String authString = username + ":" + password;
        byte[] authBytes = Base64.getEncoder().encode(authString.getBytes());

        URL url = new URL(apiURL);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("Authorization", "Basic " + authBytes.toString());
        InputStream is = urlConnection.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);

        int numCharsRead;
        char[] charArray = new char[1024];
        StringBuffer sb = new StringBuffer();
        while ((numCharsRead = isr.read(charArray)) > 0) {
            sb.append(charArray, 0, numCharsRead);
        }
        String result = sb.toString();

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("result", result);
        jsonResponse.addProperty("status", CustomerHubConstants.RESPONSE_STATUS_SUCCESS);
        GlobalUtil.writeJsonResponse(response, jsonResponse);
    }
}
