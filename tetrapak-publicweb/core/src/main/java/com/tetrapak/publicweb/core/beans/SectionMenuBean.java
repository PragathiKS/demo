package com.tetrapak.publicweb.core.beans;

/**
 * The Class SectionMenuBean.
 */
public class SectionMenuBean extends SubSectionBean {

    /** The sub section menu. */
    private SubSectionMenuBean subSectionMenu;

    /**
     * Gets the sub section menu.
     *
     * @return the sub section menu
     */
    public SubSectionMenuBean getSubSectionMenu() {
        return subSectionMenu;
    }

    /**
     * Sets the sub section menu.
     *
     * @param subSectionMenu the new sub section menu
     */
    public void setSubSectionMenu(final SubSectionMenuBean subSectionMenu) {
        this.subSectionMenu = subSectionMenu;
    }
}
