import $ from 'jquery';
import TextImage from './TextImage';
import { getLinkClickAnalytics,addLinkAttr } from '../../../scripts/common/common';


describe('TextImage', function () {
  before(function () {
    this.enableTimeouts(false);
    $(document.body).empty().html('<a class="TextImage js-textImage-analytics">Image Text Button</a>');
    this.textImage = new TextImage({
      el: document.body
    });
    this.initSpy = sinon.spy(this.textImage, 'init');
    this.analyticsSpy = sinon.spy(this.textImage, 'trackAnalytics');
    window._satellite = {
      track() { /* Dummy method */ }
    };
    this.openStub = sinon.stub(window, 'open');
    this.textImage.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.analyticsSpy.restore();
    this.openStub.restore();
  });
  it('should initialize', function () {
    expect(this.textImage.init.called).to.be.true;
  });
  it('should track analytics on click of "TextImage" button', function () {
    $('.js-textImage-analytics').trigger('click');
    expect(this.textImage.trackAnalytics.called).to.be.true;
  });
})
