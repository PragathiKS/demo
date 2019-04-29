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

import static org.junit.Assert.assertTrue;

/**
 * @author swalamba
 *
 */
public class MaintenanceFilteringModelTest {

    private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/en/installed-equipment";
    private static final String COMPONENT_PATH = "/content/tetrapak/customerhub/en/installed-equipment/jcr:content/root/responsivegrid/maintenancefiltering";
    private static final String RESOURCE_JSON = "maintenance-filtering.json";
    private MaintenanceFilteringModel maintenanceFilteringModel;

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON, CONTENT_ROOT);

    @Before
    public void setup() {
        Resource resource = aemContext.currentResource(COMPONENT_PATH);
        maintenanceFilteringModel = resource.adaptTo(MaintenanceFilteringModel.class);
    }

    @Test
    public void testGetI18nKeysContainsValidKey() {
        assertTrue(maintenanceFilteringModel.getI18nKeys().contains("deliveryNumberLabel"));
    }
}
