package com.tetrapak.publicweb.core.mock;

import java.util.ArrayList;
import java.util.List;

import org.apache.sling.api.resource.ResourceResolver;

import com.day.cq.wcm.api.Page;
import com.tetrapak.publicweb.core.services.TeaserSearchService;

public class MockTeaserSearchServiceImpl implements TeaserSearchService {

	@Override
	public List<Page> getListOfTeasers(ResourceResolver resourceResolver, String[] tags, String rootPath, int limit) {
		List<Page> pages = new ArrayList<>();
		return pages;
	}

}
