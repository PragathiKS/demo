package com.tetrapak.customerhub.core.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.*;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.RebuildingKitsDetailsService;
import io.wcm.testing.mock.aem.junit.AemContext;

import javax.servlet.http.Cookie;

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

	@Mock
	private RebuildingKitsDetailsService rebuildingKitsDetailsService;


	/**
	 * Sets the up.
	 */
	@Before
	public void setUp () {
		MockitoAnnotations.initMocks(this);
        aemContext.registerService(APIGEEService.class, apigeeService);
		aemContext.registerService(RebuildingKitsDetailsService.class, rebuildingKitsDetailsService);
		aemContext.runMode("publish");
        when(apigeeService.getApigeeServiceUrl()).thenReturn(new String("https://api-dev.tetrapak.com"));
        when(apigeeService.getApiMappings()).thenReturn(new String[]{"rebuildingkits-rebuildingkitdetails:installedbase/rebuildingkits","rebuildingkits-implstatuslist:installedbase/rebuildingkits/implstatuses","technicalbulletins:technicalbulletins"});
		Resource resource = aemContext.currentResource(RESOURCE_PATH);
		aemContext.request().setResource(resource);
		model = aemContext.request().adaptTo(RebuildingKitDetailsModel.class);
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
		assertEquals("cuhu.rebuildingkits.viewInEBizButton",model.getViewInEBizButton());
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
		assertEquals("cuhu.rebuildingkits.rkSubType",model.getRkSubType());
		assertEquals("cuhu.rebuildingkits.rkValidation",model.getRkValidation());
		assertEquals("cuhu.rebuildingkits.rkNoCtiText",model.getRkNoCtiText());
		assertEquals("cuhu.rebuildingkits.rkReqCtiText",model.getRkReqCtiText());
		assertEquals("cuhu.rebuildingkits.ctiSubject",model.getRkctisubjecttext());
		assertEquals("cuhu.rebuildingkits.ctiSalutation",model.getRkctisalutationtext());
		assertEquals("cuhu.rebuildingkits.ctiBody",model.getRkctibodytext());
		assertEquals("cuhu.rebuildingkits.ctiEmailAddressText",model.getRkctiemailaddress());
		assertEquals("cuhu.rebuildingkits.ctiUsernametext",model.getRkctiUsernameText());
		assertEquals("cuhu.rebuildingkits.ctiFunctionalLocationText",model.getRkctifunctionalLocationtext());
		assertEquals("cuhu.rebuildingkits.ctiMconText",model.getRkctimcontext());
		assertEquals("cuhu.rebuildingkits.ctiRequestedLanguageText",model.getRkctirequestedlanguage());
		assertEquals("cuhu.rebuildingkits.ctiRkTbNumberText",model.getRktbnumbertext());
		assertEquals("cuhu.rebuildingkits.ctiCommentText",model.getRkcticommenttext());
		assertNotNull("non empty i18keys",model.getI18nKeys());
	}
	
	/**
	 * Test Api.
	 */
	@Test
	public void testApi() {
		assertTrue(model.getRebuildingKitDetailsApi().contains("installedbase/rebuildingkits"));
		assertTrue(model.getRebuildingKitImplStatusListApi().contains("installedbase/rebuildingkits"));
		assertTrue(model.getTechnicalBulletinApi().contains("technicalbulletins"));
	}

	/**
	 * Test isPulish.
	 */
	@Test
	public void testIsPublish() {
		assertTrue(model.isPublishEnvironment());
	}

	/**
	 * Test nonNullMethods.
	 */
	@Test
	public void testNonNullMethods() {
		assertNotNull("Invalid Api url",model.getEmailApiUrl());
		assertNotNull("Invalid sling request",model.getRequest());
		assertNotNull("Invalid Username",model.getUserNameValue());
		assertNotNull("Invalid email Id",model.getEmailAddressValue());
	}
}
