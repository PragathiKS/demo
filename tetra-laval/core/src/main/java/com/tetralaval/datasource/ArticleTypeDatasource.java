package com.tetralaval.datasource;

import com.adobe.cq.sightly.WCMUsePojo;
import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.day.cq.commons.jcr.JcrConstants;
import com.tetralaval.models.search.FilterModel;
import com.tetralaval.services.ArticleService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.osgi.service.component.annotations.Activate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ArticleTypeDatasource extends WCMUsePojo {

    @Activate
    public void activate() throws Exception {
        ArticleService articleService = getSlingScriptHelper().getService(ArticleService.class);
        List<Resource> values = new ArrayList<>();

        for (FilterModel filterModel : articleService.getFilterTypes()) {
            ValueMap vm = new ValueMapDecorator(new HashMap<>());
            vm.put("value", filterModel.getKey());
            vm.put("text", filterModel.getLabel());
            values.add(new ValueMapResource(getResourceResolver(), new ResourceMetadata(),
                    JcrConstants.NT_UNSTRUCTURED, vm));
        }
        getRequest().setAttribute(DataSource.class.getName(), new SimpleDataSource(values.iterator()));
    }

}
