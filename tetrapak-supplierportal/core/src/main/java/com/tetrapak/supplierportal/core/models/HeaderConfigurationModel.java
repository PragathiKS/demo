package com.tetrapak.supplierportal.core.models;

import com.tetrapak.supplierportal.core.utils.LinkUtil;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeaderConfigurationModel {

    @ValueMapValue private String logoUrl;

    @ValueMapValue private String mLogoLink;

    @ValueMapValue private String dLogoLink;

    private boolean mLogoLinkInternal;

    private boolean dLogoLinkInternal;

    @PostConstruct protected void init() {
        this.mLogoLinkInternal = !LinkUtil.isExternalLink(mLogoLink);
        this.dLogoLinkInternal = !LinkUtil.isExternalLink(dLogoLink);
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getmLogoLink() {
        return mLogoLink;
    }

    public String getdLogoLink() {
        return dLogoLink;
    }

    public boolean ismLogoLinkInternal() {
        return mLogoLinkInternal;
    }

    public boolean isdLogoLinkInternal() {
        return dLogoLinkInternal;
    }
}

