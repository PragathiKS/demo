package com.tetrapak.customerhub.core.servlets;

import com.day.cq.wcm.api.Page;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.GenericServiceType;
import com.tetrapak.customerhub.core.mock.MockFinancialResultsApiServiceImpl;
import com.tetrapak.customerhub.core.mock.MockUrlServiceImpl;
import com.tetrapak.customerhub.core.services.FinancialResultsApiService;
import com.tetrapak.customerhub.core.services.FinancialResultsExcelService;
import com.tetrapak.customerhub.core.services.FinancialResultsPDFService;
import com.tetrapak.customerhub.core.services.UrlService;
import com.tetrapak.customerhub.core.services.impl.FinancialResultsExcelServiceImpl;
import com.tetrapak.customerhub.core.services.impl.FinancialResultsPDFServiceImpl;
import io.wcm.testing.mock.aem.junit.AemContext;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Test class for Financials Results Service
 *
 * @author ruhsharm
 */
@RunWith(MockitoJUnitRunner.class)
public class FinancialResultsDownloadFileServletTest {

    @Mock
    private Page mockPage;

    @Mock
    FinancialResultsPDFService financialResultsPDFService = new FinancialResultsPDFServiceImpl();

    @Mock
    FinancialResultsExcelService financialResultsExcelService = new FinancialResultsExcelServiceImpl();

    @Mock
    FinancialResultsApiService financialsResultsApiService = new MockFinancialResultsApiServiceImpl();

    @InjectMocks
    FinancialResultsDownloadFileServlet financialResultsDownloadFileServlet;

    @Mock
    private ResourceBundleProvider mockResourceBundleProvider;

    private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/financials";
    private static final String COMPONENT_PATH = "/content/tetrapak/customerhub/global/en/financials/jcr:content/root/responsivegrid/financialstatement";
    private static final String SERVLET_RESOURCE_JSON = "allContent.json";
    private static final String RESOURCE_JSON = "financialsresultspage.json";
    private static final String I18_RESOURCE = "/apps/customerhub/i18n/en";
    private static final String I18_RESOURCE_JSON = "/financialsresultsI18n.json";
    private static final String PARAM_STRING = "{\\n  \\\"summary\\\": [\\n    {\\n      \\\"currency\\\": \\\"USD\\\",\\n      \\\"current\\\": \\\"$0.00\\\",\\n      \\\"overdue\\\": \\\"-$222\\\",\\n      \\\"thirty\\\": \\\"$0.00\\\",\\n      \\\"sixty\\\": \\\"$0.00\\\",\\n      \\\"ninty\\\": \\\"$0.00\\\",\\n      \\\"nintyPlus\\\": \\\"$0.00\\\",\\n      \\\"total\\\": \\\"$33333\\\"\\n    }\\n  ],\\n  \\\"documents\\\": [\\n    {\\n      \\\"salesOffice\\\": \\\"Packaging\\\",\\n      \\\"totalAmount\\\": \\\"$12345\\\",\\n      \\\"records\\\": [\\n        {\\n          \\\"documentNumber\\\": \\\"123\\\",\\n          \\\"desc\\\": \\\"Invoice\\\",\\n          \\\"invoiceReference\\\": \\\"http://tetrapak.com\\\",\\n          \\\"poNumber\\\": \\\"4444-33\\\",\\n          \\\"docDate\\\": \\\"yyy-mm-dd\\\",\\n          \\\"dueDate\\\": \\\"yyyy-mm-dd\\\",\\n          \\\"currency\\\": \\\"USD\\\",\\n          \\\"orgAmount\\\": \\\"$3333\\\",\\n          \\\"salesOffice\\\": \\\"Packaging\\\",\\n          \\\"salesLocalData\\\": \\\"alphanumeric\\\"\\n        },\\n        {\\n          \\\"documentNumber\\\": \\\"234\\\",\\n          \\\"desc\\\": \\\"Invoice\\\",\\n          \\\"invoiceReference\\\": \\\"http://tetrapak.com\\\",\\n          \\\"poNumber\\\": \\\"4444-33\\\",\\n          \\\"docDate\\\": \\\"yyy-mm-dd\\\",\\n          \\\"dueDate\\\": \\\"yyyy-mm-dd\\\",\\n          \\\"currency\\\": \\\"USD\\\",\\n          \\\"orgAmount\\\": \\\"$3333\\\",\\n          \\\"salesOffice\\\": \\\"Packaging\\\",\\n          \\\"salesLocalData\\\": \\\"alphanumeric\\\"\\n        }\\n      ]\\n    },\\n    {\\n      \\\"salesOffice\\\": \\\"others\\\",\\n      \\\"totalAmount\\\": \\\"$12345\\\",\\n      \\\"records\\\": [\\n        {\\n          \\\"documentNumber\\\": \\\"555\\\",\\n          \\\"desc\\\": \\\"Invoice\\\",\\n          \\\"invoiceReference\\\": \\\"http://tetrapak.com\\\",\\n          \\\"poNumber\\\": \\\"4444-33\\\",\\n          \\\"docDate\\\": \\\"yyy-mm-dd\\\",\\n          \\\"dueDate\\\": \\\"yyyy-mm-dd\\\",\\n          \\\"currency\\\": \\\"USD\\\",\\n          \\\"orgAmount\\\": \\\"$3333\\\",\\n          \\\"salesOffice\\\": \\\"others\\\",\\n          \\\"salesLocalData\\\": \\\"alphanumeric\\\"\\n        },\\n        {\\n          \\\"documentNumber\\\": \\\"777\\\",\\n          \\\"desc\\\": \\\"Invoice\\\",\\n          \\\"invoiceReference\\\": \\\"http://tetrapak.com\\\",\\n          \\\"poNumber\\\": \\\"4444-33\\\",\\n          \\\"docDate\\\": \\\"yyy-mm-dd\\\",\\n          \\\"dueDate\\\": \\\"yyyy-mm-dd\\\",\\n          \\\"currency\\\": \\\"USD\\\",\\n          \\\"orgAmount\\\": \\\"$3333\\\",\\n          \\\"salesOffice\\\": \\\"others\\\",\\n          \\\"salesLocalData\\\": \\\"alphanumeric\\\"\\n        }\\n      ]\\n    },\\n    {\\n      \\\"salesOffice\\\": \\\"Office 1\\\",\\n      \\\"totalAmount\\\": \\\"Multiple Currency\\\",\\n      \\\"records\\\": [\\n        {\\n          \\\"documentNumber\\\": \\\"876\\\",\\n          \\\"desc\\\": \\\"Invoice\\\",\\n          \\\"invoiceReference\\\": \\\"http://tetrapak.com\\\",\\n          \\\"poNumber\\\": \\\"4444-33\\\",\\n          \\\"docDate\\\": \\\"yyy-mm-dd\\\",\\n          \\\"dueDate\\\": \\\"yyyy-mm-dd\\\",\\n          \\\"currency\\\": \\\"USD\\\",\\n          \\\"orgAmount\\\": \\\"$3333\\\",\\n          \\\"salesOffice\\\": \\\"others\\\",\\n          \\\"salesLocalData\\\": \\\"alphanumeric\\\"\\n        },\\n        {\\n          \\\"documentNumber\\\": \\\"543\\\",\\n          \\\"desc\\\": \\\"Invoice\\\",\\n          \\\"invoiceReference\\\": \\\"http://tetrapak.com\\\",\\n          \\\"poNumber\\\": \\\"4444-33\\\",\\n          \\\"docDate\\\": \\\"yyy-mm-dd\\\",\\n          \\\"dueDate\\\": \\\"yyyy-mm-dd\\\",\\n          \\\"currency\\\": \\\"SEK\\\",\\n          \\\"orgAmount\\\": \\\"$3333\\\",\\n          \\\"salesOffice\\\": \\\"others\\\",\\n          \\\"salesLocalData\\\": \\\"alphanumeric\\\"\\n        }\\n      ]\\n    },\\n    {\\n      \\\"salesOffice\\\": \\\"Office 3\\\",\\n      \\\"totalAmount\\\": \\\"$12345\\\",\\n      \\\"records\\\": [\\n        {\\n          \\\"documentNumber\\\": \\\"111\\\",\\n          \\\"desc\\\": \\\"Invoice\\\",\\n          \\\"invoiceReference\\\": \\\"http://tetrapak.com\\\",\\n          \\\"poNumber\\\": \\\"4444-33\\\",\\n          \\\"docDate\\\": \\\"yyy-mm-dd\\\",\\n          \\\"dueDate\\\": \\\"yyyy-mm-dd\\\",\\n          \\\"currency\\\": \\\"USD\\\",\\n          \\\"orgAmount\\\": \\\"$3333\\\",\\n          \\\"salesOffice\\\": \\\"others\\\",\\n          \\\"salesLocalData\\\": \\\"alphanumeric\\\"\\n        },\\n        {\\n          \\\"documentNumber\\\": \\\"234\\\",\\n          \\\"desc\\\": \\\"Invoice\\\",\\n          \\\"invoiceReference\\\": \\\"http://tetrapak.com\\\",\\n          \\\"poNumber\\\": \\\"4444-33\\\",\\n          \\\"docDate\\\": \\\"yyy-mm-dd\\\",\\n          \\\"dueDate\\\": \\\"yyyy-mm-dd\\\",\\n          \\\"currency\\\": \\\"USD\\\",\\n          \\\"orgAmount\\\": \\\"$3333\\\",\\n          \\\"salesOffice\\\": \\\"others\\\",\\n          \\\"salesLocalData\\\": \\\"alphanumeric\\\"\\n        }\\n      ]\\n    },\\n    {\\n      \\\"salesOffice\\\": \\\"Office 4\\\",\\n      \\\"totalAmount\\\": \\\"$12345\\\",\\n      \\\"records\\\": [\\n        {\\n          \\\"documentNumber\\\": \\\"874\\\",\\n          \\\"desc\\\": \\\"Invoice\\\",\\n          \\\"invoiceReference\\\": \\\"http://tetrapak.com\\\",\\n          \\\"poNumber\\\": \\\"4444-33\\\",\\n          \\\"docDate\\\": \\\"yyy-mm-dd\\\",\\n          \\\"dueDate\\\": \\\"yyyy-mm-dd\\\",\\n          \\\"currency\\\": \\\"USD\\\",\\n          \\\"orgAmount\\\": \\\"$3333\\\",\\n          \\\"salesOffice\\\": \\\"others\\\",\\n          \\\"salesLocalData\\\": \\\"alphanumeric\\\"\\n        },\\n        {\\n          \\\"documentNumber\\\": \\\"263\\\",\\n          \\\"desc\\\": \\\"Invoice\\\",\\n          \\\"invoiceReference\\\": \\\"http://tetrapak.com\\\",\\n          \\\"poNumber\\\": \\\"4444-33\\\",\\n          \\\"docDate\\\": \\\"yyy-mm-dd\\\",\\n          \\\"dueDate\\\": \\\"yyyy-mm-dd\\\",\\n          \\\"currency\\\": \\\"USD\\\",\\n          \\\"orgAmount\\\": \\\"$3333\\\",\\n          \\\"salesOffice\\\": \\\"others\\\",\\n          \\\"salesLocalData\\\": \\\"alphanumeric\\\"\\n        }\\n      ]\\n    }\\n  ]\\n}";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContextWithJcrMock(RESOURCE_JSON, CONTENT_ROOT);

    @Before
    public void setup() throws IOException {
        ResourceBundle resourceBundle = new PropertyResourceBundle(
                this.getClass().getResourceAsStream("/i18n.properties"));
        aemContext.registerService(ResourceBundleProvider.class, mockResourceBundleProvider);
        when(mockResourceBundleProvider.getResourceBundle(any(), any())).thenReturn(resourceBundle);
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty(CustomerHubConstants.RESULT, PARAM_STRING);
        jsonResponse.addProperty("status", 200);
        when(financialsResultsApiService.getFinancialResults(any(), any())).thenReturn(jsonResponse);
        when(financialResultsExcelService.generateFinancialResultsExcel(any(),any(),any(),any())).thenReturn(true);
        when(financialResultsPDFService.generateFinancialResultsPDF(any(),any(),any(),any(),any())).thenReturn(true);
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

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(CustomerHubConstants.TOKEN, CustomerHubConstants.TEST_TOKEN);
        parameters.put("params", "{\n" +
                "   \"startDate\":\"2019-06-11\",\n" +
                "   \"soaDate\":\"2019-07-26\",\n" +
                "   \"endDate\":\"2019-07-26\",\n" +
                "   \"customerData\":{\n" +
                "      \"customerNumber\":\"0000321004\",\n" +
                "      \"customerName\":\"Danone Tikvesli Gida ve Icecek\",\n" +
                "      \"info\":{\n" +
                "         \"accountNo\":\"0000321004\",\n" +
                "         \"name1\":\"Danone Tikvesli Gida ve Icecek\",\n" +
                "         \"name2\":\"San. ve Tic. A.S.\",\n" +
                "         \"street\":\"Kucukbakkalkoy Mah.No:3 B Blok\",\n" +
                "         \"city\":\"Istanbul\",\n" +
                "         \"state\":\"\",\n" +
                "         \"postalcode\":\"34805\",\n" +
                "         \"country\":\"TR\"\n" +
                "      },\n" +
                "      \"key\":\"0000321004\",\n" +
                "      \"desc\":\"0000321004 - Danone Tikvesli Gida ve Icecek - Istanbul\"\n" +
                "   },\n" +
                "   \"status\":{\n" +
                "      \"key\":\"B\",\n" +
                "      \"desc\":\"\"\n" +
                "   },\n" +
                "   \"documentType\":{\n" +
                "      \"key\":\"INV\",\n" +
                "      \"desc\":\"Invoice\"\n" +
                "   },\n" +
                "   \"documentNumber\":\"\",\n" +
                "   \"statusList\":[\n" +
                "      {\n" +
                "         \"key\":\"C\",\n" +
                "         \"desc\":\"Cleared\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"key\":\"O\",\n" +
                "         \"desc\":\"Open\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"key\":\"B\",\n" +
                "         \"desc\":\"Both\"\n" +
                "      }\n" +
                "   ],\n" +
                "   \"documentTypeList\":[\n" +
                "      {\n" +
                "         \"key\":\"INV\",\n" +
                "         \"desc\":\"Invoice\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"key\":\"CM\",\n" +
                "         \"desc\":\"Credit Memo\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"key\":\"PMT\",\n" +
                "         \"desc\":\"Payment\"\n" +
                "      }\n" +
                "   ]\n" +
                "}\n");
        request.setParameterMap(parameters);

        financialResultsDownloadFileServlet.doPost(request, response);
        assertEquals("status should be ok", HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    public void doPostForExcel() throws IOException {
        MockRequestPathInfo requestPathInfo = (MockRequestPathInfo) aemContext.request().getRequestPathInfo();
        requestPathInfo.setExtension("excel");
        MockSlingHttpServletRequest request = aemContext.request();
        MockSlingHttpServletResponse response = aemContext.response();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(CustomerHubConstants.TOKEN, CustomerHubConstants.TEST_TOKEN);
        parameters.put("params", "{\n" +
                "   \"startDate\":\"2019-06-11\",\n" +
                "   \"soaDate\":\"2019-07-26\",\n" +
                "   \"endDate\":\"2019-07-26\",\n" +
                "   \"customerData\":{\n" +
                "      \"customerNumber\":\"0000321004\",\n" +
                "      \"customerName\":\"Danone Tikvesli Gida ve Icecek\",\n" +
                "      \"info\":{\n" +
                "         \"accountNo\":\"0000321004\",\n" +
                "         \"name1\":\"Danone Tikvesli Gida ve Icecek\",\n" +
                "         \"name2\":\"San. ve Tic. A.S.\",\n" +
                "         \"street\":\"Kucukbakkalkoy Mah.No:3 B Blok\",\n" +
                "         \"city\":\"Istanbul\",\n" +
                "         \"state\":\"\",\n" +
                "         \"postalcode\":\"34805\",\n" +
                "         \"country\":\"TR\"\n" +
                "      },\n" +
                "      \"key\":\"0000321004\",\n" +
                "      \"desc\":\"0000321004 - Danone Tikvesli Gida ve Icecek - Istanbul\"\n" +
                "   },\n" +
                "   \"status\":{\n" +
                "      \"key\":\"B\",\n" +
                "      \"desc\":\"\"\n" +
                "   },\n" +
                "   \"documentType\":{\n" +
                "      \"key\":\"INV\",\n" +
                "      \"desc\":\"Invoice\"\n" +
                "   },\n" +
                "   \"documentNumber\":\"\",\n" +
                "   \"statusList\":[\n" +
                "      {\n" +
                "         \"key\":\"C\",\n" +
                "         \"desc\":\"Cleared\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"key\":\"O\",\n" +
                "         \"desc\":\"Open\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"key\":\"B\",\n" +
                "         \"desc\":\"Both\"\n" +
                "      }\n" +
                "   ],\n" +
                "   \"documentTypeList\":[\n" +
                "      {\n" +
                "         \"key\":\"INV\",\n" +
                "         \"desc\":\"Invoice\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"key\":\"CM\",\n" +
                "         \"desc\":\"Credit Memo\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"key\":\"PMT\",\n" +
                "         \"desc\":\"Payment\"\n" +
                "      }\n" +
                "   ]\n" +
                "}\n");
        request.setParameterMap(parameters);

        financialResultsDownloadFileServlet.doPost(request, response);
        assertEquals("status should be ok", HttpStatus.SC_OK, response.getStatus());
    }
}
