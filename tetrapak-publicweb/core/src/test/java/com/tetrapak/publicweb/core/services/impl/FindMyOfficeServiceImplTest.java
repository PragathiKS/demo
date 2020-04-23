package com.tetrapak.publicweb.core.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.InvalidTagFormatException;
import com.tetrapak.publicweb.core.services.impl.FindMyOfficeServiceImpl.FindMyOfficeServiceConfig;

import io.wcm.testing.mock.aem.junit.AemContext;

import io.wcm.testing.mock.aem.junit.AemContext;

public class FindMyOfficeServiceImplTest {
    private static final String TEST_COUNTRY_CONTENT = "/contentFragments/country.json";
    private static final String TEST_OFFICE_CONTENT = "/contentFragments/office.json";
    private static final String TEST_COUNTRY_ROOT = "/content/dam/tetrapak/findMyOffice/contentFragments/countries";

    private static final String TEST_OFFICE_ROOT = "/content/dam/tetrapak/findMyOffice/contentFragments/offices";

    private FindMyOfficeServiceImpl findMyOfficeServiceImpl = new FindMyOfficeServiceImpl();

    @Rule
    public final AemContext aemContext = new AemContext(ResourceResolverType.JCR_MOCK);
    
    private Resource resource;

    
    @Mock
    ResourceResolver resolver;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Map<String, Object> _config = new HashMap<>();
        _config.put("getCountriesContentFragmentRootPath",
                "/content/dam/tetrapak/findMyOffice/contentFragments/countries");
        _config.put("getOfficesContentFragmentRootPath", "/content/dam/tetrapak/findMyOffice/contentFragments/offices");
        _config.put("getGoogleAPIKey", "AIzaSyC1w2gKCuwiRCsgqBR9RnSbWNuFvI5lryQ");
        aemContext.registerInjectActivateService(findMyOfficeServiceImpl, _config);
        aemContext.load().json(TEST_COUNTRY_CONTENT, TEST_COUNTRY_ROOT);
        aemContext.load().json(TEST_OFFICE_CONTENT, TEST_OFFICE_ROOT);

    }
    
    @Test
    public void testData() {
        Assert.assertEquals("AIzaSyC1w2gKCuwiRCsgqBR9RnSbWNuFvI5lryQ",findMyOfficeServiceImpl.getGoogleApiKey());
        Assert.assertNotNull(findMyOfficeServiceImpl.getFindMyOfficeData(resolver));
    }

}
