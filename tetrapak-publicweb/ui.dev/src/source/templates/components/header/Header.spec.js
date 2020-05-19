import Header from './Header';
import $ from 'jquery';
import headerTemplate from '../../../test-templates-hbs/header.hbs';
import { loc } from '../../../scripts/common/common';
import { trackAnalytics } from '../../../scripts/utils/analytics';

describe('Header', function () {
  before(function () {
    $(document.body).empty().html(headerTemplate());
    this.header = new Header({ el: document.body });
    this.initSpy = sinon.spy(this.header, 'init');
    this.openMobileMenuBoxToggleSpy = sinon.spy(this.header, 'openMobileMenuBoxToggle');
    this.hideMobileMenuOnResizeSpy = sinon.spy(this.header, 'hideMobileMenuOnResize');
    this.handleMouseOverSpy = sinon.spy(this.header, 'handleMouseOver');
    this.handleMouseOutSpy = sinon.spy(this.header, 'handleMouseOut');
    this.handleMenuClickSpy = sinon.spy(this.header, 'handleMenuClick');
    this.trackAnalyticsSpy = sinon.spy(this.header, 'trackAnalytics');
    this.replaceStub = sinon.stub(loc, 'replace');
    this.replaceStub.returns(true);
    window.digitalData = {};
    window._satellite = {
      track() { /* Dummy method */ }
    };
    this.header.init();

  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.openMobileMenuBoxToggleSpy.restore();
    this.hideMobileMenuOnResizeSpy.restore();
    this.handleMouseOverSpy.restore();
    this.handleMouseOutSpy.restore();
    this.handleMenuClickSpy.restore();

    this.trackAnalyticsSpy.restore();
    this.replaceStub.restore();
  });
  it('should initialize', function () {
    expect(this.header.init.called).to.be.true;
  });

  it('should open mobile menu box on click', function () {
    $('.js-tp-pw-header__hamburger').trigger('click');
    expect(this.header.openMobileMenuBoxToggle.called).to.be.true;
  });

  it('should close mobile menu on window resize', function() {
    $(window).trigger('resize');
    expect(this.header.hideMobileMenuOnResize.called).to.be.true;
  });

  it('should close mobile menu box on click', function () {
    this.header.toggleFlag = true;
    $('.js-tp-pw-header__hamburger').trigger('click');
    expect(this.header.openMobileMenuBoxToggle.called).to.be.true;
  });

  it('should call track analytics on click', function () {
    $('.js-tp-pw-header-logo-digital-data').trigger('click');
    expect(this.header.trackAnalytics.called).to.be.true;
  });

  it('should call handleMouseOver on mouseover', function () {
    $('.js-hover-menu-link').trigger('mouseover');
    expect(this.header.handleMouseOver.called).to.be.true;
  });

  it('should call handleMouseOut on mouseout', function () {
    $('.js-hover-menu-link').trigger('mouseout');
    expect(this.header.handleMouseOut.called).to.be.true;
  });

  it('should call handleMenuClick on click', function () {
    $('.js-click-menu-link').trigger('click');
    expect(this.header.handleMenuClick.called).to.be.true;
  });

});
