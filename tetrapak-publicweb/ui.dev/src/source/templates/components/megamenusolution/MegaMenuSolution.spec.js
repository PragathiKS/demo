import MegaMenuSolution from './MegaMenuSolution';
import $ from 'jquery';
import megamenuTemplate from '../../../test-templates-hbs/megamenusolution.hbs';

describe('MegaMenuSolution', function () {
  before(function () {
    $(document.body).empty().html(megamenuTemplate());
    this.megaMenu = new MegaMenuSolution({
      el: document.body,
    });
    this.initSpy = sinon.spy(this.megaMenu, 'init');
    this.handleOpenEventSpy = sinon.spy(this.megaMenu, 'handleOpenEvent');
    this.handleCloseEventSpy = sinon.spy(this.megaMenu, 'handleCloseEvent');
    this.trackAnalyticsSpy = sinon.spy(this.megaMenu, 'trackAnalytics');
    this.mobileModeStub = sinon.stub(this.megaMenu, 'isMobileMode');
    this.mobileModeStub.returns(true);

    this.megaMenu.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.handleOpenEventSpy.restore();
    this.trackAnalyticsSpy.restore();
    this.mobileModeStub.restore();
  });
  it('should initialize', function () {
    expect(this.initSpy.called).to.be.true;
  });

  it('should call handleOpenEvent on click', function () {
    $('.js-open-menu').trigger('click');
    expect(this.megaMenu.handleOpenEvent.called).to.be.true;
  });

  it('should call handleCloseEvent on click', function () {
    $('.js-close-menu').trigger('click');
    expect(this.megaMenu.handleCloseEvent.called).to.be.true;
  });
  it('should call trackAnalytics on click', function () {
    $('.js-navigation-Link').trigger('click');
    expect(this.megaMenu.trackAnalytics.called).to.be.true;
  });
});
