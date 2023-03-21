package com.tetralaval.datasource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.Servlet;

import org.apache.commons.collections4.iterators.TransformIterator;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.day.cq.commons.jcr.JcrConstants;
import com.tetralaval.beans.Dropdown;
import com.tetralaval.services.FormService;

@Component(service = Servlet.class, property = {
	Constants.SERVICE_DESCRIPTION
		+ "= This drop down datasource provides data for all dialog drop downs used in form dialog",
	"sling.servlet.resourceTypes=" + "tetra-laval/components/content/form/datasource/formdata" })
public class FormDataSource extends SlingSafeMethodsServlet {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5259185175033873202L;
    private static final String TEXT = "text";
    private static final String VALUE = "value";
    private static final String ACTION_TYPE = "actionType";
    private static final String EMAIL_TEMPLATE = "emailTemplate";
    private static final String CONTACTUS_CONTENTFRAGMENT = "contactDetails";
    private static final String DROP_DOWN_TYPE = "dropDownType";
    private static final String DATASOURCE = "datasource";

    private static Logger LOGGER = LoggerFactory.getLogger(FormDataSource.class);

    @Reference
    private FormService formService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
	try {
	    ResourceResolver resourceResolver = request.getResourceResolver();
	    List<Dropdown> dropDownList = new ArrayList<>();
	    String dropDownType = request.getResource().getChild(DATASOURCE).getValueMap().get(DROP_DOWN_TYPE,
		    String.class);
	    switch (dropDownType) {
	    case ACTION_TYPE:
		dropDownList = formService.getActionTypesAsDropdown();
		break;
	    case EMAIL_TEMPLATE:
		dropDownList = formService.getEmailTemplatesAsDropdown();
		break;
	    case CONTACTUS_CONTENTFRAGMENT:
		dropDownList = formService.getContactUsFragmentsAsDropdown(resourceResolver);
	    default:
		break;
	    }
	    @SuppressWarnings({ "unchecked", "rawtypes" })
	    DataSource dataSource = new SimpleDataSource(new TransformIterator(dropDownList.iterator(), input -> {
		Dropdown dropdown = (Dropdown) input;
		ValueMap valueMap = new ValueMapDecorator(new HashMap<>());
		String label = dropdown.getLabel();
		String value = dropdown.getValue();
		valueMap.put(VALUE, value);
		valueMap.put(TEXT, label);
		LOGGER.debug("Label: {} ::: Value:{}", label, value);
		return new ValueMapResource(resourceResolver, new ResourceMetadata(), JcrConstants.NT_UNSTRUCTURED,
			valueMap);
	    }));
	    request.setAttribute(DataSource.class.getName(), dataSource);
	} catch (Exception e) {
	    LOGGER.error("An error occured when setting datasource", e);
	}
    }
}
