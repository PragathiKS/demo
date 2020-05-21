import Anchor from './Anchor';
import $ from 'jquery';
import anchorTemplate from '../../../test-templates-hbs/anchor.hbs';
import { scrollToElement } from '../../../scripts/common/common';
import { trackAnalytics } from '../../../scripts/utils/analytics';

describe('Anchor', function() {
  before(function() {
    $(document.body)
      .empty()
      .html(anchorTemplate());
    this.Anchor = new Anchor({
      el: document.body
    });
    this.initSpy = sinon.spy(this.Anchor, 'init');
    this.scrollToSectionSpy = sinon.spy(this.Anchor, 'scrollToSection');
    this.Anchor.init();
  });

  after(function() {
    $(document.body).empty();
    this.initSpy.restore();
    this.scrollToSectionSpy.restore();
  });

  it('should initialize', function() {
    expect(this.Anchor.init.called).to.be.true;
  });
  it('should call scrollToSection on click', function() {
    $('a').trigger('click');
    expect(this.Anchor.scrollToSection.called).to.be.true;
  });
});
