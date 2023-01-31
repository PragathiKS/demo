package com.tetrapak.customerhub.core.servlets;

import com.tetrapak.customerhub.core.beans.rebuildingkits.RebuildingKits;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.models.RebuildingKitsModel;
import com.tetrapak.customerhub.core.services.RebuildingKitsApiService;
import com.tetrapak.customerhub.core.services.impl.RebuildingKitsExcelServiceImpl;
import com.tetrapak.customerhub.core.utils.HttpUtil;
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

import javax.servlet.Servlet;
import java.util.List;

/**
 * This servlet is used to download list of rebuilding kits
 *
 */
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Rebuilding Kits List Excel Generator Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_POST, "sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.resourceTypes=" + "customerhub/components/content/rebuildingkits",
		"sling.servlet.selectors=" + "download", "sling.servlet.extensions=" + CustomerHubConstants.EXCEL })
public class RebuildingKitsListExcelDownloadServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = -8665484872354848654L;

	@Reference
	private transient RebuildingKitsApiService rebuildingKitsApiService;

	@Reference
	private transient RebuildingKitsExcelServiceImpl excelService;

	private static final Logger LOGGER = LoggerFactory.getLogger(RebuildingKitsListExcelDownloadServlet.class);

	private static final String AUTH_TOKEN = "authToken";

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		try {
			boolean flag = false;
			String countryCode = request.getParameter(CustomerHubConstants.COUNTRY_CODE);

			final String token = request.getCookie(AUTH_TOKEN) == null ? StringUtils.EMPTY : getAuthTokenValue(request);
			LOGGER.debug("Got authToken from cookie : {}", token);

			StopWatch rbkAPIClock = new StopWatch();
			rbkAPIClock.start();
			List<RebuildingKits> results = rebuildingKitsApiService.getRebuildingkitsList(token, countryCode);
			rbkAPIClock.stop();
			LOGGER.debug("Total time taken for calling equipment list api is {} " ,rbkAPIClock.getTime());

			RebuildingKitsModel rbkmodel = request.getResource().adaptTo(RebuildingKitsModel.class);

			if (null == rbkmodel) {
				LOGGER.error("Rebuilding Kits List is null!");
			} else {
				StopWatch csvGenerationClock = new StopWatch();
				csvGenerationClock.start();
				flag = excelService.generateCSV(results,request,response);
				csvGenerationClock.stop();
				LOGGER.debug("Total Time taken for CSV generation : {}", csvGenerationClock.getTime());

			}

			if (!flag) {
				LOGGER.error("Rebuilding kits List results file download failed!");
				HttpUtil.sendErrorMessage(response);
			}
		} catch (Exception e) {
			LOGGER.error("Rebuilding kits List results file download failed", e);
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
