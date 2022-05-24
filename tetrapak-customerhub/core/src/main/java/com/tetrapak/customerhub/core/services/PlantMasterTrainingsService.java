package com.tetrapak.customerhub.core.services;

import org.apache.sling.api.SlingHttpServletRequest;

import com.tetrapak.customerhub.core.beans.aip.PlantMasterTrainingsFormBean;

/**
 * Plant Master Trainings Service.
 */
public interface PlantMasterTrainingsService {

    /**
     * Consolidates email data and initiates sling job to send email .
     *
     * @param plantMasterTrainingsFormBean
     *            the plant master trainings form bean
     * @param request
     *            the request
     * @return status of operation
     */
    boolean sendEmail(PlantMasterTrainingsFormBean plantMasterTrainingsFormBean,
            SlingHttpServletRequest request);

    /**
     * Creates the plant master trainings form bean.
     *
     * @param request
     *            the request
     * @return the plant master trainings form bean
     */
    PlantMasterTrainingsFormBean createPlantMasterTrainingsFormBean(SlingHttpServletRequest request);
}
