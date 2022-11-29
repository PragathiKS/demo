package com.tetrapak.supplierportal.core.models;

import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;
import com.tetrapak.supplierportal.core.utils.LinkUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeaderModel {
    private static final String CONFIGURATION_PATH = "/jcr:content/root/responsivegrid/headerconfiguration";
    private static final String LOGO_TEXT_I18N_KEY = "sp.logotext.text";
    private static final String USER_INFO_I18N = "Charlie Svensson";

    @SlingObject private SlingHttpServletRequest request;

    private String logoUrl;

    private String mLogoLink;

    private String dLogoLink;

    private boolean mLogoLinkInternal;

    private boolean dLogoLinkInternal;

    @PostConstruct protected void init() {
        Resource resource = request.getResourceResolver()
                .getResource(SupplierPortalConstants.CONTENT_ROOT + CONFIGURATION_PATH);
        if (null != resource) {
            HeaderConfigurationModel model = resource.adaptTo(HeaderConfigurationModel.class);
            if (model != null) {
                logoUrl = model.getLogoUrl();
                mLogoLink = LinkUtil.getValidLink(request.getResource(), model.getmLogoLink());
                mLogoLinkInternal = !LinkUtil.isExternalLink(model.getmLogoLink());
                dLogoLink = LinkUtil.getValidLink(request.getResource(), model.getdLogoLink());
                dLogoLinkInternal = !LinkUtil.isExternalLink(model.getdLogoLink());
            }
        }
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getMLogoLink() {
        return mLogoLink;
    }

    public boolean mLogoLinkInternal() {
        return mLogoLinkInternal;
    }

    public String getDLogoLink() {
        return dLogoLink;
    }

    public boolean dLogoLinkInternal() {
        return dLogoLinkInternal;
    }

    public String getLogoTextI18n() {
        return LOGO_TEXT_I18N_KEY;
    }

    public String getGetUserInfoI18n() {
        return USER_INFO_I18N;
    }

}
