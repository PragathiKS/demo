package com.tetrapak.publicweb.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.tetrapak.publicweb.core.services.PadrotService;

/**
 * The Class BusinessInquiryModel.
 *
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BusinessInquiryModel extends FormModel {

    /** The resource. */
    @Self
    private Resource resource;


    /** The padrot service. */
    @OSGiService
    private PadrotService padrotService;


    /**
     * Gets the api url.
     *
     * @return the api url
     */
    @Override
    public String getApiUrl() {
        return padrotService.getBusinesInquiryServiceURL();
    }

}
