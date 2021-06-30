package com.trs.core.servlets;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.day.cq.dam.scene7.api.S7Config;
import com.day.cq.dam.scene7.api.S7ConfigResolver;
import com.day.cq.dam.scene7.api.Scene7Service;
import com.day.cq.search.QueryBuilder;
import com.trs.core.services.TrsConfigurationService;
import com.trs.core.services.impl.AssetMetadataServiceImpl;
import com.trs.core.utils.TestUtils;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class AssetPathExporterServletTest {

    @Mock
    private QueryBuilder builder;

    @Mock
    private Scene7Service scene7Service;

    @Mock
    private S7ConfigResolver s7ConfigResolver;
    
    @Mock
    private S7Config s7Config;
    
    @Mock
    TrsConfigurationService trsConfig;
    
    public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);
    
    private AssetPathExporterServlet assetPathExporterServlet;
    
    @BeforeEach
    void setUp() throws Exception {
        
        MockitoAnnotations.initMocks(this);
        TestUtils.setupAssetMetadataService(context, builder, scene7Service, s7ConfigResolver, s7Config);
        Map<String, Object> props = new HashMap<>();
        context.registerInjectActivateService(new AssetMetadataServiceImpl(), props);
        // registering configuration service
        TestUtils.setupTrsConfiguration(context, trsConfig);
        props.put("sling.servlet.resourceTypes", "trs/components/structure/empty-page");
        props.put("sling.servlet.selectors", "assetdata");
        props.put("sling.servlet.extensions", "html");
        assetPathExporterServlet = context.registerInjectActivateService(new AssetPathExporterServlet(), props);
    }

    @Test
    final void testDoGetSlingHttpServletRequestSlingHttpServletResponse() throws ServletException, IOException {
        MockSlingHttpServletResponse response = context.response();
        MockSlingHttpServletRequest request = context.request();
        request.addRequestParameter("name", "video2.mp4");
        assetPathExporterServlet.doGet(request, response);
        assertEquals("application/json;charset=UTF-8", response.getContentType(),"Incorrect response type");
    }

}
