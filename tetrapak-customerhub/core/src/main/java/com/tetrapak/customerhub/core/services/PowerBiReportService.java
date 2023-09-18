
package com.tetrapak.customerhub.core.services;


public interface PowerBiReportService {

    /**
     * @return getPbireportid
     */
    String getPbireportid();
    
    /**
     * @return getGenerateEmbedToken
     */
    public String getGenerateEmbedToken(String reportId);

    public String getEmbedTokenBasedOnBP(String bpNumber, String reportId);

}