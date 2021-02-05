package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.HashMap;
import java.util.Map;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tetrapak.publicweb.core.services.DataEncryptionService;
import com.tetrapak.publicweb.core.services.ManagePrefContentFragService;
import com.tetrapak.publicweb.core.services.PardotService;
import com.tetrapak.publicweb.core.services.impl.DataEncryptionServiceImpl;
import com.tetrapak.publicweb.core.services.impl.ManagePrefContentFragServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class ManagePreferencesModelTest.
 *
 * @author ojaswarn
 */
public class ManagePreferencesModelTest {
	
	/** The context. */
	@Rule
	public AemContext context = new AemContext();
	
	/** The Constant TEST_CONTENT. */
	private static final String TEST_CONTENT = "/managePreference/test-content.json";
	
	/** The Constant TEST_COUNTRY_CF. */
	private static final String TEST_COUNTRY_CF = "/managePreference/test-country-cf.json";
	
	/** The Constant TEST_LANGUAGE_CF. */
	private static final String TEST_LANGUAGE_CF = "/managePreference/test-language-cf.json";
	
	/** The Constant TEST_CONTENT_ROOT. */
	private static final String TEST_CONTENT_ROOT = "/content/tetrapak/publicweb/lang-masters/en";
	
	/** The Constant TEST_COUNTRY_CFROOT. */
	private static final String TEST_COUNTRY_CFROOT = "/content/dam/tetrapak/publicweb/contentfragment/manage-preference-countries";
	
	/** The Constant TEST_LANGUAGE_CFROOT. */
	private static final String TEST_LANGUAGE_CFROOT = "/content/dam/tetrapak/publicweb/contentfragment/manage-preference-languages";
	
	/** The Constant RESOURCE_PATH. */
	private static final String RESOURCE_PATH = TEST_CONTENT_ROOT+"/jcr:content/root/responsivegrid/managepreferenceform";
	
	/** The resource. */
	private Resource resource;
	
	/** The model class. */
	Class<ManagePreferencesModel> modelClass = ManagePreferencesModel.class;
	
	/** The cf service. */
	private ManagePrefContentFragService cfService;
	
	/** The pardot service. */
	private PardotService pardotService;
	
	/** The encryption service. */
	private DataEncryptionService encryptionService;
	
	/** The model. */
	private ManagePreferencesModel model;

	/**
	 * Sets the up.
	 */
	@Before
	public void setUp() {
		cfService = new ManagePrefContentFragServiceImpl();
		encryptionService = new DataEncryptionServiceImpl();
		context.load().json(TEST_CONTENT, TEST_CONTENT_ROOT);
		context.load().json(TEST_COUNTRY_CF, TEST_COUNTRY_CFROOT);
		context.load().json(TEST_LANGUAGE_CF, TEST_LANGUAGE_CFROOT);
		pardotService = mock(PardotService.class);
		context.registerService(ManagePrefContentFragService.class ,cfService);
		context.registerService(PardotService.class ,pardotService);
		context.registerService(DataEncryptionService.class ,encryptionService);
		context.addModelsForClasses(modelClass);
		initializeManageCfConfig();
		initializeDataEncryptionConfig();
		when(pardotService.getManagePrefJson("testpreference4@yopmail.com")).thenReturn(sampleJson());
		resource = context.currentResource(TEST_COUNTRY_CFROOT);
		resource = context.currentResource(TEST_LANGUAGE_CFROOT);
		resource = context.currentResource(RESOURCE_PATH);
        context.request().setQueryString("id=BFS7CThuKGMpRtSNIpXrEOCaOAmWbViidmy8jJI1lrs=");
		model = context.request().adaptTo(ManagePreferencesModel.class);
	}

	/**
	 * Initialize data encryption config.
	 */
	private void initializeDataEncryptionConfig() {
		final Map<String, Object> config = new HashMap<>();
		config.put("aesEncryptionkey","publicwebEncryptionkey");
        MockOsgi.activate(context.getService(DataEncryptionService.class), context.bundleContext(), config);
	}

	/**
	 * Initialize manage cf config.
	 */
	private void initializeManageCfConfig() {
		final Map<String, Object> managePrefCfConfig = new HashMap<>();
		managePrefCfConfig.put("getCountryContentFragmentRootPath",TEST_COUNTRY_CFROOT);
		managePrefCfConfig.put("getLanguageContentFragmentRootPath",TEST_LANGUAGE_CFROOT);
        MockOsgi.activate(context.getService(ManagePrefContentFragService.class), context.bundleContext(), managePrefCfConfig);
	}

	/**
	 * Test model not null.
	 */
	@Test
	public void testModelNotNull() {
		assertNotNull("Model Not Null: ",model);
	}
	
	/**
	 * Sample json.
	 *
	 * @return the json object
	 */
	private JsonObject sampleJson() {
		String jsonStr = "{\r\n" + 
				"   \"email\":\"testpreference4@yopmail.com\",\r\n" + 
				"   \"country\":\"india\",\r\n" + 
				"   \"language\":\"english\",\r\n" + 
				"   \"areaofinterest\":{\r\n" + 
				"      \"Services\":\"Y\",\r\n" + 
				"      \"Packaging\":\"N\",\r\n" + 
				"      \"Processing\":\"N\",\r\n" + 
				"      \"Innovation\":\"N\",\r\n" + 
				"      \"Sustainability\":\"N\",\r\n" + 
				"      \"End-to-End solutions\":\"N\"\r\n" + 
				"   },\r\n" + 
				"   \"communicationtype\":{\r\n" + 
				"      \"Marketing Communication\":\"N\",\r\n" + 
				"      \"Press and Media Communication\":\"Y\",\r\n" + 
				"      \"Event Invitations\":\"Y\"\r\n" + 
				"   }\r\n" + 
				"}";
		return new JsonParser().parse(jsonStr).getAsJsonObject();
	}
}
