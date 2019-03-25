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
}
