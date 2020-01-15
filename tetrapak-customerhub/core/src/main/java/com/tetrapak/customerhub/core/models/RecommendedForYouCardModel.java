package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.beans.GetStartedBean;
import com.tetrapak.customerhub.core.beans.ImageBean;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import com.tetrapak.customerhub.core.utils.LinkUtil;
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

                String imageName = GlobalUtil.getValidName((String) valueMap.get("tabId") + "-image");
                imageList.add(imageName);

                GetStartedBean recommendedForYouCardBean = new GetStartedBean();
                recommendedForYouCardBean.setTitleI18n((String) valueMap.get("titleI18n"));
                recommendedForYouCardBean.setDescriptionI18n((String) valueMap.get("descriptionI18n"));

                Resource imageResource = GlobalUtil.getImageResource(res, imageName);
                if (null != imageResource) {
                    ImageBean imageBean = GlobalUtil.getImageBean(imageResource);
                    recommendedForYouCardBean.setImage(imageBean);
                }

                getStartedList.add(recommendedForYouCardBean);
            }
        }
    }

    public int getCols() {
        return getStartedList.size();
    }

}
