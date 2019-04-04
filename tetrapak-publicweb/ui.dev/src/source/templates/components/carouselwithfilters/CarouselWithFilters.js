import $ from 'jquery';
import 'bootstrap';
import { render } from '../../../scripts/utils/render';

function _renderPractice (parentId, catId) {
  let productType = $('.producttype-data-prodtype', '#'+parentId).attr('data');
  let rootPath = $('.practice-data-rootpath', '#'+parentId).attr('data');
  let btnVariant = $('.practice-data-pwButtonTheme', '#'+parentId).attr('data');
  render.fn({
    template: 'carouselItem',
    url: '/bin/tetrapak/pw-carousellisting',
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
      this.data = data[0];
      this.data.variant = btnVariant;
    }
  });
}

function _rebindSubcategories(parentId) {
  $('.js-filter-subcategory .dropdown-item', '#'+parentId).click( function(e) {
    e.preventDefault;
    let parent = e.target.closest('.pw-carousel');
    let pId = $(parent).attr('id');
    let catId = e.target.getAttribute('data-category');
    let catLabel = e.target.innerText;
    $('.subcategory__toggle', '#'+parentId).text(catLabel);
    $('.js-filter-subcategory .active', '#'+parentId).removeClass('active');
    $(e.target).addClass('active');
    _renderPractice(pId, catId);
  });
}

function _renderSubcategories (parentId, catId) {
  render.fn({
    template: 'dropdownItems',
    url: '/bin/tetrapak/pw-subcategorytag',
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
    _renderPractice(parentId, subcatId);
    _rebindSubcategories(parentId);
  });
}

class CarouselWithFilters {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    let parentId = this.root.attr('id');
    this.cache.$catFilterItem = $('.pw-carousel__filters .js-filter-category .dropdown-item', '#'+parentId);
  }
  bindEvents() {
    /* Bind jQuery events here */
    this.cache.$catFilterItem.click(function(e) {
      e.preventDefault;
      let parent = e.target.closest('.pw-carousel');
      let parentId = $(parent).attr('id');
      let catId = e.target.getAttribute('data-category');
      let catLabel = e.target.innerText;
      $('.category__toggle', '#'+parentId).text(catLabel);
      $('.js-filter-category .active', '#'+parentId).removeClass('active');
      $(e.target).addClass('active');
      _renderSubcategories(parentId, catId);
    });
  }
  initFilters() {
    let parentId = this.root.attr('id');
    let catId = $('.js-filter-category .active', '#'+parentId).attr('data-category');
    let catLabel = $('.js-filter-category .active', '#'+parentId).text();
    $('.category__toggle', '#'+parentId).text(catLabel);
    _renderSubcategories(parentId, catId);
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.initFilters();
  }
}

export default CarouselWithFilters;
