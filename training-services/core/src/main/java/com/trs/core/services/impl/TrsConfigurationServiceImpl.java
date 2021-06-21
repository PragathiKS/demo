package com.trs.core.services.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;

import com.trs.core.configuration.TrsConfiguration;
import com.trs.core.services.TrsConfigurationService;

@Component(immediate = true, service = TrsConfigurationService.class, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(ocd = TrsConfiguration.class)
public class TrsConfigurationServiceImpl implements TrsConfigurationService {

    private TrsConfiguration config;

    @Activate
    public void activate(TrsConfiguration config) {

        this.config = config;
    }

    @Override
    public String getTrsSitesRootFolderName() {
        return config.trs_sites_root_name();
    }

    @Override
    public String getTrsVideoPageTemplatePath() {
        return config.trs_video_page_template_path();
    }

    @Override
    public String getTrsAssetsRootFolderName() {
        return config.trs_assets_root_name();
    }

    @Override
    public String getTrsEmptyPageTemplatePath() {
        return config.trs_empty_page_template_path();
    }

    @Override
    public String getTrsDNS() {
        return config.trs_dns();
    }

    @Override
    public String getTrsAuthorDNS() {
        return config.trs_author_dns();
    }

    @Override
    public String getTrsXylemeMappingsPath() {
        return config.trs_xyleme_mappings_path();
    }

    @Override
    public String getDynamicMediaDomain() {
        return config.dm_domain();
    }

    @Override
    public String getXylemeTagsPropertyName() {
        return config.xyleme_tags_asset_metadata_property();
    }

    @Override
    public String getXylemeAEMTagMappingFilePath() {
        return config.xyleme_aem_tag_mapping_file_path();
    }
    
    @Override
    public String getPageCreationLogFilePath() {
        return config.page_creation_log_file_path();
    }

    @Override
    public String getTaxonomyTransformationLogFilePath() {
        return config.taxonomy_transformation_log_file_path();
    }

    @Override
    public String getMlvIdJCRPropName() {
        return config.mlvid_property();
    }

    @Override
    public String getAssetMetadataAPIBasePath() {
        return config.asset_metadata_api_base_path();
    }

}
