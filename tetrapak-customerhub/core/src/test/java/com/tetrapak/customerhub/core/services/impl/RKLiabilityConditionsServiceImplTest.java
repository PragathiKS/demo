package com.tetrapak.customerhub.core.services.impl;

import com.adobe.acs.commons.genericlists.GenericList;
import com.day.cq.wcm.api.Page;
import com.tetrapak.customerhub.core.beans.rebuildingkits.RKLiabilityConditionsPDF;
import com.tetrapak.customerhub.core.exceptions.KeylinesException;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class RKLiabilityConditionsServiceImplTest {

    @InjectMocks
    private RKLiabilityConditionsServiceImpl rkLiabilityConditionsServiceImpl = new RKLiabilityConditionsServiceImpl();

    private static final String PDF_MAPPING_GL_JSON = "/pdfmappinggl.json";

    private static final String RESOURCE_JSON = "liability-conditions-page.json";

    private static final String RESOURCE_PATH = "/content/tetrapak/customerhub/global/en/installed-equipments/rebuilding-kits/jcr:content/root/responsivegrid/rkliabilitycondition";

    private static final String ASSET_1 = "/content/dam/tetrapak/customerhub/english/rebuildingkits/liability-conditions.pdf";

    private static final String ASSET_2 = "/content/dam/tetrapak/customerhub/french/rebuildingkits/liability-conditions.pdf";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON, RESOURCE_PATH);

    @Mock
    private GenericList genericList;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Resource resource = aemContext.currentResource(RESOURCE_PATH);
        aemContext.request().setResource(resource);
        aemContext.load().json("/liability-conditions-english.json", ASSET_1);
        aemContext.load().json("/liability-conditions-french.json", ASSET_2);
        aemContext.load().json(PDF_MAPPING_GL_JSON, "/etc/acs-commons/lists/rk-liability-conditions");
        Map<String, Object> config = new HashMap<>();
        config.put("pdfFolderMappingGLPath", "/etc/acs-commons/lists/rk-liability-conditions");
        aemContext.registerInjectActivateService(rkLiabilityConditionsServiceImpl, config);
        aemContext.registerService(RKLiabilityConditionsServiceImpl.class, rkLiabilityConditionsServiceImpl);
        aemContext.registerAdapter(Page.class,GenericList.class,genericList);
        List<GenericList.Item> items = new ArrayList<>();
        items.add(new GenericList.Item() {
            @Override public String getTitle() {
                return "en";
            }

            @Override public String getTitle(Locale locale) {
                return null;
            }

            @Override public String getValue() {
                return "/content/dam/tetrapak/customerhub/english/rebuildingkits/liability-conditions.pdf";
            }
        });
        items.add(new GenericList.Item() {
            @Override public String getTitle() {
                return "fr";
            }

            @Override public String getTitle(Locale locale) {
                return null;
            }

            @Override public String getValue() {
                return "/content/dam/tetrapak/customerhub/french/rebuildingkits/liability-conditions.pdf";
            }
        });
        Mockito.when(genericList.getItems()).thenReturn(items);
    }

    @Test
    public void testGetRKLiabilityConditionsEnglishPDF() throws Exception {
        RKLiabilityConditionsPDF rkLiabilityConditionsPDF = rkLiabilityConditionsServiceImpl.getPDFLinksJSON(aemContext.resourceResolver(),"en");
        assertNotNull(rkLiabilityConditionsPDF);
        assertNotNull(rkLiabilityConditionsPDF.getEnglishPDF());
    }

    @Test
    public void testGetRKLiabilityConditionsFrenchPDF() throws Exception {
        RKLiabilityConditionsPDF rkLiabilityConditionsPDF = rkLiabilityConditionsServiceImpl.getPDFLinksJSON(aemContext.resourceResolver(),"fr");
        assertNotNull(rkLiabilityConditionsPDF);
        assertNotNull(rkLiabilityConditionsPDF.getEnglishPDF());
        assertNotNull(rkLiabilityConditionsPDF.getPreferredLanguagePDF());
    }


}
