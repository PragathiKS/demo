import $ from 'jquery';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import TextImage from './TextImage';

describe('TextImage', function () {
  before(function () {
    $(document.body).empty().html('<a class="TextImage js-textImage-analytics">Image Text Button</a>');
    this.TextImage = new TextImage({
      el: document.body
    });
    this.initSpy = sinon.spy(this.TextImage, 'init');
    this.analyticsSpy = sinon.spy(this.TextImage, 'trackAnalytics');
    window.digitalData = {};
    window._satellite = {
      track() { /* Dummy method */ }
    };
    this.TextImage.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.analyticsSpy.restore();
  });
  it('should initialize', function () {
    expect(this.initSpy.called).to.be.true;
  });
  it('should track analytics on click of "TextImage" button', function () {
    $('.js-textImage-analytics').trigger('click');
    expect(this.analyticsSpy.called).to.be.true;
  });
})