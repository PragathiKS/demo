package com.tetrapak.supplierportal.core.models;

import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;
import com.tetrapak.supplierportal.core.services.UserPreferenceService;
import com.tetrapak.supplierportal.core.utils.GlobalUtil;
import com.tetrapak.supplierportal.core.utils.LinkUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeaderModel {
    private static final String CONFIGURATION_PATH = "/jcr:content/root/responsivegrid/headerconfiguration";
    private static final String LOGO_TEXT_I18N_KEY = "sp.logotext.text";
    private static final String USER_INFO_I18N = "Charlie Svensson";

    @SlingObject private SlingHttpServletRequest request;

    @OSGiService private UserPreferenceService userPreferenceService;

    private String logoUrl;

    private String logoLink;

    private boolean logoLinkInternal;

    @PostConstruct protected void init() {
        String language = GlobalUtil.getSelectedLanguage(request, userPreferenceService);
        Resource resource = request.getResourceResolver()
                .getResource(SupplierPortalConstants.SUPPLIER_PATH + language + CONFIGURATION_PATH);
        if (null != resource) {
            HeaderConfigurationModel model = resource.adaptTo(HeaderConfigurationModel.class);
            if (model != null) {
                logoUrl = model.getLogoUrl();
                logoLink = LinkUtil.getValidLink(request.getResource(), model.getLogoLink());
                logoLinkInternal = !LinkUtil.isExternalLink(model.getLogoLink());
            }
        }
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getLogoLink() {
        return logoLink;
    }

    public boolean logoLinkInternal() {
        return logoLinkInternal;
    }

    public String getLogoTextI18n() {
        return LOGO_TEXT_I18N_KEY;
    }

    public String getGetUserInfoI18n() {
        return USER_INFO_I18N;
    }

}
