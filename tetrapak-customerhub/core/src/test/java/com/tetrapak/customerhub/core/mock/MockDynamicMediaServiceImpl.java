package com.tetrapak.customerhub.core.mock;


import com.tetrapak.customerhub.core.services.DynamicMediaService;

public class MockDynamicMediaServiceImpl implements DynamicMediaService{


    @Override
    public String getImageServiceUrl() {
        return "http://s7g10.scene7.com/is/image";
    }
    
    @Override
    public String[] getDynamicMediaConfMap() {
        String[] a = {"[getstarted-desktop=1440\\,300,getstarted-tabletL=1024\\,214,getstarted-tabletP=991\\,206,getstarted-mobileL=414\\,259\\,0.333\\,0\\,0.333\\,1,getstarted-mobileP=414\\,259\\,0.333\\,0\\,0.333\\,1,panel-mobileP=240\\,144\\,0\\,0.08\\,1\\,0.9,panel-desktop=150\\,172\\,0.24\\,0\\,0.58\\,1,keySpeaker-desktop=420\\,264\\,0\\,0\\,1\\,0.94,keySpeaker-mobileP=280\\,180\\,0\\,0\\,1\\,0.965,mainnavigation-desktop=192\\,108,mainnavigation-tabletL=192\\,108,mainnavigation-tabletP=192\\,108,eventListingTile-desktop=813\\,508,eventListingTile-tabletL=813\\,508,eventListingTile-mobileL=767\\,479,eventListingTile-mobileP=414\\,259,eventListingTile-tabletP=813\\,508,searchTile-desktop=240\\,110\\,0\\,0.133\\,1\\,0.733,searchTile-tabletL=240\\,110\\,0\\,0.133\\,1\\,0.733,searchTile-mobileL=574\\,263\\,0\\,0.133\\,1\\,0.733,searchTile-mobileP=292\\,134\\,0\\,0.133\\,1\\,0.733,searchTile-tabletP=240\\,110\\,0\\,0.133\\,1\\,0.733,eventRegister-desktop=320\\,200,eventRegister-tabletP=170\\,106,genericimage-desktopL-15:11=720\\,747\\,0\\,0.156\\,1\\,0.688,genericimage-desktop-15:11=720\\,528\\,0\\,0.256\\,1\\,0.486,genericimage-tabletP-15:11=384\\,740\\,0.108\\,0\\,0.782\\,1,genericimage-tabletL-15:11=512\\,430\\,0\\,0.222\\,1\\,0.556,genericimage-desktop-144:299=720\\,1495,genericimage-tabletP-144:299=496\\,950\\,0.6\\,0\\,0\\,0.95,genericimage-tabletL-144:299=512\\,1100\\,0.1\\,2\\,0.885\\,0,genericimage-desktop-144:217=720\\,1085,genericimage-tabletP-144:217=496\\,710\\,0.6\\,0\\,0\\,0.95,genericimage-tabletL-144:217=512\\,872\\,0.1\\,2\\,0.885\\,0,productLogo-desktop=100\\,50,productLogo-tabletL=100\\,50,productLogo-tabletP=100\\,50,productLogo-mobileL=100\\,50,productLogo-mobileP=100\\,50,downloadEventMaterial-desktop=103\\,149,downloadEventMaterial-tabletL=64\\,91,downloadEventMaterial-mobileL=75\\,106,modalSidebar-desktop=400\\,1440,medicinePack-desktop=240\\,150,medicinePack-tabletL=240\\,150,medicinePack-tabletP=240\\,150,medicinePack-mobileL=480\\,300,medicinePack-mobileP=280\\,165,medicinePackDetail-desktop=320\\,200,medicinePackDetail-tabletL=260\\,160,medicinePackDetail-tabletP=200\\,125,genericimage-desktop-2:1=360\\,180,genericimage-tabletL-2:1=360\\,180,genericimage-mobileL-2:1=200\\,100,errorComponent-desktop=1440\\,750,errorComponent-mobileL=767\\,550\\,0.087\\,0\\,0.726\\,0,errorComponent-mobileP=414\\,550\\,0.3\\,0.5\\,0.39\\,0,speakerBio-desktop-article=80\\,80\\,0.1\\,2.5\\,0.63\\,0,speakerBio-tabletL-article=70\\,70\\,0.2\\,2.5\\,0.63\\,0,speakerBio-tabletP-article=60\\,60\\,0.2\\,2.5\\,0.63\\,0,promoPanel-desktop=405\\,222\\,0\\,0.061\\,1\\,0.877,promoPanel-tabletL=405\\,222\\,0\\,0.063\\,1\\,0.873,promoPanel-tabletP=405\\,222\\,0\\,0.063\\,1\\,0.873,promoPanel-mobileL=240\\,131\\,0\\,0.063\\,1\\,0.873,promoPanel-mobileP=240\\,131\\,0\\,0.063\\,1\\,0.873,materialListingTile-desktop=420\\,200,sectionlisting-desktop=360\\,180,sectionlisting-mobilep=200\\,100,servicesSupport-desktop=60\\,60,resourceListingTile-desktop=420\\,263,resourceListingTile-mobileP=280\\,175]"};
        return a;
    }


    @Override
    public String getRootPath() {
        return "/tetrapak";
    }
    
}
