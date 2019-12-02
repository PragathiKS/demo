package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test class for Financial Model
 * @author ruhsharm
 *
 */
public class FinancialStatementModelTest {

    private FinancialStatementModel financialStatementModel = null;
    private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/global/en";
    private static final String COMPONENT_PATH = "/content/tetrapak/customerhub/global/en/financials/jcr:content/root/responsivegrid/financialstatement";
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
        String config = financialStatementModel.getI18nKeys();
        Assert.assertTrue("Search statement",config.contains("Search statement"));
        Assert.assertTrue("Reset search",config.contains("Reset search"));
        Assert.assertTrue("Statement Summary",config.contains("Statement Summary"));
        Assert.assertTrue("Find customer",config.contains("Find customer"));
        Assert.assertTrue("Document Number",config.contains("Document Number"));
        Assert.assertTrue("Create PDF",config.contains("Create PDF"));
        Assert.assertTrue("date range error",config.contains("Please enter a valid date"));
        String url = financialStatementModel.getDownloadPdfExcelServletUrl();
        Assert.assertEquals("URL","/content/tetrapak/customerhub/global/en/financials/jcr:content/root/responsivegrid/financialstatement.download.{extnType}", url);
        
    }
}
