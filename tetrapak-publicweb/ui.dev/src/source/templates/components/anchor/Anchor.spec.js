import Anchor from './Anchor';
import $ from 'jquery';
import anchorTemplate from '../../../test-templates-hbs/anchor.hbs';

describe('Anchor', function() {
  before(function() {
    $(document.body)
      .empty()
      .html(anchorTemplate());
    this.Anchor = new Anchor({
      el: document.body
    });
    this.initSpy = sinon.spy(this.Anchor, 'init');
    this.trackAnalyticsSpy = sinon.spy(this.Anchor, 'trackAnalytics');
    this.Anchor.init();
  });

  after(function() {
    $(document.body).empty();
    this.initSpy.restore();
    this.trackAnalyticsSpy.restore();
  });

  it('should initialize', function() {
    expect(this.Anchor.init.called).to.be.true;
  });
  it('should call track analytics on click', function() {
    $('a').trigger('click');
    expect(this.Anchor.trackAnalytics.called).to.be.true;
  });
});
