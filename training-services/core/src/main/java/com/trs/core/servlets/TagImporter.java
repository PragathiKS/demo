package com.trs.core.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.AssetManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trs.core.services.TagImporterService;
import com.trs.core.services.TrsConfigurationService;
import com.trs.core.utils.TrsUtils;

/**
 * This servlet is responsible for importing taxonomy generated from Xyleme
 * System into AEM. It expects file input in the same format as ACS Commons Tag
 * Importer utility
 */
@Component(service = Servlet.class, property = { "sling.servlet.methods=" + HttpConstants.METHOD_POST,
        "sling.servlet.paths=" + "/bin/tagimporter" })
@ServiceDescription("TRS Tag Importer Servlet")
public class TagImporter extends SlingAllMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(TagImporter.class);
    private static final long serialVersionUID = 1L;
    public static final String ASSET_RENDITION_SUFFIX = "/jcr:content/renditions/original/jcr:content";
    public static final String TRS_RESOURCE_RESOLVER_SUBSERVICE = "trs-taxonomy-subservice";
    public static final String TAG_NAMESPACE_SEPARATOR = ":";
    public static final String TAG_ID_SEPARATOR = "/";
    public static final String JCR_DATA = "jcr:data";
    private static final String SERVLET_SUCCESS_MESSAGE = "Tags created successfully!";
    private static final String FAILURE_MESSAGE_PREFIX = "Operation failed!";

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private TrsConfigurationService trsConfig;
    
    @Reference
    private TagImporterService tagImporterService;

    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse resp)
            throws ServletException, IOException {

        ResourceResolver resourceResolver = null;
        try {
            resourceResolver = TrsUtils.getTrsResourceResolver(resolverFactory);
        } catch (LoginException e) {
            LOGGER.error("Error while fetching resource resolver for TagImporter :", e);
        }

        AssetManager assetManager = resourceResolver.adaptTo(AssetManager.class);
        Map<String, String> tagMappings = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        String servletResponseMessage;
        StringJoiner errorItems = new StringJoiner(System.lineSeparator());

        tagImporterService.readTagMappingFile(resourceResolver, tagMappings,mapper);
        
        if (ServletFileUpload.isMultipartContent(request)) {
            Map<String, RequestParameter[]> requestParameters = request.getRequestParameterMap();
            for (final Map.Entry<String, RequestParameter[]> entry : requestParameters.entrySet()) {
                String formField = entry.getKey();
                LOGGER.debug("Request param key" + formField);
                RequestParameter[] pArr = entry.getValue();
                RequestParameter param = pArr[0];
                InputStream stream = param.getInputStream();

                if (param.isFormField()) {
                    LOGGER.debug("Request param is form field");

                } else {
                    LOGGER.debug("Request param is not form field");
                    LOGGER.debug("Request param file name : " + param.getFileName());

                    final XSSFWorkbook workbook = new XSSFWorkbook(stream);

                    final XSSFSheet sheet = workbook.getSheetAt(0);
                    final Iterator<Row> rows = sheet.rowIterator();

                    tagImporterService.iterateRows(resourceResolver, tagMappings, errorItems, rows);
                    workbook.close();

                    InputStream targetStream = IOUtils.toInputStream(mapper.writeValueAsString(tagMappings),
                            StandardCharsets.UTF_8);
                    assetManager.createAsset(trsConfig.getXylemeAEMTagMappingFilePath(), targetStream, "text/plain", true);

                    targetStream.close();
                }
                stream.close();
            }
        }
        resourceResolver.close();

        if (StringUtils.isEmpty(errorItems.toString())) {
            LOGGER.info("All the Xyleme tags imported successfully into AEM!");
            servletResponseMessage = SERVLET_SUCCESS_MESSAGE;
        } else {
            LOGGER.info("Issues while importing following items : " + errorItems);
            servletResponseMessage = FAILURE_MESSAGE_PREFIX;
        }
        resp.setContentType("text/plain");
        resp.getWriter().write(servletResponseMessage);
    }

   
}
