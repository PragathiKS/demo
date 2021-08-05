package com.tetralaval.services.impl;

import com.tetralaval.config.ArticleConfiguration;
import com.tetralaval.constants.TLConstants;
import com.tetralaval.models.search.FilterModel;
import com.tetralaval.services.ArticleService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Component(immediate = true, service = ArticleService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = ArticleConfiguration.class)
public class ArticleServiceImpl implements ArticleService {
    private static final Logger log = LoggerFactory.getLogger(ArticleServiceImpl.class);

    private ArticleConfiguration config;

    @Activate
    public void activate(ArticleConfiguration config) {
        this.config = config;
    }

    @Override
    public String[] getType() {
        return config.type();
    }

    @Override
    public List<FilterModel> getFilterTypes() {
        List<FilterModel> filterModels = new ArrayList<>();
        for (String type : getType()) {
            String[] parts = type.split(TLConstants.COLON);
            FilterModel filterModel = new FilterModel();
            filterModel.setKey(parts[0]);
            filterModel.setLabel(parts[1]);
            filterModels.add(filterModel);
        }
        return filterModels;
    }
}