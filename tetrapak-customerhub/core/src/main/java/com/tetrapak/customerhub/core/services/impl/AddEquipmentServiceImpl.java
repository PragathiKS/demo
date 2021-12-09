package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.equipment.AddEquipmentApiRequestBean;
import com.tetrapak.customerhub.core.beans.equipment.AddEquipmentFormBean;
import com.tetrapak.customerhub.core.beans.equipmentlist.Equipments;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.AddEquipmentService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import com.tetrapak.customerhub.core.utils.HttpUtil;
import org.apache.commons.lang3.RegExUtils;
import org.apache.sling.xss.XSSFilter;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class AddEquipmentServiceImpl.
 */
@Component(service = AddEquipmentService.class, immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class AddEquipmentServiceImpl implements AddEquipmentService {

    private static final String MYEQUIPMENT_REPORT_MISSING = "myequipment-reportmissing";

    private static final Logger LOGGER = LoggerFactory.getLogger(AddEquipmentServiceImpl.class);

    @Reference
    private APIGEEService apigeeService;

    @Reference
    private XSSFilter xssFilter;

    @Override
    public JsonObject addEquipment(String userId, AddEquipmentFormBean bean, String token) {

        final String url = apigeeService.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR
                + GlobalUtil.getSelectedApiMapping(apigeeService, MYEQUIPMENT_REPORT_MISSING);

        Gson gson = new GsonBuilder().serializeNulls().create();
        String apiJsonBean = gson.toJson(convertFormToApiJson(userId, bean));
        JsonObject jsonObject = HttpUtil.sendAPIGeePostWithEntity(url, token, apiJsonBean);

        String id = resolveIdFromResponse(jsonObject);
        LOGGER.debug("created equipment with id: "+id);

        return jsonObject;
    }

    private String resolveIdFromResponse(JsonObject jsonObject) {
        Gson gson = new Gson();
        Equipments result = gson.fromJson(
                RegExUtils.removeAll(jsonObject.get("result").getAsString(), "[\\n]"),
                Equipments.class);
        String toReturn = null;
        if (result != null) {
            toReturn = result.getId();
        }
        return toReturn;
    }

    private AddEquipmentApiRequestBean convertFormToApiJson(String userId, AddEquipmentFormBean bean) {
        AddEquipmentApiRequestBean requestBean = new AddEquipmentApiRequestBean();

        requestBean.setReportedBy(userId);
        requestBean.setSerialNumber(xssFilter.filter(bean.getEquipmentSerialNumber()));
        requestBean.setCountry(xssFilter.filter(bean.getEquipmentCountry()));
        requestBean.setSite(xssFilter.filter(bean.getEquipmentSite()));
        requestBean.setLine(xssFilter.filter(bean.getEquipmentLine()));
        requestBean.setPosition(xssFilter.filter(bean.getEquipmentPosition()));
        requestBean.setUserStatus(xssFilter.filter(bean.getEquipmentStatus()));
        requestBean.setManufacture(xssFilter.filter(bean.getEquipmentMachineSystem()));
        requestBean.setEquipmentDesciption(xssFilter.filter(bean.getEquipmentDescription()));
        requestBean.setManufactureModelNumber(xssFilter.filter(bean.getEquipmentManufactureModelNumber()));
        requestBean.setManufacture(xssFilter.filter(bean.getEquipmentManufactureOfAsset()));
        requestBean.setManufactureCountry(xssFilter.filter(bean.getEquipmentCountryOfManufacture()));
        requestBean.setManufactureYear(xssFilter.filter(bean.getEquipmentConstructionYear()));
        requestBean.setComment(xssFilter.filter(bean.getEquipmentComments()));
        requestBean.setSource(CustomerHubConstants.TETRAPAK_CUSTOMERHUB);

        return requestBean;
    }
}
