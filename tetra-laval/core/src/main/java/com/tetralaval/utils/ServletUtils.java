package com.tetralaval.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.xss.XSSAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tetralaval.beans.ContactUsResponse;
import com.tetralaval.constants.TLConstants;

public class ServletUtils {

    /**
     * The Constant for joining strings using String Util.
     */
    private static final String JOIN_CHARACTER = ",";

    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServletUtils.class);

    /**
     * process all request parameters
     * 
     * @param request
     * @param xssAPI
     * @param ignoreParameters
     * @return
     */
    public static Map<String, String> processInputParameters(final SlingHttpServletRequest request, XSSAPI xssAPI,
	    String[] ignoreParameters) {
	Map<String, String> emailParams = new HashMap<>();
	Map<String, String[]> requestParams = request.getParameterMap();
	requestParams.entrySet().stream().forEach((Map.Entry<String, String[]> entry) -> {
	    LOGGER.debug("Key: {} ::: Value: {}", entry.getKey(), entry.getValue());
	    if (!StringUtils.startsWithAny(entry.getKey(), ignoreParameters)) {
		String newValue = "";
		if (entry.getValue().length > 1) {
		    newValue = StringUtils.join(entry.getValue(), JOIN_CHARACTER);
		} else {
		    newValue = entry.getValue()[0];
		}
		newValue = xssAPI.encodeForHTML(newValue);
		emailParams.put(entry.getKey(), newValue);
	    }
	});
	return emailParams;
    }

}
