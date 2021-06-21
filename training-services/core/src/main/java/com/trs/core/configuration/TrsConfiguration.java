package com.trs.core.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "TrS Generic Configuration")
public @interface TrsConfiguration {

    @AttributeDefinition(name = "TrS Sites Root folder name", description = "TrS Site Root folder name",
            type = AttributeType.STRING)
    String trs_sites_root_name() default "trs/training-videos";

    @AttributeDefinition(name = "TrS Assets Root folder name", description = "TrS Assets Root folder name",
            type = AttributeType.STRING)
    String trs_assets_root_name() default "training-services";

    @AttributeDefinition(name = "TrS Video Page template path", description = "TrS Video Page template path",
            type = AttributeType.STRING)
    String trs_video_page_template_path() default "/conf/trs/settings/wcm/templates/video-page";

    @AttributeDefinition(name = "TrS Video Page template path", description = "TrS Video Page template path",
            type = AttributeType.STRING)
    String trs_empty_page_template_path() default "/conf/trs/settings/wcm/templates/empty-page";

    @AttributeDefinition(name = "TrS DNS", description = "TrS DNS", type = AttributeType.STRING)
    String trs_dns() default "http://anytimelearning.tetrapak.com";

    @AttributeDefinition(name = "TrS Author Server DNS", description = "TrS Author Server DNS",
            type = AttributeType.STRING)
    String trs_author_dns() default "https://author-tetrapak-prod65.adobecqms.net/editor.html";

    @AttributeDefinition(name = "TrS Xyleme Mappings path", description = "TrS Xyleme Mappings path",
            type = AttributeType.STRING)
    String trs_xyleme_mappings_path() default "/content/trs/xyleme-tag-mapping";

    @AttributeDefinition(name = "Dynamic Media Domain", description = "Dynamic Media Domain",
            type = AttributeType.STRING)
    String dm_domain() default "https://s7g10.scene7.com";

    @AttributeDefinition(name = "Xyleme Tags Asset Metadata Property name",
            description = "Xyleme Tags Asset Metadata Property name", type = AttributeType.STRING)
    String xyleme_tags_asset_metadata_property() default "dam:xylemeTags";

    @AttributeDefinition(name = "Xyleme Tags to AEM Tags mapping file path",
            description = "Xyleme Tags to AEM Tags mapping file path", type = AttributeType.STRING)
    String xyleme_aem_tag_mapping_file_path() default "/content/dam/training-services/system/mapping.json";

    @AttributeDefinition(name = "Xyleme Tags to AEM Tags mapping file path",
            description = "Xyleme Tags to AEM Tags mapping file path", type = AttributeType.STRING)
    String page_creation_log_file_path() default "/content/dam/training-services/system/page-creation-author-log.xlsx";

    @AttributeDefinition(name = "Xyleme Tags to AEM Tags mapping file path",
            description = "Xyleme Tags to AEM Tags mapping file path", type = AttributeType.STRING)
    String taxonomy_transformation_log_file_path() default "/content/dam/training-services/system/xyleme-tag-transformation-log.xlsx";
    
    @AttributeDefinition(name = "MLV ID JCR Property name",
            description = "MLV ID JCR Property name", type = AttributeType.STRING)
    String mlvid_property() default "dam:mlvId";
    
    @AttributeDefinition(name = "Asset Metadata API Base path",
            description = "Asset Metadata API Base path", type = AttributeType.STRING)
    String asset_metadata_api_base_path() default "/content/dam/training-services";
}
