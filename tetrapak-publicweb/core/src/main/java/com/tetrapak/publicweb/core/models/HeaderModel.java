package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;
import com.tetrapak.publicweb.core.beans.NavigationLinkBean;
import com.tetrapak.publicweb.core.utils.LinkUtils;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeaderModel {

    private static final Logger log = LoggerFactory.getLogger(FooterModel.class);

    @Self
    private Resource resource;

    private String loginTextI18n;
    private String searchRootPath;
    private String searchResultsPath;
    
    @Inject
    private Boolean enableHeroImage;
    private List<NavigationLinkBean> megaMenuLinksList = new ArrayList<>();

    @PostConstruct
    protected void init() {
        InheritanceValueMap inheritanceValueMap = new HierarchyNodeInheritanceValueMap(resource);
        loginTextI18n = inheritanceValueMap.getInherited("loginTextI18n", String.class);
        searchRootPath = inheritanceValueMap.getInherited("searchRootPath", String.class);
        searchResultsPath = inheritanceValueMap.getInherited("searchResultsPath", String.class);
        String[] megaMenuLinks = inheritanceValueMap.getInherited("megamenuLinks", String[].class);
        LinkUtils.setMultifieldNavLinkItems(megaMenuLinks, megaMenuLinksList, log);
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
    
}
