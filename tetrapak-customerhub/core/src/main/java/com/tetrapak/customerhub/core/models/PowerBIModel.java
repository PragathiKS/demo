package com.tetrapak.customerhub.core.models;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.PowerBiReportService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;

/**
 * Model class for PowerBiReport
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PowerBIModel {

    @OSGiService
    private PowerBiReportService pbireportService;


    /** 
    public String getApiURL() {
        return GlobalUtil.getApiURL(pbireportService, "");
    }

    public String getApiMappings() {
        String apiUrl = GlobalUtil.getApiURL(pbireportService, "");
        JsonObject jsonObject = new JsonObject();
        String[] mapArray = GlobalUtil.getApiMappings(pbireportService);
        for (String mapping : mapArray) {
        	if (mapping.contains("token-generator")) { 
        		jsonObject.addProperty(StringUtils.substringBefore(mapping, ":"),
                    CustomerHubConstants.PATH_SEPARATOR + StringUtils.substringAfter(mapping, ":"));
        	} else {
        		jsonObject.addProperty(StringUtils.substringBefore(mapping, ":"),
                        apiUrl + CustomerHubConstants.PATH_SEPARATOR + StringUtils.substringAfter(mapping, ":"));
        	}
        }
        return jsonObject.toString();
    }
     */

}
