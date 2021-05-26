package com.tetrapak.customerhub.core.models;

import static org.junit.Assert.*;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * @author ojaswarn
 * The Class MyEquipmentModelTest.
 */
public class MyEquipmentModelTest {
	
	/** The Constant TEST_CONTENT. */
	private static final String TEST_CONTENT = "myequipment.json";
	
	/** The Constant TEST_CONTENT_ROOT. */
	private static final String TEST_CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/myequipment";
	
	/** The Constant RESOURCE_PATH. */
	private static final String RESOURCE_PATH = TEST_CONTENT_ROOT+"/jcr:content/root/responsivegrid/myequipment";
	
	/** The aem context. */
	@Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(TEST_CONTENT, TEST_CONTENT_ROOT);

	/** The model. */
	private MyEquipmentModel model;
	
	/**
	 * Sets the up.
	 */
	@Before
	public void setUp () {
		Resource resource = aemContext.currentResource(RESOURCE_PATH);
		model = resource.adaptTo(MyEquipmentModel.class);
	}

	/**
	 * Test model not null.
	 */
	@Test
	public void testModelNotNull() {
		assertNotNull("Model Not null",model);
	}
}
