package com.tetrapak.publicweb.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TeaserItemModel extends BasePageModel {

    private ValueMap jcrMap;

    private String title;
    private String description;
    private String imagePath;
    private String altText;
    private String linkText;
    private String linkPath;
    private boolean targetNew;
    private Date lastPublished;
    private String teaserDate;

    @Override
    @PostConstruct
    public void init() {
        super.init();
        jcrMap = super.getPageContent().getJcrMap();

        if (jcrMap != null) {
            title = jcrMap.get("jcr:title", String.class);
            description = jcrMap.get("jcr:description", String.class);

            Date lastPublished = getPageContent().getLastReplicated();
            if (lastPublished == null) {
                lastPublished = getPageContent().getLastUpdate();
            }
            if (lastPublished == null) {
                lastPublished = getPageContent().getCreatedOn();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            teaserDate = simpleDateFormat.format(lastPublished);

            imagePath = jcrMap.get("articleImagePath", String.class);
            altText = jcrMap.get("imageAltTextI18n", String.class);
            linkText = jcrMap.get("linkText", String.class);


        }
    }

    public ValueMap getJcrMap() {
        return jcrMap;
    }


    public String getDescription() {
        return description;
    }


    public String getLinkText() {
        return linkText;
    }


}
