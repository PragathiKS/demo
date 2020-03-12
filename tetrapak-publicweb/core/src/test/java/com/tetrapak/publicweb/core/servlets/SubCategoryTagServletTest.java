package com.tetrapak.publicweb.core.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import javax.jcr.Session;
import javax.servlet.http.HttpServletResponse;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.day.cq.tagging.InvalidTagFormatException;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * @author shisriva4
 *
 */
public class SubCategoryTagServletTest {

	@Rule
	public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

	@Mock
	SlingHttpServletRequest request;

	@Mock
	SlingHttpServletResponse response;
	@Mock
	ResourceResolver resolver;

	@Mock
	Session session;

	@Mock
	Resource resource;
	@Mock
	SubCategoryTagServlet.Config config;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		when(config.category_tag()).thenReturn(categoryTag);
		when(String.valueOf(config.category_tag())).thenReturn(categoryTag);
	}

	@Mock
	private ResourceResolverFactory resolverFactory;

	@Mock
	private ResourceResolver resourceResolver;
	@Mock
	TagManager tagManager;
	@Mock
	Iterator<Tag> subCategoryTags;

	private String categoryTag;
	@InjectMocks
	private SubCategoryTagServlet subCategoryTagServlet;

	@Test
	public void testDoGetTagManagerNUll() throws InvalidTagFormatException, IOException {
		Tag tagtest = context.create().tag("test1");
		context.create().tag("test1/1");
		context.create().tag("test1/2");
		context.create().tag("test1/3");
		when(request.getResourceResolver()).thenReturn(resourceResolver);
		when(resourceResolver.adaptTo(TagManager.class)).thenReturn(tagManager);
		when(request.getParameter(categoryTag)).thenReturn(categoryTag);
		when(tagManager.resolve(categoryTag)).thenReturn(tagtest);
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
		subCategoryTagServlet.doGet(request, (SlingHttpServletResponse) response);
		response = (SlingHttpServletResponse) response;
		assertNotNull( response.getStatus());
	}

	@Test
	public void testActivate() {
		subCategoryTagServlet.activate(config);
		assertNull(categoryTag);
	}

}
