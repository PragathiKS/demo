package com.tetrapak.customerhub.core.services.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Locale;

import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.day.cq.search.QueryBuilder;
import com.day.cq.tagging.TagManager;
import com.tetrapak.customerhub.core.exceptions.KeylinesException;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.services.config.KeylinesConfiguration;

import io.wcm.testing.mock.aem.junit.AemContext;

public class KeylinesServiceImplTest {

    /** The Constant TEST_CONTENT. */
    private static final String TEST_CONTENT = "keylines.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/customerhub/content-components/en/keyline";

    /** The Constant RESOURCE_PATH. */
    private static final String RESOURCE_PATH = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/keylines";

    @Mock
    private KeylinesConfiguration configuration;

    @Mock
    private TagManager tagManager;

    @Mock
    private ResourceResolver resourceResolver;

    @Mock
    private QueryBuilder queryBuilder;
    
    @Mock
    private Session session;

    @Spy
    @InjectMocks
    private KeylinesServiceImpl keylinesServiceImpl = new KeylinesServiceImpl();

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(TEST_CONTENT, TEST_CONTENT_ROOT);

    @Before
    public void setUp() throws Exception {
	MockitoAnnotations.initMocks(this);
	aemContext.registerService(KeylinesConfiguration.class, configuration);
	when(configuration.path()).thenReturn("/content/dam/tetrapak/media-box/global/en/keylines");
	when(configuration.type()).thenReturn("dam:Asset");
	aemContext.registerService(KeylinesServiceImpl.class, keylinesServiceImpl);
	Mockito.when(resourceResolver.adaptTo(TagManager.class)).thenReturn(tagManager);
	Mockito.when(resourceResolver.adaptTo(QueryBuilder.class)).thenReturn(queryBuilder);
	Mockito.when(resourceResolver.adaptTo(Session.class)).thenReturn(session);

    }

    @Test
    public void testGetKeylines() throws KeylinesException {
	Resource resource = aemContext.currentResource(RESOURCE_PATH);
	aemContext.request().setResource(resource);
	ArrayList<String> shapes = new ArrayList<String>();
	shapes.add("tetrapak:keylines/tetra-rex/mid");
	shapes.add("tetrapak:keylines/tetra-rex/base");
	keylinesServiceImpl.getKeylines(resourceResolver, "tetrapak:keylines/tetra-rex", shapes, new Locale("en"));

    }

    @Test(expected = KeylinesException.class)
    public void testGetKeylinesEmpty() throws KeylinesException {
	Resource resource = aemContext.currentResource(RESOURCE_PATH);
	aemContext.request().setResource(resource);
	ArrayList<String> shapes = new ArrayList<String>();
	keylinesServiceImpl.getKeylines(resourceResolver, "tetrapak:keylines/tetra-rex", shapes, new Locale("en"));

    }
}
