package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.publicweb.core.utils.LinkUtils;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductListingModel {

    private static final Logger log = LoggerFactory.getLogger(ProductListingModel.class);

    @Inject
    private String titleI18n;

    @Inject
    private String[] tabLinks;

    public String getTitleI18n() {
        return titleI18n;
    }

    public String[] getTabLinks() {
        return getTabLinks(tabLinks).toArray(new String[0]);
    }

    /**
     * Method to get the tab link text from the multifield property saved in CRX for
     * each of the tab links.
     *
     * @param tabLinks String[]
     * @return List<String>
     */
    public static List<String> getTabLinks(String[] tabLinks) {
        @SuppressWarnings("deprecation")
        List<String> tabs = new ArrayList<String>();
        JSONObject jObj;
        try {
            if (tabLinks == null) {
                log.error("Tab Links value is NULL");
            } else {
                for (int i = 0; i < tabLinks.length; i++) {
                    jObj = new JSONObject(tabLinks[i]);

                    String tabLinkTextI18n = "";
                    if (jObj.has("tabLinkTextI18n")) {
                        tabLinkTextI18n = jObj.getString("tabLinkTextI18n");
                    }
                    tabs.add(tabLinkTextI18n);

                }
            }
        } catch (Exception e) {
            log.error("Exception while Multifield data {}", e.getMessage(), e);
        }
        return tabs;
    }

}
