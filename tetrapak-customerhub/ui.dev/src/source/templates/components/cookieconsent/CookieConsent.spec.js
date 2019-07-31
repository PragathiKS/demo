import $ from 'jquery';
import CookieConsent from './CookieConsent';
import cookieConsentHtml from '../../../test-templates-hbs/cookieConsent.hbs';

describe('CookieConsent', function () {
  before(function () {
    $(document.body).empty().html(cookieConsentHtml());
    this.cookieConsent = new CookieConsent({
      el: $('.js-cookie-consent')
    });
    this.initSpy = sinon.spy(this.cookieConsent, 'init');
    this.removeBannerSpy = sinon.spy(this.cookieConsent, 'removeBanner');
    this.cookieConsent.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.removeBannerSpy.restore();
  });
  it('should initialize', function (done) {
    expect(this.initSpy.called).to.be.true;
    done();
  });
  it('should dismiss cookie banner on click of OK button', function (done) {
    $('.js-cookie-consent__btn').trigger('click');
    $('.js-cookie-consent').trigger('transitionend');
    expect(this.removeBannerSpy.called).to.be.true;
    done();
  });
});
