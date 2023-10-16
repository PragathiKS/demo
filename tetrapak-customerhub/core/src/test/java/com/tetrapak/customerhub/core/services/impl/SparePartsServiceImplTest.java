package com.tetrapak.customerhub.core.services.impl;

import com.tetrapak.customerhub.core.beans.spareparts.ImageLinks;
import com.tetrapak.customerhub.core.services.APIGEEService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpClientBuilder.class)
@PowerMockIgnore({ "javax.net.ssl.*", "jdk.internal.reflect.*" })
public class SparePartsServiceImplTest {

    @InjectMocks
    private SparePartsServiceImpl sparePartsServiceImpl = new SparePartsServiceImpl();

    @Mock
    private APIGEEService mockApigeeService;

    @Mock
    private HttpClientBuilder httpClientBuilder;

    @Mock
    private CloseableHttpClient httpClient;

    @Mock
    private CloseableHttpResponse httpResponse;

    @Mock
    private StatusLine statusLine;

    @Mock
    private HttpEntity httpEntity;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetImageLinksWithNoError() throws IOException {
        when(mockApigeeService.getApigeeServiceUrl()).thenReturn("mockurl");
        when(mockApigeeService.getApiMappings())
                .thenReturn(new String[] { "sparepartsmedialinks:spareparts/media/v1/links" });
        PowerMockito.mockStatic(HttpClientBuilder.class);
        PowerMockito.when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);
        PowerMockito.when(httpClientBuilder.build()).thenReturn(httpClient);
        when(httpClient.execute(any())).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_OK);
        when(httpResponse.getEntity()).thenReturn(httpEntity);
        sparePartsServiceImpl.getImageLinks("648Wx486H","2033-0008");
        Mockito.verify(httpResponse).getEntity();
    }

    @Test
    public void testGetImageLinksWithError() throws IOException {
        when(mockApigeeService.getApigeeServiceUrl()).thenReturn("mockurl");
        when(mockApigeeService.getApiMappings())
                .thenReturn(new String[] { "sparepartsmedialinks:spareparts/media/v1/links" });
        PowerMockito.mockStatic(HttpClientBuilder.class);
        PowerMockito.when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);
        PowerMockito.when(httpClientBuilder.build()).thenReturn(httpClient);
        when(httpClient.execute(any())).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        ImageLinks imageLinks = sparePartsServiceImpl.getImageLinks("648Wx486H","2033-0008");
        assertNull(imageLinks);
    }

    @Test
    public void testGetImageWithNoError() throws IOException {
        PowerMockito.mockStatic(HttpClientBuilder.class);
        PowerMockito.when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);
        PowerMockito.when(httpClientBuilder.build()).thenReturn(httpClient);
        when(httpClient.execute(any())).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_OK);
        HttpResponse httpResponse1 = sparePartsServiceImpl.getImage("https://api-mig.tetrapak.com/spareparts/media/v1/medias/sys_master/images/images/h44/haa/13150358339614/2033-0008-0-1200Wx900H-648Wx486H.jpg");
        assertNotNull(httpResponse1);
    }

    @Test
    public void testGetImageWithError() throws IOException {
        PowerMockito.mockStatic(HttpClientBuilder.class);
        PowerMockito.when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);
        PowerMockito.when(httpClientBuilder.build()).thenReturn(httpClient);
        when(httpClient.execute(any())).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        HttpResponse httpResponse1 = sparePartsServiceImpl.getImage("https://api-mig.tetrapak.com/spareparts/media/v1/medias/sys_master/images/images/h44/haa/13150358339614/2033-0008-0-1200Wx900H-648Wx486H.jpg");
        assertNull(httpResponse1);
    }
}
