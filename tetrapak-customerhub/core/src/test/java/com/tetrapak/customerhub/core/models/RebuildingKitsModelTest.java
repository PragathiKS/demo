package com.tetrapak.customerhub.core.models;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.services.APIGEEService;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * @author semathura
 * The Class RebuildingKitsModelTest.
 */
public class RebuildingKitsModelTest {
	
	/** The Constant TEST_CONTENT. */
	private static final String TEST_CONTENT = "rebuildingkits.json";
	
	/** The Constant TEST_CONTENT_ROOT. */
	private static final String TEST_CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/installedequipment/rebuildingkits";
	
	/** The Constant RESOURCE_PATH. */
	private static final String RESOURCE_PATH = TEST_CONTENT_ROOT+"/jcr:content/root/responsivegrid/rebuildingkits";
	
	/** The aem context. */
	@Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(TEST_CONTENT, TEST_CONTENT_ROOT);

	/** The model. */
	private RebuildingKitsModel model;
	
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
        when(apigeeService.getApiMappings()).thenReturn(new String[]{"rebuildingkits-countrylist:installedbase/rebuildingkits/countries","rebuildingkits-rebuildingkitslist:installedbase/rebuildingkits"});
		Resource resource = aemContext.currentResource(RESOURCE_PATH);
		model = resource.adaptTo(RebuildingKitsModel.class);
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
		assertEquals("cuhu.rebuildingkits.country",model.getCountryLabel());
		assertEquals("cuhu.rebuildingkits.functionalLocation",model.getFunctionalLocationLabel());
		assertEquals("cuhu.rebuildingkits.desc",model.getDescriptionLabel());
		assertEquals("cuhu.rebuildingkits.machineSystem",model.getMachineSystemLabel());
		assertEquals("cuhu.rebuildingkits.serialNumber",model.getSerialNumberLabel());
		assertEquals("cuhu.rebuildingkits.status",model.getStatusLabel());
		assertEquals("cuhu.rebuildingkits.rkNumber",model.getRkNumberLabel());
		assertEquals("cuhu.rebuildingkits.rkDesc",model.getRkDescriptionLabel());
		assertEquals("cuhu.rebuildingkits.implStatus",model.getImplStatusLabel());
		assertEquals("cuhu.rebuildingkits.implDate",model.getImplDateLabel());
		assertEquals("cuhu.rebuildingkits.implStatusDate",model.getImplStatusDateLabel());
		assertEquals("cuhu.rebuildingkits.rkType",model.getRkTypeLabel());
		assertEquals("cuhu.rebuildingkits.rkStatus",model.getRkStatusLabel());
		assertEquals("cuhu.rebuildingkits.rkHandling",model.getRkHandlingLabel());
		assertEquals("cuhu.rebuildingkits.implDeadline",model.getImplDeadlineLabel());
		assertEquals("cuhu.rebuildingkits.plannedDate",model.getPlannedDateLabel());
		assertEquals("cuhu.rebuildingkits.releaseDate",model.getReleaseDateLabel());
		assertEquals("cuhu.rebuildingkits.generalRkNumber",model.getGeneralRkNumberLabel());
		assertEquals("cuhu.rebuildingkits.pvc",model.getPermanentVolumeConversionLabel());
		assertEquals("cuhu.rebuildingkits.rebuildingkits",model.getRebuildingKitsLabel());
		assertEquals("cuhu.rebuildingkits.order",model.getOrder());
		assertEquals("cuhu.rebuildingkits.hideAndShowCta",model.getHideAndShowCta());
		assertEquals("cuhu.rebuildingkits.setDefaultFilterCTA",model.getSetDefaultFilterCTA());
		assertEquals("cuhu.rebuildingkits.dateFilter.fromDateText",model.getFromDateText());
		assertEquals("cuhu.rebuildingkits.dateFilter.toDateText",model.getToDateText());
		assertEquals("cuhu.rebuildingkits.dateFilter.invalidDateText",model.getInvalidDateText());
		assertEquals("cuhu.rebuildingkits.dateFilter.pastDateErrorText",model.getPastDateErrorText());
		assertEquals("cuhu.rebuildingkits.dateFilter.dateLaterThanError",model.getDateLaterThanError());
		assertEquals("cuhu.rebuildingkits.dateFilter.dateBeforeThanError",model.getDateBeforeThanError());
	}
	
	/**
	 * Test tool tip i18n keys messages.
	 */
	@Test
	public void testToolTipi18nKeys() {
		assertEquals("cuhu.rebuildingkits.countryToolTip",model.getCountryToolTip());
		assertEquals("cuhu.rebuildingkits.functionalLocationToolTip",model.getFunctionalLocationToolTip());
		assertEquals("cuhu.rebuildingkits.descToolTip",model.getDescriptionToolTip());
		assertEquals("cuhu.rebuildingkits.machineSystemToolTip",model.getMachineSystemToolTip());
		assertEquals("cuhu.rebuildingkits.serialNumberToolTip",model.getSerialNumberToolTip());
		assertEquals("cuhu.rebuildingkits.statusToolTip",model.getStatusToolTip());
		assertEquals("cuhu.rebuildingkits.rkNumberToolTip",model.getRkNumberToolTip());
		assertEquals("cuhu.rebuildingkits.rkDescToolTip",model.getRkDescriptionToolTip());
		assertEquals("cuhu.rebuildingkits.implStatusToolTip",model.getImplStatusToolTip());
		assertEquals("cuhu.rebuildingkits.implDateToolTip",model.getImplDateToolTip());
		assertEquals("cuhu.rebuildingkits.implStatusDateToolTip",model.getImplStatusDateToolTip());
		assertEquals("cuhu.rebuildingkits.rkTypeToolTip",model.getRkTypeToolTip());
		assertEquals("cuhu.rebuildingkits.rkStatusToolTip",model.getRkStatusToolTip());
		assertEquals("cuhu.rebuildingkits.rkHandlingToolTip",model.getRkHandlingToolTip());
		assertEquals("cuhu.rebuildingkits.implDeadlineToolTip",model.getImplDeadlineToolTip());
		assertEquals("cuhu.rebuildingkits.plannedDateToolTip",model.getPlannedDateToolTip());
		assertEquals("cuhu.rebuildingkits.releaseDateToolTip",model.getReleaseDateToolTip());
		assertEquals("cuhu.rebuildingkits.generalRkNumToolTip",model.getGeneralRkNumberToolTip());
		assertEquals("cuhu.rebuildingkits.orderToolTip",model.getOrderToolTip());
	}
	
	/**
	 * Test Api.
	 */
	@Test
	public void testApi() {
		assertTrue(model.getCountryApi().contains("installedbase/rebuildingkits/countries"));
		assertTrue(model.getRebuildingKitsListApi().contains("installedbase/rebuildingkits"));
		assertEquals(RESOURCE_PATH+CustomerHubConstants.EXCEL_DOWNLOAD_EXTENSION,model.getDownloadCsvServletUrl());
	}
}
