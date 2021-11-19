package com.tetrapak.customerhub.core.services;

import com.tetrapak.customerhub.core.beans.equipment.EquipmentResponse;
import com.tetrapak.customerhub.core.beans.equipment.EquipmentUpdateFormBean;

/**
 * Tetra Pak Equipment Details Service
 */
public interface EquipmentDetailsService {

    EquipmentResponse editEquipment(final EquipmentUpdateFormBean bean);

}
