package com.tetrapak.customerhub.core.models;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.tetrapak.customerhub.core.utils.LinkUtil;

/**
 * The Class MediaLinkModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MediaLinkModel {

    /** The resource. */
    @Self
    private Resource resource;

    /** The anchor id. */
    @ValueMapValue
    private String anchorId;
    
    /** The anchor title. */
    @ValueMapValue
    private String anchorTitle;
    
    /** The heading. */
    @ValueMapValue
    private String heading;
    
    /** The pw theme. */
    @ValueMapValue
    private String pwTheme;

    /** The column one title. */
    @ValueMapValue
    private String columnOneTitle;

    /** The column two title. */
    @ValueMapValue
    private String columnTwoTitle;

    /** The column three title. */
    @ValueMapValue
    private String columnThreeTitle;

    /** The column one list. */
    @ChildResource
    private List<LinkModel> columnOneList;

    /** The column two list. */
    @ChildResource
    private List<LinkModel> columnTwoList;

    /** The column three list. */
    @ChildResource
    private List<LinkModel> columnThreeList;

    @PostConstruct
    protected void init() {
	if (CollectionUtils.isNotEmpty(columnOneList)) {
	    columnOneList.forEach(link -> link.setLinkUrl(LinkUtil.getValidLink(resource, link.getLinkUrl())));
	}
	if (CollectionUtils.isNotEmpty(columnTwoList)) {
	    columnTwoList.forEach(link -> link.setLinkUrl(LinkUtil.getValidLink(resource, link.getLinkUrl())));
	}
	if (CollectionUtils.isNotEmpty(columnThreeList)) {
	    columnThreeList.forEach(link -> link.setLinkUrl(LinkUtil.getValidLink(resource, link.getLinkUrl())));
	}
    }

    /**
     * Gets the heading.
     *
     * @return the heading
     */
    public String getHeading() {
	return heading;
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
     * Gets the pw theme.
     *
     * @return the pw theme
     */
    public String getPwTheme() {
        return pwTheme;
    }

    /**
     * Gets the column one title.
     *
     * @return the column one title
     */
    public String getColumnOneTitle() {
	return columnOneTitle;
    }

    /**
     * Gets the column two title.
     *
     * @return the column two title
     */
    public String getColumnTwoTitle() {
	return columnTwoTitle;
    }

    /**
     * Gets the column three title.
     *
     * @return the column three title
     */
    public String getColumnThreeTitle() {
	return columnThreeTitle;
    }

    /**
     * Gets the column one list.
     *
     * @return the column one list
     */
    public List<LinkModel> getColumnOneList() {
	return columnOneList;
    }

    /**
     * Gets the column two list.
     *
     * @return the column two list
     */
    public List<LinkModel> getColumnTwoList() {
	return columnTwoList;
    }

    /**
     * Gets the column three list.
     *
     * @return the column three list
     */
    public List<LinkModel> getColumnThreeList() {
	return columnThreeList;
    }

}
