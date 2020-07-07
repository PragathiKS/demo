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

    /** The col 1 limit. */
    private int col1Limit;

    /** The col 2 limit. */
    private int col2Limit;

    /** The col 3 limit. */
    private int col3Limit;

    /** The col 4 limit. */
    private int col4Limit;

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
     * @param pseudoCategoriesSection
     *            the new pseudo categories section
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
     * @param subSections
     *            the new sub sections
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
     * @param subSectionCount
     *            the new sub section count
     */
    public void setSubSectionCount(final int subSectionCount) {
        this.subSectionCount = subSectionCount;
    }

    /**
     * Gets the col 1 limit.
     *
     * @return the col 1 limit
     */
    public int getCol1Limit() {
        return col1Limit;
    }

    /**
     * Sets the col 1 limit.
     *
     * @param col1Limit
     *            the new col 1 limit
     */
    public void setCol1Limit(final int col1Limit) {
        this.col1Limit = col1Limit;
    }

    /**
     * Gets the col 2 limit.
     *
     * @return the col 2 limit
     */
    public int getCol2Limit() {
        return col2Limit;
    }

    /**
     * Sets the col 2 limit.
     *
     * @param col2Limit
     *            the new col 2 limit
     */
    public void setCol2Limit(final int col2Limit) {
        this.col2Limit = col2Limit;
    }

    /**
     * Gets the col 3 limit.
     *
     * @return the col 3 limit
     */
    public int getCol3Limit() {
        return col3Limit;
    }

    /**
     * Sets the col 3 limit.
     *
     * @param col3Limit
     *            the new col 3 limit
     */
    public void setCol3Limit(final int col3Limit) {
        this.col3Limit = col3Limit;
    }

    /**
     * Gets the col 4 limit.
     *
     * @return the col 4 limit
     */
    public int getCol4Limit() {
        return col4Limit;
    }

    /**
     * Sets the col 4 limit.
     *
     * @param col4Limit
     *            the new col 4 limit
     */
    public void setCol4Limit(final int col4Limit) {
        this.col4Limit = col4Limit;
    }
}
