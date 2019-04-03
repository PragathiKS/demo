package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.beans.GetStartedBean;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import java.util.Iterator;

/**
 * Model class for Recommended for you card component
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RecommendedForYouCardModel extends GetStartedModel {

    @Override
    @PostConstruct
    protected void init() {
        Resource childResource = resource.getChild("list");
        if (null != childResource) {
            Iterator<Resource> itr = childResource.listChildren();
            while (itr.hasNext()) {
                Resource res = itr.next();
                ValueMap valueMap = res.getValueMap();
                GetStartedBean recommendedForYouCardBean = new GetStartedBean();
                recommendedForYouCardBean.setImagePath((String) valueMap.get("imagePath"));
                recommendedForYouCardBean.setImageAltI18n((String) valueMap.get("imageAltI18n"));
                recommendedForYouCardBean.setTitleI18n((String) valueMap.get("titleI18n"));
                recommendedForYouCardBean.setDescriptionI18n((String) valueMap.get("descriptionI18n"));
                recommendedForYouCardBean.setLinkUrl((String) valueMap.get("linkUrl"));
                recommendedForYouCardBean.setLinkTextI18n((String) valueMap.get("linkTextI18n"));
                getStartedList.add(recommendedForYouCardBean);
            }
        }
    }

    public int getCols() {
        return getStartedList.size();
    }

}
