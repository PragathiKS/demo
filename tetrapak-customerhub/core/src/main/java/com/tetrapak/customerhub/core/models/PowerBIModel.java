package com.tetrapak.customerhub.core.models;


import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.PowerBiReportService;
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
    private String embedtoken;
    private String reportId;
    private String embedURL;
   
  @PostConstruct
    protected void init() {
        reportId=pbireportService.getPbireportid(); 
        embedURL= CustomerHubConstants.PBIEMBTOKEN_PRETURL + pbireportService.getPbireportid();
        embedtoken= pbireportService.getGenerateEmbedToken();
        Cookie cookie = new Cookie(CustomerHubConstants.PBI_COOKIE_EMBEDTOKEN, embedtoken);
        cookie.setMaxAge(3600); 
        response.addCookie(cookie);
        LOGGER.debug("PBI COOKIE added ::::");
  }

   public void setEmbedtoken(String embedtoken) {
        this.embedtoken=embedtoken;
  }

   public void setReportid(String reportId) {
        this.reportId=reportId;
  }
  
   public void setEmbedURL(String embedURL) {
        this.embedURL=embedURL;
  }

   public String getEmbedtoken() {
        LOGGER.debug("PBI Embedtoken :::: {}",embedtoken);
        return embedtoken;
  }

  public String getReportid() {
        LOGGER.debug("PBI Report ID :::: {}",reportId);
        return reportId;
  }

   public String getEmbedURL() {
        LOGGER.debug("PBI EmbedURL :::: {}",embedURL);
        reportId=pbireportService.getPbireportid(); 
        embedURL= CustomerHubConstants.PBIEMBTOKEN_PRETURL + pbireportService.getPbireportid();
        return embedURL;
   }
  
}