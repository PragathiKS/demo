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
    this.clock = sinon.useFakeTimers();
    this.initSpy = sinon.spy(this.AnchorMenu, 'init');
    this.scrollToSectionSpy = sinon.spy(this.AnchorMenu, 'scrollToSection');
    this.onPageScrollSpy = sinon.spy(this.AnchorMenu, 'onPageScroll');
    this.maxPrimaryLinksSpy = sinon.spy(this.AnchorMenu, 'getMaxNumberOfPrimaryLinks');
    this.currentIndexOnLaunchSpy = sinon.spy(this.AnchorMenu, 'setAnchorMenuSelection');
    this.resetOverflowMenuSpy = sinon.spy(this.AnchorMenu, 'resetOverflowMenu');
    this.onMoreButtonClickSpy = sinon.spy(this.AnchorMenu, 'onMoreButtonClick');
    this.toggleVisibilityOfItemsSpy = sinon.spy(this.AnchorMenu, 'toggleVisibilityOfItems');    
    this.AnchorMenu.init();
  });

  after(function() {
    $(document.body).empty();
    this.initSpy.restore();
    this.scrollToSectionSpy.restore();
    this.onPageScrollSpy.restore();
    this.clock.restore();
    this.maxPrimaryLinksSpy.restore();
    this.currentIndexOnLaunchSpy.restore();
    this.resetOverflowMenuSpy.restore();
    this.onMoreButtonClickSpy.restore();
    this.toggleVisibilityOfItemsSpy.restore();
  });

  it('should initialize', function(done) {
    expect(this.AnchorMenu.init.called).to.be.true;
    done();
  });
  it('should check instance of component', function () {
    expect(this.AnchorMenu).to.be.instanceOf(AnchorMenu);
  });
  it('should return true if element is in view port', function(done) {
    const ele = document.getElementsByClassName('pw-anchor-menu-list');
    expect(this.AnchorMenu.isElementInViewport(ele[0])).to.be.true;
    done();
  });
  it('should return max number of primary links', function(done) {
    this.AnchorMenu.getMaxNumberOfPrimaryLinks();
    expect(this.AnchorMenu.cache.$maxPrimaryLinks).to.equal(7);
    done();
  });
  it('should call getMaxNumberOfPrimaryLinks on window resize', function (done) {
    $(window).trigger('resize');
    expect(this.AnchorMenu.getMaxNumberOfPrimaryLinks.called).to.be.true;
    done();
  });
  it('should call setAnchorMenuSelection on window resize', function (done) {
    $(window).trigger('resize');
    expect(this.AnchorMenu.setAnchorMenuSelection.called).to.be.true;
    done();
  });
  it('getMaxNumberOfPrimaryLinks should return 5 if window size is > 1024 and < 1280', function(done) {
    window.outerWidth = 1034;
    this.AnchorMenu.getMaxNumberOfPrimaryLinks();
    expect(this.AnchorMenu.cache.$maxPrimaryLinks).to.equal(5);
    done();
  });
  it('getMaxNumberOfPrimaryLinks should return 7 if window size is > 1280', function(done) {
    window.outerWidth = 1284;
    this.AnchorMenu.getMaxNumberOfPrimaryLinks();
    expect(this.AnchorMenu.cache.$maxPrimaryLinks).to.equal(7);
    done();
  });
});