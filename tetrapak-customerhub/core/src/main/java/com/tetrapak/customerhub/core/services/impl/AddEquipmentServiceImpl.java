package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.equipment.AddEquipmentFormBean;
import com.tetrapak.customerhub.core.services.AddEquipmentService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;

/**
 * The Class AddEquipmentServiceImpl.
 */
@Component(service = AddEquipmentService.class, immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class AddEquipmentServiceImpl implements AddEquipmentService {

    private static final String MYEQUIPMENT_REPORT_MISSING = "myequipment-reportmissing";

    @Override public JsonObject addEquipment(String userId, AddEquipmentFormBean bean, String token) {
        return null;
    }
}
