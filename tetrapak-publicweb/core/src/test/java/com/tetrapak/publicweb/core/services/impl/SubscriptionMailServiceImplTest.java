package com.tetrapak.publicweb.core.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.testing.mock.osgi.MockOsgi;
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
import com.tetrapak.publicweb.core.services.DataEncryptionService;
import com.tetrapak.publicweb.core.services.DynamicMediaService;
import com.tetrapak.publicweb.core.services.SubscriptionMailService;

import io.wcm.testing.mock.aem.junit.AemContext;
import junitx.util.PrivateAccessor;

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

    /** The encryption service. */
    private DataEncryptionService encryptionService;

    /** The media service. */
    private DynamicMediaService mediaService;

    /** The resolver. */
    private ResourceResolver resolver;

    /** The mail service. */
    private final SubscriptionMailService mailService = new SubscriptionMailServiceImpl();;

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
        mediaService = new MockDynamicMediaServiceImpl();
        encryptionService = new DataEncryptionServiceImpl();
        context.registerService(DynamicMediaService.class, mediaService);
        context.registerService(DataEncryptionService.class, encryptionService);
        initializeDataEncryptionConfig();
        MockitoAnnotations.initMocks(this);
        Mockito.when(bean.getTitle()).thenReturn("Test Title");
        Mockito.when(bean.getDescription()).thenReturn("Test Desription");
        String heroImagePath = "/content/dam/tetrapak/publicweb/gb/en/about/light-head-upscaled.jpg";
        String logoImagePath = "/content/dam/tetrapak/publicweb/gb/tetra-pak-two-liner.png";
        Mockito.when(bean.getHeroImage()).thenReturn(heroImagePath);
        Mockito.when(bean.getHeaderLogo()).thenReturn(logoImagePath);
        Mockito.when(bean.getFooterLogo()).thenReturn(logoImagePath);
        Mockito.when(bean.getFooterLogoBackground()).thenReturn("2,63,136");
        Mockito.when(bean.getNewsroomLink())
                .thenReturn("https://www-qa.tetrapak.com/en-za/about-tetra-pak/news-and-events/news-room");
        Mockito.when(bean.getLegalInformationLink())
                .thenReturn("https://www-qa.tetrapak.com/en-za/about-tetra-pak/legal-information");
        Mockito.when(bean.getManagePreferenceLink())
                .thenReturn("https://www-qa.tetrapak.com/en-za/about-tetra-pak/manage-preference");
        Mockito.when(bean.getLanguage()).thenReturn("en");
        resolver = context.resourceResolver();
        Mockito.when(bean.getPageLink()).thenReturn(
                "https://www-qa.tetrapak.com/en-za/about-tetra-pak/news-and-events/subscribe-email-scenario-11/susbcriber-email-test-101");
        context.registerService(SubscriptionMailService.class, mailService);
        PrivateAccessor.setField(context.getService(SubscriptionMailService.class), "encryptionService",
                context.getService(DataEncryptionService.class));
        PrivateAccessor.setField(context.getService(SubscriptionMailService.class), "mediaService",
                context.getService(DynamicMediaService.class));
        PrivateAccessor.setField(context.getService(SubscriptionMailService.class), "jobMgr", jobManager);
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

    /**
     * Initialize data encryption config.
     */
    private void initializeDataEncryptionConfig() {
        final Map<String, Object> config = new HashMap<>();
        config.put("aesEncryptionkey", "publicwebEncryptionkey");
        MockOsgi.activate(context.getService(DataEncryptionService.class), context.bundleContext(), config);
    }

}
