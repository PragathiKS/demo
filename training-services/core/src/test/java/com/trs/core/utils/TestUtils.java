package com.trs.core.utils;

import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.Session;

import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.mockito.Mockito;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.scene7.api.S7Config;
import com.day.cq.dam.scene7.api.S7ConfigResolver;
import com.day.cq.dam.scene7.api.Scene7Service;
import com.day.cq.replication.Replicator;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.google.common.collect.ImmutableMap;
import com.trs.core.services.TrsConfigurationService;
import com.trs.core.services.impl.AssetMetadataServiceImpl;
import com.trs.core.services.impl.AssetMetadataServiceImplTest;
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

	public static Resource setupAssetMetadataService(AemContext context,QueryBuilder builder,Scene7Service scene7Service,S7ConfigResolver s7ConfigResolver,S7Config s7Config) {
	    
        context.registerService(QueryBuilder.class, builder);
        context.registerService(Scene7Service.class, scene7Service);
        context.registerService(S7ConfigResolver.class, s7ConfigResolver);
        
        context.create().asset(TestUtils.TEST_ASSET_PATH,new ByteArrayInputStream("text".getBytes()),"video/mp4");
        context.create().resource(TestUtils.TEST_ASSET_PATH +"/jcr:content", ImmutableMap.<String, Object>builder()
                .put("jcr:primaryType", "dam:AssetContent")
                .build());
        context.create().resource(TestUtils.TEST_ASSET_PATH +"/jcr:content/metadata", ImmutableMap.<String, Object>builder()
                .put("dam:mlvId", "MLV00121")
                .put(AssetMetadataServiceImpl.DAMSCENE7ID, AssetMetadataServiceImplTest.SCENE7_ID)
                .build());
        Resource resource = context.resourceResolver().getResource(TestUtils.TEST_ASSET_PATH);
        
        Mockito.when(s7ConfigResolver.getS7ConfigPathForResource(context.resourceResolver(), resource)).thenReturn(AssetMetadataServiceImplTest.SCENE7_CONFIG_PATH);
        Mockito.when(s7ConfigResolver.getS7Config(context.resourceResolver(), AssetMetadataServiceImplTest.SCENE7_CONFIG_PATH)).thenReturn(s7Config);
        Mockito.when(scene7Service.getS7AssetMetadata(s7Config, AssetMetadataServiceImplTest.SCENE7_ID)).thenReturn(ImmutableMap.<String, String>builder()
                .put("Video Frame Rate", "12345")
                .build());
        
        Node resultNode = context.resourceResolver().getResource(TestUtils.TEST_ASSET_PATH).adaptTo(Node.class);
        final Query query = Mockito.mock(Query.class);
        Mockito.when(builder.createQuery(Mockito.any(PredicateGroup.class), Mockito.any(Session.class))).then(invocation -> {
            return query;
        });
        SearchResult result = Mockito.mock(SearchResult.class);
        Hit hit = Mockito.mock(Hit.class);
        List<Hit> hits = new ArrayList<Hit>();
        hits.add(hit);
        Mockito.when(result.getNodes()).thenReturn(Arrays.asList(resultNode).iterator());
        Mockito.when(result.getHits()).thenReturn(hits);
        Mockito.when(query.getResult()).thenReturn(result);
        
        return resource;
	    
	}
}
