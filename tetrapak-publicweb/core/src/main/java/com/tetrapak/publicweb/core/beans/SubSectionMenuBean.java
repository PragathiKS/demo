package com.tetrapak.publicweb.core.beans;

import java.util.List;

/**
 * The Class SubSectionMenuBean.
 */
public class SubSectionMenuBean {

    /** The pseudo categories section. */
    private List<PseudoCategoriesSectionBean> pseudoCategoriesSection;

    /** The sub sections. */
    private List<SubSectionBean> subSections;

    /** The sub section count. */
    private int subSectionCount;

    /**
     * Gets the pseudo categories section.
     *
     * @return the pseudo categories section
     */
    public List<PseudoCategoriesSectionBean> getPseudoCategoriesSection() {
        return pseudoCategoriesSection;
    }

    /**
     * Sets the pseudo categories section.
     *
     * @param pseudoCategoriesSection the new pseudo categories section
     */
    public void setPseudoCategoriesSection(final List<PseudoCategoriesSectionBean> pseudoCategoriesSection) {
        this.pseudoCategoriesSection = pseudoCategoriesSection;
    }

    /**
     * Gets the sub sections.
     *
     * @return the sub sections
     */
    public List<SubSectionBean> getSubSections() {
        return subSections;
    }

    /**
     * Sets the sub sections.
     *
     * @param subSections the new sub sections
     */
    public void setSubSections(final List<SubSectionBean> subSections) {
        this.subSections = subSections;
    }

    /**
     * Gets the sub section count.
     *
     * @return the sub section count
     */
    public int getSubSectionCount() {
        return subSectionCount;
    }

    /**
     * Sets the sub section count.
     *
     * @param subSectionCount the new sub section count
     */
    public void setSubSectionCount(final int subSectionCount) {
        this.subSectionCount = subSectionCount;
    }
}
