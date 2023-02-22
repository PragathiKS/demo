package com.tetrapak.supplierportal.core.mock;

import com.tetrapak.supplierportal.core.services.UserPreferenceService;

public class MockUserPreferenceServiceImpl implements UserPreferenceService {
    @Override
    public String getSavedPreferences(String userId, String prefType) {
        return "fr";
    }

    @Override
    public boolean setPreferences(String userId, String prefType, String userPreferencesData) {
        return true;
    }
}
