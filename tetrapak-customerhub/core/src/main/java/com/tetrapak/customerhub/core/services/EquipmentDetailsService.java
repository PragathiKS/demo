package com.tetrapak.customerhub.core.services;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.equipment.EquipmentUpdateFormBean;

/**
 * Tetra Pak Equipment Details Service
 */
public interface EquipmentDetailsService {

    JsonObject editEquipment(String userId, final EquipmentUpdateFormBean bean, String token);

}
