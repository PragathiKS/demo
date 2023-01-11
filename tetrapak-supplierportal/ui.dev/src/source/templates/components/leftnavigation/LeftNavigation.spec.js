import LeftNavigation from './LeftNavigation';
import $ from 'jquery';
import leftNavigationTemplate from '../../../test-templates-hbs/leftnavigation.hbs';

describe('LeftNavigation', function () {
  before(function () {
    $(document.body).empty().html(leftNavigationTemplate());
    this.leftNavigation = new LeftNavigation({ el: document.body });
    this.initSpy = sinon.spy(this.leftNavigation, 'init');
    this.closeSideNavSpy = sinon.spy(this.leftNavigation, 'closeSideNav');
    this.subsectionSpy = sinon.spy(this.leftNavigation, 'openSubMenu');
    this.hideAsideSpy = sinon.spy(this.leftNavigation, 'hideAside');
    this.eventOneStub = sinon.stub($.fn, 'one').callsArg(1);
    this.leftNavigation.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.closeSideNavSpy.restore();
    this.hideAsideSpy.restore();
    this.subsectionSpy.restore();
    this.eventOneStub.restore();
  });
  it('should initialize', function (done) {
    expect(this.leftNavigation.init.called).to.be.true;
    done();
  });

  it('should close left navigation on click of overlay icon', function (done) {
    $('.js-left-nav__overlay').trigger('click');
    expect(this.leftNavigation.closeSideNav.called).to.be.true;
    done();
  });

  it('should open/close side nav submenu sections', function (done) {
    $('.tpatom-list-item__btn').first().trigger('click'); // Open state
    this.leftNavigation.cache.anim = false;
    $('.tpatom-list-item__btn').first().trigger('click'); // Closed state
    expect(this.subsectionSpy.called).to.be.true;
    done();
  });

  it('should hide aside section', function (done) {
    $('.js-left-nav__overlay').trigger('transitionend');
    expect(this.leftNavigation.hideAside.called).to.be.true;
    done();
  });

});
