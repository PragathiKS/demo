package com.tetrapak.customerhub.core.services;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.equipment.AddEquipmentFormBean;

import java.io.File;
import java.util.List;

/**
 * Tetra Pak Equipment Details Service
 */
public interface AddEquipmentService {

    JsonObject addEquipment(String emailId, final AddEquipmentFormBean bean, String token, List<File> attachments);

}
