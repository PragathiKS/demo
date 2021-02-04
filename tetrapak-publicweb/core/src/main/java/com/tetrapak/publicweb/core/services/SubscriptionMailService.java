package com.tetrapak.publicweb.core.services;

import java.util.List;

import org.apache.sling.api.resource.ResourceResolver;

import com.tetrapak.publicweb.core.beans.NewsEventBean;

/**
 * The Interface SubscriptionMailService.
 */
public interface SubscriptionMailService {

    /**
     * Send subscription email.
     *
     * @param newsEvent
     *            the news event
     * @param mailAddress
     *            the mail address
     * @param resolver
     *            the resolver
     * @return the string
     */
    String sendSubscriptionEmail(NewsEventBean newsEvent, List<String> mailAddress, ResourceResolver resolver);

}
