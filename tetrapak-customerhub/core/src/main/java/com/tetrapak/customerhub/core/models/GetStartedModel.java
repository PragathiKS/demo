package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.beans.GetStartedBean;
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
 * Model class for Get Started Component
 *
 * @author Nitin Kumar
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class GetStartedModel {

    @Self
    protected Resource resource;

    @Inject
    private String headingI18n;

    @Inject
    protected String className;

    private List<GetStartedBean> getStartedList = new ArrayList<>();

    @PostConstruct
    protected void init() {
        Resource childResource = resource.getChild("list");
        if (null != childResource) {
            Iterator<Resource> itr = childResource.listChildren();
            int index = 0;
            while (itr.hasNext()) {
                Resource res = itr.next();
                Resource imageResource = getImageResource(index, res);
                index = index + 1;
                ValueMap resValueMap = res.getValueMap();
                GetStartedBean bean = new GetStartedBean();
                bean.setTitleI18n((String) resValueMap.get("titleI18n"));
                bean.setDescriptionI18n((String) resValueMap.get("descriptionI18n"));

                if (null != imageResource) {
                    ValueMap imgValueMap = imageResource.getValueMap();
                    bean.setDheight((String) imgValueMap.get("dheight"));
                    bean.setDwidth((String) imgValueMap.get("dwidth"));
                    bean.setMheightl((String) imgValueMap.get("mheightl"));
                    bean.setMwidthl((String) imgValueMap.get("mwidthl"));
                    bean.setMheightp((String) imgValueMap.get("mheightp"));
                    bean.setMwidthp((String) imgValueMap.get("mwidthp"));
                    bean.setImageCrop((String) imgValueMap.get("imageCrop"));
                    bean.setImagePath((String) imgValueMap.get("fileReference"));
                    bean.setImageAltI18n((String) imgValueMap.get("alt"));
                }
                getStartedList.add(bean);


            }
        }
    }

    private Resource getImageResource(int index, Resource res) {

        Resource listResource = res.getParent();
        if (null == listResource) {
            return null;
        }
        Resource getStartedResource = listResource.getParent();
        if (null == getStartedResource) {
            return null;
        }
        return getStartedResource.getChild(Integer.toString(index));
    }

    public List<GetStartedBean> getGetStartedList() {
        return getStartedList;
    }

    public String getHeadingI18n() {
        return headingI18n;
    }

    public String getClassName() {
        return className;
    }
}
