package com.tetrapak.customerhub.core.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.financials.results.Params;
import com.tetrapak.customerhub.core.beans.financials.results.RequestParams;
import com.tetrapak.customerhub.core.beans.financials.results.Results;
import com.tetrapak.customerhub.core.beans.oderdetails.OrderDetailsData;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.FinancialsResultsApiService;
import com.tetrapak.customerhub.core.services.FinancialsResultsPDFService;
import com.tetrapak.customerhub.core.services.OrderDetailsExcelService;
import com.tetrapak.customerhub.core.utils.HttpUtil;

import org.apache.http.client.methods.HttpPost;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.tika.io.IOUtils;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonReader;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

/**
 * PDF and Excel Generator Servlet
 * @author ruhsharm
 */
@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=PDF and Excel Generator Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.resourceTypes=" + "customerhub/components/content/financialstatement",
                "sling.servlet.extension=[" + CustomerHubConstants.PDF + "," + CustomerHubConstants.EXCEL + "]",
                "sling.servlet.paths=" + "/bin/customerhub/financialsresults"
        })
public class FinancialsResultsDownloadFileServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 2323660841296799482L;

    @Reference
    private FinancialsResultsApiService financialsResultsApiService;

    @Reference
    private FinancialsResultsPDFService generatePDF;


    private static final Logger LOGGER = LoggerFactory.getLogger(FinancialsResultsDownloadFileServlet.class);
    
    @Override
    protected void doGet(SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws ServletException,
            IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response ) throws IOException {
        final String extension = request.getRequestPathInfo().getExtension();   
        String params = request.getParameter("params"); 
        
        GsonBuilder builder = new GsonBuilder();
         Gson gson = builder.create();
     
        Params paramsRequest= gson.fromJson(params,Params.class);
        final String status = paramsRequest.getStatus().getKey();
        final String documentType = paramsRequest.getDocumentType().getKey();
        final String invoiceDateFrom = paramsRequest.getEndDate();
        final String customerkey = paramsRequest.getCustomerData().getKey();
        final String token = request.getParameter("token");  
  
        JsonObject jsonResponse = financialsResultsApiService.getFinancialsResults(
                status, documentType,invoiceDateFrom, customerkey, token);
               
        JsonElement statusResponse = jsonResponse.get(CustomerHubConstants.STATUS);
        
        if (!CustomerHubConstants.RESPONSE_STATUS_OK.equalsIgnoreCase(statusResponse.toString())) {
            response.setStatus(Integer.parseInt(status.toString()));
            try {
                HttpUtil.writeJsonResponse(response, jsonResponse);
            } catch (IOException e) {
                LOGGER.error("IOException in FinancialsResultsDownloadFileServlet {}", e);
            }
            LOGGER.error("Unable to retrieve response from API");
        } else {
            JsonElement resultsResponse = jsonResponse.get(CustomerHubConstants.RESULT);
            Results results = gson.fromJson(HttpUtil.getStringFromJsonWithoutEscape(resultsResponse),
                    Results.class);
        boolean flag = false;  
        if (CustomerHubConstants.PDF.equals(extension)) {
            generatePDF.generateFinancialsResultsPDF(response, results, paramsRequest);
        }
        /* else if (CustomerHubConstants.EXCEL.equals(extension)) {
            
        }*/else {
            LOGGER.error("File type not specified for the download operation.");
        }
        if (!flag) {
            sendErrorMessage(response);
        }
    }
  }
    private void sendErrorMessage(SlingHttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        try {
            JsonObject obj = new JsonObject();
            obj.addProperty("errorMsg", "Some internal server error occurred while processing the request!");
            HttpUtil.writeJsonResponse(response, obj);
        } catch (IOException e) {
            LOGGER.error("IOException in FinancialsResultsDownloadFileServlet {}", e);
        }
    }
}
