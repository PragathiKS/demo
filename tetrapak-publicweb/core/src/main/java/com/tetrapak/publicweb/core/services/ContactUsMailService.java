package com.tetrapak.publicweb.core.services;

import org.apache.sling.api.SlingHttpServletRequest;

import com.tetrapak.publicweb.core.beans.ContactUs;
import com.tetrapak.publicweb.core.beans.ContactUsResponse;

public interface ContactUsMailService {

    ContactUsResponse sendEmailForNotification(ContactUs contactUs, SlingHttpServletRequest request);

}
