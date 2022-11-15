package com.tetrapak.customerhub.core.mock;

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

	public List<RebuildingKits> getMockRebuildingKitsList() {
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
		List<RebuildingKits> rbk = new ArrayList<>();
		rbk.add(paramRequest);
		return rbk;
	}
}
