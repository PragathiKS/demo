package com.tetrapak.customerhub.core.mock;

import com.tetrapak.customerhub.core.services.AIPCategoryService;

public class MockAIPCategoryServiceImpl implements AIPCategoryService {

    @Override
    public String getAutomationTrainingsId() {
        return "4466";
    }

    @Override
    public String getEngineeringLicensesId() {
        return "4820";
    }

    @Override
    public String getSiteLicensesId() {
        return "4821";
    }
}
