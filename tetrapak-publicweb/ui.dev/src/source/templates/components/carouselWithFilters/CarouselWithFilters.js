import $ from 'jquery';
import 'bootstrap';
import { render } from '../../../scripts/utils/render';
import { GET_CAROUSEL_ITEM } from '../../../scripts/utils/constants';

class CarouselWithFilters {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$catFilterItem = this.root.find('.pw-carousel__filters .js-filter-category .dropdown-item');
    this.cache.$subcatFilterItem = this.root.find('.pw-carousel__filters .js-filter-subcategory .dropdown-item');
    this.cache.categoriesMap = this.root.data('categories');
  }
  renderPractice(subCategoryVal) {
    const productType = this.root.data('prodtype');
    const rootPath = this.root.data('rootpath');
    const btnVariant = this.root.data('buttontheme');
    render.fn({
      template: 'carouselItem',
      url: GET_CAROUSEL_ITEM,
      ajaxConfig: {
        method: 'GET',
        data: {
          productType,
          subCategoryVal,
          rootPath
        }
      },
      target: this.root.find('.js-carouselfiltered-item'),
      beforeRender(data) {
        if (data && data.length) {
          this.data = data[0];
          this.data.variant = btnVariant;
        }
      }
    });
  }
  renderSubcategories(catId) {
    this.root.find('.dropdown-category-wrapper').removeClass('show');
    this.root.find('.dropdown-category-wrapper .dropdown-item').removeClass('active');
    this.root.find(`.dropdown-category-wrapper[data-category="${catId}"]`).addClass('show');
    const $selectedSubcat = $(`.dropdown-category-wrapper[data-category="${catId}"] .dropdown-item:first`);
    $selectedSubcat.addClass('active');
    this.root.find('.subcategory__toggle').text($selectedSubcat.text());
    this.renderPractice($selectedSubcat.data('category'));
  }
  bindEvents() {
    /* Bind jQuery events here */
    this.cache.$catFilterItem.on('click', e => {
      e.preventDefault();
      const $target = $(e.target);
      const catId = $target.data('category');
      const catLabel = $target.text();
      this.root.find('.category__toggle').text(catLabel);
      this.root.find('.js-filter-category .active').removeClass('active');
      $target.addClass('active');
      this.renderSubcategories(catId);
    });

    this.cache.$subcatFilterItem.on('click', e => {
      e.preventDefault();
      const $target = $(e.target);
      const catId = $target.data('category');
      const catLabel = $target.text();
      this.root.find('.subcategory__toggle').text(catLabel);
      this.root.find('.js-filter-subcategory .active').removeClass('active');
      $(e.target).addClass('active');
      this.renderPractice(catId);
    });
  }
  initFilters() {
    const catId = this.root.find('.js-filter-category .active').data('category');
    const catLabel = this.root.find('.js-filter-category .active').text();
    this.root.find('.category__toggle').text(catLabel);
    this.renderSubcategories(catId);
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.initFilters();
  }
}

export default CarouselWithFilters;
