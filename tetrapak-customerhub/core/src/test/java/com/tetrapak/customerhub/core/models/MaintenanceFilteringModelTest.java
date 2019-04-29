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
    public void testModelGetters() {
        assertTrue("There should be site filter label in i18n keys string", maintenanceFilteringModel.getI18nKeys().contains("siteFilterLabel"));
        assertEquals("There should be site filter label", "Site", maintenanceFilteringModel.getSiteFilterLabel());
        assertEquals("There should be line filter label", "line", maintenanceFilteringModel.getLineFilterLabel());
        assertEquals("There should be equipment filter label", "Equipment", maintenanceFilteringModel.getEquipmentFilterLabel());
        assertEquals("There should be all option Text", "All", maintenanceFilteringModel.getAllOptionText());
        assertEquals("There should be contacts heading", "tetra pak contact", maintenanceFilteringModel.getContactsHeading());
        assertEquals("There should be schedule heading", "Maintenance Schedule", maintenanceFilteringModel.getScheduleHeading());
        assertEquals("There should be events heading", "Maintenance events", maintenanceFilteringModel.getEventsHeading());
        assertEquals("There should be status label", "Status", maintenanceFilteringModel.getStatusLabel());
        assertEquals("There should be service agreement label", "Service agreement", maintenanceFilteringModel.getServiceAgreementLabel());
        assertEquals("There should be planned duration label", "Planned duration", maintenanceFilteringModel.getPlannedDurationLabel());
        assertEquals("There should be planned start label", "Planned start", maintenanceFilteringModel.getPlannedStartLabel());
        assertEquals("There should be planned finished label", "Planned finished", maintenanceFilteringModel.getPlannedFinishedLabel());
        assertEquals("There should be line label", "Line", maintenanceFilteringModel.getLineLabel());
        assertEquals("There should be equipment label", "Equipment", maintenanceFilteringModel.getEquipmentLabel());
        assertEquals("There should be service order label", "Service order", maintenanceFilteringModel.getServiceOrderLabel());
        assertEquals("There should be operation hort text label", "Operation short text", maintenanceFilteringModel.getOperationShortTextLabel());
        assertEquals("There should be no data message", "no data", maintenanceFilteringModel.getNoDataMsg());
        assertEquals("There should be error message", "error", maintenanceFilteringModel.getErrorMsg());
    }
}
