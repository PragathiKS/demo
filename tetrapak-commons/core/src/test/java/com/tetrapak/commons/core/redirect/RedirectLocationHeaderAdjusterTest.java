package com.tetrapak.commons.core.redirect;

import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

public class RedirectLocationHeaderAdjusterTest {
    @InjectMocks
    private final RedirectLocationHeaderAdjuster redirectLocationHeaderAdjuster = new RedirectLocationHeaderAdjuster();

    @Rule
    public AemContext context = new AemContext();

    private String targetLocationWithHtml = "/content/tetrapak/publicweb/global/en/solutions/automation/connected-package.html";
    private String targetLocationWithOutHtml = "/content/tetrapak/publicweb/global/en/solutions/automation/connected-package";

    MockSlingHttpServletRequest request = context.request();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }



    @Test
    public void testAdjustWithHtml() {
        context.request().setPathInfo(targetLocationWithHtml);
        assertEquals("Redirection Location Header Test", "/content/tetrapak/publicweb/global/en/solutions/automation/connected-package", redirectLocationHeaderAdjuster.adjust(request,targetLocationWithHtml));
    }

    @Test
    public void testAdjustWithOutHtml() {
        context.request().setPathInfo(targetLocationWithOutHtml);
        assertEquals("Redirection Location Header Test", "/content/tetrapak/publicweb/global/en/solutions/automation/connected-package", redirectLocationHeaderAdjuster.adjust(request,targetLocationWithOutHtml));
    }

}
