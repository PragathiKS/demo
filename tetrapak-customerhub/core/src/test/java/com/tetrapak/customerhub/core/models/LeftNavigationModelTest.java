package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.beans.LeftNavigationBean;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

public class LeftNavigationModelTest {

    private LeftNavigationModel leftNavigationModel = null;
    private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/global";
    private static final String RESOURCE_JSON = "allContent.json";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON, CONTENT_ROOT);

    @Before
    public void setup() {
        Resource resource = aemContext.currentResource(CONTENT_ROOT);
        leftNavigationModel = resource.adaptTo(LeftNavigationModel.class);
    }

    @Test
    public void testGetStartedMessage() {
        Assert.assertEquals("My Tetra Pak", leftNavigationModel.getNavHeading());
        Assert.assertEquals("Close", leftNavigationModel.getCloseBtnText());
        List<LeftNavigationBean> list = leftNavigationModel.getLeftNavItems();
        LeftNavigationBean leftNavigationBean = list.get(0);
        Assert.assertEquals("/content/tetrapak/customerhub/global/dashboard.html", leftNavigationBean.getHref());
        Assert.assertEquals("icon-Dashboard", leftNavigationBean.getIconClass());
        Assert.assertEquals("DASHBOARD", leftNavigationBean.getIconLabel());
        Assert.assertEquals(false, leftNavigationBean.isExternalLink());
        Assert.assertEquals(false, leftNavigationBean.isActive());

        LeftNavigationBean leftNavigationBeanWithSubMenu = list.get(3);
        List<LeftNavigationBean> subMenuList = leftNavigationBeanWithSubMenu.getSubMenuList();
        LeftNavigationBean leftNavigationBeanFromSubMenu = subMenuList.get(0);
        Assert.assertEquals("/content/tetrapak/customerhub/global/ordering/order-history.html", leftNavigationBeanFromSubMenu.getHref());
        Assert.assertEquals("Order History", leftNavigationBeanFromSubMenu.getIconLabel());
        Assert.assertEquals(false, leftNavigationBeanFromSubMenu.isExternalLink());
        Assert.assertEquals(false, leftNavigationBeanFromSubMenu.isActive());

        LeftNavigationBean bean = leftNavigationModel.getTpLogoListItem();
        Assert.assertEquals("https://www.tetrapak.com", bean.getHref());
        Assert.assertEquals("icon-TetraPak", bean.getIconClass());
        Assert.assertEquals("Tetra Pak Home", bean.getIconLabel());
        Assert.assertEquals(true, bean.isExternalLink());
        Assert.assertEquals(false, bean.isActive());

    }
}
