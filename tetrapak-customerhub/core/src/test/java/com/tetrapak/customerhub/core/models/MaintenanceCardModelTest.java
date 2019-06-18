
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
public class MaintenanceCardModelTest {

    private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/maintenancecard";
    private static final String COMPONENT_PATH = "/content/tetrapak/customerhub/global/en/maintenancecard/jcr:content/root/responsivegrid/maintenancecard";
    private static final String RESOURCE_JSON = "documents.json";
    private MaintenanceCardModel maintenanceCardModel;

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON, CONTENT_ROOT);

    @Before
    public void setup() {
        Resource resource = aemContext.currentResource(COMPONENT_PATH);
        maintenanceCardModel = resource.adaptTo(MaintenanceCardModel.class);
    }

    @Test
    public void testModelGetters() {
        assertTrue("Service order", maintenanceCardModel.getI18nKeys().contains("serviceOrderLabel"));
        assertEquals("There should be site filter label", "Contact", maintenanceCardModel.getContactsHeading());
        assertEquals("There should be line filter label", "Planned duration", maintenanceCardModel.getPlannedDurationLabel());
        assertEquals("There should be equipment filter label", "Service agreement", maintenanceCardModel.getServiceAgreementLabel());
        assertEquals("There should be all option Text", "No data found", maintenanceCardModel.getNoDataMsg());
        assertEquals("There should be error message", "Error message incase any other error on the component", maintenanceCardModel.getErrorMsg());
    }
}
