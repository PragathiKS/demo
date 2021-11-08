package com.tetrapak.customerhub.core.services;

import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletRequest;

/**
 * Tetra Pak Equipment Details Service
 */
public interface EquipmentDetailsService {

    HttpStatus addEquipment(final SlingHttpServletRequest request);

    HttpStatus editEquipment(final SlingHttpServletRequest request);

}
