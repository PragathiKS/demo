package com.tetrapak.publicweb.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * The Class BusinessInquiryFormModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BusinessInquiryFormModel extends FormModel {
    /** The resource. */
    @Self
    private Resource resource;

    /** The business area text. */
    @ValueMapValue
    private String businessAreaText;

    /** The company info text. */
    @ValueMapValue
    private String companyInfoText;

    /**
     * Gets the business area text.
     *
     * @return the business area text
     */
    public String getBusinessAreaText() {
        return businessAreaText;
    }

    /**
     * Gets the company info text.
     *
     * @return the company info text
     */
    public String getCompanyInfoText() {
        return companyInfoText;
    }

}
