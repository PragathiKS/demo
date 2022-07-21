package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.MockAIPCategoryServiceImpl;
import com.tetrapak.customerhub.core.mock.MockAPIGEEServiceImpl;
import com.tetrapak.customerhub.core.services.AIPCategoryService;
import com.tetrapak.customerhub.core.services.APIGEEService;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Spy;

import static org.junit.Assert.*;

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

	/** The Constant COMPONENT_PATH. */
	private static final String COMPONENT_PATH = "/content/tetrapak/customerhub/global/en/automation-digital/plantmaster-trainings/_jcr_content/root/responsivegrid/plantmastertrainings";

	/** The Constant GROUP_SERVLET_URL. */
	private static final String GROUP_SERVLET_URL =
			"/content/tetrapak/customerhub/global/en/automation-digital/plantmaster-trainings/jcr:content/root/responsivegrid/plantmastertrainings.plantmaster.json";

	/** The Constant LEARNING_HISTORY_MIG_API. */
	private static final String LEARNING_HISTORY_MIG_API = "https://api-mig.tetrapak.com/application/vmware/learningplatform/transcripts";

	/** The Constant TRAINING_DETAILS_API. */
	private static final String TRAINING_DETAILS_MIG_API =
	"https://api-mig.tetrapak.com/productinformation/categories/1234/products?includechildren=true&details=true";
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
		assertEquals("Unexpected value", COMPONENT_PATH, model.getComponentPath());
		assertEquals("Unexpected value", ".email.html", model.getComponentPathExtension());
		assertTrue("Publish Env is true", model.isPublishEnvironment());
		assertEquals("Unexpected value", LEARNING_HISTORY_MIG_API, model.getLearningHistoryApi());
		assertEquals("Unexpected value", GROUP_SERVLET_URL, model.getGroupServletUrl());
		assertEquals("Unexpected value",TRAINING_DETAILS_MIG_API, model.getTrainingDetailsApi());
		assertEquals("Unexpected value","cuhu.plantmastertrainings.trainingMaterialHandouts", model.getTrainingMaterialHandouts());
		assertEquals("Unexpected value","cuhu.plantmastertrainings.subTitle", model.getSubTitle());
		MockSlingHttpServletRequest request = aemContext.request();
	}
}
