package com.tetralaval.models;

import com.tetralaval.constants.TLConstants;
import com.tetralaval.models.multifield.ContactDetailsModel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.List;

/**
 * ContactModel
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContactModel {
    /** LOGGER constant */
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactModel.class);

    /** CONTACT_DETAILS_NODE constant */
    private static final String CONTACT_DETAILS_NODE = "contactDetails";

    /** resource */
    @Inject
    private Resource resource;

    /** title */
    @ValueMapValue
    private String title;

    /** mapUrl */
    @ValueMapValue
    private String mapUrl;

    /** pwTheme */
    @ValueMapValue
    private String pwTheme;

    /** pwPadding */
    @ValueMapValue
    private String pwPadding;

    /** contactDetails */
    private List<ContactDetailsModel> contactDetails;

    /**
     * Init method
     */
    @PostConstruct
    protected void init() {
        contactDetails = prepareContactDetails();
    }

    /**
     * title getter
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * mapUrl getter
     * @return mapUrl
     */
    public String getMapUrl() {
        return mapUrl;
    }

    /**
     * pwTheme getter
     * @return pwTheme
     */
    public String getPwTheme() {
        return pwTheme;
    }

    /**
     * pwPadding getter
     * @return pwPadding
     */
    public String getPwPadding() {
        return pwPadding;
    }

    /**
     * contactDetails getter
     * @return contactDetails
     */
    public List<ContactDetailsModel> getContactDetails() {
        return contactDetails;
    }

    /**
     * Preparing contact details
     * @return list of ContactDetailsModel
     */
    private List<ContactDetailsModel> prepareContactDetails() {
        Resource contactDetailsResource = resource.getChild(CONTACT_DETAILS_NODE);
        if (contactDetailsResource == null) {
            return new ArrayList<>();
        }

        List<ContactDetailsModel> contactDetailsModels = new ArrayList<>();
        try {
            Node contactDetailsNode = contactDetailsResource.adaptTo(Node.class);
            NodeIterator iterator = contactDetailsNode.getNodes();

            while (iterator.hasNext()) {
                Node currentNode = iterator.nextNode();
                ContactDetailsModel contactDetailsModel = new ContactDetailsModel();

                if (currentNode.hasProperty(TLConstants.ANCHOR_ID_PROPERTY)) {
                    contactDetailsModel.setAnchorId(currentNode.getProperty(TLConstants.ANCHOR_ID_PROPERTY).getString());
                }
                if (currentNode.hasProperty(TLConstants.ANCHOR_TITLE_PROPERTY)) {
                    contactDetailsModel.setAnchorTitle(currentNode.getProperty(TLConstants.ANCHOR_TITLE_PROPERTY).getString());
                }
                if (currentNode.hasProperty(TLConstants.TEXT_PROPERTY)) {
                    contactDetailsModel.setText(currentNode.getProperty(TLConstants.TEXT_PROPERTY).getString());
                }
                contactDetailsModels.add(contactDetailsModel);
            }
        } catch (RepositoryException re) {
            LOGGER.error("Exception in ContactModel#getContactDetails():{} ", re.getMessage(), re);
            return new ArrayList<>();
        }
        return contactDetailsModels;
    }
}
