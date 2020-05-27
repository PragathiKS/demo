import $ from 'jquery';
import Marketselector from './Marketselector';
import marketSelectorTemplate from '../../../test-templates-hbs/marketSelector.hbs';

describe('Marketselector', function () {
  before(function () {
    $(document.body).empty().html(marketSelectorTemplate());
    this.marketselector = new Marketselector({
      el: document.body
    });
    this.initSpy = sinon.spy(this.marketselector, 'init');
    this.hidePopUpSpy = sinon.spy(this.marketselector, 'hidePopUp');
    this.marketselector.init();
    this.marketselector.showPopup();
  });

  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.hidePopUpSpy.restore();
  });

  it('should initialize', function () {
    expect(this.marketselector.init.called).to.be.true;
  });

  it('should close popup when close button is clicked', function () {
    $('.js-close-btn').trigger('click');
    expect(this.marketselector.hidePopUp.called).to.be.true;
  });
});
