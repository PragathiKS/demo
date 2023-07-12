package com.tetrapak.customerhub.core.services.impl;

import com.tetrapak.customerhub.core.services.PowerBiReportService;
import com.tetrapak.customerhub.core.services.config.PowerBiReportConfig;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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

    @Reference
    private HttpClientBuilderFactory HttpFactory;

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
    @Override
    public String getPbiServiceUrl() {

        return config.pbiserviceurl();
    }
     /**
     * @return the PowerBI Resource URL
     */
    @Override
    public String getPbiResourceUrl() {

        return config.pbiresourceurl();
    }

 /**
     * @return the PowerBI Embedtoken URL
     */
    @Override
    public String getPbiEmbedtokenUrl() {

        return config.pbiembedtokenurl();
    }

    /**
     * @return the azureID tenantid
     */
    @Override
    public String getAzureidtenantid() {
        return config.azureidtenantid();
    }

    /**
     * @return the powerbi clientid
     */
    @Override
    public String getPbicid() {
        return config.pbicid();
    }

    /**
     * @return the powerbi client sceret
     */
    @Override
    public String getPbics() {
        return config.pbics();
    }

    /**
     * @return the powerbi datasetid
     */
    @Override
    public String getPbidatasetid() {
        return config.pbidatasetid();
    }

    /**
     * @return the powerbi reportid
     */
    @Override
    public String getPbireportid() {
        return config.pbireportid();
    }

    /**
     * @return the powerbi workspaceid
     */
    @Override
    public String getPbiworkspaceid() {
        return config.pbiworkspaceid();
    }

    // generate Bearer URL
    public String getGenerateApi() {

        //LOGGER.info("Inside generateAPI method  ::::");



       //String url = "https://login.microsoftonline.com/d2d2794a-61cc-4823-9690-8e288fd554cc/oauth2/token";
       String url = getPbiServiceUrl()+getAzureidtenantid()+ "/oauth2/token";
       //LOGGER.info(" String URL2  :::: {}", url);

        HttpClient httpClient = HttpFactory.newBuilder().build();

        String result = "";
        try {

            //LOGGER.info("Inside generateAPI Try  ::::");

            URIBuilder uribuilder = new URIBuilder(url);
           
            // add request parameter
            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair("resource", getPbiResourceUrl()));
            pairs.add(new BasicNameValuePair("client_id", getPbicid()));
            pairs.add(new BasicNameValuePair("client_secret", getPbics()));
            pairs.add(new BasicNameValuePair("grant_type", "client_credentials"));

            //LOGGER.info("Request Parameter : {}", pairs);
            HttpPost httppost = new HttpPost(uribuilder.build());

            httppost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");

            //LOGGER.info("Httppost before setEntity : {}", httppost);
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(pairs, "utf-8");
            formEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");

            httppost.setEntity(formEntity);
            // httppost.setEntity(new UrlEncodedFormEntity(pairs,StandardCharsets.UTF_8));

            //LOGGER.info("httppost : {}", httppost);

            // Execute and get the response.
            HttpResponse httpResponse = httpClient.execute(httppost);

            // LOGGER.info("Http Response before respEntity :
            // {}",IOUtils.toString(httpResponse.getEntity().getContent()));
            // HttpEntity respEntity = httpResponse.getEntity();
            // LOGGER.info("respEntity : {}",respEntity);

            // LOGGER.info("Status equal to 200 :
            // {}",httpResponse.getStatusLine().getStatusCode());

            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                //LOGGER.info("Status not equal to 200 : {}", httpResponse.getStatusLine().getStatusCode());
                throw new RuntimeException(
                        " Failed To Generate Bearer Token : HTTP error code : " + httpResponse.getStatusLine().getStatusCode());
            } else {
                // EntityUtils to get the response content
                // String content = EntityUtils.toString(respEntity);

                result = IOUtils.toString(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8);

                //LOGGER.info(" Inside respEntity result : {}", result);

            }

        } catch (URISyntaxException ue) {
            ue.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    public String getGenerateEmbedToken(String accessToken1) {

        //LOGGER.info("ET Inside getGenerateEmbedToken Method  ::::");

        String resourceet = "https://api.powerbi.com/v1.0/myorg/groups/"+getPbiworkspaceid()+"/reports/"+getPbireportid()+"/GenerateToken?accessLevel=View";
        //String resourceet2 = "https://api.powerbi.com/v1.0/myorg/groups/17884c21-49de-42ba-8519-dea43237c2df/reports/5d3c5f2a-b062-4ddb-ab09-4224fb845a99/GenerateToken?accessLevel=View";
        

         //LOGGER.info("ET ResourceEmbedToken >>>>>>  :::: {}", resourceet);
        HttpClient httpClientet = HttpFactory.newBuilder().build();

        String embedtoken = "";

        //LOGGER.info("ET Inside getGenerateEmbedToken AccessToken  :::: {}", accessToken1);
        //LOGGER.info("ET Inside getGenerateEmbedToken EmbedToken  :::: {}", embedtoken);

        try {
            //LOGGER.info("ET Inside getGenerateEmbedToken Try  ::::");

            URIBuilder uribuilder = new URIBuilder(resourceet);

            List<NameValuePair> pairset = new ArrayList<>();
            pairset.add(new BasicNameValuePair("accessLevel", "View"));
            pairset.add(new BasicNameValuePair("allowSaveAs", "false"));
            pairset.add(new BasicNameValuePair("datasetId", getPbidatasetid()));
            // pairset.add(new BasicNameValuePair("grant_type","client_credentials"));

            //LOGGER.info("ET Request Parameter : {}", pairset);
            HttpPost httppostet = new HttpPost(uribuilder.build());

            // httppostet.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httppostet.setHeader("Authorization", "Bearer "+ accessToken1);
            httppostet.setHeader("Accept", "application/json");
            httppostet.setHeader("Content-Type", "application/x-www-form-urlencoded");

            //LOGGER.info("ET Httppost before setEntity : {}", httppostet);
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(pairset, "utf-8");
            formEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");

            httppostet.setEntity(formEntity);
            // httppost.setEntity(new UrlEncodedFormEntity(pairs,StandardCharsets.UTF_8));
            //LOGGER.info("ET httppost after setEntity: {}", httppostet);

            // Execute and get the response.
            HttpResponse httpResponseet = httpClientet.execute(httppostet);

            /*LOGGER.info("ET Http Response before respEntity : {}",
                    IOUtils.toString(httpResponseet.getEntity().getContent()));
            HttpEntity respEntity = httpResponseet.getEntity();
            LOGGER.info("ET respEntity : {}", respEntity);*/

            

            if (httpResponseet.getStatusLine().getStatusCode() != 200) {
                //LOGGER.info("ET Status not equal to 200 : {}", httpResponseet.getStatusLine().getStatusCode());
                throw new RuntimeException(
                        " Failed To Generate Embed Token : HTTP error code : " + httpResponseet.getStatusLine().getStatusCode());
            } else {
                // EntityUtils to get the response content
                // String content = EntityUtils.toString(respEntity);

                //LOGGER.info("ET Status  equal to 200 : {}", httpResponseet.getStatusLine().getStatusCode());

                embedtoken = IOUtils.toString(httpResponseet.getEntity().getContent(), StandardCharsets.UTF_8);

                //LOGGER.info("ET Inside respEntity result : {}", embedtoken);

            }
        } catch (Exception e) {
            //LOGGER.info("ET Inside getGenerateEmbedToken Try  Catch ::::");
            //LOGGER.info("ET Inside Catch : {}", e.printStackTrace());
            //System.out.println(" SOP >>>>>>>> "+e.printStackTrace().);
            e.printStackTrace();
        }

        return embedtoken;

    }

    

}

