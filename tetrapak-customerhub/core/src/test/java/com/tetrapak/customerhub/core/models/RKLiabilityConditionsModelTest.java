package com.tetrapak.customerhub.core.models;

import com.day.cq.tagging.InvalidTagFormatException;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.security.AccessControlException;

import static org.junit.Assert.assertEquals;

public class RKLiabilityConditionsModelTest {

    private static final String TEST_CONTENT = "liability-conditions-page.json";

    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/installed-equipments/rebuilding-kits";

    /** The Constant RESOURCE_PATH. */
    private static final String RESOURCE_PATH = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/rkliabilitycondition";

    private static final String I18N_KEYS = "{\"rkMandatoryKitsText\":\"cuhu.rkliabilityconditions.mandatorykits\"}";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(TEST_CONTENT, TEST_CONTENT_ROOT);

    private RKLiabilityConditionsModel model;

    @Before
    public void setUp() throws Exception {
        Resource resource = aemContext.currentResource(RESOURCE_PATH);
        aemContext.request().setResource(resource);
        model = resource.adaptTo(RKLiabilityConditionsModel.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testModel() throws AccessControlException, InvalidTagFormatException {

        assertEquals("cuhu.rkliabilityconditions.mandatorykits", model.getRkMandatoryKitsText());
        assertEquals("/content/tetrapak/customerhub/global/en/installed-equipments/rebuilding-kits/_jcr_content/root/responsivegrid/rkliabilitycondition.getpdflinks.json", model.getAPIPath());
        assertEquals(I18N_KEYS, model.getI18nKeys());
    }


}
