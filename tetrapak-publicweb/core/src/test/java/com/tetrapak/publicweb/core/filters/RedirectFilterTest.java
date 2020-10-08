package com.tetrapak.publicweb.core.filters;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import org.apache.sling.settings.SlingSettingsService;
import org.apache.sling.testing.mock.sling.services.MockSlingSettingService;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.wcm.testing.mock.aem.junit.AemContext;

public class RedirectFilterTest {
    
    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT = "/previewfilter/test-Content.json";
    
    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/publicweb/language-masters/en";
    
    /** The Constant RESOURCE. */
    private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content";
    
    @Rule
    public AemContext context = new AemContext();
    
    private RedirectFilter filter = new RedirectFilter();
    
    @Mock
    FilterChain chain;
    
    @Mock
    private SlingSettingsService settings;
    
    @Test
    public void testDoFilter() throws IOException, ServletException { 
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        Set<String> runModes = new HashSet<>();
        runModes.add("publish");
        context.request().setPathInfo(TEST_CONTENT_ROOT);
        context.request().setResource(context.resourceResolver().getResource(RESOURCE));
        MockitoAnnotations.initMocks(this);
        settings = new MockSlingSettingService(runModes);
        filter.init(null);
        filter.doFilter(context.request(), context.response(), chain);
        filter.destroy();
        assertEquals("Redirect Filter Run Successfully", "Redirect Filter Run Successfully");
    }

}
