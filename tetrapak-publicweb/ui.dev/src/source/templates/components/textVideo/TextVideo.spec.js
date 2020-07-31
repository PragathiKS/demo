import $ from 'jquery';
import TextVideo from './TextVideo';
import textVideoTemplate from '../../../test-templates-hbs/textVideo.hbs';

describe('TextVideo', function () {
  before(function () {
    this.enableTimeouts(false);
    $(document.body).empty().html(textVideoTemplate());
    this.textVideo = new TextVideo({ el: document.body });
    this.initSpy = sinon.spy(this.textVideo, 'init');
    this.trackAnalyticsSpy = sinon.spy(this.textVideo, 'trackAnalytics');
    this.openStub = sinon.stub(window, 'open');
    window._satellite = {
      track() { /* Dummy method */ }
    };
    this.textVideo.init();

  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.trackAnalyticsSpy.restore();
    this.openStub.restore();
  });
  it('should initialize', function (done) {
    expect(this.textVideo.init.called).to.be.true;
    done()
  });

  it('should call track analytics on click', function (done) {
    // eslint-disable-next-line no-console
    console.log('text video test cases called13');
    $('.js-textVideo-analytics').trigger('click');
    expect(this.textVideo.trackAnalytics.called).to.be.true;
    done();
  });

});
