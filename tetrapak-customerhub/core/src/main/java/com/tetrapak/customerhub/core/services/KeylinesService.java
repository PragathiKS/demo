package com.tetrapak.customerhub.core.services;

import java.util.List;

import org.apache.sling.api.resource.ResourceResolver;

import com.tetrapak.customerhub.core.beans.keylines.Keylines;
import com.tetrapak.customerhub.core.exceptions.KeylinesException;

/**
 * Service class related to Keylines
 * 
 * @author selennys
 *
 */
public interface KeylinesService {

    public Keylines getKeylines(ResourceResolver resourceResolver, String packageType, List<String> shapes)
	    throws KeylinesException;
}
