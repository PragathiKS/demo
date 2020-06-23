
package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.servlethelpers.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.publicweb.core.services.PardotService;
import com.tetrapak.publicweb.core.services.impl.PardotServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;


public class BusinessInquiryModelTest {

    /** The context. */
    @Rule
    public AemContext context = new AemContext();

    /** The Constant TEST_RESOURCE_CONTENT. */
    private static final String TEST_RESOURCE_CONTENT = "/businessinquiryform/test-content.json";
    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/publicweb/gb";

    private static final String CONTACT_US_CONTENT_ROOT = "/content/tetrapak/publicweb/gb/en/contact-us";
    /** The model class. */
    Class<BusinessInquiryModel> modelClass = BusinessInquiryModel.class;

    /** The request. */
    private final MockSlingHttpServletRequest request = context.request();

    /** The model. */
    private BusinessInquiryModel model;

    private PardotService pardotService;

    /**
     * The Constant PXP_FEATURES.
     */
    private static final String RESOURCE = CONTACT_US_CONTENT_ROOT + "/jcr:content/businessinquiryform";

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
        pardotService = new PardotServiceImpl();
        context.load().json(TEST_RESOURCE_CONTENT, TEST_CONTENT_ROOT);

        context.addModelsForClasses(modelClass);
        context.registerService(PardotService.class, pardotService);
        // context.registerInjectActivateService(countryDetailService);
        final Map<String, Object> pardotConfig = new HashMap<>();
        pardotConfig.put("pardotBusinessInquiryServiceUrl",
                "http://pardotURL");
        MockOsgi.activate(context.getService(PardotService.class), context.bundleContext(), pardotConfig);

        request.setPathInfo(CONTACT_US_CONTENT_ROOT);
        request.setResource(context.currentResource(RESOURCE));
        model = request.adaptTo(modelClass);

    }

    /**
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testDailogValues() throws Exception {
        assertEquals("Form", "Business Enquiry", model.getFormConfig().getHeading());
        assertEquals("Form", "title", model.getAnchorTitle());
        assertEquals("Form", "test01", model.getAnchorId());
        assertEquals("Form", "Thank you for your request", model.getFormConfig().getThankyouHeading());
        assertEquals("Form", "/content/dam/tetrapak/publicweb/contactus.PNG", model.getFormConfig().getThankyouImage());
        assertEquals("Form", "Thank you", model.getFormConfig().getThankyouImageAltText());
        assertEquals("Form", "We will get back to you as soon as possible",
                model.getFormConfig().getThankyouDescriptionText());
        assertEquals("Form",
                "I agree that the information I have provided will only be used in accordance with Tetra Pak privacy policy.",
                model.getConsentConfig().getPrivacyPolicy());
        assertEquals("Form", "/content/dam/tetrapak/publicweb/contactus.PNG", model.getFormConfig().getImage());
        assertEquals("Form", "Contact us", model.getFormConfig().getAlt());
        assertEquals("Form", "Description", model.getFormConfig().getDescriptionText());
        assertEquals("Form", "grayscale-white", model.getPwTheme());
        assertEquals("Form",
                "/content/tetrapak/publicweb/gb/en/contact-us/jcr:content/businessinquiryform.pardotbusinessenquiry.json",
                model.getApiUrl());
        assertEquals("Form", "Marketing Consent", model.getConsentConfig().getMarketingConsent());
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
