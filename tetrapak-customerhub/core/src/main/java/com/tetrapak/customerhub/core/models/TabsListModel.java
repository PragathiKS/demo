package com.tetrapak.customerhub.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.tetrapak.customerhub.core.beans.TabsListBean;

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
    private String headingI18n;
    
    private String youtubeVideoID;
    
    private List<TabsListBean> tabsListContent = new ArrayList<>();
    
    private String youtubeEmbedURL = StringUtils.EMPTY;

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
                tabsListBean.setTabTitleI18n((String) valueMap.get("tabTitleI18n"));
                tabsListBean.setMediaTitleI18n((String) valueMap.get("mediaTitleI18n"));
                tabsListBean.setMediaDescriptionI18n((String) valueMap.get("mediaDescriptionI18n"));
                tabsListBean.setVideoSource((String) valueMap.get("videoSource"));
                tabsListBean.setDamVideoPath((String) valueMap.get("damVideoPath"));
                youtubeVideoID = (String) valueMap.get("youtubeVideoID");
                if (StringUtils.isNotBlank(youtubeVideoID)) {
                    youtubeEmbedURL = "https://www.youtube.com/embed/" + youtubeVideoID;
                    tabsListBean.setYoutubeEmbedURL(youtubeEmbedURL);
                }
                tabsListBean.setThumbnailPath((String) valueMap.get("thumbnailPath"));
                tabsListBean.setImagePath((String) valueMap.get("imagePath"));
                tabsListBean.setImageAltI18n((String) valueMap.get("imageAltI18n"));
                tabsListBean.setIsExternal(Boolean.valueOf((String)valueMap.get("isExternal")));
                tabsListBean.setLinkTextI18n((String) valueMap.get("linkTextI18n"));
                tabsListBean.setLinkURL((String) valueMap.get("linkURL"));
                tabsListBean.setLinkType((String) valueMap.get("linkType"));
                tabsListContent.add(tabsListBean);
            }
        }
    }

    public String getHeadingI18n() {
        return headingI18n;
    }
    
    public List<TabsListBean> getTabs() {
        return tabsListContent;
    }
}
