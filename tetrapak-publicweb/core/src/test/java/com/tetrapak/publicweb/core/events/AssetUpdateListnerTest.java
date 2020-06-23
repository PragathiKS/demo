package com.tetrapak.publicweb.core.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.tetrapak.publicweb.core.constants.PWConstants;

@RunWith(MockitoJUnitRunner.class)
public class AssetUpdateListnerTest {
   
    
    /** The resolver factory. */
    @Mock
    private ResourceResolverFactory resolverFactory;

    /** The resource resolver. */
    @Mock
    private ResourceResolver resourceResolver;
    
    @Mock
    ResourceChange change;
    
    @Mock
    Resource assetJcrResource;
    
    @Mock
    ValueMap valueMap;
    
    @Mock
    ModifiableValueMap modificableMap;

    /** The site search servlet. */
    @InjectMocks    
    private AssetUpdateListner listner = new AssetUpdateListner();
    
    /**
     * Sets the up.
     *
     * @param context
     *            the new up
     * @throws Exception
     *             the exception
     */
    @SuppressWarnings("deprecation")
    @Before
    public void setUp() throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        MockitoAnnotations.initMocks(this);
        paramMap.put(ResourceResolverFactory.SUBSERVICE, "tetrapak-system-user");
        Mockito.when(resolverFactory.getServiceResourceResolver(paramMap)).thenReturn(resourceResolver);
        Set<String> addedProps = new HashSet<>();
        addedProps.add(PWConstants.JCR_LAST_MODIFIED);
        Mockito.when(change.getAddedPropertyNames()).thenReturn(addedProps);
        Mockito.when(change.getPath()).thenReturn("/content/dam/tetrapak/publicweb/qa/coconut_water_banner.jpg/jcr:content");
        Mockito.when(resourceResolver.getResource("/content/dam/tetrapak/publicweb/qa/coconut_water_banner.jpg/jcr:content")).thenReturn(assetJcrResource);
        Mockito.when(assetJcrResource.getValueMap()).thenReturn(valueMap);
        Mockito.when(valueMap.containsKey(PWConstants.JCR_LAST_MODIFIED)).thenReturn(true);
        Mockito.when(assetJcrResource.adaptTo(ModifiableValueMap.class)).thenReturn(modificableMap);
    }
    
    /**
     * Test model, resource and all getters of the ContactAnchorLink model.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testOnChange() throws Exception {
        List<ResourceChange> changes = new ArrayList<>();
        changes.add(change);
        listner.onChange(changes);
    }

}
