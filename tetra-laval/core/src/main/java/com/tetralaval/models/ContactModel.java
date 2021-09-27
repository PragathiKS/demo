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

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContactModel {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactModel.class);

    private static final String CONTACT_DETAILS_NODE = "contactDetails";

    @Inject
    private Resource resource;

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String mapUrl;

    @ValueMapValue
    private String pwTheme;

    @ValueMapValue
    private String pwPadding;

    private List<ContactDetailsModel> contactDetails;

    @PostConstruct
    protected void init() {
        contactDetails = prepareContactDetails();
    }

    public String getTitle() {
        return title;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public String getPwTheme() {
        return pwTheme;
    }

    public String getPwPadding() {
        return pwPadding;
    }

    public List<ContactDetailsModel> getContactDetails() {
        return contactDetails;
    }

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
            LOGGER.error("Exception in ContactModel#getContactDetails()", re.getMessage(), re);
            return new ArrayList<>();
        }
        return contactDetailsModels;
    }
}
