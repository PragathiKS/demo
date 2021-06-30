package com.tetralaval.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * The Class BusinessInquiryModel.
 *
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FormConsentConfigModel {

    /** The resource. */
    @Self
    private Resource resource;

    /** The privacy policy. */
    @ValueMapValue
    private String privacyPolicy;

    /** The marketing consent. */
    @ValueMapValue
    private String marketingConsent;

    /**
     * Gets the privacy policy.
     *
     * @return the privacy policy
     */
    public String getPrivacyPolicy() {
        return privacyPolicy;
    }

    /**
     * Gets the marketing consent.
     *
     * @return the marketing consent
     */
    public String getMarketingConsent() {
        return marketingConsent;
    }

}