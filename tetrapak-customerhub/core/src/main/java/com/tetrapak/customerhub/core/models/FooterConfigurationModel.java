package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.models.multifield.FooterLinkModel;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FooterConfigurationModel {

    @Self
    private Resource resource;

    @Inject
    private List<FooterLinkModel> footerNavigationLinks;

    @Inject
    private String languageSwitchLabel;

    public List<FooterLinkModel> getFooterLinks() {
        final List<FooterLinkModel> lists = new ArrayList<>();
        if (Objects.nonNull(footerNavigationLinks)) {
            lists.addAll(footerNavigationLinks);
        }
        return lists;

    }

    public String getLanguageSwitchLabel() {
        return languageSwitchLabel;
    }
}
