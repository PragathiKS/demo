package com.tetrapak.supplierportal.core.mock;

import java.io.IOException;

import com.google.gson.JsonObject;
import com.tetrapak.supplierportal.core.services.APIGEEService;

public class MockAPIGEEServiceImpl implements APIGEEService {
	@Override
	public String getApigeeServiceUrl() {
		return "https://api-mig.tetrapak.com";
	}

	@Override
	public String[] getApiMappings() {
		return new String[] { "token-generator:bin/supplierportal/token-generator",
				"auth-token:/oauth2/v2/token",
				"sp-filters:/supplierpayments/filters",
				"sp-invoices:/supplierpayments/invoices"};
	}

	@Override
	public JsonObject retrieveAPIGEEToken(String accToken) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}
