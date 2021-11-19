package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.beans.equipment.EquipmentApiUpdateRequestBean;
import com.tetrapak.customerhub.core.beans.equipment.EquipmentMetaData;
import com.tetrapak.customerhub.core.beans.equipment.EquipmentResponse;
import com.tetrapak.customerhub.core.beans.equipment.EquipmentUpdateFormBean;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.EquipmentDetailsService;
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
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * The Class EquipmentDetailsServiceImpl.
 */
@Component(service = EquipmentDetailsService.class, immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(ocd = EquipmentDetailsServiceImpl.Config.class)
public class EquipmentDetailsServiceImpl implements EquipmentDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentDetailsServiceImpl.class);
    public static final String AUTH_TYPE = "Basic";

    @Reference
    private APIGEEService apigeeService;

    /**
     * The Interface Config.
     */
    @ObjectClassDefinition(
            name = "Tetra Pak - Equipment Details Service",
            description = "Tetra Pak - Equipment Details Service")
    public static @interface Config {

    }

    @Override
    public EquipmentResponse editEquipment(EquipmentUpdateFormBean bean) {

        final String url = apigeeService.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR
                + "equipments/requestupdate?version=preview";
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost apiRequest = new HttpPost(url);

        final String authString = apigeeService.getApigeeClientID() + ":" + apigeeService.getApigeeClientSecret();
        final String encodedAuthString = Base64.getEncoder()
                .encodeToString(authString.getBytes(StandardCharsets.UTF_8));

        apiRequest.addHeader("Authorization", AUTH_TYPE + " " + encodedAuthString);
        apiRequest.addHeader("Content-Type", "application/json");

        Gson gson = new Gson();
        String jsonObject = gson.toJson(convertFormToApiJson(bean));

        EquipmentResponse response = new EquipmentResponse("200", HttpStatus.SC_OK);
        try {
            apiRequest.setEntity(new StringEntity(jsonObject));
            HttpResponse res = client.execute(apiRequest);
            LOGGER.debug("request send: " + res.getStatusLine());
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
}
