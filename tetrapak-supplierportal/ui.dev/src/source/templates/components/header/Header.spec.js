import LeftNavigation from '../leftnavigation/LeftNavigation';
import Header from './Header';
import $ from 'jquery';
import headerTemplate from '../../../test-templates-hbs/header.hbs';

describe('Header', function () {
  before(function () {
    $(document.body).empty().html(headerTemplate());
    this.header = new Header({ el: document.body });
    this.leftNavigation = new LeftNavigation({ el: document.body });
    this.initSpy = sinon.spy(this.header, 'init');
    this.initLeftNavigationSpy = sinon.spy(this.leftNavigation, 'init');
    this.openSideNavSpy = sinon.spy(this.leftNavigation, 'openSideNav');
    this.closeSideNavSpy = sinon.spy(this.leftNavigation, 'closeSideNav');
    this.leftNavigation.init();
    this.header.init();

  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.initLeftNavigationSpy.restore();
    this.openSideNavSpy.restore();
    this.closeSideNavSpy.restore();
  });

  it('should initialize LeftNavigation', function (done) {
    expect(this.leftNavigation.init.called).to.be.true;
    done();
  });
  it('should initialize', function (done) {
    expect(this.header.init.called).to.be.true;
    done();
  });
  it('should open left navigation on click of burger menu icon', function (done) {
    $('.tp-header__burger-menu').trigger('click');
    expect(this.leftNavigation.openSideNav.called).to.be.true;
    done();
  });
  it('should close left navigation on click of close menu icon', function (done) {
    $('.js-close-btn').trigger('click');
    expect(this.leftNavigation.closeSideNav.called).to.be.true;
    done();
  });
});
