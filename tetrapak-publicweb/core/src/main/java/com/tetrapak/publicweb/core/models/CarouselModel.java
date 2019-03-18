package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.publicweb.core.beans.PracticeLineBean;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CarouselModel {
	
	private static final Logger log = LoggerFactory.getLogger(CarouselModel.class);

	@Self
	private Resource resource;

	@Inject
	private String titleI18n;

	@Inject
	private String titleAlignment;

	@Inject
	private Boolean showBox;
	
	@Inject
	private String[] tabDetails;
	
	private List<PracticeLineBean> tabs = new ArrayList<>();

	@PostConstruct
	protected void init() {
		setTabDeatils(tabDetails, resource);
	}

	private void setTabDeatils(String[] tabDetails, Resource resource) {
		ResourceResolver resolver = resource.getResourceResolver();
		PageManager pageManager = resolver.adaptTo(PageManager.class);

		@SuppressWarnings("deprecation")
		JSONObject jObj;
		try {
			if (tabDetails == null) {
				log.error("Tab Details is NULL");
				return;
			}

			if (tabDetails != null) {
				for (int i = 0; i < tabDetails.length; i++) {
					jObj = new JSONObject(tabDetails[i]);
					PracticeLineBean bean = new PracticeLineBean();

					String contentType = "";
					String socialMediaLinkPath = "";
					if (jObj.has("contentType")) {
						contentType = jObj.getString("contentType");
					}
					
					if ("automatic".equals(contentType)) {
						String articlePath = "";
						if (jObj.has("articlePath")) {
							articlePath = jObj.getString("articlePath");
						}
						
						Page landingPage = pageManager.getPage(articlePath);
						if (landingPage != null) {
							Resource jcrContentResource = landingPage.getContentResource();
							LandingPageModel landingPageModel = jcrContentResource.adaptTo(LandingPageModel.class);
							if (landingPageModel != null) {
								bean.setArticleTitle(landingPageModel.getTitle());
								bean.setVanityDescriptionI18n(landingPageModel.getVanityDescription());
								bean.setCtaTexti18nKey(landingPageModel.getCtaTexti18nKey());
								bean.setOpenInNewWindow(landingPageModel.isOpenInNewWindow() != null ? landingPageModel.isOpenInNewWindow() : false);
								bean.setShowImage(landingPageModel.getShowImage() != null ? landingPageModel.getShowImage() : false);
								bean.setArticleImagePath(landingPageModel.getArticleImagePath());
								bean.setArticleImageAltI18n(landingPageModel.getArticleImageAltI18n());
							}
						}

					} else {
					
						if (jObj.has("articleTitle")) {
							bean.setArticleTitle(jObj.getString("articleTitle"));
						}
						
						if (jObj.has("vanityDescriptionI18n")) {
							bean.setVanityDescriptionI18n(jObj.getString("vanityDescriptionI18n"));
						}
						
						if (jObj.has("ctaTexti18nKey")) {
							bean.setCtaTexti18nKey(jObj.getString("ctaTexti18nKey"));
						}
						
						if (jObj.has("showImage")) {
							bean.setShowImage(Boolean.parseBoolean(jObj.getString("showImage")));
						}
						
						if (jObj.has("openInNewWindow")) {
							bean.setOpenInNewWindow(Boolean.parseBoolean(jObj.getString("openInNewWindow")));
						}
						
						if (jObj.has("articleImagePath")) {
							bean.setArticleImagePath(jObj.getString("articleImagePath"));
						}
						
						if (jObj.has("articleImageAltI18n")) {
							bean.setArticleImageAltI18n(jObj.getString("articleImageAltI18n"));
						}

					}
					
					tabs.add(bean);

				}
			}
		} catch (Exception e) {
			log.error("Exception while Multifield data {}", e.getMessage(), e);
		}
		
	}

	public String getTitleI18n() {
		return titleI18n;
	}

	public String getTitleAlignment() {
		return titleAlignment;
	}

	public Boolean getShowBox(){
		return showBox;
	}

	public List<PracticeLineBean> getTabs() {
		return tabs;
	}
	
}
