import LeftNavigation from '../leftnavigation/LeftNavigation';
import Header from './Header';
import $ from 'jquery';
import headerTemplate from '../../../test-templates-hbs/header.hbs';

describe('Header', function () {
  before(function () {
    $(document.body).empty().html(headerTemplate());
    this.header = new Header({ el: document.body });
    this.leftNavigation = new LeftNavigation({ el: document.body });
    this.initSpy = sinon.spy(this.leftNavigation, 'init');
    this.initHeaderSpy = sinon.spy(this.header, 'init');
    this.openSideNavSpy = sinon.spy(this.leftNavigation, 'openSideNav');
    this.leftNavigation.init();
    this.header.init();

  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.initHeaderSpy.restore();
    this.openSideNavSpy.restore();
  });
  it('should initialize', function () {
    expect(this.leftNavigation.init.called).to.be.true;
  });
  it('should initialize header', function () {
    expect(this.header.init.called).to.be.true;
  });
  it('should open left navigation on click of burger menu icon', function () {
    $('.tp-header__burger-menu').trigger('click');
    expect(this.leftNavigation.openSideNav.called).to.be.true;
  });
});
