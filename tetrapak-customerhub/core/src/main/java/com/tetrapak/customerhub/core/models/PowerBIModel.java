package com.tetrapak.customerhub.core.models;


import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.PowerBiReportService;
import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;

import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
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

  @Self
  private SlingHttpServletRequest request;

  @SlingObject
    protected SlingHttpServletResponse response;
    private String embedtoken;
    @ValueMapValue
    private String reportId;
    private String embedURL;

    @ValueMapValue
    private boolean reportWithRLS;
   
  @PostConstruct
    protected void init() {
        LOGGER.debug("PBI MOdel Init Method ::::");
        if(StringUtils.isBlank(reportId)){
            reportId=pbireportService.getPbireportid();
        }
        embedURL= CustomerHubConstants.PBIEMBTOKEN_PRETURL + reportId;
        if(reportWithRLS){
            String bpNumber = GlobalUtil.getCustomerBPNumber(request);
            embedtoken = pbireportService.getEmbedTokenBasedOnBP(bpNumber,reportId);
        }else{
            embedtoken= pbireportService.getGenerateEmbedToken(reportId);
        }
        Cookie cookie = new Cookie(CustomerHubConstants.PBI_COOKIE_EMBEDTOKEN, embedtoken);
        cookie.setMaxAge(3600); 
        response.addCookie(cookie);
        LOGGER.debug("PBI COOKIE added ::::" );
  }

  public String getEmbedtoken() {
        return embedtoken;
  }

  public String getReportid() {
        return reportId;
  }

   public String getEmbedURL() {
        return embedURL;
   }
  
}