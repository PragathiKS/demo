package com.tetrapak.customerhub.core.services;

import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;

import org.apache.sling.api.resource.ResourceResolver;
import org.json.JSONException;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * DAM Utility Service class.
 * 
 * @author Aalekh Mathur
 */
public interface DamUtilityService {

    /**
     * Gets list of all pdf assets in Json format.
     *
     * @return the assets
     * @throws JSONException 
     */
    String getAssetsFromDam(ResourceResolver resolver) throws JsonProcessingException, ValueFormatException, PathNotFoundException, RepositoryException, JSONException;

}
