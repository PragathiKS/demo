package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.tetrapak.publicweb.core.models.multifield.LinkModel;

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

    /** The anchor id. */
    @ValueMapValue
    private String anchorId;

    /** The anchor title. */
    @ValueMapValue
    private String anchorTitle;

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
    @Inject
    private List<LinkModel> columnOneList;

    /** The column two list. */
    @Inject
    private List<LinkModel> columnTwoList;

    /** The column three list. */
    @Inject
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
        final List<LinkModel> lists = new ArrayList<>();
        if (Objects.nonNull(columnOneList)) {
            lists.addAll(columnOneList);
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
        if (Objects.nonNull(columnTwoList)) {
            lists.addAll(columnTwoList);
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
        if (Objects.nonNull(columnThreeList)) {
            lists.addAll(columnThreeList);
        }
        return lists;
    }

}
