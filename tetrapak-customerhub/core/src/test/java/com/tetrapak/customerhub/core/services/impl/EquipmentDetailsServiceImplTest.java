package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tetrapak.customerhub.core.beans.equipment.EquipmentUpdateFormBean;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.utils.HttpUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
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

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpUtil.class)
@PowerMockIgnore({ "javax.net.ssl.*", "jdk.internal.reflect.*" })
public class EquipmentDetailsServiceImplTest {

    private static final String BEAN_OK_FILE = "src/test/resources/equipmentUpdateFormBean.json";

    @InjectMocks
    private EquipmentDetailsServiceImpl equipmentDetailsService = new EquipmentDetailsServiceImpl();

    private EquipmentUpdateFormBean bean;

    @Mock
    private APIGEEService mockApigeeService;

    @Mock
    private XSSFilter mockXssFilter;

    @Before
    public void setUp() throws IOException {
        String content = readFileFromPath(BEAN_OK_FILE);
        Mockito.when(mockApigeeService.getApigeeServiceUrl()).thenReturn("mockurl");
        Mockito.when(mockApigeeService.getApiMappings())
                .thenReturn(new String[] { "myequipment-requestUpdate:installedbase/equipments/requestupdate" });
        Mockito.when(mockXssFilter.filter(anyString())).thenAnswer(i -> i.getArgumentAt(0, String.class));
        bean = (new Gson()).fromJson(content, EquipmentUpdateFormBean.class);
    }

    @Test
    public void testAddEquipmentResponseNotNull() {
        JsonObject jsonObject = equipmentDetailsService.editEquipment("mockUserId", bean, StringUtils.EMPTY);
        assertNotNull("Should return non null", jsonObject);
    }

    @Test
    public void testEditEquipmentRequestValues() {
        PowerMockito.mockStatic(HttpUtil.class);
        PowerMockito.when(HttpUtil.sendAPIGeePostWithEntity(any(), anyString(), anyString())).thenAnswer(i -> {
            final String entity = i.getArgumentAt(2, String.class);
            return new JsonParser().parse(entity);
        });

        JsonObject jsonObject = equipmentDetailsService.editEquipment("mockUserId", bean, StringUtils.EMPTY);
        assertEquals("Mapped value equal to input", "8000000930", jsonObject.get("equipmentNumber").getAsString());
        assertEquals("Mapped value equal to input", "MyTetraPak_App", jsonObject.get("source").getAsString());
        assertEquals("Mapped value equal to input", "mockUserId", jsonObject.get("reportedBy").getAsString());
        assertEquals("Mapped value equal to input", "", jsonObject.get("comment").getAsString());
        assertEquals("Mapped value equal to input", "Country", getMetadata(jsonObject, 0, "metaDataName"));
        assertEquals("Mapped value equal to input", "AR", getMetadata(jsonObject, 0, "metaDataRequestedValue"));
        assertEquals("Mapped value equal to input", "Germany", getMetadata(jsonObject, 0, "metaDataActualValue"));
        assertEquals("Mapped value equal to input", "Comment", getMetadata(jsonObject, 2, "metaDataName"));
        assertEquals("Mapped value equal to input", "test", getMetadata(jsonObject, 2, "metaDataRequestedValue"));
        assertEquals("Mapped value equal to input", "", getMetadata(jsonObject, 2, "metaDataActualValue"));
    }

    private String getMetadata(JsonObject jsonObject, int index, String metaDataName) {
        return jsonObject.getAsJsonArray("metaDatas").get(index).getAsJsonObject().get(metaDataName).getAsString();
    }

    private String readFileFromPath(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        return IOUtils.toString(fis, StandardCharsets.UTF_8);
    }
}
