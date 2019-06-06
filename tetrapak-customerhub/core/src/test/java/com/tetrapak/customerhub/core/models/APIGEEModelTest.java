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

    private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/global/en";
    private static final String COMPONENT_PATH = "/content/tetrapak/customerhub/global/en/ordering/jcr:content/root/responsivegrid/orderingcard";
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
        Assert.assertEquals("api mappings","{\"token-generator\":\"/bin/customerhub/token-generator\",\"auth-token\":\"https://api-mig.tetrapak.com//oauth2/v2/token\",\"orderingcard\":\"https://api-mig.tetrapak.com/orders/history\",\"ordersearch\":\"https://api-mig.tetrapak.com/orders/summary\",\"orderdetails-parts\":\"https://api-mig.tetrapak.com/orders/details/parts\",\"orderdetails-packmat\":\"https://api-mig.tetrapak.com/orders/details/packmat\",\"financialstatement-filter\":\"https://api-mig.tetrapak.com/financials/summary\",\"financialstatement-results\":\"https://api-mig.tetrapak.com/financials/results\",\"financialstatement-invoice\":\"https://api-mig.tetrapak.com/demo/cfe/invoices\",\"maintenance-filter\":\"https://api-mig.tetrapak.com/mock/materials/equipments/installations\",\"maintenance-events\":\"https://api-mig.tetrapak.com/orders/service/events\",\"maintenancecard\":\"https://api-mig.tetrapak.com/orders/service/events\",\"documents-filter\":\"https://api-mig.tetrapak.com/mock/materials/equipments/installations\",\"documents\":\"https://api-mig.tetrapak.com/techpub/search\"}", apigeeModel.getApiMappings());
    }

}
