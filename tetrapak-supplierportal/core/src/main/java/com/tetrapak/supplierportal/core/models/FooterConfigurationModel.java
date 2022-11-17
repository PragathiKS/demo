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

	/** The TetraPak path. */
	@ValueMapValue
	private String tetraPakLabel;

	/** The TetraPak link. */
	@ValueMapValue
	private String tetraPakLink;

	/** The Footer text. */
	@ValueMapValue
	private String footerText;

	/** The footer links. */
	@Inject
	private List<FooterLinkModel> footerLinks;

	/**
	 * Gets the TetraPak Label.
	 *
	 * @return the tetraPakLabel
	 */
	public String getTetraPakLabel() {
		return tetraPakLabel;
	}

	/**
	 * Gets the TetraPak Link.
	 *
	 * @return the tetraPakLink
	 */
	public String getTetraPakLink() {
		return tetraPakLink;
	}

	/**
	 * Gets the Footer Text.
	 *
	 * @return the footerText
	 */
	public String getFooterText() {
		return footerText;
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
