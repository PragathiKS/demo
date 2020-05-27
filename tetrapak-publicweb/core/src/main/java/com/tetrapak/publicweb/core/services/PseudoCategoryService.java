package com.tetrapak.publicweb.core.services;

import com.tetrapak.publicweb.core.beans.PseudoCategoryCFBean;

import org.apache.sling.api.resource.ResourceResolver;

import java.util.List;

public interface PseudoCategoryService {

    List<PseudoCategoryCFBean> fetchPseudoCategories(final ResourceResolver resourceResolver);

    String getPseudoCategoriesCFRootPath();
}
