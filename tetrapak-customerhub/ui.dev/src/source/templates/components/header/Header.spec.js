import LeftNavigation from '../leftnavigation/LeftNavigation';
import $ from 'jquery';
import headerTemplate from '../../../test-templates-hbs/header.hbs';

describe('Header', function () {
  before(function () {
    $(document.body).empty().html(headerTemplate());
    this.leftNavigation = new LeftNavigation({ el: document.body });
    this.initSpy = sinon.spy(this.leftNavigation, 'init');
    this.openSideNavSpy = sinon.spy(this.leftNavigation, 'openSideNav');
    this.leftNavigation.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.openSideNavSpy.restore();
  });
  it('should initialize', function () {
    expect(this.leftNavigation.init.called).to.be.true;
  });
  it('should open left navigation on click of burger menu icon', function () {
    $(document.body).trigger('showLeftNav');
    expect(this.leftNavigation.openSideNav.called).to.be.true;
  });
});
