package com.tetrapak.publicweb.core.models;

import java.util.LinkedHashMap;
import java.util.Locale;
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

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SearchResultsModel {

	@Self
	Resource resource;

	@Inject
	private String filterTitle;

	@Inject
	private String[] filterTagPaths;

	protected final Logger log = LoggerFactory.getLogger(SearchResultsModel.class);
	private LinkedHashMap<String, String> tagsMap = new LinkedHashMap<>();

	@PostConstruct
	protected void init() {
		log.info("Executing init method.");
		ResourceResolver resourceResolver = resource.getResourceResolver();
		PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
		TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
		if (pageManager != null && tagManager != null) {
			Page currentPage = pageManager.getContainingPage(resource);
			Locale locale = currentPage.getLanguage(true);
			if (filterTagPaths != null) {
				for (String tagPath : filterTagPaths) {
					log.info("Tag path : {}", tagPath);
					Tag tag = tagManager.resolve(tagPath);
					tagsMap.put(tag.getTitle(locale), tagPath);
				}
			}
		}
	}

	public String getFilterTitle() {
		return filterTitle;
	}

	public Map<String, String> getTagsMap() {
		return tagsMap;
	}

}
