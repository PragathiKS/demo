package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.tetrapak.publicweb.core.beans.AnchorBean;

/**
 * The Class AnchorModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AnchorModel {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AnchorModel.class);

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The current page. */
    @Inject
    private Page currentPage;

    /** The anchor detail list. */
    private List<AnchorBean> anchorDetailList = new ArrayList<>();

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        LOGGER.debug("Inside init method");
        final String rootPath = currentPage.getPath() + "/jcr:content/root/responsivegrid";
        Resource rootResource = request.getResourceResolver().getResource(rootPath);
        if (Objects.nonNull(rootResource)) {
            final Iterator<Resource> rootIterator = rootResource.listChildren();
            while (rootIterator.hasNext()) {
                final AnchorBean bean = new AnchorBean();
                final Resource childResource = rootIterator.next();
                if (Objects.nonNull(childResource)) {
                    final ValueMap vMap = childResource.getValueMap();
                    final String anchorId = vMap.get("anchorId", StringUtils.EMPTY);
                    final String anchorTitle = vMap.get("anchorTitle", StringUtils.EMPTY);
                    setAnchorBean(bean, anchorId, anchorTitle);
                }
            }
        }
    }

    /**
     * Sets the anchor bean.
     *
     * @param bean
     *            the bean
     * @param anchorId
     *            the anchor id
     * @param anchorTitle
     *            the anchor title
     */
    private void setAnchorBean(final AnchorBean bean, final String anchorId, final String anchorTitle) {
        if (StringUtils.isNotBlank(anchorId) && StringUtils.isNotBlank(anchorTitle)) {
            bean.setAnchorId(anchorId);
            bean.setAnchorTitle(anchorTitle);
            anchorDetailList.add(bean);
        }
    }

    /**
     * Gets the anchor detail list.
     *
     * @return the anchor detail list
     */
    public List<AnchorBean> getAnchorDetailList() {
        return new ArrayList<AnchorBean>(anchorDetailList);
    }

}
