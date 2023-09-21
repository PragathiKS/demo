package com.tetrapak.supplierportal.core.services.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.tetrapak.supplierportal.core.mock.SupplierPortalCoreAemContext;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * 
 * @author Sunil Kumar Yadav(sunmanak)
 *
 */
public class InvoiceStatusServiceImplTest {

	private static final String[] INVOICE_CODE_MAPPINGS = {"In Process-0:1:2:3:4:5:6:7:11:12:13:14:19:20:21:22:23:24:25:26:27:28:29:30:34:40:41:42:44:45:46:50:51","Rejected-8:9:10:16:17","Posted-15:18:31:32:33:97:98"};

	private static final String SERVLET_RESOURCE_JSON = "allContent.json";
	private static final String SERVLET_RESOURCE_PATH = "";

	private InvoiceStatusServiceImpl service = new InvoiceStatusServiceImpl();

	@Rule
	public final AemContext aemContext = SupplierPortalCoreAemContext.getAemContextWithJcrMock(SERVLET_RESOURCE_JSON,
			SERVLET_RESOURCE_PATH);

	@Before
	public void setup() throws IOException {
		MockitoAnnotations.initMocks(this);
		Map<String, Object> _config = new HashMap<>();
		_config.put("invoiceStatusCodeMappings", INVOICE_CODE_MAPPINGS);
		aemContext.registerInjectActivateService(service, _config);
	}

	@Test
	public void invoiceStatusCodeMappings() throws IOException {
		Map<String, List<String>> map = service.invoiceStatusCodeMap();
		Assert.assertNotNull(map);
	}
}
