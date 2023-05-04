package com.tetrapak.customerhub.core.servlets;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;

import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.DamUtilityService;
import com.tetrapak.customerhub.core.services.impl.DamUtilityServiceImpl;
import io.wcm.testing.mock.aem.junit.AemContext;

public class DamMetaDataAssetInfoServletTest {

	private static final String PAYLOAD_ASSET_PATH = "/assets_pdf.json";

    /** The context. */
    @Rule
    public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    private DamMetaDataAssetInfoServlet damMetaDataAssetInfoServlet = new DamMetaDataAssetInfoServlet();

    private DamUtilityService damUtilityService;
    
    /**
     * Setup.
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    @Before
    public void setup() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    	damUtilityService = new DamUtilityServiceImpl();
        Map<String, Object> config = new HashMap<>();
        config.put("DamUtilityRootPath", CustomerHubConstants.PW_CONTENT_DAM_PATH);
        context.registerService(DamUtilityService.class, damUtilityService);
        context.getService(DamUtilityService.class);
        MockOsgi.activate(context.getService(DamUtilityService.class), context.bundleContext(), config);
        
        damMetaDataAssetInfoServlet = getServlet(context, DamMetaDataAssetInfoServlet.class);
        context.load().json(PAYLOAD_ASSET_PATH, CustomerHubConstants.PW_CONTENT_DAM_PATH);
        context.currentResource(context.resourceResolver().getResource(CustomerHubConstants.PW_CONTENT_DAM_PATH));
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
                "{\"customerhub\":[{\"assetSize\":\"0,88 MB\",\"assetPath\":\"/content/dam/tetrapak/customerhub/Cap_GCLP_English.pdf\",\"assetTitle\":\"Cap policy pdf\"}]}",
                context.response().getOutputAsString());
    }
    
    @SuppressWarnings("unchecked")
    public static <T extends SlingSafeMethodsServlet> T getServlet(final AemContext context,
            final Class<T> servletName) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Servlet servlet;
        	Servlet finalServlet = null;
            servlet = (Servlet) Class.forName(servletName.getName()).newInstance();
            context.registerInjectActivateService(servlet);
            for (final Servlet servletService : context.getServices(Servlet.class, null)) {
                if (servletName.getName().equals(servletService.getClass().getName())) {
                    //return (T) servletService;
                	finalServlet = (T) servletService;
                }
            }
        return (T) finalServlet;
    }
}