package com.tetrapak.publicweb.core.servlets;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.publicweb.core.mock.MockHelper;
import com.tetrapak.publicweb.core.services.DamUtilityService;
import com.tetrapak.publicweb.core.services.impl.DamUtilityServiceImpl;
import io.wcm.testing.mock.aem.junit.AemContext;

public class DamMetaDataAssetInfoServletTest {
    
	/** The Constant TEST_RESOURCE_CONTENT. */
    private static final String TEST_DAM_ASSET_CONTENT = "/damUtility/assets_pdf.json";

    /** The Constant TEST_RESOURCE_CONTENT. */
    private static final String TEST_DAM_ASSET_ROOT = "/content/dam/tetrapak/publicweb";

    /** The context. */
    @Rule
    public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    private DamMetaDataAssetInfoServlet damMetaDataAssetInfoServlet = new DamMetaDataAssetInfoServlet();

    private DamUtilityService damUtilityService;

    /**
     * Setup.
     */
    @Before
    public void setup() {
    	damUtilityService = new DamUtilityServiceImpl();
        Map<String, Object> config = new HashMap<>();
        config.put("DamUtilityRootPath", TEST_DAM_ASSET_ROOT);
        context.registerService(DamUtilityService.class, damUtilityService);
        context.getService(DamUtilityService.class);
        MockOsgi.activate(context.getService(DamUtilityService.class), context.bundleContext(), config);
        damMetaDataAssetInfoServlet = MockHelper.getServlet(context, DamMetaDataAssetInfoServlet.class);
        context.load().json(TEST_DAM_ASSET_CONTENT, TEST_DAM_ASSET_ROOT);
        context.request().setResource(context.currentResource());
    }

    /**
     * Do get
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void doGet(){
    	damMetaDataAssetInfoServlet.doGet(context.request(), context.response());
        assertEquals(
                "{\"publicweb\":[{\"assetSize\":\"0.88 MB\",\"assetPath\":\"/content/dam/tetrapak/publicweb/Cap_GCLP_English.pdf\",\"assetTitle\":\"Cap policy pdf\"}]}",
                context.response().getOutputAsString());
    }
}
