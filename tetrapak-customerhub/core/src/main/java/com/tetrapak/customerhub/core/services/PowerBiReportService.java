
package com.tetrapak.customerhub.core.services;


public interface PowerBiReportService {

    String getPbiServiceUrl();

    String getPbiResourceUrl();

    String getPbiEmbedtokenUrl();
    
    String getAzureidtenantid();

    String getPbicid();

    String getPbics();
    
    String getPbidatasetid();
    
    String getPbireportid();
    
    String getPbiworkspaceid();

    public String getGenerateApi();

    public String getGenerateEmbedToken(String accessToken);

}