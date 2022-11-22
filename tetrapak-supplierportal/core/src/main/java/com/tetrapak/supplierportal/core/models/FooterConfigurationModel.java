package com.tetrapak.supplierportal.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.tetrapak.supplierportal.core.multifield.FooterLinkModel;

/**
 * The Class FooterConfigurationModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FooterConfigurationModel {

	/** The request. */
	@Self
	private Resource resource;

	/** The Footer text. */
	@ValueMapValue
	private String copyrightText;

	/** The footer links. */
	@Inject
	private List<FooterLinkModel> footerLinks;

	/**
	 * Gets the Footer Text.
	 *
	 * @return the footerText
	 */
	public String getFooterText() {
		return copyrightText;
	}

	/**
	 * Gets the footer link.
	 *
	 * @return the footer link
	 */
	public List<FooterLinkModel> getFooterLinks() {
		final List<FooterLinkModel> lists = new ArrayList<>();
		if (Objects.nonNull(footerLinks)) {
			lists.addAll(footerLinks);
		}
		return lists;

	}

}
