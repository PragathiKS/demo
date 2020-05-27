import $ from 'jquery';
import sectionMenuTemplate from '../../../test-templates-hbs/sectionMenu.hbs';
import SectionMenu from './SectionMenu';


describe('SectionMenu', function() {
  before(function() {
    this.enableTimeouts(false);
    $(document.body)
      .empty()
      .html(sectionMenuTemplate());
    this.sectionMenu = new SectionMenu({
      el: document.body
    });
    this.initSpy = sinon.spy(this.sectionMenu, 'init');
    this.handleSectionMenuItemMouseOverSpy = sinon.spy(this.sectionMenu, 'handleSectionMenuItemMouseOver');
    this.handleSectionMenuItemMouseOutSpy = sinon.spy(this.sectionMenu, 'handleSectionMenuItemMouseOut');
    this.sectionMenu.init();
  });
  after(function() {
    $(document.body).empty();
    this.initSpy.restore();
    this.handleSectionMenuItemMouseOverSpy.restore();
    this.handleSectionMenuItemMouseOutSpy.restore();
  });
  it('should initialize', function() {
    expect(this.sectionMenu.init.called).to.be.true;
  });
  it('should call handleSectionMenuItemMouseOver on mouseover', function () {
    $('.js-section-menu-navigation-Link').trigger('mouseover');
    expect(this.sectionMenu.handleSectionMenuItemMouseOver.called).to.be.true;
  });

  it('should call handleSectionMenuItemMouseOut on mouseout', function () {
    $('.js-section-menu-navigation-Link').trigger('mouseout');
    expect(this.sectionMenu.handleSectionMenuItemMouseOut.called).to.be.true;
  });
});
