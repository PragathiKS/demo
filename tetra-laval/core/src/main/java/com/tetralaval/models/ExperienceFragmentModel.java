package com.tetralaval.models;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.tetralaval.constants.TLConstants;
import com.tetralaval.utils.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
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
 * The Class ExperienceFragmentModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ExperienceFragmentModel {
    /** LOGGER constant */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperienceFragmentModel.class);

    /** HEADER_EXPERIENCE_FRAGMENT_NODENAME constant */
    private static final String HEADER_EXPERIENCE_FRAGMENT_NODENAME = "header";
    /** FOOTER_EXPERIENCE_FRAGMENT_NODENAME constant */
    private static final String FOOTER_EXPERIENCE_FRAGMENT_NODENAME = "footer";

    /** FRAGMENT_PATH_PROPERTY constant */
    private static final String FRAGMENT_PATH_PROPERTY = "fragmentPath";

    /** The resource. */
    @Self
    private Resource resource;

    /** headerPath */
    private String headerPath;

    /** footerPath */
    private String footerPath;

    /**
     * Init method
     */
    @PostConstruct
    protected void init() {
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

    /**
     * Check if page is an experience fragment
     * @return boolean
     */
    private boolean checkIfExperienceFragment() {
        return resource.getPath().indexOf(TLConstants.EXPERIENCE_FRAGMENTS_PATH) != -1;
    }

    /**
     * Get experience fragment as node base on nodename
     * @param node
     * @param xfNodename
     * @return experience fragment node
     */
    private Node getExperienceFragmentNode(Node node, String xfNodename) {
        String path = String.format("%s/%s", JcrConstants.JCR_CONTENT, xfNodename);
        Node xfNode = null;

        try {
            xfNode = node.getNode(path);
        } catch (RepositoryException re) {
            LOGGER.error("Node from path {} does not exist", path, re);
        }
        return xfNode;
    }

    /**
     * Get fragment path stored in properties
     * @param node
     * @return fragment path
     */
    private String getFragmentPath(Node node) {
        if (node == null) {
            return null;
        }
        
        String fragmentPath = null;
        try {
            fragmentPath = String.format("%s/%s/root.content.html", node.getProperty(FRAGMENT_PATH_PROPERTY).getString(),
                    JcrConstants.JCR_CONTENT);
        } catch (RepositoryException re) {
            LOGGER.error("Property {} does not exist", FRAGMENT_PATH_PROPERTY, re);
        }
        return fragmentPath;
    }

    /**
     * headerPath getter
     * @return headerPath
     */
    public String getHeaderPath() {
        return headerPath;
    }

    /**
     * footerPath getter
     * @return footerPath
     */
    public String getFooterPath() {
        return footerPath;
    }
}
