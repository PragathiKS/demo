package com.tetrapak.publicweb.core.beans;

import java.util.List;

/**
 * The Class PseudoCategoriesSectionBean.
 */
public class PseudoCategoriesSectionBean {

    /** The title. */
    private String title;

    /** The sub section count. */
    private int subSectionCount;

    /** The subsections. */
    private List<SubSectionBean> subSections;

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title the new title
     */
    public void setTitle(final String title) {
        this.title = title;
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
}
