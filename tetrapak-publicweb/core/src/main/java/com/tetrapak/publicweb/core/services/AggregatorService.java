package com.tetrapak.publicweb.core.services;

import java.util.List;
import org.apache.sling.api.resource.Resource;
import com.day.cq.wcm.api.Page;
import com.tetrapak.publicweb.core.models.AggregatorModel;
import com.tetrapak.publicweb.core.models.multifield.SemiAutomaticModel;

/**
 * The Interface AggregatorService.
 */
public interface AggregatorService {

    /**
     * Gets the aggregator list.
     *
     * @param resource
     *            the resource
     * @param pagePaths
     *            the page paths
     * @return the aggregator list
     */
    List<AggregatorModel> getAggregatorList(Resource resource, List<SemiAutomaticModel> pagePaths);

    /**
     * Gets the aggregator list.
     *
     * @param resource
     *            the resource
     * @param tags
     *            the tags
     * @param maxTabs
     *            the max tabs
     * @param logicalOperator
     *            the logical operator
     * @return the aggregator list
     */
    List<AggregatorModel> getAggregatorList(Resource resource, String[] tags, int maxTabs, String logicalOperator);

    /**
     * Gets the aggregator.
     *
     * @param currentPage
     *            the current page
     * @return the aggregator
     */
    AggregatorModel getAggregator(Page currentPage);

}
