package com.tetrapak.supplierportal.core.models;

import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;
import com.tetrapak.supplierportal.core.services.UserPreferenceService;
import com.tetrapak.supplierportal.core.utils.GlobalUtil;
import com.tetrapak.supplierportal.core.utils.LinkUtil;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ChatbotModel {

	/** The request. */
	@SlingObject
	private SlingHttpServletRequest request;

	@OSGiService
	private UserPreferenceService userPreferenceService;

	private static final String CONFIGURATION_PATH = "/jcr:content/root/responsivegrid/footerconfiguration";

	/** The chatbot link. */
	private String chatbotLink;

	/** The chatbot text. */
	private String chatbotText;

	@PostConstruct
	public void init() {
		String language = GlobalUtil.getSelectedLanguage(request, userPreferenceService);
		final String path = SupplierPortalConstants.SUPPLIER_PATH + language + CONFIGURATION_PATH;
		final Resource footerConfigurationResource = request.getResourceResolver().getResource(path);
		if (Objects.nonNull(footerConfigurationResource)) {
			final FooterConfigurationModel configurationModel = footerConfigurationResource
					.adaptTo(FooterConfigurationModel.class);
			if (Objects.nonNull(configurationModel)) {
				chatbotLink = LinkUtil.getValidLink(request.getResource(), configurationModel.getChatbotLink());
				chatbotText = configurationModel.getChatbotText();
			}
		}
	}

	/**
	 * Gets the chatbot link.
	 *
	 * @return the chatbot link
	 */
	public String getChatbotLink() {
		return chatbotLink;
	}

	/**
	 * Gets the chatbot text.
	 *
	 * @return the chatbot text
	 */
	public String getChatbotText() {
		return chatbotText;
	}

}
