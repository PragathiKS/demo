package com.tetrapak.customerhub.core.models;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BaseMaintenanceModel {

	@Inject
	private String statusLabel;
	@Inject
	private String serviceAgreementLabel;
	@Inject
	private String plannedDurationLabel;
	@Inject
	private String plannedStartLabel;
	@Inject
	private String plannedFinishedLabel;

	public String getStatusLabel() {
		return statusLabel;
	}

	public String getServiceAgreementLabel() {
		return serviceAgreementLabel;
	}

	public String getPlannedDurationLabel() {
		return plannedDurationLabel;
	}

	public String getPlannedStartLabel() {
		return plannedStartLabel;
	}

	public String getPlannedFinishedLabel() {
		return plannedFinishedLabel;
	}

}
