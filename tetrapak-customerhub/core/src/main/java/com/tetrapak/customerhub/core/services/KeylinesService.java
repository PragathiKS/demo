package com.tetrapak.customerhub.core.services;

import java.util.List;
import java.util.Locale;

import com.tetrapak.customerhub.core.beans.keylines.Keylines;
import com.tetrapak.customerhub.core.exceptions.KeylinesException;

/**
 * Service class related to Keylines
 * 
 * @author selennys
 *
 */
public interface KeylinesService {

    public Keylines getKeylines(String packageType, List<String> shapes, Locale locale) throws KeylinesException;
}
