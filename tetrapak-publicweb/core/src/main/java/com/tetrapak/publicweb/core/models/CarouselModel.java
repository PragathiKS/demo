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
import com.tetrapak.publicweb.core.beans.BestPracticeLineBean;
import com.tetrapak.publicweb.core.utils.LinkUtils;

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
    private String pwTheme;

    @Inject
    private String pwButtonTheme;

    @Inject
    private String pwPadding;

    private Integer carouselId = (int )(Math.random() * 1000 + 1);

	private List<BestPracticeLineBean> tabs = new ArrayList<>();

	@PostConstruct
	protected void init() {
		ResourceResolver resolver = resource.getResourceResolver();
		PageManager pageManager = resolver.adaptTo(PageManager.class);

		Resource childResource = resource.getChild("tabDetails");
		if (null != childResource) {
			log.info("This is the child resource : " + childResource.getPath());
			Iterator<Resource> itr = childResource.listChildren();
			while (itr.hasNext()) {
				Resource res = itr.next();
				ValueMap valueMap = res.getValueMap();

				BestPracticeLineBean bean = new BestPracticeLineBean();

				String contentType = "";
				if (valueMap.containsKey("contentType")) {
					contentType = valueMap.get("contentType", String.class);
				}

				if ("automatic".equals(contentType)) {
					String practicePath = "";
					if (valueMap.containsKey("practicePath")) {
						practicePath = valueMap.get("practicePath", String.class);
					}

					Page landingPage = pageManager.getPage(practicePath);
					if (landingPage != null) {
						Resource jcrContentResource = landingPage.getContentResource();
						BestPracticeLinePageModel practiceLinePageModel = jcrContentResource.adaptTo(BestPracticeLinePageModel.class);
						if (practiceLinePageModel != null) {
							bean.setPracticeTitle(practiceLinePageModel.getTitle());
							bean.setVanityDescription(practiceLinePageModel.getVanityDescription());
							bean.setCtaTexti18nKey(practiceLinePageModel.getCtaTexti18nKey());
							bean.setPracticeImagePath(practiceLinePageModel.getPracticeImagePath());
							bean.setPracticeImageAltI18n(practiceLinePageModel.getPracticeImageAltI18n());
							bean.setPracticePath(LinkUtils.sanitizeLink(practicePath));
						}
					}
				} else {

					if (valueMap.containsKey("practiceTitle")) {
						bean.setPracticeTitle(valueMap.get("practiceTitle", String.class));
					}

					if (valueMap.containsKey("vanityDescription")) {
						bean.setVanityDescription(valueMap.get("vanityDescription", String.class));
					}

					if (valueMap.containsKey("ctaTexti18nKey")) {
						bean.setCtaTexti18nKey(valueMap.get("ctaTexti18nKey", String.class));
					}

					if (valueMap.containsKey("linkTarget")) {
						bean.setLinkTarget(valueMap.get("linkTarget", String.class));
					}

					if (valueMap.containsKey("practiceImagePath")) {
						bean.setPracticeImagePath(valueMap.get("practiceImagePath", String.class));
					}

					if (valueMap.containsKey("practiceImageAltI18n")) {
						bean.setPracticeImageAltI18n(valueMap.get("practiceImageAltI18n", String.class));
					}

					if (valueMap.containsKey("practicePath")) {
						String path = valueMap.get("practicePath", String.class);
						bean.setPracticePath(LinkUtils.sanitizeLink(path));
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

	public List<BestPracticeLineBean> getTabs() {
		return tabs;
	}

}
