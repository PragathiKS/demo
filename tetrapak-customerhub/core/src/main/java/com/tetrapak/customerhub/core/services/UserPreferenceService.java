package com.tetrapak.customerhub.core.services;

import org.apache.sling.api.resource.Resource;

import java.util.Set;

/**
 * User preference Service Interface
 */
@FunctionalInterface
public interface UserPreferenceService {

    /**
     * @param resource sling resource
     * @return
     */
    Set<String> getSavedPreferences(Resource resource);
}
