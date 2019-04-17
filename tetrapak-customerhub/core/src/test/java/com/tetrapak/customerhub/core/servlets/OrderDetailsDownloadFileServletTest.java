package com.tetrapak.customerhub.core.servlets;

import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.GenericServiceType;
import com.tetrapak.customerhub.core.mock.MockOrderDetailsApiServiceImpl;
import com.tetrapak.customerhub.core.services.OrderDetailsApiService;
import com.tetrapak.customerhub.core.services.OrderDetailsExcelService;
import com.tetrapak.customerhub.core.services.OrderDetailsPDFService;
import com.tetrapak.customerhub.core.services.impl.OrderDetailsExcelServiceImpl;
import com.tetrapak.customerhub.core.services.impl.OrderDetailsPDFServiceImpl;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.http.HttpStatus;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class OrderDetailsDownloadFileServletTest {

    private static final String SERVLET_RESOURCE_PATH = "/bin/customerhub/order-detail.parts." + CustomerHubConstants.PDF + "?orderNumber=123&token=213123123";
    private static final String SERVLET_RESOURCE_JSON = "allContent.json";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(SERVLET_RESOURCE_JSON, SERVLET_RESOURCE_PATH, getMultipleMockedService());

    @Before
    public void setup() {
        aemContext.currentResource(SERVLET_RESOURCE_PATH);
        aemContext.request().setServletPath(SERVLET_RESOURCE_PATH);
        aemContext.request().setMethod(HttpConstants.METHOD_GET);
    }

    @Test
    public void doGet() {
        MockSlingHttpServletRequest request = aemContext.request();
        MockSlingHttpServletResponse response = aemContext.response();
        OrderDetailsDownloadFileServlet orderDetailsDownloadFileServlet = aemContext.getService(OrderDetailsDownloadFileServlet.class);
        aemContext.registerInjectActivateService(orderDetailsDownloadFileServlet);
        orderDetailsDownloadFileServlet.doGet(request, response);
        assertEquals(HttpStatus.SC_OK, response.getStatus());
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
