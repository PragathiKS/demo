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

import static org.junit.Assert.*;

/**
 * The Class PlantMasterTrainingsModelTest.
 */
public class PlantMasterLearningHistoryModelTest {
	
	/** The Constant TEST_CONTENT. */
	private static final String TEST_CONTENT = "plantmastertrainings.json";
	
	/** The Constant TEST_CONTENT_ROOT. */
	private static final String TEST_CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/automation-digital/plantmaster-trainings";
	
	/** The Constant COMPONENT_PATH. */
	private static final String COMPONENT_PATH = "/content/tetrapak/customerhub/global/en/automation-digital/plantmaster-trainings/jcr:content/root/responsivegrid/plantmastertrainings/learningHistory";

	/** The aem context. */
	@Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(TEST_CONTENT, TEST_CONTENT_ROOT);

	private PlantMasterLearningHistoryModel plantMasterLearningHistoryModel;

	@Before
	public void setup() {
		Resource resource = aemContext.currentResource(COMPONENT_PATH);
		plantMasterLearningHistoryModel = resource.adaptTo(PlantMasterLearningHistoryModel.class);
	}

	@Test
	public void testModelGetters() {
		assertEquals("Unexpected value","cuhu.plantmastertraining.lh.learningHistoryText" , plantMasterLearningHistoryModel.getLearninghistorytext());
		assertEquals("Unexpected value","cuhu.plantmastertraining.lh.authenticatedText" , plantMasterLearningHistoryModel.getAuthenticatedtext());
		assertEquals("Unexpected value","cuhu.plantmastertraining.lh.accreditedText" , plantMasterLearningHistoryModel.getAccreditedtext());
		assertEquals("Unexpected value","cuhu.plantmastertraining.lh.diplomaText" , plantMasterLearningHistoryModel.getDiplomatext());
		assertEquals("Unexpected value","cuhu.plantmastertraining.lh.userText" , plantMasterLearningHistoryModel.getUsertext());
		assertEquals("Unexpected value","cuhu.plantmastertraining.lh.itemText" , plantMasterLearningHistoryModel.getItemtext());
		assertEquals("Unexpected value","cuhu.plantmastertraining.lh.completionDateText" , plantMasterLearningHistoryModel.getCompletiondatetext());
	}
}
