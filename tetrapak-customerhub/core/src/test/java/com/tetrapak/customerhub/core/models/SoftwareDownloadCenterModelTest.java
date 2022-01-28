package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.sling.api.resource.Resource;

/**
 * Test class for SoftwareDownloadCenterModel class.
 *
 * @author aalekh
 */
public class SoftwareDownloadCenterModelTest {

    private SoftwareDownloadCenterModel softwareDownloadCenterModel;

    private static final String SOFTWARE_DOWNLOAD_CENTER = "/content/tetrapak/customerhub/global/en/automation-digital/software-download-center/jcr:content/root/responsivegrid/softwaredownloadcent";
    private static final String SOFTWARE_DOWNLOAD_RESOURCE_JSON = "softwaredownloadcentercomponent.json";
    private static final String LINK_PATH = "https://tppmdownload.tetrapak.com/";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(SOFTWARE_DOWNLOAD_RESOURCE_JSON, SOFTWARE_DOWNLOAD_CENTER);

    /**
     * Setup method for the class.
     */
    @Before
    public void setUp() throws Exception {
    	Resource resource = aemContext.currentResource(SOFTWARE_DOWNLOAD_CENTER);
        softwareDownloadCenterModel = resource.adaptTo(SoftwareDownloadCenterModel.class);
    }

    /**
     * Test method for SoftwareDownloadCenter model.
     */
    @Test
    public void testSoftwareDownloadCenterModel() {
    	assertNotNull("Link path is not null", softwareDownloadCenterModel.getLinkPath());
        assertEquals("link path", LINK_PATH, softwareDownloadCenterModel.getLinkPath());
    }

}
