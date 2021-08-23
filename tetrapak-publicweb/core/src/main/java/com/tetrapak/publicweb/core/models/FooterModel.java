package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tetrapak.publicweb.core.models.multifield.FooterLinkModel;
import com.tetrapak.publicweb.core.models.multifield.SocialLinkModel;
import com.tetrapak.publicweb.core.utils.LinkUtils;

/**
 * The Class FooterModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FooterModel {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(FooterModel.class);

	/** The request. */
	@SlingObject
	private SlingHttpServletRequest request;

	/** The logo image path. */
	private String logoImagePath;

	/** The logo link. */
	private String logoLink;

	/** The logo alt. */
	private String logoAlt;

	/** The social links. */
	private List<SocialLinkModel> socialLinks;

	/** The footer links. */
	private List<FooterLinkModel> footerLinksSanitized = new ArrayList<>();

	/** The go to top label. */
	private String goToTopLabel;

	/** We chat QR code Text */
	private String wechatQrCodeText;

	/** We chat QR code */
	private String wechatQrCodeReference;

	/** We chat Alt text */
	private String qrAltText;

	/** We chat App Store */
	private String appStoreReference;

	/** We chat App Store Alt text */
	private String appStoreAltText;

	/** We chat Google play */
	private String googlePlayReference;

	/** We chat Google play Alt text */
	private String googlePlayAltText;

	/**
	 * Inits the.
	 */
	@PostConstruct
	protected void init() {
		final List<FooterLinkModel> footerLinks;
		LOGGER.debug("inside init method");
		final String rootPath = LinkUtils.getRootPath(request.getPathInfo());
		final String path = rootPath + "/jcr:content/root/responsivegrid/footerconfiguration";
		final Resource footerConfigurationResource = request.getResourceResolver().getResource(path);
		if (Objects.nonNull(footerConfigurationResource)) {
			final FooterConfigurationModel configurationModel = footerConfigurationResource
					.adaptTo(FooterConfigurationModel.class);
			if (Objects.nonNull(configurationModel)) {
				logoImagePath = configurationModel.getLogoImagePath();
				logoLink = LinkUtils.sanitizeLink(configurationModel.getLogoLink(), request);
				logoAlt = configurationModel.getLogoAlt();
				socialLinks = configurationModel.getSocialLinks();
				footerLinks = configurationModel.getFooterLinks();
				for (FooterLinkModel footerLink : footerLinks) {
					String sanitizedPath = LinkUtils.sanitizeLink(footerLink.getLinkPath(), request);
					footerLink.setLinkPath(sanitizedPath);
					footerLinksSanitized.add(footerLink);
				}
				goToTopLabel = configurationModel.getGoToTopLabel();
				wechatQrCodeText = configurationModel.getWechatQrCodeText();
				wechatQrCodeReference = configurationModel.getWechatQrCodeReference();
				qrAltText = configurationModel.getQrAltText();
				appStoreReference = configurationModel.getAppStoreReference();
				appStoreAltText = configurationModel.getAppStoreAltText();
				googlePlayReference = configurationModel.getGooglePlayReference();
				googlePlayAltText = configurationModel.getGooglePlayAltText();
			}
		}
	}

	/**
	 * Gets the logo image path.
	 *
	 * @return the logo image path
	 */
	public String getLogoImagePath() {
		return logoImagePath;
	}

	/**
	 * Gets the logo link.
	 *
	 * @return the logo link
	 */
	public String getLogoLink() {
		return logoLink;
	}

	/**
	 * Gets the logo alt.
	 *
	 * @return the logo alt
	 */
	public String getLogoAlt() {
		return logoAlt;
	}

	/**
	 * Gets the social links.
	 *
	 * @return the social links
	 */
	public List<SocialLinkModel> getSocialLinks() {
		return new ArrayList<>(socialLinks);
	}

	/**
	 * Gets the footer links.
	 *
	 * @return the footer links
	 */
	public List<FooterLinkModel> getFooterLinks() {
		return new ArrayList<>(footerLinksSanitized);
	}

	/**
	 * Gets the go to top label.
	 *
	 * @return the go to top label
	 */
	public String getGoToTopLabel() {
		return goToTopLabel;
	}

	/**
	 * Gets the We chat QR code.
	 *
	 * @return the We chat QR code.
	 */
	public String getWechatQrCodeReference() {
		return wechatQrCodeReference;
	}

	/**
	 * Gets the Image Alt text.
	 *
	 * @return the Image Alt text.
	 */
	public String getQrAltText() {
		return qrAltText;
	}

	/**
	 * Gets the QR code text.
	 * 
	 * @return the QR code text.
	 */
	public String getWechatQrCodeText() {
		return wechatQrCodeText;
	}

	/**
	 * Gets the App store.
	 * 
	 * @return the App store.
	 */
	public String getAppStoreReference() {
		return appStoreReference;
	}

	/**
	 * Gets the App store Alt text.
	 * 
	 * @return the App store Alt text.
	 */
	public String getAppStoreAltText() {
		return appStoreAltText;
	}

	/**
	 * Gets the Google play store.
	 * 
	 * @return the Google play store.
	 */
	public String getGooglePlayReference() {
		return googlePlayReference;
	}

	/**
	 * Gets the Google play Alt text.
	 * 
	 * @return the Google play Alt text.
	 */
	public String getGooglePlayAltText() {
		return googlePlayAltText;
	}
}
