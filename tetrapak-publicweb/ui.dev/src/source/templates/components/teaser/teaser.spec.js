/* eslint-disable no-console */
import $ from 'jquery';
import Teaser from './teaser';
import { trackAnalytics } from '../../../scripts/utils/analytics';

describe('Teaser', function () {
  before(function () {
    $(document.body).empty().html('<a class="teaser js-teaser-analytics" href="https://loripsum.net/abc.pdf" data-link-section="Teaser_CTA_Download" data-download-type="download" data-asset-name="Asset">Teaser Button</a>');
    this.teaser = new Teaser({
      el: document.body
    });
    this.initSpy = sinon.spy(this.teaser, 'init');
    this.analyticsSpy = sinon.spy(this.teaser, 'trackAnalytics');
    this.openStub = sinon.stub(window, 'open');
    this.teaser.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.analyticsSpy.restore();
    this.openStub.restore();
  });
  it('should initialize', function () {
    expect(this.teaser.init.called).to.be.true;
  });
  it('should track analytics on click of "teaser" button', function () {
    $('.js-teaser-analytics').trigger('click');
    expect(this.teaser.trackAnalytics.called).to.be.true;
  });
})
