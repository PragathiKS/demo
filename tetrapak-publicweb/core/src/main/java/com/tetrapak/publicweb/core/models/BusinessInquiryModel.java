package com.tetrapak.publicweb.core.models;

import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.tetrapak.publicweb.core.services.PardotService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;
import com.tetrapak.publicweb.core.utils.PageUtil;

/**
 * The Class BusinessInquiryModel.
 *
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BusinessInquiryModel extends FormModel {

    /** The resource. */
    @Self
    private Resource resource;

    /** The pardot service. */
    @OSGiService
    private PardotService pardotService;

    private FormConfigModel formConfig;

    private FormConsentConfigModel consentConfig;

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

        final Resource formConfigResource = GlobalUtil.fetchConfigResource(resource,
                "/jcr:content/root/responsivegrid/businessinquiryformc");
        if (Objects.nonNull(formConfigResource))
            this.formConfig = formConfigResource.adaptTo(FormConfigModel.class);

        final Resource consentConfigResource = GlobalUtil.fetchConfigResource(resource,
                "/jcr:content/root/responsivegrid/formconsenttextsconf");
        if (Objects.nonNull(consentConfigResource))
            this.consentConfig = consentConfigResource.adaptTo(FormConsentConfigModel.class);
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

    /**
     * Gets the form config.
     *
     * @return the form config
     */
    public FormConfigModel getFormConfig() {
        return formConfig;
    }

    /**
     * Gets the consent config.
     *
     * @return the consent config
     */
    public FormConsentConfigModel getConsentConfig() {
        return consentConfig;
    }

}
