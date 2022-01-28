package com.tetrapak.customerhub.core.mock;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.equipmentlist.Equipments;
import com.tetrapak.customerhub.core.beans.equipmentlist.Results;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.EquipmentListApiService;

import java.util.ArrayList;
import java.util.List;

public class MockEquipmentListApiServiceImpl implements EquipmentListApiService {
	private static final String PARAM_STRING = "{\\n  \\\"data\\\": [\\n    {\\n      \\\"id\\\": \\\"8901000009\\\",\\n      \\\"countryCode\\\": \\\"DE\\\",\\n      \\\"countryName\\\": \\\"Germany\\\",\\n      \\\"lineName\\\": \\\"\\\",\\n      \\\"equipmentStatus\\\": \\\"Produced New Machine\\\",\\n      \\\"isSecondhand\\\": \\\"false\\\",\\n      \\\"equipmentType\\\": \\\"CE_AUX\\\",\\n      \\\"equipmentTypeDesc\\\": \\\"CE Auxiliary Eq\\\",\\n     \\\"functionalLocation\\\": \\\"DEC-PARMALATBERLI-00008\\\",\\n     \\\"functionalLocationDesc\\\": \\\"\\\",\\n     \\\"serialNumber\\\": \\\"8901000009\\\",\\n     \\\"siteName\\\": \\\"PARMALATBERLI\\\",\\n     \\\"siteDesc\\\": \\\"\\\",\\n     \\\"permanentVolumeConversion\\\": \\\"false\\\",\\n     \\\"position\\\": \\\"\\\",\\n     \\\"machineSystem\\\": \\\"CE Other\\\",\\n     \\\"material\\\": \\\"228496-0302\\\",\\n     \\\"machineSystem\\\": \\\"CE Other\\\",\\n     \\\"materialDesc\\\": \\\"PLATFORM\\\",\\n     \\\"manufacturerModelNumber\\\": \\\"3543\\\",\\n     \\\"manufacturerSerialNumber\\\": \\\"\\\",\\n     \\\"superiorEquipment\\\": \\\"9901000043\\\",\\n     \\\"superiorEquipmentName\\\": \\\"R2 200 TETRA BRIK MACHINE TBA/19\\\",\\n     \\\"superiorEquipmentSerialNumber\\\": \\\"64816/00008\\\",\\n     \\\"manufacturer\\\": \\\"Weisel\\\",\\n     \\\"manufacturerCountry\\\": \\\"DE\\\",\\n     \\\"constructionYear\\\": \\\"\\\",\\n     \\\"customerWarrantyStartDate\\\": \\\"\\\",\\n     \\\"customerWarrantyEndDate\\\": \\\"\\\",\\n     \\\"businessType\\\": \\\"Packaging\\\",\\n     \\\"equipmentCategory\\\": \\\"E\\\",\\n     \\\"equipmentCategoryDesc\\\": \\\"External equipment (3rd Party)\\\",\\n     \\\"eofsConfirmationDate\\\": \\\"\\\",\\n     \\\"eofsValidFromDate\\\": \\\"\\\" }    ]\\n}";

	@Override
	public List<Equipments> getEquipmentList(String token, String countryCode) {
		return getMockEquipmentList();
	}

	@Override
	public int getNoOfRecordsCount() {
		return 750;
	}

	public List<Equipments> getMockEquipmentList() {
		Equipments paramRequest = new Equipments();
		paramRequest.setId("ID");
		paramRequest.setCountryCode("Country");
		paramRequest.setCountryName("Country Name");
		paramRequest.setLineName("Line");
		paramRequest.setEquipmentStatus("Equipment Status");
		paramRequest.setIsSecondhand("Is Secondhand");
		paramRequest.setEquipmentType("Equipment Type");
		paramRequest.setEquipmentTypeDesc("Equipment Type Description");
		paramRequest.setFunctionalLocation("Functional Location");
		paramRequest.setFunctionalLocationDesc("Functional Location Description");
		paramRequest.setSerialNumber("Serial Number");
		paramRequest.setSiteName("Site Name");
		paramRequest.setSiteDesc("Site Description");
		paramRequest.setPosition("Position");
		paramRequest.setMachineSystem("Machine System");
		paramRequest.setMaterial("Material");
		paramRequest.setMaterialDesc("Material Description");
		paramRequest.setManufacturerModelNumber("Manufacturer Model Number");
		paramRequest.setManufacturerSerialNumber("Manufacturer Serial Number");
		paramRequest.setSuperiorEquipment("Superior Equipment");
		paramRequest.setSuperiorEquipmentName("Superior Equipment Name");
		paramRequest.setSuperiorEquipmentSerialNumber("Superior Equipment Serial Number");
		paramRequest.setManufacturer("Manufacturer");
		paramRequest.setManufacturerCountry("Manufacturer Country");
		paramRequest.setConstructionYear("Construction Year");
		paramRequest.setCustomerWarrantyEndDate("Customer Warranty End Date");
		paramRequest.setCustomerWarrantyStartDate("Customer Warranty Start Date");
		paramRequest.setBusinessType("Business Type");
		paramRequest.setEquipmentCategory("Equipment Category");
		paramRequest.setEquipmentCategoryDesc("Equipment Category Description");
		paramRequest.setEofsConfirmationDate("Eofs Confirmation Date");
		paramRequest.setEofsValidFromDate("Eofs Valid From Date");
		List<Equipments> equipments = new ArrayList<>();
		equipments.add(paramRequest);
		return equipments;
	}
}
