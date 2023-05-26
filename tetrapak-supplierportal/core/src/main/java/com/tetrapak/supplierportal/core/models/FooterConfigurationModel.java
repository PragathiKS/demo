package com.tetrapak.supplierportal.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.tetrapak.supplierportal.core.multifield.FooterLinkModel;

/**
 * The Class FooterConfigurationModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FooterConfigurationModel {

	/** The request. */
	@Self
	private Resource resource;

	/** The footer links. */
	@Inject
	private List<FooterLinkModel> footerLinks;
	
	@Inject
	private String changeLanguage;
	
	@Inject
	private String chatbotText;
	
	@Inject
	private String chatbotLink;
	

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
	
	 public String getChangeLanguage() {
	        return changeLanguage;
	    }

	 public String getChatbotText() {
	        return chatbotText;
	    }
	 public String getChatbotLink() {
	        return chatbotLink;
	    }
}
