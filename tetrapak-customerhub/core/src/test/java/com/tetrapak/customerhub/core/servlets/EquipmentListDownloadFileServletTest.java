package com.tetrapak.customerhub.core.servlets;

import com.day.cq.wcm.api.Page;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.MockEquipmentListApiServiceImpl;
import com.tetrapak.customerhub.core.services.impl.EquipmentListExcelServiceImpl;
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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EquipmentListDownloadFileServletTest {

	@Mock
	private Page mockPage;

	@Mock
	EquipmentListExcelServiceImpl equipmentListExcelService = new EquipmentListExcelServiceImpl();

	@Mock
	MockEquipmentListApiServiceImpl equipmentListApiService = new MockEquipmentListApiServiceImpl();

	@Mock
	EquipmentListDownloadFileServlet equipmentListDownloadFileServlet;

	@Mock
	private ResourceBundleProvider mockResourceBundleProvider;

	private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/equipments";
	private static final String COMPONENT_PATH = "/content/tetrapak/customerhub/global/en/equipments/jcr:content/root/responsivegrid/myequipment";
	private static final String RESOURCE_JSON = "myequipment.json";
	private static final String PARAM_STRING = "{\\n  \\\"data\\\": [\\n    {\\n      \\\"id\\\": \\\"8901000009\\\",\\n      \\\"countryCode\\\": \\\"DE\\\",\\n      \\\"countryName\\\": \\\"Germany\\\",\\n      \\\"lineName\\\": \\\"\\\",\\n      \\\"equipmentStatus\\\": \\\"Produced New Machine\\\",\\n      \\\"isSecondhand\\\": \\\"false\\\",\\n      \\\"equipmentType\\\": \\\"CE_AUX\\\",\\n      \\\"equipmentTypeDesc\\\": \\\"CE Auxiliary Eq\\\",\\n     \\\"functionalLocation\\\": \\\"DEC-PARMALATBERLI-00008\\\",\\n     \\\"functionalLocationDesc\\\": \\\"\\\",\\n     \\\"serialNumber\\\": \\\"8901000009\\\",\\n     \\\"siteName\\\": \\\"PARMALATBERLI\\\",\\n     \\\"siteDesc\\\": \\\"\\\",\\n     \\\"permanentVolumeConversion\\\": \\\"false\\\",\\n     \\\"position\\\": \\\"\\\",\\n     \\\"machineSystem\\\": \\\"CE Other\\\",\\n     \\\"material\\\": \\\"228496-0302\\\",\\n     \\\"machineSystem\\\": \\\"CE Other\\\",\\n     \\\"materialDesc\\\": \\\"PLATFORM\\\",\\n     \\\"manufacturerModelNumber\\\": \\\"3543\\\",\\n     \\\"manufacturerSerialNumber\\\": \\\"\\\",\\n     \\\"superiorEquipment\\\": \\\"9901000043\\\",\\n     \\\"superiorEquipmentName\\\": \\\"R2 200 TETRA BRIK MACHINE TBA/19\\\",\\n     \\\"superiorEquipmentSerialNumber\\\": \\\"64816/00008\\\",\\n     \\\"manufacturer\\\": \\\"Weisel\\\",\\n     \\\"manufacturerCountry\\\": \\\"DE\\\",\\n     \\\"constructionYear\\\": \\\"\\\",\\n     \\\"customerWarrantyStartDate\\\": \\\"\\\",\\n     \\\"customerWarrantyEndDate\\\": \\\"\\\",\\n     \\\"businessType\\\": \\\"Packaging\\\",\\n     \\\"equipmentCategory\\\": \\\"E\\\",\\n     \\\"equipmentCategoryDesc\\\": \\\"External equipment (3rd Party)\\\",\\n     \\\"eofsConfirmationDate\\\": \\\"\\\",\\n     \\\"eofsValidFromDate\\\": \\\"\\\" }    ]\\n}";

	@Rule
	public final AemContext aemContext = CuhuCoreAemContext.getAemContextWithJcrMock(RESOURCE_JSON, CONTENT_ROOT);

	@Before
	public void setup() throws IOException {
		JsonObject jsonResponse = new JsonObject();
		jsonResponse.addProperty(CustomerHubConstants.RESULT, PARAM_STRING);
		jsonResponse.addProperty("status", 200);
		when(equipmentListApiService.getEquipmentList(any(), any())).thenReturn(jsonResponse);
		when(equipmentListExcelService.generateEquipmentListExcel(any(), any(), any())).thenReturn(true);
		aemContext.currentResource(COMPONENT_PATH);
		aemContext.request().setServletPath(COMPONENT_PATH);
		aemContext.request().setMethod(HttpConstants.METHOD_POST);
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
		equipmentListDownloadFileServlet.doGet(request, response);
		assertEquals("status should be ok", HttpStatus.SC_OK, response.getStatus());
	}

}
