package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.tagging.TagManager;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CarouselWithFiltersModel {
	
	private static final Logger log = LoggerFactory.getLogger(CarouselWithFiltersModel.class);

	private static final String PUBLIC_WEB_GLOBAL_PAGE = "/content/tetrapak/public-web/global";

	@Self
	Resource resource;
	
	@Inject
	private String titleI18n;

	@Inject
	private String titleAlignment;
	
	@Inject
	private String[] productType;
	
	@Inject
	private String[] category;
	
	@Inject
	private String rootPath;

	@Inject
    private String pwTheme;

    @Inject
    private String pwButtonTheme;

    @Inject
    private String pwPadding;

    private Integer carouselId = (int )(Math.random() * 1000 + 1);
	
	private List<String> prodTypeList = new ArrayList<>();
	private Map<String, String> categoryTagsMap = new HashMap<>();
	
	private ResourceResolver resolver;
	private TagManager tagManager;
	
	@PostConstruct
	protected void init() {
		resolver = resource.getResourceResolver();
		tagManager = resolver.adaptTo(TagManager.class);
		if (tagManager!=null) {
			getTagList(productType);		
			getCategoryTagsMap(category);
		}
	}

	private void getCategoryTagsMap(String[] tagID) {
		if(tagID != null) {
			for (String tag : tagID) {
				String tagTitle = tagManager.resolve(tag).getTitle();
				log.info("Category tags map : {} - {}", tag, tagTitle);
				categoryTagsMap.put(tag, tagTitle);
			}
		}		
	}

	private void getTagList(String[] tagID) {
		if(tagID != null) {
			for (String tag : tagID) {
				String tagPath = tagManager.resolve(tag).getTagID();
				log.info("Tag Path : {}", tagPath);
				prodTypeList.add(tagPath);
			}
		}		
	}

	public String getTitleI18n() {
		return titleI18n;
	}

	public String getTitleAlignment() {
		return titleAlignment;
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

   	public Integer getCarouselId() {
        return carouselId;
    }

	public String getProdType() {
		return prodTypeList.get(0);
	}

	public Map<String, String> getCategoryTagsMap() {
		return categoryTagsMap;
	}

	public String getRootPath() {
		if (rootPath == null) {
			rootPath = PUBLIC_WEB_GLOBAL_PAGE;
		}
		return rootPath;
	}
	
}
