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
    private List<SubSectionBean> subsections;

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
     * Gets the subsections.
     *
     * @return the subsections
     */
    public List<SubSectionBean> getSubsections() {
        return subsections;
    }

    /**
     * Sets the subsections.
     *
     * @param subsections the new subsections
     */
    public void setSubsections(final List<SubSectionBean> subsections) {
        this.subsections = subsections;
    }
}
