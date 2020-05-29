import $ from 'jquery';
import Banner from './Banner';
import { trackAnalytics } from '../../../scripts/utils/analytics';


describe('Banner', function () {
  before(function () {
    $(document.body).empty().html(`
      <a class="js-banner-analytics">Analytics</a>
    `);
    this.banner = new Banner({
      el: document.body
    });
    this.initSpy = sinon.spy(this.banner, 'init');
    this.analyticsSpy = sinon.spy(this.banner, 'trackAnalytics');
    this.openStub = sinon.stub(window, 'open');
    window.digitalData = {};
    window._satellite = {
      track() { /* Dummy method */ }
    };
    this.banner.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.analyticsSpy.restore();
    this.openStub.restore();
  });
  it('should initialize', function () {
    expect(this.initSpy.called).to.be.true;
  });
  it('should track analytics on click of "itblink" link', function () {
    $('.js-banner-analytics').trigger('click');
    expect(this.analyticsSpy.called).to.be.true;
  });
});
