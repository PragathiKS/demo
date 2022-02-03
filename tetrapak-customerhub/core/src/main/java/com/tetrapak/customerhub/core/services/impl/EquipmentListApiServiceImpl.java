package com.tetrapak.customerhub.core.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tetrapak.customerhub.core.beans.equipmentlist.Equipments;
import com.tetrapak.customerhub.core.beans.equipmentlist.Results;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.EquipmentListApiService;
import com.tetrapak.customerhub.core.services.config.EquipmentListApiServiceConfig;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import com.tetrapak.customerhub.core.utils.HttpUtil;

@Component(immediate = true, service = EquipmentListApiService.class, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(ocd = EquipmentListApiServiceConfig.class)
public class EquipmentListApiServiceImpl implements EquipmentListApiService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentListApiServiceImpl.class);

	@Reference
	private APIGEEService apigeeService;

	private EquipmentListApiServiceConfig config;

	ExecutorService executor;

	/**
	 * activate method
	 * 
	 * @param config API GEE Service configuration
	 */
	@Activate
	public void activate(EquipmentListApiServiceConfig config) {

		this.config = config;
		int processors = Runtime.getRuntime().availableProcessors();
		executor = Executors.newFixedThreadPool(processors - 1);
		String numberOfProcessors = String.valueOf(processors);
		LOGGER.error("Number of CPU processors : {}", numberOfProcessors);
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

	@Override
	public List<Equipments> getEquipmentList(String token, String countryCode) {

		List<Equipments> allResults = new ArrayList<>();
		List<Integer> skipElements = new ArrayList<>();

		Map<String, String> getEquipmentInitial = getAllEquipmentAPI(0, token, countryCode);

		// Make a call to first set of results to get the total number of results
		parseAPIResponse(getEquipmentInitial).ifPresent(s -> {
			Results equipmentsFirstSet = parseAPIResponse(getEquipmentInitial).get();
			int totalEquipmentCount = equipmentsFirstSet.getMeta().getTotal();
			LOGGER.debug("Total equipment count is {}", String.valueOf(totalEquipmentCount));
			LOGGER.debug("Number of records to fetch in single set is {}", getNoOfRecordsCount());

			allResults.addAll(equipmentsFirstSet.getData());

			// Divide the total number of results to facilitate concurrent calling of API
			// corresponding to each set
			int numberOfCalls = totalEquipmentCount / getNoOfRecordsCount();
			LOGGER.debug("Number of API calls : {}", numberOfCalls);
			IntStream.rangeClosed(1, numberOfCalls).forEach(index -> skipElements.add(getNoOfRecordsCount() * index));
		});

		// Concurrently make call to API for fetching results in smaller sets
		List<CompletableFuture<List<Equipments>>> apiCallFutures = skipElements.stream()
				.map(skipElement -> CompletableFuture
						.supplyAsync(() -> getAllEquipmentAPI(skipElement, token, countryCode),executor)
						.exceptionally(exception -> {
							LOGGER.error("Error while executing API for index starting from {}", skipElement,
									exception);
							return new HashMap<>();
						}))
				.map(apiResponseFuture -> apiResponseFuture.thenApply(this::extractEquipmentList))
				.collect(Collectors.<CompletableFuture<List<Equipments>>>toList());

		CompletableFuture.allOf(apiCallFutures.toArray(new CompletableFuture[apiCallFutures.size()]));
		for (CompletableFuture<List<Equipments>> future : apiCallFutures) {
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
	 * This method extracts the list of equipments from parsed API response
	 * 
	 * @param apiResponse
	 * @return resultsList list of equipments
	 */
	public List<Equipments> extractEquipmentList(Map<String, String> apiResponse) {
		return parseAPIResponse(apiResponse).map(Results::getData).orElse(new ArrayList<>());
	}

	/**
	 * This method parses the Equipment API response
	 * 
	 * @param apiResponse
	 * @return resultsList list of equipments
	 */
	public Optional<Results> parseAPIResponse(Map<String, String> apiResponse) {
		Optional<Results> results = Optional.empty();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().disableHtmlEscaping().create();
		String statusResponse = apiResponse.get(CustomerHubConstants.STATUS);
		if (null != statusResponse && !CustomerHubConstants.RESPONSE_STATUS_OK.equalsIgnoreCase(statusResponse)) {
			LOGGER.error("Unable to retrieve response from API got status code:{}", statusResponse);
		} else {
			results = Optional.of(apiResponse.get(CustomerHubConstants.RESULT)).filter(StringUtils::isNotEmpty)
					.map(s -> gson.fromJson(s, Results.class));
			results.ifPresent(s -> LOGGER.debug("Total results in API call starting with index {} are {}", s.getMeta().getSkip(),
					s.getData().size()));
		}
		return results;
	}

	/**
	 * This method constructs the URL for the API to fetch all equipments.
	 * Subsequently it executes an HTTP call to the said API.
	 * 
	 * @param skip
	 * @param token
	 * @param countryCode
	 * @return Map map containing JSON response of API and HTTP call status.
	 */
	public Map<String, String> getAllEquipmentAPI(Integer skip, String token, String countryCode) {
		LOGGER.debug("Current Thread name : {}", Thread.currentThread().getName());
		final String url = apigeeService.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR
				+ GlobalUtil.getSelectedApiMapping(apigeeService, "myequipment-equipmentlist")
				+ CustomerHubConstants.QUESTION_MARK + CustomerHubConstants.COUNTRY_CODE + CustomerHubConstants.EQUALS_CHAR
				+ countryCode + CustomerHubConstants.AMPERSAND
				+ CustomerHubConstants.DOWNLOAD_EQUIPMENT_EXCEL_API_PARAMETER + CustomerHubConstants.AMPERSAND
				+ CustomerHubConstants.SKIP + CustomerHubConstants.EQUALS_CHAR + skip + CustomerHubConstants.AMPERSAND
				+ CustomerHubConstants.COUNT + CustomerHubConstants.EQUALS_CHAR + getNoOfRecordsCount();
		return HttpUtil.executeHttp(token, url);
	}
}
