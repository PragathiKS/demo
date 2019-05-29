package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class OrderSearchModelTest {

    private OrderSearchModel orderSearchModel = null;
    private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/global";
    private static final String COMPONENT_PATH = "/content/tetrapak/customerhub/global/ordering/order-history/jcr:content/root/responsivegrid/ordersearch";
    private static final String RESOURCE_JSON = "allContent.json";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON, CONTENT_ROOT);

    @Before
    public void setup() {
        Resource resource = aemContext.currentResource(COMPONENT_PATH);
        orderSearchModel = resource.adaptTo(OrderSearchModel.class);
    }

    @Test
    public void testGetStartedMessage() {
        String config = orderSearchModel.getConfig();
        Assert.assertTrue("config should contain search orders string", config.contains("Search Orders"));
        Assert.assertEquals("size", 5, orderSearchModel.getEnabledFields().size());
        Assert.assertEquals("order details link", "/content/tetrapak/customerhub/global/ordering/order-history.html", orderSearchModel.getOrderDetailLink());
    }
}
