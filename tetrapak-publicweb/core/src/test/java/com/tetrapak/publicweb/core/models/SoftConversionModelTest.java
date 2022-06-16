package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.publicweb.core.services.CountryDetailService;
import com.tetrapak.publicweb.core.services.impl.CountryDetailServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class SoftConversionModelTest.
 */
public class SoftConversionModelTest {

    /** The context. */
    @Rule
    public AemContext context = new AemContext();

    /** The Constant TEST_RESOURCE_CONTENT. */
    private static final String TEST_RESOURCE_CONTENT = "/softconversion/test-content.json";

    /** The Constant SOFTCONVERSION_CONTENT_ROOT. */
    private static final String SOFTCONVERSION_CONTENT_ROOT = "/content/tetrapak/publicweb/gb";

    /** The Constant TEST_RESOURCE_CFM. */
    private static final String TEST_RESOURCE_CFM = "/contactus/test-countries-content.json";

    /** The Constant COUNTRIES_ROOT. */
    private static final String COUNTRIES_ROOT = "/content/dam/tetrapak/publicweb/cfm/countries";

    /** The model class. */
    Class<SoftConversionModel> modelClass = SoftConversionModel.class;

    /** The model. */
    private SoftConversionModel model;

    /** The Constant RESOURCE. */
    private static final String RESOURCE = "/content/tetrapak/publicweb/gb/en/textimage/jcr:content/textimage";

    /** The country detail service. */
    private CountryDetailService countryDetailService;

    /**
     * The resource.
     */
    private Resource resource;
    
    /** The Constant TEST_POSITIONTAGS_RESOURCE_CONTENT. */
    private static final String TEST_POSITIONTAGS_RESOURCE_CONTENT = "/softconversion/position-tags.json";
    
    /** The Constant POSITIONTAGS_CONTENT_ROOT. */
    private static final String POSITIONTAGS_CONTENT_ROOT = "/content/cq:tags/pardot-system-config/job-title";
    
    /** The Constant TEST_FUNCTIONTAGS_RESOURCE_CONTENT. */
    private static final String TEST_FUNCTIONTAGS_RESOURCE_CONTENT = "/softconversion/function-tags.json";
    
    /** The Constant FUNCTIONTAGS_CONTENT_ROOT. */
    private static final String FUNCTIONTAGS_CONTENT_ROOT = "/content/cq:tags/pardot-system-config/function";

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {

        context.load().json(TEST_RESOURCE_CONTENT, SOFTCONVERSION_CONTENT_ROOT);
        context.load().json(TEST_RESOURCE_CFM, COUNTRIES_ROOT);
        context.load().json(TEST_POSITIONTAGS_RESOURCE_CONTENT, POSITIONTAGS_CONTENT_ROOT);
        context.load().json(TEST_FUNCTIONTAGS_RESOURCE_CONTENT, FUNCTIONTAGS_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);

        countryDetailService = new CountryDetailServiceImpl();
        context.registerService(CountryDetailService.class, countryDetailService);
        // context.registerInjectActivateService(countryDetailService);
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
     * Test dailog values.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testDailogValues() throws Exception {
        assertEquals("Form", "Business Enquiry", model.getHeading());
        assertEquals("Form", "Thank you for your request", model.getFormConfig().getThankyouHeading());
        assertEquals("Form", "We will get back to you as soon as possible",
                model.getFormConfig().getThankyouDescriptionText());
        assertEquals("Form",
                "I agree that the information I have provided will only be used in accordance with Tetra Pak privacy policy.",
                model.getConsentConfig().getPrivacyPolicy());
        assertEquals("Form", "Description", model.getDescriptionText());
        assertEquals("Form",
                "/content/tetrapak/publicweb/gb/en/textimage/jcr:content/textimage.pardotsoftconversion.json",
                model.getApiUrl());
        assertEquals("Form", "Marketing Consent", model.getConsentConfig().getMarketingConsent());
        assertEquals("Form", "welcome back", model.getFormConfig().getWelcomeBackHeading());
        assertEquals("Form", "Welcome back to our portal", model.getFormConfig().getWelcomeBackDescriptionText());
        assertEquals("Form", "Download Ready", model.getFormConfig().getDownloadReadyHeading());
        assertEquals("Form", "your download is ready", model.getFormConfig().getDownloadReadyDescriptionText());
        assertEquals("Form", "/content/search.html", model.getMoreButtonAction());
        assertEquals("Form", "More whitepapers", model.getMoreButtonLabel());
        assertEquals("Form", "Yes, I am", model.getFormConfig().getYesButtonLabel());
        assertEquals("Form", "No, I am not", model.getFormConfig().getNoButtonLabel());
        assertEquals("form", "http://pardotURL", model.getPardotUrl());
        assertEquals("form", "pardotChinaURL", model.getPardotChinaUrl());
        assertEquals("Form", "/content/cq:tags/pardot-system-config/job-title", model.getFormConfig().getPositionTagsPath());
        assertEquals("Form", "/content/cq:tags/pardot-system-config/function", model.getFormConfig().getFunctionTagsPath());

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
     * @throws Exception
     *             the exception
     */
    @Test
    public void testCountries() throws Exception {

        assertEquals("ContactUs", 2, model.getCountryOptions().size());
        assertEquals("ContactUs", "Albania", model.getCountryOptions().get(0).getKey());

    }
    
    /**
     * Test position tags.
     *
     * @throws Exception
     *             the exception
     */
    @Test
	public void testPositionTags() {
    	assertEquals("Form", 2, model.getPositionOptions().size());
	    assertEquals("Form", "C-suite", model.getPositionOptions().get(0).getKey());
	}
    
    /**
     * Test function tags.
     *
     * @throws Exception
     *             the exception
     */
    @Test
	public void testFunctionTags() {
    	assertEquals("Form", 2, model.getFunctionOptions().size());
	    assertEquals("Form", "Administrative", model.getFunctionOptions().get(0).getKey());
	}

}
