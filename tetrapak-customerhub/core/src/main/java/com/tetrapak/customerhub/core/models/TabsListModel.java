package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.beans.ImageBean;
import com.tetrapak.customerhub.core.beans.TabsListBean;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.commons.lang3.StringUtils;
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
 * Model class for Tab list content component.
 *
 * @author tustusha
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TabsListModel {

    @Self
    private Resource resource;

    @Inject
    private String heading;

    private List<TabsListBean> tabsListContent = new ArrayList<>();

    private List<String> imageList = new ArrayList<>();

    /**
     * Post construct method to get the tab content from the multifield property saved in CRX for
     * each of the tab.
     */
    @PostConstruct
    protected void init() {
        Resource childResource = resource.getChild("tablist");
        if (null != childResource) {
            Iterator<Resource> itr = childResource.listChildren();
            while (itr.hasNext()) {
                Resource res = itr.next();

                ValueMap valueMap = res.getValueMap();
                TabsListBean tabsListBean = new TabsListBean();
                tabsListBean.setType((String) valueMap.get("type"));
                if ("image".equalsIgnoreCase(tabsListBean.getType())) {
                    imageList.add(res.getName() + "-image");
                }
                tabsListBean.setTabTitleI18n((String) valueMap.get("tabTitleI18n"));
                tabsListBean.setMediaTitleI18n((String) valueMap.get("mediaTitleI18n"));
                tabsListBean.setMediaDescriptionI18n((String) valueMap.get("mediaDescriptionI18n"));
                tabsListBean.setVideoSource((String) valueMap.get("videoSource"));
                tabsListBean.setDamVideoPath((String) valueMap.get("damVideoPath"));
                String youtubeVideoID = (String) valueMap.get("youtubeVideoID");
                if (StringUtils.isNotBlank(youtubeVideoID)) {
                    tabsListBean.setYoutubeEmbedURL("https://www.youtube.com/embed/" + youtubeVideoID);
                }
                tabsListBean.setThumbnailPath((String) valueMap.get("thumbnailPath"));

                Resource imageResource = GlobalUtil.getImageResource(res);
                if (null != imageResource) {
                    ImageBean imageBean = GlobalUtil.getImageBean(imageResource);
                    tabsListBean.setImage(imageBean);
                }

                tabsListBean.setIsExternal(Boolean.valueOf((String) valueMap.get("isExternal")));
                tabsListBean.setLinkTextI18n((String) valueMap.get("linkTextI18n"));
                tabsListBean.setLinkURL((String) valueMap.get("linkURL"));
                tabsListBean.setLinkType((String) valueMap.get("linkType"));
                tabsListContent.add(tabsListBean);
            }
        }
    }

    public String getHeading() {
        return heading;
    }

    public List<TabsListBean> getTabs() {
        return tabsListContent;
    }

    public List<String> getImageList() {
        return imageList;
    }
}
