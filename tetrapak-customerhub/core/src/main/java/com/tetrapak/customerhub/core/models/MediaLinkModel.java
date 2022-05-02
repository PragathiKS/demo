package com.tetrapak.customerhub.core.models;

import java.util.ArrayList;
import java.util.List;

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

    /** The heading. */
    @ValueMapValue
    private String heading;

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

    /**
     * Gets the heading.
     *
     * @return the heading
     */
    public String getHeading() {
        return heading;
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
        final List<LinkModel> lists = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(columnOneList)) {
            columnOneList.forEach(f -> {
                f.setLinkUrl(LinkUtil.getValidLink(resource, f.getLinkUrl()));
                lists.add(f);
            });
        }
        return lists;
    }

    /**
     * Gets the column two list.
     *
     * @return the column two list
     */
    public List<LinkModel> getColumnTwoList() {
        final List<LinkModel> lists = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(columnTwoList)) {
            columnTwoList.forEach(f -> {
                f.setLinkUrl(LinkUtil.getValidLink(resource, f.getLinkUrl()));
                lists.add(f);
            });
        }
        return lists;
    }

    /**
     * Gets the column three list.
     *
     * @return the column three list
     */
    public List<LinkModel> getColumnThreeList() {
        final List<LinkModel> lists = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(columnThreeList)) {
            columnThreeList.forEach(f -> {
                f.setLinkUrl(LinkUtil.getValidLink(resource, f.getLinkUrl()));
                lists.add(f);
            });
        }
        return lists;
    }

}
