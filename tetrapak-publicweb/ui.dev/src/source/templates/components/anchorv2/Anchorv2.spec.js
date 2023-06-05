import Anchorv2 from './Anchorv2';
import $ from 'jquery';
import anchorMenuTemplate from '../../../test-templates-hbs/anchorv2.hbs';

describe('Anchorv2', function() {
  before(function() {
    $(document.body)
      .empty()
      .html(anchorMenuTemplate());
    this.Anchorv2 = new Anchorv2({
      el: document.body
    });
    this.clock = sinon.useFakeTimers();
    this.initSpy = sinon.spy(this.Anchorv2, 'init');
    this.scrollToSectionSpy = sinon.spy(this.Anchorv2, 'scrollToSection');
    this.onPageScrollSpy = sinon.spy(this.Anchorv2, 'onPageScroll');
    this.maxPrimaryLinksSpy = sinon.spy(this.Anchorv2, 'getMaxNumberOfPrimaryLinks');
    this.currentIndexOnLaunchSpy = sinon.spy(this.Anchorv2, 'setAnchorMenuSelection');
    this.resetOverflowMenuSpy = sinon.spy(this.Anchorv2, 'resetOverflowMenu');
    this.onMoreButtonClickSpy = sinon.spy(this.Anchorv2, 'onMoreButtonClick');
    this.toggleVisibilityOfItemsSpy = sinon.spy(this.Anchorv2, 'toggleVisibilityOfItems');    
    this.Anchorv2.init();
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
    expect(this.Anchorv2.init.called).to.be.true;
    done();
  });
  it('should check instance of component', function () {
    expect(this.Anchorv2).to.be.instanceOf(Anchorv2);
  });
  it('should return true if element is in view port', function(done) {
    const ele = document.getElementsByClassName('pw-anchor-menu-list');
    expect(this.Anchorv2.isElementInViewport(ele[0])).to.be.true;
    done();
  });
  it('should return max number of primary links', function(done) {
    this.Anchorv2.getMaxNumberOfPrimaryLinks();
    expect(this.Anchorv2.cache.$maxPrimaryLinks).to.equal(7);
    done();
  });
  it('should call getMaxNumberOfPrimaryLinks on window resize', function (done) {
    $(window).trigger('resize');
    expect(this.Anchorv2.getMaxNumberOfPrimaryLinks.called).to.be.true;
    done();
  });
  it('should call setAnchorMenuSelection on window resize', function (done) {
    $(window).trigger('resize');
    expect(this.Anchorv2.setAnchorMenuSelection.called).to.be.true;
    done();
  });
  it('getMaxNumberOfPrimaryLinks should return 5 if window size is > 1024 and < 1280', function(done) {
    window.outerWidth = 1034;
    this.Anchorv2.getMaxNumberOfPrimaryLinks();
    expect(this.Anchorv2.cache.$maxPrimaryLinks).to.equal(5);
    done();
  });
  it('getMaxNumberOfPrimaryLinks should return 7 if window size is > 1280', function(done) {
    window.outerWidth = 1284;
    this.Anchorv2.getMaxNumberOfPrimaryLinks();
    expect(this.Anchorv2.cache.$maxPrimaryLinks).to.equal(7);
    done();
  });
});