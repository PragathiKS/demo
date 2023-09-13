package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tetrapak.customerhub.core.beans.powerbi.BPIdentity;
import com.tetrapak.customerhub.core.beans.powerbi.PowerBIRequestWithBP;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.PowerBiReportService;
import com.tetrapak.customerhub.core.services.config.PowerBiReportConfig;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
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

    public String getPbidatasetidrls() {
        return config.pbidatasetidrls();
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
    public String getAccessToken() {
        String accessToken = "";

        LOGGER.debug("Inside generateAPI method of PowerBiReportServiceImpl ::::");
        String url = getPbiServiceUrl()+getAzureidtenantid()+ CustomerHubConstants.PBIGENERATEAPI_POSTURL;
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
                LOGGER.debug("PBI Raw Access Token Generated ::");
                Gson gson = new Gson();
                Type mapType  = new TypeToken<Map<String,String>>(){}.getType();
                Map<String,String> mapAccessToken = gson.fromJson(accessToken, mapType);
                accessToken = mapAccessToken.get(CustomerHubConstants.ACCESS_TOKEN);
                LOGGER.debug("PBI Access Token Generated ");
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
    public String getGenerateEmbedToken(String reportId) {
        LOGGER.debug("PBI Inside getGenerateEmbedToken ::::::");
        String accessToken= getAccessToken();
        String resourceet = getPbiEmbedtokenUrl()+getPbiworkspaceid()+ CustomerHubConstants.PBIEMB_REPORT +reportId+ CustomerHubConstants.PBIEMBTOKEN_POSTURL;

        String embedtoken = "";
        try {
            URIBuilder uribuilder = new URIBuilder(resourceet);

            List<NameValuePair> pairset = new ArrayList<>();
            pairset.add(new BasicNameValuePair(CustomerHubConstants.ACCESS_LEVEL, CustomerHubConstants.VIEW));
            pairset.add(new BasicNameValuePair(CustomerHubConstants.ALLOW_SAVEAS_AS, CustomerHubConstants.FALSE));
            pairset.add(new BasicNameValuePair(CustomerHubConstants.PBI_DATASETID, getPbidatasetid()));
            HttpPost httppostet = new HttpPost(uribuilder.build());
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(pairset, CustomerHubConstants.UTF_8);
            formEntity.setContentType(CustomerHubConstants.ENTITY_CONTENTTYPE);
            httppostet.setEntity(formEntity);
            embedtoken = executeAPIRequestAndGetEmbedToken(httppostet,accessToken);
        } catch (Exception e) {
            
            LOGGER.error("Exception in PBI getGenerateEmbedToken : ",e);
        }

        return embedtoken;

    }

    private String executeAPIRequestAndGetEmbedToken(HttpPost httpPost, String accessToken) throws IOException {
        HttpClient httpClient = httpFactory.newBuilder().build();
        httpPost.setHeader(CustomerHubConstants.AUTHORIZATION_HEADER_NAME,CustomerHubConstants.BEARER_COOKIE_VALUE+ accessToken);
        httpPost.setHeader(CustomerHubConstants.ACCEPT, CustomerHubConstants.APPLICATION_JSON);
        httpPost.setHeader(CustomerHubConstants.CONTENT_TYPE, CustomerHubConstants.APPLICATION_JSON);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        if (httpResponse.getStatusLine().getStatusCode() != 200) {
            throw new IllegalArgumentException(
                    " Failed To Generate Embed Token : HTTP error code : " + httpResponse.getStatusLine().getStatusCode());
        } else {
            String embedtoken = IOUtils.toString(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8);
            Gson gsonet = new Gson();
            Type mapTypeet  = new TypeToken<Map<String,String>>(){}.getType();
            Map<String,String> mapEmbedToken = gsonet.fromJson(embedtoken, mapTypeet);
            embedtoken = mapEmbedToken.get(CustomerHubConstants.TOKEN);
            LOGGER.debug("PBI Embedd Token Generated ");
            return embedtoken;
        }

    }

    @Override
    public String getEmbedTokenBasedOnBP(String bpNumber,String reportId) {
        String accessToken= getAccessToken();
        String apiURL = getPbiEmbedtokenUrl()+getPbiworkspaceid()+ CustomerHubConstants.PBIEMB_REPORT +reportId+ CustomerHubConstants.PBIEMBTOKEN_POSTURL;
        HttpPost httpPost = new HttpPost(apiURL);
        BPIdentity bpIdentity = new BPIdentity();
        bpIdentity.setUsername(bpNumber);
        List<String> roles = new ArrayList<>();
        roles.add("BusinessPartner");
        List<String> datasets = new ArrayList<>();
        datasets.add(getPbidatasetidrls());
        bpIdentity.setDatasets(datasets);
        bpIdentity.setRoles(roles);
        List<BPIdentity> identities = new ArrayList<>();
        identities.add(bpIdentity);
        PowerBIRequestWithBP requestWithBP = new PowerBIRequestWithBP();
        requestWithBP.setAccessLevel("View");
        requestWithBP.setAllowSaveAs(false);
        requestWithBP.setIdentities(identities);
        String requestBodyJson = new Gson().toJson(requestWithBP);
        httpPost.setEntity(new StringEntity(requestBodyJson, StandardCharsets.UTF_8));
        String embedToken = "";
        try {
            embedToken = executeAPIRequestAndGetEmbedToken(httpPost,accessToken);
        }catch (IOException e) {

            LOGGER.error("Exception in PBI getGenerateEmbedToken with BP : ",e);
        }
        return embedToken;
    }
}

