package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
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
import com.tetrapak.publicweb.core.beans.NavigationLinkBean;
import com.tetrapak.publicweb.core.utils.LinkUtils;

@Model(adaptables = Resource.class)
public class FooterModel {

	private static final Logger log = LoggerFactory.getLogger(FooterModel.class);

	@Self
	private Resource resource;

	private String ctaLabelI18n;
	private String socialMediaLinkedin;
	private String socialMediaFacebook;
	private String socialMediaTwitter;
	private String socialMediaYoutube;
	private List<NavigationLinkBean> footerNavigationLinks = new ArrayList<>();

	@PostConstruct
	protected void init() {
		InheritanceValueMap inheritanceValueMap1 = new HierarchyNodeInheritanceValueMap(resource);
		ctaLabelI18n = inheritanceValueMap1.getInherited("ctaLabelI18n", String.class);
		socialMediaLinkedin = inheritanceValueMap1.getInherited("socialMediaLinkedin", String.class);
		socialMediaFacebook = inheritanceValueMap1.getInherited("socialMediaFacebook", String.class);
		socialMediaTwitter = inheritanceValueMap1.getInherited("socialMediaTwitter", String.class);
		socialMediaYoutube = inheritanceValueMap1.getInherited("socialMediaYoutube", String.class);
		String[] footerNavLinks = inheritanceValueMap1.getInherited("footerNavigationLinks", String[].class);
		LinkUtils.setMultifieldNavLinkItems(footerNavLinks, footerNavigationLinks, log);
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

	public List<NavigationLinkBean> getFooterNavigationLinks() {
		return footerNavigationLinks;
	}

}
