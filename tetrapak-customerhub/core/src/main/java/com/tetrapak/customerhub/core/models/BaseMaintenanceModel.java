package com.tetrapak.customerhub.core.models;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BaseMaintenanceModel {

	@Inject
	protected String statusLabel;
	@Inject
	protected String serviceAgreementLabel;
	@Inject
	protected String plannedDurationLabel;
	@Inject
	protected String plannedStartLabel;
	@Inject
	protected String plannedFinishedLabel;

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
