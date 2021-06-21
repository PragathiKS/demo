package com.trs.core.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.scene7.api.S7Config;
import com.day.cq.dam.scene7.api.S7ConfigResolver;
import com.day.cq.dam.scene7.api.Scene7Service;
import com.day.cq.search.QueryBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trs.core.utils.TestUtils;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
public class AssetMetadataServiceImplTest {
    
    public static final String SCENE7_CONFIG_PATH = "scene7config";
    public static final String SCENE7_ID = "scene7id";
    
    @Mock
    private QueryBuilder builder;

    @Mock
    private Scene7Service scene7Service;

    @Mock
    private S7ConfigResolver s7ConfigResolver;
    
    @Mock
    private S7Config s7Config;
    
    @InjectMocks
    private final AssetMetadataServiceImpl assetMetadataServiceImpl = new AssetMetadataServiceImpl();
    
    public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);
    
    
    private Resource resource;
    
    private Asset asset;

    @BeforeEach
    void setUp() throws Exception {
        
        MockitoAnnotations.initMocks(this);
        resource = TestUtils.setupAssetMetadataService(context, builder, scene7Service, s7ConfigResolver, s7Config);
        asset = resource.adaptTo(Asset.class);
        
    }

    @Test
    final void testExecuteQuery() {

        Map<String, String> predicate = new HashMap<>();
        predicate.put("path", "/content/dam/training-services");
        predicate.put("nodename", "metadata");
        predicate.put("property", "dam:mlvId");
        predicate.put("property.value", "MLV00121");
        assertEquals(1, (assetMetadataServiceImpl.executeQuery(context.resourceResolver(), predicate)).size(),"Incorrect size of array!");
    }

    @Test
    final void testGetAssetMetadataJsonNode() {
        ObjectMapper mapper = new ObjectMapper();
        assertNotNull(assetMetadataServiceImpl.getAssetMetadataJsonNode(context.resourceResolver(), mapper, TestUtils.TEST_ASSET_PATH),"Return value should not be null");
    }

    @Test
    final void testResolveScene7Configuration() {
       
        assertEquals(s7Config, assetMetadataServiceImpl.resolveScene7Configuration(asset, context.resourceResolver(), s7ConfigResolver),"Incorrect scene7 config");
    }

}
