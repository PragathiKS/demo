import $ from 'jquery';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import MediaLink from './MediaLink';

describe('MediaLink', function () {
  before(function () {
    $(document.body).empty().html('<a href="#" class="MediaLink js-medialink-analytics">Link</a>');
    this.MediaLink = new MediaLink({
      el: document.body
    });
    this.initSpy = sinon.spy(this.MediaLink, 'init');
    this.analyticsSpy = sinon.spy(this.MediaLink, 'trackAnalytics');
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
  });
  it('should initialize', function () {
    expect(this.initSpy.called).to.be.true;
  });
  it('should track analytics on click of "MediaLink" button', function () {
    $('.js-medialink-analytics').trigger('click');
    expect(this.analyticsSpy.called).to.be.true;
  });
})