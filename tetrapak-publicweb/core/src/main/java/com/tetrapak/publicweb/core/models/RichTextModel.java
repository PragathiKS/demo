package com.tetrapak.publicweb.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * This is a model class for Rich Text Editor component.
 *
 * @author Nitin Kumar
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RichTextModel {

    /** The anchor id. */
    @ValueMapValue
    private String anchorId;

    /** The anchor title. */
    @ValueMapValue
    private String anchorTitle;

    /** The text. */
    @ValueMapValue
    private String text;

    /** The pw theme. */
    @ValueMapValue
    private String pwTheme;

    /** The pw padding. */
    @ValueMapValue
    private String pwPadding;

    /** The top spacing. */
    @ValueMapValue
    private String topSpacing;
    
    /** The pw text color. */
    @ValueMapValue
    private String pwTextColor;

    @ValueMapValue
    private String pwTextAlignment;

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
     * Gets the text.
     *
     * @return the text
     */
    public String getText() {
        return text;
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
     * Gets the pw padding.
     *
     * @return the pw padding
     */
    public String getPwPadding() {
        return pwPadding;
    }

    /**
     * Gets the top spacing.
     *
     * @return the top spacing
     */
    public String getTopSpacing() {
        return topSpacing;
    }

    /**
     * Gets the pw text color.
     *
     * @return the pw text color
     */
	public String getPwTextColor() {
		return pwTextColor;
	}

    public String getPwTextAlignment() {
        return pwTextAlignment;
    }
}
