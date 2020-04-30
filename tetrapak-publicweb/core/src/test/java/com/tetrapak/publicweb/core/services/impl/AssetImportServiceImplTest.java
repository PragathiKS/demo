package com.tetrapak.publicweb.core.services.impl;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import io.wcm.testing.mock.aem.junit.AemContext;

public class AssetImportServiceImplTest {

	/** The resolver factory. */
	@Mock
	private ResourceResolverFactory resolverFactory;

	/** The resource resolver. */
	@Mock
	private ResourceResolver resourceResolver;
	
	private String invalidSourceUrl = "https://media.productxplorer.tetrapak.com/highshearmixerr370-independently_controlled_twin-screw_pump.jpg";
	private String sourceUrl = "https://media.productxplorer.tetrapak.com/production/global/features/images/highshearmixerr370-independently_controlled_twin-screw_pump.jpg";
	
	/** The site search servlet. */
	@InjectMocks
	private AssetImportServiceImpl assetImportServiceImpl = new AssetImportServiceImpl();
	
	@Rule
	public final AemContext aemContext = new AemContext();

	@Before
	public void setup() {

	}
	
	@Test
	public void testGetAssetDetailfromInputStream() {
		Assert.assertEquals("highshearmixerr370-independently_controlled_twin-screw_pump.jpg",assetImportServiceImpl.getAssetDetailfromInputStream(sourceUrl).getFileName());
		Assert.assertEquals("image/jpeg",assetImportServiceImpl.getAssetDetailfromInputStream(sourceUrl).getContentType());
	}
	
	@Test
	public void testNotValidFile() {
		Assert.assertEquals(null,assetImportServiceImpl.getAssetDetailfromInputStream(invalidSourceUrl));
	}
	
	@Test
	public void testIOException() {
		assetImportServiceImpl.getAssetDetailfromInputStream("");
	}

}
