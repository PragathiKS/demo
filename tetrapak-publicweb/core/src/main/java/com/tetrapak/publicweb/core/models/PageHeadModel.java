package com.tetrapak.publicweb.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.day.cq.wcm.api.Page;
import com.tetrapak.publicweb.core.utils.PageUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.tetrapak.publicweb.core.services.BaiduMapService;

/**
 * The Class PageHeadModel.
 */
@Model(adaptables = { SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PageHeadModel {


    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    @Inject
    private Page currentPage;

    /** The BaiduMapService. */
    @OSGiService
    private BaiduMapService baiduMapService;

    /** The Baidu Map Key. */
    private String baiduMapkey;


    private String pageTitleCountrySuffix;

    /**
     * Inits the model.
     */
    @PostConstruct
    public void initModel() {
        final String path = request.getResource().getPath();
        pageTitleCountrySuffix = StringUtils.EMPTY;
        if(baiduMapService.getBaiduMapKey() != null && path.contains("/cn")) {
            baiduMapkey = baiduMapService.getBaiduMapKey();
        }
        if(currentPage!=null){
            Page countryPage = PageUtil.getCountryPage(currentPage);
            if(StringUtils.isNotBlank(countryPage.getTitle())){
                pageTitleCountrySuffix = countryPage.getTitle();
            }
        }
    }

    /**
     * Gets the Baidu Map key values.
     *
     * @return the Baidu Map key values
     */
    public String getBaiduMapkey() {
        return baiduMapkey;
    }


    public String getPageTitleCountrySuffix() {
        return pageTitleCountrySuffix;
    }
}
