package com.tetrapak.publicweb.core.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

import static com.tetrapak.publicweb.core.constants.PWConstants.*;

@Component(
        service = Servlet.class,
        property = { "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.selectors=" + "mobilemegamenu", "sling.servlet.extensions=" + "json",
                "sling.servlet.resourceTypes=" + "publicweb/components/structure/megamenuconfigv2",})
public class MobileMegaMenuServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        Resource megamenuConfigResource = request.getResource();
        String[] selectors = request.getRequestPathInfo().getSelectors();
        String currentMarket = StringUtils.EMPTY;
        if(selectors.length>=2){
            currentMarket = selectors[1];
        }
        JsonObject mobileJson = new JsonObject();
        Iterator<Resource> columns = megamenuConfigResource.listChildren();
        while (columns.hasNext()){
            Resource colResource = columns.next();
            JsonObject columnJson = new JsonObject();
            JsonObject columnItemsJson = new JsonObject();
            JsonObject subheadingsObject = new JsonObject();
            if(colResource!=null){
                Iterator<Resource> columnChildren = colResource.listChildren();
                JsonArray accordionJsonArray = new JsonArray();
                JsonObject accordionItem = new JsonObject();
                boolean isSubHeadingPassed = false;
                int subHeadingCount =0;
                while (columnChildren.hasNext()){
                    Resource columnChildResource = columnChildren.next();
                    if (columnChildResource.isResourceType(MEGAMENU_SUBHEADING_RESOURCETYPE)){
                        isSubHeadingPassed = true;
                        subHeadingCount++;
                        if(subheadingsObject.has("subheading")){
                            accordionItem.add("subHeadings",subheadingsObject);
                            accordionJsonArray.add(accordionItem);
                        }
                        subheadingsObject = new JsonObject();
                        accordionItem = new JsonObject();
                        ValueMap columnChildProps = columnChildResource.getValueMap();
                        JsonObject subheadingObject = new JsonObject();
                        setResourceProperties(columnChildProps,subheadingObject,currentMarket,request);
                        subheadingsObject.add("subheading",subheadingObject);
                    } else if (columnChildResource.isResourceType(MEGAMENU_HEADING_RESOURCETYPE) || columnChildResource.isResourceType(MEGAMENU_TEASER_RESOURCETYPE)){
                            JsonObject columnChildObject = new JsonObject();
                            String componentName = StringUtils.substringAfterLast(columnChildResource.getResourceType(),"/");
                            ValueMap columnChildProps = columnChildResource.getValueMap();
                            setResourceProperties(columnChildProps,columnChildObject,currentMarket,request);
                            columnItemsJson.add(componentName,columnChildObject);
                    } else if (columnChildResource.isResourceType(MEGAMENU_NAVLINKS_RESOURCETYPE)){
                        JsonObject navigationLinksJson = new JsonObject();
                        Resource navLinks = columnChildResource.getChild("navLinks");
                        if(navLinks!=null){
                            JsonArray navLinksArray = new JsonArray();
                            Iterator<Resource> navLinkItems = navLinks.listChildren();
                            while (navLinkItems.hasNext()){
                                Resource navLinkItem = navLinkItems.next();
                                JsonObject navLinkItemJsonObject = new JsonObject();
                                ValueMap columnChildProps = navLinkItem.getValueMap();
                                setResourceProperties(columnChildProps,navLinkItemJsonObject,currentMarket,request);
                                navLinksArray.add(navLinkItemJsonObject);
                            }

                            navigationLinksJson.add("navLinks",navLinksArray);

                        }
                        String componentName = StringUtils.substringAfterLast(columnChildResource.getResourceType(),"/");
                        if(isSubHeadingPassed){
                            subheadingsObject.add(componentName,navigationLinksJson);
                        }else{
                            columnItemsJson.add(componentName,navigationLinksJson);
                        }
                    } else {
                        JsonObject columnChildObject = new JsonObject();
                        ValueMap columnChildProps = columnChildResource.getValueMap();
                        setResourceProperties(columnChildProps,columnChildObject,currentMarket,request);
                        String componentName = StringUtils.substringAfterLast(columnChildResource.getResourceType(),"/");
                        if(isSubHeadingPassed){
                            subheadingsObject.add(componentName,columnChildObject);
                        }else{
                            columnItemsJson.add(componentName,columnChildObject);
                        }

                    }
                }
                if(!accordionItem.has("subHeadings") || accordionJsonArray.size()<subHeadingCount){
                    accordionItem.add("subHeadings",subheadingsObject);
                    accordionJsonArray.add(accordionItem);
                }
                columnItemsJson.add("megaMenuAccordions",accordionJsonArray);
                columnJson.add("columnItems",columnItemsJson);
                mobileJson.add(colResource.getName(),columnJson);
            }

        }
        PrintWriter writer = response.getWriter();
        response.setContentType(APPLICATION_JSON);
        response.setCharacterEncoding(UTF_8);
        writer.print(new Gson().toJson(mobileJson));
    }

    private void setResourceProperties(ValueMap properties, JsonObject jsonObject, String currentMarket,SlingHttpServletRequest request) {
        for(Map.Entry<String,Object> prop : properties.entrySet()){
            if(!prop.getKey().startsWith("jcr:") && !prop.getKey().startsWith("sling:")){
                if(prop.getValue().toString().contains("/content/tetrapak/publicweb")){
                    String url = prop.getValue().toString();
                    url = url.replaceAll(LANG_MASTERS,currentMarket);
                    url = LinkUtils.sanitizeLink(url,request);
                    jsonObject.addProperty(prop.getKey(),url);
                    continue;
                }
                jsonObject.addProperty(prop.getKey(),prop.getValue().toString());
            }
        }
    }

}
