package com.tetrapak.publicweb.core.models;

import java.util.Locale;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.settings.SlingSettingsService;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PageLoadAnalyticsModel {
    
    @Self
    private SlingHttpServletRequest request;

    @Inject
    private Page currentPage;
    
    @Inject
    private SlingSettingsService slingSettingsService;

	private String serverName;
	private String pageName;
	private String language;
	private String country;
	private String breadcrumb;
	private String pageType;
	private String digitalData;
	private boolean production;
	private boolean staging;
	private boolean development;
	private boolean local;
    
    @PostConstruct
    public void initModel() {
        String currentPagePath = currentPage.getPath();
        pageName = StringUtils.substringAfter(currentPagePath, currentPage.getAbsoluteParent(1).getPath() + "/");
        pageName = StringUtils.replace(pageName, "/", ":");
        
        serverName = request.getServerName();
        
        pageType = currentPage.getTemplate().getTitle();
        
        Locale pageLocale = currentPage.getLanguage(false);
        if (pageLocale != null) {
	        language = pageLocale.getLanguage();
	        country = pageLocale.getCountry();
        }
        
        StringBuilder breadcrumbBuilder = new StringBuilder("Home");
        Page homePage = currentPage.getAbsoluteParent(4);

        if (homePage != null) {
        	int pageLevel = homePage.getDepth();
        	int currentPageLevel = currentPage.getDepth();
        	while (pageLevel < currentPageLevel) {
        		Page page = currentPage.getAbsoluteParent((int) pageLevel);
        		if (page == null) {
        			break;
        		}
        		pageLevel++;
        		if (!page.isHideInNav()) {
        			String pageNavigationTitle = StringUtils.isNotBlank(page.getNavigationTitle()) ? page.getNavigationTitle() : page.getTitle();
        			breadcrumbBuilder.append(":").append(pageNavigationTitle);
        		}
        	}
        }
        breadcrumb = breadcrumbBuilder.toString();
    
        if (slingSettingsService != null) {
        	Set<String> runModes = slingSettingsService.getRunModes();
	        if(runModes.contains("prod")) {
	        	production = true;
	        } else if(runModes.contains("stage")) {
	        	staging = true;
	        } else if(runModes.contains("dev") || runModes.contains("qa")) {
	        	development = true;
	        }
        }

        digitalData = buildDigitalDataJson();
    }
    
    private String buildDigitalDataJson() {

    	JsonObject digitalData = new JsonObject();
    	
    	JsonObject pageInfo = new JsonObject();
    	pageInfo.addProperty("server", serverName);
    	pageInfo.addProperty("pageType", pageType);
    	pageInfo.addProperty("pageName", pageName);
    	pageInfo.addProperty("breadCrumb", breadcrumb);
    	pageInfo.addProperty("siteCountry", country);
    	pageInfo.addProperty("siteLanguageNew", language);
    	
    	JsonObject userInfo = new JsonObject();
    	userInfo.addProperty("loggedIn", "guest");
    	
    	digitalData.add("pageinfo", pageInfo);
    	digitalData.add("userinfo", userInfo);
        
    	Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
        return gson.toJson(digitalData);
                        
    }

	public boolean isProduction() {
		return production;
	}

	public boolean isStaging() {
		return staging;
	}

	public boolean isDevelopment() {
		return development;
	}
	
	public boolean isLocal() {
		return local;
	}
	
	public String getDigitalData() {
		return digitalData;
	}

}
