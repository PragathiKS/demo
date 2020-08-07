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
  it('should initialize', function (done) {
  	document.cookie='lang-code=fr';
    expect(x.getCookies('lang-code')).to.equal('fr');
    document.cookie='';
    done();
  });
   it('should initialize', function (done) {
   	 document.cookie='lang-code=';
    expect(x.getCookies('')).to.equal('');
    done();
  });
 it('should initialize', function (done) {
    x.setUserinfo('test');
    expect(window.digitalData.userinfo.userLanguage).to.equal('test');
    done();
 });
  it('should initialize', function (done) {
    x.setUserinfo('');
    expect(window.digitalData.userinfo.userLanguage).to.equal('');
    done();
 });
});
