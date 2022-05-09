package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.MockAIPCategoryServiceImpl;
import com.tetrapak.customerhub.core.mock.MockAPIGEEServiceImpl;
import com.tetrapak.customerhub.core.services.AIPCategoryService;
import com.tetrapak.customerhub.core.services.APIGEEService;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

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
		assertEquals("Unexpected value", "cuhu.plantmastertrainings.availableTrainings", model.getAvailableTrainings());
		assertEquals("Unexpected value", "cuhu.plantmastertrainings.title", model.getTitle());
		assertEquals("Unexpected value", "cuhu.plantmastertrainings.availableTrainings", model.getAvailableTrainings());
		assertEquals("Unexpected value", "cuhu.plantmastertrainings.learningHistory", model.getLearningHistory());
		assertEquals("Unexpected value", "cuhu.plantmastertrainings.trainingMaterialHandouts", model.getTrainingMaterialHandouts());
		assertEquals("Unexpected value", "cuhu.plantmastertrainings.courseDescription", model.getCourseDescription());
		assertEquals("Unexpected value", "cuhu.plantmastertrainings.principleObjectives", model.getPrincipleObjectives());
		assertEquals("Unexpected value", "cuhu.plantmastertrainings.targetGroups", model.getTargetGroups());
		assertEquals("Unexpected value", "cuhu.plantmastertrainings.duration", model.getDuration());
		assertEquals("Unexpected value", "cuhu.plantmastertrainings.hours", model.getHours());
		assertEquals("Unexpected value", "cuhu.plantmastertrainings.maxParticipants", model.getMaxParticipants());
		assertEquals("Unexpected value", "cuhu.plantmastertrainings.knowledgeRequirements", model.getKnowledgeRequirements());
		assertEquals("Unexpected value", "cuhu.plantmastertrainings.noOfParticipants", model.getNoOfParticipantsLabel());
		assertEquals("Unexpected value", "cuhu.plantmastertrainings.preferredDate", model.getPreferredDateLabel());
		assertEquals("Unexpected value", "cuhu.plantmastertrainings.submitButtonLabel", model.getSubmitButtonLabel());
		assertEquals("Unexpected value", "cuhu.plantmastertrainings.subject", model.getSubject());
		assertEquals("Unexpected value", "cuhu.plantmastertrainings.salutation", model.getSalutation());
		assertEquals("Unexpected value", "cuhu.plantmastertrainings.body", model.getBody());
		assertEquals("Unexpected value", "cuhu.plantmastertrainings.consent", model.getConsentLabel());
		assertEquals("Unexpected value", "cuhu.plantmastertrainings.comments", model.getCommentsLabel());
		assertEquals("Unexpected value", COMPONENT_PATH, model.getComponentPath());
		assertEquals("Unexpected value", ".email.html", model.getComponentPathExtension());
		assertEquals("Unexpected value", "cuhu.plantmastertrainings.confirmationText", model.getConfirmationText());
		assertTrue("Publish Env is true", model.isPublishEnvironment());
	}
}
