package com.tetralaval.models;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.tetralaval.constants.TLConstants;
import com.tetralaval.utils.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * The Class FooterConfigurationModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ExperienceFragmentModel {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperienceFragmentModel.class);

    private final static String EXPERIENCE_FRAGMENT_ROOT_PATH = "/content/experience-fragments/tetra-laval";

    private final static String HEADER_EXPERIENCE_FRAGMENT_NODENAME = "header";
    private final static String FOOTER_EXPERIENCE_FRAGMENT_NODENAME = "footer";

    private final static String FRAGMENT_PATH_PROPERTY = "fragmentPath";

    /** The request. */
    @Self
    private Resource resource;

    private ResourceResolver resourceResolver;

    private String headerPath;

    private String footerPath;

    @PostConstruct
    protected void init() {
        resourceResolver = resource.getResourceResolver();
        int currentLevel = Arrays.stream(PageUtil.getCurrentPage(resource).getPath().split(TLConstants.SLASH))
                .filter(s -> !StringUtils.EMPTY.equals(s)).collect(Collectors.toList()).size() - 1;
        Page page = PageUtil.getLanguagePage(resource);

        if (!checkIfExperienceFragment() && page != null && page.adaptTo(Node.class) != null &&
                currentLevel > TLConstants.LANGUAGE_PAGE_LEVEL) {
            Node currentNode = page.adaptTo(Node.class);
            Node headerNode = getExperienceFragmentNode(currentNode, HEADER_EXPERIENCE_FRAGMENT_NODENAME);
            Node footerNode = getExperienceFragmentNode(currentNode, FOOTER_EXPERIENCE_FRAGMENT_NODENAME);

            headerPath = getFragmentPath(headerNode);
            footerPath = getFragmentPath(footerNode);
        }
    }

    private boolean checkIfExperienceFragment() {
        return resource.getPath().indexOf(EXPERIENCE_FRAGMENT_ROOT_PATH) != -1;
    }


    private Node getExperienceFragmentNode(Node node, String xfNodename) {
        String path = String.format("%s/%s", JcrConstants.JCR_CONTENT, xfNodename);
        Node xfNode = null;

        try {
            xfNode = node.getNode(path);
        } catch (RepositoryException re) {
            LOGGER.error("Node from path {} does not exist", path);
        }
        return xfNode;
    }

    private String getFragmentPath(Node node) {
        String fragmentPath = null;
        try {
            fragmentPath = String.format("%s/%s/root.content.html", node.getProperty(FRAGMENT_PATH_PROPERTY).getString(), JcrConstants.JCR_CONTENT);
        } catch (RepositoryException re) {
            LOGGER.error("Property {} does not exist", FRAGMENT_PATH_PROPERTY);
        }
        return fragmentPath;
    }

    public String getHeaderPath() {
        return headerPath;
    }

    public String getFooterPath() {
        return footerPath;
    }
}