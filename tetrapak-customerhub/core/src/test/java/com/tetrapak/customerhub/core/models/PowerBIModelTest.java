package com.tetrapak.customerhub.core.models;
//import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
//import com.tetrapak.customerhub.core.services.PowerBiReportService;
//import com.sun.tools.javac.util.Assert;
import io.wcm.testing.mock.aem.junit.AemContext;

import org.junit.*;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.apache.sling.api.SlingHttpServletResponse;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.MockPowerBiReportServiceImpl;
import com.tetrapak.customerhub.core.services.PowerBiReportService;

public class PowerBIModelTest {
    

    @InjectMocks
    private PowerBIModel powerbiModel;

    @Mock  
    private PowerBiReportService powerbireportService;
    @Mock
    protected SlingHttpServletResponse response;

    private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/powerbitestpage";
    private static final String COMPONENT_PATH = "/content/tetrapak/customerhub/global/en/powerbitestpage/jcr:content/root/responsivegrid/embedpowerbireport";
	//private static final String CONTENT_PATH = "/content/tetrapak/customerhub/content-components/en/jcr:content";
	private static final String RESOURCE_JSON = "pbiContent.json";

    private MockPowerBiReportServiceImpl powerbireportServicemock;

	/**@Rule
    public AemContext aemContext = new AemContext();
	
	 * Setup method for the class.
	 */
    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContextWithJcrMock(RESOURCE_JSON, CONTENT_ROOT);
    
	@Before
	public void setup() throws Exception
    {   
        MockitoAnnotations.initMocks(this);
        powerbireportServicemock = new MockPowerBiReportServiceImpl();
        when(powerbireportService.getPbireportid()).thenReturn("5d3c5f2a-b062-4ddb-ab09-4224fb845a99");
        when(powerbireportService.getGenerateEmbedToken()).thenReturn("H4sIAAAAAAAEAB2Uta70ZgBE3-W2G8lMkf7CuGZac2daM-PnKO-eq_SnmTOj-efHTkE_pcXP3z_wRbbaubpwfl-e7RVGT8Em0PUOkfYZRPVMGxiaYfhiMe5kpbQyHXdX4pf9esxKJY-wWKvXPXKBb-OAWj_jQUpLdNx9fqtAdOISWbd7VWibTQAbKhgcmUBLeghKSZKlKe8dKnLA5s8i-P0akpPQ4ydxCQ0vL5epOeKbEDQOkrO60M1wYTilt9l-ezCNza5iPqBniParsSVhbZQK-TCaLojBbn4bcKuG6eK-jKAFs0UG_91KSsWwiBqbfjJgk3qp1jDvtssP2JdpXbtNOh5LpgJDzXVw2YaCL8jY34IVRuEUhSfOaOqtZ0dY-tXBM7T_zHVavNJINJt0i4vVx-MFM3exquBtHT-coVNXEX9cF74uKb9zj-555ZvNfih-fO9ZhkF69i0e1vIgb8PaawrgnRz65mlqYsZUTJgRicGTGjFFDt-mZHM9nIBa7u70qu7EEy6uW-lvaEsmOpqVkQS0peRihs-uU-YalEgxmRWk0lpxReN0-WKlXvNySw9kxoXocgwcGmZzNWF2a6SJc-RJWD-OXa-VhVkwXHTJ7DzuuYHHeN7MbLIH4QQk_BVF1p81hDMTPTLC6e0jlPTWxC5HqGt0r25YANsK4qsNNMWkwhk9RDs30NNobFlB9kRKrwqb6X4mLD-JbrxgQ5ebCpREhgY5q9BbLLlvrg2qSbcnxwjNP2kt4GI6pcFnKNC35UqazpO1TQaLXY0Cc8K94d5fuX7oIAx0j1JxTM6I0oVgd1qSmKu6Pz9__fArmPdJK8Hv9I14vfUxZF9TxU7kcxC6VxfuAa7-CVqllS5sZ3C2V8nxUIdFEnAwpTRsg3hzmkJPwLmk5v66t8_1_ZZRV75le1OgTjq_ztAxY4XLoRagpv4sHJkpqSGoFODLsDtIeRMaA22Th_sWpW2meMHsw9LzwcL69nkvWFxOdW7nixqvbIGmAGHeeB-X--bRzxYkwgJU_LhvN4snjCnnAhWIy5DkEohDCE0Ogem5DK6W4fAPHAXJR1akZUHqLxbr9NdKcyNN3u6IWO5vk3cmSwt0hQwGl7hsGjXZK-9y3tkCoVH_-aRKR1rDZFccHZOrDI8nx4XJ9DEgAf8WqeX2drVR40t6XFKU2T__awZzXa5K8Gu5W2eQQAb_GToTp-XG8Goodf6nPk01pvuxlr8Yy70MjXWM0HHa08UAcq586xUAJEGtv4cvqAFvttnQCLMevq6ucUNJV2tS0Mu2lsN8fk1CburDywmTF9h_Q7zjhNRco5Ws7o78fToFARJ05xu4QUJbUg47bOUn3O_jcG7uzF6twmMXj5hHpKSre5NAKYq_rhuRqIc065yKSEnSVw3jEV0uCZnQPHkM01TSUhc6pAY0s8yk9dLvG8J2OixLZj9dZoiEfsmnhVTILmS6UPNs9wmOxSF2Ydl3Lew-fYA9LocRgspGLyCgaGQEED_oQeNs2W4S_jvh5vjqWCa_79tREZXic8Y2DzYkLXqV3sfOsOcBWaE30KiGD4oN3EVXBuj61fzvfztcuR3uBQAA.eyJjbHVzdGVyVXJsIjoiaHR0cHM6Ly9XQUJJLU5PUlRILUVVUk9QRS1FLVBSSU1BUlktcmVkaXJlY3QuYW5hbHlzaXMud2luZG93cy5uZXQiLCJleHAiOjE2ODk5MzU5NjYsImFsbG93QWNjZXNzT3ZlclB1YmxpY0ludGVybmV0Ijp0cnVlfQ==");

        aemContext.registerService(PowerBiReportService.class, powerbireportService);
        powerbiModel.init();
  }

  	@Test
	public void testModelNotNull() {
		assertNotNull("Model Not null",powerbiModel);
	}

	@Test
	public void testinit() throws Exception {
    
       /*  Assert.assertEquals(powerbireportServicemock.getGenerateEmbedToken(),powerbiModel.getEmbedtoken());
        Assert.assertEquals(powerbireportService.getPbireportid(),powerbiModel.getReportid());
       */ Assert.assertEquals("https://app.powerbi.com/reportEmbed?reportId=5d3c5f2a-b062-4ddb-ab09-4224fb845a99",powerbiModel.getEmbedURL());
        
 
	}

}
