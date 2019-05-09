package com.tetrapak.customerhub.core.models;

import static org.junit.Assert.assertNotNull;

import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * Test class for Error Model
 * @author ruhsharm
 *
 */
public class ErrorModelTest {
    
    private ErrorModel errorModel = null;
    private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/en";
    private static final String ERROR_CONTENT_ROOT = "/content/tetrapak/customerhub/en/error-404/jcr:content/root/responsivegrid/error";
    private static final String ERROR_JSON = "allContent.json";
    
    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(ERROR_JSON, CONTENT_ROOT);
    
    @Before
    public void setup() {
        Resource errorResource = aemContext.currentResource(ERROR_CONTENT_ROOT);
        errorModel = errorResource.adaptTo(ErrorModel.class);
    }
    
    @Test
    public void testGetStartedMessage() {
        String heading = errorModel.getErrorTitle();
        assertNotNull(heading);
        Assert.assertEquals("Oops... Page Not found", errorModel.getErrorTitle());
        Assert.assertEquals("Error 404", errorModel.getErrorCode());
        String imagePath = errorModel.getImagePath();
        assertNotNull(imagePath);
        Assert.assertEquals("Error Cuhu", errorModel.getImageAltText());
        assertNotNull(errorModel.getErrorDescription());
    }
    
}
