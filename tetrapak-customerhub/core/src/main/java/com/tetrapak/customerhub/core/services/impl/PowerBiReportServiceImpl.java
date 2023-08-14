package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.PowerBiReportService;
import com.tetrapak.customerhub.core.services.config.PowerBiReportConfig;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true, service = PowerBiReportService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = PowerBiReportConfig.class)
public class PowerBiReportServiceImpl implements PowerBiReportService {
    private PowerBiReportConfig config;

    private static final Logger LOGGER = LoggerFactory.getLogger(PowerBiReportServiceImpl.class);

    private String accessToken;

    @Reference
    private HttpClientBuilderFactory httpFactory;

    /**
     * 
     * activate method
     *
     * @param config PowerBi Report configuration
     */
    @Activate
    public void activate(PowerBiReportConfig config) {
        this.config = config;
    }

    /**
     * @return the PowerBI Service URL
     */
    public String getPbiServiceUrl() {

        return config.pbiserviceurl();
    }
     /**
     * @return the PowerBI Resource URL
     */
    
    public String getPbiResourceUrl() {

        return config.pbiresourceurl();
    }

    /**
     * @return the PowerBI Embedtoken URL
     */
    
    public String getPbiEmbedtokenUrl() {

        return config.pbiembedtokenurl();
    }

    /**
     * @return the azureID tenantid
     */
    
    public String getAzureidtenantid() {
        return config.azureidtenantid();
    }

    /**
     * @return the powerbi clientid
     */
    
    public String getPbicid() {
        return config.pbicid();
    }

    /**
     * @return the powerbi client sceret
     */
    
    public String getPbics() {
        return config.pbics();
    }

    /**
     * @return the powerbi datasetid
     */
    
    public String getPbidatasetid() {
        return config.pbidatasetid();
    }

    /**
     * @return the powerbi reportid
     */
    
    public String getPbireportid() {
        return config.pbireportid();
    }

    /**
     * @return the powerbi workspaceid
     */
    
    public String getPbiworkspaceid() {
        return config.pbiworkspaceid();
    }

    /**
     * @return the getGenerateApi
     */
    public String getGenerateApi() {

        LOGGER.debug("Inside generateAPI method of PowerBiReportServiceImpl ::::");
        String url = getPbiServiceUrl()+getAzureidtenantid()+ CustomerHubConstants.PBIGENERATEAPI_POSTURL;
        LOGGER.debug("PBI URL for generateAPI method of PowerBiReportServiceImpl :::: {}",url);
        HttpClient httpClient = httpFactory.newBuilder().build();
        
        try {
            URIBuilder uribuilder = new URIBuilder(url);
            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair(CustomerHubConstants.RESOURCE, getPbiResourceUrl()));
            pairs.add(new BasicNameValuePair(CustomerHubConstants.PBI_CLIENTID, getPbicid()));
            pairs.add(new BasicNameValuePair(CustomerHubConstants.PBI_CLIENT_SECRET, getPbics()));
            pairs.add(new BasicNameValuePair(CustomerHubConstants.GRANT_TYPE,CustomerHubConstants.PBI_CLIENT_CREDENTIALS));

            HttpPost httppost = new HttpPost(uribuilder.build());
            httppost.setHeader(CustomerHubConstants.CONTENT_TYPE, CustomerHubConstants.APPLICATION_TYPE_URLENCODED);
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(pairs, CustomerHubConstants.UTF_8);
            formEntity.setContentType(CustomerHubConstants.ENTITY_CONTENTTYPE);
            httppost.setEntity(formEntity);
            HttpResponse httpResponse = httpClient.execute(httppost);
            if (httpResponse.getStatusLine().getStatusCode() != 200 ) {
                throw new IllegalArgumentException(" Failed To Generate Bearer Token : HTTP error code : {} " +httpResponse.getStatusLine().getStatusCode() );
            } else 
            {
                accessToken = IOUtils.toString(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8);
                Gson gson = new Gson();
                Type mapType  = new TypeToken<Map<String,String>>(){}.getType();
                Map<String,String> mapAccessToken = gson.fromJson(accessToken, mapType);
                accessToken = mapAccessToken.get(CustomerHubConstants.ACCESS_TOKEN);
                LOGGER.info("PBI Access Token Generated ");
            }

        } catch (URISyntaxException ue) {
            LOGGER.error("URISyntaxException in PBI getGenerateApi : ",ue);
           } catch (Exception e) {
            LOGGER.error("Exception in PBI getGenerateApi : ",e);
        }
        return accessToken;

    }
    /**
     * @return the getGenerateEmbedToken
     */
    public String getGenerateEmbedToken() {
        
        accessToken=getGenerateApi();
        LOGGER.debug("ET Inside getGenerateEmbedToken Method  of  PowerBiReportServiceImpl ::::");
        String resourceet = getPbiEmbedtokenUrl()+getPbiworkspaceid()+ CustomerHubConstants.PBIEMB_REPORT +getPbireportid()+ CustomerHubConstants.PBIEMBTOKEN_POSTURL;
        LOGGER.debug("PBI PreEmbedURL for getGenerateEmbedToken method of PowerBiReportServiceImpl :::: {}",resourceet);
        HttpClient httpClientet = httpFactory.newBuilder().build();
        String embedtoken = "";
        try {
            URIBuilder uribuilder = new URIBuilder(resourceet);

            List<NameValuePair> pairset = new ArrayList<>();
            pairset.add(new BasicNameValuePair(CustomerHubConstants.ACCESS_LEVEL, CustomerHubConstants.VIEW));
            pairset.add(new BasicNameValuePair(CustomerHubConstants.ALLOW_SAVEAS_AS, CustomerHubConstants.FALSE));
            pairset.add(new BasicNameValuePair(CustomerHubConstants.PBI_DATASETID, getPbidatasetid()));
            HttpPost httppostet = new HttpPost(uribuilder.build());
            httppostet.setHeader(CustomerHubConstants.AUTHORIZATION_HEADER_NAME,CustomerHubConstants.BEARER_COOKIE_VALUE+ accessToken);
            httppostet.setHeader(CustomerHubConstants.ACCEPT, CustomerHubConstants.APPLICATION_JSON);
            httppostet.setHeader(CustomerHubConstants.CONTENT_TYPE, CustomerHubConstants.APPLICATION_TYPE_URLENCODED);
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(pairset, CustomerHubConstants.UTF_8);
            formEntity.setContentType(CustomerHubConstants.ENTITY_CONTENTTYPE);
            httppostet.setEntity(formEntity);
            HttpResponse httpResponseet = httpClientet.execute(httppostet);
            if (httpResponseet.getStatusLine().getStatusCode() != 200) {
                throw new IllegalArgumentException(
                        " Failed To Generate Embed Token : HTTP error code : " + httpResponseet.getStatusLine().getStatusCode());
            } else 
            {
                embedtoken = IOUtils.toString(httpResponseet.getEntity().getContent(), StandardCharsets.UTF_8);
                Gson gsonet = new Gson();
                Type mapTypeet  = new TypeToken<Map<String,String>>(){}.getType();
                Map<String,String> mapEmbedToken = gsonet.fromJson(embedtoken, mapTypeet);
                embedtoken = mapEmbedToken.get(CustomerHubConstants.TOKEN);
                LOGGER.info("PBI Embedtoken :::: {}",embedtoken);
                LOGGER.info("PBI Embedd Token Generated ");
            }
        } catch (Exception e) {
            
            LOGGER.error("Exception in PBI getGenerateEmbedToken : ",e);
        }

        return embedtoken;

    }
}

