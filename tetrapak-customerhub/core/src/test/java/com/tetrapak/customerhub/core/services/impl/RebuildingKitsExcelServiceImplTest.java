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
		paramRequest.setCustomerName("Testing Customer");
		paramRequest.setLocation("FIRMAT");
		paramRequest.setLineCode("X-YYYY  ZZZZ-AAA 1");
		paramRequest.setPosition("0120");
		paramRequest.setRkNumber("455545");
		paramRequest.setRkDesc("Pressure Jaw Right Base Unit");
		paramRequest.setSerialNumber("5454544");
		paramRequest.setEquipmentDesc("Equipment Description");
		paramRequest.setImplStatus("");
		paramRequest.setPermanentVolumeConversion("");
		paramRequest.setImplStatusDate("");
		paramRequest.setImplDate("");
		paramRequest.setImplDeadline("");
		paramRequest.setEquipmentStatus("INPR");
		paramRequest.setEquipmentStructure("");
		paramRequest.setServiceOrder("");
		paramRequest.setOrder("");
		paramRequest.setRebuildingKitStatus("");
		paramRequest.setTechnicalBulletin("");
		paramRequest.setReleaseDate("");
		paramRequest.setReleaseDateFirst("");
		paramRequest.setRkTypeDesc("");
		paramRequest.setPlannedDate("");
		paramRequest.setMechanicalSkills("");
		paramRequest.setAutomationSkills("");
		paramRequest.setCustomerNumber("123456");
		paramRequest.setMachineSystem("MSU");
		paramRequest.setMachineSystemDesc("CE Granulate supply unit");
		paramRequest.setRkHandling("");
		paramRequest.setEquipmentMaterial("");
		paramRequest.setEquipmentMaterialDesc("");
		paramRequest.setEquipmentType("Equipment Type");
		paramRequest.setEquipmentNumber("");
		paramRequest.setCountryName("Germany");
		paramRequest.setLineName("Testing Line Name");
		paramRequest.setElectricalSkills("");
		paramRequest.setRkTime("");
		paramRequest.setKpiExcl("");
		List<RebuildingKits> rbkList = new ArrayList<>();
		rbkList.add(paramRequest);
		return rbkList;
	}
}
