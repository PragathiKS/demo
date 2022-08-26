package com.tetrapak.customerhub.core.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.security.AccessControlException;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.day.cq.tagging.InvalidTagFormatException;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.services.APIGEEService;

import io.wcm.testing.mock.aem.junit.AemContext;

public class TechnicalPublicationsModelTest {
    /** The Constant TEST_CONTENT. */
    private static final String TEST_CONTENT = "technical-publications.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/customerhub/content-components/en/technical-publications";

    /** The Constant RESOURCE_PATH. */
    private static final String RESOURCE_PATH = TEST_CONTENT_ROOT
	    + "/jcr:content/root/responsivegrid/technicalpublication";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(TEST_CONTENT, TEST_CONTENT_ROOT);

    @Mock
    private APIGEEService apigeeService;

    /** The model. */
    private TechnicalPublicationsModel model;

    /**
     * Set up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
	MockitoAnnotations.initMocks(this);
	aemContext.registerService(APIGEEService.class, apigeeService);
	when(apigeeService.getApigeeServiceUrl()).thenReturn(new String("https://api-dev.tetrapak.com"));
	when(apigeeService.getApiMappings()).thenReturn(
		new String[] { "countrylist:countries", "myequipment-lineslist:installedbase/equipments/lines",
			"myequipment-equipmentlist:installedbase/equipments",
			"myequipment-customerlist:installedbase/equipments/customers",
			"technicalpublications:technicalpublications" });
	Resource resource = aemContext.currentResource(RESOURCE_PATH);
	aemContext.request().setResource(resource);
	model = aemContext.request().adaptTo(TechnicalPublicationsModel.class);
    }

    /**
     * Test method for Keylines model.
     * 
     * @throws InvalidTagFormatException
     * @throws AccessControlException
     */
    @Test
    public void testModel() throws AccessControlException, InvalidTagFormatException {
	assertEquals("cuhu.technicalpublications.title", model.getTitle());
	assertNotNull(model.getI18nKeys());
	assertTrue(model.getAllCountriesApi().contains("countries"));
	assertTrue(model.getCustomerListApi().contains("installedbase/equipments/customers"));
	assertTrue(model.getEquipmentListApi().contains("installedbase/equipments"));
	assertTrue(model.getLineListApi().contains("installedbase/equipments/lines"));
	assertTrue(model.getTechnicalPublicationsApi().contains("technicalpublications"));
	assertEquals("en", model.getLocale());
	assertTrue(model.isPublishEnvironment());
	
	TechnicalPublicationsKeysModel i18nModel = model.getI18nKeysModel();
	assertEquals("cuhu.technicalpublications.allFiles", i18nModel.getAllFiles());
	assertEquals("cuhu.technicalpublications.country", i18nModel.getCountry());
	assertEquals("cuhu.technicalpublications.customer", i18nModel.getCustomer());
	assertEquals("cuhu.technicalpublications.documentNumber", i18nModel.getDocumentNumber());
	assertEquals("cuhu.technicalpublications.documentType", i18nModel.getDocumentType());
	assertEquals("cuhu.technicalpublications.issueDate", i18nModel.getIssueDate());
	assertEquals("cuhu.technicalpublications.line", i18nModel.getLine());
	assertEquals("cuhu.technicalpublications.lineEquipment", i18nModel.getLineEquipment());
	assertEquals("cuhu.technicalpublications.name", i18nModel.getName());
	assertEquals("cuhu.technicalpublications.rebuildingKitName", i18nModel.getRebuildingKitName());
	assertEquals("cuhu.technicalpublications.rebuildingKitNumber", i18nModel.getRebuildingKitNumber());
	assertEquals("cuhu.technicalpublications.searchResults", i18nModel.getSearchResults());
	assertEquals("cuhu.technicalpublications.serialNumber", i18nModel.getSerialNumber());
	assertEquals("cuhu.technicalpublications.materialNumber", i18nModel.getMaterialNumber());
    }
}
