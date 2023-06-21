package com.tetrapak.publicweb.core.models;

import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.tetrapak.publicweb.core.utils.LinkUtils;

/**
 * The Class ContactUsEnvelopeModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContactUsEnvelopeModel {

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The contact us link. */
    private String contactUsLink;

    /** The contact us alt text. */
    private String contactUsAltText;

    @PostConstruct
    public void init() {
        final String rootPath = LinkUtils.getRootPath(request.getPathInfo());
        final String path = rootPath + "/jcr:content/root/responsivegrid/headerconfiguration";
        final Resource headerConfigurationResource = request.getResourceResolver().getResource(path);
        if (Objects.nonNull(headerConfigurationResource)) {
            final HeaderConfigurationModel configurationModel = headerConfigurationResource
                    .adaptTo(HeaderConfigurationModel.class);
            if (Objects.nonNull(configurationModel)) {
                contactUsLink = LinkUtils.sanitizeLink(configurationModel.getContactLink(), request);
                contactUsAltText = configurationModel.getContactText();
            }
        }
    }

    /**
     * Gets the contact link.
     *
     * @return the contact link
     */
    public String getContactUsLink() {
        return contactUsLink;
    }

    /**
     * Gets the contact text.
     *
     * @return the contact text
     */
    public String getContactUsAltText() {
        return contactUsAltText;
    }

}
