package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.rebuildingkits.ImplementationStatusUpdateBean;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.utils.HttpUtil;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.xss.XSSFilter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Executors.class, Runtime.class,HttpUtil.class})
@PowerMockIgnore({ "javax.net.ssl.*", "jdk.internal.reflect.*" })
public class RebuildingKitsApiServiceImplTest {

    private static final String RESOURCE_JSON = "rebuildingkitdetails.json";

    /** The resource path */
    private static final String RESOURCE_PATH = "/content/tetrapak/customerhub/global/en/installed-equipments/rebuilding-kits/rebuilding_kits_detail/jcr:content/root/responsivegrid/rebuildingkitdetails";

    @Spy
    @InjectMocks
    private RebuildingKitsApiServiceImpl rebuildingKitsApiServiceImpl = new RebuildingKitsApiServiceImpl();

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON, RESOURCE_PATH);

    @Mock
    private APIGEEService mockApigeeService;

    @Mock
    private ExecutorService executor;

    @Mock
    private Runtime runtime;

    @Mock
    private XSSFilter mockXssFilter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Resource resource = aemContext.currentResource(RESOURCE_PATH);
        aemContext.request().setResource(resource);
        aemContext.registerService(RebuildingKitsApiServiceImpl.class, rebuildingKitsApiServiceImpl);
        when(mockApigeeService.getApigeeServiceUrl()).thenReturn("mockurl");
        when(mockApigeeService.getApiMappings())
                .thenReturn(new String[] { "rebuildingkits-requestupdate:installedbase/rebuildingkits/requestupdate" });
        PowerMockito.mockStatic(Runtime.class);
        PowerMockito.mockStatic(Executors.class);
        PowerMockito.when(Runtime.getRuntime()).thenReturn(runtime);
        PowerMockito.when(Executors.newFixedThreadPool(anyInt())).thenReturn(executor);
        PowerMockito.mockStatic(HttpUtil.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status",201);
        jsonObject.addProperty("result","{\"id\":2014,\"entityId\":null,\"rebuildingKit\":\"2861844-0101\",\"rebuildingKitName\":\"UK Chemical valve in the HCCU\",\"reportDate\":\"20230505\",\"reportedBy\":\"demo@tetrapak.com\",\"action\":\"Install\",\"installMethod\":\"Implemented; 05/05/2023\",\"comment\":\"Sample Comments\",\"ibcComment\":null,\"statusOnReportDate\":\"NotImplemented\",\"statusRequested\":\"Implemented\",\"closed\":false,\"implementationStatus\":null,\"mcon\":null,\"serviceOrder\":null,\"cluster\":null,\"marketGroup\":null,\"country\":null,\"serialNumber\":\"63636/00400\",\"equipment\":null,\"manuelClosedDate\":null,\"commentUpdateDate\":null,\"notValidRecord\":null,\"customerId\":null,\"source\":\"MyTetraPak_App\"}");
        PowerMockito.when(HttpUtil.sendAPIGeePostWithEntity(any(), any(), any())).thenReturn(jsonObject);
        Mockito.when(mockXssFilter.filter(anyString())).thenAnswer(i -> i.getArgumentAt(0, String.class));
    }

    @Test
    public void testUpdateImplementationStatus(){
        ImplementationStatusUpdateBean implBean = new ImplementationStatusUpdateBean();
        implBean.setDate("05/05/2023");
        implBean.setReportedStatus("Implemented");
        implBean.setCurrentStatus("Pending");
        implBean.setComment("Sample Comments");
        implBean.setReportedRebuildingKitName("UK Chemical valve in the HCCU");
        implBean.setReportedBy("demo@tetrapak.com");
        implBean.setReportedRebuildingKit("2861844-0101");
        implBean.setSerialNumber("63636/00400");
        JsonObject jsonObject = rebuildingKitsApiServiceImpl.updateImplementationStatus("token","demo@tetrapak.com",implBean);
        jsonObject.get("status");
        assertEquals(jsonObject.get("status").getAsInt(),201);
    }
}
