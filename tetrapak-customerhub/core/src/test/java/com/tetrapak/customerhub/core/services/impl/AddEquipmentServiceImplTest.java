package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.equipment.AddEquipmentFormBean;
import com.tetrapak.customerhub.core.services.APIGEEService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.sling.xss.XSSFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class AddEquipmentServiceImplTest {

    private static final String TEST_FILE = "src/test/resources/addEquipmentFormBean.json";

    @InjectMocks
    private AddEquipmentServiceImpl addEquipmentService = new AddEquipmentServiceImpl();

    private AddEquipmentFormBean bean;

    @Mock
    private APIGEEService mockApigeeService;

    @Mock
    private HttpClient mockClient;

    @Mock
    private HttpResponse response;

    @Mock
    private XSSFilter mockXssFilter;

    @Before
    public void setUp() throws IOException {
        String content = readFileFromPath(TEST_FILE);
        Mockito.when(mockApigeeService.getApigeeServiceUrl()).thenReturn("mockurl");
        Mockito.when(mockApigeeService.getApiMappings()).thenReturn(new String[] {"myequipment-reportmissing:installedbase/equipments/reportmissing?version=preview"});
        Mockito.when(mockClient.execute(Mockito.any(HttpPost.class)))
                .thenReturn(response);
        bean = (new Gson()).fromJson(content, AddEquipmentFormBean.class);
    }

    @Test
    public void testAddEquipmentResponseNotNull() {
        JsonObject jsonObject = addEquipmentService.addEquipment("mockUserId", bean, StringUtils.EMPTY, new ArrayList<>());
        assertNotNull("Should return non null", jsonObject);
    }

    private String readFileFromPath(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        return IOUtils.toString(fis, StandardCharsets.UTF_8);
    }
}
