package com.tetrapak.customerhub.core.services;

import com.tetrapak.customerhub.core.models.PlantMasterLicensesModel;

import java.io.IOException;
import java.util.ResourceBundle;

public interface PlantMasterLicensesService {

    /**
     * Send email to configured mailbox
     *
     *  resourceBundle
     *  licenseTypeHeader
     *  requestData
     *  masterLicensesModel
     * @return operation result
     * @throws IOException
     */
    boolean sendEmail(ResourceBundle resourceBundle, String licenseTypeHeader, String requestData,
            PlantMasterLicensesModel masterLicensesModel) throws IOException;
}
