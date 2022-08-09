package com.tetrapak.customerhub.core.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.services.APIGEEService;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * @author INNIMBALKARS
 * The Class RebuildingKitDetailsModelTest.
 */
public class RebuildingKitDetailsModelTest {
	
	/** The Constant TEST_CONTENT. */
	private static final String TEST_CONTENT = "rebuildingkitdetails.json";
	
	/** The Constant TEST_CONTENT_ROOT. */
	private static final String TEST_CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/installedequipment/rebuildingkits";
	
	/** The Constant RESOURCE_PATH. */
	private static final String RESOURCE_PATH = TEST_CONTENT_ROOT+"/jcr:content/root/responsivegrid/rebuildingkitdetails";
	
	/** The aem context. */
	@Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(TEST_CONTENT, TEST_CONTENT_ROOT);

	/** The model. */
	private RebuildingKitDetailsModel model;
	
	/** The apigeeService. */
	@Mock
	private APIGEEService apigeeService;
	
	/**
	 * Sets the up.
	 */
	@Before
	public void setUp () {
		MockitoAnnotations.initMocks(this);
        aemContext.registerService(APIGEEService.class, apigeeService);
        when(apigeeService.getApigeeServiceUrl()).thenReturn(new String("https://api-dev.tetrapak.com"));
        when(apigeeService.getApiMappings()).thenReturn(new String[]{"rebuildingkits-rebuildingkitdetails:installedbase/rebuildingkits?rknumbers={rknumbers}&equipmentnumber={equipmentnumber}"});
		Resource resource = aemContext.currentResource(RESOURCE_PATH);
		model = resource.adaptTo(RebuildingKitDetailsModel.class);
	}

	/**
	 * Test model not null.
	 */
	@Test
	public void testModelNotNull() {
		assertNotNull("Model Not null",model);
	}
	
	/**
	 * Test i18n keys messages.
	 */
	@Test
	public void testLabeli18NKeys() {
		assertEquals("cuhu.rebuildingkits.rkAndEquipmentInformation",model.getRkAndEquipmentInformation());
		assertEquals("cuhu.rebuildingkits.countryLocation",model.getCountryLocation());
		assertEquals("cuhu.rebuildingkits.functionalLocation",model.getFunctionalLocation());
		assertEquals("cuhu.rebuildingkits.equipmentMaterial",model.getEquipmentMaterial());
		assertEquals("cuhu.rebuildingkits.statusEquipmentType",model.getStatusEquipmentType());
		assertEquals("cuhu.rebuildingkits.implementationStatusDate",model.getImplementationStatusDate());
		assertEquals("cuhu.rebuildingkits.equipmentStructure",model.getEquipmentStructure());
		assertEquals("cuhu.rebuildingkits.reportImplementationStatus",model.getReportImplementationStatus());
		assertEquals("cuhu.rebuildingkits.rkNote",model.getRkNote());
		assertEquals("cuhu.rebuildingkits.rkNoteValue",model.getRkNoteValue());
		assertEquals("cuhu.rebuildingkits.rkFiles",model.getRkFiles());
		assertEquals("cuhu.rebuildingkits.rkCTI",model.getRkCTI());
		assertEquals("cuhu.rebuildingkits.moreLanguage",model.getMoreLanguage());
		assertEquals("cuhu.rebuildingkits.rkInformation",model.getRkInformation());
		assertEquals("cuhu.rebuildingkits.refReleaseDate",model.getRefReleaseDate());
		assertEquals("cuhu.rebuildingkits.rkType",model.getRkType());
		assertEquals("cuhu.rebuildingkits.rkStatus",model.getRkStatus());
		assertEquals("cuhu.rebuildingkits.rkHandling",model.getRkHandling());
		assertEquals("cuhu.rebuildingkits.rkPlanningInformation",model.getRkPlanningInformation());
		assertEquals("cuhu.rebuildingkits.implDeadline",model.getImplDeadline());
		assertEquals("cuhu.rebuildingkits.serviceOrder",model.getServiceOrder());
	}
	
	/**
	 * Test Api.
	 */
	@Test
	public void testApi() {
		assertTrue(model.getRebuildingKitDetailsApi().contains("installedbase/rebuildingkits?rknumbers={rknumbers}&equipmentnumber={equipmentnumber}"));
	}
}
