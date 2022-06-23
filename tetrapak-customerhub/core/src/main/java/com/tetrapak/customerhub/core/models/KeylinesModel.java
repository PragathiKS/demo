package com.tetrapak.customerhub.core.models;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.tagging.TagManager;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;

/**
 * The Class KeylinesModel.
 */

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class KeylinesModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeylinesModel.class);

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The title. */
    @ValueMapValue
    private String title;

    /** The dowloadText. */
    @ValueMapValue
    private String dowloadText;

    /** The manual list. */
    @Inject
    @Via("resource")
    private List<ShapeModel> shapes;

    /** The anchor id. */
    @ValueMapValue
    private String anchorId;

    /** The anchor title. */
    @ValueMapValue
    private String anchorTitle;

    /** The pw theme. */
    @ValueMapValue
    private String pwTheme;

    @Inject
    Resource resource;

    @Self
    @Via("resourceResolver")
    TagManager tagManager;

    private String apiUrl;

    private String packageType;

    @PostConstruct
    protected void init() {
	apiUrl = String.format("%s.%s.%s", resource.getPath(), CustomerHubConstants.KEYLINES_SLING_SERVLET_SELECTOR,
		CustomerHubConstants.JSON_SERVLET_EXTENSION);
	LOGGER.debug("API URL {}", apiUrl);
	if (null != shapes && !shapes.isEmpty()) {
	    String shape = shapes.get(0).getShape();
	    if (null != tagManager.resolve(shape)) {
		packageType = tagManager.resolve(shape).getParent().getTagID();
	    }
	}
	LOGGER.debug("Package Type {}", packageType);
    }

    public String getTitle() {
	return title;
    }

    public List<ShapeModel> getShapes() {
	return shapes;
    }

    public String getAnchorId() {
	return anchorId;
    }

    public String getAnchorTitle() {
	return anchorTitle;
    }

    public String getPwTheme() {
	return pwTheme;
    }

    public String getDowloadText() {
	return dowloadText;
    }

    public String getApiUrl() {
	return apiUrl;
    }

    public String getPackageType() {
	return packageType;
    }

}
