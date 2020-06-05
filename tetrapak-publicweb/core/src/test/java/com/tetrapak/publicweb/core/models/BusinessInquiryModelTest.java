
package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.publicweb.core.services.PadrotService;
import com.tetrapak.publicweb.core.services.impl.PadrotServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;


public class BusinessInquiryModelTest {

    /** The context. */
    @Rule
    public AemContext context = new AemContext();

    /** The Constant TEST_RESOURCE_CONTENT. */
    private static final String TEST_RESOURCE_CONTENT = "/businessinquiryform/test-content.json";
    private static final String CONTACT_US_CONTENT_ROOT = "/content/tetrapak/publicweb/gb";
    /** The model class. */
    Class<BusinessInquiryModel> modelClass = BusinessInquiryModel.class;

    /** The model. */
    private BusinessInquiryModel model;

    private PadrotService padrotService;

    /**
     * The Constant PXP_FEATURES.
     */
    private static final String RESOURCE = CONTACT_US_CONTENT_ROOT + "/en/jcr:content/businessinquiryform";

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
        padrotService = new PadrotServiceImpl();
        context.load().json(TEST_RESOURCE_CONTENT, CONTACT_US_CONTENT_ROOT);

        context.addModelsForClasses(modelClass);
        context.registerService(PadrotService.class, padrotService);
        // context.registerInjectActivateService(countryDetailService);
        final Map<String, Object> padrotConfig = new HashMap<>();
        padrotConfig.put("padrotBusinessInquiryServiceUrl",
                "http://padrotURL");
        MockOsgi.activate(context.getService(PadrotService.class), context.bundleContext(), padrotConfig);

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
        assertEquals("Form", "Business Enquiry", model.getHeading());
        assertEquals("Form", "title", model.getAnchorTitle());
        assertEquals("Form", "test01", model.getAnchorId());
        assertEquals("Form", "Thank you for your request", model.getThankyouHeading());
        assertEquals("Form", "/content/dam/tetrapak/publicweb/contactus.PNG", model.getThankyouImage());
        assertEquals("Form", "Thank you", model.getThankyouImageAltText());
        assertEquals("Form", "We will get back to you as soon as possible", model.getThankyouDescriptionText());
        assertEquals("Form",
                "I agree that the information I have provided will only be used in accordance with Tetra Pak privacy policy.",
                model.getPrivacyPolicy());
        assertEquals("Form", "/content/dam/tetrapak/publicweb/contactus.PNG", model.getImage());
        assertEquals("Form", "Contact us", model.getAlt());
        assertEquals("Form", "Description", model.getDescriptionText());
        assertEquals("Form", "grayscale-white", model.getPwTheme());
        assertEquals("Form", "http://padrotURL", model.getApiUrl());
        assertEquals("Form", "Marketing Consent", model.getMarketingConsent());
       }

    @Test
    public void testFetchLanguage() {
        assertEquals("en", model.getSiteLanguage());
    }

    @Test
    public void testFetchCountry() {
        assertEquals("gb", model.getSiteCountry());

    }

}
