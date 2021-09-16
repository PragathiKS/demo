package com.tetralaval.models;

import com.tetralaval.models.multifield.ContactDetailsModel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContactModel {

    private final static String CONTACT_DETAILS_NODE = "contactDetails";
    private final static String ANCHOR_ID_PROPERTY = "anchorId";
    private final static String ANCHOR_TITLE_PROPERTY = "anchorTitle";
    private final static String TEXT_PROPERTY = "text";

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
        List<ContactDetailsModel> contactDetailsModels = new ArrayList<>();
        Resource contactDetailsResource = resource.getChild(CONTACT_DETAILS_NODE);
        if (contactDetailsResource.adaptTo(Node.class) != null) {
            try {
                Node contactDetailsNode = contactDetailsResource.adaptTo(Node.class);
                NodeIterator iterator = contactDetailsNode.getNodes();

                while (iterator.hasNext()) {
                    Node currentNode = iterator.nextNode();
                    ContactDetailsModel contactDetailsModel = new ContactDetailsModel();

                    contactDetailsModel.setAnchorId(currentNode.hasProperty(ANCHOR_ID_PROPERTY) ?
                            currentNode.getProperty(ANCHOR_ID_PROPERTY).getString() : null);
                    contactDetailsModel.setAnchorTitle(currentNode.hasProperty(ANCHOR_TITLE_PROPERTY) ?
                            currentNode.getProperty(ANCHOR_TITLE_PROPERTY).getString() : null);
                    contactDetailsModel.setText(currentNode.hasProperty(TEXT_PROPERTY) ?
                            currentNode.getProperty(TEXT_PROPERTY).getString() : null);
                    contactDetailsModels.add(contactDetailsModel);
                }
            } catch (RepositoryException re) {
                return new ArrayList<>();
            }
        }
        return contactDetailsModels;
    }
}
