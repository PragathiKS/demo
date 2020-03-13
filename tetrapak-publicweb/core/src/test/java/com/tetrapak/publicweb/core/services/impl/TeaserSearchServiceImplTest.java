package com.tetrapak.publicweb.core.services.impl;

import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class TeaserSearchServiceImplTest.
 */
public class TeaserSearchServiceImplTest {

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

	/** The resource. */
	@Mock
	Resource resource;
	/** The session. */
	@Mock
	Session session;

	/** The query builder. */
	@Mock
	private QueryBuilder queryBuilder;

	/** The result. */
	@Mock
	SearchResult searchResult;

	/** The query. */
	@Mock
	private Query query;

	/** The tags. */
	String[] tags = new String[] { "one", "two", "three" };

	/** The teaser search service impl. */
	private TeaserSearchServiceImpl teaserSearchServiceImpl = new TeaserSearchServiceImpl();

	/** The resource resolver. */
	@Mock
	private ResourceResolver resourceResolver;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Test.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void test() throws IOException {
		// when(request.getResourceResolver()).thenReturn(resourceResolver);
		when(resourceResolver.adaptTo(Session.class)).thenReturn(session);
		when(resourceResolver.adaptTo(QueryBuilder.class)).thenReturn(queryBuilder);
		when(queryBuilder.createQuery(Mockito.any(PredicateGroup.class), Mockito.any(Session.class))).thenReturn(query);
		when(query.getResult()).thenReturn(searchResult);
		teaserSearchServiceImpl.getListOfTeasers(resourceResolver, tags, "/content", 4);
	}

}
