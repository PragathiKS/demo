package com.tetrapak.customerhub.core.mock;


import com.tetrapak.customerhub.core.services.PowerBiReportService;

public class MockPowerBiReportServiceImpl implements PowerBiReportService {
    


    public String getPbiServiceUrl() {

        return  "https://login.microsoftonline.com/";
    }
     /**
     * @return the PowerBI Resource URL
     */
    
    public String getPbiResourceUrl() {

        return  "https://analysis.windows.net/powerbi/api";
    }

    /**
     * @return the PowerBI Embedtoken URL
     */
    
    public String getPbiEmbedtokenUrl() {

        return  "https://api.powerbi.com/v1.0/myorg/groups/";
    }

    /**
     * @return the azureID tenantid
     */
    
    public String getAzureidtenantid() {
        return  "https://api.powerbi.com/v1.0/myorg/groups/";
    }

    /**
     * @return the powerbi clientid
     */
    
    public String getPbicid() {
        return  "4f0f766b-d2eb-4a50-be2a-52ff8e1657b7";
    }

    /**
     * @return the powerbi client sceret
     */
    
    public String getPbics() {
        return  "zYs8Q~OvTfJsJsqGoQmxBWzfw6V2s-z8WpJCwcBW";
    }

    /**
     * @return the powerbi datasetid
     */
    
    public String getPbidatasetid() {
        return  "5b01daa5-56c1-46f8-94e8-a18f20cc0fb7";
    }

    /**
     * @return the powerbi workspaceid
     */
    
    public String getPbiworkspaceid() {
        return  "17884c21-49de-42ba-8519-dea43237c2df";
    }
   @Override    
   public String getPbireportid() {
        return  "5d3c5f2a-b062-4ddb-ab09-4224fb845a99";
    }
   /**
    * @return getGenerateEmbedToken
    */
   @Override
   public String getGenerateEmbedToken() {
        return  "H4sIAAAAAAAEAB2Uta70ZgBE3-W2G8lMkf7CuGZac2daM-PnKO-eq_SnmTOj-efHTkE_pcXP3z_wRbbaubpwfl-e7RVGT8Em0PUOkfYZRPVMGxiaYfhiMe5kpbQyHXdX4pf9esxKJY-wWKvXPXKBb-OAWj_jQUpLdNx9fqtAdOISWbd7VWibTQAbKhgcmUBLeghKSZKlKe8dKnLA5s8i-P0akpPQ4ydxCQ0vL5epOeKbEDQOkrO60M1wYTilt9l-ezCNza5iPqBniParsSVhbZQK-TCaLojBbn4bcKuG6eK-jKAFs0UG_91KSsWwiBqbfjJgk3qp1jDvtssP2JdpXbtNOh5LpgJDzXVw2YaCL8jY34IVRuEUhSfOaOqtZ0dY-tXBM7T_zHVavNJINJt0i4vVx-MFM3exquBtHT-coVNXEX9cF74uKb9zj-555ZvNfih-fO9ZhkF69i0e1vIgb8PaawrgnRz65mlqYsZUTJgRicGTGjFFDt-mZHM9nIBa7u70qu7EEy6uW-lvaEsmOpqVkQS0peRihs-uU-YalEgxmRWk0lpxReN0-WKlXvNySw9kxoXocgwcGmZzNWF2a6SJc-RJWD-OXa-VhVkwXHTJ7DzuuYHHeN7MbLIH4QQk_BVF1p81hDMTPTLC6e0jlPTWxC5HqGt0r25YANsK4qsNNMWkwhk9RDs30NNobFlB9kRKrwqb6X4mLD-JbrxgQ5ebCpREhgY5q9BbLLlvrg2qSbcnxwjNP2kt4GI6pcFnKNC35UqazpO1TQaLXY0Cc8K94d5fuX7oIAx0j1JxTM6I0oVgd1qSmKu6Pz9__fArmPdJK8Hv9I14vfUxZF9TxU7kcxC6VxfuAa7-CVqllS5sZ3C2V8nxUIdFEnAwpTRsg3hzmkJPwLmk5v66t8_1_ZZRV75le1OgTjq_ztAxY4XLoRagpv4sHJkpqSGoFODLsDtIeRMaA22Th_sWpW2meMHsw9LzwcL69nkvWFxOdW7nixqvbIGmAGHeeB-X--bRzxYkwgJU_LhvN4snjCnnAhWIy5DkEohDCE0Ogem5DK6W4fAPHAXJR1akZUHqLxbr9NdKcyNN3u6IWO5vk3cmSwt0hQwGl7hsGjXZK-9y3tkCoVH_-aRKR1rDZFccHZOrDI8nx4XJ9DEgAf8WqeX2drVR40t6XFKU2T__awZzXa5K8Gu5W2eQQAb_GToTp-XG8Goodf6nPk01pvuxlr8Yy70MjXWM0HHa08UAcq586xUAJEGtv4cvqAFvttnQCLMevq6ucUNJV2tS0Mu2lsN8fk1CburDywmTF9h_Q7zjhNRco5Ws7o78fToFARJ05xu4QUJbUg47bOUn3O_jcG7uzF6twmMXj5hHpKSre5NAKYq_rhuRqIc065yKSEnSVw3jEV0uCZnQPHkM01TSUhc6pAY0s8yk9dLvG8J2OixLZj9dZoiEfsmnhVTILmS6UPNs9wmOxSF2Ydl3Lew-fYA9LocRgspGLyCgaGQEED_oQeNs2W4S_jvh5vjqWCa_79tREZXic8Y2DzYkLXqV3sfOsOcBWaE30KiGD4oN3EVXBuj61fzvfztcuR3uBQAA.eyJjbHVzdGVyVXJsIjoiaHR0cHM6Ly9XQUJJLU5PUlRILUVVUk9QRS1FLVBSSU1BUlktcmVkaXJlY3QuYW5hbHlzaXMud2luZG93cy5uZXQiLCJleHAiOjE2ODk5MzU5NjYsImFsbG93QWNjZXNzT3ZlclB1YmxpY0ludGVybmV0Ijp0cnVlfQ==";
    }

      /*  @Override
   public String getEmbedURL() {
        return  "https://app.powerbi.com/reportEmbed?reportId=5d3c5f2a-b062-4ddb-ab09-4224fb845a99";
    }*/
}
