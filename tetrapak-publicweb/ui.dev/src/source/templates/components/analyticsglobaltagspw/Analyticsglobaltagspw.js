import $ from 'jquery';

class Analyticsglobaltagspw {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    /* Initialize selector cache here */
    /**
     * Use "this.root" to find elements within current component
     * Example:
     * this.cache.$submitBtn = this.root.find('.js-submit-btn');
     */
    const cookieValue=this.getCookies('lang-code');
    if(cookieValue!==''){
      window.digitalData.userinfo.userLanguage=cookieValue;
      window.digitalData.userinfo.loginStatus='logged-in';
      window.digitalData.userinfo.userRoles='sales-manager';
      window.digitalData.userinfo.userType='customer';
    }
    else{
      window.digitalData.userinfo.userLanguage='';
      window.digitalData.userinfo.loginStatus='';
      window.digitalData.userinfo.userRoles='';
      window.digitalData.userinfo.userType='';
    }
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
  bindEvents() {
    /* Bind jQuery events here */
    /**
     * Example:
     * const { $submitBtn } = this.cache;
     * $submitBtn.on('click', () => { ... });
     */
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default Analyticsglobaltagspw;
