package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.tetrapak.publicweb.core.beans.ProductListBean;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductListingModel {

    private static final Logger log = LoggerFactory.getLogger(ProductListingModel.class);

    @Self
    private Resource resource;

    @Inject
    private String titleI18n;

    @Inject
    private String productRootPath;

    @Inject
    private String pwTheme;

    @Inject
    private String firstTabLinkTextI18n;

    @Inject
    private String pwPadding;

    @Inject
    private String[] tabLinks;

    public String getTitleI18n() {
        return titleI18n;
    }

    public String getProductRootPath() {
        return productRootPath;
    }

    public String[] getTabLinks() {
        return tabLinks;
    }

    public String getFirstTabLinkTextI18n() {
        return firstTabLinkTextI18n;
    }

    public String getPwPadding() {
        return pwPadding;
    }

    public String getPwTheme() {
        return pwTheme;
    }


    /**
     * Method to get the tab link text from the multifield property saved in CRX for
     * each of the tab links.
     *
     * @param tabLinks String[]
     * @return List<ProductBean>
     */
    public List<ProductListBean> getTabLinks(String[] tabLinks) {
        ResourceResolver resourceResolver = resource.getResourceResolver();
        TagManager tagManager = resourceResolver.adaptTo(TagManager.class);

        List<ProductListBean> tabs = new ArrayList<ProductListBean>();
        JSONObject jObj;
        try {
            if (tabLinks == null) {
                log.error("Tab Links value is NULL");
            } else {
                for (int i = 0; i < tabLinks.length; i++) {
                    ProductListBean bean = new ProductListBean();
                    jObj = new JSONObject(tabLinks[i]);

                    if (jObj.has("tabLinkTextI18n")) {
                        bean.setTabLinkTextI18n(jObj.getString("tabLinkTextI18n"));
                    }

                    if (jObj.has("categoryTag")) {
                        Tag tag = tagManager.resolve(jObj.getString("categoryTag"));
                        log.info("Tag : " + tag.getTagID());
                        bean.setCategoryTag(tag.getTagID());
                        
                        String tagPath = jObj.getString("categoryTag");
                        if(tagPath.startsWith(PageLoadAnalyticsModel.TETRAPAK_TAGS_ROOT_PATH)) {
                    		tagPath = StringUtils.substringAfter(tagPath, PageLoadAnalyticsModel.TETRAPAK_TAGS_ROOT_PATH);
                    		String categoryTagAnalyticsPath = StringUtils.replace(tagPath, "/", ":");
                    		bean.setCategoryTagAnalyticsPath(categoryTagAnalyticsPath);
                    	}
                    }
                    tabs.add(bean);
                }
            }
        } catch (Exception e) {
            log.error("Exception while Multifield data {}", e.getMessage(), e);
        }
        return tabs;
    }

    public List<ProductListBean> getTabs() {
        return getTabLinks(tabLinks);
    }

}
