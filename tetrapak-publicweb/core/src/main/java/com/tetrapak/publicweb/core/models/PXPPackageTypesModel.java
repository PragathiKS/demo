package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.publicweb.core.beans.pxp.Packagetype;
import com.tetrapak.publicweb.core.beans.pxp.Shape;
import com.tetrapak.publicweb.core.models.multifield.ManualModel;

/**
 * The Class PXPPackageTypesModel.
 *
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PXPPackageTypesModel {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PXPPackageTypesModel.class);

    /** The resource. */
    @Self
    private Resource resource;

    /** The heading. */
    @ValueMapValue
    @Default(values = "Package Types")
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

    /** The teaser list. */
    private List<ManualModel> teaserList = new ArrayList<>();

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        ProductModel product = resource.adaptTo(ProductModel.class);
        setPackageTypeList(product.getPackageTypeReferences());
    }

    /**
     * Sets the teaser list from Shapes.
     * 
     * @param list
     */
    private void setPackageTypeList(List<Packagetype> list) {
        
        final Map<String, String> productPageMap = fetchPageMap(getIdList(list));
        for (Packagetype packageType : list) {
            for (Shape shape : packageType.getShapes()) {
                ManualModel teaser = new ManualModel();
                teaser.setTitle(packageType.getName().concat(" ").concat(shape.getName()));
                if (null != shape.getVolumes()) {
                    teaser.setDescription(getDescription(shape.getVolumes()));
                }
                teaser.setFileReference(shape.getThumbnail());
                teaser.setAlt(shape.getName());
                teaser.setLinkText("Read more");               
                teaser.setLinkPath(productPageMap.get(packageType.getId()));
                teaserList.add(teaser);
            }
        }
        LOGGER.debug("Teaser list updated successfully.");
    }

    private Map<String, String> fetchPageMap(List<String> list) {
        Map<String, String> productPageMap = new HashMap<>();
        productPageMap.put(list.get(0), "/content");
        return productPageMap;
        
    }
    
    /**
     * Concatenate volumes value to generate comma separated description
     * 
     * @param volumes
     * @return
     */
    private String getDescription(List<String> volumes) {
        return String.join(", ", volumes);
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
     * Gets the teaser list.
     *
     * @return the teaser list
     */
    public List<ManualModel> getTeaserList() {
        return new ArrayList<>(teaserList);
    }
    
    /**
     * Gets the id list.
     * @param packageTypeList 
     *
     * @return the id list
     */
    private List<String> getIdList(List<Packagetype> packageTypeList) {
        return packageTypeList.stream().filter(list -> list.getId() != null).map(Packagetype::getId)
                .collect(Collectors.toList());
    }
}
