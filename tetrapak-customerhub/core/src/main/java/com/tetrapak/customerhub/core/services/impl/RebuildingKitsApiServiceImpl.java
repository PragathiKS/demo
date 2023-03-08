package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.equipment.EquipmentApiUpdateRequestBean;
import com.tetrapak.customerhub.core.beans.rebuildingkits.ImplementationStatusUpdateBean;
import com.tetrapak.customerhub.core.beans.rebuildingkits.RKResults;
import com.tetrapak.customerhub.core.beans.rebuildingkits.RebuildingKits;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.RebuildingKitsApiService;
import com.tetrapak.customerhub.core.services.config.RebuildingKitsApiServiceConfig;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import com.tetrapak.customerhub.core.utils.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.xss.XSSFilter;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component(immediate = true, service = RebuildingKitsApiService.class, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(ocd = RebuildingKitsApiServiceConfig.class)
public class RebuildingKitsApiServiceImpl implements RebuildingKitsApiService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RebuildingKitsApiServiceImpl.class);

	@Reference
	private APIGEEService apigeeService;

	private RebuildingKitsApiServiceConfig config;

	@Reference
	private XSSFilter xssFilter;

	ExecutorService executor;

	private static final String RK_REQUEST_UPDATE = "rebuildingkits-requestupdate";

	/**
	 * activate method
	 * 
	 * @param config API GEE Service configuration
	 */
	@Activate
	public void activate(RebuildingKitsApiServiceConfig config) {
		this.config = config;
		int processors = Runtime.getRuntime().availableProcessors();
		executor = Executors.newFixedThreadPool(processors - 1);
		String numberOfProcessors = String.valueOf(processors);
		LOGGER.info("Number of CPU processors : {}", numberOfProcessors);
	}

	@Deactivate
	private void deactivate() {
		executor.shutdown();
	}

	/**
	 * @param
	 * @return number of records to fetch
	 */
	@Override
	public int getNoOfRecordsCount() {
		return config.noOfRecords();
	}

	@Override public JsonObject updateImplementationStatus(String token, String emailId, ImplementationStatusUpdateBean bean) {
		final String url = apigeeService.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR
				+ GlobalUtil.getSelectedApiMapping(apigeeService, RK_REQUEST_UPDATE);
		Gson gson = new Gson();
		String apiJsonBean = gson.toJson(convertFormToApiJson(emailId, bean));
		return HttpUtil.sendAPIGeePostWithEntity(url, token, apiJsonBean);
	}

	private ImplementationStatusUpdateBean convertFormToApiJson(String emailId, ImplementationStatusUpdateBean bean) {
		ImplementationStatusUpdateBean requestBean = new ImplementationStatusUpdateBean();
		requestBean.setReportedBy(emailId);
		requestBean.setReportedRebuildingKit(xssFilter.filter(bean.getReportedRebuildingKit()));
		requestBean.setReportedRebuildingKitName(xssFilter.filter(bean.getReportedRebuildingKitName()));
		requestBean.setSerialNumber(xssFilter.filter(bean.getSerialNumber()));
		requestBean.setComment(xssFilter.filter(bean.getComment()));
		requestBean.setSource("My Tetra Pak");
		requestBean.setCurrentStatus(xssFilter.filter(bean.getCurrentStatus()));
		requestBean.setReportedStatus(xssFilter.filter(bean.getReportedStatus()));
		requestBean.setDate((xssFilter.filter(bean.getDate())));
		return requestBean;
	}

	@Override
	public List<RebuildingKits> getRebuildingkitsList(String token, String countryCode) {

		List<RebuildingKits> allResults = new ArrayList<>();
		List<Integer> skipElements = new ArrayList<>();

		Map<String, String> getrbkInitial = getAllRebuildingKitsAPI(0, token, countryCode);

		// Make a call to first set of results to get the total number of results
		parseAPIResponse(getrbkInitial).ifPresent(s -> {
			RKResults rbkFirstSet = parseAPIResponse(getrbkInitial).get();
			int totalRbkListCount = rbkFirstSet.getMeta().getTotal();
			LOGGER.debug("Total rebuildingkits list count is {}", String.valueOf(totalRbkListCount));
			LOGGER.debug("Number of records to fetch in single set is {}", getNoOfRecordsCount());

			allResults.addAll(rbkFirstSet.getData());

			// Divide the total number of results to facilitate concurrent calling of API
			// corresponding to each set
			int numberOfCalls = totalRbkListCount / getNoOfRecordsCount();
			LOGGER.debug("Number of API calls : {}", numberOfCalls);
			IntStream.rangeClosed(1, numberOfCalls).forEach(index -> skipElements.add(getNoOfRecordsCount() * index));
		});

		// Concurrently make call to API for fetching results in smaller sets
		List<CompletableFuture<List<RebuildingKits>>> apiCallFutures = skipElements.stream()
				.map(skipElement -> CompletableFuture
						.supplyAsync(() -> getAllRebuildingKitsAPI(skipElement, token, countryCode),executor)
						.exceptionally(exception -> {
							LOGGER.error("Error while executing API for index starting from {}", skipElement,
									exception);
							return new HashMap<>();
						}))
				.map(apiResponseFuture -> apiResponseFuture.thenApply(this::extractRebuildingKitsList))
				.collect(Collectors.<CompletableFuture<List<RebuildingKits>>>toList());

		CompletableFuture.allOf(apiCallFutures.toArray(new CompletableFuture[apiCallFutures.size()]));
		for (CompletableFuture<List<RebuildingKits>> future : apiCallFutures) {
			try {
				allResults.addAll(future.get());
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				LOGGER.error("Error while making Equipment API Call : ", e);
			} catch (ExecutionException e) {
				LOGGER.error("Error while making Equipment API Call : ", e);
			}
		}
		LOGGER.info("Total number of results in equipment list generated is {} ", allResults.size());
		return allResults;
	}

	/**
	 * This method extracts the list of rebuilding kits list from parsed API response
	 * 
	 * @param apiResponse
	 * @return resultsList list of rebuilding kits
	 */
	private List<RebuildingKits> extractRebuildingKitsList(Map<String, String> apiResponse) {
		return parseAPIResponse(apiResponse).map(RKResults::getData).orElse(new ArrayList<>());
	}

	/**
	 * This method parses the Rebuilding Kits API response
	 * 
	 * @param apiResponse
	 * @return resultsList list of rebuilding kits */
	private Optional<RKResults> parseAPIResponse(Map<String, String> apiResponse) {
		Optional<RKResults> results = Optional.empty();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().disableHtmlEscaping().create();
		String statusResponse = apiResponse.get(CustomerHubConstants.STATUS);
		if (null != statusResponse && !CustomerHubConstants.RESPONSE_STATUS_OK.equalsIgnoreCase(statusResponse)) {
			LOGGER.error("Unable to retrieve response from API got status code:{}", statusResponse);
		} else {
			results = Optional.of(apiResponse.get(CustomerHubConstants.RESULT)).filter(StringUtils::isNotEmpty)
					.map(s -> gson.fromJson(s, RKResults.class));
			results.ifPresent(s -> LOGGER.debug("Total results in API call starting with index {} are {}", s.getMeta().getSkip(),
					s.getData().size()));
		}
		return results;
	}

	/**
	 * This method constructs the URL for the API to fetch all rebuilding kits List.
	 * Subsequently it executes an HTTP call to the said API.
	 * 
	 * @param skip
	 * @param token
	 * @param countryCode
	 * @return Map map containing JSON response of API and HTTP call status.
	 */
	private Map<String, String> getAllRebuildingKitsAPI(Integer skip, String token, String countryCode) {
		LOGGER.debug("Current Thread name : {}", Thread.currentThread().getName());
		final String url = apigeeService.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR
				+ GlobalUtil.getSelectedApiMapping(apigeeService, "rebuildingkits-rebuildingkitslist")
				+ CustomerHubConstants.QUESTION_MARK + CustomerHubConstants.COUNTRY_CODE + CustomerHubConstants.EQUALS_CHAR
				+ countryCode + CustomerHubConstants.AMPERSAND
				+ CustomerHubConstants.DOWNLOAD_EQUIPMENT_EXCEL_API_PARAMETER + CustomerHubConstants.AMPERSAND
				+ CustomerHubConstants.SKIP + CustomerHubConstants.EQUALS_CHAR + skip + CustomerHubConstants.AMPERSAND
				+ CustomerHubConstants.COUNT + CustomerHubConstants.EQUALS_CHAR + getNoOfRecordsCount() + CustomerHubConstants.AMPERSAND
				+ CustomerHubConstants.EQUIPMENT_API_SORT + CustomerHubConstants.EQUALS_CHAR + CustomerHubConstants.LINE_CODE + CustomerHubConstants.COMMA + CustomerHubConstants.POSITION;
		return HttpUtil.executeHttp(token, url);
	}
}
