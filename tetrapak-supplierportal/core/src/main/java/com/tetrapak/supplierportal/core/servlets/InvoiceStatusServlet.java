package com.tetrapak.supplierportal.core.servlets;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;
import com.tetrapak.supplierportal.core.services.InvoiceStatusService;

@Component(service = Servlet.class, property = {
		Constants.SERVICE_DESCRIPTION + "=" + "Servlet to get the Invoice Status",
		ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
		ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES + "=" + "supplierportal/components/content/allpayments",
		ServletResolverConstants.SLING_SERVLET_SELECTORS + "=" + "invoice.status" })
public class InvoiceStatusServlet extends SlingSafeMethodsServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Reference
	private InvoiceStatusService service;

	private Gson gson = new GsonBuilder().create();

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		Map<String, List<String>> map = service.invoiceStatusCodeMap();
		String jsonString = gson.toJson(map);
		response.setContentType(SupplierPortalConstants.APPLICATION_JSON);
		response.getWriter().write(jsonString);
	}
}
