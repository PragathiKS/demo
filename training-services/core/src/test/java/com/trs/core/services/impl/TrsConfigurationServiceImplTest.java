package com.trs.core.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class TrsConfigurationServiceImplTest {

	public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);
	
	private TrsConfigurationServiceImpl trsConfigurationServiceImpl;
	
	private final String TRS_SITES_ROOT_NAME = "trs" ;
	
	private final String TRS_ASSETS_ROOT_NAME =  "training-services" ;
	
	private final String TRS_VIDEO_PAGE_TEMPLATE_PATH =  "/conf/trs/settings/wcm/templates/video-page" ;
	
	private final String TRS_EMPTY_PAGE_TEMPLATE_PATH =  "/conf/trs/settings/wcm/templates/empty-page" ;
	
	private final String TRS_DNS =  "http://anytimelearning.tetrapak.com" ;
	
	private final String TRS_AUTHOR_DNS =  "https://author-tetrapak-prod65.adobecqms.net/editor.html" ;
	
	private final String TRS_XYLEME_MAPPINGS_PATH =  "/content/trs/xyleme-tag-mapping" ;
	
	private final String DM_DOMAIN =  "https://s7g10.scene7.com" ;
	
	private final String XYLEME_TAGS_ASSET_METADATA_PROPERTY =  "dam:xylemeTags" ;
	
	private final String XYLEME_AEM_TAG_MAPPING_FILE_PATH =  "/content/dam/training-services/system/mapping.json" ;
	
	@BeforeEach
	void setUp() throws Exception {
		Map<String, Object> props = new HashMap<>();
        props.put("trs.sites.root.name", "trs");
        props.put("trs.assets.root.name", TRS_ASSETS_ROOT_NAME);
        props.put("trs.video.page.template.path", TRS_VIDEO_PAGE_TEMPLATE_PATH);
        props.put("trs.empty.page.template.path", TRS_EMPTY_PAGE_TEMPLATE_PATH);
        props.put("trs.dns", TRS_DNS);
        props.put("trs.author.dns", TRS_AUTHOR_DNS);        
        props.put("trs.xyleme.mappings.path", TRS_XYLEME_MAPPINGS_PATH);
        props.put("dm.domain", DM_DOMAIN);
        props.put("xyleme.tags.asset.metadata.property", XYLEME_TAGS_ASSET_METADATA_PROPERTY);
        props.put("xyleme.aem.tag.mapping.file.path", XYLEME_AEM_TAG_MAPPING_FILE_PATH);
		trsConfigurationServiceImpl = context.registerInjectActivateService(new TrsConfigurationServiceImpl(),props); 
	}

	@Test
	final void testGetTrsSitesRootFolderName() {
		assertEquals(TRS_SITES_ROOT_NAME, trsConfigurationServiceImpl.getTrsSitesRootFolderName(),"Unexpected configuration value");
	}

	@Test
	final void testGetTrsVideoPageTemplatePath() {
		assertEquals(TRS_VIDEO_PAGE_TEMPLATE_PATH, trsConfigurationServiceImpl.getTrsVideoPageTemplatePath(),"Unexpected configuration value");
	}

	@Test
	final void testGetTrsAssetsRootFolderName() {
		assertEquals(TRS_ASSETS_ROOT_NAME, trsConfigurationServiceImpl.getTrsAssetsRootFolderName(),"Unexpected configuration value");
	}

	@Test
	final void testGetTrsEmptyPageTemplatePath() {
		assertEquals(TRS_EMPTY_PAGE_TEMPLATE_PATH, trsConfigurationServiceImpl.getTrsEmptyPageTemplatePath(),"Unexpected configuration value");
	}

	@Test
	final void testGetTrsDNS() {
		assertEquals(TRS_DNS, trsConfigurationServiceImpl.getTrsDNS(),"Unexpected configuration value");
	}

	@Test
	final void testGetTrsAuthorDNS() {
		assertEquals(TRS_AUTHOR_DNS, trsConfigurationServiceImpl.getTrsAuthorDNS(),"Unexpected configuration value");
	}

	@Test
	final void testGetTrsXylemeMappingsPath() {
		assertEquals(TRS_XYLEME_MAPPINGS_PATH, trsConfigurationServiceImpl.getTrsXylemeMappingsPath(),"Unexpected configuration value");
	}

	@Test
	final void testGetDynamicMediaDomain() {
		assertEquals(DM_DOMAIN, trsConfigurationServiceImpl.getDynamicMediaDomain(),"Unexpected configuration value");
	}

	@Test
	final void testGetXylemeTagsPropertyName() {
		assertEquals(XYLEME_TAGS_ASSET_METADATA_PROPERTY, trsConfigurationServiceImpl.getXylemeTagsPropertyName(),"Unexpected configuration value");
	}

	@Test
	final void testGetXylemeAEMTagMappingFilePath() {
		assertEquals(XYLEME_AEM_TAG_MAPPING_FILE_PATH, trsConfigurationServiceImpl.getXylemeAEMTagMappingFilePath(),"Unexpected configuration value");
	}

}
