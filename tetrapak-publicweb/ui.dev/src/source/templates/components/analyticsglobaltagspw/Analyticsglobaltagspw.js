import $ from 'jquery';
import { storageUtil } from '../../../scripts/common/common';
class Analyticsglobaltagspw {
  constructor({ el }) {
    this.root = $(el);
  }
  initCache() {
    /* Initialize selector cache here */
    /**
     * Use "this.root" to find elements within current component
     * Example:
     * this.cache.$submitBtn = this.root.find('.js-submit-btn');
     */
    const cookieValue=this.getCookies('lang-code');
    this.setUserinfo(cookieValue);

    if(window.digitalData.error && window.digitalData.error.errorcode) {
      const oldPageName = window.digitalData.pageinfo.pageName;
      window.digitalData.pageinfo.pageName = `${oldPageName}:${document.location.pathname}`;
    }
    if(window.digitalData.productInfo && window.digitalData.productInfo.productName) {
      window.digitalData['event'] = {
        event:'product explorer page',
        eventType:'content-load'
      };
    }

    if(!storageUtil.get('gdprCookie')) {
      window.digitalData.cookie={
        cookieConsent:'no'
      };
    }else {
      window.digitalData.cookie={
        cookieConsent:'yes'
      };
    }

    window.digitalData.linkClick = {
      linkType:'',
      linkSection:'',
      linkParentTitle:'',
      linkName:''
    };
  }
  getCookies(){
    if (arguments.length > 0) {
      const key = arguments[0];
      const cookieString = decodeURIComponent(document.cookie);
      let allCookies = null;
      allCookies = cookieString.split(';');
      if (allCookies.length) {
        let returnValue = '';
        allCookies.forEach(c => {
          const keyIndexOf = c.indexOf((`${key}=`));
          if (keyIndexOf > -1) {
            returnValue = c.substring((keyIndexOf + (`${key}=`).length), c.length).trim();
          }
        });
        return returnValue;
      }
    }
  }
  setUserinfo(cookieValue){
    if(cookieValue!==''){
      window.digitalData.userinfo={
        userLanguage:cookieValue,
        loginStatus:'logged-in',
        userRole:'sales-manager',
        userType:'customer'
      };
    }
    else{
      window.digitalData.userinfo.userLanguage='';
      window.digitalData.userinfo.loginStatus='';
      window.digitalData.userinfo.userRole='';
      window.digitalData.userinfo.userType='';
    }
  }
  init() {
    /* Mandatory method */
    this.initCache();
  }
}

export default Analyticsglobaltagspw;
