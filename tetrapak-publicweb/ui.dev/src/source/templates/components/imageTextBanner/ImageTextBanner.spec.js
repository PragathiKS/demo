import $ from 'jquery';
import ImageTextBanner from './ImageTextBanner';

describe('ImageTextBanner', function () {
  before(function () {
    $(document.body).empty().html(`
      <a class="js-banner-analytics">Analytics</a>
    `);
    this.imageTextBanner = new ImageTextBanner({
      el: document.body
    });
    this.initSpy = sinon.spy(this.imageTextBanner, 'init');
    this.analyticsSpy = sinon.spy(this.imageTextBanner, 'trackAnalytics');
    window.digitalData = {};
    window._satellite = {
      track() { /* Dummy method */ }
    };
    this.imageTextBanner.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.analyticsSpy.restore();
  });
  it('should initialize', function () {
    expect(this.initSpy.called).to.be.true;
  });
  it('should track analytics on click of "itblink" link', function () {
    $('.js-banner-analytics').trigger('click');
    expect(this.analyticsSpy.called).to.be.true;
  });
});
