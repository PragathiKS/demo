package com.tetrapak.customerhub.core.models;

import com.day.cq.wcm.api.Page;
import com.tetrapak.customerhub.core.models.multifield.FooterLinkModel;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import com.tetrapak.customerhub.core.utils.LinkUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.tetrapak.customerhub.core.constants.CustomerHubConstants.LINK_TARGET_NEWTAB;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FooterModel {

    @SlingObject
    private SlingHttpServletRequest request;

    @OSGiService
    private UserPreferenceService userPreferenceService;

    private List<FooterLinkModel> footerValidLinks = new ArrayList<>();

    private String languageSwitchLabel;

    @PostConstruct
    protected void init() {
        final List<FooterLinkModel> footerLinks;

        Page languagePage = GlobalUtil.getLanguagePage(request, userPreferenceService);
        if (null != languagePage) {
            final Resource footerConfigurationResource = request.getResourceResolver().getResource(languagePage.getPath()
                    + "/jcr:content/root/responsivegrid/footerconfiguration");
            if (Objects.nonNull(footerConfigurationResource)) {
                final FooterConfigurationModel configurationModel = footerConfigurationResource
                        .adaptTo(FooterConfigurationModel.class);
                if (Objects.nonNull(configurationModel)) {
                    footerLinks = configurationModel.getFooterLinks();
                    for (FooterLinkModel footerLink : footerLinks) {
                        if(LinkUtil.isExternalLink(footerLink.getLinkPath())){
                            footerLink.setLinkTarget(LINK_TARGET_NEWTAB);
                        }
                        String validLink = LinkUtil.getValidLink(request.getResource(), footerLink.getLinkPath());

                        footerLink.setLinkPath(validLink);
                        footerValidLinks.add(footerLink);
                    }
                languageSwitchLabel = configurationModel.getLanguageSwitchLabel();
                }
            }
        }


    }

    public List<FooterLinkModel> getFooterLinks() {
        return new ArrayList<>(footerValidLinks);
    }

    public String getLanguageSwitchLabel() {
        return languageSwitchLabel;
    }
}
