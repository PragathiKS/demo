package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.equipment.AddEquipmentFormBean;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.utils.HttpUtil;
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
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpUtil.class)
@PowerMockIgnore({ "javax.net.ssl.*", "jdk.internal.reflect.*" })
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
        Mockito.when(mockApigeeService.getApiMappings()).thenReturn(
                new String[] { "myequipment-reportmissing:installedbase/equipments/reportmissing",
                        "myequipment-reportmissing-attachments:installedbase/equipments/reportmissing/{id}/attachments?filenumber={filenumber}"
                });
        Mockito.when(mockClient.execute(Mockito.any(HttpPost.class))).thenReturn(response);
        bean = (new Gson()).fromJson(content, AddEquipmentFormBean.class);
    }

    @Test
    public void testAddEquipmentResponseNotNull() {
        JsonObject jsonObject = addEquipmentService.addEquipment("mockUserId", bean, StringUtils.EMPTY,
                new ArrayList<>());
        assertNotNull("Should return non null", jsonObject);
    }

    @Test
    public void testAddEquipmentUrlWithFiles() {
        PowerMockito.mockStatic(HttpUtil.class);
        PowerMockito.when(HttpUtil.sendAPIGeePostWithEntity(any(), anyString(), anyString())).thenAnswer(i -> {
            JsonObject result = new JsonObject();
            result.addProperty("result", "{\"id\":127}");
            return result;
        });
        PowerMockito.when(HttpUtil.sendAPIGeePostWithFiles(anyString(), anyString(), any())).thenAnswer(i -> {
            JsonObject result = new JsonObject();
            result.addProperty("result", i.getArgumentAt(0, String.class));
            return result;
        });

        JsonObject jsonObject = addEquipmentService.addEquipment("mockUserId", bean, StringUtils.EMPTY,
                Arrays.asList(new File("file-1"), new File("file-2")));
        String expectedAttachment1Url = "\"mockurl/installedbase/equipments/reportmissing/127/attachments?filenumber=1\"";
        String expectedAttachment2Url = "\"mockurl/installedbase/equipments/reportmissing/127/attachments?filenumber=2\"";
        assertEquals("Url should have id and file number 1", expectedAttachment1Url,
                jsonObject.get("file0").getAsString());
        assertEquals("Url should have id and file number 2", expectedAttachment2Url,
                jsonObject.get("file1").getAsString());
    }

    private String readFileFromPath(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        return IOUtils.toString(fis, StandardCharsets.UTF_8);
    }
}
