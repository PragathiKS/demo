package com.tetrapak.customerhub.core.models;

import static org.junit.Assert.assertEquals;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;

import io.wcm.testing.mock.aem.junit.AemContext;

public class RefrencesModelTest {

	private ReferenceModel referenceModel;

	private static final String REFERENCE = "/content/tetrapak/customerhub/global/en/dashboard/jcr:content/root/responsivegrid/reference";
	private static final String REFERENCE_RESOURCE_JSON = "referencecomponent.json";

	@Rule
	public final AemContext aemContext = CuhuCoreAemContext.getAemContext(REFERENCE_RESOURCE_JSON, REFERENCE);

	@Before
	public void setUp() throws Exception {
		aemContext.currentResource(REFERENCE);
		aemContext.request().setAttribute("contentPath", "/content/tetrapak/customerhub/content-components/en/dashboard/jcr:content/root/responsivegrid/recommendedforyoucard");
		referenceModel = aemContext.request().adaptTo(ReferenceModel.class);
	}

	@Test
	public void testGetContentPath() {
		assertEquals("content path",
				"/content/tetrapak/customerhub/content-components/en/dashboard/jcr:content/root/responsivegrid/recommendedforyoucard",
				referenceModel.getContentPath());
	}

}
