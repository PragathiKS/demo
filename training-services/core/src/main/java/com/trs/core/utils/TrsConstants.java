package com.trs.core.utils;

public final class TrsConstants {
    
    public static final String FORWARD_SLASH = "/";
    public static final String DAM = "dam";
    public static final String ASSET_TITLE_PROPERTY = "dc:title";
    public static final String ASSET_DESCRIPTION_PROPERTY = "dc:description";
    public static final String PAGE_SCENE7_FILENAME_PROPERTY = "s7assetFile";
    public static final String ASSET_SCENE7_FILENAME_PROPERTY = "dam:scene7FileAvs";
    public static final String ASSET_SCENE7_FILENAME_FALLBACK_PROPERTY = "dam:scene7File";
    public static final String ASSET_ACTIVATE_PAGE_PROPERTY = "dc:activatePage";
    public static final String ASSET_SYNC_PROPS = "dc:syncProperties";
    public static final String ASSET_METADATA_RELATIVE_PATH = "/jcr:content/metadata";
    public static final String XYLEME_TAGS_ASSET_PROPERTY = "xylemeTags";
    public static final String CQ_TAGS_PROPERTY = "cq:tags";
    public static final String METADATA_NODE_RELATIVE_PATH = "jcr:content/metadata";
    public static final String JCR_CONTENT = "jcr:content";
    public static final String ASSET_RENDITION_SUFFIX = "/jcr:content/renditions/original/jcr:content";
    public static final String COMMA = ",";
    public static final String HYPHEN = "-";
    public static final String PIPE = "|";
    public static final String XYLEME_TAG_MAPPING_NODE_PROPERTY = "aemTagPath";
    public static final String SOURCE_ASSET_PATH = "sourceAssetPath";
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    
    private TrsConstants() {
        throw new IllegalStateException("Utility class");
    }

}
