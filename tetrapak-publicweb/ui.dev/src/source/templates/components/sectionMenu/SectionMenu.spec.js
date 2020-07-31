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
    this.openStub = sinon.stub(window, 'open');
    this.handleSectionMenuItemMouseOverSpy = sinon.spy(this.sectionMenu, 'handleSectionMenuItemMouseOver');
    this.handleSectionMenuItemMouseOutSpy = sinon.spy(this.sectionMenu, 'handleSectionMenuItemMouseOut');
    this.handleSectionMenuClickSpy = sinon.spy(this.sectionMenu, 'handleSectionMenuClick');
    this.handleSubSectionMenuClickSpy = sinon.spy(this.sectionMenu, 'handleSubSectionMenuClick');
    this.sectionMenu.init();
  });
  after(function() {
    $(document.body).empty();
    this.initSpy.restore();
    this.handleSectionMenuItemMouseOverSpy.restore();
    this.handleSectionMenuItemMouseOutSpy.restore();
    this.handleSectionMenuClickSpy.restore();
    this.openStub.restore();
    this.handleSubSectionMenuClickSpy.restore();
  });
  it('should initialize', function(done) {
    expect(this.sectionMenu.init.called).to.be.true;
    done();
  });
  it('should call handleSectionMenuItemMouseOver on mouseover', function (done) {
    $('.js-section-menu-navigation-Link').trigger('mouseover');
    expect(this.sectionMenu.handleSectionMenuItemMouseOver.called).to.be.true;
    done();
  });

  it('should call handleSectionMenuItemMouseOut on mouseout', function (done) {
    $('.js-section-menu-navigation-Link').trigger('mouseout');
    expect(this.sectionMenu.handleSectionMenuItemMouseOut.called).to.be.true;
    done();
  });
  it('should call handleSectionMenuClick on click', function (done) {
    $('.js-section-menu-item-link').trigger('click');
    expect(this.sectionMenu.handleSectionMenuClick.called).to.be.true;
    done();
  });
  it('should call handleSubSectionMenuClick on click', function (done) {
    $('.js-sub-menu-navigation-link-item').trigger('click');
    expect(this.sectionMenu.handleSubSectionMenuClick.called).to.be.true;
    done();
  });
});
