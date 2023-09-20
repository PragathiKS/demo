package com.tetrapak.customerhub.core.services.impl;

import io.wcm.testing.mock.aem.junit.AemContext;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import com.tetrapak.customerhub.core.services.config.PowerBiReportConfig;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.services.PowerBiReportService;

public class PowerBiReportServiceImplTest {
    
    @InjectMocks
    private PowerBiReportServiceImpl pbiService;
    
    HttpPost httpPost;
    HttpClientBuilderFactory httpFactory;
    CloseableHttpClient httpClient;
    HttpClientBuilder httpClientBuilder;
    CloseableHttpResponse httpResponse;
    PowerBiReportConfig pbiConfig;
    StatusLine statusLine;
    HttpEntity httpEntity;

    private static final String RESOURCE_JSON = "pbiContent.json";
    private static final String RESOURCE_PATH = "";
  
    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON,RESOURCE_PATH);
         
    @Before
    public void setup() throws Exception {  
    httpFactory = PowerMockito.mock(HttpClientBuilderFactory.class);
    httpClient= PowerMockito.mock(CloseableHttpClient.class);
    httpClientBuilder= PowerMockito.mock(HttpClientBuilder.class);
    httpResponse= PowerMockito.mock(CloseableHttpResponse.class);
    pbiConfig= PowerMockito.mock(PowerBiReportConfig.class);
    httpPost = PowerMockito.mock(HttpPost.class);
    statusLine=PowerMockito.mock(StatusLine.class);
    httpEntity=PowerMockito.mock(HttpEntity.class);
    MockitoAnnotations.initMocks(this);
    httpClient = PowerMockito.mock(CloseableHttpClient.class);
    httpResponse=PowerMockito.mock(CloseableHttpResponse.class);
    PowerMockito.when( httpFactory.newBuilder()).thenReturn(httpClientBuilder);
    PowerMockito.when( httpFactory.newBuilder().build()).thenReturn(httpClient);
    PowerMockito.when(httpClient.execute(org.mockito.Matchers.any())).thenReturn(httpResponse);
    PowerMockito.when(httpResponse.getStatusLine()).thenReturn(statusLine);
    PowerMockito.when(httpResponse.getStatusLine().getStatusCode()).thenReturn(200);
    PowerMockito.when(httpResponse.getEntity()).thenReturn(httpEntity);
    String access_token="{\"token_type\":\"Bearer\",\"expires_in\":\"3599\",\"ext_expires_in\":\"3599\",\"expires_on\":\"1691499333\",\"not_before\":\"1691495433\",\"resource\":\"https://analysis.windows.net/powerbi/api\",\"access_token\":\"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Ii1LSTNROW5OUjdiUm9meG1lWm9YcWJIWkdldyIsImtpZCI6Ii1LSTNROW5OUjdiUm9meG1lWm9YcWJIWkdldyJ9.eyJhdWQiOiJodHRwczovL2FuYWx5c2lzLndpbmRvd3MubmV0L3Bvd2VyYmkvYXBpIiwiaXNzIjoiaHR0cHM6Ly9zdHMud2luZG93cy5uZXQvZDJkMjc5NGEtNjFjYy00ODIzLTk2OTAtOGUyODhmZDU1NGNjLyIsImlhdCI6MTY5MTQ5NTQzMywibmJmIjoxNjkxNDk1NDMzLCJleHAiOjE2OTE0OTkzMzMsImFpbyI6IkUyRmdZRmd0SmowbE1mVzJ4cnhjN1MwMWJKT1VBQT09IiwiYXBwaWQiOiI0ZjBmNzY2Yi1kMmViLTRhNTAtYmUyYS01MmZmOGUxNjU3YjciLCJhcHBpZGFjciI6IjEiLCJpZHAiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC9kMmQyNzk0YS02MWNjLTQ4MjMtOTY5MC04ZTI4OGZkNTU0Y2MvIiwiaWR0eXAiOiJhcHAiLCJvaWQiOiJlMGFkZjMwYy1kNDVlLTQ0MzYtOTliOS1iZTZiNzU1NzY5ZWEiLCJyaCI6IjAuQVFJQVNublMwc3hoSTBpV2tJNG9qOVZVekFrQUFBQUFBQUFBd0FBQUFBQUFBQUFDQUFBLiIsInN1YiI6ImUwYWRmMzBjLWQ0NWUtNDQzNi05OWI5LWJlNmI3NTU3NjllYSIsInRpZCI6ImQyZDI3OTRhLTYxY2MtNDgyMy05NjkwLThlMjg4ZmQ1NTRjYyIsInV0aSI6InhOdnRtTl9mdkVteExzOG5nZndzQUEiLCJ2ZXIiOiIxLjAifQ.qM2jRwkIoj1uuTiu1r6FrwzeFIsvbIMrBxWeVBeiFgRFvfNzEaSqrOCG0hURbIQ1NNE_FmTjUc_j7Tmr8ruUWYq6u9IviHoO6-LREhKU_oOQpcPaoHsqMN5CEleNfL0ZDstAgOek0cfrTy-H-gBPmoTmFthG-CNJmGps0rl2h2CTC53BUhdrZeKy4wHxnmb6bJBH1zJvcd8h3osyE0fsCNyyM1CUtgrezMN5Vs02xAbq2CXztIgmcPDw1kreZ165E9wJ8UNXLSU7ERS_w1PXGjkZPlMgo0gyQTkPwh3OiBwe40z4vZIRnBRf_MI7DwTlTfut8AS13lEUSemzV2HZbw\"}";
    InputStream stream = new ByteArrayInputStream(access_token.getBytes(StandardCharsets.UTF_8));
    PowerMockito.when(httpResponse.getEntity().getContent()).thenReturn (stream);
    PowerMockito.when(pbiService.getPbiServiceUrl()).thenReturn( "https://login.microsoftonline.com/");
    PowerMockito.when(pbiService.getAzureidtenantid()).thenReturn("d2d2794a-61cc-4823-9690-8e288fd554cc");
    PowerMockito.when(pbiService.getPbireportid()).thenReturn("5d3c5f2a-b062-4ddb-ab09-4224fb845a99");
    aemContext.registerService(PowerBiReportService.class, pbiService);

    }
 @Test
    public void testPowerBiReportService() {
  
        Assert.assertEquals("PBI Report ID", "5d3c5f2a-b062-4ddb-ab09-4224fb845a99",pbiService.getPbireportid());
        Assert.assertEquals("PBI Embed Token","", pbiService.getGenerateEmbedToken("5d3c5f2a-b062-4ddb-ab09-4224fb845a99"));
 }
}


