import LeftNavigation from './LeftNavigation';
import $ from 'jquery';
import leftNavigationTemplate from '../../../test-templates-hbs/leftnavigation.hbs';

describe('LeftNavigation', function () {
  before(function () {
    $(document.body).empty().html(leftNavigationTemplate());
    this.leftNavigation = new LeftNavigation({ el: document.body });
    this.initSpy = sinon.spy(this.leftNavigation, 'init');
    this.closeSideNavSpy = sinon.spy(this.leftNavigation, 'closeSideNav');
    this.leftNavigation.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.closeSideNavSpy.restore();
  });
  it('should initialize', function () {
    expect(this.leftNavigation.init.called).to.be.true;
  });
  it('should close left navigation on click of close icon', function () {
    $('.js-close-btn').trigger('click');
    expect(this.leftNavigation.closeSideNav.called).to.be.true;
  });

  it('should close left navigation on click of overlay icon', function () {
    $('.js-left-nav__overlay').trigger('click');
    expect(this.leftNavigation.closeSideNav.called).to.be.true;
  });
});
