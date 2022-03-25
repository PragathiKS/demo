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
import com.tetrapak.publicweb.core.services.PardotService;
import com.tetrapak.publicweb.core.services.impl.CountryDetailServiceImpl;
import com.tetrapak.publicweb.core.services.impl.PardotServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class BusinessInquiryModelTest.
 */
public class BusinessInquiryModelTest {

    /** The context. */
    @Rule
    public AemContext context = new AemContext();

    /** The context 2. */
    @Rule
    public AemContext context2 = new AemContext();

    /** The Constant TEST_RESOURCE_CONTENT. */
    private static final String TEST_RESOURCE_CONTENT = "/businessinquiryform/test-content.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/publicweb/gb";

    /** The Constant CONTACT_US_CONTENT_ROOT. */
    private static final String CONTACT_US_CONTENT_ROOT = "/content/tetrapak/publicweb/gb/en/contact-us";

    /** The Constant TEST_RESOURCE_CFM. */
    private static final String TEST_RESOURCE_CFM = "/contactus/test-countries-content.json";

    /** The Constant COUNTRIES_ROOT. */
    private static final String COUNTRIES_ROOT = "/content/dam/tetrapak/publicweb/cfm/countries";

    /** The model class. */
    Class<BusinessInquiryModel> modelClass = BusinessInquiryModel.class;

    /** The model. */
    private BusinessInquiryModel model;

    /** The pardot service. */
    private PardotService pardotService;

    /** The country detail service. */
    private CountryDetailService countryDetailService;

    /**
     * The resource.
     */
    private Resource resource;

    /**
     * The Constant PXP_FEATURES.
     */
    private static final String RESOURCE = CONTACT_US_CONTENT_ROOT + "/jcr:content/businessinquiryform";

    /**
     * Sets the up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        pardotService = new PardotServiceImpl();
        context.load().json(TEST_RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.load().json(TEST_RESOURCE_CFM, COUNTRIES_ROOT);
        context.load().json("/businessinquiryform/tags.json", "/content/cq:tags/pardot-system-config");

        context.addModelsForClasses(modelClass);
        context.registerService(PardotService.class, pardotService);
        // context.registerInjectActivateService(countryDetailService);
        final Map<String, Object> pardotConfig = new HashMap<>();
        MockOsgi.activate(context.getService(PardotService.class), context.bundleContext(), pardotConfig);

        countryDetailService = new CountryDetailServiceImpl();
        context.registerService(CountryDetailService.class, countryDetailService);
        // context.registerInjectActivateService(countryDetailService);
        final Map<String, Object> countryConfig = new HashMap<>();
        countryConfig.put("getPardotCountriesCFRootPath", "/content/dam/tetrapak/publicweb/cfm/countries");
        MockOsgi.activate(context.getService(CountryDetailService.class), context.bundleContext(), countryConfig);

        resource = context.currentResource(RESOURCE);
        model = resource.adaptTo(modelClass);

    }

    /**
     * Test dailog values.
     *
     * @throws Exception the exception
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
        assertEquals("Form", "pardot-system-config:job-title", model.getFormConfig().getPardotSystemConfigTags()[0]);
        assertEquals("Form", "http://go.tetrapak.com/l/857883/2020-05-29/w6xt", model.getBefPardotURL());
        assertEquals("Form", "go.tetrapak.com/l/857883/2020-05-29/w6xt", model.getBefChinaPardotURL());
    }
    
    @Test
    public void testTagTitles() {
        assertEquals("Form", true, model.getTagTitles().containsValue("Associate"));
    }

    @Test
    public void testTagFunctions() {
        assertEquals("Form", true, model.getTagFunctions().containsValue("Administrative"));
    }

    @Test
    public void testTagProcessingRoles() {
        assertEquals("Form", true, model.getTagProcessingRoles().containsValue("Consultant"));
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

    /**
     * Test countries.
     *
     * @throws Exception the exception
     */
    @Test
    public void testCountries() throws Exception {

        assertEquals("ContactUs", 2, model.getCountryOptions().size());
        assertEquals("ContactUs", "Albania", model.getCountryOptions().get(0).getKey());

    }

}