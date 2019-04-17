package com.tetrapak.publicweb.core.services;

import java.util.List;

import org.apache.sling.api.resource.ResourceResolver;

import com.tetrapak.publicweb.core.beans.BestPracticeLineBean;

public interface BestPracticeLineService {
	
    Boolean checkIfPracticeLineExists(ResourceResolver resourceResolver, String productType, String subCategoryVal, String rootPath);
	
	List<BestPracticeLineBean> getListOfPracticeLines(ResourceResolver resourceResolver, String productType, String subCategoryVal, String rootPath);

}
