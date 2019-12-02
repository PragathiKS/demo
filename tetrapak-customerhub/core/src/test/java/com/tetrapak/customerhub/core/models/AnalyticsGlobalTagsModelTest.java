package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test class for Analytics Global Tags Model
 *
 * @author Nitin Kumar
 */
public class AnalyticsGlobalTagsModelTest {

    private AnalyticsGlobalTagsModel analyticsGlobalTagsModel = null;
    private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/global/en";
    private static final String RESOURCE_ROOT = "/content/tetrapak/customerhub/global/en/dashboard/jcr:content";
    private static final String JSON = "allContent.json";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContextWithJcrMock(JSON, CONTENT_ROOT);

    @Before
    public void setup() {
        Resource resource = aemContext.currentResource(RESOURCE_ROOT);
        assert resource != null;
        aemContext.registerService(Resource.class, resource);
        analyticsGlobalTagsModel = resource.adaptTo(AnalyticsGlobalTagsModel.class);
    }

    @Test
    public void testGetStartedMessage() {
        Assert.assertEquals("Channel", "dashboard", analyticsGlobalTagsModel.getChannel());
        Assert.assertEquals("login status", "logged-in", analyticsGlobalTagsModel.getLogInStatus());
        Assert.assertEquals("page type", "dashboard", analyticsGlobalTagsModel.getPageType());
        Assert.assertEquals("sales force ID", StringUtils.EMPTY, analyticsGlobalTagsModel.getSalesForceId());
        Assert.assertEquals("site country", StringUtils.EMPTY, analyticsGlobalTagsModel.getSiteCountry());
        Assert.assertEquals("country code", StringUtils.EMPTY, analyticsGlobalTagsModel.getUserCountryCode());
        Assert.assertEquals("site name", "customerhub", analyticsGlobalTagsModel.getSiteName());
        Assert.assertEquals("encoded visitor Id", "UFIlZf/RQRGoyLls0p8LyA==", analyticsGlobalTagsModel.getVisitorId());
        Assert.assertNull("error message", analyticsGlobalTagsModel.getErrorMessage());
        Assert.assertNull("error code", analyticsGlobalTagsModel.getErrorCode());
        Assert.assertEquals("user roles", 0, analyticsGlobalTagsModel.getUserRoles().size());
        Assert.assertFalse("is subpage", analyticsGlobalTagsModel.isSubPage());
    }

}
