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

import java.util.Date;

import javax.servlet.Servlet;

@Component(service = Servlet.class, property = {
        Constants.SERVICE_DESCRIPTION + "=Excel Generator Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_POST, "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.resourceTypes=" + "customerhub/components/content/myequipment",
        "sling.servlet.selectors=" + "download", "sling.servlet.extensions=" + CustomerHubConstants.EXCEL
})
public class EquipmentListDownloadFileServlet extends SlingAllMethodsServlet{

	private static final long serialVersionUID = -8769537523421118922L;

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
        boolean flag = false;
        String countryCode = request.getParameter("countrycode");

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        final String token = request.getCookie(AUTH_TOKEN) == null ? StringUtils.EMPTY
                : getAuthTokenValue(request);
        LOGGER.debug("Got authToken from cookie : {}", token);

        LOGGER.debug("In EquipmentListDownloadFileServlet.doPost(), start time of equipment list api is {}", new Date());
        JsonObject jsonResponse = equipmentListApiService.getEquipmentList(token, countryCode);
        LOGGER.debug("In EquipmentListDownloadFileServlet.doPost(), end time of equipment list api is {}", new Date());
        JsonElement statusResponse = jsonResponse.get(CustomerHubConstants.STATUS);

        MyEquipmentModel myEquipmentModel = request.getResource().adaptTo(MyEquipmentModel.class);

        if (null == myEquipmentModel){
            LOGGER.error("My Equipment Model is null!");
        } else if (!CustomerHubConstants.RESPONSE_STATUS_OK.equalsIgnoreCase(statusResponse.toString())) {
            LOGGER.error("Unable to retrieve response from API got status code:{}", statusResponse);
        } else {
        	JsonElement resultsResponse = jsonResponse.get(CustomerHubConstants.RESULT);
        	Results results = gson.fromJson(HttpUtil.getStringFromJsonWithoutEscape(resultsResponse), Results.class);
            if (CustomerHubConstants.EXCEL.equalsIgnoreCase(extension)) {
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
