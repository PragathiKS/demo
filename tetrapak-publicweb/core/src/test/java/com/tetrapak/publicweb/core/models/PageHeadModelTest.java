package com.tetrapak.publicweb.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class PageHeadModelTest {

    /** The Inject Model Class. */
    @InjectMocks
    private PageHeadModel objectUnderTest = spy(new PageHeadModel());

    /** The Baidu Map Key. */
    private static final String BAIDU_MAP_KEY = "abcd1234utrx";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test model, resource and all getters of the Page Head model.
     *
     * @throws Exception the exception
     */
    @Test
    public void simpleLoadAndGettersTest() throws Exception {
        when(objectUnderTest.getBaiduMapkey()).thenReturn(BAIDU_MAP_KEY);
    }

}
