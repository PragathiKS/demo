import $ from 'jquery';
import Footer from './Footer';
import footerTemplate from '../../../test-templates-hbs/footer.hbs';

describe('Footer', function () {
  before(function () {
    $(document.body).empty().html(footerTemplate());
    this.footer = new Footer({
      el: document.body
    });
    this.initSpy = sinon.spy(this.footer, 'init');
    this.trackAnalyticsSpy = sinon.spy(this.footer, 'trackAnalytics');
    this.goToTopSpy = sinon.spy(this.footer, 'goToTop');
    this.showPopupSpy = sinon.spy(this.footer, 'showPopup');
    this.openStub = sinon.stub(window, 'open');
    this.isExternalStub = sinon.stub(this.footer, 'isExternal');
    this.isExternalStub.returns(true);
    window.digitalData = {};
    window._satellite = {
      track() { /* Dummy method */ }
    };
    this.footer.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.goToTopSpy.restore();
    this.trackAnalyticsSpy.restore();
    this.openStub.restore();
    this.showPopupSpy.restore();
    this.isExternalStub.restore();
  });
  it('should initialize', function (done) {
    expect(this.footer.init.called).to.be.true;
    done();
  });
  it('should go to top on click of "top" button', function (done) {
     $('#tp-pw-footer__link').trigger('click');
     expect(this.footer.goToTop.called).to.be.true;
     done();
  });
  it('should call track analytics on click', function (done) {
    $('.tp-pw-footer-data-analytics').trigger('click');
    expect(this.footer.trackAnalytics.called).to.be.true;
    done();
  });
  it('should open modal', function (done) {
    $('.tp-pw-footer-data-analytics').click();
    expect(this.footer.showPopup.called).to.be.true;
    done();
  });
  it('should click of js-close-btn', function (done) {
    $('.js-close-btn').trigger('click');
    done();
  });
});
