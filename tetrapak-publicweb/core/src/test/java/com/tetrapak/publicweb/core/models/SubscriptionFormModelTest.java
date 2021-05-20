package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.HashMap;
import java.util.Map;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import com.tetrapak.publicweb.core.services.CountryDetailService;
import com.tetrapak.publicweb.core.services.PardotService;
import com.tetrapak.publicweb.core.services.impl.CountryDetailServiceImpl;
import com.tetrapak.publicweb.core.services.impl.PardotServiceImpl;
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
    
    /** The Constant CONTACT_US_CONTENT_ROOT. */
    private static final String CONTACT_US_CONTENT_ROOT = "/content/tetrapak/publicweb/gb";
    
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
    private static final String RESOURCE = CONTACT_US_CONTENT_ROOT
            + "/en/subscriptionform/jcr:content/subscriptionform";

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
        pardotService = new PardotServiceImpl();
        countryService = new CountryDetailServiceImpl();
        context.load().json(TEST_RESOURCE_CONTENT, CONTACT_US_CONTENT_ROOT);
        context.load().json(TEST_RESOURCE_CFM, COUNTRIES_ROOT);
        context.addModelsForClasses(modelClass);
       
        context.registerService(PardotService.class, pardotService);
        final Map<String, Object> pardotConfig = new HashMap<>();
        initializeConfig(pardotConfig, "pardotsubscriptionServiceUrl", "http://pardotURL", PardotService.class);
        
        context.registerService(CountryDetailService.class, countryService);
        final Map<String, Object> countryConfig = new HashMap<>();
        initializeConfig(countryConfig, "getPardotCountriesCFRootPath", "/content/dam/tetrapak/publicweb/cfm/countries", CountryDetailService.class);

        resource = context.currentResource(RESOURCE);
        model = resource.adaptTo(modelClass);
    }

    /**
     * Initialize config.
     *
     * @param config the config
     * @param configKey the config key
     * @param configValue the config value
     * @param injectClass the inject class
     */
    private void initializeConfig(Map<String, Object> config, String configKey, String configValue, Class<?> injectClass) {
        config.put(configKey, configValue);
        MockOsgi.activate(context.getService(injectClass), context.bundleContext(), config);
    }
 
    /**
     * Test model not null.
     */
    @Ignore
    public void testModelNotNull() {
        assertNotNull("Model not null", model);
    }
    
    /**
     * Test fetch country.
     */
    @Ignore
    public void testFetchCountry() {
        assertEquals("gb", model.getSiteCountry());
    }
    
    /**
     * Test fetch language.
     */
    @Ignore
    public void testFetchLanguage() {
        assertEquals("en", model.getSiteLanguage());
    }
    
    /**
     * Test other getters.
     */
    @Ignore
    public void testOtherGetters() {
    	assertEquals("/content/tetrapak/publicweb/gb/en/subscriptionform/jcr:content/subscriptionform.pardotsubscription.json", model.getApiUrl());
    	assertEquals("Form", "Marketing Consent", model.getConsentConfig().getMarketingConsent());
    	assertEquals("Form", "albania",model.getCountryOptions().get(0).getKey());
    	assertEquals("Form", "Business Enquiry", model.getFormConfig().getHeading());
    }
}
