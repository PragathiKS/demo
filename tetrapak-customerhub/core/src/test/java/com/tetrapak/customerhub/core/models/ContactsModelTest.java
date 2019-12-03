
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
 * Test class for ContactsModel
 *
 * @author ruhsharma
 */
public class ContactsModelTest {

    private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/contacts";
    private static final String COMPONENT_PATH = "/content/tetrapak/customerhub/global/en/contacts/jcr:content/root/responsivegrid/contacts";
    private static final String RESOURCE_JSON = "documents.json";
    private ContactsModel contactsModel;

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON, CONTENT_ROOT);

    @Before
    public void setup() {
        Resource resource = aemContext.currentResource(COMPONENT_PATH);
        contactsModel = resource.adaptTo(ContactsModel.class);
    }

    @Test
    public void testModelGetters() {
        assertTrue("Find contacts on your site", contactsModel.getI18nKeys().contains("siteFilterHeading"));
        assertEquals("There should be site filter heading", "Find contacts on your site", contactsModel.getSiteFilterHeading());
        assertEquals("There should be all no contacts found message", "No contacts found", contactsModel.getNoContactMsg());
        assertEquals("There should be error message", "Error message incase any other error on the component", contactsModel.getErrorMsg());
    }
}
