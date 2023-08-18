package com.tetrapak.publicweb.core.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tetrapak.publicweb.core.models.megamenucolumnitem.MegaMenuAccordion;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.factory.ModelFactory;
import org.json.JSONException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Model(
        adaptables = { Resource.class },
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = "publicweb/components/structure/megamenucolumn")
@Exporter(name = "jackson", extensions = "json")
public class MegaMenuColumnModel {

    @SlingObject
    private Resource resource;

    @JsonIgnore
    private List<Resource> items;

    private Map<String,Object> columnItems;

    private List<MegaMenuAccordion> megaMenuAccordions;

    @OSGiService
    private ModelFactory modelFactory;

    @ValueMapValue
    private String bgColor;

    @PostConstruct
    protected void init() throws JSONException {
        items = new ArrayList<>();
        columnItems = new HashMap<>();
        if(resource!=null) {
            Iterator<Resource> children = resource.listChildren();
            megaMenuAccordions = new ArrayList<>();
            MegaMenuAccordion megaMenuAccordion = null;
            while (children.hasNext()) {
                Resource itemResource = children.next();
                items.add(itemResource);
                if (itemResource.isResourceType("publicweb/components/structure/megamenucolumnitems/subheading")){
                    megaMenuAccordion = new MegaMenuAccordion();
                    Map<String,Object> accordionItems = new HashMap<>();
                    accordionItems.put(itemResource.getName(),itemResource.adaptTo(modelFactory.getModelFromResource(itemResource).getClass()));
                    megaMenuAccordion.setSubHeadings(accordionItems);
                    megaMenuAccordions.add(megaMenuAccordion);
                }else if(megaMenuAccordion !=null && !itemResource.isResourceType("publicweb/components/structure/megamenucolumnitems/heading")) {
                    Map<String,Object> accordionItems = megaMenuAccordion.getSubHeadings();
                    accordionItems.put(itemResource.getName(),itemResource.adaptTo(modelFactory.getModelFromResource(itemResource).getClass()));
                    megaMenuAccordion.setSubHeadings(accordionItems);
                }else{
                    columnItems.put(itemResource.getName(),itemResource.adaptTo(modelFactory.getModelFromResource(itemResource).getClass()));
                }
            }
            columnItems.put("megaMenuAccordions",megaMenuAccordions);
        }
    }

    public String getBgColor() {
        return bgColor;
    }

    public List<Resource> getItems() {
        return items;
    }

    public Map<String,Object> getColumnItems() {
        return columnItems;
    }
}
