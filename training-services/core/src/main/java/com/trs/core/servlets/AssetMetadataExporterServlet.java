package com.trs.core.servlets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.http.entity.ContentType;
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

import com.day.cq.search.result.Hit;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.trs.core.services.AssetMetadataService;
import com.trs.core.services.TrsConfigurationService;
import com.trs.core.utils.TrsUtils;

/**
 * This servlet is responsible for fetching selected metadata of an Asset
 */
@Component(service = Servlet.class,
        property = { "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.resourceTypes=" + "trs/components/structure/empty-page",
                "sling.servlet.selectors=assetdata", "sling.servlet.extensions=html" })
@ServiceDescription("Asset Metadata Exporter Servlet")
public class AssetMetadataExporterServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 5985429522998460184L;

    private static final Logger LOGGER = LoggerFactory.getLogger(AssetMetadataExporterServlet.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private TrsConfigurationService trsConfig;

    @Reference
    private AssetMetadataService assetMetadataService;

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse resp)
            throws ServletException, IOException {

        ResourceResolver resourceResolver = null;
        final String assetMlvId = request.getParameter("id");
        String assetPath;
        LOGGER.info("Entered AssetMetadataExporterServlet servlet");
        LOGGER.info(" Query param :" + request.getParameter("id"));
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
        LOGGER.info("This comment is only applicable for DEV. Please make sure dam:mlvId property is present");
        predicate.put("path", trsConfig.getAssetMetadataAPIBasePath());
        predicate.put("nodename", "metadata");
        predicate.put("property", trsConfig.getMlvIdJCRPropName());
        predicate.put("property.value", assetMlvId);

        List<Hit> resultHits = assetMetadataService.executeQuery(resourceResolver, predicate);

        for (Hit hit : resultHits) {

            try {
                assetPath = hit.getPath();
                LOGGER.info(" hit : " + hit.getPath());
                arrayNode.add(assetMetadataService.getAssetMetadataJsonNode(resourceResolver, mapper, assetPath));
            } catch (RepositoryException e) {
                LOGGER.error("Error while getting querybuilder hit's path :", e);
            }

        }
        String assetPropertiesJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);
        LOGGER.info(assetPropertiesJson);

        resourceResolver.close();
        resp.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.getWriter().write(assetPropertiesJson);
    }

}
