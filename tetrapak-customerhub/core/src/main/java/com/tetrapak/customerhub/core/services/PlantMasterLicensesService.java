package com.tetrapak.customerhub.core.services;

import org.apache.sling.api.SlingHttpServletRequest;

import java.io.IOException;

public interface PlantMasterLicensesService {
    
    /**
     * Send email to configured mailbox
     * 
     * @param request
     * @return operation result
     * @throws IOException
     */
    boolean sendEmail(SlingHttpServletRequest request) throws IOException;
}
