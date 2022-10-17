package com.tetrapak.customerhub.core.servlets;

import com.day.cq.wcm.api.Page;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.MockRebuildingKitsApiServiceImpl;
import com.tetrapak.customerhub.core.services.impl.RebuildingKitsExcelServiceImpl;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.http.HttpStatus;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.i18n.ResourceBundleProvider;
import org.apache.sling.testing.mock.sling.servlet.MockRequestPathInfo;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class RebuildingKitsListExcelDownloadServletTest {

	@Mock
	private Page mockPage;

	@Mock
	RebuildingKitsExcelServiceImpl rebuildingKitsExcelService = new RebuildingKitsExcelServiceImpl();

	@Mock
	MockRebuildingKitsApiServiceImpl rebuildingKitsApiService = new MockRebuildingKitsApiServiceImpl();

	@Mock
	RebuildingKitsListExcelDownloadServlet rebuildingKitsListExcelDownloadServlet;

	@Mock
	private ResourceBundleProvider mockResourceBundleProvider;

	private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/installed-equipments/rebuilding-kits";
	private static final String COMPONENT_PATH = "/content/tetrapak/customerhub/global/en/installed-equipments/rebuilding-kits/jcr:content/root/responsivegrid/rebuildingkitdetails";
	private static final String RESOURCE_JSON = "rebuildingkitdetails.json";

	@Rule
	public final AemContext aemContext = CuhuCoreAemContext.getAemContextWithJcrMock(RESOURCE_JSON, CONTENT_ROOT);

	@Before
	public void setup() throws IOException {
		aemContext.currentResource(COMPONENT_PATH);
		aemContext.request().setServletPath(COMPONENT_PATH);
		aemContext.request().setMethod(HttpConstants.METHOD_GET);
		Cookie cookie = new Cookie("authToken", "cLBKhQAPhQCZ2bzGW5j2yXYBb6de");
		aemContext.request().addCookie(cookie);
	}

	@Test
	public void doGetForExcel() throws IOException {
		MockRequestPathInfo requestPathInfo = (MockRequestPathInfo) aemContext.request().getRequestPathInfo();
		requestPathInfo.setExtension("excel");
		MockSlingHttpServletRequest request = aemContext.request();
		MockSlingHttpServletResponse response = aemContext.response();
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(CustomerHubConstants.TOKEN, CustomerHubConstants.TEST_TOKEN);
		parameters.put("countrycode", "DE");
		request.setParameterMap(parameters);
		rebuildingKitsListExcelDownloadServlet.doGet(request, response);
		assertEquals("status should be ok", HttpStatus.SC_OK, response.getStatus());
	}
}
