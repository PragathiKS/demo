package com.tetrapak.customerhub.core.servlets;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.exceptions.SecutiyRuntimeException;
import com.tetrapak.customerhub.core.services.FinancialResultsApiService;
import com.tetrapak.customerhub.core.utils.HttpUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpResponse;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.xss.XSSAPI;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Financial Invoice PDF Download FileServlet using URL: /bin/customerhub/invoice/document.<doc number>.pdf supports
 * both GET and POST
 *
 * @author Nitin Kumar
 */
@Component(service = Servlet.class, property = {
        Constants.SERVICE_DESCRIPTION + "=PDF Generator Servlet for Invoice for given document ID",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.paths=" + "/bin/customerhub/invoice/document",
        "sling.servlet.extensions=" + CustomerHubConstants.PDF
})
public class FinancialInvoiceDownloadFileServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 2323660841296799482L;

    @Reference
    private FinancialResultsApiService financialsResultsApiService;

    private static final Logger LOGGER = LoggerFactory.getLogger(FinancialInvoiceDownloadFileServlet.class);

    private static final String AUTH_TOKEN = "authToken";

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        final String extension = request.getRequestPathInfo().getExtension();
        String documentNumber = request.getRequestPathInfo().getSelectorString();
        final String token = request.getCookie(AUTH_TOKEN) == null ? StringUtils.EMPTY
                : getAuthTokenValue(request);
        LOGGER.debug("Got authToken from cookie : {}", token);

        if (CustomerHubConstants.PDF.equals(extension) && StringUtils.isNotBlank(token)
                && StringUtils.isNotBlank(documentNumber)) {
            HttpResponse httpResp = null;
            
            try {
            	httpResp = financialsResultsApiService.getFinancialInvoice(documentNumber, token);
            } catch (SecutiyRuntimeException secRTExcep) {
            	LOGGER.error("Auth token is invalid! {}", secRTExcep.getMessage());
            	response.setStatus(HttpServletResponse.SC_LENGTH_REQUIRED);
                JsonObject jsonResponse = new JsonObject();
                jsonResponse.addProperty("errorMsg",
                        "Auth token is invalid!" + request.getRequestPathInfo());
                HttpUtil.writeJsonResponse(response, jsonResponse);
                throw secRTExcep;
			} catch (Exception e) {
				LOGGER.error("Exception {}", e);
			}

            int statusCode = httpResp.getStatusLine().getStatusCode();
            LOGGER.debug("Retrieved response from API got status code:{}", statusCode);

            HeaderIterator headerItr = httpResp.headerIterator();
            while (headerItr.hasNext()) {
                Header apiRespHeader = headerItr.nextHeader();
                response.setHeader(apiRespHeader.getName(), apiRespHeader.getValue());
            }
            httpResp.getEntity().writeTo(response.getOutputStream());
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("errorMsg",
                    "URL is invalid or auth token is missing : " + request.getRequestPathInfo());
            HttpUtil.writeJsonResponse(response, jsonResponse);
        }
    }

    private String getAuthTokenValue(SlingHttpServletRequest request) {
        if (null == request.getCookie(AUTH_TOKEN)) {
            return StringUtils.EMPTY;
        }
        XSSAPI xssAPI = request.getResourceResolver().adaptTo(XSSAPI.class);
        return xssAPI.encodeForHTML(request.getCookie(AUTH_TOKEN).getValue()) ;
    }

}
