package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.beans.equipment.EquipmentApiUpdateRequestBean;
import com.tetrapak.customerhub.core.beans.equipment.EquipmentMetaData;
import com.tetrapak.customerhub.core.beans.equipment.EquipmentResponse;
import com.tetrapak.customerhub.core.beans.equipment.EquipmentUpdateFormBean;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.EquipmentDetailsService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class EquipmentDetailsServiceImpl.
 */
@Component(service = EquipmentDetailsService.class, immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class EquipmentDetailsServiceImpl implements EquipmentDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentDetailsServiceImpl.class);
    private static final String MYEQUIPMENT_REQUEST_UPDATE = "myequipment-requestUpdate";

    private HttpClient client = HttpClientBuilder.create().build();

    @Reference
    private APIGEEService apigeeService;

    @Override
    public EquipmentResponse editEquipment(EquipmentUpdateFormBean bean, String token) {

        if (StringUtils.isEmpty(token)) {
            return new EquipmentResponse("Missing token", HttpStatus.SC_BAD_REQUEST);
        }

        final String url = apigeeService.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR
                + GlobalUtil.getSelectedApiMapping(apigeeService, MYEQUIPMENT_REQUEST_UPDATE);

        Gson gson = new Gson();
        String jsonObject = gson.toJson(convertFormToApiJson(bean));

        return sendAPIGeePost(url, token, jsonObject);
    }

    private EquipmentApiUpdateRequestBean convertFormToApiJson(EquipmentUpdateFormBean bean) {
        EquipmentApiUpdateRequestBean requestBean = new EquipmentApiUpdateRequestBean();

        requestBean.setSerialNumber(bean.getEquipmentId());
        requestBean.setComment(bean.getComments());
        requestBean.setSource("tetrapak-customerhub");
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

    private EquipmentResponse sendAPIGeePost(String url, String token, String entity) {

        HttpPost apiRequest = new HttpPost(url);

        apiRequest.addHeader("Authorization", "Bearer " + token);
        apiRequest.addHeader("Content-Type", "application/json");
        apiRequest.addHeader("accept", "*/*");
        apiRequest.setEntity(new StringEntity(entity, StandardCharsets.UTF_8));

        EquipmentResponse response;
        try {
            HttpResponse res = client.execute(apiRequest);
            response = new EquipmentResponse(res.getStatusLine().getReasonPhrase(), res.getStatusLine().getStatusCode());
            LOGGER.debug("request sent: " + res.getStatusLine());
        } catch (UnsupportedEncodingException e) {
            response = new EquipmentResponse("Unsupported Encoding", HttpStatus.SC_INTERNAL_SERVER_ERROR);
            LOGGER.error("Unsupported Encoding" + e);
        } catch (ClientProtocolException e) {
            response = new EquipmentResponse("ClientProtocolException Error", HttpStatus.SC_INTERNAL_SERVER_ERROR);
            LOGGER.error("ClientProtocolException Error" + e);
        } catch (IOException e) {
            response = new EquipmentResponse("IO Error", HttpStatus.SC_INTERNAL_SERVER_ERROR);
            LOGGER.error("IO Error" + e);
        }
        return response;
    }
}
