
package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author ruhsharma
 *
 */
public class ContactCardModelTest {

    private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/en/contactcard";
    private static final String COMPONENT_PATH = "/content/tetrapak/customerhub/en/contactcard/jcr:content/root/responsivegrid/contactcard";
    private static final String RESOURCE_JSON = "documents.json";
    private ContactCardModel contactCardModel;

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON, CONTENT_ROOT);

    @Before
    public void setup() {
        Resource resource = aemContext.currentResource(COMPONENT_PATH);
        contactCardModel = resource.adaptTo(ContactCardModel.class);
    }

    @Test
    public void testModelGetters() {
        assertEquals("There should be Sub Heading label", "Contacts", contactCardModel.getHeadingI18n());
        assertEquals("There should be Sub Heading label", "Get in touch with right people", contactCardModel.getSubHeadingI18n());
        assertEquals("There should be View contacts button", "View contacts", contactCardModel.getViewContactBtnTextI18n());
        assertEquals("There should be Image Path label", "/content/dam/customerhub/cow-blue-background.png", contactCardModel.getImagePath());
        assertEquals("There should be Image Alt Text", "Cow Blue Background", contactCardModel.getImageAltTextI18n());
    }
}
