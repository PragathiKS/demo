package com.tetrapak.customerhub.core.services;

import org.apache.sling.api.SlingHttpServletRequest;

public interface PlantMasterLicensesService {
    boolean sendEmail(SlingHttpServletRequest request);
}
