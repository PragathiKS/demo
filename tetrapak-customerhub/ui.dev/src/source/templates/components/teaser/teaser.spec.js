import $ from 'jquery';
import Teaser from './teaser';
import teaserTemplate from '../../../test-templates-hbs/teaser.hbs';

describe('Teaser', function () {
  before(function () {
    $(document.body).empty().html(
      teaserTemplate());
    this.teaser = new Teaser({
      el: document.body
    });
    this.initSpy = sinon.spy(this.teaser, 'init');
    this.analyticsSpy = sinon.spy(this.teaser, 'trackAnalytics');
    this.onInitializedSpy = sinon.spy(this.teaser, 'onInitialized');
    this.onResizeSpy = sinon.spy(this.teaser, 'onResize');
    this.openStub = sinon.stub(window, 'open');
    this.teaser.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.analyticsSpy.restore();
    this.onInitializedSpy.restore();
    this.onResizeSpy.restore();
    this.openStub.restore();
  });
  it('should initialize', function (done) {
    expect(this.teaser.init.called).to.be.true;
    done();
  });
  it('should track analytics on click of "teaser" button', function (done) {
    $('.js-teaser-analytics').trigger('click');
    expect(this.teaser.trackAnalytics.called).to.be.true;
    done();
  });
})
