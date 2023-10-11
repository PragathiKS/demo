package com.tetrapak.publicweb.core.servlets;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tetrapak.publicweb.core.beans.CountryBean;
import com.tetrapak.publicweb.core.models.FindMyOfficeModel;
import com.tetrapak.publicweb.core.services.FindMyOfficeService;

/**
 * The Class FindMyOfficeServlet.
 */
@Component(
        service = Servlet.class,
        property = { Constants.SERVICE_DESCRIPTION + "=Find My office data Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.resourceTypes=" + "publicweb/components/content/findMyOffice" })
public class FindMyOfficeServlet extends SlingSafeMethodsServlet {
    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(FindMyOfficeServlet.class);

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -5454246014078059527L;

    /** The find my office service. */
    @Reference
    private FindMyOfficeService findMyOfficeService;

    /**
     * Do get.
     *
     * @param request
     *            the request
     * @param resp
     *            the resp
     */
    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse resp) {
        final ResourceResolver resolver = request.getResourceResolver();
        FindMyOfficeModel findMyOfficeModel = request.getResource().adaptTo(FindMyOfficeModel.class);
        final Map<String, CountryBean> treeMap = new TreeMap<String, CountryBean>(
                findMyOfficeService.getFindMyOfficeData(resolver, findMyOfficeModel));
        Map<String, CountryBean> fullMap = new LinkedHashMap<>();
        fullMap.putAll(findMyOfficeService.getCorporateOfficeList());
        fullMap.putAll(treeMap);
        ObjectMapper mapper = new ObjectMapper();
        try {
            resp.setContentType("text/html; charset=UTF-8");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(mapper.writeValueAsString(fullMap));
        } catch (IOException ioException) {
            LOGGER.error("ioException :{}", ioException.getMessage(), ioException);
        }
    }
}
