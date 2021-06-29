package com.trs.core.services;

public interface TrsConfigurationService {

    String getTrsSitesRootFolderName();

    String getTrsAssetsRootFolderName();

    String getTrsVideoPageTemplatePath();

    String getTrsEmptyPageTemplatePath();

    String getTrsDNS();

    String getTrsAuthorDNS();

    String getTrsXylemeMappingsPath();

    String getDynamicMediaDomain();

    String getXylemeTagsPropertyName();

    String getXylemeAEMTagMappingFilePath();
    
    String getPageCreationLogFilePath();
    
    String getTaxonomyTransformationLogFilePath();
    
    String getMlvIdJCRPropName();
    
    String getAssetMetadataAPIBasePath();
    
    long getAssetMetadataAPILanguageFolderLevel();
    
    String getAssetMetadataAPIScopePath();

}
