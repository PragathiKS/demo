package com.tetrapak.publicweb.core.models;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Objects;
import java.util.UUID;
import com.auth0.jwt.algorithms.Algorithm;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.interfaces.RSAPrivateKey;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import java.io.File;
import java.io.FileInputStream;
import java.io.DataInputStream;

/**
 * The Class BannerModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BannerModel {

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;
    
    /** The resource. */
    private Resource resource;

    /** The banner type. */
    @ValueMapValue
    private String bannerType;

    /** The subtitle. */
    @ValueMapValue
    private String subtitle;

    /** The heading tag. */
    @ValueMapValue
    private String headingTag;

    /** The title. */
    @ValueMapValue
    private String title;

    /** The text. */
    @ValueMapValue
    private String text;

    /** The link label. */
    @ValueMapValue
    private String linkLabel;

    /** The link path. */
    @ValueMapValue
    private String linkPath;

    /** The file reference. */
    @ValueMapValue
    private String fileReference;

    /** The alt. */
    @ValueMapValue
    private String alt;

    /** The pw display. */
    @ValueMapValue
    private String pwDisplay;

    /** The pw theme. */
    @ValueMapValue
    private String pwTheme;

    /** The pw button theme. */
    @ValueMapValue
    private String pwButtonTheme;

    /** The pw card theme. */
    @ValueMapValue
    private String pwCardTheme;

    /** The anchor id. */
    @ValueMapValue
    private String anchorId;

    /** The anchor title. */
    @ValueMapValue
    private String anchorTitle;

    /** The image crop. */
    @ValueMapValue
    private String imageCrop;

    /** The enable forms. */
    @ValueMapValue
    private String formType;

    /** The Constant FORWARD_SLASH. */
    private static final String FORWARD_SLASH = "/";
    
    /** The Constant HERO_TEST. */
    private static final String HERO = "hero";
    
    /** The Constant HERO_TEST. */
    private static final String HERO_WIDE = "hero-wide";

    /** The Constant SKY_BLUE. */
    private static final String SKY_BLUE = "sky-blue";

    /** The enable softcoversion. */
    @ValueMapValue
    private String enableSoftcoversion;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AnchorModel.class);

    /**
     * The init method.
     */
    @PostConstruct
    protected void init() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        resource = request.getResource();
        final ValueMap vMap = resource.getValueMap();
        enableSoftcoversion = vMap.get("enableSoftcoversion", StringUtils.EMPTY);
        formType = vMap.get("formType", StringUtils.EMPTY);
        UUID uuid = UUID.randomUUID();
        int variant = uuid.variant();
        int version = uuid.version();
        LOGGER.error("checking guid variant",variant);
        LOGGER.error("chcking guid version",version);
        Resource resource1 = resource.getResourceResolver().getResource("/etc/oneTrustkey/TetraPakCookieMgmtPrivateKey.pem");
       // File file  = resource1.adaptTo(File.class);
        File file1 = new File(resource1.getPath());
       // File file1 = new File(resource1.getName());
        RSAPrivateKey privateKey = (RSAPrivateKey) getPrivateKey("D:\\private_key.pem");
        String jwtToken = createJWTAndSign("SneppetsMobileApp", "admin@sneppets.com", "sneppets.com", "sneppets_device_100", "2.1.3", "ios", "student", "SneppetsMobileApp", "1234", privateKey);

    }

    private static String createJWTAndSign(String issuer, String subject, String server, String deviceid, String appversion,
                                           String os, String userType, String clientid, String pin, RSAPrivateKey privateKey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {


        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;

        JwtBuilder builder = Jwts.builder().claim("issuer", issuer)
                .claim("subject", subject)
                .claim("server", server)
                .claim("device_id", deviceid)
                .claim("app_version", appversion)
                .claim("os", os)
                .claim("user_type", userType)
                .claim("client_id", clientid)
                .claim("pin", pin)
                .signWith(privateKey,signatureAlgorithm);

        return builder.compact();

    }

    private static RSAPrivateKey getPrivateKey(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        File file = new File(filename);
        FileInputStream fis = new FileInputStream(file);
        DataInputStream dis = new DataInputStream(fis);

        byte[] keyBytes = new byte[(int) file.length()];
        dis.readFully(keyBytes);
        dis.close();
        String key = new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset());
        String privateKeyPEM = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----BEGIN PRIVATE KEY-----", "");
        byte[] encoded = Base64.decodeBase64(privateKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);


       // PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
      //  KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      //  RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(spec);

       // return privateKey;
    }

    /**
     * Gets the banner type.
     *
     * @return the banner type
     */
    public String getBannerType() {
        return bannerType;
    }

    /**
     * Gets the subtitle.
     *
     * @return the subtitle
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * Gets the heading tag.
     *
     * @return the heading tag
     */
    public String getHeadingTag() {
        return headingTag;
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Gets the link label.
     *
     * @return the link label
     */
    public String getLinkLabel() {
        return linkLabel;
    }

    /**
     * Gets the link path.
     *
     * @return the link path
     */
    public String getLinkPath() {
        return LinkUtils.sanitizeLink(linkPath, request);
    }

    /**
     * Gets the file reference.
     *
     * @return the file reference
     */
    public String getFileReference() {
        return fileReference;
    }

    /**
     * Gets the alt.
     *
     * @return the alt
     */
    public String getAlt() {
        return alt;
    }

    /**
     * Gets the pw display.
     *
     * @return the pw display
     */
    public String getPwDisplay() {
        return pwDisplay;
    }

    /**
     * Gets the pw theme.
     *
     * @return the pw theme
     */
    public String getPwTheme() {
        return pwTheme;
    }

    /**
     * Gets the pw button theme.
     *
     * @return the pw button theme
     */
    public String getPwButtonTheme() {
        return pwButtonTheme;
    }

    /**
     * Gets the pw card theme.
     *
     * @return the pw card theme
     */
    public String getPwCardTheme() {
        if ((Objects.isNull(pwCardTheme))
                && (bannerType.equalsIgnoreCase(HERO) || bannerType.equalsIgnoreCase(HERO_WIDE))) {
            pwCardTheme = SKY_BLUE;
        }
        return pwCardTheme;
    }

    /**
     * Gets the anchor id.
     *
     * @return the anchor id
     */
    public String getAnchorId() {
        return anchorId;
    }

    /**
     * Gets the anchor title.
     *
     * @return the anchor title
     */
    public String getAnchorTitle() {
        return anchorTitle;
    }

    /**
     * Gets the image crop.
     *
     * @return the image crop
     */
    public String getImageCrop() {
        return imageCrop;
    }


    /**
     * Gets the asset name.
     *
     * @return the asset name
     */
    public String getAssetName() {
        String assetName = StringUtils.EMPTY;
        if (StringUtils.isNoneEmpty(linkPath)) {
            assetName = StringUtils.substringAfterLast(linkPath, FORWARD_SLASH);
        }
        return assetName;
    }
    /**
     * Gets the enable form.
     *
     * @return the enable form
     */
    public String getFormType() {
        return formType;
    }

    /**
     * Gets the soft conversion data.
     *
     * @return the soft conversion data
     */
    public SoftConversionModel getSoftConversionData() {
        return request.adaptTo(SoftConversionModel.class);
    }


    /**
     * Gets the subscription form data.
     *
     * @return the subscription form data
     */
    public SubscriptionFormModel getSubscriptionData() {
        return request.adaptTo(SubscriptionFormModel.class);
    }

    /**
     * Gets the enable softcoversion.
     *
     * @return the enable softcoversion
     */
    public String getEnableSoftcoversion() {
        return enableSoftcoversion;
    }
}
