package com.tetrapak.customerhub.core.models;

import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.customerhub.core.utils.GlobalUtil;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ShapeModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShapeModel.class);

    /** The alt. */
    @ValueMapValue
    private String shape;

    /** The file reference. */
    @ValueMapValue
    private String fileReference;

    /** The alt. */
    @ValueMapValue
    private String alt;

    /** The title of shape tag **/
    private String title;

    /** The name of shape tag **/
    private String name;

    TagManager tagManager;

    @Self
    @Via("resourceResolver")
    PageManager pageManager;

    @Self
    private Resource resource;

    @Inject
    private ResourceResolverFactory resourceResolverFactory;

    @PostConstruct
    protected void init() {
	try (ResourceResolver resourceResolver = GlobalUtil
		.getResourceResolverFromSubService(resourceResolverFactory)) {
	    tagManager = resourceResolver.adaptTo(TagManager.class);
	    Page currentPage = pageManager.getContainingPage(resource);
	    Locale currentLocale = currentPage.getLanguage(true);
	    if (StringUtils.isNotBlank(shape)) {
		Tag tag = tagManager.resolve(shape);
		if (null != tag) {
		    title = tag.getTitle(currentLocale);
		    name = tag.getName();
		}
	    }
	} catch (Exception e) {
	    LOGGER.error("Error when getting Resource Resolver from Factory");
	}

    }

    public String getShape() {
	return shape;
    }

    public String getFileReference() {
	return fileReference;
    }

    public String getAlt() {
	return alt;
    }

    public String getTitle() {
	return title;
    }

    public String getName() {
	return name;
    }

}
