package com.tetrapak.customerhub.core.servlets;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.spareparts.ImageLinks;
import com.tetrapak.customerhub.core.beans.spareparts.ImageResponse;
import com.tetrapak.customerhub.core.beans.spareparts.SparePart;
import com.tetrapak.customerhub.core.services.SparePartsService;
import com.tetrapak.customerhub.core.utils.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.post.JSONResponse;
import org.apache.sling.xss.XSSAPI;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.Servlet;
import java.io.IOException;
import java.util.ArrayList;

import static com.tetrapak.customerhub.core.constants.CustomerHubConstants.DIMENSION;
import static com.tetrapak.customerhub.core.constants.CustomerHubConstants.PART_NUMBER;

/**
 * This servlet is used to get the product image for upgrade kits. It involves 2 API Calls
 * - one to get the image link and another to get the binary
 */
@Component(service = Servlet.class, property = { "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.selectors=" + GetEbizProductImageServlet.SLING_SERVLET_SELECTOR,
        "sling.servlet.extensions=" + GetEbizProductImageServlet.SLING_SERVLET_EXTENSION,
        "sling.servlet.resourceTypes=" + GetEbizProductImageServlet.SLING_SERVLET_RESOURCE_TYPES
})
public class GetEbizProductImageServlet extends SlingSafeMethodsServlet {

    @Reference
    private SparePartsService sparePartsService;

    public static final String SLING_SERVLET_SELECTOR = "ebizproductimage";

    public static final String SLING_SERVLET_EXTENSION = "img";

    public static final String SLING_SERVLET_RESOURCE_TYPES = "customerhub/components/content/rebuildingkitdetails";

    private static final Logger LOGGER = LoggerFactory.getLogger(GetEbizProductImageServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {

        String partNumber = request.getParameter(PART_NUMBER);
        String dimension = request.getParameter(DIMENSION);
        if(StringUtils.isBlank(partNumber) || StringUtils.isBlank(dimension)){
            LOGGER.error("Part number or dimension is blank");
            JsonObject jsonObject = new JsonObject();
            HttpUtil.setJsonResponse(jsonObject,"Bad Request", HttpStatus.SC_BAD_REQUEST);
            HttpUtil.writeJsonResponse(response,jsonObject);
            return;
        }

        ImageResponse imageResponse = sparePartsService.getImage(dimension,partNumber);
        if(imageResponse!=null){
            MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
            String mimeType = fileTypeMap.getContentType(StringUtils.substringAfterLast(imageResponse.getImageLink(),"/"));
            response.setContentType(mimeType);
            imageResponse.getHttpResponse().getEntity().writeTo(response.getOutputStream());
        }else {
            LOGGER.error("Error from Image response");
            JsonObject jsonObject = new JsonObject();
            HttpUtil.setJsonResponse(jsonObject,"System Error", HttpStatus.SC_INTERNAL_SERVER_ERROR);
            HttpUtil.writeJsonResponse(response,jsonObject);
        }
    }
}
