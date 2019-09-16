package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.beans.ImageBean;
import com.tetrapak.customerhub.core.beans.TabsListBean;
import com.tetrapak.customerhub.core.services.DynamicMediaService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.settings.SlingSettingsService;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Model class for Tab list content component.
 *
 * @author Nitin Kumar
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TabsListModel {

    @Self
    private Resource resource;

    @OSGiService
    private DynamicMediaService dynamicMediaService;

    @OSGiService
    private SlingSettingsService slingSettingsService;

    @Inject
    private String heading;

    private String componentId = "tushar";

    private List<TabsListBean> tabsListContent = new ArrayList<>();

    private List<String> imageList = new ArrayList<>();

    /**
     * Post construct method to get the tab content from the multifield property
     * saved in CRX for each of the tab.
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
                String damVideoPath = (String) valueMap.get("damVideoPath");
                if (!slingSettingsService.getRunModes().contains("author") && null != dynamicMediaService) {
                    damVideoPath = GlobalUtil.getVideoUrlFromScene7(damVideoPath, dynamicMediaService);
                }
                tabsListBean.setDamVideoPath(damVideoPath);
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
        componentId = UUID.randomUUID().toString().replace("-", "");
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

    /**
     * Method to generate a unique string as component ID
     *
     * @return String componentId
     */
    public String getComponentId() {
        return componentId;
    }
}
