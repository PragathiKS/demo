package com.tetrapak.customerhub.core.models;

import com.day.cq.wcm.api.Page;
import com.tetrapak.customerhub.core.beans.AnchorBean;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * The Class AnchorModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AnchorModel {

    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AnchorModel.class);

    /**
     * The request.
     */
    @SlingObject
    private SlingHttpServletRequest request;

    /**
     * The current page.
     */
    @Inject
    private Page currentPage;

    /**
     * The anchor detail list.
     */
    private List<AnchorBean> anchorDetailList = new ArrayList<>();

    @OSGiService
    private UserPreferenceService userPreferenceService;

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        LOGGER.debug("Inside init of {}", this.getClass().getName());
        final String rootPath = currentPage.getPath() + "/jcr:content/root/responsivegrid";
        Resource rootResource = request.getResourceResolver().getResource(rootPath);
        if (Objects.nonNull(rootResource)) {
            final Iterator<Resource> rootIterator = rootResource.listChildren();
            while (rootIterator.hasNext()) {
                final AnchorBean bean = new AnchorBean();
                final Resource childResource = rootIterator.next();
                if (Objects.nonNull(childResource)) {
                    ValueMap vMap = childResource.getValueMap();
                    if ("customerhub/components/content/reference".equals(childResource.getResourceType())) {
                        String refPath = vMap.get("contentPath", StringUtils.EMPTY);
                        String locale = GlobalUtil.getSelectedLanguage(request, userPreferenceService);
                        locale = org.apache.commons.lang.StringUtils.isNotBlank(locale) ? locale : CustomerHubConstants.DEFAULT_LOCALE;
                        if (null != refPath) {
                            refPath = refPath.replace(CustomerHubConstants.PATH_SEPARATOR + CustomerHubConstants.DEFAULT_LOCALE,
                                    CustomerHubConstants.PATH_SEPARATOR + locale);
                            Resource refResource = request.getResourceResolver().getResource(refPath);
                            if(refResource != null) {
                                vMap = refResource.getValueMap();
                            }
                        }
                    }
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
     * @param bean        the bean
     * @param anchorId    the anchor id
     * @param anchorTitle the anchor title
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
