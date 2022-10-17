package com.tetrapak.customerhub.core.mock;

import com.tetrapak.customerhub.core.beans.rebuildingkits.RebuildingKits;
import com.tetrapak.customerhub.core.services.RebuildingKitsApiService;
import java.util.ArrayList;
import java.util.List;

public class MockRebuildingKitsApiServiceImpl implements RebuildingKitsApiService {
	private static final String PARAM_STRING = "{\\n  \\\"data\\\": [\\n    {\\n      \\\"countryCode\\\": \\\"DE\\\",\\n      \\\"countryName\\\": \\\"Germany\\\",\\n      \\\"lineName\\\": \\\"\\\",\\n      \\\"equipmentStatus\\\": \\\"Produced New Machine\\\",\\n      \\\"equipmentType\\\": \\\"CE_AUX\\\",\\n      \\\"equipmentTypeDesc\\\": \\\"CE Auxiliary Eq\\\",\\n     \\\"serialNumber\\\": \\\"8901000009\\\",\\n      \\\"permanentVolumeConversion\\\": \\\"false\\\",\\n     \\\"position\\\": \\\"0020\\\",\\n     \\\"machineSystem\\\": \\\"CE Other\\\",\\n     \\\"customerNumber\\\": \\\"1571332\\\",\\n     \\\"location\\\": \\\"FIRMAT\\\",\\n     \\\"manufacturerSerialNumber\\\": \\\"\\\",\\n     \\\"equipmentNumber\\\": \\\"9000196572\\\",\\n     \\\"equipmentDesc\\\": \\\"GSU-050V\\\",\\n     \\\"equipmentMaterial\\\": \\\"1025041-0500\\\",\\n     \\\"equipmentMaterialDesc\\\": \\\"Granulate supply unit\\\",\\n     \\\"rkNumber\\\": \\\"3304551-0102\\\",\\n     \\\"rkDesc\\\": \\\"UK REPL 90459-5632, 4807 with DeviceNet\\\",\\n     \\\"rkTypeCode\\\": \\\"B2\\\",\\n     \\\"rkTypeDesc\\\": \\\"UK REPL 90459-5632, 4807 with DeviceNet\\\",\\n     \\\"implStatus\\\": \\\"Pending\\\",\\n     \\\"implStatusGroup\\\": \\\"Pending\\\",\\n     \\\"implStatusDate\\\": \\\"\\\",\\n     \\\"rebuildingKitStatus\\\": \\\"Released\\\",\\n     \\\"equipmentStatusDesc\\\": \\\"In Production\\\" ,\\n      \\\"releaseDateFirst\\\": \\\"2013-12-19\\\" ,\\n      \\\"releaseDate\\\": \\\"2013-12-19\\\" ,\\n      \\\"mechanicalSkills\\\": \\\"\\\" ,\\n      \\\"automationSkills\\\": \\\"\\\" ,\\n      \\\"electricalSkills\\\": \\\"\\\" ,\\n      \\\"machineSystem\\\": \\\"GSU\\\" ,\\n      \\\"machineSystemDesc\\\": \\\"CE Granulate supply unit\\\"}    ]\\n}";

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
		paramRequest.setEquipmentTypeDesc("Equipment Type Description");
		paramRequest.setEquipmentStatusDescription("Equipment Status Description");
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
