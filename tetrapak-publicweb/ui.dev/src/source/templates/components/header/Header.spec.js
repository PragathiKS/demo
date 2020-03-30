import Header from './Header';
import $ from 'jquery';
import headerTemplate from '../../../test-templates-hbs/header.hbs';
import { loc } from '../../../scripts/common/common';

describe('Header', function () {
  before(function () {
    $(document.body).empty().html(headerTemplate());
    this.header = new Header({ el: document.body });
    this.initSpy = sinon.spy(this.header, 'init');
    this.openMobileMenuBoxToggleSpy = sinon.spy(this.header, 'openMobileMenuBoxToggle');
    this.hideMobileMenuOnResizeSpy = sinon.spy(this.header, 'hideMobileMenuOnResize');
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

  it("should set digitalData after track analytics call", function() {
    expect(window.digitalData.linkClick.linkType).to.equal("internal");
  });

});
