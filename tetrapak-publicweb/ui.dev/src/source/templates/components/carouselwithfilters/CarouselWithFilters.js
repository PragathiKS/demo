import $ from 'jquery';
import 'bootstrap';

function _getSubcategories (id, results) {
  for (var i = results.length - 1; i >= 0; i--) {
    if(results[i].tagId === id) {
      return results[i].subcategories;
    }
  }
}

function _getPractice (id, results) {
  for (var i = results.length - 1; i >= 0; i--) {
    if(results[i].tagId === id) {
      return results[i];
    }
  }
}

function _renderPractice (parentId, catId, that) {
  let practice = _getPractice(catId, that.cache.results);
  let $itemContainer =  $('.js-carouselfiltered-item', '#'+parentId);
  $itemContainer.html(that.templates.carouselItem(practice));
}

function _rebindSubcategories(parentId, that) {
  $('.js-filter-subcategory .dropdown-item', '#'+parentId).click( function(e) {
    e.preventDefault;
    let parent = e.target.closest('.pw-carousel');
    let pId = $(parent).attr('id');
    let catId = e.target.getAttribute('data-category');
    _renderPractice(pId, catId, that);
  });
}

function _renderSubcategories (parentId, catId, that) {
  let subcats = _getSubcategories(catId, that.cache.catSubcat);
  let $subcfilter = $('.js-filter-subcategory', '#'+parentId);
  $subcfilter.html(that.templates.dropdownItems(subcats));
  _rebindSubcategories(parentId, that);
}

class CarouselWithFilters {
  constructor({ templates }) {
    this.templates = templates;
  }
  cache = {};
  initCache() {
    /* Initialize cache here */
    this.cache.catSubcat = [
      {
        'label':'category1',
        'tagId':'cat1',
        'subcategories': [
          {
            'label': 'subcategory1-1',
            'tagId': 'subc1-1'
          },
          {
            'label': 'subcategory1-2',
            'tagId': 'subc1-2'
          },
          {
            'label': 'subcategory1-3',
            'tagId': 'subc1-3'
          }
        ]
      },
      {'label':'category2',
        'tagId':'cat2',
        'subcategories': [
          {
            'label': 'subcategory2-1',
            'tagId': 'subc2-1'
          },
          {
            'label': 'subcategory2-2',
            'tagId': 'subc2-2'
          },
          {
            'label': 'subcategory2-3',
            'tagId': 'subc2-3'
          }
        ]
      }
    ];
    this.cache.results = [
      {
        'tagId': 'subc1-1',
        'practiceTitle': 'Juice lines (NFC and FC)',
        'vanityDescription': 'description2',
        'articlepath': 'http://www.google.com',
        'ctaTexti18nKey': 'Look at mixer solutions for Juice lines',
        'linkTarget': '_blank',
        'practiceImagePath': 'https://picsum.photos/1440/301',
        'practiceImageAltI18n': 'image-alt',
        'variant': 'link'
      },
      {
        'tagId': 'subc1-2',
        'practiceTitle': 'Juice lines (NFC and FC)',
        'vanityDescription': 'description2',
        'articlepath': 'http://www.google.com',
        'ctaTexti18nKey': 'Look at mixer solutions for Juice lines',
        'linkTarget': '_blank',
        'practiceImagePath': 'https://picsum.photos/1440/301',
        'practiceImageAltI18n': 'image-alt',
        'variant': 'link'
      },
      {
        'tagId': 'subc2-1',
        'practiceTitle': 'Juice lines (NFC and FC)',
        'vanityDescription': 'description2',
        'articlepath': 'http://www.google.com',
        'ctaTexti18nKey': 'Look at mixer solutions for Juice lines',
        'linkTarget': '_blank',
        'practiceImagePath': 'https://picsum.photos/1440/301',
        'practiceImageAltI18n': 'image-alt',
        'variant': 'link'
      }
    ];
    let defaultCatId = this.cache.catSubcat[0].tagId;
    let defaultSubcats = _getSubcategories(defaultCatId, this.cache.catSubcat);
    let defaultItem = _getPractice(defaultSubcats[0].tagId, this.cache.results);
    $('.js-filter-category').html(this.templates.dropdownItems(this.cache.catSubcat));
    $('.js-filter-subcategory').html(this.templates.dropdownItems(defaultSubcats));
    $('.js-carouselfiltered-item').html(this.templates.carouselItem(defaultItem));

    this.cache.$catFilterItem = $('.pw-carousel__filters .js-filter-category .dropdown-item');
    this.cache.$subcatFilterItem = $('.pw-carousel__filters .js-filter-subcategory .dropdown-item');
  }
  bindEvents() {
    /* Bind jQuery events here */
    const that = this;
    this.cache.$catFilterItem.click(function(e) {
      e.preventDefault;
      let parent = e.target.closest('.pw-carousel');
      let parentId = $(parent).attr('id');
      let catId = e.target.getAttribute('data-category');
      _renderSubcategories(parentId, catId, that);
    });

    this.cache.$subcatFilterItem.click(function(e) {
      e.preventDefault;
      let parent = e.target.closest('.pw-carousel');
      let parentId = $(parent).attr('id');
      let catId = e.target.getAttribute('data-category');
      _renderPractice(parentId, catId, that);
    });
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default CarouselWithFilters;
