package com.tetrapak.customerhub.core.services.impl;

import com.tetrapak.customerhub.core.beans.equipmentlist.Equipments;
import com.tetrapak.customerhub.core.beans.financials.results.Params;
import com.tetrapak.customerhub.core.beans.financials.results.Results;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.ServletOutputStream;

import static org.junit.Assert.assertFalse;

@RunWith(MockitoJUnitRunner.class)
public class EquipmentListExcelServiceImplTest {

    @Mock
    private SlingHttpServletRequest servletRequest;

    @Mock
    private SlingHttpServletResponse response;

    EquipmentListExcelServiceImpl equipmentListExcelService = new EquipmentListExcelServiceImpl();

    @Mock
    ServletOutputStream servletOutputStream;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

        Mockito.when(response.getOutputStream()).thenReturn(servletOutputStream);
        Mockito.doNothing().when(servletOutputStream).write(Mockito.any(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void testGenerateFinancialsResultsExcel() {
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
    }
}
