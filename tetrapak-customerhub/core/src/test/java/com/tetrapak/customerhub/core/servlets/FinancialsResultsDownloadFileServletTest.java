package com.tetrapak.customerhub.core.servlets;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.mockito.runners.MockitoJUnitRunner;

import com.day.cq.wcm.api.Page;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.GenericServiceType;
import com.tetrapak.customerhub.core.mock.MockFinancialsResultsApiServiceImpl;
import com.tetrapak.customerhub.core.services.FinancialsResultsApiService;
import com.tetrapak.customerhub.core.services.FinancialsResultsExcelService;
import com.tetrapak.customerhub.core.services.FinancialsResultsPDFService;
import com.tetrapak.customerhub.core.services.impl.FinancialsResultsExcelServiceImpl;
import com.tetrapak.customerhub.core.services.impl.FinancialsResultsPDFServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * Test class for Financials Results Service
 *
 * @author ruhsharm
 */
@RunWith(MockitoJUnitRunner.class)
public class FinancialsResultsDownloadFileServletTest {

    @Mock
    private Page mockPage;

    @Mock
    private ResourceBundleProvider mockResourceBundleProvider;

    private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/financials";
    private static final String COMPONENT_PATH = "/content/tetrapak/customerhub/global/en/financials/jcr:content/root/responsivegrid/financialstatement";
    private static final String SERVLET_RESOURCE_JSON = "allContent.json";
    private static final String RESOURCE_JSON = "financialsresultspage.json";
    private static final String I18_RESOURCE = "/apps/customerhub/i18n/en";
    private static final String I18_RESOURCE_JSON = "/financialsresultsI18n.json";


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
		aemContext.request().addCookie(cookie );
    }

    @Test
    public void doPostForPdf() throws IOException {
        MockRequestPathInfo requestPathInfo = (MockRequestPathInfo) aemContext.request().getRequestPathInfo();
        requestPathInfo.setExtension("pdf");
        MockSlingHttpServletRequest request = aemContext.request();
        MockSlingHttpServletResponse response = aemContext.response();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(CustomerHubConstants.TOKEN, CustomerHubConstants.TEST_TOKEN);
        parameters.put("params", "{\"startDate\":\"2019-04-25\",\"customerData\":{\"key\":\"123\",\"desc\":\"John - Malmo\",\"info\":{\"acountNo\":\"12345\",\"title\":\"California Aseptic Beverages\",\"address\":\"Street 1A\"}},\"status\":{\"key\":\"1\",\"desc\":\"Open\"},\"documentType\":{\"key\":\"1\",\"desc\":\"Confirmed\"},\"documentNumber\":\"\"}");
        request.setParameterMap(parameters);

        FinancialsResultsDownloadFileServlet financialsResultsDownloadFileServlet = aemContext
                .getService(FinancialsResultsDownloadFileServlet.class);
        aemContext.registerInjectActivateService(financialsResultsDownloadFileServlet);
        financialsResultsDownloadFileServlet.doPost(request, response);
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    public void doPostForExcel() throws IOException {
        MockRequestPathInfo requestPathInfo = (MockRequestPathInfo) aemContext.request().getRequestPathInfo();
        requestPathInfo.setExtension("excel");
        MockSlingHttpServletRequest request = aemContext.request();
        MockSlingHttpServletResponse response = aemContext.response();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(CustomerHubConstants.TOKEN, CustomerHubConstants.TEST_TOKEN);
        parameters.put("params", "{\"startDate\":\"2019-04-25\",\"customerData\":{\"key\":\"123\",\"desc\":\"John - Malmo\",\"info\":{\"acountNo\":\"12345\",\"title\":\"California Aseptic Beverages\",\"address\":\"Street 1A\"}},\"status\":{\"key\":\"1\",\"desc\":\"Open\"},\"documentType\":{\"key\":\"1\",\"desc\":\"Confirmed\"},\"documentNumber\":\"\"}");
        request.setParameterMap(parameters);

        FinancialsResultsDownloadFileServlet financialsResultsDownloadFileServlet = aemContext
                .getService(FinancialsResultsDownloadFileServlet.class);
        aemContext.registerInjectActivateService(financialsResultsDownloadFileServlet);
        financialsResultsDownloadFileServlet.doPost(request, response);
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    public <T> List<GenericServiceType<T>> getMultipleMockedService() {

        GenericServiceType<FinancialsResultsApiService> apigeeServiceGenericServiceType = new GenericServiceType<>();
        apigeeServiceGenericServiceType.setClazzType(FinancialsResultsApiService.class);
        apigeeServiceGenericServiceType.set(new MockFinancialsResultsApiServiceImpl());

        GenericServiceType<FinancialsResultsPDFService> financialsResultsPDFServiceGenericServiceType = new GenericServiceType<>();
        financialsResultsPDFServiceGenericServiceType.setClazzType(FinancialsResultsPDFService.class);
        financialsResultsPDFServiceGenericServiceType.set(new FinancialsResultsPDFServiceImpl());

        GenericServiceType<FinancialsResultsExcelService> excelServiceGenericServiceType = new GenericServiceType<>();
        excelServiceGenericServiceType.setClazzType(FinancialsResultsExcelService.class);
        excelServiceGenericServiceType.set(new FinancialsResultsExcelServiceImpl());

        GenericServiceType<FinancialsResultsDownloadFileServlet> financialsResultsDownloadFileServletGenericServiceType = new GenericServiceType<>();
        financialsResultsDownloadFileServletGenericServiceType.setClazzType(FinancialsResultsDownloadFileServlet.class);
        financialsResultsDownloadFileServletGenericServiceType.set(new FinancialsResultsDownloadFileServlet());

        List<GenericServiceType<T>> serviceTypes = new ArrayList<>();
        serviceTypes.add((GenericServiceType<T>) apigeeServiceGenericServiceType);
        serviceTypes.add((GenericServiceType<T>) financialsResultsPDFServiceGenericServiceType);
        serviceTypes.add((GenericServiceType<T>) financialsResultsDownloadFileServletGenericServiceType);
        serviceTypes.add((GenericServiceType<T>) excelServiceGenericServiceType);
        return serviceTypes;
    }

}
