package com.tetrapak.publicweb.core.beans;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class PseudoCategoryCFBean.
 */
public class PseudoCategoryCFBean {

    /** The pseudo category key. */
    private String pseudoCategoryKey;

    /** The pseudo category value. */
    private String pseudoCategoryValue;

    /** The pseudo category order. */
    private int pseudoCategoryOrder;

    private List<String> pageList;

    /**
     * Gets the pseudo category key.
     *
     * @return the pseudo category key
     */
    public String getPseudoCategoryKey() {
        return pseudoCategoryKey;
    }

    /**
     * Sets the pseudo category key.
     *
     * @param pseudoCategoryKey
     *            the new pseudo category key
     */
    public void setPseudoCategoryKey(final String pseudoCategoryKey) {
        this.pseudoCategoryKey = pseudoCategoryKey;
    }

    /**
     * Gets the pseudo category value.
     *
     * @return the pseudo category value
     */
    public String getPseudoCategoryValue() {
        return pseudoCategoryValue;
    }

    /**
     * Sets the pseudo category value.
     *
     * @param pseudoCategoryValue
     *            the new pseudo category value
     */
    public void setPseudoCategoryValue(final String pseudoCategoryValue) {
        this.pseudoCategoryValue = pseudoCategoryValue;
    }

    public int getPseudoCategoryOrder() {
        return pseudoCategoryOrder;
    }

    public void setPseudoCategoryOrder(final int pseudoCategoryOrder) {
        this.pseudoCategoryOrder = pseudoCategoryOrder;
    }

    public List<String> getPageList() {
        if (CollectionUtils.isEmpty(pageList)) {
            pageList = new ArrayList<>();
        }
        return pageList;
    }

    public void setPageList(final List<String> pageList) {
        this.pageList = pageList;
    }
}
