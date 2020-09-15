package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import com.tetrapak.publicweb.core.models.multifield.PseudoCategoryModel;
import com.tetrapak.publicweb.core.utils.LinkUtils;

/**
 * The Class MegaMenuSolutionModel.
 */
@Model(
        adaptables = { SlingHttpServletRequest.class, Resource.class },
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MegaMenuConfigurationModel {

    /** The request. */
    @SlingObject
    SlingHttpServletRequest request;

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
    @Via("resource")
    private List<MegaMenuSolutionModel> endToEndSolutionSection;

    /** The link section. */
    @Inject
    @Via("resource")
    private List<LinkModel> linkSection;

    /** The food category section. */
    @Inject
    @Via("resource")
    private List<MegaMenuSolutionModel> foodCategorySection;

    /** The pseudo category bean. */
    @Inject
    @Via("resource")
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
        final List<MegaMenuSolutionModel> endToEndSolutionList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(endToEndSolutionSection)) {
            endToEndSolutionSection.forEach(f -> {
                f.setPath(LinkUtils.sanitizeLink(f.getPath(), request));
                endToEndSolutionList.add(f);
            });
        }
        return endToEndSolutionList;
    }

    /**
     * Gets the link section.
     *
     * @return the link section
     */
    public List<LinkModel> getLinkSection() {
        final List<LinkModel> linkSectionList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(linkSection)) {
            linkSection.forEach(f -> {
                f.setLinkUrl(LinkUtils.sanitizeLink(f.getLinkUrl(), request));
                linkSectionList.add(f);
            });
        }
        return linkSectionList;
    }

    /**
     * Gets the food category section.
     *
     * @return the food category section
     */
    public List<MegaMenuSolutionModel> getFoodCategorySection() {
        final List<MegaMenuSolutionModel> foodCategoryList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(foodCategorySection)) {
            foodCategorySection.forEach(f -> {
                f.setPath(LinkUtils.sanitizeLink(f.getPath(), request));
                foodCategoryList.add(f);
            });
        }
        return foodCategoryList;
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
