package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.beans.LeftNavigationBean;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

/**
 * @author Nitin Kumar
 */
public class LeftNavigationModelTest {

    private LeftNavigationModel leftNavigationModel = null;
    private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/global/en";
    private static final String RESOURCE_JSON = "allContent.json";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON, CONTENT_ROOT);

    @Before
    public void setup() {
        aemContext.currentResource(CONTENT_ROOT);
        leftNavigationModel = aemContext.request().adaptTo(LeftNavigationModel.class);
    }

    @Test
    public void testGetStartedMessage() {
        Assert.assertEquals("Heading", "My Tetra Pak", leftNavigationModel.getNavHeading());
        Assert.assertEquals("close text", "Close", leftNavigationModel.getCloseBtnText());
        Assert.assertEquals("locale", "en", leftNavigationModel.getLocale());
        List<LeftNavigationBean> list = leftNavigationModel.getLeftNavItems();
        LeftNavigationBean leftNavigationBean = list.get(0);
        Assert.assertEquals("href", "/content/tetrapak/customerhub/global/en/dashboard.html", leftNavigationBean.getHref());
        Assert.assertEquals("icon class", "icon-Dashboard", leftNavigationBean.getIconClass());
        Assert.assertEquals("icon label", "DASHBOARD", leftNavigationBean.getIconLabel());
        Assert.assertEquals("is external link", false, leftNavigationBean.isExternalLink());
        Assert.assertEquals("is active", false, leftNavigationBean.isActive());
        Assert.assertEquals("is expanded", false, leftNavigationBean.isExpanded());

        LeftNavigationBean leftNavigationBeanWithSubMenu = list.get(3);
        List<LeftNavigationBean> subMenuList = leftNavigationBeanWithSubMenu.getSubMenuList();
        LeftNavigationBean leftNavigationBeanFromSubMenu = subMenuList.get(0);
        Assert.assertEquals("href", "/content/tetrapak/customerhub/global/en/ordering/order-history.html", leftNavigationBeanFromSubMenu.getHref());
        Assert.assertEquals("icon label", "Order History", leftNavigationBeanFromSubMenu.getIconLabel());
        Assert.assertEquals("is external link", false, leftNavigationBeanFromSubMenu.isExternalLink());
        Assert.assertEquals("is active", false, leftNavigationBeanFromSubMenu.isActive());

    }
}
