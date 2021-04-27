package com.trs.core.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.day.cq.dam.api.AssetManager;
import com.trs.core.exceptions.TaxonomyOperationException;
import com.trs.core.reports.StatefulReport;
import com.trs.core.services.TrsConfigurationService;
import com.trs.core.utils.TestUtils;
import com.trs.core.utils.TrsConstants;

import io.wcm.testing.mock.aem.junit5.AemContext;

public class TaxonomyServiceImplTest {

	@Mock
	TrsConfigurationService trsConfig;

	@Mock
	AssetManager assetManager;

	@InjectMocks
	private final TaxonomyServiceImpl taxonomyServiceImpl = new TaxonomyServiceImpl();

	public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

	private ResourceResolver resourceResolver;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		resourceResolver = context.resourceResolver();
		context.load().json(TaxonomyServiceImplTest.class.getResourceAsStream("videoFolder.json"), "/content/dam/training-services/test");
		TestUtils.setUpTaxonomyService(context);
		TestUtils.setupTrsConfiguration(context, trsConfig);
	}

	@Test
	final void testConvertXylemeTagsToAEMTagsResourceResolverStringMapOfStringStringStatefulReport() {
		StatefulReport tagConversionReport = new StatefulReport.Builder().reportWorkbook().reportWorkBooksheet().build();
		taxonomyServiceImpl.convertXylemeTagsToAEMTags(context.resourceResolver(), TestUtils.TEST_ASSET_PATH, null,
				tagConversionReport);
		assertEquals(0, tagConversionReport.getRowCounter(),
				"There should be no entries in failure report when this operation is success!");
	}

	@Test
	final void testConvertXylemeTagsToAEMTagsResourceResolverStringMapOfStringString() {
		taxonomyServiceImpl.convertXylemeTagsToAEMTags(context.resourceResolver(), TestUtils.TEST_ASSET_PATH, null);
		String[] cqTagsArray = new String[] {
				"trs:ts_level_category/general/tetra_pak_packaging/filling_machine/tetra_pak_a3/tetra_pak_a3_cf/tetra_pak_a3_cf_0200",
				"trs:ts_level_category/general/tetra_pak_packaging/filling_machine/tetra_pak_a3/tetra_pak_a3_cf/tetra_pak_a3_cf_0400" };
		Assertions.assertArrayEquals(
				cqTagsArray, resourceResolver.getResource(TestUtils.TEST_ASSET_PATH).getChild(TrsConstants.METADATA_NODE_RELATIVE_PATH)
						.getValueMap().get(TrsConstants.CQ_TAGS_PROPERTY, String[].class),
				"Unexpected value of cq:tags");
	}

	@Test
	final void testGetXylemeToAEMTagMapping() throws ValueFormatException, RepositoryException, IOException, TaxonomyOperationException {

		Map<String, String> tagMappings = taxonomyServiceImpl.getXylemeToAEMTagMapping(context.resourceResolver());
		assertEquals(
				"trs:ts_level_category/general/tetra_pak_packaging/filling_machine/tetra_pak_a3/tetra_pak_a3_cf/tetra_pak_a3_cf_0400",
				tagMappings.get("46de5d5e-ae5d-4eab-8538-935394b98d75"),"Unexpected value of cq:tag corresponding to Xyleme tag");

	}

	@Test
	final void testCreateTaxonomyServiceReport() {
		StatefulReport taxonomyReport = taxonomyServiceImpl.createTaxonomyServiceReport();
		assertNotNull(taxonomyReport, "Report object should not be null");
	}

}
