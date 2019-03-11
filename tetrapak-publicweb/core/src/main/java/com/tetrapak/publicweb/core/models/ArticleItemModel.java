package com.tetrapak.publicweb.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import com.tetrapak.publicweb.core.utils.LinkUtils;


@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticleItemModel {

	@Self
	private Resource resource;

	@Inject
	private String contentType;

	@Inject
	private String articlePath;

	@Inject
	private String articleTitle;

	@Inject
	private String vanityDescriptionI18n;

	@Inject
	private String ctaTexti18nKey;

	@Inject
	private Boolean openInNewWindow;

	@Inject
	private Boolean showImage;

	@Inject
	private String articleImagePath;

	@Inject
	private String articleImageAltI18n;

	@PostConstruct
	protected void init() {
		ResourceResolver resolver = resource.getResourceResolver();
		PageManager pageManager = resolver.adaptTo(PageManager.class);

		if ("automatic".equals(contentType)) {
			articleTitle = "";
			vanityDescriptionI18n = "";
			ctaTexti18nKey = "";
			openInNewWindow = false;

			Page landingPage = pageManager.getPage(articlePath);
			if (landingPage != null) {
				Resource jcrContentResource = landingPage.getContentResource();
				LandingPageModel landingPageModel = jcrContentResource.adaptTo(LandingPageModel.class);
				if (landingPageModel != null) {
					articleTitle = landingPageModel.getTitle();
					vanityDescriptionI18n = landingPageModel.getVanityDescription();
					ctaTexti18nKey = landingPageModel.getCtaTexti18nKey();
					openInNewWindow = landingPageModel.isOpenInNewWindow();
					showImage = landingPageModel.getShowImage();
					articleImagePath = landingPageModel.getArticleImagePath();
					articleImageAltI18n = landingPageModel.getArticleImageAltI18n();
				}
			}

		}
	}

	public Resource getResource() {
		return resource;
	}

	public String getArticleTitle() {
		return articleTitle;
	}

	public String getVanityDescriptionI18n() {
		return vanityDescriptionI18n;
	}

	public String getCtaTexti18nKey() {
		return ctaTexti18nKey;
	}

	public Boolean isOpenInNewWindow() {
		return openInNewWindow;
	}

	public String getContentType() {
		return contentType;
	}

	public String getArticlePath() {
		return LinkUtils.sanitizeLink(articlePath);
	}

	public Boolean getOpenInNewWindow() {
		return openInNewWindow;
	}

	public Boolean getShowImage() {
		return showImage;
	}

	public String getArticleImagePath() {
		return articleImagePath;
	}

	public String getArticleImageAltI18n() {
		return articleImageAltI18n;
	}
}
