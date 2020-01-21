package com.tetrapak.publicweb.core.models;

import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;


@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RecommendedReadingModel {

    @Inject
    private String textI18n;

    @Inject
    private String link;
    
    @PostConstruct
    protected void init() {
        if (StringUtils.isNotEmpty(link)) {
            link = LinkUtils.sanitizeLink(link);
        }
    }

    public String getTextI18n() {
        return textI18n;
    }

    public String getLink() {
        return link;
    }
}
