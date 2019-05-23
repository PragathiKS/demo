import $ from 'jquery';
import {render} from '../../../scripts/utils/render';
import {ajaxMethods, API_PRODUCT_LISTING} from '../../../scripts/utils/constants';

class ProductListing {
  constructor({el}) {
    this.root = $(el);
  }

  cache = {};

  initCache() {
    this.cache.initialCall = $('.js-product-listng-tab.tpatom-button--group-item--active', this.root).attr('data');
    this.cache.tabButtons = $('.js-product-listng-tab', this.root);
    this.cache.dropDown = $('.js-pw-product-listing__navigation__dropdown', this.root);
    this.cache.productRootPath = this.root.data('productRootPath');
    this.cache.digitalData = window.digitalData;
  }

  bindEvents() {
    /* Bind jQuery events here */
    this.renderCards();
    this.cache.tabButtons.on('click', (e) => {
      e.preventDefault();
      let $this = $(e.target);
      let category = $this.data('custom');
      this.cache.tabButtons.removeClass('tpatom-button--group-item--active');
      this.renderCards(category);
      $this.addClass('tpatom-button--group-item--active');
      if (this.cache.digitalData) {
        this.cache.digitalData.linkClick = {};
        this.cache.digitalData.compContentInfo = {};
        this.cache.digitalData.linkClick.linkType = 'internal';
        this.cache.digitalData.linkClick.linkSection = 'product category filter';
        this.cache.digitalData.linkClick.linkParentTitle = $this.closest('.pw-product-listing__navigation').prev().text().trim();
        this.cache.digitalData.linkClick.linkName = $this.text();
        if ($this.prev('.analytics-subcomponent-category-tag').val() !== undefined) {
          const temp = $this.prev('.analytics-subcomponent-category-tag').val().split(':');
          if (temp[0] && temp.length > 1) {
            this.cache.digitalData.compContentInfo[temp[0]] = temp.slice(1).join(':');
          }
        }
        if (typeof _satellite !== 'undefined') { //eslint-disable-line
          _satellite.track('linkClicked'); //eslint-disable-line
        }
      }
    });

    this.cache.dropDown.on('change', (e) => {
      e.preventDefault();
      let $this = $(e.target);
      let category = $this.val();
      this.renderCards(category);
    });
  }


  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }

  renderCards = (category) => {
    render.fn({
      template: 'productGrid',
      url: API_PRODUCT_LISTING,
      ajaxConfig: {
        method: ajaxMethods.GET,
        data: {
          productCategory: category || 'all',
          productRootPath: this.cache.productRootPath
        }
      },
      target: '.js-pw-product-listing__cards-container'
    });
  }
}

export default ProductListing;
