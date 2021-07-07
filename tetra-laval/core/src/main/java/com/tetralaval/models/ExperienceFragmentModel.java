package com.tetralaval.models;

import com.day.cq.commons.jcr.JcrConstants;
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
import java.util.HashMap;
import java.util.Map;

/**
 * The Class FooterConfigurationModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ExperienceFragmentModel {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperienceFragmentModel.class);

    private final static String CONTENT_ROOT_PATH = "/content/tetra-laval/home";
    private final static String EXPERIENCE_FRAGMENT_ROOT_PATH = "/content/experience-fragments/tetra-laval/en";

    private final static String HEADER_EXPERIENCE_FRAGMENT_NODENAME = "header";
    private final static String FOOTER_EXPERIENCE_FRAGMENT_NODENAME = "footer";

    private final static String FRAGMENT_PATH_PROPERTY = "fragmentPath";

    /** The request. */
    @Self
    private Resource resource;

    private ResourceResolver resourceResolver;

    private Map<String, String> headerMap;

    private Map<String, String> footerMap;

    @PostConstruct
    protected void init() {
        resourceResolver = resource.getResourceResolver();
        Node contentRootNode = getContentRootNode();

        if (!checkIfExperienceFragment() && !checkIfContentRootPage() && contentRootNode != null) {
            Node headerNode = getExperienceFragmentNode(contentRootNode, HEADER_EXPERIENCE_FRAGMENT_NODENAME);
            Node footerNode = getExperienceFragmentNode(contentRootNode, FOOTER_EXPERIENCE_FRAGMENT_NODENAME);

            headerMap = getExperienceFragmentMap(headerNode);
            footerMap = getExperienceFragmentMap(footerNode);
        }
    }

    private boolean checkIfExperienceFragment() {
        return resource.getPath().indexOf(EXPERIENCE_FRAGMENT_ROOT_PATH) != -1;
    }

    private boolean checkIfContentRootPage() {
        return resource.getPath().equals(String.format("%s/%s", CONTENT_ROOT_PATH, JcrConstants.JCR_CONTENT));
    }

    private Node getContentRootNode() {
        Resource resource = resourceResolver.resolve(CONTENT_ROOT_PATH);
        if (resource != null && resource.adaptTo(Node.class) != null) {
            return resource.adaptTo(Node.class);
        }
        return null;
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

    private Map<String, String> getExperienceFragmentMap(Node node) {
        Map<String, String> map = new HashMap<>();
        try {
            map.put(FRAGMENT_PATH_PROPERTY, node.getProperty(FRAGMENT_PATH_PROPERTY).getString());
        } catch (RepositoryException re) {
            LOGGER.error("Property {} does not exist", FRAGMENT_PATH_PROPERTY);
            return null;
        }
        return map;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public Map<String, String> getFooterMap() {
        return footerMap;
    }
}
