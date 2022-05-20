import $ from 'jquery';
import TextImage from './TextImage';

describe('TextImage', function () {
  before(function () {
    this.enableTimeouts(false);
    $(document.body).empty().html('<div class="pw-text-image__title"><div class="js-softconversion-pw" /><h1></h1><a class="TextImage js-textImage-analytics">Image Text Button</a></div>');
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
  it('should initialize', function (done) {
    expect(this.textImage.init.called).to.be.true;
    done();
  });
  it('should track analytics on click of "TextImage" button', function (done) {
    $('.js-textImage-analytics').trigger('click');
    expect(this.textImage.trackAnalytics.called).to.be.true;
    done();
  });
  it('check for h2 tag', function (done) {
    $('h1').remove();
    this.textImage.init();
    done();
  });
})
