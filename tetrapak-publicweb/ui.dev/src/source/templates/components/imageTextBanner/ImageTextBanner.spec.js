import $ from 'jquery';
import ImageTextBanner from './ImageTextBanner';

describe('ImageTextBanner', function () {
  before(function () {
    $(document.body).empty().html(`
      <button class="opensoftc">Open soft conversion modal</button>
      <button class="itblink">Analytics</button>
    `);
    this.imageTextBanner = new ImageTextBanner({
      el: document.body
    });
    this.initSpy = sinon.spy(this.imageTextBanner, 'init');
    this.softConversionFlowSpy = sinon.spy(this.imageTextBanner, 'softConversionFlow');
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
    this.softConversionFlowSpy.restore();
    this.analyticsSpy.restore();
  });
  it('should initialize', function () {
    expect(this.initSpy.called).to.be.true;
  });
  it('should open soft conversion modal on click of ".opensoftc" button', function () {
    $('.opensoftc').trigger('click');
    expect(this.softConversionFlowSpy.called).to.be.true;
  });
  it('should track analytics on click of "itblink" link', function () {
    $('.itblink').trigger('click');
    expect(this.analyticsSpy.called).to.be.true;
  });
});
