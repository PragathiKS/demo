package com.tetrapak.supplierportal.core.models;

import com.tetrapak.supplierportal.core.beans.HeaderBean;
import com.tetrapak.supplierportal.core.mock.SupplierPortalCoreAemContext;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;


@Ignore
public class HeaderModelTest {

    private HeaderModel headerModel = null;
    private static final String CONTENT_ROOT = "/content/tetrapak/supplierportal/global/en/dashboard/jcr:content";
    private static final String RESOURCE_JSON = "header.json";

    @Rule
    public final AemContext aemContext = SupplierPortalCoreAemContext.getAemContext(RESOURCE_JSON, "/content/tetrapak/supplierportal");

    @Before
    public void setup() {
        aemContext.currentResource(CONTENT_ROOT);
        headerModel = aemContext.request().adaptTo(HeaderModel.class);
    }

    @Test
    public void testMessage() {
        Assert.assertEquals("desktop logo link", "/content/tetrapak/supplierportal/global/en/dashboard.html", headerModel.getDLogoLink());
        Assert.assertEquals("mobile logo link", "/content/tetrapak/supplierportal/global/en/dashboard.html", headerModel.getMLogoLink());
        Assert.assertTrue("list size should not be 0", headerModel.getHeaderNavLinks().size() > 0);
        Assert.assertEquals("list size should be 5", 5, headerModel.getHeaderNavLinks().size());
        List<HeaderBean> list = headerModel.getHeaderNavLinks();
        HeaderBean headerBean = list.get(0);
        Assert.assertEquals("href", "https://www.tetrapak.com/processing.html", headerBean.getHref());
        Assert.assertEquals("href", "Processing", headerBean.getName());
        Assert.assertTrue("this should be true", headerBean.isTargetNew());
    }

}
