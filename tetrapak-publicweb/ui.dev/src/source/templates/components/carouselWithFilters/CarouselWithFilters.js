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
    let parentId = this.root.attr('id');
    this.cache.$catFilterItem = $('.pw-carousel__filters .js-filter-category .dropdown-item', '#'+parentId);
    this.cache.$subcatFilterItem = $('.pw-carousel__filters .js-filter-subcategory .dropdown-item', '#'+parentId);
    this.cache.categoriesMap = this.root.data('categories');
  }
  renderPractice (parentId, catId) {
    let productType = $('#'+parentId).data('prodtype');
    let rootPath = $('#'+parentId).data('rootpath');
    let btnVariant = $('#'+parentId).data('buttontheme');
    render.fn({
      template: 'carouselItem',
      url: GET_CAROUSEL_ITEM,
      ajaxConfig: {
        method: 'GET',
        data: {
          productType: productType,
          subCategoryVal: catId,
          rootPath: rootPath
        }
      },
      target: '#'+parentId+' .js-carouselfiltered-item',
      beforeRender(data) {
        if (data.length) {
          this.data = data[0];
          this.data.variant = btnVariant;
        }
      }
    });
  }
  renderSubcategories (parentId, catId) {
    $('.dropdown-category-wrapper', '#'+parentId).removeClass('show');
    $('.dropdown-category-wrapper .dropdown-item', '#'+parentId).removeClass('active');
    $('.dropdown-category-wrapper[data-category="'+catId+'"]', '#'+parentId).addClass('show');
    let $selectedSubcat = $('.dropdown-category-wrapper[data-category="'+catId+'"] .dropdown-item:first', '#'+parentId);
    $selectedSubcat.addClass('active');
    $('.subcategory__toggle', '#'+parentId).text($selectedSubcat.text());
    this.renderPractice(parentId, $selectedSubcat.data('category'));
  }
  bindEvents() {
    /* Bind jQuery events here */
    this.cache.$catFilterItem.click(e => {
      e.preventDefault();
      let parent = e.target.closest('.pw-carousel');
      let parentId = $(parent).attr('id');
      let catId = $(e.target).data('category');
      let catLabel = e.target.innerText;
      $('.category__toggle', '#'+parentId).text(catLabel);
      $('.js-filter-category .active', '#'+parentId).removeClass('active');
      $(e.target).addClass('active');
      this.renderSubcategories(parentId, catId);
    });

    this.cache.$subcatFilterItem.click( e => {
      e.preventDefault();
      let parent = e.target.closest('.pw-carousel');
      let parentId = $(parent).attr('id');
      let catId = $(e.target).data('category');
      let catLabel = e.target.innerText;
      $('.subcategory__toggle', '#'+parentId).text(catLabel);
      $('.js-filter-subcategory .active', '#'+parentId).removeClass('active');
      $(e.target).addClass('active');
      this.renderPractice(parentId, catId);
    });
  }
  initFilters() {
    let parentId = this.root.attr('id');
    let catId = $('.js-filter-category .active', '#'+parentId).data('category');
    let catLabel = $('.js-filter-category .active', '#'+parentId).text();
    $('.category__toggle', '#'+parentId).text(catLabel);
    this.renderSubcategories(parentId, catId);
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.initFilters();
  }
}

export default CarouselWithFilters;
