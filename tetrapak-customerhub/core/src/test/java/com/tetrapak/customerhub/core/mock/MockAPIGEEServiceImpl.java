package com.tetrapak.customerhub.core.mock;

import com.tetrapak.customerhub.core.services.APIGEEService;

public class MockAPIGEEServiceImpl implements APIGEEService {
    @Override
    public String getApigeeServiceUrl() {
        return "https://api-mig.tetrapak.com";
    }

    @Override
    public String getApigeeClientID() {
        return "KHEnJskMGGogWrJAD3OyUI3VwerCLSDQ";
    }

    @Override
    public String getApigeeClientSecret() {
        return "jX38HGX7Ze4j6vvZ";
    }

    @Override
    public String[] getApiMappings() {
        return new String[]{"token-generator:bin/customerhub/token-generator","auth-token:/oauth2/v2/token","orderingcard:orders/history","ordersearch:orders/summary","orderdetails-parts:orders/details/parts","orderdetails-packmat:orders/details/packmat","financialstatement-filter:finance/statements/summary","financialstatement-results:finance/statements","financialstatement-invoice:content/billingdocuments","maintenance-filter:mock/materials/equipments/installations","maintenance-events:orders/service/events","maintenancecard:orders/service/events","documents-filter:mock/materials/equipments/installations","documents:techpub/search"};
    }
}
