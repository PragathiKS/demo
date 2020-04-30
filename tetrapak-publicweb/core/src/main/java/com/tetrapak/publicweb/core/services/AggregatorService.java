package com.tetrapak.publicweb.core.services;

import java.util.List;
import org.apache.sling.api.resource.Resource;
import com.day.cq.wcm.api.Page;
import com.tetrapak.publicweb.core.models.AggregatorModel;
import com.tetrapak.publicweb.core.models.multifield.SemiAutomaticModel;

public interface AggregatorService {

    List<AggregatorModel> getAggregatorList(Resource resource, List<SemiAutomaticModel> pagePaths);

    List<AggregatorModel> getAggregatorList(Resource resource,String[] tags,int maxTabs, String logicalOperator);

    AggregatorModel getAggregator(Page currentPage);

}
