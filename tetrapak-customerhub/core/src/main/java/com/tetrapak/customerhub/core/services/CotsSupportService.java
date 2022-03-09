package com.tetrapak.customerhub.core.services;

import com.tetrapak.customerhub.core.beans.aip.CotsSupportFormBean;
import org.apache.sling.api.SlingHttpServletRequest;

import java.util.List;
import java.util.Map;

/**
 * COTS Support Service
 */
public interface CotsSupportService {

    
    /**
     * 
     * Consolidates email data and initiates sling job to send email 
     * @param attachments
     * @param cotsSupportFormBean
     * @param request
     * @return status of operation
     */
    boolean sendEmail(List<Map<String,String>> attachments, CotsSupportFormBean cotsSupportFormBean,
            SlingHttpServletRequest request);
    
    
    /**
     * Create COTS Support form bean from request
     * @param request
     * @return
     */
    CotsSupportFormBean createCotsSupportFormBean(SlingHttpServletRequest request);
}
