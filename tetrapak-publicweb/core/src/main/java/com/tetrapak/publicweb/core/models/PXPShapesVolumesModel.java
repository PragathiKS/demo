package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.publicweb.core.beans.pxp.Shape;
import com.tetrapak.publicweb.core.models.multifield.ManualModel;

/**
 * The Class PXPShapesVolumesModel.
 *
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PXPShapesVolumesModel {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PXPShapesVolumesModel.class);

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
    @Default(values = "grayscale-white")
    private String pwTheme;

    @ValueMapValue
    private String pwImgBackground;

    /** The teaser list. */
    private final List<ManualModel> teaserList = new ArrayList<>();

    /** The Weburl. */
    private String webUrl;


    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() { 
        LOGGER.debug("Inside init of {}", this.getClass().getName());
        final ProductModel product = resource.adaptTo(ProductModel.class);
        if (Objects.nonNull(product)) {
            if(product.getWebUrl()!=null) {
                webUrl = product.getWebUrl();
            }
        	setShapesList(product.getShapes());
        }
    }

    /**
     * Sets the teaser list from Shapes.
     *
     * @param list
     */
    private void setShapesList(final List<Shape> list) {
        for (final Shape shape : list) {
            final ManualModel teaser = new ManualModel();
            teaser.setTitle(shape.getName());
            if(null != shape.getVolumes()) {
                teaser.setDescription(getDescription(shape.getVolumes()));
            }
            teaser.setFileReference(shape.getThumbnail());
            teaser.setAlt(shape.getName());
            teaserList.add(teaser);
        }
        LOGGER.debug("Teaser list updated successfully.");
    }

    /**
     * Concatenate volumes value to generate comma separated description
     *
     * @param volumes
     * @return
     */
    private String getDescription(final List<String> volumes) {
        return String.join(", ",volumes);
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
     * Gets the card style.
     *
     * @return the card style
     */
    public String getPwImgBackground() {
        return pwImgBackground;
    }

    /**
     * Gets the teaser list.
     *
     * @return the teaser list
     */
    public List<ManualModel> getTeaserList() {
        return new ArrayList<>(teaserList);
    }

    /**
     * Gets the Weburl.
     *
     * @return the Weburl
     */
    public String getWebUrl() {
        return webUrl;
    }
}
