package com.tetrapak.publicweb.core.models.megamenucolumnitem;


import org.apache.sling.api.resource.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MegaMenuAccordion {

    private Map<String,Object> subHeadings;

    public Map<String,Object> getSubHeadings() {
        return subHeadings !=null ? subHeadings : new HashMap<>();
    }

    public void setSubHeadings(Map<String,Object> subHeadings) {
        this.subHeadings = subHeadings;
    }
}
