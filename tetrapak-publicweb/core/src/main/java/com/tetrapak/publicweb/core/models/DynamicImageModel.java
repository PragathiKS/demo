package com.tetrapak.publicweb.core.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.tetrapak.publicweb.core.services.DynamicMediaService;

/**
 * class for dynamic image model
 *
 * @author Sandip Kumar
 */
@Model(adaptables = { SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DynamicImageModel {

    @SlingObject
    private SlingHttpServletRequest request;

    @RequestAttribute
    private String imagePath;

    @RequestAttribute
    private String dheight;

    @RequestAttribute
    private String dwidth;

    @RequestAttribute
    private String mheightl;

    @RequestAttribute
    private String mwidthl;

    @RequestAttribute
    private String mheightp;

    @RequestAttribute
    private String mwidthp;

    @RequestAttribute
    private String imageCrop;

    @RequestAttribute
    private String componentName;

    @RequestAttribute
    private String imgBackground;

    /**
     * The asset alt text.
     */
    @RequestAttribute
    private String altText;

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
     * The Constant FMT_PNG_ALPHA.
     */
    private static final String FMT_PNG_ALPHA = "fmt=png-alpha";

    private static final String IMG_BGC_GRAY = "bg-gray";

    /** The Constant BGC_GRAY. */
    private static final String BGC_GRAY = "bgc=246,246,246";

    /**
     * The Constant IMAGE_SHARPNESS.
     */
    private static final String IMAGE_SHARPNESS = "resMode=bisharp";

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
     * The Constant DESKTOP.
     */
    private static final String DESKTOP = "desktop";

    /**
     * The Constant DESKTOP.
     */
    private static final String DESKTOP_LARGE = "desktopL";

    /**
     * The Constant MOBILELANDSCAPE.
     */
    private static final String MOBILELANDSCAPE = "mobileL";

    /**
     * The Constant PATH_SEPARATOR.
     */
    private static final String PATH_SEPARATOR = "/";

    /**
     * The Constant MOBILEPORTRAIT.
     */
    private static final String MOBILEPORTRAIT = "mobileP";

    @PostConstruct
    protected void postConstruct() {
        String dynamicMediaUrl = getImageServiceURL();
        final String rootPath = getRootPath();
        String damPath;
        if (imagePath != null) {
            String subString;
            final int iend = imagePath.indexOf('.');
            if (iend != -1) {
                subString = imagePath.substring(0, iend);
                damPath = StringUtils.substringBeforeLast(subString, PATH_SEPARATOR);
                damPath = damPath.replace(damPath, rootPath);
                finalPath = damPath + PATH_SEPARATOR + getScene7FileName();
            }
        }

        if (null != dynamicMediaUrl) {
            dynamicMediaUrl = StringUtils.removeEndIgnoreCase(dynamicMediaUrl, PATH_SEPARATOR) + finalPath;
        }

        if (StringUtils.isNotBlank(dynamicMediaUrl)) {
            setDesktopUrl(createDynamicMediaUrl(DESKTOP, dynamicMediaUrl));
            setDesktopLargeUrl(createDynamicMediaUrl(DESKTOP_LARGE, dynamicMediaUrl));
            setMobilePortraitUrl(createDynamicMediaUrl(MOBILEPORTRAIT, dynamicMediaUrl));
            setMobileLandscapeUrl(createDynamicMediaUrl(MOBILELANDSCAPE, dynamicMediaUrl));
        }
        setDefaultImage();
    }

    /**
     * Gets the scene 7 file name.
     *
     * @return the scene 7 file name
     */
    private String getScene7FileName() {
        String fileName = StringUtils.EMPTY;
        final Resource resource = request.getResourceResolver().getResource(imagePath + "/jcr:content/metadata");
        if (Objects.nonNull(resource)) {
            final ValueMap properties = resource.getValueMap();
            fileName = properties.get("dam:scene7Name", StringUtils.EMPTY);
        }
        return fileName;
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
        return url.append(appendingString).append(FMT_PNG_ALPHA);
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
     * Creates the url
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

    private String getCropParameterForScene7(final Long height, Long width) {
        if (null == imageCrop) {
            return StringUtils.EMPTY;
        }
        if(width > 1280) {
            width = (long) 1280;
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
            return StringUtils.substringAfterLast(resource.getResourceType(), PATH_SEPARATOR);
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
     * @return the url
     */
    private static String getUrl(final String paramUrl, final String width, final String height,
            final String crop, final String imgBackground) {
        boolean queryFlag = false;
        StringBuilder url = new StringBuilder(paramUrl);
        if (StringUtils.isNotEmpty(width)) {
            url.append(QUERY_PARAMETER).append(WIDTH).append(EQUALS).append(width);
            queryFlag = true;
        }
        if (StringUtils.isNotEmpty(height)) {
            if (queryFlag) {
                url = url.append(AMPERSAND).append(HEIGHT).append(EQUALS).append(height);
            } else {
                url = url.append(QUERY_PARAMETER).append(HEIGHT).append(EQUALS).append(height);
            }
            queryFlag = true;
        }
        if (StringUtils.isNotEmpty(crop)) {
            if (queryFlag) {
                url = url.append(AMPERSAND).append(CROPN).append(EQUALS).append(crop);
            } else {
                url = url.append(QUERY_PARAMETER).append(CROPN).append(EQUALS).append(crop);
            }
            queryFlag = true;
        }

        if (StringUtils.isEmpty(imgBackground)) {
            if (queryFlag) {
                url = appendTransparency(url, AMPERSAND);
            } else {
                url = appendTransparency(url, QUERY_PARAMETER);
            }
            queryFlag = true;
        } else if (IMG_BGC_GRAY.equalsIgnoreCase(imgBackground)) {
            if (queryFlag) {
                url = appendBackgroundColor(url, AMPERSAND);
            } else {
                url = appendBackgroundColor(url, QUERY_PARAMETER);
            }
            queryFlag = true;
        }

        if (queryFlag) {
            url = appendSharpness(url, AMPERSAND);
        } else {
            url = appendSharpness(url, QUERY_PARAMETER);
        }

        return url.toString();
    }

    /**
     * Creates the URL for the devices(Desktop,IPad,Mobile).
     *
     * @param deviceType
     *            the device type
     * @return the string
     */
    private String createDynamicMediaUrl(final String deviceType, final String imagePath) {
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

    private String getImageConfigurations(final String deviceType, final Map<String, String> dynamicMediaConfiguration,
            final StringBuilder key) {
        String imageConfiguration;
        final String cropping = getCroppingFromMobile();
        if (deviceType.equals(DESKTOP) && StringUtils.isNotBlank(dwidth) && StringUtils.isNotBlank(dheight)) {
            imageConfiguration = dwidth + "," + dheight;
        } else if (deviceType.equals(MOBILELANDSCAPE)) {
            imageConfiguration = getImageConfigurationForMobile(dynamicMediaConfiguration, key, cropping, mwidthl,
                    mheightl);
        } else if (deviceType.equals(MOBILEPORTRAIT)) {
            imageConfiguration = getImageConfigurationForMobile(dynamicMediaConfiguration, key, cropping, mwidthp,
                    mheightp);
        } else {
            imageConfiguration = dynamicMediaConfiguration.get(key.toString());
        }
        return imageConfiguration;
    }

    private String getImageConfigurationForMobile(final Map<String, String> dynamicMediaConfiguration, final StringBuilder key,
            final String cropping, final String mWidth, final String mHeight) {
        if (StringUtils.isNotEmpty(mWidth) && StringUtils.isNotEmpty(mHeight)) {
            return mWidth + "," + mHeight + "," + cropping;
        } else if (StringUtils.isNotEmpty(cropping)) {
            return ",," + cropping;
        }
        return dynamicMediaConfiguration.get(key.toString());
    }

    private Map<String, String> getMap(final String[] dynamicMediaConfiguration) {
        final Map<String, String> map = new HashMap<>();
        for (final String propValue : dynamicMediaConfiguration) {
            map.put(StringUtils.substringBefore(propValue, "="), StringUtils.substringAfter(propValue, "="));
        }
        return map;
    }

    public String getAltText() {
        if (StringUtils.isNotEmpty(altText)) {
            return altText;
        } else {
            return StringUtils.EMPTY;
        }
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getDefaultImageUrl() {
        return defaultImageUrl;
    }

    public String getDesktopUrl() {
        return desktopUrl;
    }

    public String[] getDynamicMediaConfiguration() {
        return dynamicMediaService.getDynamicMediaConfMap();
    }

    public String getImageServiceURL() {
        return dynamicMediaService.getImageServiceUrl();
    }

    public String getVideoServiceUrl() {
        return dynamicMediaService.getVideoServiceUrl();
    }

    public String getRootPath() {
        return dynamicMediaService.getRootPath();
    }

    public String getMobileLandscapeUrl() {
        return mobileLandscapeUrl;
    }

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

    public void setDefaultImageUrl(final String defaultImageUrl) {
        this.defaultImageUrl = defaultImageUrl;
    }

    public void setDesktopUrl(final String desktopUrl) {
        this.desktopUrl = desktopUrl;
    }

    public String getDesktopLargeUrl() {
        return desktopLargeUrl;
    }

    public void setDesktopLargeUrl(final String desktopLargeUrl) {
        this.desktopLargeUrl = desktopLargeUrl;
    }

    public void setMobileLandscapeUrl(final String mobileLandscapeUrl) {
        this.mobileLandscapeUrl = mobileLandscapeUrl;
    }

    public void setMobilePortraitUrl(final String mobilePortraitUrl) {
        this.mobilePortraitUrl = mobilePortraitUrl;
    }

    public String getFinalPath() {
        return finalPath;
    }

}
