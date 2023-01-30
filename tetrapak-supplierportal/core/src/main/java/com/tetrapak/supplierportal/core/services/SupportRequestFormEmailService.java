package com.tetrapak.supplierportal.core.services;

import com.tetrapak.supplierportal.core.models.SupportRequestFormBean;

public interface SupportRequestFormEmailService {
	/**
	 * Send email for notification.
	 *
	 * @param SupportRequestFormBean
	 * @param mailAddresses          the mail addresses
	 * @return
	 */
	public void sendEmailForNotification(SupportRequestFormBean SupportRequestFormBean, String mailAddresses);

}
