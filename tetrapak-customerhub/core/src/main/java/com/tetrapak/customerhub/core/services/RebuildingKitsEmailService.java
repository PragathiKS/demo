package com.tetrapak.customerhub.core.services;

import com.tetrapak.customerhub.core.models.RebuildingKitDetailsModel;

import java.util.ResourceBundle;

/**
 * COTS Support Service
 */
public interface RebuildingKitsEmailService {

    
    /**
     * 
     * Consolidates email data and initiates sling job to send email 
     * @param resourceBundle
     * @param requestData
     * @param rebuildingKitDetailsModel
     * @return status of operation
     */
    boolean sendEmail(ResourceBundle resourceBundle, String requestData,
                      RebuildingKitDetailsModel rebuildingKitDetailsModel);
}
