/**
 *
 */
package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.publicweb.core.services.CountryDetailService;
import com.tetrapak.publicweb.core.services.impl.CountryDetailServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 *
 */
public class BusinessInquiryModelTest {

    /** The context. */
    @Rule
    public AemContext context = new AemContext();

    /** The Constant TEST_RESOURCE_CONTENT. */
    private static final String TEST_RESOURCE_CONTENT = "/businessinquiryform/test-content.json";
    private static final String CONTACT_US_CONTENT_ROOT = "/content/tetrapak/publicweb/lang-master/en";
    /** The model class. */
    Class<BusinessInquiryFormModel> modelClass = BusinessInquiryFormModel.class;

    /** The model. */
    private BusinessInquiryFormModel model;

    private CountryDetailService countryDetailService;

    /**
     * The Constant PXP_FEATURES.
     */
    private static final String RESOURCE = CONTACT_US_CONTENT_ROOT + "/jcr:content/businessinquiryform";

    /** The resource. */
    private Resource resource;

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
        countryDetailService = new CountryDetailServiceImpl();
        context.load().json(TEST_RESOURCE_CONTENT, CONTACT_US_CONTENT_ROOT);

        context.addModelsForClasses(modelClass);
        context.registerService(CountryDetailService.class, countryDetailService);
        // context.registerInjectActivateService(countryDetailService);
        final Map<String, Object> countryConfig = new HashMap<>();
        countryConfig.put("getCountriesContentFragmentRootPath",
                "/content/dam/tetrapak/publicweb/cfm/countries");
        MockOsgi.activate(context.getService(CountryDetailService.class), context.bundleContext(), countryConfig);

        resource = context.currentResource(RESOURCE);
        model = resource.adaptTo(modelClass);

    }

    /**
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testDailogValues() throws Exception {
        assertEquals("ContactUs", "Contact info", model.getContactInfoText());
        assertEquals("ContactUs", "Business Enquiry", model.getHeading());
        assertEquals("ContactUs", "title", model.getAnchorTitle());
        assertEquals("ContactUs", "test01", model.getAnchorId());
        assertEquals("ContactUs", "Thank you for your request", model.getThankyouHeading());
        assertEquals("ContactUs", "/content/dam/tetrapak/publicweb/contactus.PNG", model.getThankyouImage());
        assertEquals("ContactUs", "Thank you", model.getThankyouImageAltText());
        assertEquals("ContactUs", "We will get back to you as soon as possible", model.getThankyouDescriptionText());
        assertEquals("ContactUs",
                "I agree that the information I have provided will only be used in accordance with Tetra Pak privacy policy.",
                model.getPrivacyPolicy());
        assertEquals("ContactUs", "/content/dam/tetrapak/publicweb/contactus.PNG", model.getImage());
        assertEquals("ContactUs", "Contact us", model.getAlt());
        assertEquals("ContactUs", "Description", model.getDescriptionText());
        assertEquals("ContactUs", "Purpose of contact", model.getPurposeOfContactText());
        assertEquals("ContactUs", "Company Information", model.getCompanyInfoText());
        assertEquals("ContactUs", "Summary", model.getSummaryText());
        assertEquals("ContactUs", "grayscale-white", model.getPwTheme());
        assertEquals("ContactUs", "API Url", model.getApiUrl());
        assertEquals("ContactUs", "Business Area", model.getBusinessAreaText());
        assertEquals("ContactUs", "Marketing Consent", model.getMarketingConsent());
       }


}
