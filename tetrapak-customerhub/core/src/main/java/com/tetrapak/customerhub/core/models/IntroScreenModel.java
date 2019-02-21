package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.beans.GetStartedBean;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Model(adaptables = Resource.class)
public class IntroScreenModel {

    @Self
    private Resource resource;

    @Inject
    @Optional
    private String headingI18n;

    @Inject
    @Optional
    private String nextBtnI18n;

    @Inject
    @Optional
    private String closeBtnI18n;

    @Inject
    @Optional
    private String getStartedBtnI18n;

    private List<GetStartedBean> getStartedList = new ArrayList<>();

    @PostConstruct
    protected void init() {
        Resource childResource = resource.getChild("list");
        Iterator<Resource> itr = childResource.listChildren();
        if (null != childResource) {
            while (itr.hasNext()) {
                Resource res = itr.next();
                ValueMap valueMap = res.getValueMap();
                GetStartedBean bean = new GetStartedBean();
                bean.setImagePath((String) valueMap.get("imagePath"));
                bean.setImageAltI18n((String) valueMap.get("imageAltI18n"));
                bean.setTitleI18n((String) valueMap.get("titleI18n"));
                bean.setDescriptionI18n((String) valueMap.get("descriptionI18n"));
                getStartedList.add(bean);
            }
        }
    }

    public List<GetStartedBean> getGetStartedList() {
        return getStartedList;
    }

    public String getHeadingI18n() {
        return headingI18n;
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
