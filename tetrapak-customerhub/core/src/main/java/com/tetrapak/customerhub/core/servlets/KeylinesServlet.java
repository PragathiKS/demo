package com.tetrapak.customerhub.core.servlets;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Servlet;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.beans.keylines.Keylines;
import com.tetrapak.customerhub.core.beans.keylines.KeylinesError;
import com.tetrapak.customerhub.core.services.KeylinesService;

/**
 * Servlet to fetch keyline assets and send the keylines together with openings
 * and volumes as a json
 * 
 * @author selennys
 *
 */
@Component(service = Servlet.class, property = { "sling.servlet.methods=" + HttpConstants.METHOD_POST,
	"sling.servlet.selectors=" + KeylinesServlet.SLING_SERVLET_SELECTOR,
	"sling.servlet.extensions=" + KeylinesServlet.SLING_SERVLET_EXTENSION,
	"sling.servlet.resourceTypes=" + KeylinesServlet.SLING_SERVLET_RESOURCE_TYPES })
public class KeylinesServlet extends SlingSafeMethodsServlet {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -5553813180119669122L;

    private static final Logger LOGGER = LoggerFactory.getLogger(KeylinesServlet.class);

    public static final String SLING_SERVLET_RESOURCE_TYPES = "customerhub/components/content/keylines";
    public static final String SLING_SERVLET_EXTENSION = "json";
    public static final String SLING_SERVLET_SELECTOR = "keylines";

    @Reference
    private transient KeylinesService keylinesService;

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) {
	LOGGER.trace("Inside KeylinesServlet servlet : Get method");
	List<String> shapes = new ArrayList<>();
	String packageTypeParameter = request.getParameter("type");
	LOGGER.debug("Package Type: {}", packageTypeParameter);
	String[] shapesParameter = request.getParameterValues("shapes");
	if (shapesParameter != null) {
	    for (String shape : shapesParameter) {
		if (StringUtils.isNotBlank(shape)) {
		    shapes.add(shape);
		}
	    }
	}
	LOGGER.debug("Package Type: {} --- Package Shape:{}", packageTypeParameter, shapes);
	String responseString = "";
	Gson gson = new Gson();
	try {
	    Keylines keylines = keylinesService.getKeylines(request.getResourceResolver(), packageTypeParameter,
		    shapes);

	    if (keylines != null) {
		responseString = gson.toJson(keylines, Keylines.class);
		response.setStatus(HttpURLConnection.HTTP_OK);
	    } else {
		responseString = setKeylineErrorResponse(gson, HttpURLConnection.HTTP_BAD_REQUEST,
			"No Keylines. Please check all parameters");
		response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
	    }
	} catch (Exception e) {
	    responseString = setKeylineErrorResponse(gson, HttpURLConnection.HTTP_INTERNAL_ERROR, e.getMessage());
	    response.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
	}
	try {
	    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
	    response.getWriter().print(responseString);
	} catch (IOException e) {
	    LOGGER.error("IOException", e);
	}
	LOGGER.trace("End KeylinesServlet servlet : Get method");

    }

    private String setKeylineErrorResponse(Gson gson, int status, String message) {
	String responseString;
	KeylinesError error = new KeylinesError();
	error.setStatus(status);
	error.setMessage(message);
	responseString = gson.toJson(error, KeylinesError.class);
	return responseString;
    }

}
