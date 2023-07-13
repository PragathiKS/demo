package com.tetrapak.customerhub.core.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tetrapak.customerhub.core.services.PowerBiReportService;
import java.lang.reflect.Type;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Model class for PowerBiReport
 */
@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PowerBIModel {

   private static final Logger LOGGER = LoggerFactory.getLogger(PowerBIModel.class);

    @OSGiService
    private PowerBiReportService pbireportService;

    @SlingObject
     protected SlingHttpServletResponse response;

    private String result;
    private String embedtoken;
    private String reportId;
    private String embedURL;

    @PostConstruct
    protected void init() {
      result= pbireportService.getGenerateApi();

      reportId=pbireportService.getPbireportid(); 
      
      embedURL="https://app.powerbi.com/reportEmbed?reportId="+pbireportService.getPbireportid();

      LOGGER.info("PBI EmbedURL>>>>>>>>>>  {}",embedURL);

      Gson gson = new Gson();
      Type mapType  = new TypeToken<Map<String,String>>(){}.getType();
      Map<String,String> ser = gson.fromJson(result, mapType);
      String accessToken = ser.get("access_token");
      LOGGER.info("PBI Access Token Generated ");

      embedtoken= pbireportService.getGenerateEmbedToken(accessToken);
      Gson gsonet = new Gson();
      Type mapTypeet  = new TypeToken<Map<String,String>>(){}.getType();
      Map<String,String> seret = gsonet.fromJson(embedtoken, mapTypeet);
      String accessTokenet = seret.get("token");

      LOGGER.info("PBI Embedd Token Generated " );
      Cookie cookie = new Cookie("pbi-accesstoken", accessTokenet);
      cookie.setMaxAge(3600); // in seconds, 3600 = 1 hour.
      response.addCookie(cookie);
      LOGGER.info("PBI COOKIE added ::::");
      //LOGGER.info(" Response  Info: {}", response);
      embedtoken= accessTokenet;
    }

    public void setEmbedURL(String setEmbedURL) {
    LOGGER.info("Inside setEmbedURL method");
        this.embedURL = embedURL; }

   public String getEmbedtoken() {
      return embedtoken;
      }

    public String getReportid() {
        LOGGER.info("PBI Report ID :::: {}",reportId);
        return reportId;
      }

     public String getEmbedURL() {
        LOGGER.info("PBI EmbedURL :::: {}",embedURL);
        return embedURL;
      }
    public String getResult() {
        return result;
      }

}
