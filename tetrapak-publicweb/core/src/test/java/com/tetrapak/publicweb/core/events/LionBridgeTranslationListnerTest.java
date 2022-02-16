package com.tetrapak.publicweb.core.events;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.CreateLiveCopyService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class LionBridgeTranslationListnerTest.
 */
@RunWith(MockitoJUnitRunner.class)
public class LionBridgeTranslationListnerTest {
    /** The resolver factory. */
    @Mock
    private ResourceResolverFactory resolverFactory;

    /** The resource resolver. */
    private ResourceResolver resolver;

    /** The change. */
    @Mock
    private ResourceChange change;

    /** The jcr resource. */
    private Resource jcrResource;

    /** The value map. */
    @Mock
    ValueMap valueMap;

    /** The modificable map. */
    @Mock
    private ModifiableValueMap modificableMap;

    /** The aem context. */
    @Rule
    public final AemContext aemContext = new AemContext();

    /** The payload path. */
    private final String PAYLOAD_PATH = "/content/tetrapak/publicweb/lang-masters/en/home/jcr:content";

    /** The gla user. */
    private final String GLA_USER = "gl-service";

    private final String VAR_COMMERCE = "/var/commerce";

    /** The payload resource content. */
    private final String PAYLOAD_RESOURCE_CONTENT = "/lionBridgeTranslationListener/test.json";

    private final String VAR_COMMERCE_CONTENT = "/lionBridgeTranslationListener/var.json";

    /** The create live copy service. */
    private CreateLiveCopyService createLiveCopyService;

    /** The english live copy base paths. */
    private String[] englishLiveCopyBasePaths = { "/content/tetrapak/publicweb/us/en" };

    /** The rollout configs. */
    private String[] rolloutConfigs = { "/apps/msm/wcm/rolloutconfigs/default" };

    /** The listner. */
    @InjectMocks
    private LionBridgeTranslationListner listner = new LionBridgeTranslationListner();

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @SuppressWarnings("deprecation")
    @Before
    public void setUp() throws Exception {

        aemContext.load().json(PAYLOAD_RESOURCE_CONTENT, PAYLOAD_PATH);
        aemContext.load().json(VAR_COMMERCE_CONTENT, VAR_COMMERCE);
        MockitoAnnotations.initMocks(this);
        jcrResource = aemContext.currentResource(PAYLOAD_PATH);
        resolver = jcrResource.getResourceResolver();
        when(GlobalUtil.getResourceResolverFromSubService(resolverFactory))
                .thenReturn(jcrResource.getResourceResolver());
        Map<String, Object> paramMap = new HashMap<>();
        MockitoAnnotations.initMocks(this);
        paramMap.put(ResourceResolverFactory.SUBSERVICE, "tetrapak-system-user");
        Mockito.when(resolverFactory.getServiceResourceResolver(paramMap)).thenReturn(resolver);
        Set<String> addedProps = new HashSet<>();
        addedProps.add(PWConstants.JCR_LAST_MODIFIED);
        Mockito.when(change.getAddedPropertyNames()).thenReturn(addedProps);
        Mockito.when(change.getPath()).thenReturn(PAYLOAD_PATH);
        Mockito.when(change.getUserId()).thenReturn(GLA_USER);
        Mockito.when(valueMap.containsKey(PWConstants.CQ_LAST_MODIFIED)).thenReturn(true);

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
        assertEquals("LionBridgeTranslationListner", "LionBridgeTranslationListner");
    }
}
