package com.tetrapak.customerhub.core.services.impl;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.DamUtilityService;
import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class SubscriptionMailServiceImplTest.
 */
public class DamUtilityServiceImplTest {

    /** The context. */
    @Rule
    public AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    /** The job manager. */
    @Mock
    private JobManager jobManager;

    /** The resource. */
    private Resource resource;

    /** The resolver. */
    private ResourceResolver resolver;

    /** The mail service. */
    private final DamUtilityService damUtilityService = new DamUtilityServiceImpl();
    
    private static final String PAYLOAD_ASSET_PATH = "/assets_pdf.json";
    
    @Mock
    private Node node;

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
    	
    	Map<String, Object> config = new HashMap<>();
        config.put("DamUtilityRootPath", CustomerHubConstants.PW_CONTENT_DAM_PATH);
        context.registerService(DamUtilityService.class, damUtilityService);
        context.getService(DamUtilityService.class);
        MockOsgi.activate(context.getService(DamUtilityService.class), context.bundleContext(), config);
        context.load().json(PAYLOAD_ASSET_PATH, CustomerHubConstants.PW_CONTENT_DAM_PATH);
        resource = context.currentResource(CustomerHubConstants.PW_CONTENT_DAM_PATH);
        resolver = context.resourceResolver();
    }

    /**
     * Test get assets from dam.
     * @throws JSONException 
     * @throws RepositoryException 
     * @throws PathNotFoundException 
     * @throws ValueFormatException 
     * @throws JsonProcessingException 
     */
    @Test
    public void testGetAssetsFromDam() throws JsonProcessingException, ValueFormatException, PathNotFoundException, RepositoryException, JSONException {
        Assert.assertEquals(
                context.getService(DamUtilityService.class).getAssetsFromDam(resolver),
                "{\"customerhub\":[{\"assetSize\":\"0.88 MB\",\"assetPath\":\"/content/dam/tetrapak/customerhub/Cap_GCLP_English.pdf\",\"assetTitle\":\"Cap policy pdf\"}]}");
    }


}
