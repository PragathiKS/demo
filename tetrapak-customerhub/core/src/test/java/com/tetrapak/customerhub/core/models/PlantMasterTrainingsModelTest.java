package com.tetrapak.customerhub.core.models;

import static org.junit.Assert.*;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.MockAIPCategoryServiceImpl;
import com.tetrapak.customerhub.core.mock.MockAPIGEEServiceImpl;
import com.tetrapak.customerhub.core.services.AIPCategoryService;
import com.tetrapak.customerhub.core.services.APIGEEService;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class PlantMasterTrainingsModelTest.
 */
public class PlantMasterTrainingsModelTest {
	
	/** The Constant TEST_CONTENT. */
	private static final String TEST_CONTENT = "plantmastertrainings.json";
	
	/** The Constant TEST_CONTENT_ROOT. */
	private static final String TEST_CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/automation-digital/plantmaster-trainings";
	
	/** The Constant RESOURCE_PATH. */
	private static final String RESOURCE_PATH = TEST_CONTENT_ROOT+"/jcr:content/root/responsivegrid/plantmastertrainings";
	
	/** The aem context. */
	@Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(TEST_CONTENT, TEST_CONTENT_ROOT);

	/** The model. */
	private PlantMasterTrainingsModel model;
	
	/** The apigeeService. */
	private APIGEEService apigeeService;
	
	/** The aip category service. */
	private AIPCategoryService aipCategoryService;
	
	/**
	 * Sets the up.
	 */
	@Before
	public void setUp () {
	    
	    apigeeService = new MockAPIGEEServiceImpl();
        aemContext.registerService(APIGEEService.class, apigeeService);
        aipCategoryService = new MockAIPCategoryServiceImpl();
        aemContext.registerService(AIPCategoryService.class, aipCategoryService);
        
		Resource resource = aemContext.currentResource(RESOURCE_PATH);
		aemContext.request().setResource(resource);
		model = aemContext.request().adaptTo(PlantMasterTrainingsModel.class);
	}

	/**
	 * Test model not null.
	 */
	@Test
	public void testModelNotNull() {
		assertNotNull("Model Not null", model);
	}
}
