package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
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
		ResourceResolver resolver = resource.getResourceResolver();
		PageManager pageManager = resolver.adaptTo(PageManager.class);

		Resource childResource = resource.getChild("tabDetails");
		log.info("This is the child resource : " + childResource.getPath());

		if (null != childResource) {
			Iterator<Resource> itr = childResource.listChildren();
			while (itr.hasNext()) {
				Resource res = itr.next();
				ValueMap valueMap = res.getValueMap();

				PracticeLineBean bean = new PracticeLineBean();

				String contentType = "";
				if (valueMap.containsKey("contentType")) {
					contentType = valueMap.get("contentType", String.class);
				}

				if ("automatic".equals(contentType)) {
					String articlePath = "";
					if (valueMap.containsKey("articlePath")) {
						articlePath = valueMap.get("articlePath", String.class);
					}

					Page landingPage = pageManager.getPage(articlePath);
					if (landingPage != null) {
						Resource jcrContentResource = landingPage.getContentResource();
						LandingPageModel landingPageModel = jcrContentResource.adaptTo(LandingPageModel.class);
						if (landingPageModel != null) {
							bean.setArticleTitle(landingPageModel.getTitle());
							bean.setVanityDescriptionI18n(landingPageModel.getVanityDescription());
							bean.setCtaTexti18nKey(landingPageModel.getCtaTexti18nKey());
							bean.setOpenInNewWindow(
									landingPageModel.isOpenInNewWindow() != null ? landingPageModel.isOpenInNewWindow()
											: false);
							bean.setShowImage(
									landingPageModel.getShowImage() != null ? landingPageModel.getShowImage() : false);
							bean.setArticleImagePath(landingPageModel.getArticleImagePath());
							bean.setArticleImageAltI18n(landingPageModel.getArticleImageAltI18n());
						}
					}
				} else {

					if (valueMap.containsKey("articleTitle")) {
						bean.setArticleTitle(valueMap.get("articleTitle", String.class));
					}

					if (valueMap.containsKey("vanityDescriptionI18n")) {
						bean.setVanityDescriptionI18n(valueMap.get("vanityDescriptionI18n", String.class));
					}

					if (valueMap.containsKey("ctaTexti18nKey")) {
						bean.setCtaTexti18nKey(valueMap.get("ctaTexti18nKey", String.class));
					}

					if (valueMap.containsKey("showImage")) {
						bean.setShowImage(Boolean.parseBoolean(valueMap.get("showImage", String.class)));
					}

					if (valueMap.containsKey("openInNewWindow")) {
						bean.setOpenInNewWindow(Boolean.parseBoolean(valueMap.get("openInNewWindow", String.class)));
					}

					if (valueMap.containsKey("articleImagePath")) {
						bean.setArticleImagePath(valueMap.get("articleImagePath", String.class));
					}

					if (valueMap.containsKey("articleImageAltI18n")) {
						bean.setArticleImageAltI18n(valueMap.get("articleImageAltI18n", String.class));
					}

				}

				tabs.add(bean);

			}
		}

	}

	public String getTitleI18n() {
		return titleI18n;
	}

	public String getTitleAlignment() {
		return titleAlignment;
	}

	public Boolean getShowBox() {
		return showBox;
	}

	public List<PracticeLineBean> getTabs() {
		return tabs;
	}
	
}
