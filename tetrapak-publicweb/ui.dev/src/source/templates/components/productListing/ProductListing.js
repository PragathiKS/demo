import $ from 'jquery';
import { render } from '../../../scripts/utils/render';
import { ajaxMethods, API_PRODUCT_LISTING } from '../../../scripts/utils/constants';

class ProductListing {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.initialCall = this.root.find('.js-product-listng-tab.tpatom-button--group-item--active').attr('data');
    this.cache.tabButtons = this.root.find('.js-product-listng-tab');
    this.cache.dropDown = this.root.find('.js-pw-product-listing__navigation__dropdown');
    this.cache.productRootPath = this.root.data('productRootPath');
  }
  bindEvents() {
    const { tabButtons, dropDown } = this.cache;
    tabButtons.on('click', (e) => {
      e.preventDefault();
      const $this = $(e.target);
      const category = $this.data('custom');
      tabButtons.removeClass('tpatom-button--group-item--active');
      this.renderCards(category);
      $this.addClass('tpatom-button--group-item--active');
      if (window.digitalData) {
        $.extend(window.digitalData, {
          compContentInfo: {},
          linkClick: {
            linkType: 'internal',
            linkSection: 'product category filter',
            linkParentTitle: $.trim($this.closest('.pw-product-listing__navigation').prev().text()),
            linkName: $this.text()
          }
        });
        const subComponentCategoryTag = $this.prev('.analytics-subcomponent-category-tag').val();
        if (subComponentCategoryTag) {
          const categoryParts = subComponentCategoryTag.split(':');
          if (categoryParts[0] && categoryParts.length > 1) {
            window.digitalData.compContentInfo[categoryParts[0]] = categoryParts.slice(1).join(':');
          }
        }
        if (window._satellite) {
          window._satellite.track('linkClicked');
        }
      }
    });

    dropDown.on('change', (e) => {
      e.preventDefault();
      const $this = $(e.target);
      const category = $this.val();
      this.renderCards(category);
    });
  }

  productCardOnClickFn = (e) => {
    e.preventDefault();
    const $this = $(e.target);
    if (window.digitalData) {
      $.extend(window.digitalData, {
        linkClick: {
          linkType: 'internal',
          linkSection: 'category filtered products',
          linkParentTitle: $.trim($this.prevAll('.pw-product-card-grid__item__title').text()),
          linkName: $.trim($this.text()),
          contentName: $.trim(this.root.find('.js-product-listng-tab.tpatom-button--group-item--active').text())
        }
      });
      if (window._satellite) {
        window._satellite.track('linkClicked');
      }
    }
  };

  init() {
    this.initCache();
    this.renderCards();
    this.bindEvents();
  }

  renderCards = (category) => {
    const { productRootPath } = this.cache;
    render.fn({
      template: 'productGrid',
      url: API_PRODUCT_LISTING,
      ajaxConfig: {
        method: ajaxMethods.GET,
        data: {
          productCategory: category || 'all',
          productRootPath: productRootPath
        }
      },
      target: '.js-pw-product-listing__cards-container'
    }, () => {
      this.root.find('.pw-product-card-grid__item__link').on('click', this.productCardOnClickFn);
    });
  }
}

export default ProductListing;
