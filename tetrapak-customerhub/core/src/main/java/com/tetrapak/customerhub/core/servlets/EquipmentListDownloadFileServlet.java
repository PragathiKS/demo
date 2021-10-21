package com.tetrapak.customerhub.core.servlets;

import java.util.Date;
import java.util.List;

import javax.servlet.Servlet;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tetrapak.customerhub.core.beans.equipmentlist.Equipments;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.models.MyEquipmentModel;
import com.tetrapak.customerhub.core.services.EquipmentListApiService;
import com.tetrapak.customerhub.core.services.EquipmentListExcelService;
import com.tetrapak.customerhub.core.utils.HttpUtil;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Excel Generator Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_POST, "sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.resourceTypes=" + "customerhub/components/content/myequipment",
		"sling.servlet.selectors=" + "download", "sling.servlet.extensions=" + CustomerHubConstants.EXCEL })
public class EquipmentListDownloadFileServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = -8769537523421118922L;

	@Reference
	private EquipmentListApiService equipmentListApiService;

	@Reference
	private EquipmentListExcelService excelService;

	private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentListDownloadFileServlet.class);

	private static final String AUTH_TOKEN = "authToken";

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		try {
			boolean flag = false;
			String countryCode = request.getParameter("countrycode");

			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();

			final String token = request.getCookie(AUTH_TOKEN) == null ? StringUtils.EMPTY : getAuthTokenValue(request);
			LOGGER.debug("Got authToken from cookie : {}", token);

			LOGGER.debug("In EquipmentListDownloadFileServlet.doPost(), start time of equipment list api is {}",
					new Date());
			StopWatch watch = new StopWatch();
			watch.start();
			List<Equipments> results = equipmentListApiService.getEquipmentList(token, countryCode);
			watch.stop();
			LOGGER.debug(
					"In EquipmentListDownloadFileServlet.doPost(), total time taken for calling equipment list api is {} "
							+ watch.getTime());

			MyEquipmentModel myEquipmentModel = request.getResource().adaptTo(MyEquipmentModel.class);

			if (null == myEquipmentModel) {
				LOGGER.error("My Equipment Model is null!");
			} else {
				flag = excelService.generateEquipmentListExcel(request, response, results);
			}

			if (!flag) {
				LOGGER.error("Equipment results file download failed!");
				HttpUtil.sendErrorMessage(response);
			}
		} catch (Exception e) {
			LOGGER.error("Equipment results file download failed", e);
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
