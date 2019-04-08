import $ from 'jquery';
import 'bootstrap';
import { render } from '../../../scripts/utils/render';
import { GET_SUBCATEGORIES, GET_CAROUSEL_ITEM } from '../../../scripts/utils/constants';

class CarouselWithFilters {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    let parentId = this.root.attr('id');
    this.cache.$catFilterItem = $('.pw-carousel__filters .js-filter-category .dropdown-item', '#'+parentId);
  }
  renderPractice (parentId, catId) {
    let productType = $('#'+parentId).attr('data-prodtype');
    let rootPath = $('#'+parentId).attr('data-rootpath');
    let btnVariant = $('#'+parentId).attr('data-buttontheme');
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
        if (this.data) {
          this.data = data[0];
          this.data.variant = btnVariant;
        }
      }
    });
  }
  bindSubcategories(parentId) {
    let that = this;
    $('.js-filter-subcategory .dropdown-item', '#'+parentId).click( function(e) {
      e.preventDefault();
      let parent = e.target.closest('.pw-carousel');
      let pId = $(parent).attr('id');
      let catId = e.target.getAttribute('data-category');
      let catLabel = e.target.innerText;
      $('.subcategory__toggle', '#'+parentId).text(catLabel);
      $('.js-filter-subcategory .active', '#'+parentId).removeClass('active');
      $(e.target).addClass('active');
      that.renderPractice(pId, catId);
    });
  }
  renderSubcategories (parentId, catId) {
    let that = this;
    render.fn({
      template: 'dropdownItems',
      url: GET_SUBCATEGORIES,
      ajaxConfig: {
        method: 'GET',
        dataType: 'text',
        data: {
          categoryTag: catId
        }
      },
      target: '#'+parentId+' .pw-carousel__filters .js-filter-subcategory'
    },
    function () {
      // Callback function executed post rendering
      let catLabel = $('.pw-carousel__filters .js-filter-subcategory .active', '#'+parentId).text();
      $('.subcategory__toggle', '#'+parentId).text(catLabel);
      let subcatId = $('.pw-carousel__filters .js-filter-subcategory .active', '#'+parentId).attr('data-category');
      that.renderPractice(parentId, subcatId);
      that.bindSubcategories(parentId);
    });
  }
  bindEvents() {
    /* Bind jQuery events here */
    let that = this;
    this.cache.$catFilterItem.click(function(e) {
      e.preventDefault();
      let parent = e.target.closest('.pw-carousel');
      let parentId = $(parent).attr('id');
      let catId = e.target.getAttribute('data-category');
      let catLabel = e.target.innerText;
      $('.category__toggle', '#'+parentId).text(catLabel);
      $('.js-filter-category .active', '#'+parentId).removeClass('active');
      $(e.target).addClass('active');
      that.renderSubcategories(parentId, catId);
    });
  }
  initFilters() {
    let parentId = this.root.attr('id');
    let catId = $('.js-filter-category .active', '#'+parentId).attr('data-category');
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
