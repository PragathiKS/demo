
package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.publicweb.core.services.PardotService;
import com.tetrapak.publicweb.core.services.impl.PardotServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;


public class SubscriptionFormModelTest {

    /** The context. */
    @Rule
    public AemContext context = new AemContext();

    /** The Constant TEST_RESOURCE_CONTENT. */
    private static final String TEST_RESOURCE_CONTENT = "/subscriptionform/test-content.json";
    private static final String CONTACT_US_CONTENT_ROOT = "/content/tetrapak/publicweb/gb";
    /** The model class. */
    Class<SubscriptionFormModel> modelClass = SubscriptionFormModel.class;

    /** The model. */
    private SubscriptionFormModel model;

    /** The pardot service. */
    private PardotService pardotService;

    /** The Constant RESOURCE. */
    private static final String RESOURCE = CONTACT_US_CONTENT_ROOT
            + "/en/subscriptionform/jcr:content/subscriptionform";

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
        pardotService = new PardotServiceImpl();
        context.load().json(TEST_RESOURCE_CONTENT, CONTACT_US_CONTENT_ROOT);

        context.addModelsForClasses(modelClass);
        context.registerService(PardotService.class, pardotService);
        // context.registerInjectActivateService(countryDetailService);
        final Map<String, Object> pardotConfig = new HashMap<>();
        pardotConfig.put("pardotsubscriptionServiceUrl",
                "http://pardotURL");
        MockOsgi.activate(context.getService(PardotService.class), context.bundleContext(), pardotConfig);

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
        assertEquals("Form", "Business Enquiry", model.getFormConfig().getHeading());
        assertEquals("Form", "title", model.getAnchorTitle());
        assertEquals("Form", "test01", model.getAnchorId());
        assertEquals("Form", "Thank you for your request", model.getFormConfig().getThankyouHeading());
        assertEquals("Form", "We will get back to you as soon as possible",
                model.getFormConfig().getThankyouDescriptionText());
        assertEquals("Form",
                "I agree that the information I have provided will only be used in accordance with Tetra Pak privacy policy.",
                model.getConsentConfig().getPrivacyPolicy());
        assertEquals("Form", "Description", model.getFormConfig().getDescriptionText());
        assertEquals("Form", "grayscale-white", model.getPwTheme());
        assertEquals("Form",
                "/content/tetrapak/publicweb/gb/en/subscriptionform/jcr:content/subscriptionform.pardotsubscription.json",
                model.getApiUrl());
        assertEquals("Form", "Marketing Consent", model.getConsentConfig().getMarketingConsent());
       }


    /**
     * Test fetch language.
     */
    @Test
    public void testFetchLanguage() {
        assertEquals("en", model.getSiteLanguage());
    }


    /**
     * Test fetch country.
     */
    @Test
    public void testFetchCountry() {
        assertEquals("gb", model.getSiteCountry());

    }

}
