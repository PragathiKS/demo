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
import com.tetrapak.customerhub.core.mock.MockFinancialResultsApiServiceImpl;
import com.tetrapak.customerhub.core.services.FinancialResultsApiService;
import com.tetrapak.customerhub.core.services.FinancialResultsExcelService;
import com.tetrapak.customerhub.core.services.FinancialResultsPDFService;
import com.tetrapak.customerhub.core.services.impl.FinancialResultsExcelServiceImpl;
import com.tetrapak.customerhub.core.services.impl.FinancialResultsPDFServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * Test class for Financials Results Service
 * @author ruhsharm
 */
@RunWith(MockitoJUnitRunner.class)
public class FinancialResultsDownloadFileServletTest {
    
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
        aemContext.request().addCookie(cookie);
    }
    
    @Test
    public void doPostForPdf() throws IOException {
        MockRequestPathInfo requestPathInfo = (MockRequestPathInfo) aemContext.request().getRequestPathInfo();
        requestPathInfo.setExtension("pdf");
        MockSlingHttpServletRequest request = aemContext.request();
        MockSlingHttpServletResponse response = aemContext.response();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(CustomerHubConstants.TOKEN, CustomerHubConstants.TEST_TOKEN);
        parameters.put("params",
                "{\"startDate\":\"2018-05-24\",\"soaDate\":\"2018-05-25\",\"endDate\":\"2018-05-25\",\"customerData\":{\"customerNumber\":\"0000010050\",\"customerName\":\"Arla Foods AB1\",\"info\":{\"accountNo\":\"0000010050\",\"name1\":\"Arla Foods AB1\",\"name2\":\"Leverantorsreskontran\",\"street\":\"Arla Foods Bolagskontor\",\"city\":\"STOCKHOLM\",\"state\":\"012\",\"postalcode\":\"105 46\",\"country\":\"SE\"},\"key\":\"0000010050\",\"desc\":\"0000010050 - Arla Foods AB1 - STOCKHOLM\"},\"status\":{\"key\":\"B\",\"desc\":\"Both\"},\"documentType\":{\"key\":\"CM\",\"desc\":\"Credit Memo\"},\"documentNumber\":\"\"}");
        request.setParameterMap(parameters);
        
        FinancialResultsDownloadFileServlet financialsResultsDownloadFileServlet = aemContext
                .getService(FinancialResultsDownloadFileServlet.class);
        aemContext.registerInjectActivateService(financialsResultsDownloadFileServlet);
        financialsResultsDownloadFileServlet.doPost(request, response);
        assertEquals("status should be ok", HttpStatus.SC_OK, response.getStatus());
    }
    
    @Test
    public void doPostForExcel() throws IOException {
        MockRequestPathInfo requestPathInfo = (MockRequestPathInfo) aemContext.request().getRequestPathInfo();
        requestPathInfo.setExtension("excel");
        MockSlingHttpServletRequest request = aemContext.request();
        MockSlingHttpServletResponse response = aemContext.response();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(CustomerHubConstants.TOKEN, CustomerHubConstants.TEST_TOKEN);
        parameters.put("params",
                "{\"startDate\":\"2018-05-24\",\"soaDate\":\"2018-05-25\",\"endDate\":\"2018-05-25\",\"customerData\":{\"customerNumber\":\"0000010050\",\"customerName\":\"Arla Foods AB1\",\"info\":{\"accountNo\":\"0000010050\",\"name1\":\"Arla Foods AB1\",\"name2\":\"Leverantorsreskontran\",\"street\":\"Arla Foods Bolagskontor\",\"city\":\"STOCKHOLM\",\"state\":\"012\",\"postalcode\":\"105 46\",\"country\":\"SE\"},\"key\":\"0000010050\",\"desc\":\"0000010050 - Arla Foods AB1 - STOCKHOLM\"},\"status\":{\"key\":\"B\",\"desc\":\"Both\"},\"documentType\":{\"key\":\"CM\",\"desc\":\"Credit Memo\"},\"documentNumber\":\"\"}");
        request.setParameterMap(parameters);
        
        FinancialResultsDownloadFileServlet financialsResultsDownloadFileServlet = aemContext
                .getService(FinancialResultsDownloadFileServlet.class);
        aemContext.registerInjectActivateService(financialsResultsDownloadFileServlet);
        financialsResultsDownloadFileServlet.doPost(request, response);
        assertEquals("status should be ok", HttpStatus.SC_OK, response.getStatus());
    }
    
    public <T> List<GenericServiceType<T>> getMultipleMockedService() {
        
        GenericServiceType<FinancialResultsApiService> apigeeServiceGenericServiceType = new GenericServiceType<>();
        apigeeServiceGenericServiceType.setClazzType(FinancialResultsApiService.class);
        apigeeServiceGenericServiceType.set(new MockFinancialResultsApiServiceImpl());
        
        GenericServiceType<FinancialResultsPDFService> financialsResultsPDFServiceGenericServiceType = new GenericServiceType<>();
        financialsResultsPDFServiceGenericServiceType.setClazzType(FinancialResultsPDFService.class);
        financialsResultsPDFServiceGenericServiceType.set(new FinancialResultsPDFServiceImpl());
        
        GenericServiceType<FinancialResultsExcelService> excelServiceGenericServiceType = new GenericServiceType<>();
        excelServiceGenericServiceType.setClazzType(FinancialResultsExcelService.class);
        excelServiceGenericServiceType.set(new FinancialResultsExcelServiceImpl());
        
        GenericServiceType<FinancialResultsDownloadFileServlet> financialsResultsDownloadFileServletGenericServiceType = new GenericServiceType<>();
        financialsResultsDownloadFileServletGenericServiceType.setClazzType(FinancialResultsDownloadFileServlet.class);
        financialsResultsDownloadFileServletGenericServiceType.set(new FinancialResultsDownloadFileServlet());
        
        List<GenericServiceType<T>> serviceTypes = new ArrayList<>();
        serviceTypes.add((GenericServiceType<T>) apigeeServiceGenericServiceType);
        serviceTypes.add((GenericServiceType<T>) financialsResultsPDFServiceGenericServiceType);
        serviceTypes.add((GenericServiceType<T>) financialsResultsDownloadFileServletGenericServiceType);
        serviceTypes.add((GenericServiceType<T>) excelServiceGenericServiceType);
        return serviceTypes;
    }
    
}
