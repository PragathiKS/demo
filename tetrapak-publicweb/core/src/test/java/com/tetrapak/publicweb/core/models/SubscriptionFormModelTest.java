package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.publicweb.core.services.CountryDetailService;
import com.tetrapak.publicweb.core.services.PardotService;
import com.tetrapak.publicweb.core.services.impl.CountryDetailServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class SubscriptionFormModelTest.
 */
public class SubscriptionFormModelTest {

    /** The context. */
    @Rule
    public AemContext context = new AemContext();

    /** The Constant TEST_RESOURCE_CONTENT. */
    private static final String TEST_RESOURCE_CONTENT = "/subscriptionform/test-content.json";
    
    /** The Constant SUBSCRIPTION_FORM_CONTENT_ROOT. */
    private static final String SUBSCRIPTION_FORM_CONTENT_ROOT = "/content/tetrapak/publicweb/gb";
    
    /** The Constant TEST_RESOURCE_CFM. */
    private static final String TEST_RESOURCE_CFM = "/contactus/test-countries-content.json";
    
    /** The Constant COUNTRIES_ROOT. */
    private static final String COUNTRIES_ROOT = "/content/dam/tetrapak/publicweb/cfm/countries";
    
    /** The model class. */
    Class<SubscriptionFormModel> modelClass = SubscriptionFormModel.class;

    /** The model. */
    private SubscriptionFormModel model;

    /** The pardot service. */
    private PardotService pardotService;

    /** The Constant RESOURCE. */
    private static final String RESOURCE = "/content/tetrapak/publicweb/gb/en/textimage/jcr:content/textimage";

    /** The resource. */
    private Resource resource;
    
    /** The country service. */
    private CountryDetailService countryService;

    /**
     * Sets the up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        context.load().json(TEST_RESOURCE_CONTENT, SUBSCRIPTION_FORM_CONTENT_ROOT);
        context.load().json(TEST_RESOURCE_CFM, COUNTRIES_ROOT);
        context.addModelsForClasses(modelClass);

        countryService = new CountryDetailServiceImpl();
        context.registerService(CountryDetailService.class, countryService);
        final Map<String, Object> countryConfig = new HashMap<>();
        countryConfig.put("getPardotCountriesCFRootPath", "/content/dam/tetrapak/publicweb/cfm/countries");
        MockOsgi.activate(context.getService(CountryDetailService.class), context.bundleContext(), countryConfig);

        MockSlingHttpServletRequest request = context.request();
        context.request().setPathInfo(RESOURCE);
        request.setResource(context.resourceResolver().getResource(RESOURCE));
        resource = context.currentResource(RESOURCE);
        model = request.adaptTo(modelClass);
    }
 
    /**
     * Test model not null.
     */
    @Test
    public void testModelNotNull() {
        assertNotNull("Model not null", model);
    }
    
    /**
     * Test fetch country.
     */
    @Test
    public void testFetchCountry() {
        assertEquals("gb", model.getSiteCountry());
    }
    
    /**
     * Test fetch language.
     */
    @Test
    public void testFetchLanguage() {
        assertEquals("en", model.getSiteLanguage());
    }
    
    /**
     * Test other getters.
     */
    @Test
    public void testOtherGetters() {
        assertEquals("Form", "Business Enquiry", model.getHeadingSubscription());
        assertEquals("Form", "Thank you for your request", model.getFormConfig().getThankyouHeading());
        assertEquals("Form", "We will get back to you as soon as possible",
                model.getFormConfig().getThankyouDescriptionText());
        assertEquals("Form",
                "I agree that the information I have provided will only be used in accordance with Tetra Pak privacy policy.",
                model.getConsentConfig().getPrivacyPolicy());
        assertEquals("Form",
                "/content/tetrapak/publicweb/gb/en/textimage/jcr:content/textimage.pardotsubscription.json",
                model.getApiUrl());
        assertEquals("Form", "Marketing Consent", model.getConsentConfig().getMarketingConsent());
        assertEquals("Form", "/content/search.html", model.getMoreButtonActionSubscription());
        assertEquals("Form", "More Insights", model.getMoreButtonLabelSubscription());
        assertEquals("form", "http://pardotURL", model.getPardotUrlSubscription());
        assertEquals("form", "pardotChinaURL", model.getPardotChinaUrlSubscription());

    }

    @Test
    public void testCountries() throws Exception {
        assertEquals("ContactUs", 2, model.getCountryOptions().size());
        assertEquals("ContactUs", "Albania", model.getCountryOptions().get(0).getKey());

    }
}
