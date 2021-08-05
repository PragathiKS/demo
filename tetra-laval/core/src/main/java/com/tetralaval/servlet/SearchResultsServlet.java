package com.tetralaval.servlet;

import com.google.gson.Gson;
import com.tetralaval.services.SearchResultsService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameterMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_METHODS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES;

@Component(
        service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Tetra Laval - Search service",
                SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
                SLING_SERVLET_RESOURCE_TYPES + "=tetra-laval/components/structure/search-page"
        }
)
public class SearchResultsServlet extends SlingSafeMethodsServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchResultsServlet.class);

    @Reference
    private SearchResultsService searchResultsService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        try {
            RequestParameterMap params = request.getRequestParameterMap();
            Map<String, String> map = searchResultsService.setSearchQueryMap(request, params);

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);

            PrintWriter writer = response.getWriter();
            writer.println(new Gson().toJson(searchResultsService.getResults(request, map)));
            writer.flush();
            writer.close();
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Error while decoding the query term.", e);
        } catch (IOException e) {
            LOGGER.error("Error while writing the response object.", e);
        }
    }
}