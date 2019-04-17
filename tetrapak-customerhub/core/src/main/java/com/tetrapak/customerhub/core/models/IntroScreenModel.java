package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.beans.GetStartedBean;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Iterator;


/**
 * Model class for Intro Screen Component
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class IntroScreenModel extends GetStartedModel {

    @Inject
    private String nextBtnI18n;

    @Inject
    private String closeBtnI18n;

    @Inject
    private String getStartedBtnI18n;

    @Override
    @PostConstruct
    protected void init() {
        Resource childResource = resource.getChild("list");
        if (null != childResource) {
            Iterator<Resource> itr = childResource.listChildren();
            while (itr.hasNext()) {
                Resource res = itr.next();
                ValueMap valueMap = res.getValueMap();
                GetStartedBean introScreenBean = new GetStartedBean();
                introScreenBean.setImagePath((String) valueMap.get("imagePath"));
                introScreenBean.setImageAltI18n((String) valueMap.get("imageAltI18n"));
                introScreenBean.setTitleI18n((String) valueMap.get("titleI18n"));
                introScreenBean.setDescriptionI18n((String) valueMap.get("descriptionI18n"));
                getStartedList.add(introScreenBean);
            }
        }
    }

    public String getNextBtnI18n() {
        return nextBtnI18n;
    }

    public String getCloseBtnI18n() {
        return closeBtnI18n;
    }

    public String getGetStartedBtnI18n() {
        return getStartedBtnI18n;
    }
}
