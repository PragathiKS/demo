package com.tetrapak.publicweb.core.models.multifield;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * The Class TabBeanModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TabBeanSemiAutoModel {
    
    /** The pageURL */
    @ValueMapValue
    private String pageURL;

    public String getPageURL() {
        return pageURL;
    }  

}
