/**
 *
 */
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
 * Test class for Documents Model
 * @author ruhsharma
 *
 */
public class DocumentsModelTest {

    private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/en/Documents";
    private static final String COMPONENT_PATH = "/content/tetrapak/customerhub/en/Documents/jcr:content/root/responsivegrid/documents";
    private static final String RESOURCE_JSON = "documents.json";
    private DocumentsModel documentsModel;

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON, CONTENT_ROOT);

    @Before
    public void setup() {
        Resource resource = aemContext.currentResource(COMPONENT_PATH);
        documentsModel = resource.adaptTo(DocumentsModel.class);
    }

    @Test
    public void testModelGetters() {
        assertTrue("There should be site filter label in i18n keys string", documentsModel.getI18nKeys().contains("siteFilterLabel"));
        assertEquals("There should be site filter label", "Site", documentsModel.getSiteFilterLabel());
        assertEquals("There should be line filter label", "Line/Area", documentsModel.getLineFilterLabel());
        assertEquals("There should be equipment filter label", "Document for {0} in {1}", documentsModel.getDocumentHeading());
        assertEquals("There should be all option Text", "No documents message", documentsModel.getNoDataMsg());
        assertEquals("There should be error message", "Error message incase any other error on the component", documentsModel.getErrorMsg());
    }
}
