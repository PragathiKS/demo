package com.tetrapak.publicweb.core.models;

import com.day.cq.tagging.Tag;
import com.day.cq.wcm.api.Page;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.settings.SlingSettingsService;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PageLoadAnalyticsModel {
    
    @Self
    private SlingHttpServletRequest request;

    @Inject
    private Page currentPage;
    
    @Inject
    private SlingSettingsService slingSettingsService;
    
    private static final String SITE_NAME = "publicweb";
    private static final String PAGE_LOAD_EVENT = "content-load";
    private static final String PRODUCT_PAGE_LOAD_EVENT = "pdp-load";
    public static final String TETRAPAK_TAGS_ROOT_PATH = "/content/cq:tags/tetrapak/";
    private static final String PRODUCT_PAGE_TEMPLATE_PATH = "/apps/publicweb/templates/productpage";
    
    private String channel = StringUtils.EMPTY;
	private String pageName = StringUtils.EMPTY;
	private String siteLanguage = StringUtils.EMPTY;
	private String siteCountry = StringUtils.EMPTY;
	private String breadcrumb = StringUtils.EMPTY;
	private String pageType = StringUtils.EMPTY;
	private String contentName = StringUtils.EMPTY;
	private String digitalData;
	private boolean production;
	private boolean staging;
	private boolean development;
	private StringBuilder siteSection1 = new StringBuilder(StringUtils.EMPTY);
	private StringBuilder siteSection2 = new StringBuilder(StringUtils.EMPTY);
	private StringBuilder siteSection3 = new StringBuilder(StringUtils.EMPTY);
	private StringBuilder siteSection4 = new StringBuilder(StringUtils.EMPTY);
	private Map<String, String> tagsMap = new HashMap<String, String>();
	private boolean productPage;
	private String productTitle = StringUtils.EMPTY;
    
    @PostConstruct
    public void initModel() {
        String currentPagePath = currentPage.getPath();
        
        String templatePath = currentPage.getProperties().get("cq:template", StringUtils.EMPTY);
        if (StringUtils.equals(templatePath, PRODUCT_PAGE_TEMPLATE_PATH)) {
        	productPage = true;
        	productTitle = currentPage.getProperties().get("title", String.class);
        }
        pageType = StringUtils.substringAfterLast(templatePath, "/");
        
        Locale pageLocale = currentPage.getLanguage(false);
        if (pageLocale != null) {
	        siteLanguage = pageLocale.getLanguage();
	        siteCountry = pageLocale.getCountry();
        }
        if (StringUtils.isBlank(siteCountry)) {
        	siteCountry = "gb";
        }
        
        updateBreadcrumb();
        updateRunMode();
        updateSiteSections();
        updatePageName();
        
        updateTagsMap();
        
        contentName = currentPage.getName();
        digitalData = buildDigitalDataJson();
    }

	private void updateBreadcrumb() {
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
    }
    
    private void updateSiteSections() {
    	int siteSectionIndex = 5;
        int currentPageIndex = currentPage.getDepth() - 1;
        if (updateSectionName(siteSectionIndex, currentPageIndex, siteSection1)) {
        	channel = siteSection1.toString();
        	siteSectionIndex++;
        	if (updateSectionName(siteSectionIndex, currentPageIndex, siteSection2)) {
        		siteSectionIndex++;
            	if (updateSectionName(siteSectionIndex, currentPageIndex, siteSection3)) {
            		siteSectionIndex++;
                	updateSectionName(siteSectionIndex, currentPageIndex, siteSection4);
                }
            }
        }
    }
    
    private void updatePageName() {
		pageName = SITE_NAME + ":" + siteCountry + "-" + siteLanguage + ":home";
        if (StringUtils.isNotEmpty(siteSection1.toString())) {
        	pageName += ":" + siteSection1.toString();
        	if (StringUtils.isNotEmpty(siteSection2.toString())) {
            	pageName += ":" + siteSection2.toString();
            	if (StringUtils.isNotEmpty(siteSection3.toString())) {
                	pageName += ":" + siteSection3.toString();
                	if (StringUtils.isNotEmpty(siteSection4.toString())) {
                    	pageName += ":" + siteSection4.toString();
                    }
                }
            }
        }
        if (currentPage.getDepth() > 5) {
        	pageName += ":" + currentPage.getName();
        }
	}
    
    private void updateTagsMap() {
    	Tag[] pageTags = currentPage.getTags();
        for (Tag tag: pageTags) {
        	String tagPath = tag.getPath();
        	if(tagPath.startsWith(TETRAPAK_TAGS_ROOT_PATH)) {
        		tagPath = StringUtils.substringAfter(tagPath, TETRAPAK_TAGS_ROOT_PATH);
        		String contentAttribute = StringUtils.substringBefore(tagPath, "/");
        		String contentValue = StringUtils.substringAfter(tagPath, "/");
        		if (StringUtils.isNoneBlank(contentValue)) {
        			contentValue = StringUtils.replace(contentValue, "/", ":");
        			tagsMap.put(contentAttribute, contentValue);
        		}
        	}
        }
	}
    
    private boolean updateSectionName(int siteSectionIndex, int currentPageIndex, StringBuilder siteSection) {
    	if (siteSectionIndex < currentPageIndex) {
	        Page siteSectionPage = currentPage.getAbsoluteParent(siteSectionIndex);
	        if (siteSectionPage != null) {
	        	siteSection.append(siteSectionPage.getName());
	        	return true;
	        }
        }
    	return false;
    }
    
    private void updateRunMode() {
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
    }
    
    private String buildDigitalDataJson() {

    	JsonObject digitalData = new JsonObject();
    	
    	JsonObject pageInfo = new JsonObject();
    	pageInfo.addProperty("channel", channel);
    	pageInfo.addProperty("pageType", pageType);
    	pageInfo.addProperty("pageName", pageName);
    	pageInfo.addProperty("breadCrumb", breadcrumb);
    	pageInfo.addProperty("siteSection1", siteSection1.toString());
    	pageInfo.addProperty("siteSection2", siteSection2.toString());
    	pageInfo.addProperty("siteSection3", siteSection3.toString());
    	pageInfo.addProperty("siteSection4", siteSection4.toString());
    	pageInfo.addProperty("siteCountry", siteCountry);
    	pageInfo.addProperty("siteLanguage", siteLanguage);
    	pageInfo.addProperty("siteName", SITE_NAME);
    	
    	if (productPage) {
    		pageInfo.addProperty("event", PRODUCT_PAGE_LOAD_EVENT);
    		pageInfo.addProperty("productName", productTitle);
    	} else {
    		pageInfo.addProperty("event", PAGE_LOAD_EVENT);
    	}
    	
    	JsonObject conentInfo = new JsonObject();
    	conentInfo.addProperty("contentName", contentName);
    	if(!tagsMap.isEmpty()) {
    		for (Map.Entry<String, String> entry : tagsMap.entrySet()) {
    			conentInfo.addProperty(entry.getKey(), entry.getValue());
    		}
    	}
    	
    	JsonObject userInfo = new JsonObject();
    	userInfo.addProperty("loginStatus", "guest");
    	userInfo.addProperty("salesForceId", "");
    	userInfo.addProperty("userRoles", "");
    	userInfo.addProperty("userCountryCode", "");
    	userInfo.addProperty("userLanguage", "");
    	
    	JsonObject errorInfo = new JsonObject();
    	errorInfo.addProperty("errorcode", "");
    	errorInfo.addProperty("errortype", "");
    	
    	digitalData.add("pageinfo", pageInfo);
    	digitalData.add("conentInfo", conentInfo);
    	digitalData.add("userinfo", userInfo);
    	digitalData.add("error", errorInfo);
        
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
	
	public String getDigitalData() {
		return digitalData;
	}

}
