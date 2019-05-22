package com.tetrapak.publicweb.core.models;

import java.util.Date; 

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticlePageModel extends BasePageModel {

	private ValueMap jcrMap;

	private String articleTitle;
	private Boolean showArticleDate;
	private Date articleDate;
	private String articleImagePath;
	private String imageAltTextI18n;

	private Boolean showStickyNavigation;
	private String thumbnailImagePath;
	private String stickyNavTitle;
	private String stickyNavDescription;
	private String navLinkText;
	private String navLinkTarget;

	@PostConstruct
	public void init() {
		super.init();

		jcrMap = super.getPageContent().getJcrMap();

		if (jcrMap != null) {
			articleTitle = jcrMap.get("articleTitle", String.class);
			showArticleDate = jcrMap.get("showArticleDate", false);
			articleDate = jcrMap.get("articleDate", Date.class);
			articleImagePath = jcrMap.get("articleImagePath", String.class);
			imageAltTextI18n = jcrMap.get("imageAltTextI18n", String.class);

			showStickyNavigation = jcrMap.get("showStickyNavigation", false);
			thumbnailImagePath = jcrMap.get("thumbnailImagePath", String.class);
			stickyNavTitle = jcrMap.get("stickyNavTitle", String.class);
			stickyNavDescription = jcrMap.get("stickyNavDescription", String.class);
			navLinkText = jcrMap.get("navLinkText", String.class);
			navLinkTarget = jcrMap.get("navLinkTarget", String.class);
		}
	}

	public ValueMap getJcrMap() {
		return jcrMap;
	}

	public String getArticleTitle() {
		return articleTitle;
	}

	public Boolean getShowArticleDate() {
		return showArticleDate;
	}

	public Date getArticleDate() {
		return articleDate;
	}

	public String getArticleImagePath() {
		return articleImagePath;
	}

	public String getImageAltTextI18n() {
		return imageAltTextI18n;
	}

	public Boolean getShowStickyNavigation() {
		return showStickyNavigation;
	}

	public String getThumbnailImagePath() {
		return thumbnailImagePath;
	}

	public String getStickyNavTitle() {
		return stickyNavTitle;
	}

	public String getStickyNavDescription() {
		return stickyNavDescription;
	}

	public String getNavLinkText() {
		return navLinkText;
	}

	public String getNavLinkTarget() {
		return navLinkTarget;
	}

}
