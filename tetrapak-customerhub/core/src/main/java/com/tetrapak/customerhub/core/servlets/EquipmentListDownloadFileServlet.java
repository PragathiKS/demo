package com.tetrapak.customerhub.core.servlets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import javax.servlet.Servlet;
import javax.servlet.ServletOutputStream;

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

import com.tetrapak.customerhub.core.beans.equipmentlist.Equipments;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.models.MyEquipmentModel;
import com.tetrapak.customerhub.core.services.EquipmentListApiService;
import com.tetrapak.customerhub.core.services.EquipmentListExcelService;
import com.tetrapak.customerhub.core.utils.HttpUtil;

/**
 * This servlet is used to download list of equipments
 *
 */
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Excel Generator Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_POST, "sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.resourceTypes=" + "customerhub/components/content/myequipment",
		"sling.servlet.selectors=" + "download", "sling.servlet.extensions=" + CustomerHubConstants.EXCEL })
public class EquipmentListDownloadFileServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = -8769537523421118922L;

	@Reference
	private transient EquipmentListApiService equipmentListApiService;

	@Reference
	private transient EquipmentListExcelService excelService;

	private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentListDownloadFileServlet.class);

	private static final String AUTH_TOKEN = "authToken";

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		try {
			boolean flag = false;
			String countryCode = request.getParameter(CustomerHubConstants.COUNTRY_CODE);

			final String token = request.getCookie(AUTH_TOKEN) == null ? StringUtils.EMPTY : getAuthTokenValue(request);
			LOGGER.debug("Got authToken from cookie : {}", token);

			StopWatch equipmentAPIClock = new StopWatch();
			equipmentAPIClock.start();
			List<Equipments> results = equipmentListApiService.getEquipmentList(token, countryCode);
			equipmentAPIClock.stop();
			LOGGER.debug("Total time taken for calling equipment list api is {} " ,equipmentAPIClock.getTime());

			MyEquipmentModel myEquipmentModel = request.getResource().adaptTo(MyEquipmentModel.class);

			if (null == myEquipmentModel) {
				LOGGER.error("My Equipment Model is null!");
			} else {
				StopWatch csvGenerationClock = new StopWatch();
				csvGenerationClock.start();
				flag = excelService.generateCSV(results,request,response);
				csvGenerationClock.stop();
				LOGGER.debug("Total Time taken for CSV generation : {}", csvGenerationClock.getTime());

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
