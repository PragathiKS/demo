package com.tetrapak.publicweb.core.servlets;

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

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class SiteSearchServletTest.
 */
public class SiteSearchServletTest {
	
	/** The context. */
	@Rule
	public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

	/** The request. */
	@Mock
	SlingHttpServletRequest request;

	/** The response. */
	@Mock
	SlingHttpServletResponse response;
	
	/** The resolver. */
	@Mock
	ResourceResolver resolver;

	/** The session. */
	@Mock
	Session session;
	
	/** The query builder. */
	@Mock
	private QueryBuilder queryBuilder;
	
	/** The result. */
	@Mock
	SearchResult result;
	
	/** The query. */
	@Mock
	private Query query; 

	/** The resource. */
	@Mock
	Resource resource;
	
	/** The config. */
	@Mock
	SiteSearchServlet.Config config;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		
		when(String.valueOf(config.search_rootpath())).thenReturn(SEARCH_ROOT_PATH);
		when(String.valueOf(config.fulltext_searchterm())).thenReturn(FULLTEXT_SEARCH_TERM);
	}

	/** The resolver factory. */
	@Mock
	private ResourceResolverFactory resolverFactory;

	/** The resource resolver. */
	@Mock
	private ResourceResolver resourceResolver;
	
	/** The site search servlet. */
	@InjectMocks
	private SiteSearchServlet siteSearchServlet;
	
	/** The search root path. */
	private String SEARCH_ROOT_PATH;
	
	/** The map. */
	Map<String, String> map = new HashMap<>();
	
    /** The fulltext search term. */
    private String FULLTEXT_SEARCH_TERM;

	/**
	 * Test do get tag manager N ull.
	 *
	 * @throws InvalidTagFormatException the invalid tag format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testDoGetTagManagerNUll() throws IOException {
		when(request.getResourceResolver()).thenReturn(resourceResolver);
		when(resourceResolver.adaptTo(Session.class)).thenReturn(session);
		when(resourceResolver.adaptTo(QueryBuilder.class)).thenReturn(queryBuilder);

		when(request.getParameter(FULLTEXT_SEARCH_TERM)).thenReturn("search%20term");
		when(request.getParameter(SEARCH_ROOT_PATH)).thenReturn("/content");
		Map<String, String> map = new HashMap<>();
		map.put("group.p.or", "true");
		map.put("group.1_property", JcrConstants.JCR_TITLE);
		map.put("group.1_property.value", "true");
		map.put("group.1_property.operation", "exists");
		map.put("group.2_property", "title");
		map.put("group.2_property.value", "true");
		map.put("group.2_property.operation", "exists");
		when(queryBuilder.createQuery(Mockito.any(PredicateGroup.class), Mockito.any(Session.class))).thenReturn(query);
		when(query.getResult()).thenReturn(result);
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
		siteSearchServlet.doGet(request, (SlingHttpServletResponse) response);
		siteSearchServlet.activate(config);
		response = (SlingHttpServletResponse) response;
		assertNotNull( response.getStatus());
	}

}
