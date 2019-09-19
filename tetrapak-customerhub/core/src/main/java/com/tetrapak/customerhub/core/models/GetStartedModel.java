package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.beans.GetStartedBean;
import com.tetrapak.customerhub.core.beans.ImageBean;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private String className;

    private List<GetStartedBean> getStartedList = new ArrayList<>();

    private List<String> imageList = new ArrayList<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(GetStartedModel.class);

    @PostConstruct
    protected void init() {
        Resource childResource = resource.getChild("list");
        if (null != childResource) {
            Iterator<Resource> itr = childResource.listChildren();
            while (itr.hasNext()) {
                Resource res = itr.next();
                ValueMap resValueMap = res.getValueMap();
                String imageName = GlobalUtil.getValidName((String) resValueMap.get("titleI18n") + "-image");
                imageList.add(imageName);

                GetStartedBean bean = new GetStartedBean();
                bean.setTitleI18n((String) resValueMap.get("titleI18n"));
                bean.setDescriptionI18n((String) resValueMap.get("descriptionI18n"));

                Resource imageResource = GlobalUtil.getImageResource(res, imageName);
                if (null != imageResource) {
                    ImageBean imageBean = GlobalUtil.getImageBean(imageResource);
                    bean.setImage(imageBean);
                }
                getStartedList.add(bean);
            }
        }
        try {
            GlobalUtil.cleanUpImages(resource, "list", imageList);
        } catch (PersistenceException e) {
            LOGGER.error("Persistence Exception while deleting node", e);
        }
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

    public List<String> getImageList() {
        return imageList;
    }
}
