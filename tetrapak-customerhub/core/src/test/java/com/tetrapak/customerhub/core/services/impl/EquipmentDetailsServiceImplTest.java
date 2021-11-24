package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.beans.equipment.EquipmentResponse;
import com.tetrapak.customerhub.core.beans.equipment.EquipmentUpdateFormBean;
import com.tetrapak.customerhub.core.services.APIGEEService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class EquipmentDetailsServiceImplTest {

    private static final String BEAN_OK_FILE = "src/test/resources/equipmentUpdateFormBean.json";

    @InjectMocks
    private EquipmentDetailsServiceImpl equipmentDetailsService = new EquipmentDetailsServiceImpl();

    private EquipmentUpdateFormBean bean;

    @Mock
    private APIGEEService mockApigeeService;

    @Mock
    private HttpClient mockClient;

    @Mock
    private HttpResponse response;

    @Before
    public void setUp() throws IOException {
        String content = readFileFromPath(BEAN_OK_FILE);
        Mockito.when(mockApigeeService.getApigeeServiceUrl()).thenReturn("mockurl");
        Mockito.when(mockClient.execute(Mockito.any(HttpPost.class)))
                .thenReturn(response);
        bean = (new Gson()).fromJson(content, EquipmentUpdateFormBean.class);
    }

    @Test
    public void testAddEquipmentMissingToken() {
        EquipmentResponse response = equipmentDetailsService.editEquipment(bean, StringUtils.EMPTY);
        assertEquals("Should return bad request", HttpStatus.SC_BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testAddEquipmentResponseNotNull() {
        EquipmentResponse response = equipmentDetailsService.editEquipment(bean, StringUtils.EMPTY);
        assertNotNull("Should return non null", response);
    }

    private String readFileFromPath(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        return IOUtils.toString(fis, StandardCharsets.UTF_8);
    }
}
