package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class FinancialStatementModelTest {

    private FinancialStatementModel financialStatementModel = null;
    private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/en";
    private static final String COMPONENT_PATH = "/content/tetrapak/customerhub/en/financials/jcr:content/root/responsivegrid/financialstatement";
    private static final String RESOURCE_JSON = "allContent.json";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON, CONTENT_ROOT);

    @Before
    public void setup() {
        Resource resource = aemContext.currentResource(COMPONENT_PATH);
        financialStatementModel = resource.adaptTo(FinancialStatementModel.class);
    }

    @Test
    public void testGetConfigMessage() {
        String config = financialStatementModel.getConfig();
        Assert.assertTrue(config.contains("Search statement"));
        Assert.assertTrue(config.contains("Reset search"));
        Assert.assertTrue(config.contains("Statement Summary"));
        Assert.assertTrue(config.contains("Find customer"));
        Assert.assertTrue(config.contains("Document Number"));  
        Assert.assertTrue(config.contains("Create PDF"));
    }
}