package com.tetrapak.customerhub.core.services;

import org.apache.sling.api.SlingHttpServletRequest;

import java.io.IOException;

public interface PlantMasterLicensesService {
    boolean sendEmail(SlingHttpServletRequest request) throws IOException;
}
