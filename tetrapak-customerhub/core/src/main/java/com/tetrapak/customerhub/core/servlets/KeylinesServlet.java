package com.tetrapak.customerhub.core.servlets;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.Servlet;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.gson.Gson;
import com.tetrapak.customerhub.core.beans.keylines.Keylines;
import com.tetrapak.customerhub.core.beans.keylines.KeylinesError;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.KeylinesService;

/**
 * Servlet to fetch keyline assets and send the keylines together with openings
 * and volumes as a json
 * 
 * @author selennys
 *
 */
@Component(service = Servlet.class, property = { "sling.servlet.methods=" + HttpConstants.METHOD_GET,
	"sling.servlet.selectors=" + CustomerHubConstants.KEYLINES_SLING_SERVLET_SELECTOR,
	"sling.servlet.extensions=" + CustomerHubConstants.JSON_SERVLET_EXTENSION,
	"sling.servlet.resourceTypes=" + KeylinesServlet.SLING_SERVLET_RESOURCE_TYPES })
public class KeylinesServlet extends SlingSafeMethodsServlet {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -5553813180119669122L;

    private static final Logger LOGGER = LoggerFactory.getLogger(KeylinesServlet.class);

    public static final String SLING_SERVLET_RESOURCE_TYPES = "customerhub/components/content/keylines";

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
	Locale locale = request.getLocale();
	try {
	    Resource resource = request.getResource();
	    PageManager pageManager = request.getResourceResolver().adaptTo(PageManager.class);
	    Page page = pageManager.getContainingPage(resource);
	    locale = page.getLanguage(true);
	} catch (Exception e) {
	    LOGGER.error("Error while getting locale from resource: {}", e.getMessage());
	}
	LOGGER.debug("Locale from Resource {}: ", locale);
	LOGGER.debug("Package Type: {} --- Package Shape:{} --- Locale: {}", packageTypeParameter, shapes, locale);
	String responseString = "";
	Gson gson = new Gson();
	try {
	    Keylines keylines = keylinesService.getKeylines(request.getResourceResolver(), packageTypeParameter, shapes,
		    locale);

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
