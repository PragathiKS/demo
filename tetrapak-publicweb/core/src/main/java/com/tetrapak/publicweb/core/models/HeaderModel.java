package com.tetrapak.publicweb.core.models;

import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;
import com.tetrapak.publicweb.core.beans.GeneralInfoBean;
import com.tetrapak.publicweb.core.beans.NavigationLinkBean;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeaderModel {

    private static final Logger log = LoggerFactory.getLogger(HeaderModel.class);

    @Self
    private Resource resource;

    private String loginTextI18n;
    private String searchRootPath;
    private String searchResultsPath;
    private String searchPlaceholderText;
    private String[] searchPanelTiles;

    @Inject
    private Boolean enableHeroImage;
    private List<NavigationLinkBean> megaMenuLinksList = new ArrayList<>();

    @PostConstruct
    protected void init() {

        InheritanceValueMap inheritanceValueMap = new HierarchyNodeInheritanceValueMap(resource);
        loginTextI18n = inheritanceValueMap.getInherited("loginTextI18n", String.class);
        searchRootPath = inheritanceValueMap.getInherited("searchRootPath", String.class);
        searchResultsPath = inheritanceValueMap.getInherited("searchResultsPath", String.class);
        searchPlaceholderText = inheritanceValueMap.getInherited("searchPlaceholderText", String.class);
        searchPanelTiles = inheritanceValueMap.getInherited("searchPanelTiles", String[].class);
        String[] megaMenuLinks = inheritanceValueMap.getInherited("megamenuLinks", String[].class);
        LinkUtils.setMultifieldNavLinkItems(megaMenuLinks, megaMenuLinksList, log);
    }

    /**
     * Method to get the search panel items from the multifield property saved in CRX for
     * each of the search panel tiles.
     *
     * @param searchPanelTiles String[]
     * @return List<String>
     */
    public static List<GeneralInfoBean> getTiles(String[] searchPanelTiles) {
        @SuppressWarnings("deprecation")
        List<GeneralInfoBean> tileList = new ArrayList<GeneralInfoBean>();
        JSONObject jObj;
        try {
            if (searchPanelTiles == null) {
                log.error("searchPanelTiles value is NULL");
            } else {
                for (int i = 0; i < searchPanelTiles.length; i++) {
                    jObj = new JSONObject(searchPanelTiles[i]);
                    GeneralInfoBean bean = new GeneralInfoBean();


                    if (jObj.has("tileImage")) {
                        bean.setImage(jObj.getString("tileImage"));
                    }
                    if (jObj.has("tileImageAltI18n")) {
                        bean.setImageAltText(jObj.getString("tileImageAltI18n"));
                    }
                    if (jObj.has("tileTitle")) {
                        bean.setTitle(jObj.getString("tileTitle"));
                    }
                    if (jObj.has("tileDescription")) {
                        bean.setDescription(jObj.getString("tileDescription"));
                    }
                    if (jObj.has("tileLinkPath")) {
                        bean.setLinkPath(jObj.getString("tileLinkPath"));
                    }
                    if (jObj.has("tileLinkTarget")) {
                        bean.setTargetBlank(jObj.getString("tileLinkTarget"));
                    }
                    tileList.add(bean);

                }
            }
        } catch (Exception e) {
            log.error("Exception while Multifield data {}", e.getMessage(), e);
        }
        return tileList;
    }

    public List<NavigationLinkBean> getMegaMenuLinksList() {
        return megaMenuLinksList;
    }

    public Boolean getEnableHeroImage() {
        return enableHeroImage;
    }

    public String getLoginTextI18n() {
        return loginTextI18n;
    }

    public String getSearchRootPath() {
        return searchRootPath;
    }

    public String getSearchResultsPath() {
        return searchResultsPath;
    }

    public String getSearchPlaceholderText() {
        return searchPlaceholderText;
    }

    public List<GeneralInfoBean> getSearchPanelTiles() {
        return getTiles(searchPanelTiles);
    }

}
