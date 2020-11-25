package com.tetrapak.publicweb.core.services;

import com.tetrapak.publicweb.core.beans.ContactUs;
import com.tetrapak.publicweb.core.beans.ContactUsResponse;

/**
 * The Interface ContactUsMailService.
 */
public interface ContactUsMailService {

    /**
     * Send email for notification.
     *
     * @param contactUs
     *            the contact us
     * @param logo
     *            the logo
     * @param mailAddresses
     *            the mail addresses
     * @return the contact us response
     */
    ContactUsResponse sendEmailForNotification(ContactUs contactUs, String logo, String[] mailAddresses);

}
