package com.trs.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.trs.core.services.AssetMetadataService;
import com.trs.core.services.TrsConfigurationService;
import com.trs.core.utils.TrsUtils;

/**
 * This servlet is responsible for fetching the AEM path corresponding to the
 * asset file name provided as input
 */
@Component(service = Servlet.class,
        property = { "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.resourceTypes=" + "trs/components/structure/empty-page",
                "sling.servlet.selectors=assetpath", "sling.servlet.extensions=html" })
@ServiceDescription("Asset Path Exporter Servlet")
public class AssetPathExporterServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 3916097813018666824L;
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetPathExporterServlet.class);
    private static final String QUERY_PARAMETER_NAME = "name";
    private static final String QUERYBUILDER_PREDICATE_PATH = "path";

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private TrsConfigurationService trsConfig;

    @Reference
    private AssetMetadataService assetMetadataService;

    @Reference
    private QueryBuilder builder;

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse resp)
            throws ServletException, IOException {

        LOGGER.trace("Entered AssetPathExporterServlet servlet");
        ResourceResolver resourceResolver = null;
        final String assetName = request.getParameter(QUERY_PARAMETER_NAME);
        try {
            resourceResolver = TrsUtils.getTrsResourceResolver(resolverFactory);
        } catch (LoginException e) {
            LOGGER.error("Error while getting resource resolver : ", e);
        }
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = mapper.createArrayNode();

        /**
         * Map for the predicates
         */
        Map<String, String> predicate = new HashMap<>();

        /**
         * Configuring the Map for the predicate
         */
        predicate.put("path", trsConfig.getAssetMetadataAPIBasePath());
        predicate.put("nodename", assetName);

        List<Hit> resultHits = assetMetadataService.executeQuery(resourceResolver, predicate);

        for (Hit hit : resultHits) {

            try {
                ObjectNode assetNode = mapper.createObjectNode();
                assetNode.put(QUERYBUILDER_PREDICATE_PATH, hit.getPath());
                arrayNode.add(assetNode);
            } catch (RepositoryException e) {
                LOGGER.error("Error while getting querybuilder hit's path :", e);
            }

        }
        String assetPropertiesJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);
        LOGGER.info(assetPropertiesJson);

        resourceResolver.close();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(assetPropertiesJson);
        LOGGER.trace("Exiting AssetPathExporterServlet servlet");
    }

}
