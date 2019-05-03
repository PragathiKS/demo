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
public class MaintenanceModelTest {

    private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/en/installed-equipment";
    private static final String COMPONENT_PATH = "/content/tetrapak/customerhub/en/installed-equipment/jcr:content/root/responsivegrid/maintenance";
    private static final String RESOURCE_JSON = "maintenance.json";
    private MaintenanceModel maintenanceModelModel;

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON, CONTENT_ROOT);

    @Before
    public void setup() {
        Resource resource = aemContext.currentResource(COMPONENT_PATH);
        maintenanceModelModel = resource.adaptTo(MaintenanceModel.class);
    }

    @Test
    public void testModelGetters() {
        assertTrue("There should be site filter label in i18n keys string", maintenanceModelModel.getI18nKeys().contains("siteFilterLabel"));
        assertEquals("There should be site filter label", "Site", maintenanceModelModel.getSiteFilterLabel());
        assertEquals("There should be line filter label", "line", maintenanceModelModel.getLineFilterLabel());
        assertEquals("There should be equipment filter label", "Equipment", maintenanceModelModel.getEquipmentFilterLabel());
        assertEquals("There should be all option Text", "All", maintenanceModelModel.getAllOptionText());
        assertEquals("There should be contacts heading", "tetra pak contact", maintenanceModelModel.getContactsHeading());
        assertEquals("There should be schedule heading", "Maintenance Schedule", maintenanceModelModel.getScheduleHeading());
        assertEquals("There should be events heading", "Maintenance events", maintenanceModelModel.getEventsHeading());
        assertEquals("There should be status label", "Status", maintenanceModelModel.getStatusLabel());
        assertEquals("There should be service agreement label", "Service agreement", maintenanceModelModel.getServiceAgreementLabel());
        assertEquals("There should be planned duration label", "Planned duration", maintenanceModelModel.getPlannedDurationLabel());
        assertEquals("There should be planned start label", "Planned start", maintenanceModelModel.getPlannedStartLabel());
        assertEquals("There should be planned finished label", "Planned finished", maintenanceModelModel.getPlannedFinishedLabel());
        assertEquals("There should be line label", "Line", maintenanceModelModel.getLineLabel());
        assertEquals("There should be equipment label", "Equipment", maintenanceModelModel.getEquipmentLabel());
        assertEquals("There should be service order label", "Service order", maintenanceModelModel.getServiceOrderLabel());
        assertEquals("There should be operation hort text label", "Operation short text", maintenanceModelModel.getOperationShortTextLabel());
        assertEquals("There should be no data message", "no data", maintenanceModelModel.getNoDataMsg());
        assertEquals("There should be error message", "error", maintenanceModelModel.getErrorMsg());
    }
}
