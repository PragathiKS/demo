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

@Component(
        service = Servlet.class,
        property = { "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.paths=" + "/bin/publicweb/megamenucolumns" })
public class MegaMenuColumnsServlet extends SlingAllMethodsServlet {

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        String numberOfColumns = request.getParameter("columns");
        String contentResourcePath = request.getParameter("contentPath");
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
