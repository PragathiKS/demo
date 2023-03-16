package com.tetrapak.customerhub.core.services.impl;

import com.adobe.acs.commons.email.EmailService;
import com.day.cq.wcm.api.LanguageManager;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.models.RebuildingKitDetailsModel;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.config.RebuildingKitsEmailConfiguration;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.event.jobs.JobManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RebuildingKitsEmailServiceImplTest {

	/** The job manager. */
	@Mock
	private JobManager jobManager;

	@Mock
	private EmailService emailService;

	@Mock
	private RebuildingKitsEmailConfiguration configuration;

	@Mock
	private LanguageManager languageManager;

	/** The Constant TEST_CONTENT. */
	private static final String TEST_CONTENT = "rebuildingkitdetails.json";

	/** The Constant TEST_CONTENT_ROOT. */
	private static final String TEST_CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/installedequipment/rebuildingkits";

	/** The Constant RESOURCE_PATH. */
	private static final String RESOURCE_PATH = TEST_CONTENT_ROOT+"/jcr:content/root/responsivegrid/rebuildingkitdetails";

	/** The aem context. */
	@Rule
	public final AemContext aemContext = CuhuCoreAemContext.getAemContext(TEST_CONTENT, TEST_CONTENT_ROOT);

	private final String ERROR_MESSAGE = "Unexpected value. Please check.";
	private static final String recipientEmail = "testUser@company.com";

	@Mock
	private APIGEEService apigeeService;

	/** The model. */
	private RebuildingKitDetailsModel model;

	@Spy
	@InjectMocks
	private RebuildingKitsEmailServiceImpl rebuildingKitsEmailService = new RebuildingKitsEmailServiceImpl() ;

	@Before public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		aemContext.registerService(JobManager.class,jobManager);
		aemContext.registerService(EmailService.class,emailService);
		aemContext.registerService(LanguageManager.class,languageManager);
		aemContext.registerService(APIGEEService.class, apigeeService);
		when(configuration.isRebuildingKitsEmailServiceEnabled()).thenReturn(true);
		when(apigeeService.getApiMappings()).thenReturn(new String[]{"rebuildingkits-rebuildingkitdetails:installedbase/rebuildingkits"});
		aemContext.registerService( RebuildingKitsEmailServiceImpl.class,rebuildingKitsEmailService);
		Resource resource = aemContext.currentResource(RESOURCE_PATH);
		aemContext.request().setResource(resource);
		model = aemContext.request().adaptTo(RebuildingKitDetailsModel.class);
	}

	@Test
	public void testActivateConfiguration() {
		Map<String, Object> _config = new HashMap<>();
		_config.put("rebuildingKitsRecipientAddresses", "testing@test.com");
		_config.put("isRebuildingKitsEmailServiceEnabled", "true");
		aemContext.registerService(RebuildingKitsEmailConfiguration.class, configuration);
		aemContext.registerInjectActivateService(rebuildingKitsEmailService, _config);
		aemContext.request().setResource(aemContext.resourceResolver().getResource(RESOURCE_PATH));
		String requestBody = "{\"rkTbNumber\": \"RK TB Number\", \"mcon\": \"MCON\", \"functionalLocation\": \"Pune\", \"requestedCTILanguage\" : \"Requested Language\"}";
		aemContext.request().setContent(requestBody.getBytes(StandardCharsets.UTF_8));
		ResourceBundle resourceBundle = aemContext.request().getResourceBundle(aemContext.request().getLocale());
		when(rebuildingKitsEmailService.getI18nValue(any(),any(),any())).thenReturn("");
		String requestData = aemContext.request().getReader().lines().collect(Collectors.joining());
		assertEquals(ERROR_MESSAGE, true, rebuildingKitsEmailService.sendEmail(resourceBundle,
				requestData, model));
	}


	@Test
	public void testSendEmail() {
		when(configuration.rebuildingKitsRecipientAddresses()).thenReturn(new String[]{recipientEmail});
		String requestBody = "{\"rkTbNumber\": \"RK TB Number\", \"mcon\": \"MCON\", \"functionalLocation\": \"Pune\", \"requestedCTILanguage\" : \"Requested Language\"}";
		aemContext.request().setContent(requestBody.getBytes(StandardCharsets.UTF_8));
		ResourceBundle resourceBundle = aemContext.request().getResourceBundle(aemContext.request().getLocale());
		when(rebuildingKitsEmailService.getI18nValue(any(),any(),any())).thenReturn("");
		String requestData = aemContext.request().getReader().lines().collect(Collectors.joining());
		assertEquals(ERROR_MESSAGE, true, rebuildingKitsEmailService.sendEmail(resourceBundle,
				 requestData, model));
	}

	@Test
	public void testSendEmailWithEmptyRecipientList() {
		when(configuration.rebuildingKitsRecipientAddresses()).thenReturn(null);
		String requestBody = "{\"rkTbNumber\": \"RK TB Number\", \"mcon\": \"MCON\", \"functionalLocation\": \"Pune\", \"requestedCTILanguage\" : \"Requested Language\"}";
		aemContext.request().setContent(requestBody.getBytes(StandardCharsets.UTF_8));
		ResourceBundle resourceBundle = aemContext.request().getResourceBundle(aemContext.request().getLocale());
		when(rebuildingKitsEmailService.getI18nValue(any(),any(),any())).thenReturn("");
		String requestData = aemContext.request().getReader().lines().collect(Collectors.joining());
		assertEquals(ERROR_MESSAGE, false, rebuildingKitsEmailService.sendEmail(resourceBundle,
				requestData, model));
	}
}
