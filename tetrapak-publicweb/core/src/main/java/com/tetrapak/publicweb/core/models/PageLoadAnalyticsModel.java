package com.tetrapak.publicweb.core.models;

import java.util.Locale;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.settings.SlingSettingsService;
import org.apache.sling.api.resource.Resource;

import com.day.cq.wcm.api.Page;
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
    private ResourceResolver resolver;
    
    @Inject
    private SlingSettingsService slingSettingsService;
    
    private String channel = StringUtils.EMPTY;
	private String pageName = StringUtils.EMPTY;;
	private String siteLanguage = StringUtils.EMPTY;;
	private String siteCountry = StringUtils.EMPTY;;
	private String breadcrumb = StringUtils.EMPTY;;
	private String pageType = StringUtils.EMPTY;;
	private String digitalData;
	private boolean production;
	private boolean staging;
	private boolean development;
	private String siteSection1 = StringUtils.EMPTY;;
	private String siteSection2 = StringUtils.EMPTY;;
	private String siteSection3 = StringUtils.EMPTY;;
	private String siteSection4 = StringUtils.EMPTY;;
    
    @PostConstruct
    public void initModel() {
        String currentPagePath = currentPage.getPath();
        pageName = StringUtils.substringAfter(currentPagePath, currentPage.getAbsoluteParent(1).getPath() + "/");
        pageName = StringUtils.replace(pageName, "/", ":");
        
        /*
        String templatePath = currentPage.getProperties().get("cq:template", String.class);
        Resource template = resolver.getResource(templatePath);
        if (template != null) {
        	pageType = template.getValueMap().get("jcr:title", String.class);
        }
        */
        
        Locale pageLocale = currentPage.getLanguage(false);
        if (pageLocale != null) {
	        siteLanguage = pageLocale.getLanguage();
	        siteCountry = pageLocale.getCountry();
        }
        
        if (!currentPage.isHideInNav()) {
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
        }
        
        int siteSectionIndex = 5;
        int currentPageIndex = currentPage.getDepth() - 1;
        if (siteSectionIndex < currentPageIndex) {
	        Page siteSection1Page = currentPage.getAbsoluteParent(siteSectionIndex);
	        if (siteSection1Page != null) {
	        	siteSection1 = siteSection1Page.getName();
	        	siteSectionIndex++;
	        	if (siteSectionIndex < currentPageIndex) {
	    	        Page siteSection2Page = currentPage.getAbsoluteParent(siteSectionIndex);
	    	        if (siteSection2Page != null) {
	    	        	siteSection2 = siteSection1Page.getName();
	    	        	siteSectionIndex++;
	    	        }
	            }
	        }
        }
        
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
    	
    	JsonObject conentInfo = new JsonObject();
    	conentInfo.addProperty("contentCategory", "");
    	
    	
    	JsonObject userInfo = new JsonObject();
    	userInfo.addProperty("loginStatus", "guest");
    	userInfo.addProperty("salesForceId", "");
    	userInfo.addProperty("userRoles", "");
    	userInfo.addProperty("userCountryCode", "");
    	userInfo.addProperty("userLanguage", "");
    	
    	digitalData.add("pageinfo", pageInfo);
    	digitalData.add("conentInfo", conentInfo);
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
