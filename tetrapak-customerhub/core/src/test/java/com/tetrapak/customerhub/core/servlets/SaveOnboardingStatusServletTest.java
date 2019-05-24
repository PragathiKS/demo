package com.tetrapak.customerhub.core.servlets;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.GenericServiceType;

import io.wcm.testing.mock.aem.junit.AemContext;
/**
 * Test class for SaveOnboardingStatusServlet
 * @author tustusha
 */
@Ignore
@RunWith(MockitoJUnitRunner.class)
public class SaveOnboardingStatusServletTest {

	private static final String SERVLET_RESOURCE_PATH = "/content/tetrapak/customerhub/"
			+ "global/dashboard/jcr:content/root/responsivegrid/introscreen";
	private static final String SERVLET_RESOURCE_JSON = "allContent.json";

	@Rule
	public final AemContext aemContext = CuhuCoreAemContext.getAemContextWithJcrMock(SERVLET_RESOURCE_JSON,
			SERVLET_RESOURCE_PATH, getMultipleMockedService());

	@Before
	private void setup() {
		aemContext.load().json("/" + "user.json", "/home");
		aemContext.currentResource(SERVLET_RESOURCE_PATH);
		aemContext.request().setServletPath(SERVLET_RESOURCE_PATH);
		aemContext.request().setMethod(HttpConstants.METHOD_GET);
	}

	@Test
	private void doPost() throws IOException {
		MockSlingHttpServletRequest request = aemContext.request();
		MockSlingHttpServletResponse response = aemContext.response();
		SaveOrderPreferencesServlet saveOrderPreferencesServlet = aemContext
				.getService(SaveOrderPreferencesServlet.class);
		aemContext.registerInjectActivateService(saveOrderPreferencesServlet);
		saveOrderPreferencesServlet.doPost(request, response);
		assertEquals(HttpStatus.SC_OK, response.getStatus());
	}

	private <T> List<GenericServiceType<T>> getMultipleMockedService() {
		GenericServiceType<SaveOrderPreferencesServlet> saveOnboardingStatusServletGenericServiceType = new GenericServiceType<>();
		saveOnboardingStatusServletGenericServiceType.setClazzType(SaveOrderPreferencesServlet.class);
		saveOnboardingStatusServletGenericServiceType.set(new SaveOrderPreferencesServlet());
		List<GenericServiceType<T>> serviceTypes = new ArrayList<>();
		serviceTypes.add((GenericServiceType<T>) saveOnboardingStatusServletGenericServiceType);
		return serviceTypes;
	}
}
