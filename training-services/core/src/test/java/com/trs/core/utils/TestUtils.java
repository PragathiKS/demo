package com.trs.core.utils;

import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;

import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.mockito.Mockito;

import com.day.cq.replication.Replicator;
import com.trs.core.services.TrsConfigurationService;
import com.trs.core.services.impl.AssetPageOpsServiceImplTest;
import com.trs.core.services.impl.TaxonomyServiceImplTest;

import io.wcm.testing.mock.aem.junit5.AemContext;

public class TestUtils {
	
	public final static String TEST_ASSET_PATH = "/content/dam/training-services/test/video2.mp4";
	
	public static void setUpAssetPageOpsService(AemContext context, String assetPath, Replicator replicator) {
		context.load().json(AssetPageOpsServiceImplTest.class.getResourceAsStream("trs.json"), "/content/trs");
		context.load().json(AssetPageOpsServiceImplTest.class.getResourceAsStream("conf.json"), "/conf/trs");
		context.create().asset(assetPath,new ByteArrayInputStream("text".getBytes()),"video/mp4");
		
		context.registerService(Replicator.class, replicator);
		context.registerAdapter(Resource.class, ModifiableValueMap.class, mock(ModifiableValueMap.class));
	}
	
	public static void setupTrsConfiguration(AemContext context, TrsConfigurationService trsConfig) {
		context.registerService(TrsConfigurationService.class, trsConfig);
		Mockito.when(trsConfig.getTrsSitesRootFolderName()).thenReturn("trs");
		Mockito.when(trsConfig.getTrsAssetsRootFolderName()).thenReturn("training-services");
		Mockito.when(trsConfig.getTrsVideoPageTemplatePath()).thenReturn("/conf/trs/settings/wcm/templates/video-page");
		Mockito.when(trsConfig.getTrsEmptyPageTemplatePath()).thenReturn("/conf/trs/settings/wcm/templates/empty-page");
		Mockito.when(trsConfig.getXylemeTagsPropertyName()).thenReturn("dam:xylemeTags");
		Mockito.when(trsConfig.getTrsXylemeMappingsPath()).thenReturn("/content/trs/xyleme-tag-mapping");
		Mockito.when(trsConfig.getXylemeAEMTagMappingFilePath()).thenReturn("/content/dam/training-services/system/mapping.json");
		Mockito.when(trsConfig.getDynamicMediaDomain()).thenReturn("https://s7g10.scene7.com");
		Mockito.when(trsConfig.getPageCreationLogFilePath()).thenReturn("/content/dam/training-services/system/page-creation-author-log.xlsx");
		Mockito.when(trsConfig.getTaxonomyTransformationLogFilePath()).thenReturn("/content/dam/training-services/system/xyleme-tag-transformation-log.xlsx");
	}
	
	public static void setUpTaxonomyService(AemContext context) {
		
		context.load().json(TaxonomyServiceImplTest.class.getResourceAsStream("xylemeTagMappingsAsNode.json"), "/content/trs/xyleme-tag-mapping");
		context.load().json(TaxonomyServiceImplTest.class.getResourceAsStream("xylemeTagMappingsAsFileNode.json"), "/content/dam/training-services/system/mapping.json");
		context.load().binaryFile(TaxonomyServiceImplTest.class.getResourceAsStream("xylemeTagMappingsAsFile.json"), "/content/dam/training-services/system/mapping.json/jcr:content/renditions/original");
		
	}

}
