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
    this.scrollToSectionSpy = sinon.spy(this.Anchor, 'scrollToSection');
    this.Anchor.init();
  });

  after(function() {
    $(document.body).empty();
    this.initSpy.restore();
    this.scrollToSectionSpy.restore();
  });

  it('should initialize', function(done) {
    expect(this.Anchor.init.called).to.be.true;
    done();
  });
  it('should call scrollToSection on click', function(done) {
    $('a').trigger('click');
    expect(this.Anchor.scrollToSection.called).to.be.true;
    done();
  });
});
