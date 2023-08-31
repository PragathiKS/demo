package com.tetrapak.publicweb.core.servlets;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.Iterator;

import static com.tetrapak.publicweb.core.constants.PWConstants.MEGAMENU_CONFIGURATION_DIALOG_SELECTED_COLUMNS;
import static com.tetrapak.publicweb.core.constants.PWConstants.MEGAMENU_CONFIGURATION_RESOURCE_PATH;

/*
This servlet is used to modify the backend column nodes based on
the user selection of number of columns in dialog. For example, if the number of columns selected by author
is 2 and there are already 3 column nodes, the 3rd node will be deleted.
 */
@Component(
        service = Servlet.class,
        property = { "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.selectors=" + "updatemegamenucolumns", "sling.servlet.extensions=" + "json",
                "sling.servlet.resourceTypes=" + "publicweb/components/structure/megamenuconfigv2",})
public class MegaMenuColumnsServlet extends SlingAllMethodsServlet {

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        String numberOfColumns = request.getParameter(MEGAMENU_CONFIGURATION_DIALOG_SELECTED_COLUMNS);
        String contentResourcePath = request.getParameter(MEGAMENU_CONFIGURATION_RESOURCE_PATH);
        if(StringUtils.isNotBlank(numberOfColumns) && StringUtils.isNotBlank(contentResourcePath)){
            int selectedColumns = Integer.parseInt(numberOfColumns);
            Resource contentResource = request.getResourceResolver().getResource(contentResourcePath);
            if(contentResource!=null){
                Iterator<Resource> columns = contentResource.listChildren();
                int colIndex = 1;
                while(columns.hasNext()){
                    Resource columnResource = columns.next();
                    if(colIndex > selectedColumns){
                        request.getResourceResolver().delete(columnResource);
                    }
                    colIndex++;
                }
                if(request.getResourceResolver().hasChanges()){
                    request.getResourceResolver().commit();
                }
            }
        }

    }
}
