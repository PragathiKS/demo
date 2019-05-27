package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.MockAPIGEEServiceImpl;
import com.tetrapak.customerhub.core.services.APIGEEService;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class APIGEEModelTest {

    private APIGEEModel apigeeModel;

    private APIGEEService apigeeService;

    private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/global";
    private static final String COMPONENT_PATH = "/content/tetrapak/customerhub/global/ordering/jcr:content/root/responsivegrid/orderingcard";
    private static final String RESOURCE_JSON = "allContent.json";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContextWithJcrMock(RESOURCE_JSON, CONTENT_ROOT);

    @Before
    public void setup() {
        apigeeService = new MockAPIGEEServiceImpl();
        aemContext.registerService(APIGEEService.class, apigeeService);

        Resource resource = aemContext.currentResource(COMPONENT_PATH);
        apigeeModel = resource.adaptTo(APIGEEModel.class);
    }

    @Test
    public void testModel() {
        Assert.assertEquals("api url","https://api-mig.tetrapak.com", apigeeModel.getApiURL());
        Assert.assertEquals("api mappings","{\"token-generator\":\"https://api-mig.tetrapak.combin/customerhub/token-generator,orderingcard\"}", apigeeModel.getApiMappings());
    }

}
