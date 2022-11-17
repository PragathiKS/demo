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

import com.tetrapak.supplierportal.core.multifield.FooterLinkModel;
import com.tetrapak.supplierportal.core.utils.LinkUtils;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FooterModel {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(FooterModel.class);

	/** The request. */
	@SlingObject
	private SlingHttpServletRequest request;

	/** The tetra pak label. */
	private String tetraPakLabel;

	/** The tetra pak link. */
	private String tetraPakLink;

	/** The footer links. */
	private List<FooterLinkModel> footerLinksSanitized = new ArrayList<>();

	/** The footer copyright text */
	private String footerText;

	/**
	 * Inits the.
	 */
	@PostConstruct
	protected void init() {
		final List<FooterLinkModel> footerLinks;
		LOGGER.debug("inside init method");
		final String rootPath = LinkUtils.getRootPath(request.getPathInfo());
		final String path = rootPath + "/jcr:content/root/responsivegrid/footerconfiguration";
		final Resource footerConfigurationResource = request.getResourceResolver().getResource(path);
		if (Objects.nonNull(footerConfigurationResource)) {
			final FooterConfigurationModel configurationModel = footerConfigurationResource
					.adaptTo(FooterConfigurationModel.class);
			if (Objects.nonNull(configurationModel)) {
				tetraPakLabel = configurationModel.getTetraPakLabel();
				tetraPakLink = LinkUtils.sanitizeLink(configurationModel.getTetraPakLink(), request);
				footerText = configurationModel.getFooterText();
				footerLinks = configurationModel.getFooterLinks();
				for (FooterLinkModel footerLink : footerLinks) {
					String sanitizedPath = LinkUtils.sanitizeLink(footerLink.getLinkPath(), request);
					footerLink.setLinkPath(sanitizedPath);
					footerLinksSanitized.add(footerLink);
				}

			}
		}
	}

	/**
	 * Gets the TetraPak Title.
	 *
	 * @return the tetraPakLabel
	 */
	public String getTetraPakLabel() {
		return tetraPakLabel;
	}

	/**
	 * Gets the TetraPak link.
	 *
	 * @return the tetraPakLink
	 */
	public String getTetraPakLink() {
		return tetraPakLink;
	}

	/**
	 * Gets the Footer Text.
	 *
	 * @return the Footer Text
	 */
	public String getFooterText() {
		return footerText;
	}

	public List<FooterLinkModel> getFooterLinks() {
		return new ArrayList<>(footerLinksSanitized);
	}
}