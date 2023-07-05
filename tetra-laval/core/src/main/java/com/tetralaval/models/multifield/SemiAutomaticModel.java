package com.tetralaval.models.multifield;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * The Class TabBeanModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SemiAutomaticModel {

    /** The pageURL. */
    @ValueMapValue
    private String pageURL;

    /**
     * Gets the page URL.
     *
     * @return the page URL
     */
    public String getPageURL() {
        return pageURL;
    }

}
