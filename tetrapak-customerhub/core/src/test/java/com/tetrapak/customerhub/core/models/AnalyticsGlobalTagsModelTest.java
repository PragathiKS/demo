package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Test class for Error Model
 *
 * @author ruhsharm
 */
public class AnalyticsGlobalTagsModelTest {

    private AnalyticsGlobalTagsModel analyticsGlobalTagsModel = null;
    private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/global/en";
    private static final String ERROR_CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/404/jcr:content/root/responsivegrid/error";
    private static final String ERROR_JSON = "allContent.json";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(ERROR_JSON, CONTENT_ROOT);

    @Before
    public void setup() {
        Resource errorResource = aemContext.currentResource(ERROR_CONTENT_ROOT);
        analyticsGlobalTagsModel = errorResource.adaptTo(AnalyticsGlobalTagsModel.class);
    }

    @Test
    public void testGetStartedMessage() {
        Assert.assertEquals("Oops... Page Not found", "Oops... Page Not found", analyticsGlobalTagsModel.getChannel());
    }

}
