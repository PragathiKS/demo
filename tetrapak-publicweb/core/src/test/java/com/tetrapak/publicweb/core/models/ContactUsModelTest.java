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
 * The Class ContactUsModelTest.
 */
public class ContactUsModelTest {

    /** The context. */
    @Rule
    public AemContext context = new AemContext();

    /** The Constant TEST_RESOURCE_CONTENT. */
    private static final String TEST_RESOURCE_CONTENT = "/contactus/test-content.json";

    /** The Constant TEST_RESOURCE_CFM. */
    private static final String TEST_RESOURCE_CFM = "/contactus/test-countries-content.json";

    /** The Constant CONTACT_US_CONTENT_ROOT. */
    private static final String CONTACT_US_CONTENT_ROOT = "/content/tetrapak/publicweb/gb";

    /** The Constant PARDOT_COUNTRIES_ROOT. */
    private static final String PARDOT_COUNTRIES_ROOT = "/content/dam/tetrapak/publicweb/contentfragment/pardot-countries";

    /** The model class. */
    Class<ContactUsModel> modelClass = ContactUsModel.class;

    /** The model. */
    private ContactUsModel model;

    /** The country detail service. */
    private CountryDetailService countryDetailService;

    /**
     * The Constant PXP_FEATURES.
     */
    private static final String RESOURCE = CONTACT_US_CONTENT_ROOT + "/en/jcr:content/contactus";

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
        context.load().json(TEST_RESOURCE_CFM, PARDOT_COUNTRIES_ROOT);

        context.addModelsForClasses(modelClass);
        context.registerService(CountryDetailService.class, countryDetailService);
        // context.registerInjectActivateService(countryDetailService);
        final Map<String, Object> countryConfig = new HashMap<>();
        countryConfig.put("getPardotCountriesCFRootPath",
                "/content/dam/tetrapak/publicweb/contentfragment/pardot-countries");
        MockOsgi.activate(context.getService(CountryDetailService.class), context.bundleContext(), countryConfig);

        resource = context.currentResource(RESOURCE);
        model = resource.adaptTo(modelClass);

    }

    /**
     * Test dailog values.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testDailogValues() throws Exception {
        assertEquals("ContactUs", "Contact us", model.getHeading());
        assertEquals("ContactUs", "title", model.getAnchorTitle());
        assertEquals("ContactUs", "test01", model.getAnchorId());
        assertEquals("ContactUs", "Thank you for your request", model.getThankyouHeading());
        assertEquals("ContactUs", "/content/dam/tetrapak/publicweb/contactus.PNG", model.getThankyouImage());
        assertEquals("ContactUs", "Thank you", model.getThankyouImageAltText());
        assertEquals("ContactUs", "We will get back to you as soon as possible", model.getThankyouDescriptionText());
        assertEquals("ContactUs",
                "I agree that the information I have provided will only be used in accordance with Tetra Pak privacy policy.",
                model.getConsentConfig().getPrivacyPolicy());
        assertEquals("ContactUs", "/content/dam/tetrapak/publicweb/contactus.PNG", model.getImage());
        assertEquals("ContactUs", "Contact us", model.getAlt());
        assertEquals("ContactUs", "Please select the countryyou wish to contact.", model.getDescriptionText());
        assertEquals("ContactUs", "grayscale-white", model.getPwTheme());
        assertEquals("ContactUs", "/content/tetrapak/publicweb/gb/en/jcr:content/contactus.sendmail.json",
                model.getApiUrl());
    }

    /**
     * Test pardot countries.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testPardotCountries() throws Exception {

        assertEquals("ContactUs", 2, model.getCountryOptions().size());
        assertEquals("ContactUs", "Albania", model.getCountryOptions().get(0).getKey());

    }

}
