package com.tetrapak.customerhub.core.services;

import org.apache.sling.api.resource.Resource;

import java.util.Set;

public interface UserPreferenceService {

    Set<String> getSavedPreferences(Resource resource);
}
