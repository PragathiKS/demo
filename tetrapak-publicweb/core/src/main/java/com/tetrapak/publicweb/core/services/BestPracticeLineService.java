package com.tetrapak.publicweb.core.services;

import com.tetrapak.publicweb.core.beans.BestPracticeLineBean;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.List;

public interface BestPracticeLineService {
	
    Boolean checkIfPracticeLineExists(ResourceResolver resourceResolver, String productType, String subCategoryVal, String rootPath);
	
	List<BestPracticeLineBean> getListOfPracticeLines(ResourceResolver resourceResolver, String productType, String subCategoryVal, String rootPath);

}
