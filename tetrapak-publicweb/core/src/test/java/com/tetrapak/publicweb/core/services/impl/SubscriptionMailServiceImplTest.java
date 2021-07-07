package com.tetrapak.publicweb.core.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale;

import com.adobe.acs.commons.i18n.I18nProvider;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import com.tetrapak.publicweb.core.utils.PageUtil;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.event.jobs.JobManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.tetrapak.publicweb.core.beans.NewsEventBean;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.mock.MockDynamicMediaServiceImpl;
import com.tetrapak.publicweb.core.services.DynamicMediaService;
import com.tetrapak.publicweb.core.services.SubscriptionMailService;

import io.wcm.testing.mock.aem.junit.AemContext;
import junitx.util.PrivateAccessor;
import org.mockito.internal.configuration.injection.MockInjection;
import org.mockito.stubbing.OngoingStubbing;
import org.osgi.service.component.annotations.Reference;

/**
 * The Class SubscriptionMailServiceImplTest.
 */
public class SubscriptionMailServiceImplTest {

    /** The context. */
    @Rule
    public AemContext context = new AemContext();

    /** The bean. */
    @Mock
    private NewsEventBean bean;

    /** The job manager. */
    @Mock
    private JobManager jobManager;

    /** The media service. */
    private DynamicMediaService mediaService;

    private  PageUtil pageUtil;

    @Mock
    private Page page;

    /** The page manager. */
    @Mock
    private PageManager pageManager;

    /** The resource. */
    private Resource resource;

    /** The resolver. */
    private ResourceResolver resolver;

    /** I18nProvider. */
    @Mock
    private I18nProvider i18nProvider;

    /** The mail service. */
    private final SubscriptionMailService mailService = new SubscriptionMailServiceImpl();

    private static final Locale TEST_LOCALE = Locale.ENGLISH;

    /** The Constant LANGUAGE_PAGE_PATH. */
    private static final String LANGUAGE_PAGE_PATH = "/content/tetrapak/publicweb/lang-masters/en";

    /** The Constant PAGE_EVENT_PATH. */
    private static final String PAGE_EVENT_PATH = "/content/tetrapak/publicweb/lang-masters/en/about-tetra-pak/news-and-events/events/gulfood-2018";

    /** The Constant PAYLOAD_PATH. */
    private static final String PAYLOAD_PATH = PAGE_EVENT_PATH + "/jcr:content";

    /** The Constant PAYLOAD_RESOURCE_PATH. */
    private static final String PAYLOAD_RESOURCE_PATH = "/newsEventPageActivationListener/test.json";

    /** The Constant ROOT_PAGE_PATH. */
    private static final String ROOT_PAGE_PATH = "/content/tetrapak/publicweb/lang-masters";


    /** The Constant ROOTPAGE_PAYLOAD_RESOURCE_PATH. */
    private static final String ROOTPAGE_PAYLOAD_RESOURCE_PATH = "/newsEventPageActivationListener/rootPagePayload.json";

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
        context.load().json(ROOTPAGE_PAYLOAD_RESOURCE_PATH, ROOT_PAGE_PATH);
        Resource languagePageResource = context.currentResource(LANGUAGE_PAGE_PATH);
        mediaService = new MockDynamicMediaServiceImpl();
        context.registerService(DynamicMediaService.class, mediaService);
        context.load().json(PAYLOAD_RESOURCE_PATH, PAYLOAD_PATH);
        resource = context.currentResource(PAYLOAD_PATH);
        resolver = resource.getResourceResolver();
        pageManager = resolver.adaptTo(PageManager.class);
        Page page = languagePageResource.adaptTo(Page.class);
        MockitoAnnotations.initMocks(this);
        Mockito.when(bean.getRootPath()).thenReturn("/content/tetrapak/publicweb/lang-masters/en");
        Mockito.when(pageManager.getPage(bean.getRootPath())).thenReturn(page);

        Mockito.when(bean.getTitle()).thenReturn("Test Title");
        Mockito.when(bean.getDescription()).thenReturn("Test Desription");
        Mockito.when(bean.getImagePath()).thenReturn("/content/dam/tetrapak/publicweb/gb/en/about/light-head-upscaled.jpg");
        Mockito.when(bean.getTemplateType()).thenReturn("Press Release");
        String logoImagePath = "/content/dam/tetrapak/publicweb/en-za/tetra-pak-two-liner.png";
        Mockito.when(bean.getHeaderLogo()).thenReturn(logoImagePath);
        Mockito.when(bean.getFooterLogo()).thenReturn(logoImagePath);
        Mockito.when(bean.getFooterLogoBackground()).thenReturn("2,63,136");
        Mockito.when(bean.getNewsroomLink())
                .thenReturn("https://www-qa.tetrapak.com/en-za/about-tetra-pak/news-and-events/news-room");
        Mockito.when(bean.getLegalInformationLink())
                .thenReturn("https://www-qa.tetrapak.com/en-za/about-tetra-pak/legal-information");
        Mockito.when(bean.getContactUsLink()).thenReturn("https://www-qa.tetrapak.com/en-za/contact-us");
        Mockito.when(bean.getLanguage()).thenReturn("en");
        Mockito.when(bean.getRootPageLink()).thenReturn("https://www-qa.tetrapak.com/en-za");

        resolver = context.resourceResolver();
        Mockito.when(bean.getPageLink()).thenReturn(
                "https://www-qa.tetrapak.com/en-za/about-tetra-pak/news-and-events/subscribe-email-scenario-11/susbcriber-email-test-101");
        context.registerService(SubscriptionMailService.class, mailService);
        PrivateAccessor.setField(context.getService(SubscriptionMailService.class), "mediaService",
                context.getService(DynamicMediaService.class));
        PrivateAccessor.setField(context.getService(SubscriptionMailService.class), "jobMgr", jobManager);
        PrivateAccessor.setField(context.getService(SubscriptionMailService.class),"i18nProvider", i18nProvider);

    }

    /**
     * Test send subscription email.
     */
    @Test
    public void testSendSubscriptionEmail() {
        List<String> mailAddresses = new ArrayList<>();
        mailAddresses.add("testmail1@yopmail.com");
        Assert.assertEquals(
                context.getService(SubscriptionMailService.class).sendSubscriptionEmail(bean, mailAddresses, resolver),
                PWConstants.STATUS_SUCCESS);
    }


}
