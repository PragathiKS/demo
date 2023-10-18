package com.tetrapak.publicweb.core.models.v2;

import com.tetrapak.publicweb.core.models.v2.HeaderConfigurationModel;
import com.tetrapak.publicweb.core.models.LinkModel;
import com.tetrapak.publicweb.core.models.MainNavigationLinkModel;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class HeaderConfigurationModelTest {

    @Rule
    public AemContext context = new AemContext();

    private Resource resource;

    private static final String RESOURCE_CONTENT = "/headerv2/headerconfigurationv2.json";

    private static final String TEST_CONTENT_ROOT = "/content/publicweb/en";

    private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/headerconfiguration";

    private HeaderConfigurationModel model;


    @Before
    public void setUp() throws Exception {

        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.addModelsForClasses(com.tetrapak.publicweb.core.models.HeaderConfigurationModel.class);

        resource = context.currentResource(RESOURCE);
        model = resource.adaptTo(HeaderConfigurationModel.class);
    }

    @Test
    public void simpleLoadAndGettersTest() throws Exception {
        assertEquals("/content/tetrapak/publicweb/lang-masters/en/home", model.getLogoLink());
        assertEquals("Logo", model.getLogoAlt());
        assertEquals("https://mypages.tetrapak.com", model.getLoginLink());
        assertEquals("My Tetra Pak", model.getLoginLabel());
        List<LinkModel> secondaryNavigationLinks = model.getSecondaryNavigationLinks();
        assertEquals(secondaryNavigationLinks.size(),2);
        List<MainNavigationLinkModel> mainNavigationLinks = model.getMainNavigationLinks();
        assertEquals(mainNavigationLinks.size(),4);
        assertEquals(secondaryNavigationLinks.size(),2);
        assertEquals("/content/dam/tetrapak/publicweb/global/header/header.png", model.getLogoImagePath());
        assertEquals("/content/tetrapak/publicweb/lang-masters/en/search", model.getSearchPage());
        assertEquals(false, model.getMarketSelectorDisabled());
    }
}
