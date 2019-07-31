import TextVideo from './TextVideo';
import $ from 'jquery';
import textVideoHtml from '../../../test-templates-hbs/textVideo.hbs';

describe('TextVideo', function () {
  before(function () {
    $(document.body).empty().html(textVideoHtml());
    this.textVideo = new TextVideo({
      el: document.body
    });
    this.initSpy = sinon.spy(this.textVideo, 'init');
    this.analyticsSpy = sinon.spy(this.textVideo, 'trackVideoTextLinkAnalytics');
    this.textVideo.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.analyticsSpy.restore();
  });
  it('should initialize', function () {
    expect(this.initSpy.called).to.be.true;
  });
  it('should track analytics on click of link', function () {
    $('.js-text-video__description-link').trigger('click');
    expect(this.analyticsSpy.called).to.be.true;
  });
});
