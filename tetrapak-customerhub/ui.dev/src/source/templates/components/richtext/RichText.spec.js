import $ from 'jquery';
import RichText from './RichText';
import richtextTemplate from '../../../test-templates-hbs/richtext.hbs';
import { trackAnalytics } from '../../../scripts/utils/analytics';

describe('RichText', function () {
  before(function () {
    this.enableTimeouts(false);
    $(document.body).empty().html(richtextTemplate());
    this.richText = new RichText({
      el: document.body
    });
    this.initSpy = sinon.spy(this.richText, 'init');
    this.trackAnalyticsSpy = sinon.spy(this.richText, 'trackAnalytics');
    this.openStub = sinon.stub(window, 'open');
    this.richText.init();
  });

  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.trackAnalyticsSpy.restore();
    this.openStub.restore();
  });

  it('should initialize', function (done) {
    expect(this.richText.init.called).to.be.true;
    done();
  });
  it('should call track analytics on click', function (done) {
    $('a').trigger('click');
    expect(this.richText.trackAnalytics.called).to.be.true;
    done();
  });
});
