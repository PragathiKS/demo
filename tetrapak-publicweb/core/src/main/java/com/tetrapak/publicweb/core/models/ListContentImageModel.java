package com.tetrapak.publicweb.core.models;

import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ListContentImageModel {

    private static final Logger log = LoggerFactory.getLogger(ListContentImageModel.class);

    @Inject
    private String titleI18n;

    @Inject
    private String tabsAlignment;

    @Inject
    private String linkTextI18n;

    @Inject
    private String linkTooltipI18n;

    @Inject
    private String linkPath;

    @Inject
    private Boolean targetBlank;

    @Inject
    private String[] tabLinks;

    @Inject
    private String pwTheme;

    @Inject
    private String pwButtonTheme;

    @Inject
    private String pwPadding;

    @Inject
    private Boolean makeCollapsable;

    private Integer componentId = (int) (Math.random() * 1000 + 1);

    public String getTitleI18n() {
        return titleI18n;
    }

    public String getTabsAlignment() {
        return tabsAlignment;
    }

    public String getLinkTextI18n() {
        return linkTextI18n;
    }

    public String getLinkTooltipI18n() {
        return linkTooltipI18n;
    }

    public String getLinkPath() {
        return LinkUtils.sanitizeLink(linkPath);
    }

    public Boolean getTargetBlank() {
        return targetBlank;
    }

    public String[] getTabLinks() {
        return getTabLinks(tabLinks).toArray(new String[0]);
    }

    public String getPwTheme() {
        return pwTheme;
    }

    public String getPwButtonTheme() {
        return pwButtonTheme;
    }

    public String getPwPadding() {
        return pwPadding;
    }

    public Boolean getMakeCollapsable() {
        return makeCollapsable;
    }

    public Integer getComponentId() {
        return componentId;
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
