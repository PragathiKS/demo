package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import com.tetrapak.publicweb.core.models.multifield.PseudoCategoryModel;

/**
 * The Class MegaMenuSolutionModel.
 */
@Model(
        adaptables = { Resource.class },
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MegaMenuConfigurationModel {

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The top section subtitle. */
    @ValueMapValue
    private String topSectionSubtitle;

    /** The bottom section subtitle. */
    @ValueMapValue
    private String bottomSectionSubtitle;

    /** The hide food categories. */
    @ValueMapValue
    private String hideFoodCategories;

    /** The end to end solution section. */
    @Inject
    private List<MegaMenuSolutionModel> endToEndSolutionSection;

    /** The link section. */
    @Inject
    private List<LinkModel> linkSection;

    /** The food category section. */
    @Inject
    private List<MegaMenuSolutionModel> foodCategorySection;

    /** The pseudo category bean. */
    @Inject
    private List<PseudoCategoryModel> pseudoCategoryBean;

    /**
     * Gets the top section subtitle.
     *
     * @return the top section subtitle
     */
    public String getTopSectionSubtitle() {
        return topSectionSubtitle;
    }

    /**
     * Gets the bottom section subtitle.
     *
     * @return the bottom section subtitle
     */
    public String getBottomSectionSubtitle() {
        return bottomSectionSubtitle;
    }

    /**
     * Gets the hide food categories.
     *
     * @return the hide food categories
     */
    public Boolean getHideFoodCategories() {
        return BooleanUtils.toBoolean(hideFoodCategories);
    }

    /**
     * Gets the end to end solution section.
     *
     * @return the end to end solution section
     */
    public List<MegaMenuSolutionModel> getEndToEndSolutionSection() {
        return endToEndSolutionSection;
    }

    /**
     * Gets the link section.
     *
     * @return the link section
     */
    public List<LinkModel> getLinkSection() {
        return linkSection;
    }

    /**
     * Gets the food category section.
     *
     * @return the food category section
     */
    public List<MegaMenuSolutionModel> getFoodCategorySection() {
        return foodCategorySection;
    }

    /**
     * Gets the pseudo category list.
     *
     * @return the pseudo category list
     */
    public List<PseudoCategoryModel> getPseudoCategoryList() {
        final List<PseudoCategoryModel> pseudoCategoryList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(pseudoCategoryBean)) {
            pseudoCategoryList.addAll(pseudoCategoryBean);
        }
        return pseudoCategoryList;
    }
}
