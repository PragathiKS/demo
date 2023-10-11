package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.publicweb.core.services.FindMyOfficeService;
import com.tetrapak.publicweb.core.services.impl.FindMyOfficeServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;

public class FindMyOfficeModelTest {

    @Rule
    public AemContext context = new AemContext();

    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT = "/findMyOffice/test-content.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/publicweb/en";

    /** The Constant RESOURCE. */
    private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/findMyOffice";

    /** The model. */
    private FindMyOfficeModel model;
    //
    private FindMyOfficeService findMyOfficeService;

    /** The resource. */
    private Resource resource;

    /**
     * Sets the up.
     *
     * @param context
     *            the new up
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
        findMyOfficeService = new FindMyOfficeServiceImpl();
        Class<FindMyOfficeModel> modelClass = FindMyOfficeModel.class;
        // load the resources for each object
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.registerService(FindMyOfficeService.class, findMyOfficeService);
        context.addModelsForClasses(modelClass);
       // Mockito.when(findMyOfficeServiceImpl.getGoogleApiKey()).thenReturn("AIzaSyC1w2gKCuwiRCsgqBR9RnSbWNuFvI5lryQ");
        context.registerInjectActivateService(findMyOfficeService);
        resource = context.currentResource(RESOURCE);
        model = resource.adaptTo(modelClass);
    }

    @Test
    public void testData() {
        assertEquals("test01", model.getAnchorId());
        assertEquals("title", model.getAnchorTitle());
        assertEquals("Heading", model.getHeading());
        assertEquals("/content/publicweb/en/jcr:content/findMyOffice.json", model.getServletPath());
        assertEquals("AIzaSyC1w2gKCuwiRCsgqBR9RnSbWNuFvI5lryQ", model.getGoogleApiKey());
        assertEquals("/content/dam/tetrapak/publicweb/contentfragment/offices", model.getOfficeCFPath());
        assertEquals("Country or Region", model.getCountryLabel());
        assertEquals("/content/dam/tetrapak/publicweb/contentfragment/countries", model.getCountryCFPath());
        assertEquals("Recycler Offices", model.getOfficeLabel());
        assertEquals("Choose your Country or Region", model.getCountryDropdownLabel());
        assertEquals("Select Recycler Offices", model.getOfficeDropdownLabel());
    }
}
