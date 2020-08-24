package com.tetrapak.publicweb.core.constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class PWConstants.
 */
public final class PWConstants {

    /** The Constant ENGLISH_LANGUAGE_ISO_CODE. */
    public static final String ENGLISH_LANGUAGE_ISO_CODE = "en";

    /** The Constant GB_COUNTRY_ISO_CODE. */
    public static final String GB_ISO_CODE = "gb";

    /** The Constant LANGUAGE_LEVEL. */
    public static final int LANGUAGE_PAGE_LEVEL = 4;

    /** The Constant COUNTRY_LEVEL. */
    public static final int COUNTRY_PAGE_LEVEL = 3;

    /** The Constant MARKET_ROOT_PAGE_LEVEL. */
    public static final int MARKET_ROOT_PAGE_LEVEL = 2;

    /** The Constant HOME_PAGE_REL_PATH. */
    public static final String HOME_PAGE_REL_PATH = "home";

    /** The Constant LANG_MASTERS. */
    public static final String LANG_MASTERS= "lang-masters";

    /** The Constant OTHERS_SECTION_MENU_PAGE_LEVEL. */
    public static final int OTHERS_SECTION_MENU_PAGE_LEVEL = 5;

    /** The Constant SOLUTIONS_SECTION_MENU_PAGE_LEVEL. */
    public static final int SOLUTIONS_SECTION_MENU_PAGE_LEVEL = 6;

    /** The Constant SLASH. */
    public static final String SLASH = "/";

    /** The Constant ROOT_PATH. */
    public static final String ROOT_PATH = "/var/commerce/products";

    /** The Constant PXP_ROOT_PATH. */
    public static final String PXP_ROOT_PATH = "/var/commerce/products/pxp";

    /** The Constant CONTENT_ROOT_PATH. */
    public static final String CONTENT_ROOT_PATH = "/content/tetrapak/publicweb";

    /** The Constant GLOBLA_LANG_PATH. */
    public static final String GLOBLA_MARKET_PATH = "/content/tetrapak/publicweb/gb";

    /** The Constant PXP. */
    public static final String PXP = "pxp";

    /** The Constant SLING_FOLDER. */
    public static final String SLING_FOLDER = "sling:Folder";

    /** The Constant NT_UNSTRUCTURED. */
    public static final String NT_UNSTRUCTURED = "nt:unstructured";

    /** The Constant JCR_PRIMARY_TYPE. */
    public static final String JCR_PRIMARY_TYPE = "jcr:primaryType";

    /** The Constant CQ_TEMPLATE. */
    public static final String CQ_TEMPLATE = "cq:template";

    /** The Constant CQ_REDIRECT_TARGET. */
    public static final String CQ_REDIRECT_TARGET = "cq:redirectTarget";

    /** The Constant PRODUCT_ID. */
    public static final String PRODUCT_ID = "productId";

    /** The Constant HEADER. */
    public static final String HEADER = "header";

    /** The Constant ID. */
    public static final String ID = "id";

    /** The Constant NAME. */
    public static final String NAME = "name";

    /** The Constant BODY. */
    public static final String BODY = "body";

    /** The Constant THUMBNAIL. */
    public static final String THUMBNAIL = "thumbnail";

    /** The Constant BENEFITS. */
    public static final String BENEFITS = "benefits";

    /** The Constant BENEFITS_IMAGE. */
    public static final String BENEFITS_IMAGE = "benefitsimage";

    /** The Constant IMAGE. */
    public static final String IMAGE = "image";

    /** The Constant ENABLE_JS_API. */
    public static final String ENABLE_JS_API = "enablejsapi=1";

    /** The Constant COMMERCE_PROVIDER. */
    public static final String COMMERCE_PROVIDER = "cq:commerceProvider";

    /** The Constant SRC. */
    public static final String SRC = "src";

    /** The Constant POSTER. */
    public static final String POSTER = "poster";

    /** The Constant FEATURES. */
    public static final String FEATURES = "features";

    /** The Constant OPTIONS. */
    public static final String OPTIONS = "options";

    /** The Constant VIDEO. */
    public static final String VIDEO = "video";
    
    /** The Constant DOCUMENT. */
    public static final String DOCUMENT = "document";

    /** The Constant VOLUMES. */
    public static final String VOLUMES = "volumes";

    /** The filling machines. */
    public static final String FILLING_MACHINE = "fillingmachines";

    /** The processing equipments. */
    public static final String PROCESSING_EQUIPEMENT = "processingequipments";

    /** The package types. */
    public static final String PACKAGE_TYPE = "packagetypes";

    /** The FULL FEED Scheduler ID. */
    public static final String FULL_FEED_SCHEDULER_ID = "pwpxpfullfeedschedulerID@tetrapak";

    /** The DELTA FEED Scheduler ID. */
    public static final String DELTA_FEED_SCHEDULER_ID = "pwpxpdeltafeedschedulerID@tetrapak";

    /** The feed files uri. */
    public static final String FEED_FILES_URI = "/equipment/pxpparameters/files/";

    /** The filling machine template. */
    public static final String FILLING_MACHINE_TEMPLATE = "/conf/publicweb/settings/wcm/templates/public-web-filling-machine-page";

    /** The processing equipments template. */
    public static final String PROCESSING_EQUIP_TEMPLATE = "/conf/publicweb/settings/wcm/templates/public-web-processing-equipment-page";

    /** The package type template. */
    public static final String PACKAGE_TYPE_TEMPLATE = "/conf/publicweb/settings/wcm/templates/public-web-package-type-page";

    /** The Constant EXTERNAL_REDIRECT_TEMPLATE. */
    public static final String EXTERNAL_REDIRECT_TEMPLATE = "/conf/publicweb/settings/wcm/templates/public-web-external-redirect-page";

    /** The full feed. */
    public static final String FULL_FEED = "full";

    /** The delta feed. */
    public static final String DELTA_FEED = "delta";

    /** The default bearer token refresh time. */
    public static final int  FIFTY_MIN_IN_MILLI_SECONDS = 3_000_000;

    /** The Constant REPLICATION_PXP. */
    public static final String REPLICATION_PXP = "replicationpxp";

    /** The Constant PXP_OPENINGS_TYPE. */
    public static final String PXP_OPENINGS_TYPE = "pw.pxp.openingtype";

    /** The Constant PXP_OPENINGS_PRINCIPLE. */
    public static final String PXP_OPENINGS_PRINCIPLE = "pw.pxp.openingprinciple";

    /** The Constant PXP_OPENINGS_BENEFITS. */
    public static final String PXP_OPENINGS_BENEFITS = "pw.pxp.openingbenefits";

    /** The TARGET SAME TAB. */
    public static final String SELF_TARGET = "_self";

    public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";

    public static final String CONTACT_US_MAIL_TEMPLATE_PATH = "/etc/notification/email/publicweb/contactusemail.html";

    public static final String SEND_EMAIL_JOB_TOPIC = "com/tetrapak/publicweb/sendemail";
    
    /** The Constant SPACE. */
    public static final String SPACE = " ";
    
    /** The Constant NEWS. */
    public static final String NEWS = "news";
    
    /** The Constant EVENTS. */
    public static final String EVENTS = "events";
    
    /** The Constant CASES. */
    public static final String CASES = "cases";
    
    /** The Constant MEDIA. */	
    public static final String MEDIA = "media";
    
    /** The Constant PRODUCTS. */
    public static final String PRODUCTS = "products";
    
    /** The Constant JCR_LAST_MODIFIED. */
    public static final String JCR_LAST_MODIFIED = "jcr:lastModified";
    
    /** The Constant CQ_LAST_MODIFIED. */
    public static final String CQ_LAST_MODIFIED = "cq:lastModified";

    public static final String HTTPS_PROTOCOL = "https://";
    public static final String HTTP_PROTOCOL = "http://";
    public static final String WWW = "www.";

    public static final String HTML_EXTENSION = ".html";
    public static final String CONTENT_ROOT_FOLDER_PUBLIC_WEB = "/content/tetrapak/publicweb";
    public static final String CONTENT_ROOT_FOLDER_CUSTOMER_HUB = "/content/tetrapak/customerhub";
    /**Externalizer**/
    public static final  String EXTERNALIZER_DOMAIN_PUBLICWEB = "publicweb";
    public static final  String EXTERNALIZER_DOMAIN_CUSTOMERHUB = "customerhub";

    public static final List<String> siteRootPathConfig = Arrays.asList(
            CONTENT_ROOT_FOLDER_PUBLIC_WEB,CONTENT_ROOT_FOLDER_CUSTOMER_HUB);


    public static final Map<String, String> siteRootPathConfigForExternalizer;

    static {
        siteRootPathConfigForExternalizer = new HashMap<>();
        siteRootPathConfigForExternalizer.put(CONTENT_ROOT_FOLDER_PUBLIC_WEB,EXTERNALIZER_DOMAIN_PUBLICWEB);
        siteRootPathConfigForExternalizer.put(CONTENT_ROOT_FOLDER_CUSTOMER_HUB,EXTERNALIZER_DOMAIN_CUSTOMERHUB);

    }


    
    /**
     * Instantiates a new PW constants.
     */
    private PWConstants() {
        /*
         * adding a private constructor to hide the implicit one
         */
    }

}
