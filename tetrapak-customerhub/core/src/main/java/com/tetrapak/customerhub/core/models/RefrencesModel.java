package com.tetrapak.customerhub.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import com.tetrapak.customerhub.core.constants.CustomerHubConstants;

/**
 * Model class for reference component
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RefrencesModel {

	@Inject
	private String contentPath;

	private String locale;

	@PostConstruct
	protected void init() {
		// no implementation of this method required
	}

	/**
	 * Process the path to fetch the locale specific path.
	 *
	 * @return processed path
	 */
	public String getContentPath() {
		String path = contentPath;
		locale = StringUtils.isNotBlank(locale) ? locale : CustomerHubConstants.DEFAULT_LOCALE;
		if (null != path) {
			path = path.replace(CustomerHubConstants.PATH_SEPARATOR + CustomerHubConstants.DEFAULT_LOCALE,
					CustomerHubConstants.PATH_SEPARATOR + locale);
		}
		return path;
	}

}
