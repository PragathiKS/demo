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
  });
  it('should initialize', function () {
    expect(this.initSpy.called).to.be.true;
  });
  it('should go to top on click of "top" button', function () {
     $('#tp-pw-footer__link').trigger('click');
     expect(this.goToTopSpy.called).to.be.true;
  });
  it('should call track analytics on click', function () {
    $('.tp-pw-footer-data-analytics').trigger('click');
    expect(this.footer.trackAnalytics.called).to.be.true;
  });
  // it('should set digitalData after track analytics call', function() {
  //   expect(window.digitalData.linkClick.linkType).to.equal('internal');
  // });
});
