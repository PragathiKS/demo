package com.tetrapak.customerhub.core.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.financials.results.Params;
import com.tetrapak.customerhub.core.beans.financials.results.Results;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.models.FinancialStatementModel;
import com.tetrapak.customerhub.core.services.FinancialResultsApiService;
import com.tetrapak.customerhub.core.services.FinancialsResultsExcelService;
import com.tetrapak.customerhub.core.services.FinancialResultsPDFService;
import com.tetrapak.customerhub.core.utils.HttpUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;

/**
 * PDF and Excel Generator Servlet
 *
 * @author Nitin Kumar
 */
@Component(service = Servlet.class, property = {
        Constants.SERVICE_DESCRIPTION + "=PDF and Excel Generator Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_POST, "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.resourceTypes=" + "customerhub/components/content/financialstatement",
        "sling.servlet.selectors=" + "download", "sling.servlet.extensions=" + CustomerHubConstants.EXCEL,
        "sling.servlet.extensions=" + CustomerHubConstants.PDF
})
public class FinancialsResultsDownloadFileServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 2323660841296799482L;

    @Reference
    private FinancialResultsApiService financialsResultsApiService;

    @Reference
    private FinancialResultsPDFService generatePDF;

    @Reference
    private FinancialsResultsExcelService excelService;

    private static final Logger LOGGER = LoggerFactory.getLogger(FinancialsResultsDownloadFileServlet.class);

    private static final String AUTH_TOKEN = "authToken";

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        doPost(request, response);
    }

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        final String extension = request.getRequestPathInfo().getExtension();
        String params = request.getParameter("params");

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Params paramsRequest = gson.fromJson(params, Params.class);
        final String status = paramsRequest.getStatus().getKey();
        final String documentType = paramsRequest.getDocumentType().getKey();
        final String invoiceDateFrom = paramsRequest.getStartDate();
        final String invoiceDateTo = paramsRequest.getEndDate();
        final String soaDate = paramsRequest.getSoaDate();
        final String customerNumber = paramsRequest.getCustomerData().getCustomerNumber();
        final String token = request.getCookie(AUTH_TOKEN) == null ? StringUtils.EMPTY
                : getAuthTokenValue(request);
        LOGGER.debug("Got authToken from cookie : {}", token);
        JsonObject jsonResponse = financialsResultsApiService.getFinancialResults(status, documentType,
                invoiceDateFrom, invoiceDateTo, soaDate, customerNumber, token);

        JsonElement statusResponse = jsonResponse.get(CustomerHubConstants.STATUS);

        boolean flag = false;
        FinancialStatementModel financialStatementModel = request.getResource().adaptTo(FinancialStatementModel.class);

        if (null == financialStatementModel) {
            LOGGER.error("FinancialStatementModel is null!");
        } else if (!CustomerHubConstants.RESPONSE_STATUS_OK.equalsIgnoreCase(statusResponse.toString())) {
            LOGGER.error("Unable to retrieve response from API got status code:{}", status);
        } else {
            JsonElement resultsResponse = jsonResponse.get(CustomerHubConstants.RESULT);
            Results results = gson.fromJson(HttpUtil.getStringFromJsonWithoutEscape(resultsResponse), Results.class);
            if (CustomerHubConstants.PDF.equals(extension)) {
                flag = generatePDF.generateFinancialResultsPDF(request, response, results, paramsRequest,
                        financialStatementModel);
            } else if (CustomerHubConstants.EXCEL.equalsIgnoreCase(extension)) {
                flag = excelService.generateFinancialResultsExcel(request, response, results, paramsRequest);
            } else {
                LOGGER.error("File type not specified for the download operation.");
            }
        }

        if (!flag) {
            LOGGER.error("Financial results file download failed!");
            HttpUtil.sendErrorMessage(response);
        }
    }

    private String getAuthTokenValue(SlingHttpServletRequest request) {
        if (null == request.getCookie(AUTH_TOKEN)) {
            return StringUtils.EMPTY;
        }
        return request.getCookie(AUTH_TOKEN).getValue();
    }

}
