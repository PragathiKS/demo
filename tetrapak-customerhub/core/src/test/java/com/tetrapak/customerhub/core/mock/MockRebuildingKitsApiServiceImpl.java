package com.tetrapak.customerhub.core.mock;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.rebuildingkits.ImplementationStatusUpdateBean;
import com.tetrapak.customerhub.core.beans.rebuildingkits.RebuildingKits;
import com.tetrapak.customerhub.core.services.RebuildingKitsApiService;
import java.util.ArrayList;
import java.util.List;

public class MockRebuildingKitsApiServiceImpl implements RebuildingKitsApiService {

	@Override
	public List<RebuildingKits> getRebuildingkitsList(String token, String countryCode) {
		return getMockRebuildingKitsList();
	}

	@Override
	public int getNoOfRecordsCount() {
		return 750;
	}

	@Override
	public JsonObject updateImplementationStatus(String token, String emailId, ImplementationStatusUpdateBean bean) {
		return null;
	}

	public List<RebuildingKits> getMockRebuildingKitsList() {
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
		paramRequest.setRkGeneralNumber("2616315-0100");
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
		List<RebuildingKits> rbk = new ArrayList<>();
		rbk.add(paramRequest);
		return rbk;
	}
}
