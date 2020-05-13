package com.tetrapak.publicweb.core.services.impl;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tetrapak.publicweb.core.services.impl.AssetImportServiceImpl.AssetImportServiceConfig;

import io.wcm.testing.mock.aem.junit.AemContext;

public class AssetImportServiceImplTest {

    @Mock
    private AssetImportServiceConfig config;

	private final String invalidSourceUrl = "https://media.productxplorer.tetrapak.com/highshearmixerr370-independently_controlled_twin-screw_pump.jpg";
	private final String sourceUrl = "https://media.productxplorer.tetrapak.com/production/global/features/images/highshearmixerr370-independently_controlled_twin-screw_pump.jpg";
    private final String octetStreamSourceUrl = "https://media.productxplorer.tetrapak.com/production/packaging/equipment/benefitsimages/a1_tfa_benefits_new.png";

	/** The site search servlet. */
	@InjectMocks
	private final AssetImportServiceImpl assetImportServiceImpl = new AssetImportServiceImpl();

	@Rule
	public final AemContext aemContext = new AemContext();

	@Before
	public void setup() {
        MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetAssetDetailfromInputStream() {
		Assert.assertEquals("highshearmixerr370-independently_controlled_twin-screw_pump.jpg",assetImportServiceImpl.getAssetDetailfromInputStream(sourceUrl).getFileName());
		Assert.assertEquals("image/jpeg",assetImportServiceImpl.getAssetDetailfromInputStream(sourceUrl).getContentType());
	}

    @Test
    public void testOctetStream() {
        final String[] contentTypeMapping = { "image/png=png" };
        when(config.getContentTypeMapping()).thenReturn(contentTypeMapping);
        Assert.assertEquals("image/png",
                assetImportServiceImpl.getAssetDetailfromInputStream(octetStreamSourceUrl).getContentType());
    }

	@Test
	public void testNotValidFile() {
		Assert.assertEquals(null,assetImportServiceImpl.getAssetDetailfromInputStream(invalidSourceUrl));
	}

}
