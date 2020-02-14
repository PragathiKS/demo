import $ from 'jquery';
import ImageTextButton from './ImageTextButton';

describe('ImageTextButton', function () {
  before(function () {
    $(document.body).empty().html('<button class="imageTextButton">Image Text Button</button>');
    this.imageTextButton = new ImageTextButton({
      el: document.body
    });
    this.initSpy = sinon.spy(this.imageTextButton, 'init');
    this.analyticsSpy = sinon.spy(this.imageTextButton, 'trackAnalytics');
    window.digitalData = {};
    window._satellite = {
      track() { /* Dummy method */ }
    };
    this.imageTextButton.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.analyticsSpy.restore();
  });
  it('should initialize', function () {
    expect(this.initSpy.called).to.be.true;
  });
  it('should track analytics on click of "imageTextButton" button', function () {
    $('.imageTextButton').trigger('click');
    expect(this.analyticsSpy.called).to.be.true;
  });
});
