package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.DynamicMediaService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * class for dynamic image model
 *
 * @author Nitin Kumar
 */
@Model(adaptables = { SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DynamicImageModel {

    @SlingObject
    private SlingHttpServletRequest request;

    @Inject
    private String imagePath;

    @Inject
    private String dheight;

    @Inject
    private String dwidth;

    @Inject
    private String mheightl;

    @Inject
    private String mwidthl;

    @Inject
    private String mheightp;

    @Inject
    private String mwidthp;

    @Inject
    private String imageCrop;
    /**
     * The asset alt text.
     */
    @Inject
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
     * The Constant MOBILEPORTRAIT.
     */
    private static final String MOBILEPORTRAIT = "mobileP";

    private Map<String, String> dynamicMediaConfiguration = new HashMap<>();

    @PostConstruct
    protected void postConstruct() {
        String dynamicMediaUrl = getImageServiceURL();
        String rootPath = getRootPath();
        String assetName;
        if (imagePath != null) {
            assetName = GlobalUtil.getScene7FileName(request.getResourceResolver(), imagePath);
            if (StringUtils.isEmpty(assetName)) {
                assetName = getFileNameFromPath(imagePath);
            }
            finalPath = rootPath + CustomerHubConstants.PATH_SEPARATOR + assetName;
        }

        if (null != dynamicMediaUrl) {
            dynamicMediaUrl =
                    StringUtils.removeEndIgnoreCase(dynamicMediaUrl, CustomerHubConstants.PATH_SEPARATOR) + finalPath;
        }

        if (StringUtils.isNotBlank(dynamicMediaUrl) && StringUtils.isNotBlank(altText)) {
            initImageUrls(dynamicMediaUrl);
        }
        setDefaultImage();
    }

    private void initImageUrls(String dynamicMediaUrl) {
        setDesktopUrl(createDynamicMediaUrl(DESKTOP, dynamicMediaUrl));
        setDesktopLargeUrl(createDynamicMediaUrl(DESKTOP_LARGE, dynamicMediaUrl));
        setMobilePortraitUrl(createDynamicMediaUrl(MOBILEPORTRAIT, dynamicMediaUrl));
        setMobileLandscapeUrl(createDynamicMediaUrl(MOBILELANDSCAPE, dynamicMediaUrl));
    }

    private String getFileNameFromPath(String path) {
        String assetName = StringUtils.EMPTY;
        int iend = path.lastIndexOf('.');
        if (iend != -1) {
            assetName = path.substring(0, iend);
            assetName = StringUtils.substringAfterLast(assetName, CustomerHubConstants.PATH_SEPARATOR);
        }
        return assetName;
    }

    /**
     * Append transparency.
     *
     * @param url             the url
     * @param appendingString the appending string
     * @return the string builder
     */
    private static StringBuilder appendTransparency(final StringBuilder url, final String appendingString) {
        return url.append(appendingString).append(FMT_PNG_ALPHA);
    }

    /**
     * Check image transparency.
     *
     * @param paramUrl the param url
     * @return true, if successful
     */
    private static boolean checkImageTransparency(final String paramUrl) {
        return StringUtils.contains(paramUrl, ".png") || StringUtils.contains(paramUrl, ".svg");
    }

    /**
     * Creates the url.
     *
     * @param paramUrl           the url
     * @param imageConfiguration the image configuration
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
            url = getUrl(paramUrl, width, height, crop);
        }
        return url;
    }

    /**
	 * Gets the cropping from mobile.
	 *
	 * @return the cropping from mobile
	 */
	private String getCroppingFromMobile() {
		final Resource imageResource = request.getResourceResolver().getResource(imagePath + CustomerHubConstants.DAM_METADATA_PATH);
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
     * @param resource the resource
     * @return the component name
     */
    private static String getComponentName(final Resource resource) {
        return StringUtils.substringAfterLast(resource.getResourceType(), CustomerHubConstants.PATH_SEPARATOR);
    }

    /**
     * Gets the url.
     *
     * @param paramUrl the param url
     * @param width    the width
     * @param height   the height
     * @param crop     the crop
     * @return the url
     */
    private static String getUrl(final String paramUrl, final String width, final String height, final String crop) {
        boolean queryFlag = false;
        StringBuilder url = new StringBuilder(paramUrl);
        if (StringUtils.isNotEmpty(width)) {
            url.append(QUERY_PARAMETER).append(WIDTH).append(EQUALS).append(width);
            queryFlag = true;
        }
        if (StringUtils.isNotEmpty(height)) {
            url = queryFlag ?
                    url.append(AMPERSAND).append(HEIGHT).append(EQUALS).append(height) :
                    url.append(QUERY_PARAMETER).append(HEIGHT).append(EQUALS).append(height);
            queryFlag = true;
        }
        if (StringUtils.isNotEmpty(crop)) {
            url = queryFlag ?
                    url.append(AMPERSAND).append(CROPN).append(EQUALS).append(crop) :
                    url.append(QUERY_PARAMETER).append(CROPN).append(EQUALS).append(crop);
            queryFlag = true;
        }

        if (checkImageTransparency(paramUrl)) {
            url = queryFlag ? appendTransparency(url, AMPERSAND) : appendTransparency(url, QUERY_PARAMETER);
        }

        return url.toString();
    }

    /**
     * Creates the URL for the devices(Desktop,IPad,Mobile).
     *
     * @param deviceType the device type
     * @return the string
     */
    private String createDynamicMediaUrl(final String deviceType, final String imagePath) {

        String url = "";
        dynamicMediaConfiguration = getMap(getDynamicMediaConfiguration());
        final String componentName = getComponentName(request.getResource());
        if (StringUtils.isNotBlank(componentName)) {
            final StringBuilder key = new StringBuilder(componentName).append(HYPHEN).append(deviceType);

            String imageConfiguration = getImageConfigurations(deviceType, dynamicMediaConfiguration, key);

            if (StringUtils.isNotEmpty(imageConfiguration)) {
                url = createUrl(imagePath, imageConfiguration);
                hasConfiguraton = true;
            }
        }
        return url;
    }

    private String getImageConfigurations(String deviceType, Map<String, String> dynamicMediaConfiguration,
            StringBuilder key) {
        String imageConfiguration;
        String cropping = getCroppingFromMobile();
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

    private String getImageConfigurationForMobile(java.util.Map<String, String> dynamicMediaConfiguration,
            StringBuilder key, String cropping, String mWidth, String mHeight) {
        if (StringUtils.isNotEmpty(mWidth) && StringUtils.isNotEmpty(mHeight)) {
            return mWidth + "," + mHeight + "," + cropping;
        } else if (StringUtils.isNotEmpty(cropping)) {
            return ",," + cropping;
        }
        return dynamicMediaConfiguration.get(key.toString());
    }

    private Map<String, String> getMap(String[] dynamicMediaConfiguration) {
        Map<String, String> map = new HashMap<>();
        for (String propValue : dynamicMediaConfiguration) {
            map.put(StringUtils.substringBefore(propValue, "="), StringUtils.substringAfter(propValue, "="));
        }
        return map;
    }

    public String getAltText() {
        return StringUtils.isNotEmpty(altText) ? altText : "";
    }

    public String getImageCrop() {
        return imageCrop;
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

    public void setDesktopLargeUrl(String desktopLargeUrl) {
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