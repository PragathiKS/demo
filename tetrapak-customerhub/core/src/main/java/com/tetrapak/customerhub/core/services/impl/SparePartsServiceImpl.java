package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.beans.spareparts.ImageLinks;
import com.tetrapak.customerhub.core.beans.spareparts.ImageResponse;
import com.tetrapak.customerhub.core.beans.spareparts.SparePart;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.SparePartsService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimetypesFileTypeMap;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static com.tetrapak.customerhub.core.constants.CustomerHubConstants.*;

@Component(immediate = true, service = SparePartsService.class, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class SparePartsServiceImpl implements SparePartsService {

    @Reference
    private APIGEEService apigeeService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SparePartsServiceImpl.class);

    @Override
    public ImageLinks getImageLinks(String dimension, String partNumber) {
        String url = apigeeService.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR
                + GlobalUtil.getSelectedApiMapping(apigeeService, SPARE_PARTS_MEDIA_LINKS);
        url = url+QUESTION_MARK+DIMENSIONS_PARAM+EQUALS_CHAR+dimension+AMPERSAND+PART_NUMBERS_PARAM+EQUALS_CHAR+partNumber;
        HttpGet getRequest = new HttpGet(url);
        getRequest.addHeader(CONSUMER_KEY,apigeeService.getConsumerKey());
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(getRequest);
            LOGGER.debug("Http Get request status code: {}", httpResponse.getStatusLine().getStatusCode());
            if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String responseString = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
                return new Gson().fromJson(responseString,ImageLinks.class);
            }
        } catch (IOException e) {
            LOGGER.error("IOException occurred while getting invoice details response", e);
        }
        return null;
    }

    @Override
    public HttpResponse getImage(String imageLink) {
        HttpGet getRequest = new HttpGet(imageLink);
        getRequest.addHeader(CONSUMER_KEY,apigeeService.getConsumerKey());
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(getRequest);
            if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                LOGGER.debug("Http Post request status code: {}", httpResponse.getStatusLine().getStatusCode());
                return httpResponse;
            }
        } catch (IOException e) {
            LOGGER.error("IOException occurred while getting invoice details response", e);
        }
        return null;
    }

    @Override
    public ImageResponse getImage(String dimension, String partNumber) {
        ImageLinks imageLinks = getImageLinks(dimension,partNumber);
        if(imageLinks!=null){
            ArrayList<SparePart> parts = imageLinks.getParts();
            if(parts.isEmpty()){
                LOGGER.error("Empty parts from response");
                return null;
            }
            String imageLink = parts.get(0).getUrl();
            if(StringUtils.isBlank(imageLink)){
                LOGGER.error("Empty image link from response");
                return null;
            }
            HttpResponse httpResponse = getImage(imageLink);
            if(httpResponse!=null){
                ImageResponse imageResponse = new ImageResponse();
                imageResponse.setImageLink(imageLink);
                imageResponse.setHttpResponse(httpResponse);
                return imageResponse;
            }else {
                LOGGER.error("Error from Image response");
                return null;
            }
        }else{
            LOGGER.error("Error from Image Links response");
            return null;
        }
    }
}
