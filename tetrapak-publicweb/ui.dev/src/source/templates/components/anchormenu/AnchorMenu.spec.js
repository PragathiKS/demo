import AnchorMenu from './AnchorMenu';
import $ from 'jquery';
import anchorMenuTemplate from '../../../test-templates-hbs/anchorMenu.hbs';

describe('AnchorMenu', function() {
  before(function() {
    $(document.body)
      .empty()
      .html(anchorMenuTemplate());
    this.AnchorMenu = new AnchorMenu({
      el: document.body
    });
    this.initSpy = sinon.spy(this.AnchorMenu, 'init');
    this.scrollToSectionSpy = sinon.spy(this.AnchorMenu, 'scrollToSection');
    this.AnchorMenu.init();
  });

  after(function() {
    $(document.body).empty();
    this.initSpy.restore();
    this.scrollToSectionSpy.restore();
  });

  it('should initialize', function(done) {
    expect(this.AnchorMenu.init.called).to.be.true;
    done();
  });
  it('should call scrollToSection on click', function(done) {
    $('a').trigger('click');
    expect(this.AnchorMenu.scrollToSection.called).to.be.true;
    done();
  });
});