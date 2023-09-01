package com.tetrapak.supplierportal.core.servlets;

import java.io.IOException;
import java.util.Objects;
import javax.servlet.Servlet;
import javax.servlet.http.Cookie;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.xss.XSSAPI;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.JsonObject;
import com.tetrapak.supplierportal.core.services.APIGEEService;
import com.tetrapak.supplierportal.core.utils.HttpUtil;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.STATUS_CODE;

/**
 * API GEE Token Generator Servlet
 *
 * @author Sunil Kumar Yadav
 */
@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Supplier Portal API GEE Token Generator Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=" + "/bin/supplierportal/token-generator"
        })
public class APIGEETokenGeneratorServlet extends SlingSafeMethodsServlet {

    @Reference
    private APIGEEService apigeeService;

    /** The XSSAPI  */
    @Reference
    private transient XSSAPI xssAPI;

    private static final long serialVersionUID = 1;

    private static final Logger LOGGER = LoggerFactory.getLogger(APIGEETokenGeneratorServlet.class);
    
    private static final String TOKEN_NAME = "acctoken";

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        LOGGER.debug("HTTP GET request from APIGEETokenGeneratorServlet");
        String acctokenStr = StringUtils.EMPTY;
        Cookie acctoken = request.getCookie(TOKEN_NAME);
        if (Objects.nonNull(acctoken)) {
        	acctokenStr = xssAPI.encodeForHTML(acctoken.getValue());
        }
        LOGGER.info("Acctoken String {}" ,apigeeService);
        JsonObject jsonResponse = apigeeService.retrieveAPIGEEToken(acctokenStr);
        response.setStatus(jsonResponse.get(STATUS_CODE).getAsInt());
        jsonResponse.remove(STATUS_CODE);
        HttpUtil.writeJsonResponse(response, jsonResponse);       
    }
}
