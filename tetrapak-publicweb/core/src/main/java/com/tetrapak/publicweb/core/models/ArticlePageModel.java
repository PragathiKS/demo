package com.tetrapak.publicweb.core.models;

import java.text.SimpleDateFormat;
import java.util.Date; 

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import com.tetrapak.publicweb.core.utils.LinkUtils;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticlePageModel extends BasePageModel {
	
	private ValueMap jcrMap;

	private String articleTitle;
	private Boolean showArticleDate;
	private String articleDate;
	private String articleImagePath;
	private String imageAltTextI18n;

	private Boolean showStickyNavigation;
	private String thumbnailImagePath;
	private String stickyNavTitle;
	private String stickyNavDescription;
	private String navLinkText;
	private String navLinkPath;
	private String navLinkTarget;

	@PostConstruct
	public void init() {
		super.init();
		jcrMap = super.getPageContent().getJcrMap();

		if (jcrMap != null) {
			articleTitle = jcrMap.get("jcr:title", String.class);
			showArticleDate = jcrMap.get("showArticleDate", false);
			
			Date date = jcrMap.get("articleDate", Date.class);
			if (date == null) {
				date = getPageContent().getCreatedOn();
			} 
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			articleDate = simpleDateFormat.format(date);
			
			articleImagePath = jcrMap.get("articleImagePath", String.class);
			imageAltTextI18n = jcrMap.get("imageAltTextI18n", String.class);

			showStickyNavigation = jcrMap.get("showStickyNavigation", false);
			thumbnailImagePath = jcrMap.get("thumbnailImagePath", String.class);
			stickyNavTitle = jcrMap.get("stickyNavTitle", String.class);
			stickyNavDescription = jcrMap.get("stickyNavDescription", String.class);
			navLinkText = jcrMap.get("navLinkText", String.class);
			navLinkPath = jcrMap.get("navLinkPath", String.class);
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

	public String getArticleDate() {
		return articleDate;
	}

	public String getArticleImagePath() {
		return LinkUtils.sanitizeLink(articleImagePath);
	}

	public String getImageAltTextI18n() {
		return imageAltTextI18n;
	}

	public Boolean getShowStickyNavigation() {
		return showStickyNavigation;
	}

	public String getThumbnailImagePath() {
		return LinkUtils.sanitizeLink(thumbnailImagePath);
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
	
	public String getNavLinkPath() {
		return navLinkPath;
	}

	public String getNavLinkTarget() {
		return navLinkTarget;
	}

}
