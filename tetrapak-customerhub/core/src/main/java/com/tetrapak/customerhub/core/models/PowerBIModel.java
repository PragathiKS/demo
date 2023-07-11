package com.tetrapak.customerhub.core.models;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.PowerBiReportService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;

import java.lang.reflect.Type;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import org.apache.commons.lang3.StringUtils;
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
    private String rptid;

    @PostConstruct
    protected void init() {
      result= pbireportService.getGenerateApi();

      rptid= "https://app.powerbi.com/reportEmbed?reportId="+pbireportService.getPbireportid();

      LOGGER.info("Report ID ::::: {}", rptid);

      //LOGGER.debug("result debug: {}", result);
      //LOGGER.info("result  Info: {}", result);
      LOGGER.info("Inside PostConstruct Init Method ::::: {}", result.toString());


      Gson gson = new Gson();
      //LOGGER.info(" GSON 1 >> : {}",gson);
      Type mapType  = new TypeToken<Map<String,String>>(){}.getType();
      // LOGGER.info(" GSON 2 >> : {}",mapType);
      Map<String,String> ser = gson.fromJson(result, mapType);
      //  LOGGER.info(" GSON 3 >> : {}",ser);
      String accessToken = ser.get("access_token");
      LOGGER.info(" Inside PostConstruct Access Token : {}",accessToken );

      embedtoken= pbireportService.getGenerateEmbedToken(accessToken);
      LOGGER.info("ET Inside PostConstruct Embedd Token : {}",embedtoken );

      Gson gsonet = new Gson();
      //LOGGER.info("ET GSON 1 >> : {}",gsonet);
      Type mapTypeet  = new TypeToken<Map<String,String>>(){}.getType();
      //LOGGER.info("ET  GSON 2 >> : {}",mapTypeet);
      Map<String,String> seret = gsonet.fromJson(embedtoken, mapTypeet);
      //LOGGER.info("ET  GSON 3 >> : {}",seret);
      String accessTokenet = seret.get("token");
      LOGGER.info("ET Inside PostConstruct Embedd Token : {}",accessTokenet );


      Cookie cookie = new Cookie("pbi-accesstoken", accessTokenet);
      cookie.setMaxAge(86400); // in seconds, 86400 = 24 hours.
      response.addCookie(cookie);
      LOGGER.info(" COOKIE  Info: {}", cookie);
      LOGGER.info(" Response  Info: {}", response);

      embedtoken= accessTokenet;

    }

  /*public void setResult(String resulString) {
    LOGGER.info("Inside setResult method");
        this.result = result;
    }*/
  
   public String getEmbedtoken() {
        LOGGER.info("ET Inside getEmbedtoken method"); 
        LOGGER.info("ET Inside getEmbedtoken method {}",embedtoken);
        return embedtoken;

       
    }

    public String getRptid() {
        //LOGGER.info("Inside getResult method"); 
        LOGGER.info("Report ID Inside getRptid method {}",rptid);
        return rptid;

       
    }

        public String getResult() {
        //LOGGER.info("Inside getResult method"); 
        LOGGER.info("Inside getResult method {}",result);
        return result;

       
    }


    /*
    public String getValue() {
        //LOGGER.info("Inside getValue method");
        return "TestValue";
    }
    */

}
