package com.tetrapak.supplierportal.core.models;

import com.tetrapak.supplierportal.core.beans.HeaderBean;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import junit.framework.Assert;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;


@ExtendWith({ MockitoExtension.class, AemContextExtension.class})
public class HeaderModelTest {

    private final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    @BeforeEach
    public void setup(){
        context.load().json("/com/tetrapak/supplierportal/core/models/header/header.json", "/content/supplierportal/en");
    }

    @Test
//    @Disabled
    public void testMessage() {
//        Resource resource = context.resourceResolver().getResource("/content/supplierportal/en/jcr:content/headerconfiguration");
//        Assert.assertNotNull(resource);
//        context.currentResource(resource);
//
//        HeaderModel headerModel = resource.adaptTo(HeaderModel.class);
//        Assert.assertEquals("desktop logo link", "/content/tetrapak/supplierportal/global/en/dashboard.html", headerModel.getDLogoLink());
//        Assert.assertEquals("mobile logo link", "/content/tetrapak/supplierportal/global/en/dashboard.html", headerModel.getMLogoLink());
//        Assert.assertTrue("list size should not be 0", headerModel.getHeaderNavLinks().size() > 0);
//        Assert.assertEquals("list size should be 5", 5, headerModel.getHeaderNavLinks().size());
//        List<HeaderBean> list = headerModel.getHeaderNavLinks();
//        HeaderBean headerBean = list.get(0);
//        Assert.assertEquals("href", "https://www.tetrapak.com/processing.html", headerBean.getHref());
//        Assert.assertEquals("href", "Processing", headerBean.getName());
//        Assert.assertTrue("this should be true", headerBean.isTargetNew());
    }

}
