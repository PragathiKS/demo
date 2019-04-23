package com.tetrapak.customerhub.core.mock;


import com.tetrapak.customerhub.core.services.DynamicMediaService;

public class MockDynamicMediaServiceImpl implements DynamicMediaService{


    @Override
    public String getImageServiceUrl() {
        return "http://s7g10.scene7.com/is/image";
    }
    
    @Override
    public String[] getDynamicMediaConfMap() {
        String[] a = {"[getstarted-desktop=1440\\,300,getstarted-mobileL=414\\,259\\,0.333\\,0\\,0.333\\,1,getstarted-mobileP=414\\,259\\,0.333\\,0\\,0.333\\,1]"};
        return a;
    }


    @Override
    public String getRootPath() {
        return "/tetrapak";
    }
    
}
