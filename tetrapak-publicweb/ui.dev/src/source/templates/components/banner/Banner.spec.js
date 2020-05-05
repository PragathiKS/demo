import $ from 'jquery';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import Banner from './Banner';


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
  });
  it('should initialize', function () {
    expect(this.initSpy.called).to.be.true;
  });
  it('should track analytics on click of "itblink" link', function () {
    $('.js-banner-analytics').trigger('click');
    expect(this.analyticsSpy.called).to.be.true;
  });
});