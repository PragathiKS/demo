package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;

/**
 * Model class for API GEE
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class APIGEEModel {

    @OSGiService
    private APIGEEService apigeeService;

    public String getApiURL() {
        return GlobalUtil.getApiURL(apigeeService, "");
    }

}
