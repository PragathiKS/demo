import $ from 'jquery';
import Banner from './Banner';
import bannerTemplate from '../../../test-templates-hbs/banner.hbs';

describe('Banner', function () {
  before(function () {
    $(document.body).empty().html(bannerTemplate());
    this.banner = new Banner({
      el: document.body
    });
    this.initSpy = sinon.spy(this.banner, 'init');
    this.analyticsSpy = sinon.spy(this.banner, 'trackAnalytics');
    this.trackBannerImageClickSpy = sinon.spy(this.banner, 'trackBannerImageClick');
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
    this.trackBannerImageClickSpy.restore();
    this.openStub.restore(); 
  });
  it('should initialize', function (done) {
    expect(this.initSpy.called).to.be.true;
    done();
  });
  it('should track analytics on click of "itblink" link', function (done) {
    $('.js-banner-analytics').trigger('click');
    expect(this.analyticsSpy.called).to.be.true;
    done();
  });
});