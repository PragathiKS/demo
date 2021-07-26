package com.tetralaval.models;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.tetralaval.constants.PWConstants;
import com.tetralaval.utils.LinkUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Node;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Class FooterConfigurationModel.
 */
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeaderConfigurationModel {
    private final static String HIDE_IN_NAV_PROPERTY = "hideInNav";
    private final static String ROOT_PATH = "/content/tetrapak/tetralaval/global/en";

    /** The resource. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The resource. */
    @Inject
    private Resource resource;

    /** The logo image path. */
    @ValueMapValue
    private String logoImagePath;

    /** The logo link. */
    @ValueMapValue
    private String logoLink;

    /** The logo alt. */
    @ValueMapValue
    private String logoAlt;

    /** The contact link. */
    @ValueMapValue
    private String contactLink;

    /** The search link. */
    @ValueMapValue
    private String searchLink;

    private ResourceResolver resourceResolver;
    private List<NavigationModel> navigationModelList;

    @PostConstruct
    protected void init() {
        resource = request.getResource();
        resourceResolver = resource.getResourceResolver();
        navigationModelList = generateNavigation(ROOT_PATH);
    }

    public String getLogoImagePath() {
        return logoImagePath;
    }

    public String getLogoLink() {
        return LinkUtils.sanitizeLink(logoLink, request);
    }

    public String getLogoAlt() {
        return logoAlt;
    }

    public String getContactLink() {
        return LinkUtils.sanitizeLink(contactLink, request);
    }

    public String getSearchLink() {
        return LinkUtils.sanitizeLink(searchLink, request);
    }

    public List<NavigationModel> getNavigationModelList() {
        return navigationModelList;
    }

    private List<NavigationModel> generateNavigation(String path) {
        List<NavigationModel> navigationModelList = new ArrayList<>();
        List<String> paths = Arrays.stream(path.split(PWConstants.SLASH))
                .filter(s -> !StringUtils.EMPTY.equals(s)).collect(Collectors.toList());
        int currentLevel = paths.size();
        if (currentLevel <= (PWConstants.SOLUTIONS_SECTION_MENU_PAGE_LEVEL + 1)) {
            Resource currentResource = resourceResolver.resolve(path);
            if (currentResource != null && currentResource.adaptTo(Node.class) != null) {
                Page currentPage = currentResource.adaptTo(Page.class);
                Iterator<Page> pageIterator = currentPage.listChildren();

                while (pageIterator.hasNext()) {
                    Page childPage = pageIterator.next();
                    NavigationModel navigationModel = new NavigationModel();

                    ValueMap properties = childPage.getProperties();
                    boolean isHide = Boolean.parseBoolean((String) properties.getOrDefault(HIDE_IN_NAV_PROPERTY, "false"));

                    if (!isHide) {
                        String currentPath = childPage.getPath();
                        navigationModel.setLabel((String) properties.getOrDefault(JcrConstants.JCR_TITLE, null));
                        navigationModel.setLink(LinkUtils.sanitizeLink(currentPath, request));
                        navigationModel.setChildren(generateNavigation(currentPath));
                        navigationModelList.add(navigationModel);
                    }
                }
            }
        }
        return navigationModelList.size() == 0 ? null : navigationModelList;
    }

}
