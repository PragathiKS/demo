package com.trs.core.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringJoiner;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.AssetManager;
import com.day.cq.dam.scene7.api.S7Config;
import com.day.cq.dam.scene7.api.S7ConfigResolver;
import com.day.cq.dam.scene7.api.Scene7Service;
import com.day.cq.dam.scene7.api.constants.Scene7Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trs.core.services.TagImporterService;
import com.trs.core.services.TrsConfigurationService;
import com.trs.core.utils.TrsUtils;

/**
 * This servlet is responsible for importing taxonomy generated from Xyleme
 * System into AEM. It expects file input in the same format as ACS Commons Tag
 * Importer utility
 */
@Component(service = Servlet.class,
        property = { "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.resourceTypes=" + "trs/components/structure/empty-page",
                "sling.servlet.selectors=assetdata", "sling.servlet.extensions=html" })
@ServiceDescription("TRS Tag Importer Servlet")
public class AssetMetadataExporterServlet extends SlingSafeMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssetMetadataExporterServlet.class);
    private static final long serialVersionUID = 1L;
    public static final String ASSET_RENDITION_SUFFIX = "/jcr:content/renditions/original/jcr:content";
    public static final String TRS_RESOURCE_RESOLVER_SUBSERVICE = "trs-taxonomy-subservice";
    public static final String TAG_NAMESPACE_SEPARATOR = ":";
    public static final String TAG_ID_SEPARATOR = "/";
    public static final String JCR_DATA = "jcr:data";

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private TrsConfigurationService trsConfig;

    @Reference
    private TagImporterService tagImporterService;

    @Reference
    private Scene7Service scene7Service;

    @Reference
    private S7ConfigResolver s7ConfigResolver;

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse resp)
            throws ServletException, IOException {

        ResourceResolver resourceResolver = null;
        final String DAMSCENE7ID = "dam:scene7ID";
        String value = "defaultValue";
        LOGGER.info(" Query param :" + request.getParameter("id"));
        ResourceResolver configResolver = null;
        try {
            configResolver = resolverFactory.getServiceResourceResolver(Collections
                    .singletonMap(ResourceResolverFactory.SUBSERVICE, (Object) Scene7Constants.S7_CONFIG_SERVICE));
        } catch (LoginException e) {
            LOGGER.error("",e);
        }
        Asset asset = resourceResolver.getResource(request.getParameter("id"))
                .adaptTo(Asset.class);
        S7Config s7Config = resolveScene7Configuration(asset, configResolver, s7ConfigResolver, LOGGER);

        if (null != s7Config) {
            String assetHandle = asset.getMetadataValue(DAMSCENE7ID);

            if (null != scene7Service && null != assetHandle) {
                Map<String, String> assetMetadata = scene7Service.getS7AssetMetadata(s7Config, assetHandle);
                for (Map.Entry<String, String> entry : assetMetadata.entrySet()) {
                    LOGGER.info("for the key : " + entry.getKey() + " value is :" + entry.getValue());
                }
            }
        }
        resourceResolver.close();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write("{\"customresponse\":\"sucess" + " with param value as :" + request.getParameter("id")
                + "  & dm value " + value + "\"}");
    }

    private S7Config resolveScene7Configuration(Asset asset, ResourceResolver rr, S7ConfigResolver s7ConfigResolver,
            Logger log) {
        Resource resource = asset.adaptTo(Resource.class);

        String s7ConfigPath = s7ConfigResolver.getS7ConfigPathForResource(rr, resource);
        S7Config config = s7ConfigResolver.getS7Config(rr, s7ConfigPath);
        if (config == null) {
            log.error("Could not resolve a S7Config from resource {}",
                    (resource != null ? resource.getPath() : "null"));
        }

        return config;
    }

}
