package com.tetrapak.customerhub.core.services.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.day.cq.search.QueryBuilder;
import com.google.common.base.Function;
import com.tetrapak.customerhub.core.beans.keylines.Keylines;
import com.tetrapak.customerhub.core.exceptions.KeylinesException;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.MockHelper;

import io.wcm.testing.mock.aem.junit.AemContext;

public class KeylinesServiceImplTest {

    /** Sample Asset 1 - JSON file */
    private static final String ASSET_1 = "/content/dam/tetrapak/media-box/global/en/keylines/tetra-rex/mid/1750/twistcapbarrier-30/documents/Keyline 8068 TR 1750 Mid H30 6th.pdf";

    /** Sample Asset 2 - JSON file */
    private static final String ASSET_2 = "/content/dam/tetrapak/media-box/global/en/keylines/tetra-rex/mid/1500/twistcap-oso34/documents/Keyline 8065 TR 1500 Mid H33,5 6th.pdf";

    /** Sample Asset 3 - JSON file */
    private static final String ASSET_3 = "/content/dam/tetrapak/media-box/global/en/keylines/tetra-rex/base/500/easyopening/documents/Keyline 8026 TR 500 Base EO 6th.pdf";

    /** Sample Asset 4 - JSON file */
    private static final String ASSET_4 = "/content/dam/tetrapak/media-box/global/en/keylines/tetra-rex/base/500/twistcap-oso30/documents/Keyline 8124 TR 500 Base Hole 29 6th panel.pdf";

    /** The JSON file */
    private static final String RESOURCE_JSON = "keylines/keylines.json";

    /** The resource path */
    private static final String RESOURCE_PATH = "/content/tetrapak/customerhub/content-components/en/keyline/jcr:content/root/responsivegrid/keylines";

    @Spy
    @InjectMocks
    private KeylinesServiceImpl keylinesServiceImpl = new KeylinesServiceImpl();

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON, RESOURCE_PATH);

    @Before
    public void setUp() throws Exception {
	MockitoAnnotations.initMocks(this);
	Resource resource = aemContext.currentResource(RESOURCE_PATH);
	aemContext.request().setResource(resource);
	aemContext.load().json("/keylines/keyline-tags.json", "/content/cq:tags/tetrapak");
	aemContext.load().json("/keylines/keylines-dam.json", "/content/dam/tetrapak/media-box/global/en/keylines");
	aemContext.load().json("/keylines/asset-1.json", ASSET_1);
	aemContext.load().json("/keylines/asset-2.json", ASSET_2);
	aemContext.load().json("/keylines/asset-3.json", ASSET_3);
	aemContext.load().json("/keylines/asset-4.json", ASSET_4);
	List<String> pathList = new ArrayList<>();
	pathList.add(ASSET_1);
	pathList.add(ASSET_2);
	pathList.add(ASSET_3);
	pathList.add(ASSET_4);
	MockHelper.loadQuery(aemContext, pathList);
	aemContext.registerAdapter(ResourceResolver.class, QueryBuilder.class,
		new Function<ResourceResolver, QueryBuilder>() {

		    @Override
		    public QueryBuilder apply(final ResourceResolver arg0) {
			return aemContext.getService(QueryBuilder.class);
		    }
		});
	Map<String, Object> config = new HashMap<>();
	config.put("path", "/content/dam/tetrapak/media-box/global/en/keylines");
	aemContext.registerInjectActivateService(keylinesServiceImpl, config);
	aemContext.registerService(KeylinesServiceImpl.class, keylinesServiceImpl);
    }

    @Test
    public void testGetKeylines() throws KeylinesException {
	ArrayList<String> shapes = new ArrayList<String>();
	shapes.add("tetrapak:keylines/tetra-rex/mid");
	shapes.add("tetrapak:keylines/tetra-rex/base");
	Keylines keylines = keylinesServiceImpl.getKeylines("tetrapak:keylines/tetra-rex", shapes, new Locale("en"));
	assertTrue(keylines.getAssets().size() > 0);
	assertNotNull(keylines.getAssets().get(0));
	assertNotNull(keylines.getAssets().get(0).getAssetname());
	assertNotNull(keylines.getAssets().get(0).getAssetpath());
	assertNotNull(keylines.getShapes().get(0));
	assertNotNull(keylines.getShapes().get(0).getName());
	assertNotNull(keylines.getShapes().get(0).getVolumes());

    }

    @Test(expected = KeylinesException.class)
    public void testKeylinesException() throws KeylinesException {
	ArrayList<String> shapes = new ArrayList<String>();
	keylinesServiceImpl.getKeylines("tetrapak:keylines/tetra-rex", shapes, new Locale("en"));

    }
}
