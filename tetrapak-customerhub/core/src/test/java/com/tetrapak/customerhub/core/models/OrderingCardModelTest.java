package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import com.tetrapak.customerhub.core.services.impl.UserPreferenceServiceImpl;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
@Ignore
public class OrderingCardModelTest {

    private OrderingCardModel orderingCardModel = null;
    private UserPreferenceService userPreferenceService;
    private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/global";
    private static final String COMPONENT_PATH = "/content/tetrapak/customerhub/global/ordering/jcr:content/root/responsivegrid/orderingcard";
    private static final String RESOURCE_JSON = "allContent.json";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContextWithJcrMock(RESOURCE_JSON, CONTENT_ROOT);

    @Before
    public void setup() {
        aemContext.load().json("/" + "user.json", "/home");
        userPreferenceService = new UserPreferenceServiceImpl();

        aemContext.registerInjectActivateService(userPreferenceService);

        Resource resource = aemContext.currentResource(COMPONENT_PATH);
        orderingCardModel = resource.adaptTo(OrderingCardModel.class);
    }

    @Test
    public void testModel() {
        String i18nKeys = orderingCardModel.getI18nKeys();
        Assert.assertTrue(i18nKeys.contains("cuhu.ordering.title"));
        Assert.assertEquals(1, orderingCardModel.getDisabledFields().size());
        Assert.assertEquals(3, orderingCardModel.getDefaultFields().size());
        Assert.assertEquals("/content/tetrapak/customerhub/global/ordering/jcr:content/root/responsivegrid/orderingcard.preference.json", orderingCardModel.getPreferencesURL());
        Assert.assertEquals("/content/tetrapak/customerhub/global/ordering/order-history", orderingCardModel.getAllOrdersLink());
        Assert.assertEquals("/content/tetrapak/customerhub/global/ordering/order-history", orderingCardModel.getOrderDetailLink());
        Assert.assertEquals(6, orderingCardModel.getSavedPreferences().size());
    }
}
