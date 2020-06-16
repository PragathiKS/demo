package com.tetrapak.publicweb.core.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.wcm.testing.mock.aem.junit.AemContext;

public class AssetUpdateListnerTest {
    
    @Rule
    public AemContext context = new AemContext();
    
    AssetUpdateListner listner = new AssetUpdateListner();
    
    @Mock
    ResourceResolverFactory resolverFactory;
    
    @Mock
    ResourceResolver resolver;
    
    /**
     * Sets the up.
     *
     * @param context
     *            the new up
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Mockito.when(resolverFactory
                .getServiceResourceResolver(new HashMap<String, Object>())).thenReturn(context.resourceResolver());
    }
    
    /**
     * Test model, resource and all getters of the ContactAnchorLink model.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testOnChange() throws Exception {
        
        ResourceChange change = new ResourceChange( ResourceChange.ChangeType.CHANGED, "/content/dam/tetrapak/publicweb/qa/coconut_water_banner.jpg/jcr:content", false);
        List<ResourceChange> changes = new ArrayList<>();
        changes.add(change);
        listner.onChange(changes);
        
    }

}
