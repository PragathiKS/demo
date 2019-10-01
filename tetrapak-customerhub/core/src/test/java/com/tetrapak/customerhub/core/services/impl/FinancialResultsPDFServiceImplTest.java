/**
 *
 */
package com.tetrapak.customerhub.core.services.impl;

import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.tetrapak.customerhub.core.beans.financials.results.CustomerData;
import com.tetrapak.customerhub.core.beans.financials.results.Document;
import com.tetrapak.customerhub.core.beans.financials.results.DocumentType;
import com.tetrapak.customerhub.core.beans.financials.results.Info;
import com.tetrapak.customerhub.core.beans.financials.results.Params;
import com.tetrapak.customerhub.core.beans.financials.results.Record;
import com.tetrapak.customerhub.core.beans.financials.results.Results;
import com.tetrapak.customerhub.core.beans.financials.results.Status;
import com.tetrapak.customerhub.core.beans.financials.results.Summary;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.models.FinancialStatementModel;
import com.tetrapak.customerhub.core.services.UrlService;
import com.tetrapak.customerhub.core.utils.FontUtil;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import com.tetrapak.customerhub.core.utils.PDFUtil2;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.servlet.ServletOutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;

/**
 * @author Nitin Kumar
 *
 */
@Ignore
@RunWith(PowerMockRunner.class)
@PrepareForTest(value = {FontUtil.class, PDFUtil2.class, GlobalUtil.class, BaseFont.class})
public class FinancialResultsPDFServiceImplTest {

    @Mock
    private SlingHttpServletRequest servletRequest;

    @Mock
    private SlingHttpServletResponse response;

    @InjectMocks
    private FinancialResultsPDFServiceImpl finService = new FinancialResultsPDFServiceImpl();

    @Mock
    private UrlService urlService;

    @Mock
    ServletOutputStream servletOutputStream;

    @Mock
    BaseFont mockFont;

    private FinancialStatementModel financialStatementModel = null;
    private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/global/en";
    private static final String COMPONENT_PATH = "/content/tetrapak/customerhub/global/en/financials/jcr:content/root/responsivegrid/financialstatement";
    private static final String RESOURCE_JSON = "allContent.json";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON, CONTENT_ROOT);

    @Before
    public void setUp() throws Exception {
        Resource resource = aemContext.currentResource(COMPONENT_PATH);
        financialStatementModel = resource.adaptTo(FinancialStatementModel.class);
        PowerMockito.mockStatic(FontUtil.class);
        PowerMockito.mockStatic(PDFUtil2.class);
        PowerMockito.mockStatic(GlobalUtil.class);
        PowerMockito.mockStatic(BaseFont.class);
        PowerMockito.when(FontUtil.getEnFont(Mockito.anyString())).thenReturn(new Font(mockFont));
        PowerMockito.when(FontUtil.getEnFontBold(Mockito.anyString())).thenReturn(new Font(mockFont));
        PowerMockito.when(GlobalUtil.getLanguage(Mockito.any())).thenReturn("en");
        PowerMockito.when(GlobalUtil.getI18nValueForThisLanguage(Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn("abc");
        Mockito.when(response.getOutputStream()).thenReturn(servletOutputStream);
        Mockito.doNothing().when(servletOutputStream).write(Mockito.any(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void testGenerateFinancialResultsPDF() {
        Results apiResponse = new Results();
        Params paramRequest = new Params();
        CustomerData customerData = new CustomerData();
        customerData.setCustomerName("John Smith");
        customerData.setCustomerNumber("key");
        Info info = new Info();
        info.setAccountNo("12323");
        info.setName1("n1");
        info.setName2("n2");
        info.setCity("c1");
        info.setCountry("c2");
        info.setPostalcode("12323");
        info.setState("sss");
        info.setStreet("22ddd");
        customerData.setInfo(info);
        paramRequest.setCustomerData(customerData);
        paramRequest.setStartDate("startDate");
        List<Document> documents = new ArrayList<>();
        Document salesDoc1 = new Document();
        salesDoc1.setSalesOffice("salesOffice");
        salesDoc1.setTotalAmount("totalAmount");
        List<Record> records = new ArrayList<Record>();
        Record record = new Record();
        record.setCurrency("currency");
        record.setDocumentType("desc");
        record.setSalesOffice("salesOffice");
        record.setDueDate("dueDate");
        record.setOrgAmount("orgAmount");
        record.setPoNumber("poNumber");
        records.add(record);
        salesDoc1.setRecords(records);
        documents.add(salesDoc1);
        apiResponse.setDocuments(documents);
        List<Summary> summaryList = new ArrayList<>();
        Summary summary = new Summary();
        summary.setCurrency("currency");
        summary.setTotal("total");
        summaryList.add(summary);
        apiResponse.setSummary(summaryList);
        List<DocumentType> statusList = new ArrayList<>();
        DocumentType docType = new DocumentType();
        docType.setKey("key");
        docType.setDesc("desc");
        statusList.add(docType);
        List<DocumentType> documentTypeList = new ArrayList<>();
        documentTypeList.add(docType);
        paramRequest.setStatusList(statusList);
        paramRequest.setDocumentTypeList(documentTypeList);
        Status status = new Status();
        status.setKey("key");
        status.setDesc("desc");
        paramRequest.setStatus(status);
        paramRequest.setDocumentNumber("123213");
        DocumentType documentType = new DocumentType();
        documentType.setKey("key");
        documentType.setDesc("desc");
        paramRequest.setDocumentType(documentType);
        finService.generateFinancialResultsPDF(servletRequest, response, apiResponse, paramRequest, financialStatementModel);
    }
}
