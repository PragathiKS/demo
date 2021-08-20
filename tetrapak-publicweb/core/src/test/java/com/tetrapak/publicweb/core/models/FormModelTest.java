
package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.wcm.testing.mock.aem.junit.AemContext;


public class FormModelTest {

    /** The context. */
    @Rule
    public AemContext context = new AemContext();

    /** The Constant TEST_RESOURCE_CONTENT. */
    private static final String TEST_RESOURCE_CONTENT = "/businessinquiryform/test-content.json";
    private static final String CONTACT_US_CONTENT_ROOT = "/content/tetrapak/publicweb/lang-master";
    /** The model class. */
    Class<FormModel> modelClass = FormModel.class;

    /** The model. */
    private FormModel model;

    /**
     * The Constant
     */
    private static final String RESOURCE = CONTACT_US_CONTENT_ROOT + "/en/contact-us/jcr:content/form";

    private static final String SUBSCRIPTION = CONTACT_US_CONTENT_ROOT + "/en/jcr:content/root/responsivegrid/subscriptionformconf";

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
    }

    /**
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testDailogValues() throws Exception {
        resource = context.currentResource(RESOURCE);
        model = resource.adaptTo(modelClass);
        assertEquals("Form", "Business Enquiry", model.getHeading());
        assertEquals("Form", "title", model.getAnchorTitle());
        assertEquals("Form", "test01", model.getAnchorId());
        assertEquals("Form", "Thank you for your request", model.getThankyouHeading());
        assertEquals("Form", "/content/dam/tetrapak/publicweb/contactus.PNG", model.getThankyouImage());
        assertEquals("Form", "Thank you", model.getThankyouImageAltText());
        assertEquals("Form", "We will get back to you as soon as possible", model.getThankyouDescriptionText());
        assertEquals("Form", "/content/dam/tetrapak/publicweb/contactus.PNG", model.getImage());
        assertEquals("Form", "Contact us", model.getAlt());
        assertEquals("Form", "Description", model.getDescriptionText());
        assertEquals("Form", "grayscale-white", model.getPwTheme());
       }


    @Test
    public void testDailogValuesSubscription() throws Exception {
        resource = context.currentResource(SUBSCRIPTION);
        model = resource.adaptTo(modelClass);
        assertEquals("Form", "Thank you for subscribing!", model.getThankyouHeading());
        assertEquals("Form","Confirm your subscription.", model.getThankyouDescriptionText());
        assertEquals("Form","/content/tetrapak/publicweb/lang-masters/en/about-tetra-pak/legal-information",model.getLegalInfoLink());
        assertEquals("Form","/content/tetrapak/publicweb/lang-masters/en/about-tetra-pak/news-and-events/news-room", model.getNewsroomLink());
        assertEquals("Form","/content/tetrapak/publicweb/lang-masters/en/contact-us",model.getContactUsLink());
    }

}
