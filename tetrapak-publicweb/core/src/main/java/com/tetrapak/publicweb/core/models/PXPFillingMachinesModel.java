package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.QueryBuilder;
import com.tetrapak.publicweb.core.beans.pxp.FillingMachine;
import com.tetrapak.publicweb.core.models.multifield.ManualModel;
import com.tetrapak.publicweb.core.utils.ProductPageUtil;

/**
 * The Class PXPFillingMachinesModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PXPFillingMachinesModel {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PXPFillingMachinesModel.class);
    
    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;
    
    /** The resource. */
    @Self
    @Via("resource")
    private Resource resource;

    /** The heading. */
    @ValueMapValue
    private String heading;

    /** The link text. */
    @ValueMapValue
    private String linkText;

    /** The pw theme. */
    @ValueMapValue
    @Default(values = "grayscale-white")
    private String pwTheme;

    @ValueMapValue
    private String pwImgBackground;

    /** The anchor id. */
    @ValueMapValue
    private String anchorId;

    /** The anchor title. */
    @ValueMapValue
    private String anchorTitle;

    /** The teaser list. */
    private final List<ManualModel> teaserList = new ArrayList<>();

    @OSGiService
    private QueryBuilder queryBuilder;

    /**
     * The init method.
     */
    @PostConstruct
    protected void init() {
    	LOGGER.debug("Inside init of {}", this.getClass().getName());
        final ProductModel product = resource.adaptTo(ProductModel.class);
        if (Objects.nonNull(product)) {
        	setTeaserList(product.getFillingMachineReferences());
        }
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
     * Sets the teaser list.
     *
     * @param list the new teaser list
     */
    private void setTeaserList(final List<FillingMachine> list) {
        final Map<String, String> productPageMap = ProductPageUtil.getProductPageMap(request, getIdList(list), resource,
                queryBuilder);
        for (final FillingMachine fillingMachine : list) {
            final ManualModel teaser = new ManualModel();
            teaser.setTitle(fillingMachine.getName());
            teaser.setDescription(fillingMachine.getHeader());
            teaser.setFileReference(fillingMachine.getThumbnail());
            teaser.setAlt(fillingMachine.getName());
            teaser.setLinkText(linkText);
            teaser.setLinkPath(productPageMap.get(fillingMachine.getId()));
            teaserList.add(teaser);
        }
        LOGGER.debug("Teaser list updated successfully.");
    }



    /**
     * Gets the id list.
     *
     * @return the id list
     */
    private List<String> getIdList(List<FillingMachine> fillingMachineList) {
        return fillingMachineList.stream().filter(list -> list.getId() != null).map(FillingMachine::getId)
                .collect(Collectors.toList());
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
}
