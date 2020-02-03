import ProductListing from './ProductListing';
import $ from 'jquery';
import productListingTemplate from '../../../test-templates-hbs/productListing.hbs';
import { ajaxWrapper } from '../../../scripts/utils/ajax';

describe('ProductListing', function () {
  const jqRef = {
    setRequestHeader() {
      // Dummy method
    }
  };
  function ajaxResponse(response) {
    const pr = $.Deferred();
    pr.resolve(response, 'success', jqRef);
    return pr.promise();
  }
  before(function () {
    window.digitalData = {};
    window._satellite = {
      track() { /* Dummy method */ }
    };
    $(document.body).empty().html(productListingTemplate());
    this.productListing = new ProductListing({ el: document.body });
    this.initSpy = sinon.spy(this.productListing, 'init');
    this.renderCardsSpy = sinon.spy(this.productListing, 'renderCards');
    this.productCardOnClickFnSpy = sinon.spy(this.productListing, 'productCardOnClickFn');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse({}));
    this.productListing.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.renderCardsSpy.restore();
    this.productCardOnClickFnSpy.restore();
    this.ajaxStub.restore();
  });
  it('should initialize', function () {
    expect(this.productListing.init.called).to.be.true;
  });
  it('should render cards on click', function () {
    $('.pw-product-listing .js-product-listng-tab').trigger('click');
    expect(this.productListing.renderCards.called).to.be.true;
  });
  it('should render cards on dropdown change', function () {
    $('.js-pw-product-listing__navigation__dropdown').trigger('change');
    expect(this.productListing.renderCards.called).to.be.true;
  });
  it('should track analytics on click of product card grid item', function () {
    $('.pw-product-card-grid__item__link').trigger('click');
    expect(this.productCardOnClickFnSpy.called).to.be.true;
  });
});
