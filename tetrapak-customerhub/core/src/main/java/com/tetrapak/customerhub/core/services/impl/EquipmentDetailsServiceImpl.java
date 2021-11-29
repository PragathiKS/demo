package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.equipment.EquipmentApiUpdateRequestBean;
import com.tetrapak.customerhub.core.beans.equipment.EquipmentMetaData;
import com.tetrapak.customerhub.core.beans.equipment.EquipmentUpdateFormBean;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.EquipmentDetailsService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import com.tetrapak.customerhub.core.utils.HttpUtil;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class EquipmentDetailsServiceImpl.
 */
@Component(service = EquipmentDetailsService.class, immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class EquipmentDetailsServiceImpl implements EquipmentDetailsService {

    private static final String MYEQUIPMENT_REQUEST_UPDATE = "myequipment-requestUpdate";
    private static final String TETRAPAK_CUSTOMERHUB = "tetrapak-customerhub";

    private HttpClient client = HttpClientBuilder.create().build();

    @Reference
    private APIGEEService apigeeService;

    @Override
    public JsonObject editEquipment(String userId, EquipmentUpdateFormBean bean, String token) {

        final String url = apigeeService.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR
                + GlobalUtil.getSelectedApiMapping(apigeeService, MYEQUIPMENT_REQUEST_UPDATE);

        Gson gson = new Gson();
        String apiJsonBean = gson.toJson(convertFormToApiJson(userId, bean));
        return HttpUtil.sendAPIGeePostWithEntity(url, token, apiJsonBean);
    }

    private EquipmentApiUpdateRequestBean convertFormToApiJson(String userId, EquipmentUpdateFormBean bean) {
        EquipmentApiUpdateRequestBean requestBean = new EquipmentApiUpdateRequestBean();
        requestBean.setReportedBy(userId);
        requestBean.setEquipmentNumber(bean.getEquipmentId());
        requestBean.setSerialNumber(bean.getSerialNumber());
        requestBean.setComment(bean.getComments());
        requestBean.setSource(TETRAPAK_CUSTOMERHUB);
        requestBean.setMetaDatas(createCollectionOfMetadatas(bean));

        return requestBean;
    }

    private List<EquipmentMetaData> createCollectionOfMetadatas(EquipmentUpdateFormBean bean) {

        List metadatas = new ArrayList();

        metadatas.add(bean.getCountryMetadata());
        metadatas.add(bean.getLocationMetadata());
        metadatas.add(bean.getSiteMetadata());
        metadatas.add(bean.getLineMetadata());
        metadatas.add(bean.getStatusMetadata());
        metadatas.add(bean.getPositionMetadata());
        metadatas.add(bean.getDescriptionMetadata());

        return metadatas;
    }
}
