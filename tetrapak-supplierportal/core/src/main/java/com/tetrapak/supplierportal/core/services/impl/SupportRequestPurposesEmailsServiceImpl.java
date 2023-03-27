package com.tetrapak.supplierportal.core.services.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;

import com.tetrapak.supplierportal.core.services.SupportRequestPurposesEmailsService;
import com.tetrapak.supplierportal.core.services.config.SupportRequestPurposesEmailsConfig;

@Component(immediate = true, service = SupportRequestPurposesEmailsService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = SupportRequestPurposesEmailsConfig.class)
public class SupportRequestPurposesEmailsServiceImpl implements SupportRequestPurposesEmailsService {

	private SupportRequestPurposesEmailsConfig config;

	@Activate
	public void activate(SupportRequestPurposesEmailsConfig config) {

		this.config = config;
	}

	@Override
	public String[] getOnBoardingMaintananceEmail() {
		return config.getOBoardingMaintananceEmail();
	}

	@Override
	public String[] getSourcingContractingEmail() {
		return config.getSourcingContractingEmail();
	}

	@Override
	public String[] getCataloguesEmail() {
		return config.getCataloguesEmail();
	}

	@Override
	public String[] getOtherEmail() {
		return config.getOtherEmail();
	}

}
