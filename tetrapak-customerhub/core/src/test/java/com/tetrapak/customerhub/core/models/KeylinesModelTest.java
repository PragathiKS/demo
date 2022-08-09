package com.tetrapak.customerhub.core.models;

import static org.junit.Assert.assertEquals;

import java.security.AccessControlException;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.day.cq.tagging.InvalidTagFormatException;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;

import io.wcm.testing.mock.aem.junit.AemContext;

public class KeylinesModelTest {
    /** The Constant TEST_CONTENT. */
    private static final String TEST_CONTENT = "keylines/keylines.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/customerhub/content-components/en/keyline";

    /** The Constant RESOURCE_PATH. */
    private static final String RESOURCE_PATH = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/keylines";

    private static final String TEST_API_URL = ".assets.json";

    private static final String I18N_KEYS = "{\"modalTitle\":\"cuhu.packDesign.keylines.modalTitle\",\"modalDescription\":\"cuhu.packDesign.keylines.modalDescription\",\"selectVolumes\":\"cuhu.packDesign.keylines.selectVolumes\",\"selectOpenings\":\"cuhu.packDesign.keylines.selectOpenings\",\"downloadKeyline\":\"cuhu.packDesign.keylines.downloadKeyline\",\"downloadKeylineError\":\"cuhu.packDesign.keylines.errorMessage\"}";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(TEST_CONTENT, TEST_CONTENT_ROOT);

    /** The model. */
    private KeylinesModel model;

    /**
     * Set up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
	Resource resource = aemContext.currentResource(RESOURCE_PATH);
	aemContext.request().setResource(resource);
	aemContext.load().json("/keylines/keyline-tags.json", "/content/cq:tags/tetrapak");
	model = aemContext.request().adaptTo(KeylinesModel.class);
	MockitoAnnotations.initMocks(this);
    }

    /**
     * Test method for Keylines model.
     * 
     * @throws InvalidTagFormatException
     * @throws AccessControlException
     */
    @Test
    public void testModel() throws AccessControlException, InvalidTagFormatException {
	assertEquals("Keylines", model.getTitle());
	assertEquals("keylines", model.getAnchorId());
	assertEquals("Keylines", model.getAnchorTitle());
	assertEquals("grayscale-white", model.getPwTheme());
	assertEquals("cuhu.packDesign.keylines.download", model.getDowloadText());
	assertEquals(RESOURCE_PATH + TEST_API_URL, model.getApiUrl());
	assertEquals("tetrapak:keylines/tetra-rex", model.getPackageType());
	assertEquals(I18N_KEYS, model.getI18nKeys());
	ShapeModel shape = model.getShapes().get(0);
	assertEquals("tetrapak:keylines/tetra-rex/mid", shape.getShape());
	assertEquals(
		"/content/dam/tetrapak/media-box/global/en/packaging/package-type/tetra-classic-aseptic/images/test-Adobe-Image.jpg",
		shape.getFileReference());
	assertEquals("Mid", shape.getAlt());
	assertEquals("mid", shape.getName());
	assertEquals("Mid", shape.getTitle());
    }
}
