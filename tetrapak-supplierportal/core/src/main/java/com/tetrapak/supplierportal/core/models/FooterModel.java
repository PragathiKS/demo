package com.tetrapak.supplierportal.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;
import com.tetrapak.supplierportal.core.multifield.FooterLinkModel;
import com.tetrapak.supplierportal.core.utils.LinkUtil;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FooterModel {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(FooterModel.class);

	private static final String CONFIGURATION_PATH = "/jcr:content/root/responsivegrid/footerconfiguration";

	/** The request. */
	@SlingObject
	private SlingHttpServletRequest request;

	/** The footer links. */
	private List<FooterLinkModel> footerValidLinks = new ArrayList<>();
	
	private String linkLangPopup;

	/**
	 * Inits the.
	 */
	@PostConstruct
	protected void init() {
		final List<FooterLinkModel> footerLinks;
		LOGGER.debug("inside init method");

		final String path = SupplierPortalConstants.CONTENT_ROOT + CONFIGURATION_PATH;
		final Resource footerConfigurationResource = request.getResourceResolver().getResource(path);
		if (Objects.nonNull(footerConfigurationResource)) {
			final FooterConfigurationModel configurationModel = footerConfigurationResource
					.adaptTo(FooterConfigurationModel.class);
			if (Objects.nonNull(configurationModel)) {
				linkLangPopup = configurationModel.getLinkLangPopup();
				footerLinks = configurationModel.getFooterLinks();
				for (FooterLinkModel footerLink : footerLinks) {
					String validLink = LinkUtil.getValidLink(request.getResource(), footerLink.getLinkPath());
					footerLink.setLinkPath(validLink);
					footerValidLinks.add(footerLink);
				}

			}
		}
	}

	public List<FooterLinkModel> getFooterLinks() {
		return new ArrayList<>(footerValidLinks);
	}
	
	public String getLinkLangPopup() {
        return linkLangPopup;
    }
}