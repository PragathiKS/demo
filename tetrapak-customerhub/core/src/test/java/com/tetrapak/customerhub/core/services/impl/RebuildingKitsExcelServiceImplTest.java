package com.tetrapak.customerhub.core.services.impl;

import com.tetrapak.customerhub.core.beans.rebuildingkits.RebuildingKits;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class RebuildingKitsExcelServiceImplTest {

	@Mock
	private SlingHttpServletRequest servletRequest;

	@Mock
	private SlingHttpServletResponse response;

	RebuildingKitsExcelServiceImpl rebuildingKitsExcelService = new RebuildingKitsExcelServiceImpl();

	@Mock
	ServletOutputStream servletOutputStream;

	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {

		Mockito.when(response.getOutputStream()).thenReturn(servletOutputStream);
		Mockito.doNothing().when(servletOutputStream).write(Mockito.any(), Mockito.anyInt(), Mockito.anyInt());
	}

	/**
     * Test method for
     * {@link RebuildingKitsExcelServiceImpl#generateCSV(List, SlingHttpServletRequest, SlingHttpServletResponse)}.
	 * @throws IOException 
     */
    @Test
    public void testGenerateEquipmentsExcelWithNullApiResp() throws IOException {
        List<RebuildingKits> rbk = getMockRebuildingKitsList();
        assertTrue(rebuildingKitsExcelService.generateCSV(rbk, servletRequest, response));
    }
	
	private List<RebuildingKits> getMockRebuildingKitsList() {
		RebuildingKits paramRequest = new RebuildingKits();
		paramRequest.setCountryCode("DE");
		paramRequest.setCountryName("Germany");
		paramRequest.setCustomerNumber("123456");
		paramRequest.setCustomerName("Testing Customer");
		paramRequest.setLocation("FIRMAT");
		paramRequest.setLineName("Testing Line Name");
		paramRequest.setPosition("0120");
		paramRequest.setEquipmentType("Equipment Type");
		paramRequest.setEquipmentDesc("Equipment Description");
		paramRequest.setSerialNumber("5454544");
		paramRequest.setPermanentVolumeConversion("");
		paramRequest.setEquipmentStatus("INPR");
		paramRequest.setRkNumber("455545");
		paramRequest.setRkDesc("Rk Description");
		paramRequest.setImplDate("");
		paramRequest.setImplStatus("");
		paramRequest.setImplStatusDate("");
		paramRequest.setRebuildingKitStatus("");
		paramRequest.setReleaseDateFirst("");
		paramRequest.setReleaseDate("");
		paramRequest.setMechanicalSkills("");
		paramRequest.setElectricalSkills("");
		paramRequest.setMachineSystem("MSU");
		paramRequest.setMachineSystemDesc("CE Granulate supply unit");
		List<RebuildingKits> rbkList = new ArrayList<>();
		rbkList.add(paramRequest);
		return rbkList;
	}
}
