package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.publicweb.core.beans.LinkBean;
import com.tetrapak.publicweb.core.utils.LinkUtils;

/**
 * The Class HeaderModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeaderModel {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(HeaderModel.class);

	/** The request. */
	@SlingObject
	private SlingHttpServletRequest request;

	/** The configuration model. */
	@Inject
	private HeaderConfigurationModel configurationModel;

	/** The logo link. */
	private String logoLink;

	/** The logo link target. */
	private String logoLinkTarget;

	/** The logo alt. */
	private String logoAlt;

	/** The login link. */
	private String loginLink;

	/** The contact link. */
	private String contactLink;

	/** The contact link target. */
	private String contactLinkTarget;

	/** The contact text. */
	private String contactText;

	/** The login label. */
	private String loginLabel;

	/** The mega menu links list. */
	private List<LinkBean> megaMenuLinksList = new ArrayList<>();

	/**
	 * Inits the.
	 */
	@PostConstruct
	protected void init() {
		LOGGER.debug("inside init method");
		String rootPath = LinkUtils.getRootPath(request.getPathInfo());
		String path = rootPath + "/jcr:content/root/responsivegrid/headerconfiguration";
		Resource headerConfigurationResource = request.getResourceResolver().getResource(path);
		configurationModel = headerConfigurationResource.adaptTo(HeaderConfigurationModel.class);
		if (Objects.nonNull(configurationModel)) {
			logoLink = configurationModel.getLogoLink();
			logoLinkTarget = configurationModel.getLogoLinkTarget();
			logoAlt = configurationModel.getLogoAlt();
			contactLink = configurationModel.getContactLink();
			contactLinkTarget = configurationModel.getContactLinkTarget();
			contactText = configurationModel.getContactText();
			loginLabel = configurationModel.getLoginLabel();
			loginLink = configurationModel.getLoginLink();
		}
		setMegaMenuLinksList(rootPath);
	}

	/**
	 * Sets the mega menu links list.
	 *
	 * @param rootPath the new mega menu links list
	 */
	public void setMegaMenuLinksList(String rootPath) {
		final Resource rootResource = request.getResourceResolver().getResource(rootPath);
		if (Objects.nonNull(rootResource)) {
			final ResourceResolver resourceResolver = rootResource.getResourceResolver();
			final PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
			if (Objects.nonNull(pageManager)) {
				Page page = pageManager.getContainingPage(rootResource);
				setLinkBean(page);
			} else {
				LOGGER.error("Page Manager is null");
			}
		}
	}

	/**
	 * Sets the link bean.
	 *
	 * @param page the new link bean
	 */
	private void setLinkBean(Page page) {
		if (Objects.nonNull(page)) {
			final Iterator<Page> childPages = page.listChildren();
			while (childPages.hasNext()) {
				final Page childPage = childPages.next();
				if (!childPage.isHideInNav()) {
					LinkBean linkBean = new LinkBean();
					linkBean.setLinkText(childPage.getTitle());
					linkBean.setLinkPath(LinkUtils.sanitizeLink(childPage.getPath()));
					megaMenuLinksList.add(linkBean);
				} else {
					LOGGER.debug("Page {} is hide in Nav", childPage.getPath());
				}
			}
		}
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
	 * Gets the logo link target.
	 *
	 * @return the logo link target
	 */
	public String getLogoLinkTarget() {
		return logoLinkTarget;
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
	 * Gets the login link.
	 *
	 * @return the login link
	 */
	public String getLoginLink() {
		return loginLink;
	}

	/**
	 * Gets the contact link.
	 *
	 * @return the contact link
	 */
	public String getContactLink() {
		return contactLink;
	}

	/**
	 * Gets the contact link target.
	 *
	 * @return the contact link target
	 */
	public String getContactLinkTarget() {
		return contactLinkTarget;
	}

	/**
	 * Gets the contact text.
	 *
	 * @return the contact text
	 */
	public String getContactText() {
		return contactText;
	}

	/**
	 * Gets the login label.
	 *
	 * @return the login label
	 */
	public String getLoginLabel() {
		return loginLabel;
	}

	/**
	 * Gets the mega menu links list.
	 *
	 * @return the mega menu links list
	 */
	public List<LinkBean> getMegaMenuLinksList() {
		return megaMenuLinksList;
	}

}
