package com.tetrapak.customerhub.core.services;

import com.day.cq.i18n.I18n;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.aip.CotsSupportFormBean;
import com.tetrapak.customerhub.core.beans.equipment.AddEquipmentFormBean;
import com.tetrapak.customerhub.core.models.CotsSupportModel;

import javax.activation.DataSource;
import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Tetra Pak Equipment Details Service
 */
public interface CotsSupportService {

    //void sendEmail(Map<String, DataSource> attachments);
    boolean sendEmail(List<Map<String,String>> attachments, CotsSupportModel model, CotsSupportFormBean cotsSupportFormBean,
            I18n i18n);

}
