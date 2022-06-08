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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
     * Components root path on the page
     */
    private static final String CONTENT_COMPONENT_PATH = "/jcr:content/root/responsivegrid";

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
        final String rootPath = currentPage.getPath() + CONTENT_COMPONENT_PATH;
        Optional.ofNullable(request.getResourceResolver().getResource(rootPath))
                .map(Resource::getChildren)
                .map(children -> StreamSupport.stream(children.spliterator(), false))
                .orElse(Stream.empty())
                .filter(Objects::nonNull)
                .forEach(resource -> {
                    ValueMap vMap;
                    if (ReferenceModel.RESOURCE_TYPE.equals(resource.getResourceType())) {
                        vMap = getValuesFromReference(resource);
                    } else {
                        vMap = resource.getValueMap();
                    }

                    final String anchorId = vMap.get("anchorId", StringUtils.EMPTY);
                    final String anchorTitle = vMap.get("anchorTitle", StringUtils.EMPTY);
                    setAnchorBean(new AnchorBean(anchorId, anchorTitle));
        });
    }

    private ValueMap getValuesFromReference(Resource resource) {
        ValueMap vMap = resource.getValueMap();
        String refPath = vMap.get("contentPath", StringUtils.EMPTY);
        if (null != refPath) {
            String locale = GlobalUtil.getSelectedLanguage(request, userPreferenceService);
            locale = StringUtils.isNotBlank(locale) ? locale : CustomerHubConstants.DEFAULT_LOCALE;
            refPath = refPath.replace(CustomerHubConstants.PATH_SEPARATOR + CustomerHubConstants.DEFAULT_LOCALE,
                    CustomerHubConstants.PATH_SEPARATOR + locale);
            Resource refResource = request.getResourceResolver().getResource(refPath);
            if (refResource != null) {
                vMap = refResource.getValueMap();
            }
        }
        return vMap;
    }

    /**
     * Sets the anchor bean.
     *
     * @param anchorBean the bean
     */
    private void setAnchorBean(final AnchorBean anchorBean) {
        if (StringUtils.isNotBlank(anchorBean.getAnchorId()) && StringUtils.isNotBlank(anchorBean.getAnchorTitle())) {
            anchorDetailList.add(anchorBean);
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
