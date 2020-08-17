import $ from 'jquery';
import MediaLink from './MediaLink';
const filesaver = require('file-saver');

describe('MediaLink', function () {
  before(function () {
    $(document.body).empty().html('<div><a class="MediaLink js-medialink-analytics">Link<i class="icon icon-Download"></i></a><a class="MediaLink js-medialink-analytics">Link<i class="icon icon-Union"></i></a><div>');
    this.MediaLink = new MediaLink({
      el: document.body
    });
    this.initSpy = sinon.spy(this.MediaLink, 'init');
    this.analyticsSpy = sinon.spy(this.MediaLink, 'trackAnalytics');
    this.fileDownloadStub = sinon.stub(filesaver, 'saveAs');
    this.fileDownloadStub.returns(Promise.resolve('resolved'));
    window.digitalData = {};
    window._satellite = {
      track() { /* Dummy method */ }
    };
    this.MediaLink.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.analyticsSpy.restore();
    this.fileDownloadStub.restore();
  });
  it('should initialize', function (done) {
    expect(this.initSpy.called).to.be.true;
    done();
  });
  it('should track analytics on click of "MediaLink" button', function (done) {
    $('.js-medialink-analytics').attr('href','');
    $('.js-medialink-analytics').trigger('click');
    expect(this.analyticsSpy.called).to.be.true;
    done();
  });
})
