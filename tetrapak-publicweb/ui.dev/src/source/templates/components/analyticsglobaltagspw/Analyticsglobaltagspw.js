import $ from 'jquery';
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
