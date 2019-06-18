package com.tetrapak.customerhub.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpResponse;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.FinancialsResultsApiService;
import com.tetrapak.customerhub.core.utils.HttpUtil;

/**
 * Financials Invoice PDF Download FileServlet using URL:
 * /bin/customerhub/invoice/document.<doc number>.pdf supports both GET and POST
 *
 * @author Swati Lamba
 */
@Component(service = Servlet.class, property = {
		Constants.SERVICE_DESCRIPTION + "=PDF Generator Servlet for Invoice for given document ID",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.paths=" + "/bin/customerhub/invoice/document",
		"sling.servlet.extensions=" + CustomerHubConstants.PDF })
public class FinancialsInvoiceDownloadFileServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 2323660841296799482L;

	@Reference
	private FinancialsResultsApiService financialsResultsApiService;

	private static final Logger LOGGER = LoggerFactory.getLogger(FinancialsInvoiceDownloadFileServlet.class);

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
		final String extension = request.getRequestPathInfo().getExtension();
		String documentNumber = request.getRequestPathInfo().getSelectorString();
		final String token = request.getCookie("authToken") == null ? StringUtils.EMPTY
				: request.getCookie("authToken").getValue();
		LOGGER.debug("Got authToken from cookie : {}", token);

		if (CustomerHubConstants.PDF.equals(extension) && StringUtils.isNotBlank(token)
				&& StringUtils.isNotBlank(documentNumber)) {
			HttpResponse httpResp = financialsResultsApiService.getFinancialsInvoice(documentNumber, token);

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

}
