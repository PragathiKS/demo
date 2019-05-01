package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.beans.TabBean;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

public class TabsModelTest {

    private TabsModel tabsModel = null;
    private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/en/installed-equipment";
    private static final String RESOURCE = "/content/tetrapak/customerhub/en/installed-equipment/jcr:content/root/responsivegrid/tabs";
    private static final String TABS_JSON = "tabs.json";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(TABS_JSON, CONTENT_ROOT);

    @Before
    public void setup() {
        Resource resource = aemContext.currentResource(RESOURCE);
        tabsModel = resource.adaptTo(TabsModel.class);
    }

    @Test
    public void testGetStartedMessage() {
        Assert.assertEquals("/content/dam/customerhub/asset.jpg", tabsModel.getImagePath());
        Assert.assertEquals("asset.jpg", tabsModel.getImageAltText());
        Assert.assertEquals("tp-equipment", tabsModel.getComponentClasses());
        List<TabBean> list = tabsModel.getTabsList();
        TabBean bean = list.get(0);
        Assert.assertEquals("icon-document", bean.getIconClass());
        Assert.assertEquals("Documents", bean.getLabelI18n());
        Assert.assertEquals("/content/tetrapak/customerhub/en/installed-equipment.html", bean.getPageUrl());
    }

}
