import TextVideo from './TextVideo';
import $ from 'jquery';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import textVideoTemplate from '../../../test-templates-hbs/textVideo.hbs';

describe('TextVideo', function () {
  before(function () {
    $(document.body).empty().html(textVideoTemplate());
    this.textVideo = new TextVideo({ el: document.body });
    this.initSpy = sinon.spy(this.textVideo, 'init');
    this.trackAnalyticsSpy = sinon.spy(this.textVideo, 'trackAnalytics');
    window.digitalData = {};
    window._satellite = {
      track() { /* Dummy method */ }
    };
    this.textVideo.init();

  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.trackAnalyticsSpy.restore();
  });
  it('should initialize', function () {
    expect(this.textVideo.init.called).to.be.true;
  });
  
  it('should call track analytics on click', function () {
    $('.js-textVideo-analytics').trigger('click');
    expect(this.textVideo.trackAnalytics.called).to.be.true;
  });

  it('should set digitalData after track analytics call', function() {
    expect(window.digitalData.linkClick.linkType).to.equal('internal');
  });

});
