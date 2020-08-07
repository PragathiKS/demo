import $ from 'jquery';
import Marketselector from './Marketselector';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import marketSelectorTemplate from '../../../test-templates-hbs/marketSelector.hbs';

describe('Marketselector', function () {
  before(function () {
    $(document.body).empty().html(marketSelectorTemplate());
    this.marketselector = new Marketselector({
      el: document.body
    });
    this.initSpy = sinon.spy(this.marketselector, 'init');
    this.hidePopUpSpy = sinon.spy(this.marketselector, 'hidePopUp');
    this.trackAnalyticsSpy = sinon.spy(this.marketselector, 'trackMarketSelectorAnalytics');
    this.openStub = sinon.stub(window, 'open');
    this.marketselector.init();
    this.marketselector.showPopup();

  });

  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.hidePopUpSpy.restore();
    this.trackAnalyticsSpy.restore();
    this.openStub.restore();
  });

  it('should initialize', function (done) {
    expect(this.marketselector.init.called).to.be.true;
    done();
  });
  it('should call track analytics on click', function (done) {
    $('.js-lang-selector__btn > a').trigger('click');
    expect(this.marketselector.trackMarketSelectorAnalytics.called).to.be.true;
    done();
  });

  it('should close popup when close button is clicked', function (done) {
    $('.js-close-btn').trigger('click');
    expect(this.marketselector.hidePopUp.called).to.be.true;
    done();
  });
});
