
package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.servlethelpers.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.wcm.testing.mock.aem.junit.AemContext;


public class SoftConversionModelTest {

    /** The context. */
    @Rule
    public AemContext context = new AemContext();

    /** The request. */
    private final MockSlingHttpServletRequest request = context.request();

    /** The Constant TEST_RESOURCE_CONTENT. */
    private static final String TEST_RESOURCE_CONTENT = "/softconversion/test-content.json";
    private static final String SOFTCONVERSION_CONTENT_ROOT = "/content/tetrapak/publicweb/gb";
    // private static final String TEXT_IMAGE_CONTENT_ROOT = "/content/tetrapak/publicweb/gb/en/textimage";

    /** The model class. */
    Class<SoftConversionModel> modelClass = SoftConversionModel.class;

    /** The model. */
    private SoftConversionModel model;

    /** The Constant RESOURCE. */
    private static final String RESOURCE = "/content/tetrapak/publicweb/gb/en/textimage/jcr:content/textimage";

    /**
     * The resource.
     */
    private Resource resource;

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
        context.load().json(TEST_RESOURCE_CONTENT, SOFTCONVERSION_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);
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
