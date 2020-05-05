import $ from 'jquery';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import Teaser from './teaser';

describe('Teaser', function () {
  before(function () {
    $(document.body).empty().html('<a class="teaser js-teaser-analytics">Teaser Button</a>');
    this.teaser = new Teaser({
      el: document.body
    });
    this.initSpy = sinon.spy(this.teaser, 'init');
    this.analyticsSpy = sinon.spy(this.teaser, 'trackAnalytics');
    window.digitalData = {};
    window._satellite = {
      track() { /* Dummy method */ }
    };
    this.teaser.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.analyticsSpy.restore();
  });
  it('should initialize', function () {
    expect(this.initSpy.called).to.be.true;
  });
  it('should track analytics on click of "teaser" button', function () {
    $('.js-teaser-analytics').trigger('click');
    expect(this.analyticsSpy.called).to.be.true;
  });
})