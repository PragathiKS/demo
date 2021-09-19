package com.tetrapak.customerhub.core.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.equipmentlist.Results;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.models.MyEquipmentModel;
import com.tetrapak.customerhub.core.services.EquipmentListApiService;
import com.tetrapak.customerhub.core.services.EquipmentListExcelService;
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

@Component(service = Servlet.class, property = {
        Constants.SERVICE_DESCRIPTION + "=Excel Generator Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_POST, "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.resourceTypes=" + "customerhub/components/content/myequipment",
        "sling.servlet.selectors=" + "download", "sling.servlet.extensions=" + CustomerHubConstants.EXCEL
})
public class EquipmentListDownloadFileServlet extends SlingAllMethodsServlet{

    @Reference
    private EquipmentListApiService equipmentListApiService;

    @Reference
    private EquipmentListExcelService excelService;

    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentListDownloadFileServlet.class);

    private static final String AUTH_TOKEN = "authToken";

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        doPost(request, response);
    }

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        final String extension = request.getRequestPathInfo().getExtension();
        //String params = request.getParameter("id");

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        //Equipments paramsRequest = gson.fromJson(params, Equipments.class);

        final String token = request.getCookie(AUTH_TOKEN) == null ? StringUtils.EMPTY
                : getAuthTokenValue(request);
        LOGGER.debug("Got authToken from cookie : {}", token);
        //JsonObject jsonResponse = equipmentListApiService.getEquipmentList(paramsRequest,token);
        JsonObject jsonResponse = equipmentListApiService.getEquipmentList(token);
        JsonElement statusResponse = jsonResponse.get(CustomerHubConstants.STATUS);

        boolean flag = false;
        MyEquipmentModel myEquipmentModel = request.getResource().adaptTo(MyEquipmentModel.class);

        if (null == myEquipmentModel){
            LOGGER.error("My Equipment Model is null!");
        } else if (!CustomerHubConstants.RESPONSE_STATUS_OK.equalsIgnoreCase(statusResponse.toString())) {
            LOGGER.error("Unable to retrieve response from API got status code:{}", statusResponse.toString());
        } else {
        	JsonElement resultsResponse = jsonResponse.get(CustomerHubConstants.RESULT);
        	Results results = gson.fromJson(HttpUtil.getStringFromJsonWithoutEscape(resultsResponse), Results.class);
            if (CustomerHubConstants.EXCEL.equalsIgnoreCase(extension)) {
                //flag = excelService.generateEquipmentListExcel(request,response,paramsRequest);
            	flag = excelService.generateEquipmentListExcel(request,response, results);
            } else {
                LOGGER.error("File type not specified for the download operation.");
            }
        }

        if (!flag) {
            LOGGER.error("Equipment results file download failed!");
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
