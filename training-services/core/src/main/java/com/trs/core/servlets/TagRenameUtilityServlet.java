package com.trs.core.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
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

import com.trs.core.reports.StatefulReport;
import com.trs.core.services.TaxonomyService;
import com.trs.core.services.TrsConfigurationService;
import com.trs.core.utils.TrsUtils;

/**
 * The following servlet converts the GUID tags provided by Xyleme Tags (stored
 * in xylemeTags property in AEM) to AEM tags
 */
@Component(service = Servlet.class, property = { "sling.servlet.methods=" + HttpConstants.METHOD_POST,
        "sling.servlet.paths=" + "/bin/tagrenameutility" })
@ServiceDescription("Tag Conversion Utility Servlet")
public class TagRenameUtilityServlet extends SlingAllMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(TagRenameUtilityServlet.class);

    private static final long serialVersionUID = 1L;
    private static final String ANCHOR_TAG_START = "<a href=\"";
    private static final String ANCHOR_START_TAG_TERMINATION = "\">";
    private static final String ANCHOR_TAG_END = "</a>";
    private static final String LOG_FILE_LINK_TEXT = "here";

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private TrsConfigurationService trsConfig;

    @Reference
    private TaxonomyService trsTaxonomyService;

    private StatefulReport taxonomyServiceReport;

    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse resp)
            throws ServletException, IOException {

        String servletResponseMessage;

        // Creation of Author log for the Tags conversion operation
        taxonomyServiceReport = trsTaxonomyService.createTaxonomyServiceReport();
        ResourceResolver resourceResolver = null;
        try {
            resourceResolver = TrsUtils.getTrsResourceResolver(resolverFactory);
        } catch (LoginException e) {
            LOGGER.error("Error while fetching resource resolver for TagRenameUtilityServlet :", e);
        }

        if (ServletFileUpload.isMultipartContent(request)) {

            Map<String, RequestParameter[]> requestParameters = request.getRequestParameterMap();
            iterateRequestParameterMap(resourceResolver, requestParameters);
            TrsUtils.saveExcelSheetAsDamAsset(taxonomyServiceReport, resourceResolver,
                    trsConfig.getTaxonomyTransformationLogFilePath());
            resourceResolver.close();

            servletResponseMessage = "Operation completed. Check logs " + ANCHOR_TAG_START + request.getScheme() + "://"
                    + request.getServerName() + trsConfig.getTaxonomyTransformationLogFilePath()
                    + ANCHOR_START_TAG_TERMINATION + LOG_FILE_LINK_TEXT + ANCHOR_TAG_END;
            resp.setContentType("text/plain");
            resp.getWriter().write(servletResponseMessage);
        }
    }

    private void iterateRequestParameterMap(ResourceResolver resourceResolver,
            Map<String, RequestParameter[]> requestParameters) throws IOException {
        for (final Map.Entry<String, RequestParameter[]> entry : requestParameters.entrySet()) {
            RequestParameter[] pArr = entry.getValue();
            RequestParameter param = pArr[0];
            InputStream stream = param.getInputStream();

            if (param.isFormField()) {
                LOGGER.debug("param is form field");

            } else {
                iterateRows(resourceResolver, stream);
                if (stream != null) {
                    stream.close();
                }

            }
        }
    }

    private void iterateRows(ResourceResolver resourceResolver, InputStream stream) throws IOException {
        String currentLine;
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        int rowCounter = 0;
        while ((currentLine = inputReader.readLine()) != null) {
            String[] str = currentLine.split(",");
            String assetPath;

            if (str.length > 0 && rowCounter > 0) {
                if (StringUtils.isNoneBlank(str[0])) {
                    assetPath = str[0];
                } else {
                    continue;
                }
                trsTaxonomyService.convertXylemeTagsToAEMTags(resourceResolver, assetPath, null, taxonomyServiceReport);
            }
            rowCounter++;
        }
        inputReader.close();
    }
}
