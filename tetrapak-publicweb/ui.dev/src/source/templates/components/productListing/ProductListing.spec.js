import ProductListing from './ProductListing';
import $ from 'jquery';
import productListingTemplate from '../../../test-templates-hbs/productListing.hbs';

describe('ProductListing', function () {
  before(function () {
    $(document.body).empty().html(productListingTemplate());
    this.productListing = new ProductListing({ el: document.body });
    this.initSpy = sinon.spy(this.productListing, 'init');
    this.renderCardsSpy = sinon.spy(this.productListing, 'renderCards');
    this.productListing.init();

  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.renderCardsSpy.restore();
  });
  it('should initialize', function () {
    expect(this.productListing.init.called).to.be.true;
  });
  it('should render cards on click', function () {
    $('.pw-product-listing .js-product-listng-tab').trigger('click');
    expect(this.productListing.renderCards.called).to.be.true;
  });
});
