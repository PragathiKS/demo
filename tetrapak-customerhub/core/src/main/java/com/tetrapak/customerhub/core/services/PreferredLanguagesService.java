package com.tetrapak.customerhub.core.services;

import java.util.Map;

import org.apache.sling.api.resource.ResourceResolver;

/**
 * Service class related to Preferred Languages
 * 
 * @author semathura
 *
 */
public interface PreferredLanguagesService {

    public Map<String, String> getPreferredLanguages(final ResourceResolver resourceResolver);
}
