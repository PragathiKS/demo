package com.tetrapak.supplierportal.core.servlets;

import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.STATUS_CODE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.http.HttpStatus;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gson.JsonObject;
import com.tetrapak.supplierportal.core.services.InvoiceStatusService;

import io.wcm.testing.mock.aem.junit.AemContext;

@RunWith(MockitoJUnitRunner.class)
public class InvoiceStatusServletTest {

	@InjectMocks
	private InvoiceStatusServlet servlet;

	@Mock
	private InvoiceStatusService service;

	@Rule
	public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

	private MockSlingHttpServletResponse response = context.response();

	@Spy
	private MockSlingHttpServletRequest request = context.request();

	@Before
	public void setup() throws IOException {
		MockitoAnnotations.initMocks(this);
		JsonObject jsonResponse = new JsonObject();
		jsonResponse.addProperty(STATUS_CODE, 200);
		Map<String, List<Integer>> map = new HashMap<>();
		when(service.invoiceStatusCodeMap()).thenReturn(map);
	}

	@Test
	public void doGet() throws IOException, ServletException {
		servlet.doGet(request, response);
		assertEquals("Status should be ok ", HttpStatus.SC_OK, response.getStatus());
	}
}
