package com.tetrapak.supplierportal.core.services;

import com.google.gson.JsonObject;
import com.tetrapak.supplierportal.core.models.SupportRequestFormBean;

public interface SupportRequestFormEmailService {
	/**
	 * Send email for notification.
	 *
	 * @param SupportRequestFormBean
	 * @param mailAddresses          the mail addresses
	 * @return
	 */
	public JsonObject sendEmailForNotification(SupportRequestFormBean SupportRequestFormBean, String mailAddresses);

}
