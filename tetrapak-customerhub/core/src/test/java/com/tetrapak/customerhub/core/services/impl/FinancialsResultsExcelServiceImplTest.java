/**
 *
 */
package com.tetrapak.customerhub.core.services.impl;

import com.tetrapak.customerhub.core.beans.financials.results.CustomerData;
import com.tetrapak.customerhub.core.beans.financials.results.Document;
import com.tetrapak.customerhub.core.beans.financials.results.Params;
import com.tetrapak.customerhub.core.beans.financials.results.Record;
import com.tetrapak.customerhub.core.beans.financials.results.Results;
import com.tetrapak.customerhub.core.beans.financials.results.Summary;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.ServletOutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author swalamba
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class FinancialsResultsExcelServiceImplTest {

    @Mock
    private SlingHttpServletRequest servletRequest;

    @Mock
    private SlingHttpServletResponse response;

    FinancialsResultsExcelServiceImpl finService = new FinancialsResultsExcelServiceImpl();

    @Mock
    ServletOutputStream servletOutputStream;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

        Mockito.when(response.getOutputStream()).thenReturn(servletOutputStream);
        Mockito.doNothing().when(servletOutputStream).write(Mockito.any(), Mockito.anyInt(), Mockito.anyInt());
    }

    /**
     * Test method for
     * {@link com.tetrapak.customerhub.core.services.impl.FinancialsResultsExcelServiceImpl#generateFinancialsResultsExcel(org.apache.sling.api.SlingHttpServletRequest, org.apache.sling.api.SlingHttpServletResponse, com.tetrapak.customerhub.core.beans.financials.results.Results, com.tetrapak.customerhub.core.beans.financials.results.Params)}.
     */
    @Test
    public void testGenerateFinancialsResultsExcelWithNullApiResp() {
        Results apiResponse = null;
        Params paramRequest = new Params();
        assertFalse(finService.generateFinancialsResultsExcel(servletRequest, response, apiResponse, paramRequest));
    }

    /**
     * Test method for
     * {@link com.tetrapak.customerhub.core.services.impl.FinancialsResultsExcelServiceImpl#generateFinancialsResultsExcel(org.apache.sling.api.SlingHttpServletRequest, org.apache.sling.api.SlingHttpServletResponse, com.tetrapak.customerhub.core.beans.financials.results.Results, com.tetrapak.customerhub.core.beans.financials.results.Params)}.
     */
    @Test
    public void testGenerateFinancialsResultsExcelWithNullRequestParam() {
        Results apiResponse = new Results();
        Params paramRequest = null;
        assertFalse(finService.generateFinancialsResultsExcel(servletRequest, response, apiResponse, paramRequest));
    }

    /**
     * Test method for
     * {@link com.tetrapak.customerhub.core.services.impl.FinancialsResultsExcelServiceImpl#generateFinancialsResultsExcel(org.apache.sling.api.SlingHttpServletRequest, org.apache.sling.api.SlingHttpServletResponse, com.tetrapak.customerhub.core.beans.financials.results.Results, com.tetrapak.customerhub.core.beans.financials.results.Params)}.
     */
    @Test
    public void testGenerateFinancialsResultsExcelWithNoCustDataAndDocs() {
        Results apiResponse = new Results();
        Params paramRequest = new Params();
        paramRequest.setStartDate("startDate");
        paramRequest.setEndDate("endDate");
        assertTrue(finService.generateFinancialsResultsExcel(servletRequest, response, apiResponse, paramRequest));
    }

    /**
     * Test method for
     * {@link com.tetrapak.customerhub.core.services.impl.FinancialsResultsExcelServiceImpl#generateFinancialsResultsExcel(org.apache.sling.api.SlingHttpServletRequest, org.apache.sling.api.SlingHttpServletResponse, com.tetrapak.customerhub.core.beans.financials.results.Results, com.tetrapak.customerhub.core.beans.financials.results.Params)}.
     */
    @Test
    public void testGenerateFinancialsResultsExcelWithEndDateNull() {
        Results apiResponse = new Results();
        Params paramRequest = new Params();
        CustomerData customerData = new CustomerData();
        customerData.setDesc("John Smith");
        customerData.setKey("key");
        paramRequest.setCustomerData(customerData);
        paramRequest.setStartDate("startDate");
        assertTrue(finService.generateFinancialsResultsExcel(servletRequest, response, apiResponse, paramRequest));
    }

    /**
     * Test method for
     * {@link com.tetrapak.customerhub.core.services.impl.FinancialsResultsExcelServiceImpl#generateFinancialsResultsExcel(org.apache.sling.api.SlingHttpServletRequest, org.apache.sling.api.SlingHttpServletResponse, com.tetrapak.customerhub.core.beans.financials.results.Results, com.tetrapak.customerhub.core.beans.financials.results.Params)}.
     */
    @Test
    public void testGenerateFinancialsResultsExcel() {
        Results apiResponse = new Results();
        Params paramRequest = new Params();
        CustomerData customerData = new CustomerData();
        customerData.setDesc("John Smith");
        customerData.setKey("key");
        paramRequest.setCustomerData(customerData);
        paramRequest.setStartDate("startDate");
        List<Document> documents = new ArrayList<>();
        Document salesDoc1 = new Document();
        salesDoc1.setSalesOffice("salesOffice");
        salesDoc1.setTotalAmount("totalAmount");
        List<Record> records = new ArrayList<Record>();
        Record record = new Record();
        record.setCurrency("currency");
        record.setDesc("desc");
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
        assertTrue(finService.generateFinancialsResultsExcel(servletRequest, response, apiResponse, paramRequest));
    }

    /**
     * Test method for
     * {@link com.tetrapak.customerhub.core.services.impl.FinancialsResultsExcelServiceImpl#generateFinancialsResultsExcel(org.apache.sling.api.SlingHttpServletRequest, org.apache.sling.api.SlingHttpServletResponse, com.tetrapak.customerhub.core.beans.financials.results.Results, com.tetrapak.customerhub.core.beans.financials.results.Params)}.
     */
    @Test
    public void testGenerateFinancialsResultsExcelMultipleObjInSummary() {
        Results apiResponse = new Results();
        Params paramRequest = new Params();
        CustomerData customerData = new CustomerData();
        customerData.setDesc("John Smith");
        customerData.setKey("key");
        paramRequest.setCustomerData(customerData);
        paramRequest.setStartDate("startDate");
        List<Document> documents = new ArrayList<>();
        Document salesDoc1 = new Document();
        salesDoc1.setSalesOffice("salesOffice");
        salesDoc1.setTotalAmount("totalAmount");
        List<Record> records = new ArrayList<Record>();
        Record record = new Record();
        record.setCurrency("currency");
        record.setDesc("desc");
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
        summary = new Summary();
        summary.setCurrency("currency2");
        summary.setTotal("total2");
        summaryList.add(summary);
        apiResponse.setSummary(summaryList);
        assertTrue(finService.generateFinancialsResultsExcel(servletRequest, response, apiResponse, paramRequest));
    }

}
