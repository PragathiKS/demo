package com.tetrapak.customerhub.core.services.impl;
import com.tetrapak.customerhub.core.services.PowerBiReportService;
import com.tetrapak.customerhub.core.services.config.PowerBiReportConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;

@Component(immediate = true, service = PowerBiReportService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = PowerBiReportConfig.class)
public class PowerBiReportServiceImpl implements PowerBiReportService {
    private PowerBiReportConfig config;
    /**
     * activate method
     *
     * @param config PowerBi Report configuration
     */
    @Activate
    public void activate(PowerBiReportConfig config) {
        this.config = config;
    }
      /**
     * @return the PowerBI Service URL
     */
    @Override
    public String getPbiServiceUrl() {

        return config.pbiserviceurl();
    }
    /**
     * @return the azureID tenantid
     */
    @Override
    public String getAzureidtenantid() {
        return config.azureidtenantid();
    }
    /**
     * @return the powerbi clientid
     */
    @Override
    public String getPbicid() {
        return config.pbicid();
    }
    /**
     * @return the powerbi client sceret
     */
    @Override
    public String getPbics() {
        return config.pbics();
    }
    /**
     * @return the  powerbi datasetid
     */
    @Override
    public String getPbidatasetid() {
        return config.pbidatasetid();
    }
      /**
     * @return the powerbi reportid
     */
    @Override
    public String getPbireportid() {
        return config.pbireportid();
    }

      /**
     * @return the powerbi workspaceid
     */
    @Override
    public String getPbiworkspaceid() {
        return config.pbiworkspaceid();
    }


}
