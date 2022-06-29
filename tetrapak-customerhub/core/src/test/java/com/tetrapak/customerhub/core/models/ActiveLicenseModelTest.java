
package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for ActiveLicenseModel
 *
 * @author pawan kumar
 */
public class ActiveLicenseModelTest {

    private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/automation-digital/plantmaster_licenses";
    private static final String COMPONENT_PATH = "/content/tetrapak/customerhub/global/en/automation-digital/plantmaster_licenses/jcr:content/root/responsivegrid/plantmasterlicenses_/activeLicense";
    private static final String RESOURCE_JSON = "plantMasterLicenses.json";
    private ActiveLicenseModel activeLicenseModel;

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON, CONTENT_ROOT);

    @Before
    public void setup() {
        Resource resource = aemContext.currentResource(COMPONENT_PATH);
        activeLicenseModel = resource.adaptTo(ActiveLicenseModel.class);
    }

    @Test
    public void testModelGetters() {
        assertEquals("Unexpected value","Active licenses" , activeLicenseModel.getTitle());
        assertEquals("Unexpected value","Annual engineering licenses" , activeLicenseModel.getEngLicenseTitle());
        assertEquals("Unexpected value","Site licenses" , activeLicenseModel.getSiteLicenseTitle());
        assertEquals("Unexpected value","Name" , activeLicenseModel.getName());
        assertEquals("Unexpected value","Licence Key" , activeLicenseModel.getLicenceKey());
        assertEquals("Unexpected value","Platform" , activeLicenseModel.getPlatformText());
        assertEquals("Unexpected value","Start date" , activeLicenseModel.getStartDate());
        assertEquals("Unexpected value","End date" , activeLicenseModel.getEndDate());
        assertEquals("Unexpected value","Withdraw" , activeLicenseModel.getWithdrawBtn());
        assertEquals("Unexpected value","Country" , activeLicenseModel.getCountry());
        assertEquals("Unexpected value","Site" , activeLicenseModel.getSite());
        assertEquals("Unexpected value","Confirm license withdrawal" , activeLicenseModel.getModalConfirmTitle());
        assertEquals("Unexpected value","Comment to Tetra Pak (Optional)" , activeLicenseModel.getModalComments());
        assertEquals("Unexpected value","Are you sure you want to withdraw this license?" , activeLicenseModel.getModalConfirmText());
        assertEquals("Unexpected value","Yes - Confirm withdrawal" , activeLicenseModel.getModalConfirmBtn());
        assertEquals("Unexpected value","No - Go back to listing" , activeLicenseModel.getModalCancelBtn());
        assertEquals("Unexpected value","Licence will soon be withdrawn" , activeLicenseModel.getModalWithdrawSuccessTitle());
        assertEquals("Unexpected value","Your request has been submitted and we will withdraw the license once it has been processed. Thank you." , activeLicenseModel.getModalWithdrawSuccessText());
        assertEquals("Unexpected value","Ok, go back to listing" , activeLicenseModel.getModalWithdrawBack());
        assertEquals("Unexpected value","Annual engineering licenses - Withdraw" , activeLicenseModel.getSubject());
        assertEquals("Unexpected value","Dear" , activeLicenseModel.getSalutation());
        assertEquals("Unexpected value","This is to inform you" , activeLicenseModel.getBody());
        assertEquals("Unexpected value","Date" , activeLicenseModel.getSiteDate());
        assertEquals("Unexpected value","Requestor" , activeLicenseModel.getRequestorText());
        assertEquals("Unexpected value","Username" , activeLicenseModel.getUsernameText());
        assertEquals("Unexpected value","Comments" , activeLicenseModel.getEmailCommentText());
    }
}
