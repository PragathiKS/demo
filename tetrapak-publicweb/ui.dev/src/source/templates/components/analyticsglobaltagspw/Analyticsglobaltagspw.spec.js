import Analyticsglobaltagspw from './Analyticsglobaltagspw';

describe('Analyticsglobaltagspw', function () {
  let x=null;
  window.digitalData={
  	userinfo:{}
  };
  before(function () {
    x = new Analyticsglobaltagspw({ el: document.body });
    x.init();
  });
  it('should initialize', function () {
  	document.cookie='lang-code=fr';
    expect(x.getCookies('lang-code')).to.equal('fr');
    document.cookie='';
  });
   it('should initialize', function () {
   	 document.cookie='lang-code=';
    expect(x.getCookies('')).to.equal('');
  });
 it('should initialize', function () {
    x.setUserinfo('test');
    expect(window.digitalData.userinfo.userLanguage).to.equal('test');
 });
  it('should initialize', function () {
    x.setUserinfo('');
    expect(window.digitalData.userinfo.userLanguage).to.equal('');
 });
});
