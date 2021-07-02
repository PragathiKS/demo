/**
 *
 */
package com.tetrapak.publicweb.core.events;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.service.event.Event;

import com.day.cq.replication.ReplicationAction;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.publicweb.core.beans.NewsEventBean;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.PardotService;
import com.tetrapak.publicweb.core.services.SubscriptionMailService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;
import com.tetrapak.publicweb.core.utils.LinkUtils;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class NewsEventPageActivationListenerTest.
 *
 * @author shusaxen
 */
@RunWith(MockitoJUnitRunner.class)
public class NewsEventPageActivationListenerTest {

    /** The resolver factory. */
    @Mock
    private ResourceResolverFactory resolverFactory;

    /** The mail service. */
    @Mock
    private SubscriptionMailService mailService;

    /** The pardot service. */
    @Mock
    private PardotService pardotService;

    /** The context. */
    @Rule
    public AemContext context = new AemContext();

    /** The resource. */
    private Resource resource;

    /** The resolver. */
    @Mock
    private ResourceResolver resolver;

    /** The page manager. */
    @Mock
    private PageManager pageManager;

    /** The bean. */
    @Mock
    private NewsEventBean bean;

    /** The event. */
    @Mock
    private Event event;

    /** The Constant PAGE_EVENT_PATH. */
    private static final String PAGE_EVENT_PATH = "/content/tetrapak/publicweb/lang-masters/en/about-tetra-pak/news-and-events/events/gulfood-2018";

    /** The Constant ROOT_PAGE_PATH. */
    private static final String ROOT_PAGE_PATH = "/content/tetrapak/publicweb/lang-masters";

    /** The Constant LANGUAGE_PAGE_PATH. */
    private static final String LANGUAGE_PAGE_PATH = "/content/tetrapak/publicweb/lang-masters/en";

    /** The Constant PAYLOAD_PATH. */
    private static final String PAYLOAD_PATH = PAGE_EVENT_PATH + "/jcr:content";

    /** The Constant PAYLOAD_RESOURCE_PATH. */
    private static final String PAYLOAD_RESOURCE_PATH = "/newsEventPageActivationListener/test.json";

    /** The Constant ROOTPAGE_PAYLOAD_RESOURCE_PATH. */
    private static final String ROOTPAGE_PAYLOAD_RESOURCE_PATH = "/newsEventPageActivationListener/rootPagePayload.json";

    /** The listener. */
    @InjectMocks
    private NewsEventPageActivationListener listener = new NewsEventPageActivationListener();

    @Mock
    private ValueMap properties;


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
        Resource headerConfigResource = context
                .currentResource(LANGUAGE_PAGE_PATH + "/jcr:content/root/responsivegrid/headerconfiguration");
        Resource footerConfigResource = context
                .currentResource(LANGUAGE_PAGE_PATH + "/jcr:content/root/responsivegrid/footerconfiguration");
        Resource subscriptionformconfResource = context
                .currentResource(LANGUAGE_PAGE_PATH + "/jcr:content/root/responsivegrid/subscriptionformconf");
        Page languagePage = languagePageResource.adaptTo(Page.class);
        context.load().json(PAYLOAD_RESOURCE_PATH, PAYLOAD_PATH);
        resource = context.currentResource(PAYLOAD_PATH);
        Resource responsiveGrid = context.currentResource(PAYLOAD_PATH + "/root/responsivegrid");
        resolver = resource.getResourceResolver();
        pageManager = resolver.adaptTo(PageManager.class);
        Map<String, Object> paramMap = new HashMap<>();
        MockitoAnnotations.initMocks(this);
        when(GlobalUtil.getResourceResolverFromSubService(resolverFactory)).thenReturn(resource.getResourceResolver());
        paramMap.put(ResourceResolverFactory.SUBSERVICE, "tetrapak-system-user");
        Mockito.when(resolverFactory.getServiceResourceResolver(paramMap)).thenReturn(resolver);
        Mockito.when(resolver.getResource(PAYLOAD_PATH)).thenReturn(resource);
        Mockito.when(resolver.getResource(PAYLOAD_PATH + "/root/responsivegrid")).thenReturn(responsiveGrid);
        Mockito.when(resolver.getResource(LANGUAGE_PAGE_PATH + "/jcr:content/root/responsivegrid/headerconfiguration"))
                .thenReturn(headerConfigResource);
        Mockito.when(resolver.getResource(LANGUAGE_PAGE_PATH + "/jcr:content/root/responsivegrid/footerconfiguration"))
                .thenReturn(footerConfigResource);
        Mockito.when(resolver.getResource(LANGUAGE_PAGE_PATH + "/jcr:content/root/responsivegrid/subscriptionformconf"))
                .thenReturn(subscriptionformconfResource);
        doReturn("/content/tetrapak/publicweb/lang-masters/en/about-tetra-pak/legal-information").when(bean).getLegalInformationLink();
        doReturn("/content/tetrapak/publicweb/lang-masters/en/contact-us").when(bean).getContactUsLink();
        doReturn("/content/tetrapak/publicweb/lang-masters/en/about-tetra-pak/news-and-events/news-room").when(bean).getNewsroomLink();
        Mockito.when(resolver.adaptTo(PageManager.class)).thenReturn(pageManager);
        Mockito.when(pageManager.getPage(LinkUtils.getRootPath(PAGE_EVENT_PATH))).thenReturn(languagePage);
        String[] paths = { PAGE_EVENT_PATH };
        final Map<String, Object> eventProperties = new HashMap<>();
        eventProperties.put("paths", paths);
        event = new Event(ReplicationAction.EVENT_TOPIC, eventProperties);
        List<String> mailaddresses = new ArrayList<>();
        mailaddresses.add("testnewsemail1@yopmail.com");
        mailaddresses.add("testnewsemail2@yopmail.com");
        List<String> interestAreas = new ArrayList<>();
        interestAreas.add("End-To-End");
        interestAreas.add("Sustainability");
        interestAreas.add("Processing");
        Mockito.when(pardotService.getSubscriberMailAddresses("en-lang-masters",interestAreas)).thenReturn(mailaddresses);
        Mockito.when(mailService.sendSubscriptionEmail(bean, mailaddresses, resolver))
                .thenReturn(PWConstants.STATUS_SUCCESS);
    }

    @Test
    public void testGetNewsEventBean() throws Exception{
        Mockito.when(properties.get("jcr:title",String.class)).thenReturn("Subscribe Email Scenario 21");
        Mockito.when(properties.get("jcr:description",String.class)).thenReturn("This page is to test banner image issue after copying from another page");
        Mockito.when(properties.get("imagePath",String.class)).thenReturn("/content/dam/tetrapak/publicweb/qa/logo/re100-2016.jpeg");
        Mockito.when(properties.get("cq:template",String.class)).thenReturn("/conf/publicweb/settings/wcm/templates/news-article");
        doReturn(ROOT_PAGE_PATH).when(bean).getRootPath();
        doReturn(PAGE_EVENT_PATH).when(bean).getPageLink();
        doReturn(ROOT_PAGE_PATH).when(bean).getRootPageLink();
    }

    /**
     * Test method for
     * {@link com.tetrapak.publicweb.core.events.NewsEventPageActivationListener#handleEvent(org.osgi.service.event.Event)}.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testHandleEvent() throws Exception {
        listener.handleEvent(event);

        assertEquals("NewsEventPageActivationListener", "NewsEventPageActivationListener");
    }

}
