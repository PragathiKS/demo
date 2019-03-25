package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.beans.RecommendedForYouCardBean;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Model class for Recommended for you card component
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RecommendedForYouCardModel {

    @Self
    private Resource resource;

    @Inject
    private String headingI18n;

    @Inject
    private String className;

    private List<RecommendedForYouCardBean> getStartedList = new ArrayList<>();

    @PostConstruct
    protected void init() {
        Resource childResource = resource.getChild("list");
        if (null != childResource) {
            Iterator<Resource> itr = childResource.listChildren();
            while (itr.hasNext()) {
                Resource res = itr.next();
                ValueMap valueMap = res.getValueMap();
                RecommendedForYouCardBean recommendedForYouCardBean = new RecommendedForYouCardBean();
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

    public List<RecommendedForYouCardBean> getGetStartedList() {
        return getStartedList;
    }

    public String getHeadingI18n() {
        return headingI18n;
    }

    public int getCols() {
        return getStartedList.size();
    }

    public String getClassName(){
        return className;
    }
}
