package com.tetralaval.models;


import com.tetralaval.constants.TLConstants;
import com.tetralaval.services.DynamicMediaService;
import com.tetralaval.utils.GlobalUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * class for dynamic image model.
 *
 */
@Model(adaptables = { SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DynamicImageModel {

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The image path. */
    @RequestAttribute
    private String imagePath;

    /** The dheight. */
    @RequestAttribute
    private String dheight;

    /** The dwidth. */
    @RequestAttribute
    private String dwidth;

    /** The mheightl. */
    @RequestAttribute
    private String mheightl;

    /** The mwidthl. */
    @RequestAttribute
    private String mwidthl;

    /** The mheightp. */
    @RequestAttribute
    private String mheightp;

    /** The mwidthp. */
    @RequestAttribute
    private String mwidthp;

    /** The image crop. */
    @RequestAttribute
    private String imageCrop;

    /** The component name. */
    @RequestAttribute
    private String componentName;

    /** The img background. */
    @RequestAttribute
    private String imgBackground;

    /**
     * The asset alt text.
     */
    @RequestAttribute
    private String altText;

    /** The video path. */
    @RequestAttribute
    private String videoPath;

    /** The final path. */
    @Inject
    private String finalPath;

    /**
     * The configuration service.
     */
    @OSGiService
    private DynamicMediaService dynamicMediaService;

    /**
     * The desktop url.
     */
    private String desktopUrl;

    /**
     * The desktop large url.
     */
    private String desktopLargeUrl;

    /**
     * The mobile url.
     */
    private String mobilePortraitUrl;

    /**
     * The mobile landscape url.
     */
    private String mobileLandscapeUrl;

    /**
     * The default image url.
     */
    private String defaultImageUrl;

    /**
     * The has configuration.
     */
    private boolean hasConfiguraton;

    /**
     * The Constant FMT_JPG.
     */
    private static final String FMT_JPG = "fmt=jpg";

    /** The Constant ADDITIONAL_PARAMETER. */
    private static final String ADDITIONAL_PARAMETER = "qlt=85,0";

    /** The Constant OP_USM. */
    private static final String OP_USM = "op_usm=1.75,0.3,2,0";

    /** The Constant IMG_BGC_GRAY. */
    private static final String IMG_BGC_GRAY = "bg-gray";

    /** The Constant BGC_GRAY. */
    private static final String BGC_GRAY = "bgc=246,246,246";

    /**
     * The Constant IMAGE_SHARPNESS.
     */
    private static final String IMAGE_SHARPNESS = "resMode=sharp2";

    /**
     * The Constant HEIGHT.
     */
    private static final String HEIGHT = "hei";

    /**
     * The Constant AMPERSAND.
     */
    private static final String AMPERSAND = "&";

    /**
     * The Constant EQUALS.
     */
    private static final String EQUALS = "=";

    /**
     * The Constant WIDTH.
     */
    private static final String WIDTH = "wid";

    /**
     * The Constant QUERY_PARAMETER.
     */
    private static final String QUERY_PARAMETER = "?";

    /**
     * The Constant HYPHEN.
     */
    private static final String HYPHEN = "-";

    /**
     * The Constant CROPN.
     */
    private static final String CROPN = "cropn";

    /**
     * Post construct.
     */
    @PostConstruct
    protected void postConstruct() {
        String dynamicMediaUrl = getImageServiceURL();
        Resource imageRes = request.getResourceResolver().getResource(imagePath + "/jcr:content/metadata");
        final String currentComponentName = getComponentName(request.getResource());
        if (Objects.nonNull(imageRes)) {
            final ValueMap vMap = imageRes.getValueMap();
            String fileFormat = vMap.get("dam:Fileformat", StringUtils.EMPTY);
            String scene7Type = vMap.get("dam:scene7Type", StringUtils.EMPTY);
            if (fileFormat.equalsIgnoreCase("GIF") && !scene7Type.equalsIgnoreCase("Image")) {
                dynamicMediaUrl = getVideoServiceUrl();
            }
        }

        if(StringUtils.isEmpty(imagePath) && videoPath != null && (currentComponentName.equalsIgnoreCase(TLConstants.TEXT_VIDEO)
                || currentComponentName.equalsIgnoreCase(TLConstants.VIDEO))) {
            imagePath = videoPath;
        }

        if (imagePath != null) {
            finalPath = TLConstants.SLASH + GlobalUtil.getScene7FileName(request.getResourceResolver(), imagePath);
        }

        if (null != dynamicMediaUrl) {
            dynamicMediaUrl = StringUtils.removeEndIgnoreCase(dynamicMediaUrl, TLConstants.SLASH) + finalPath;
        }

        if (StringUtils.isNotBlank(dynamicMediaUrl)) {
            setDesktopUrl(createDynamicMediaUrl(TLConstants.DESKTOP, dynamicMediaUrl));
            setDesktopLargeUrl(createDynamicMediaUrl(TLConstants.DESKTOP_LARGE, dynamicMediaUrl));
            setMobilePortraitUrl(createDynamicMediaUrl(TLConstants.MOBILEPORTRAIT, dynamicMediaUrl));
            setMobileLandscapeUrl(createDynamicMediaUrl(TLConstants.MOBILELANDSCAPE, dynamicMediaUrl));
        }
        setDefaultImage();
    }

    /**
     * Append transparency.
     *
     * @param url
     *            the url
     * @param appendingString
     *            the appending string
     * @return the string builder
     */
    private static StringBuilder appendTransparency(final StringBuilder url, final String appendingString) {
        return url.append(appendingString).append(FMT_JPG);
    }

    /**
     * Append Sharpness.
     *
     * @param url
     *            the url
     * @param appendingString
     *            the appending string
     * @return the string builder
     */
    private static StringBuilder appendSharpness(final StringBuilder url, final String appendingString) {
        return url.append(appendingString).append(IMAGE_SHARPNESS);
    }

    /**
     * Additional parameter. Added this parameter to reduce the size of the img, as recommended by Adobe
     *
     * @param url
     *            the url
     * @param queryFlag
     *            the query flag
     * @return the string builder
     */
    private static StringBuilder additionalParameter(StringBuilder url, boolean queryFlag) {
        if (queryFlag) {
            url = url.append(AMPERSAND).append(ADDITIONAL_PARAMETER);
        } else {
            url = url.append(QUERY_PARAMETER).append(AMPERSAND).append(ADDITIONAL_PARAMETER);
        }
        return url;
    }

    /**
     * Append opsm. Added this parameter to reduce the size of the img, as recommended by Adobe
     *
     * @param url
     *            the url
     * @param queryFlag
     *            the query flag
     * @return the string builder
     */
    private static StringBuilder appendOpsm(StringBuilder url, boolean queryFlag) {
        if (queryFlag) {
            url = url.append(AMPERSAND).append(OP_USM);
        } else {
            url = url.append(QUERY_PARAMETER).append(AMPERSAND).append(OP_USM);
        }
        return url;
    }

    /**
     * Append background color.
     *
     * @param url
     *            the url
     * @param appendingString
     *            the appending string
     * @return the string builder
     */
    private static StringBuilder appendBackgroundColor(final StringBuilder url, final String appendingString) {
        return url.append(appendingString).append(BGC_GRAY);
    }

    /**
     * Creates the url.
     *
     * @param paramUrl
     *            the url
     * @param imageConfiguration
     *            the image configuration
     * @return the string
     */
    private String createUrl(final String paramUrl, final String imageConfiguration) {
        final int widthIndex = StringUtils.ordinalIndexOf(imageConfiguration, ",", 1);
        final int heightIndex = StringUtils.ordinalIndexOf(imageConfiguration, ",", 2);
        String width;
        String height = null;
        String crop = null;
        String url = paramUrl;
        if (widthIndex > -1) {
            width = imageConfiguration.substring(0, widthIndex);

            if (heightIndex > -1) {
                height = imageConfiguration.substring(widthIndex + 1, heightIndex);
                crop = imageConfiguration.substring(heightIndex + 1);
            } else {
                height = imageConfiguration.substring(widthIndex + 1);
            }
        } else {
            width = imageConfiguration;
        }

        if (StringUtils.isNotEmpty(width) || StringUtils.isNotEmpty(height) || StringUtils.isNotEmpty(crop)) {
            url = getUrl(paramUrl, width, height, crop, imgBackground);
        }
        return url;
    }

    /**
     * Gets the cropping from mobile.
     *
     * @return the cropping from mobile
     */
    private String getCroppingFromMobile() {
        final Resource imageResource = request.getResourceResolver().getResource(imagePath + "/jcr:content/metadata");
        if (null == imageResource) {
            return StringUtils.EMPTY;
        }
        final ValueMap vMap = imageResource.getValueMap();
        final Long height = (Long) vMap.get("tiff:ImageLength");
        final Long width = (Long) vMap.get("tiff:ImageWidth");
        return getCropParameterForScene7(height, width);
    }

    /**
     * Gets the crop parameter for scene 7.
     *
     * @param height
     *            the height
     * @param width
     *            the width
     * @return the crop parameter for scene 7
     */
    private String getCropParameterForScene7(Long height, Long width) {
        if (null == imageCrop) {
            return StringUtils.EMPTY;
        }
        if (width > 1280) {
            width = (long) 1280;
            height = (long) 468;
        }
        final String[] cropArray = imageCrop.split(",");
        final Double topW = Double.valueOf(cropArray[0]);
        final Double topH = Double.valueOf(cropArray[1]);
        final Double lowW = Double.valueOf(cropArray[2]);
        final Double lowH = Double.valueOf(cropArray[3]);

        final double normTopW = topW / width;
        final double normTopH = topH / height;
        final double normWidth = (lowW - topW) / width;
        final double normHeight = (lowH - topH) / height;

        return normTopW + "," + normTopH + "," + normWidth + "," + normHeight;
    }

    /**
     * Gets the component name.
     *
     * @param resource
     *            the resource
     * @return the component name
     */
    private String getComponentName(final Resource resource) {
        if (StringUtils.isNotBlank(componentName)) {
            return componentName;
        } else {
            return StringUtils.substringAfterLast(resource.getResourceType(), TLConstants.SLASH);
        }
    }

    /**
     * Gets the url.
     *
     * @param paramUrl
     *            the param url
     * @param width
     *            the width
     * @param height
     *            the height
     * @param crop
     *            the crop
     * @param imgBackground
     *            the img background
     * @return the url
     */
    private static String getUrl(final String paramUrl, final String width, final String height, final String crop,
                                 final String imgBackground) {
        boolean queryFlag = false;
        StringBuilder url = new StringBuilder(paramUrl);
        if (StringUtils.isNotEmpty(width)) {
            url.append(QUERY_PARAMETER).append(WIDTH).append(EQUALS).append(width);
            queryFlag = true;
        }
        if (StringUtils.isNotEmpty(height)) {
            url = addHeight(height, queryFlag, url);
            queryFlag = true;
        }
        if (StringUtils.isNotEmpty(crop)) {
            url = addCrop(crop, queryFlag, url);
            queryFlag = true;
        }

        if (StringUtils.isEmpty(imgBackground)) {
            url = checkImgBackground(queryFlag, url);
            queryFlag = true;
        } else if (IMG_BGC_GRAY.equalsIgnoreCase(imgBackground)) {
            url = checkBackgroundColor(queryFlag, url);
            queryFlag = true;
        }

        if (queryFlag) {
            url = appendSharpness(url, AMPERSAND);
        } else {
            url = appendSharpness(url, QUERY_PARAMETER);
        }

        url = additionalParameter(url, queryFlag);
        url = appendOpsm(url, queryFlag);
        return url.toString();
    }

    /**
     * Check background color.
     *
     * @param queryFlag
     *            the query flag
     * @param url
     *            the url
     * @return the string builder
     */
    private static StringBuilder checkBackgroundColor(boolean queryFlag, StringBuilder url) {
        if (queryFlag) {
            url = appendBackgroundColor(url, AMPERSAND);
        } else {
            url = appendBackgroundColor(url, QUERY_PARAMETER);
        }
        return url;
    }

    /**
     * Check img background.
     *
     * @param queryFlag
     *            the query flag
     * @param url
     *            the url
     * @return the string builder
     */
    private static StringBuilder checkImgBackground(boolean queryFlag, StringBuilder url) {
        if (queryFlag) {
            url = appendTransparency(url, AMPERSAND);
        } else {
            url = appendTransparency(url, QUERY_PARAMETER);
        }
        return url;
    }

    /**
     * Adds the crop.
     *
     * @param crop
     *            the crop
     * @param queryFlag
     *            the query flag
     * @param url
     *            the url
     * @return the string builder
     */
    private static StringBuilder addCrop(final String crop, boolean queryFlag, StringBuilder url) {
        if (queryFlag) {
            url = url.append(AMPERSAND).append(CROPN).append(EQUALS).append(crop);
        } else {
            url = url.append(QUERY_PARAMETER).append(CROPN).append(EQUALS).append(crop);
        }
        return url;
    }

    /**
     * Adds the height.
     *
     * @param height
     *            the height
     * @param queryFlag
     *            the query flag
     * @param url
     *            the url
     * @return the string builder
     */
    private static StringBuilder addHeight(final String height, boolean queryFlag, StringBuilder url) {
        if (queryFlag) {
            url = url.append(AMPERSAND).append(HEIGHT).append(EQUALS).append(height);
        } else {
            url = url.append(QUERY_PARAMETER).append(HEIGHT).append(EQUALS).append(height);
        }
        return url;
    }

    /**
     * Creates the URL for the devices(Desktop,IPad,Mobile).
     *
     * @param deviceType
     *            the device type
     * @param imagePath
     *            the image path
     * @return the string
     */
    public String createDynamicMediaUrl(final String deviceType, final String imagePath) {
        String url = "";
        final Map<String, String> dynamicMediaConfiguration = getMap(getDynamicMediaConfiguration());
        final String finalComponentName = getComponentName(request.getResource());
        if (StringUtils.isNotBlank(finalComponentName)) {
            final StringBuilder key = new StringBuilder(finalComponentName).append(HYPHEN).append(deviceType);

            final String imageConfiguration = getImageConfigurations(deviceType, dynamicMediaConfiguration, key);

            if (StringUtils.isNotEmpty(imageConfiguration)) {
                url = createUrl(imagePath, imageConfiguration);
                hasConfiguraton = true;
            } else {
                url = imagePath + "?scl=1";
            }
        }
        return url;
    }

    /**
     * Gets the image configurations.
     *
     * @param deviceType
     *            the device type
     * @param dynamicMediaConfiguration
     *            the dynamic media configuration
     * @param key
     *            the key
     * @return the image configurations
     */
    private String getImageConfigurations(final String deviceType, final Map<String, String> dynamicMediaConfiguration,
                                          final StringBuilder key) {
        String imageConfiguration;
        final String cropping = getCroppingFromMobile();
        if (deviceType.equals(TLConstants.DESKTOP) && StringUtils.isNotBlank(dwidth) && StringUtils.isNotBlank(dheight)) {
            imageConfiguration = dwidth + "," + dheight;
        } else if (deviceType.equals(TLConstants.MOBILELANDSCAPE)) {
            imageConfiguration = getImageConfigurationForMobile(dynamicMediaConfiguration, key, cropping, mwidthl,
                    mheightl);
        } else if (deviceType.equals(TLConstants.MOBILEPORTRAIT)) {
            imageConfiguration = getImageConfigurationForMobile(dynamicMediaConfiguration, key, cropping, mwidthp,
                    mheightp);
        } else {
            imageConfiguration = dynamicMediaConfiguration.get(key.toString());
        }
        return imageConfiguration;
    }

    /**
     * Gets the image configuration for mobile.
     *
     * @param dynamicMediaConfiguration
     *            the dynamic media configuration
     * @param key
     *            the key
     * @param cropping
     *            the cropping
     * @param mWidth
     *            the m width
     * @param mHeight
     *            the m height
     * @return the image configuration for mobile
     */
    private String getImageConfigurationForMobile(final Map<String, String> dynamicMediaConfiguration,
                                                  final StringBuilder key, final String cropping, final String mWidth, final String mHeight) {
        if (StringUtils.isNotEmpty(mWidth) && StringUtils.isNotEmpty(mHeight)) {
            return mWidth + "," + mHeight + "," + cropping;
        } else if (StringUtils.isNotEmpty(cropping)) {
            return ",," + cropping;
        }
        return dynamicMediaConfiguration.get(key.toString());
    }

    /**
     * Gets the map.
     *
     * @param dynamicMediaConfiguration
     *            the dynamic media configuration
     * @return the map
     */
    private Map<String, String> getMap(final String[] dynamicMediaConfiguration) {
        final Map<String, String> map = new HashMap<>();
        for (final String propValue : dynamicMediaConfiguration) {
            map.put(StringUtils.substringBefore(propValue, "="), StringUtils.substringAfter(propValue, "="));
        }
        return map;
    }

    /**
     * Gets the alt text.
     *
     * @return the alt text
     */
    public String getAltText() {
        if (StringUtils.isNotEmpty(altText)) {
            return altText;
        } else {
            return StringUtils.EMPTY;
        }
    }

    /**
     * Gets the image path.
     *
     * @return the image path
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Gets the default image url.
     *
     * @return the default image url
     */
    public String getDefaultImageUrl() {
        return defaultImageUrl;
    }

    /**
     * Gets the desktop url.
     *
     * @return the desktop url
     */
    public String getDesktopUrl() {
        return desktopUrl;
    }

    /**
     * Gets the dynamic media configuration.
     *
     * @return the dynamic media configuration
     */
    public String[] getDynamicMediaConfiguration() {
        return dynamicMediaService.getDynamicMediaConfMap();
    }

    /**
     * Gets the image service URL.
     *
     * @return the image service URL
     */
    public String getImageServiceURL() {
        return dynamicMediaService.getImageServiceUrl();
    }

    /**
     * Gets the video service url.
     *
     * @return the video service url
     */
    public String getVideoServiceUrl() {
        return dynamicMediaService.getVideoServiceUrl();
    }

    /**
     * Gets the mobile landscape url.
     *
     * @return the mobile landscape url
     */
    public String getMobileLandscapeUrl() {
        return mobileLandscapeUrl;
    }

    /**
     * Gets the mobile portrait url.
     *
     * @return the mobile portrait url
     */
    public String getMobilePortraitUrl() {
        return mobilePortraitUrl;
    }

    /**
     * Gets the img background.
     *
     * @return the img background
     */
    public String getImgBackground() {
        return imgBackground;
    }

    /**
     * Sets the default image.
     */
    public void setDefaultImage() {
        if (hasConfiguraton) {
            setDefaultImageUrl("/content/dam/customerhub/cow-blue-background.png");
        }
    }

    /**
     * Sets the default image url.
     *
     * @param defaultImageUrl
     *            the new default image url
     */
    public void setDefaultImageUrl(final String defaultImageUrl) {
        this.defaultImageUrl = defaultImageUrl;
    }

    /**
     * Sets the desktop url.
     *
     * @param desktopUrl
     *            the new desktop url
     */
    public void setDesktopUrl(final String desktopUrl) {
        this.desktopUrl = desktopUrl;
    }

    /**
     * Gets the desktop large url.
     *
     * @return the desktop large url
     */
    public String getDesktopLargeUrl() {
        return desktopLargeUrl;
    }

    /**
     * Sets the desktop large url.
     *
     * @param desktopLargeUrl
     *            the new desktop large url
     */
    public void setDesktopLargeUrl(final String desktopLargeUrl) {
        this.desktopLargeUrl = desktopLargeUrl;
    }

    /**
     * Sets the mobile landscape url.
     *
     * @param mobileLandscapeUrl
     *            the new mobile landscape url
     */
    public void setMobileLandscapeUrl(final String mobileLandscapeUrl) {
        this.mobileLandscapeUrl = mobileLandscapeUrl;
    }

    /**
     * Sets the mobile portrait url.
     *
     * @param mobilePortraitUrl
     *            the new mobile portrait url
     */
    public void setMobilePortraitUrl(final String mobilePortraitUrl) {
        this.mobilePortraitUrl = mobilePortraitUrl;
    }

    /**
     * Gets the final path.
     *
     * @return the final path
     */
    public String getFinalPath() {
        return finalPath;
    }

}
