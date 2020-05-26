import Marketselector from './Marketselector';
import marketSelectorTemplate from '../../../test-templates-hbs/marketSelector.hbs';
import $ from 'jquery';

describe('Marketselector', function () {
  before(function () {
    $(document.body).empty().html(marketSelectorTemplate());
    this.marketselector = new Marketselector({
      el: document.body
    });
    this.initSpy = sinon.spy(this.marketselector, 'init');
    this.showPopupSpy = sinon.spy(this.marketselector, 'showPopup');
    this.hidePopUpSpy = sinon.spy(this.marketselector, 'hidePopUp');
    this.marketselector.init();
    this.marketselector.showPopup();
  });

  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.showPopupSpy.restore();
    this.hidePopUpSpy.restore();
  });

  it('should initialize', function (done) {
    expect(this.marketselector.init.called).to.be.true;
    done();
  });

  it('should open popup when globe button is clicked', function (done) {
    // $('.js-header__selected-lang-pw').trigger('click');
    expect(this.marketselector.showPopup.called).to.be.true;
    done();
  });

  it('should close popup when close button is clicked', function (done) {
    $('.js-close-btn').trigger('click');
    expect(this.marketselector.hidePopUp.called).to.be.true;
    done();
  });
});
