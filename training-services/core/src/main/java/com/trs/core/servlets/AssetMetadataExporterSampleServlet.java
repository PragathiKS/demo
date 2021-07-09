package com.trs.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This servlet is responsible for importing taxonomy generated from Xyleme
 * System into AEM. It expects file input in the same format as ACS Commons Tag
 * Importer utility
 */
@Component(service = Servlet.class,
        property = { "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.resourceTypes=" + "trs/components/structure/empty-page",
                "sling.servlet.selectors=sample", "sling.servlet.extensions=html" })
@ServiceDescription("Asset Metadata Exporter Servlet")
public class AssetMetadataExporterSampleServlet extends SlingSafeMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssetMetadataExporterSampleServlet.class);
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse resp)
            throws ServletException, IOException {

     
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write("{\"customresponse\":\"sucess" + " with param value as :" + request.getParameter("id")+"\"}");
    }

}
