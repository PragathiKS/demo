package com.tetrapak.supplierportal.core.servlets;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.apache.sling.xss.XSSAPI;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tetrapak.supplierportal.core.bean.PaymentDetails;
import com.tetrapak.supplierportal.core.models.PaymentDetailsModel;
import com.tetrapak.supplierportal.core.services.PaymentInvoiceDownloadService;

import io.wcm.testing.mock.aem.junit.AemContext;

@RunWith(MockitoJUnitRunner.class)
public class PaymentInvoiceExportServletTest {

	private static final String DOCUMENT_REFERENCE_ID = "documentreferenceid";

	private static final String AUTHTOKEN = "authToken";

	private static final String STATUS_CODE = "status_code";

	@InjectMocks
	private PaymentInvoiceExportServlet servlet;

	@Mock
	private PaymentInvoiceDownloadService service;

	@Mock
	private XSSAPI xssAPI;

	@Mock
	private Resource resource;;

	@Mock
	private PaymentDetails paymentDetails;

	@Mock
	private PaymentDetailsModel model;

	@Rule
	public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

	private MockSlingHttpServletResponse response = context.response();

	@Spy
	private MockSlingHttpServletRequest request = context.request();

	@Mock
	private Cookie authToken;

	@Before
	public void setup() throws IOException {
		MockitoAnnotations.initMocks(this);
		Mockito.when(request.getCookie(AUTHTOKEN)).thenReturn(authToken);
		Mockito.when(authToken.getValue()).thenReturn(AUTHTOKEN);
		when(request.getResource()).thenReturn(resource);
		JsonObject jsonResponse = new JsonObject();
		jsonResponse.addProperty(STATUS_CODE, 200);
		when(service.preparePdf(any(), any(), any(), any())).thenReturn(true);
		when(service.retrievePaymentDetails(anyString(), anyString()))
				.thenReturn(jsonResponse);
	}

	@Test
	public void doGetWhenDocRefIdMissing() throws IOException, ServletException {
		Map<String, Object> map = new HashMap<>();
		request.setParameterMap(map);
		servlet.doGet(request, response);
		assertEquals("Status should be ok ", HttpStatus.SC_BAD_REQUEST, response.getStatus());
	}


	@Test
	public void doGetWhenAuthTokenMissing() throws IOException, ServletException {
		Map<String, Object> map = new HashMap<>();
		map.put(DOCUMENT_REFERENCE_ID, "12345678");
		Mockito.when(request.getCookie(AUTHTOKEN)).thenReturn(null);
		request.setParameterMap(map);
		servlet.doGet(request, response);
		assertEquals("Status should be ok ", HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void doGetWhenModelIsNull() throws IOException, ServletException {
		Map<String, Object> map = new HashMap<>();
		map.put(DOCUMENT_REFERENCE_ID, "12345678");
		request.setParameterMap(map);
		servlet.doGet(request, response);
		assertEquals("Status should be ok ", HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void doGetWhenStatusIsNot200() throws IOException, ServletException {
		Map<String, Object> map = new HashMap<>();
		map.put(DOCUMENT_REFERENCE_ID, "12345678");
		request.setParameterMap(map);
		JsonObject jsonResponse = new JsonObject();
		jsonResponse.addProperty(STATUS_CODE, 400);
		when(request.getResource()).thenReturn(resource);
		when(resource.adaptTo(PaymentDetailsModel.class)).thenReturn(model);
		when(service.retrievePaymentDetails(anyString(), anyString()))
				.thenReturn(jsonResponse);
		servlet.doGet(request, response);
		assertEquals("Status should be ok ", HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void doGet() throws IOException, ServletException, URISyntaxException {
		Map<String, Object> map = new HashMap<>();
		map.put(DOCUMENT_REFERENCE_ID, "12345678");
		request.setParameterMap(map);
		when(request.getResource()).thenReturn(resource);
		when(resource.adaptTo(PaymentDetailsModel.class)).thenReturn(model);
		String content = IOUtils.toString(this.getClass().getResourceAsStream("/paymentdetails/view-payment-data.json"),
				"UTF-8");
		Gson gson = new Gson();
		JsonElement element = gson.fromJson(content, JsonElement.class);
		JsonObject jsonObj = element.getAsJsonObject();
		jsonObj.addProperty(STATUS_CODE, 200);
		when(service.retrievePaymentDetails(anyString(),anyString())).thenReturn(jsonObj);
		servlet.doGet(request, response);
		assertEquals("Status should be ok ", HttpStatus.SC_OK, response.getStatus());
	}

}
