
package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.wcm.testing.mock.aem.junit.AemContext;


public class FormConfigModelTest {

    /** The context. */
    @Rule
    public AemContext context = new AemContext();

    /** The Constant TEST_RESOURCE_CONTENT. */
    private static final String TEST_RESOURCE_CONTENT = "/businessinquiryform/test-content.json";
    private static final String CONTACT_US_CONTENT_ROOT = "/content/tetrapak/publicweb/lang-master";
    /** The model class. */
    Class<FormConfigModel> modelClass = FormConfigModel.class;

    /** The model. */
    private FormConfigModel model;

    /**
     * The Constant
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
        context.load().json(TEST_RESOURCE_CONTENT, CONTACT_US_CONTENT_ROOT);
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
        assertEquals("Form", "Marketing Consent", model.getMarketingConsent());
       }


}
