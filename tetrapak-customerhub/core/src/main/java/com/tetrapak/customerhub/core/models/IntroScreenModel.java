package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.beans.GetStartedBean;
import com.tetrapak.customerhub.core.beans.ImageBean;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
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

    @Inject
    private String errorMsgI18n;

    @Override
    @PostConstruct
    protected void init() {
        Resource childResource = resource.getChild("list");
        if (null != childResource) {
            Iterator<Resource> itr = childResource.listChildren();
            while (itr.hasNext()) {
                Resource res = itr.next();
                ValueMap valueMap = res.getValueMap();

                String imageName = GlobalUtil.getValidName((String) valueMap.get("tabId") + "-image");
                imageList.add(imageName);

                GetStartedBean introScreenBean = new GetStartedBean();
                introScreenBean.setTitleI18n((String) valueMap.get("titleI18n"));
                introScreenBean.setDescriptionI18n((String) valueMap.get("descriptionI18n"));

                Resource imageResource = GlobalUtil.getImageResource(res, imageName);
                if (null != imageResource) {
                    ImageBean imageBean = GlobalUtil.getImageBean(imageResource);
                    introScreenBean.setImage(imageBean);
                }

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

    /**
     * The method provides the path for the AJAX call
     *
     * @return path
     */
    public String onBoardingStatusURL() {
        return resource.getPath();
    }

    public String getErrorMsgI18n() {
        return errorMsgI18n;
    }

}
