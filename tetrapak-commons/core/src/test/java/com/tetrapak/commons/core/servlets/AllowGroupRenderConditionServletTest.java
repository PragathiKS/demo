package com.tetrapak.commons.core.servlets;

import com.adobe.granite.ui.components.rendercondition.SimpleRenderCondition;
import com.tetrapak.commons.core.mock.MockHelper;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.tetrapak.commons.core.constants.CommonsConstants.ADMIN_USER;
import static com.tetrapak.commons.core.constants.CommonsConstants.USER_GROUP_READ_SERVICE;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class AllowGroupRenderConditionServletTest {

    @Rule
    public AemContext context = new AemContext();

    AllowGroupRenderConditionServlet allowGroupRenderConditionServlet = new AllowGroupRenderConditionServlet();

    AllowGroupRenderConditionServlet renderConditionServlet = new AllowGroupRenderConditionServlet();

    @Mock
    private ResourceResolverFactory resolverFactory;

    @Mock
    private ResourceResolver resourceResolver;

    @Mock
    private ResourceResolver requestResolver;

    @Mock
    private SlingHttpServletRequest request;

    @Captor
    ArgumentCaptor<SimpleRenderCondition> captor;

    @Mock
    private UserManager userManager;

    @Mock
    private Authorizable group;

    @Mock
    private Group authorGroup;

    @Mock
    private Authorizable currentUser;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private static final String PAGE_COMPONENT_JSON_RESOURCE= "/publicwebpage.json";

    private static final String PAGE_COMPONENT_PATH= "/apps/publicweb/components/structure/pages/page";

    private static final String RENDER_CONDITION_RESOURCE = "/apps/publicweb/components/structure/pages/page/cq:dialog/content/items/tabs/items/basic/items/column/items/title/items/hideinnav/granite:rendercondition";



    @Before
    public void setUp() throws Exception {
        Map<String, Object> config = new HashMap<>();
        context.load().json(PAGE_COMPONENT_JSON_RESOURCE, PAGE_COMPONENT_PATH);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testWithAdmin() throws Exception {
        Resource componentResource = context.currentResource(RENDER_CONDITION_RESOURCE);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, USER_GROUP_READ_SERVICE);
        when(resolverFactory.getServiceResourceResolver(paramMap)).thenReturn(resourceResolver);
        allowGroupRenderConditionServlet = MockHelper.getServlet(context, AllowGroupRenderConditionServlet.class);
        when(request.getResource()).thenReturn(componentResource);
        when(request.getResourceResolver()).thenReturn(requestResolver);
        when(requestResolver.getUserID()).thenReturn(ADMIN_USER);
        allowGroupRenderConditionServlet.doGet(request, context.response());
        ArgumentCaptor<SimpleRenderCondition> argument = ArgumentCaptor.forClass(SimpleRenderCondition.class);
        Mockito.verify(request).setAttribute(anyString(),captor.capture());
        SimpleRenderCondition simpleRenderCondition = (SimpleRenderCondition)captor.getValue();
        assertTrue(simpleRenderCondition.check());
    }

    @Test
    public void testWithNonAllowedGroup() throws Exception {
        Resource componentResource = context.currentResource(RENDER_CONDITION_RESOURCE);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, USER_GROUP_READ_SERVICE);
        when(resolverFactory.getServiceResourceResolver(paramMap)).thenReturn(resourceResolver);
        when(request.getResource()).thenReturn(componentResource);
        when(request.getResourceResolver()).thenReturn(requestResolver);
        when(requestResolver.getUserID()).thenReturn("author");
        context.registerAdapter(ResourceResolver.class,UserManager.class,userManager);
        when(userManager.getAuthorizable("administrators")).thenReturn(group);
        when(group.isGroup()).thenReturn(true);
        when(userManager.getAuthorizable("author")).thenReturn(currentUser);
        ArrayList<Group> groups = new ArrayList<>();
        groups.add(authorGroup);
        when(currentUser.memberOf()).thenReturn(groups.iterator());
        when(authorGroup.getID()).thenReturn("content-authors");
        allowGroupRenderConditionServlet = MockHelper.getServlet(context, AllowGroupRenderConditionServlet.class);
        allowGroupRenderConditionServlet.doGet(request, context.response());
        ArgumentCaptor<SimpleRenderCondition> argument = ArgumentCaptor.forClass(SimpleRenderCondition.class);
        Mockito.verify(request).setAttribute(anyString(),captor.capture());
        SimpleRenderCondition simpleRenderCondition = (SimpleRenderCondition)captor.getValue();
        assertTrue(!simpleRenderCondition.check());
    }

    @Test
    public void testWithAllowedGroup() throws Exception {
        Resource componentResource = context.currentResource(RENDER_CONDITION_RESOURCE);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, USER_GROUP_READ_SERVICE);
        when(resolverFactory.getServiceResourceResolver(paramMap)).thenReturn(resourceResolver);
        when(request.getResource()).thenReturn(componentResource);
        when(request.getResourceResolver()).thenReturn(requestResolver);
        when(requestResolver.getUserID()).thenReturn("sebrahmanays");
        context.registerAdapter(ResourceResolver.class,UserManager.class,userManager);
        when(userManager.getAuthorizable("administrators")).thenReturn(group);
        when(group.isGroup()).thenReturn(true);
        when(userManager.getAuthorizable("sebrahmanays")).thenReturn(currentUser);
        ArrayList<Group> groups = new ArrayList<>();
        groups.add(authorGroup);
        when(currentUser.memberOf()).thenReturn(groups.iterator());
        when(authorGroup.getID()).thenReturn("administrators");
        allowGroupRenderConditionServlet = MockHelper.getServlet(context, AllowGroupRenderConditionServlet.class);
        allowGroupRenderConditionServlet.doGet(request, context.response());
        ArgumentCaptor<SimpleRenderCondition> argument = ArgumentCaptor.forClass(SimpleRenderCondition.class);
        Mockito.verify(request).setAttribute(anyString(),captor.capture());
        SimpleRenderCondition simpleRenderCondition = (SimpleRenderCondition)captor.getValue();
        assertTrue(simpleRenderCondition.check());
    }

    @Test
    public void testExceptionHandling() throws Exception {
        Resource componentResource = context.currentResource(RENDER_CONDITION_RESOURCE);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, USER_GROUP_READ_SERVICE);
        when(resolverFactory.getServiceResourceResolver(paramMap)).thenReturn(resourceResolver);
        when(request.getResource()).thenReturn(componentResource);
        when(request.getResourceResolver()).thenReturn(requestResolver);
        when(requestResolver.getUserID()).thenReturn("author");
        context.registerAdapter(ResourceResolver.class,UserManager.class,userManager);
        when(userManager.getAuthorizable("administrators")).thenThrow(new RepositoryException("Repository Exception"));
        renderConditionServlet = MockHelper.getServlet(context, AllowGroupRenderConditionServlet.class);
        renderConditionServlet.doGet(request, context.response());
        Mockito.spy(Logger.class).error("Error while accessing repository Repository Exception");
    }
}
