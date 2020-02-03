package com.tetrapak.publicweb.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BestPracticeLinePageModel extends BasePageModel {

    private String title;
    private String vanityDescription;
    private String ctaTexti18nKey;
    private String practiceImagePath;
    private String practiceImageAltI18n;

    @Override
    @PostConstruct
    public void init() {
        super.init();

        ValueMap jcrMap = super.getPageContent().getJcrMap();

        if (jcrMap != null) {
            title = jcrMap.get("jcr:title", String.class);
            vanityDescription = jcrMap.get("jcr:description", String.class);
            ctaTexti18nKey = jcrMap.get("ctaTexti18nKey", String.class);
            practiceImagePath = jcrMap.get("practiceImagePath", String.class);
            practiceImageAltI18n = jcrMap.get("practiceImageAltI18n", String.class);
        }
    }

    public String getTitle() {
        return title;
    }

    public String getVanityDescription() {
        return vanityDescription;
    }

    public String getCtaTexti18nKey() {
        return ctaTexti18nKey;
    }

    public String getPracticeImagePath() {
        return practiceImagePath;
    }

    public String getPracticeImageAltI18n() {
        return practiceImageAltI18n;
    }


}
