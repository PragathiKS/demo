package com.tetrapak.supplierportal.core.services.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import com.tetrapak.supplierportal.core.services.UrlService;

/**
 * Impl class for URL Service
 *
 * @author sunmanak (Sunil Kumar Yadav)
 */
@Component(immediate = true, service = UrlService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = UrlServiceImpl.UrlServiceConfig.class)
public class UrlServiceImpl implements UrlService {

	private UrlServiceConfig config;

	@ObjectClassDefinition(name = "SupplierPortal Fonts And Images URL Service Configuration", description = "URL Service Configuration for fonts and images")
	@interface UrlServiceConfig {

		/**
		 * Returns fonts URL
		 *
		 * @return fonts URL
		 */
		@AttributeDefinition(name = "URL Service Fonts URL", description = "URL Service URL", type = AttributeType.STRING)
		String fontsUrl();

		/**
		 * Returns images URL
		 *
		 * @return images URL
		 */
		@AttributeDefinition(name = "URL Service Images URL", description = "URL Service URL", type = AttributeType.STRING)
		String imagesUrl();
	}

	/**
	 * activate method
	 *
	 * @param config URL Service configuration
	 */
	@Activate
	public void activate(UrlServiceConfig config) {

		this.config = config;
	}

	@Override
	public String getFontsUrl() {
		return config.fontsUrl();
	}

	@Override
	public String getImagesUrl() {
		return config.imagesUrl();
	}

}
