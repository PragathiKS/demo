import RichText from './RichText';
import $ from 'jquery';
import richtextTemplate from '../../../test-templates-hbs/richtext.hbs';

describe('RichText', function () {
  before(function () {
    $(document.body).empty().html(richtextTemplate());
    this.richText = new RichText({
      el: document.body
    });
    this.initSpy = sinon.spy(this.richText, 'init');
    this.addIconSpy = sinon.spy(this.richText, 'addIcon');
    this.trackAnalyticsSpy = sinon.spy(this.richText, 'trackAnalytics');
    this.richText.init();
  });

  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.addIconSpy.restore();
    this.trackAnalyticsSpy.restore();
  });

  it('should initialize', function () {
    expect(this.richText.init.called).to.be.true;
  });
  it('should call addIcon', function () {
    expect(this.richText.addIcon.called).to.be.true;
  });
  it('should call track analytics on click', function () {
    $('a').trigger('click');
    expect(this.richText.trackAnalytics.called).to.be.true;
  });
});
