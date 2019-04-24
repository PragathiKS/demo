package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.beans.TabBean;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.utils.PageUtil;
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
 * Model class for Tabs Component
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TabsModel {

    @Self
    protected Resource resource;

    @Inject
    protected String imagePath;

    protected List<TabBean> tabsList = new ArrayList<>();

    @PostConstruct
    protected void init() {
        Resource childResource = resource.getChild("tabsList");
        if (null != childResource) {
            Iterator<Resource> itr = childResource.listChildren();
            while (itr.hasNext()) {
                Resource res = itr.next();
                ValueMap valueMap = res.getValueMap();
                TabBean tabBean = new TabBean();
                tabBean.setPageUrl(valueMap.get("pageUrl") + CustomerHubConstants.HTML_EXTENSION);
                tabBean.setIconClass((String) valueMap.get("iconClass"));
                tabBean.setLabelI18n((String) valueMap.get("labelI18n"));
                tabBean.setActive(PageUtil.isCurrentPage(
                        PageUtil.getPageFromPath(resource, (String) valueMap.get("pageUrl")), resource));
                tabsList.add(tabBean);
            }
        }
    }
}
