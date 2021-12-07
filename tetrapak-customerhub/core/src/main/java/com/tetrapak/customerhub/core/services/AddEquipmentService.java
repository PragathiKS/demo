package com.tetrapak.customerhub.core.services;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.equipment.AddEquipmentFormBean;
import com.tetrapak.customerhub.core.beans.equipment.EquipmentUpdateFormBean;

/**
 * Tetra Pak Equipment Details Service
 */
public interface AddEquipmentService {

    JsonObject addEquipment(String userId, final AddEquipmentFormBean bean, String token);

}
