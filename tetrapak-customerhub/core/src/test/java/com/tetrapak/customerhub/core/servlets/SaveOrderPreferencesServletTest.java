package com.tetrapak.customerhub.core.servlets;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.GenericServiceType;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.http.HttpStatus;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class SaveOrderPreferencesServletTest {

	private static final String SERVLET_RESOURCE_PATH = "/content/tetrapak/customerhub/global/ordering/jcr:content/root/responsivegrid/orderingcard";
	private static final String SERVLET_RESOURCE_JSON = "allContent.json";

	@Rule
	public final AemContext aemContext = CuhuCoreAemContext.getAemContextWithJcrMock(SERVLET_RESOURCE_JSON,
			SERVLET_RESOURCE_PATH, getMultipleMockedService());

	@Before
	public void setup() {
		aemContext.load().json("/" + "user.json", "/home");
		aemContext.currentResource(SERVLET_RESOURCE_PATH);
		aemContext.request().setServletPath(SERVLET_RESOURCE_PATH);
		aemContext.request().setMethod(HttpConstants.METHOD_POST);
		Map<String, Object> params = new HashMap<>();
		params.put("fields", "orderNumber.poNumber");
		aemContext.request().setParameterMap(params);
	}

	@Test
	public void doPost() throws IOException {
		MockSlingHttpServletRequest request = aemContext.request();
		MockSlingHttpServletResponse response = aemContext.response();
		SaveOrderPreferencesServlet saveOrderPreferencesServlet = aemContext
				.getService(SaveOrderPreferencesServlet.class);
		aemContext.registerInjectActivateService(saveOrderPreferencesServlet);
		saveOrderPreferencesServlet.doPost(request, response);
		assertEquals(HttpStatus.SC_OK, response.getStatus());
	}

	public <T> List<GenericServiceType<T>> getMultipleMockedService() {

		// GenericServiceType<APIGEEService> apigeeServiceGenericServiceType = new
		// GenericServiceType<>();
		// apigeeServiceGenericServiceType.setClazzType(APIGEEService.class);
		// apigeeServiceGenericServiceType.set(new MockAPIGEEServiceImpl());

		GenericServiceType<SaveOrderPreferencesServlet> saveOrderPreferencesServletGenericServiceType = new GenericServiceType<>();
		saveOrderPreferencesServletGenericServiceType.setClazzType(SaveOrderPreferencesServlet.class);
		saveOrderPreferencesServletGenericServiceType.set(new SaveOrderPreferencesServlet());

		List<GenericServiceType<T>> serviceTypes = new ArrayList<>();
		// serviceTypes.add((GenericServiceType<T>)apigeeServiceGenericServiceType);
		serviceTypes.add((GenericServiceType<T>) saveOrderPreferencesServletGenericServiceType);
		return serviceTypes;
	}
}
