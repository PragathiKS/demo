package com.tetrapak.publicweb.core.services;

import com.tetrapak.publicweb.core.beans.ContactUs;
import com.tetrapak.publicweb.core.beans.ContactUsResponse;

public interface ContactUsMailService {

    ContactUsResponse sendEmailForNotification(ContactUs contactUs, String logo, String[] mailAddresses);

}
