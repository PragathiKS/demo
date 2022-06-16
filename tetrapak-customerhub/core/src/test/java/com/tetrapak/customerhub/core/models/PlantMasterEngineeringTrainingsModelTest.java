package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * The Class PlantMasterTrainingsModelTest.
 */
public class PlantMasterEngineeringTrainingsModelTest {
	
	/** The Constant TEST_CONTENT. */
	private static final String TEST_CONTENT = "plantmastertrainings.json";
	
	/** The Constant TEST_CONTENT_ROOT. */
	private static final String TEST_CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/automation-digital/plantmaster-trainings";
	
	/** The Constant COMPONENT_PATH. */
	private static final String COMPONENT_PATH = "/content/tetrapak/customerhub/global/en/automation-digital/plantmaster-trainings/jcr:content/root/responsivegrid/plantmastertrainings";

	/** The aem context. */
	@Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(TEST_CONTENT, TEST_CONTENT_ROOT);

	private PlantMasterEngineeringTrainingsModel plantMasterEngineeringTrainingsModel;

	@Before
	public void setup() {
		Resource resource = aemContext.currentResource(COMPONENT_PATH);
		plantMasterEngineeringTrainingsModel = resource.adaptTo(PlantMasterEngineeringTrainingsModel.class);
	}

	@Test
	public void testModelGetters() {
		assertEquals("Unexpected value","cuhu.plantmastertrainings.title" , plantMasterEngineeringTrainingsModel.getTitle());
		assertEquals("Unexpected value","cuhu.plantmastertrainings.availableTrainings" , plantMasterEngineeringTrainingsModel.getAvailableTrainings());
		assertEquals("Unexpected value","cuhu.plantmastertrainings.courseDescription" , plantMasterEngineeringTrainingsModel.getCourseDescription());
		assertEquals("Unexpected value","cuhu.plantmastertrainings.principleObjectives" , plantMasterEngineeringTrainingsModel.getPrincipleObjectives());
		assertEquals("Unexpected value","cuhu.plantmastertrainings.targetGroups" , plantMasterEngineeringTrainingsModel.getTargetGroups());
		assertEquals("Unexpected value","cuhu.plantmastertrainings.duration" , plantMasterEngineeringTrainingsModel.getDuration());
		assertEquals("Unexpected value","cuhu.plantmastertrainings.hours" , plantMasterEngineeringTrainingsModel.getHours());
		assertEquals("Unexpected value","cuhu.plantmastertrainings.maxParticipants" , plantMasterEngineeringTrainingsModel.getMaxParticipants());
		assertEquals("Unexpected value","cuhu.plantmastertrainings.knowledgeRequirements" , plantMasterEngineeringTrainingsModel.getKnowledgeRequirements());
		assertEquals("Unexpected value","cuhu.plantmastertrainings.noOfParticipants" , plantMasterEngineeringTrainingsModel.getNoOfParticipantsLabel());
		assertEquals("Unexpected value","cuhu.plantmastertrainings.preferredLocation" , plantMasterEngineeringTrainingsModel.getPreferredLocationLabel());
		assertEquals("Unexpected value","cuhu.plantmastertrainings.preferredDate" , plantMasterEngineeringTrainingsModel.getPreferredDateLabel());
		assertEquals("Unexpected value","cuhu.plantmastertrainings.preferredDatePlaceholder" , plantMasterEngineeringTrainingsModel.getPreferredDatePlaceholder());
		assertEquals("Unexpected value","cuhu.plantmastertrainings.comments" , plantMasterEngineeringTrainingsModel.getCommentsLabel());
		assertEquals("Unexpected value","cuhu.plantmastertrainings.confirmationText" , plantMasterEngineeringTrainingsModel.getConfirmationText());
		assertEquals("Unexpected value","cuhu.plantmastertrainings.submitButtonLabel" , plantMasterEngineeringTrainingsModel.getSubmitButtonLabel());
		assertEquals("Unexpected value","cuhu.plantmastertrainings.inputErrorMsg" , plantMasterEngineeringTrainingsModel.getInputErrorMsg());
		assertEquals("Unexpected value","cuhu.plantmastertrainings.confirmationErrorMsg" , plantMasterEngineeringTrainingsModel.getConfirmationErrorMsg());
		assertEquals("Unexpected value","cuhu.plantmasterlicense.engineering.inputFormatErrorMsg" , plantMasterEngineeringTrainingsModel.getInputFormatErrorMsg());
		assertEquals("Unexpected value","cuhu.plantmasterlicense.engineering.numberFieldErrorMsg" , plantMasterEngineeringTrainingsModel.getNumberFieldErrorMsg());
		assertEquals("Unexpected value","cuhu.plantmastertrainings.successMessage" , plantMasterEngineeringTrainingsModel.getSuccessMessage());
		assertEquals("Unexpected value","cuhu.plantmastertrainings.subject" , plantMasterEngineeringTrainingsModel.getSubject());
		assertEquals("Unexpected value","cuhu.plantmastertrainings.salutation" , plantMasterEngineeringTrainingsModel.getSalutation());
		assertEquals("Unexpected value","cuhu.plantmastertrainings.body" , plantMasterEngineeringTrainingsModel.getBody());
		assertEquals("Unexpected value","cuhu.plantmastertrainings.consent" , plantMasterEngineeringTrainingsModel.getConsentLabel());
		assertEquals("Unexpected value","cuhu.plantmastertrainings.contactDetails" , plantMasterEngineeringTrainingsModel.getContactDetails());
		assertEquals("Unexpected value","cuhu.plantmastertrainings.trainingIdLabel" , plantMasterEngineeringTrainingsModel.getTrainingIdLabel());
		assertEquals("Unexpected value","cuhu.plantmastertrainings.trainingNameLabel" , plantMasterEngineeringTrainingsModel.getTrainingNameLabel());
		assertEquals("Unexpected value","cuhu.plantmastertrainings.formTitle" , plantMasterEngineeringTrainingsModel.getFormTitle());
	}
}
