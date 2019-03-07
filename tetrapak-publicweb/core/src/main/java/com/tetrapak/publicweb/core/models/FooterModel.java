package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;
import com.tetrapak.publicweb.core.beans.FooterBean;

@Model(adaptables = Resource.class)
public class FooterModel {

	private static final Logger log = LoggerFactory.getLogger(FooterModel.class);

	@Self
	private Resource resource;

	private String imagePath;
	private String imageAltI18n;
	private String imageLink;
	private String imageTitleI18n;
	private String ctaLabelI18n;
	private String linkPath;
	private String socialMediaLinkedin;
	private String socialMediaFacebook;
	private String socialMediaTwitter;
	private String socialMediaYoutube;
	private List<FooterBean> footerNavigationLinks = new ArrayList<>();

	@PostConstruct
	protected void init() {
		InheritanceValueMap inheritanceValueMap1 = new HierarchyNodeInheritanceValueMap(resource);
		imagePath = inheritanceValueMap1.getInherited("imagePath", String.class);
		imageAltI18n = inheritanceValueMap1.getInherited("imageAltI18n", String.class);
		imageLink = inheritanceValueMap1.getInherited("imageLink", String.class);
		imageTitleI18n = inheritanceValueMap1.getInherited("imageTitleI18n", String.class);
		ctaLabelI18n = inheritanceValueMap1.getInherited("ctaLabelI18n", String.class);
		linkPath = inheritanceValueMap1.getInherited("linkPath", String.class);
		socialMediaLinkedin = inheritanceValueMap1.getInherited("socialMediaLinkedin", String.class);
		socialMediaFacebook = inheritanceValueMap1.getInherited("socialMediaFacebook", String.class);
		socialMediaTwitter = inheritanceValueMap1.getInherited("socialMediaTwitter", String.class);
		socialMediaYoutube = inheritanceValueMap1.getInherited("socialMediaYoutube", String.class);
		String[] footerNavLinks = inheritanceValueMap1.getInherited("footerNavigationLinks", String[].class);
		setMultiFieldItems(footerNavLinks);
	}

	/**
	 * Method to get Multi field data
	 * 
	 * @return submenuItems
	 */
	private void setMultiFieldItems(String[] footerNavLinks) {
		@SuppressWarnings("deprecation")
		JSONObject jObj;
		try {
			if (footerNavLinks == null) {
				log.error("footerNavLinks is NULL");
			}

			if (footerNavLinks != null) {
				for (int i = 0; i < footerNavLinks.length; i++) {
					jObj = new JSONObject(footerNavLinks[i]);
					FooterBean bean = new FooterBean();
					log.info("*^*^*^*^*^*^^*^*^*^*^*^^*^*^*^*");
					String linkTextI18n = jObj.getString("linkTextI18n");
					String linkTooltipI18n = jObj.getString("linkTooltipI18n");
					String linkPath = jObj.getString("linkPath");
					String targetBlank = jObj.getString("targetBlank");
					log.info("linkTextI18n : " + linkTextI18n);
					log.info("linkTooltipI18n : " + linkTooltipI18n);
					log.info("linkPath : " + linkPath);
					log.info("targetBlank : " + targetBlank);
					bean.setLinkTextI18n(linkTextI18n);
					bean.setLinkTooltipI18n(linkTooltipI18n);
					bean.setLinkPath(linkPath);
					bean.setTargetBlank(targetBlank);
					footerNavigationLinks.add(bean);

				}
			}
		} catch (Exception e) {
			log.error("Exception while Multifield data {}", e.getMessage(), e);
		}

	}

	public String getImagePath() {
		return imagePath;
	}

	public String getImageAltI18n() {
		return imageAltI18n;
	}

	public String getImageLink() {
		return imageLink;
	}

	public String getImageTitleI18n() {
		return imageTitleI18n;
	}

	public String getLinkPath() {
		return linkPath;
	}

	public String getCtaLabelI18n() {
		return ctaLabelI18n;
	}

	public String getSocialMediaLinkedin() {
		return socialMediaLinkedin;
	}

	public String getSocialMediaFacebook() {
		return socialMediaFacebook;
	}

	public String getSocialMediaTwitter() {
		return socialMediaTwitter;
	}

	public String getSocialMediaYoutube() {
		return socialMediaYoutube;
	}

	public List<FooterBean> getFooterNavigationLinks() {
		Iterator<FooterBean> abc = footerNavigationLinks.iterator();
		while(abc.hasNext()) {
			log.info(abc.next().getLinkPath());
		}
		return footerNavigationLinks;
	}

}
