package com.tetrapak.customerhub.core.servlets;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.servlet.http.Cookie;

import org.apache.http.HttpStatus;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.i18n.ResourceBundleProvider;
import org.apache.sling.testing.mock.sling.servlet.MockRequestPathInfo;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import com.day.cq.wcm.api.Page;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.GenericServiceType;
import com.tetrapak.customerhub.core.mock.MockOrderDetailsApiServiceImpl;
import com.tetrapak.customerhub.core.services.OrderDetailsApiService;
import com.tetrapak.customerhub.core.services.OrderDetailsExcelService;
import com.tetrapak.customerhub.core.services.OrderDetailsPDFService;
import com.tetrapak.customerhub.core.services.impl.OrderDetailsExcelServiceImpl;
import com.tetrapak.customerhub.core.services.impl.OrderDetailsPDFServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OrderDetailsDownloadFileServletTest {

	@Mock
	private Page mockPage;

	@Mock
	private ResourceBundleProvider mockResourceBundleProvider;

	private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/en/ordering/order-history/order-details-parts";
	private static final String COMPONENT_PATH = "/content/tetrapak/customerhub/en/ordering/order-history/order-details-parts/jcr:content/root/responsivegrid/orderdetails";
	private static final String RESOURCE_JSON = "order-detailspage.json";
	private static final String I18_RESOURCE = "/apps/customerhub/i18n/en";
	private static final String I18_RESOURCE_JSON = "/orderDetailsI18n.json";

	@Rule
	public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON, CONTENT_ROOT,
			getMultipleMockedService());

	@Before
	public void setup() throws IOException {
		ResourceBundle resourceBundle = new PropertyResourceBundle(
				this.getClass().getResourceAsStream("/i18n.properties"));
		aemContext.registerService(ResourceBundleProvider.class, mockResourceBundleProvider);
		when(mockResourceBundleProvider.getResourceBundle(any(), any())).thenReturn(resourceBundle);
		aemContext.load().json(I18_RESOURCE_JSON, I18_RESOURCE);
		aemContext.currentResource(COMPONENT_PATH);
		aemContext.request().setServletPath(COMPONENT_PATH);
		aemContext.request().setMethod(HttpConstants.METHOD_POST);
		Cookie cookie = new Cookie("authToken", "cLBKhQAPhQCZ2bzGW5j2yXYBb6de");
		aemContext.request().addCookie(cookie);
	}

	@Test
	public void doPostForPdfParts() {
		MockRequestPathInfo requestPathInfo = (MockRequestPathInfo) aemContext.request().getRequestPathInfo();
		requestPathInfo.setSelectorString("parts");
		requestPathInfo.setExtension("pdf");
		requestPathInfo.setSuffix("orderNumber=1234&token=9KK12diCgjVCmJF8MzeAt1IauZOq");
		MockSlingHttpServletRequest request = aemContext.request();
		MockSlingHttpServletResponse response = aemContext.response();
		OrderDetailsDownloadFileServlet orderDetailsDownloadFileServlet = aemContext
				.getService(OrderDetailsDownloadFileServlet.class);
		aemContext.registerInjectActivateService(orderDetailsDownloadFileServlet);
		orderDetailsDownloadFileServlet.doPost(request, response);
		assertEquals(HttpStatus.SC_OK, response.getStatus());
	}

	@Test
	public void doPostForPdfPackMat() {
		MockRequestPathInfo requestPathInfo = (MockRequestPathInfo) aemContext.request().getRequestPathInfo();
		requestPathInfo.setSelectorString("packmat");
		requestPathInfo.setExtension("pdf");
		requestPathInfo.setSuffix("orderNumber=1234&token=9KK12diCgjVCmJF8MzeAt1IauZOq");
		MockSlingHttpServletRequest request = aemContext.request();
		MockSlingHttpServletResponse response = aemContext.response();
		OrderDetailsDownloadFileServlet orderDetailsDownloadFileServlet = aemContext
				.getService(OrderDetailsDownloadFileServlet.class);
		aemContext.registerInjectActivateService(orderDetailsDownloadFileServlet);
		orderDetailsDownloadFileServlet.doPost(request, response);
		assertEquals(HttpStatus.SC_OK, response.getStatus());
	}

	@Test
	public void doPostForExcelParts() {
		MockRequestPathInfo requestPathInfo = (MockRequestPathInfo) aemContext.request().getRequestPathInfo();
		requestPathInfo.setSelectorString("parts");
		requestPathInfo.setExtension("excel");
		requestPathInfo.setSuffix("orderNumber=1234&token=9KK12diCgjVCmJF8MzeAt1IauZOq");
		MockSlingHttpServletRequest request = aemContext.request();
		MockSlingHttpServletResponse response = aemContext.response();
		OrderDetailsDownloadFileServlet orderDetailsDownloadFileServlet = aemContext
				.getService(OrderDetailsDownloadFileServlet.class);
		aemContext.registerInjectActivateService(orderDetailsDownloadFileServlet);
		orderDetailsDownloadFileServlet.doPost(request, response);
		assertEquals(HttpStatus.SC_OK, response.getStatus());
	}

	@Test
	public void doPostForErrors() {
		MockRequestPathInfo requestPathInfo = (MockRequestPathInfo) aemContext.request().getRequestPathInfo();
		requestPathInfo.setSelectorString("parts");
		requestPathInfo.setExtension("error");
		requestPathInfo.setSuffix("orderNumber=1234&token=9KK12diCgjVCmJF8MzeAt1IauZOq");
		MockSlingHttpServletRequest request = aemContext.request();
		MockSlingHttpServletResponse response = aemContext.response();
		OrderDetailsDownloadFileServlet orderDetailsDownloadFileServlet = aemContext
				.getService(OrderDetailsDownloadFileServlet.class);
		aemContext.registerInjectActivateService(orderDetailsDownloadFileServlet);
		orderDetailsDownloadFileServlet.doPost(request, response);
		assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatus());
	}

	public <T> List<GenericServiceType<T>> getMultipleMockedService() {

		GenericServiceType<OrderDetailsApiService> orderDetailsApiServiceGenericServiceType = new GenericServiceType<>();
		orderDetailsApiServiceGenericServiceType.setClazzType(OrderDetailsApiService.class);
		orderDetailsApiServiceGenericServiceType.set(new MockOrderDetailsApiServiceImpl());

		GenericServiceType<OrderDetailsPDFService> orderDetailsPDFServiceGenericServiceType = new GenericServiceType<>();
		orderDetailsPDFServiceGenericServiceType.setClazzType(OrderDetailsPDFService.class);
		orderDetailsPDFServiceGenericServiceType.set(new OrderDetailsPDFServiceImpl());

		GenericServiceType<OrderDetailsExcelService> orderDetailsExcelServiceGenericServiceType = new GenericServiceType<>();
		orderDetailsExcelServiceGenericServiceType.setClazzType(OrderDetailsExcelService.class);
		orderDetailsExcelServiceGenericServiceType.set(new OrderDetailsExcelServiceImpl());

		GenericServiceType<OrderDetailsDownloadFileServlet> orderDetailsDownloadFileServletGenericServiceType = new GenericServiceType<>();
		orderDetailsDownloadFileServletGenericServiceType.setClazzType(OrderDetailsDownloadFileServlet.class);
		orderDetailsDownloadFileServletGenericServiceType.set(new OrderDetailsDownloadFileServlet());

		List<GenericServiceType<T>> serviceTypes = new ArrayList<>();
		serviceTypes.add((GenericServiceType<T>) orderDetailsApiServiceGenericServiceType);
		serviceTypes.add((GenericServiceType<T>) orderDetailsPDFServiceGenericServiceType);
		serviceTypes.add((GenericServiceType<T>) orderDetailsExcelServiceGenericServiceType);
		serviceTypes.add((GenericServiceType<T>) orderDetailsDownloadFileServletGenericServiceType);
		return serviceTypes;
	}
}
