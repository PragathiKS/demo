package com.trs.core.servlets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.jcr.RepositoryException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
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

import com.day.cq.dam.api.Asset;
import com.day.cq.search.result.Hit;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.trs.core.services.AssetMetadataService;
import com.trs.core.services.AssetPageOpsService;
import com.trs.core.services.TrsConfigurationService;
import com.trs.core.utils.TrsConstants;
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

    // API Output Keys for Non DM Asset Metadata
    private static final String MLV_ID = "mlv_id";
    private static final String ASSET_NAME = "asset_name";
    private static final String ASSET_PATH = "asset_path";
    private static final String ASSET_METADATA_LANGUAGE = "asset_property_language";
    private static final String ASSET_FOLDER_HIERARCY_LANGUAGE = "content_hierarchy_language";
    private static final String LAST_MODIFICATION_DATE = "last_modification_date";
    private static final String PUBLIC_URL = "public_url";

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
        LOGGER.info("Entered AssetMetadataExporterServlet servlet");
        LOGGER.info(" Query param :" + request.getParameter("id"));
        try {
            resourceResolver = TrsUtils.getTrsResourceResolver(resolverFactory);
        } catch (LoginException e) {
            LOGGER.error("Error while getting resource resolver : ", e);
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
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
            ObjectNode assetNode = mapper.createObjectNode();
            try {
                String assetPath = hit.getPath().replace(TrsConstants.ASSET_METADATA_RELATIVE_PATH, StringUtils.EMPTY);
                LOGGER.info(" hit : " + hit.getPath());
                Asset asset = resourceResolver.getResource(assetPath).adaptTo(Asset.class);
                Optional.ofNullable(asset.getMetadataValue(trsConfig.getMlvIdJCRPropName())).ifPresent(s -> {
                    assetNode.put(MLV_ID, s);
                });
                assetNode.put(ASSET_NAME, asset.getName());
                assetNode.put(ASSET_PATH, assetPath);
                Optional.ofNullable(asset.getMetadataValue(TrsConstants.ASSET_DC_LANGUAGE)).ifPresent(s -> {
                    assetNode.put(ASSET_METADATA_LANGUAGE, s);
                });
                assetNode.put(ASSET_FOLDER_HIERARCY_LANGUAGE,
                        assetPath.split(TrsConstants.FORWARD_SLASH)[Math.toIntExact(trsConfig.getAssetMetadataAPILanguageFolderLevel())]);
                assetNode.put(LAST_MODIFICATION_DATE, asset.getLastModified());
                Optional.ofNullable(asset.getMetadataValue(AssetPageOpsService.PAGE_PUBLIC_URL_JCR_PROPERTY))
                        .ifPresent(s -> {
                            assetNode.put(PUBLIC_URL, s);
                        });
                assetMetadataService.getAssetMetadataJsonNode(resourceResolver, assetNode, assetPath);
                arrayNode.add(assetNode);
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
