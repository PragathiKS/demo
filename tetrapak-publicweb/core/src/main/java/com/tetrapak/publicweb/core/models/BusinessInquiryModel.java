package com.tetrapak.publicweb.core.models;

import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.tetrapak.publicweb.core.services.PardotService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;
import com.tetrapak.publicweb.core.utils.PageUtil;

/**
 * The Class BusinessInquiryModel.
 *
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BusinessInquiryModel extends FormModel {

    /** The request. */
    @Self
    private SlingHttpServletRequest request;

    /** The resource. */
    @Self
    @Via("resource")
    private Resource resource;

    /** The pardot service. */
    @OSGiService
    private PardotService pardotService;

    private FormConfigModel formConfig;

    private FormConfigModel consentConfig;

    /**
     * The init method.
     */
    @PostConstruct
    protected void init() {
        setFormConfig();
    }

    /**
     * Sets the form configs.
     */
    public void setFormConfig() {

        final Resource formConfigResource = GlobalUtil.fetchConfigResource(request,
                "/jcr:content/root/responsivegrid/businessinquiryformc");
        if (Objects.nonNull(formConfigResource))
            this.formConfig = formConfigResource.adaptTo(FormConfigModel.class);

        final Resource consentConfigResource = GlobalUtil.fetchConfigResource(request,
                "/jcr:content/root/responsivegrid/formconsenttextsconf");
        if (Objects.nonNull(consentConfigResource))
            this.consentConfig = consentConfigResource.adaptTo(FormConfigModel.class);
    }

    public String getApiUrl() {
        return resource.getPath() + ".pardotbusinessenquiry.json";
    }

    /**
     * Gets the site language.
     *
     * @return the site language
     */
    public String getSiteLanguage() {
        return PageUtil.getLanguageCodeFromResource(resource);
    }

    /**
     * Gets the site country.
     *
     * @return the site country
     */
    public String getSiteCountry() {
        return PageUtil.getCountryCodeFromResource(resource);
    }

    public FormConfigModel getFormConfig() {
        return formConfig;
    }

    public FormConfigModel getConsentConfig() {
        return consentConfig;
    }

}
