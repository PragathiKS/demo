package com.tetrapak.supplierportal.core.services;

import java.util.List;
import java.util.Map;

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
	public JsonObject sendEmailForNotification(SupportRequestFormBean supportRequestFormBean, String[] mailAddresses,
			List<Map<String, String>> files);

}
