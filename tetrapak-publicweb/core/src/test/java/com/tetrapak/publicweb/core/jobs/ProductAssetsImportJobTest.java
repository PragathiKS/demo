package com.tetrapak.publicweb.core.jobs;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer.JobResult;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.day.cq.dam.api.AssetManager;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.tetrapak.publicweb.core.beans.pxp.AssetDetail;
import com.tetrapak.publicweb.core.services.AssetImportService;

import io.wcm.testing.mock.aem.junit.AemContext;

public class ProductAssetsImportJobTest {

	/** The resolver factory. */
	@Mock
	private ResourceResolverFactory resolverFactory;

	/** The resource resolver. */
	@Mock
	private ResourceResolver resourceResolver;
	
	@Mock
	private Replicator replicator;
	
	@Mock
	private Resource existingResource;
	
	@Mock
	private AssetManager assetManager;

	@Mock
	private AssetImportService assetImportService;
	
	@Mock
	private Session session;
	
	/** The resource resolver. */
	@Mock
	private Job job;

	/** The site search servlet. */
	@InjectMocks
	private ProductAssetsImportJob assetImportjob = new ProductAssetsImportJob();

	@Rule
	public final AemContext aemContext = new AemContext();

	private String sourceUrl = "https://myimageurl/myimage.jpg";
	private String existingDAMPath  = "/content/dam/tetrapak/pxp/category4/product3/images/myimage.jpg";
	private String nonExistingDAMPath = "/content/dam/tetrapak/pxp/category1/product1/images/myimage.jpg";
	private AssetDetail assetDetail = new AssetDetail();
	@Mock
	private Throwable replicationException; 
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		
	}

	@Test
	public void testProcess() {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put(ResourceResolverFactory.SUBSERVICE, "tetrapak-system-user");
		try {
			when(resolverFactory.getServiceResourceResolver(paramMap)).thenReturn(resourceResolver);
			when(resourceResolver.adaptTo(Session.class)).thenReturn(session);
			when(job.getProperty("sourceurl")).thenReturn(sourceUrl);
			when(job.getProperty("finalDAMPath")).thenReturn(nonExistingDAMPath);
			when(resourceResolver.getResource(nonExistingDAMPath)).thenReturn(null);
			when(resourceResolver.adaptTo(AssetManager.class)).thenReturn(assetManager);
			when(assetImportService.getAssetDetailfromInputStream(sourceUrl)).thenReturn(assetDetail);
			when(session.isLive()).thenReturn(true);
		} catch (LoginException e) {

		}
		assertEquals(JobResult.OK, assetImportjob.process(job));
	}

	@Test
	public void testSessionExpired() {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put(ResourceResolverFactory.SUBSERVICE, "tetrapak-system-user");
		try {
			when(resolverFactory.getServiceResourceResolver(paramMap)).thenReturn(resourceResolver);
			when(resourceResolver.adaptTo(Session.class)).thenReturn(session);
			when(job.getProperty("sourceurl")).thenReturn(sourceUrl);
			when(job.getProperty("finalDAMPath")).thenReturn(nonExistingDAMPath);
			when(resourceResolver.getResource(nonExistingDAMPath)).thenReturn(null);
			when(resourceResolver.adaptTo(AssetManager.class)).thenReturn(assetManager);
			when(assetImportService.getAssetDetailfromInputStream(sourceUrl)).thenReturn(assetDetail);
			when(session.isLive()).thenReturn(false);
		} catch (LoginException e) {
		}
		assertEquals(JobResult.FAILED, assetImportjob.process(job));	
	}
	
	@Test
	public void testSessionNull() {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put(ResourceResolverFactory.SUBSERVICE, "tetrapak-system-user");
		try {
			when(resolverFactory.getServiceResourceResolver(paramMap)).thenReturn(resourceResolver);
			when(resourceResolver.adaptTo(Session.class)).thenReturn(null);
		} catch (LoginException e) {
		}
		assertEquals(JobResult.FAILED, assetImportjob.process(job));	
	}
	
	
	@Test
	public void testNullAssetManager() {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put(ResourceResolverFactory.SUBSERVICE, "tetrapak-system-user");
		try {
			when(resolverFactory.getServiceResourceResolver(paramMap)).thenReturn(resourceResolver);
			when(resourceResolver.adaptTo(Session.class)).thenReturn(session);
			when(job.getProperty("sourceurl")).thenReturn("https://myimageurl/myimage.jpg");
			when(job.getProperty("finalDAMPath")).thenReturn(nonExistingDAMPath);
			when(resourceResolver.getResource(nonExistingDAMPath)).thenReturn(null);
			when(resourceResolver.adaptTo(AssetManager.class)).thenReturn(null);

		} catch (LoginException e) {

		}
		assertEquals(JobResult.FAILED, assetImportjob.process(job));
	}
	
	@Test
	public void testNullResourceResolver() {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put(ResourceResolverFactory.SUBSERVICE, "tetrapak-system-user");
		try {
			when(resolverFactory.getServiceResourceResolver(paramMap)).thenReturn(null);
		} catch (LoginException e) {

		}
		assertEquals(JobResult.FAILED, assetImportjob.process(job));
	}
	
	@Test
	public void testAssetAlreadyExists() {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put(ResourceResolverFactory.SUBSERVICE, "tetrapak-system-user");
		try {
			when(resolverFactory.getServiceResourceResolver(paramMap)).thenReturn(resourceResolver);
			when(resourceResolver.adaptTo(Session.class)).thenReturn(session);
			when(job.getProperty("sourceurl")).thenReturn("https://myimageurl/myimage.jpg");
			when(job.getProperty("finalDAMPath")).thenReturn(existingDAMPath);		
			when(resourceResolver.getResource(existingDAMPath)).thenReturn(existingResource);
		} catch (LoginException e) {
		}
		assertEquals(JobResult.CANCEL, assetImportjob.process(job));
	}
	
}
