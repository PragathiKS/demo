package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.tetrapak.publicweb.core.models.multifield.LinkModel;
import com.tetrapak.publicweb.core.utils.LinkUtils;

/**
 * The Class MediaLinkModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MediaLinkModel {

	/** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The heading. */
    @ValueMapValue
    private String heading;

    /** The heading Description. */
    @ValueMapValue
    private String headingDesc;

    /** The enable show image. */
    @ValueMapValue @Default(values = "false")
    private Boolean showImage;
    
    /** The Image aspect Ratio. */
    @ValueMapValue @Default(values = "1:1")
    private String imageAspectRatio;
    
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
    @Via("resource")
    private List<LinkModel> columnOneList;

    /** The column two list. */
    @Inject
    @Via("resource")
    private List<LinkModel> columnTwoList;

    /** The column three list. */
    @Inject
    @Via("resource")
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
     * Gets the headingDesc
     * 
     * @return headingDesc
     */
    public String getHeadingDesc() {
		return headingDesc;
	}

    /**
     * Gets the show image.
     *
     * @return the show image
     */
    public Boolean getShowImage() {
        return showImage;
    }
       
    /**
     * Gets the image aspect ratio.
     *
     * @return the image aspect ratio.
     */
    public String getImageAspectRatio() {
        return imageAspectRatio;
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
        if (CollectionUtils.isNotEmpty(columnOneList)) {
            columnOneList.forEach(f -> {
                f.setLinkUrl(LinkUtils.sanitizeLink(f.getLinkUrl(), request));
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
                f.setLinkUrl(LinkUtils.sanitizeLink(f.getLinkUrl(), request));
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
                f.setLinkUrl(LinkUtils.sanitizeLink(f.getLinkUrl(), request));
                lists.add(f);
            });
        }
        return lists;
    }

}
