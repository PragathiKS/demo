import $ from 'jquery';
import deparam from 'deparam.js';

import { render } from '../../../scripts/utils/render';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { ajaxMethods } from '../../../scripts/utils/constants';

class SearchResults {

  constructor({ el }) {
    this.root = $(el);
  }

  cache = {};

  initCache() {
    this.cache.$clearBtn = this.root.find('.js-search-close');
    this.cache.$filterBoxToggleAction = this.root.find('.js-search-filter-toggle');
    this.cache.$searchBoxToggle = $('.js-tp-pw-header__search-box-toggle');
    this.cache.$applyFilterBtn = this.root.find('.js-apply-filter');
    this.cache.$filterChecks = $('.js-pw-search-results-filter-check', this.root);
    this.cache.$searchInput = $('.js-pw-search-input', this.root);
    this.cache.$searchInput1 = $('.js-pw-search-results-filter-check-1', this.root);
    this.cache.servletPath = this.root.data('servlet');
    this.cache.$filterRemoveBtn = this.root.find('.js-filter-remove');
    this.cache.results = [];
    this.cache.queryParams = '';
    this.cache.resultsPerPage = 10;
    this.cache.$resultsList = $('.js-pw-search-results__results-list', this.root);
    this.cache.$searchResultsTitle = $('.js-pw-search-results__title', this.root);
    this.cache.resultsTitle = this.cache.$searchResultsTitle.data('resultsTitle');
    this.cache.noResultsText = this.cache.$searchResultsTitle.data('noResultsText');

    this.cache.$pagiantion = $('.js-pagination', this.root);
    this.cache.searchTerm = '';
    this.cache.filteredData = [];
    this.cache.filterObj = { tabValue: 'all', checks: [], filterTags: { 'searchTerm': [], 'contentType': [], 'theme': [], 'page': [2] } };
    this.cache.filterTags = {};
    this.cache.totalPages = 0;
  }

  bindEvents() {
    const { $clearBtn, $searchInput, $filterBoxToggleAction, $applyFilterBtn, filterObj, $filterRemoveBtn, $pagiantion } = this.cache;
    const $this = this;
    $applyFilterBtn.on('click', this.applyFilters);
    $filterBoxToggleAction.on('click', this.toggleFilterContainer);
    $('.js-filter-container-chips').on('click', $filterRemoveBtn, this.removeFilter);
    $(window).on('popstate', this.windowPopStateHandler);
    $pagiantion.on('click', '.js-page-number', this.renderPaginationResult);

    this.cache.$filterChecks.change((e) => {
      const $this = $(e.target);
      const category = $this.data('category');
      this.cache.filterObj.checks = this.cache.filterObj.checks || [];
      if ($this[0].checked) {
        this.cache.filterObj.checks.push($this.val());
        this.cache.filterObj.filterTags[category].push($this.attr('id'));
      } else {
        this.cache.filterObj.checks = this.cache.filterObj.checks.filter(item => item !== $this.val());
        this.cache.filterObj.filterTags[category] = this.cache.filterObj.filterTags[category].filter(item => item !== $this.attr('id'));
      }
    });

    $clearBtn.on('click', function () {
      $searchInput.val('');
      $(this).closest('.pw-search-bar__icons__close').removeClass('d-flex');
    });

    $searchInput.keyup((e) => {
      const $input = $(e.target);
      const searchVal = $input.val().trim();
      (searchVal !== '') ? $('.pw-search-bar__icons__close').addClass('d-flex') : $('.pw-search-bar__icons__close').removeClass('d-flex');
      if (e.keyCode === 13) {
        $this.pushIntoUrl();
        if (searchVal === '') {
          filterObj.filterTags['searchTerm'].pop();
          if ((filterObj.filterTags.contentType.length === 0) && (filterObj.filterTags.theme.length === 0)) {
            this.renderTitle('', this.cache.noResultsText, '');
            this.cache.$resultsList.empty();
          } else {
            this.search();
          }
        } else {
          filterObj.filterTags['searchTerm'][0] = searchVal;
          this.search();
        }
      }
    });
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.extractQueryParams();
    this.search();
  }

  windowPopStateHandler = () => {
    this.cache.queryParams = window.location.search;
    this.search();
  }

  renderPaginationResult = e => {
    const $this = $(e.target).closest('.js-page-number');
    const pageNumber = $this.data('pageNumber');
    console.log('pagenumber>>>', pageNumber); //eslint-disable-line
    this.cache.filterObj.filterTags.page[0] = pageNumber;
    this.pushIntoUrl();
    this.search();
  }

  removeFilter = e => {
    const $target = $(e.target);
    const filter = $target.closest('.filter-tag').data('filter');
    const category = $(`label[for="${filter}"]>input`).data('category');
    $(`label[for="${filter}"]>input`).prop('checked', false);
    var index = this.cache.filterObj.filterTags[category].indexOf(filter);
    if (index !== -1) {
      this.cache.filterObj.filterTags[category].splice(index, 1);
    }
    console.log('filter>>>', filter, this.cache.filterObj.filterTags); //eslint-disable-line
    this.applyFilters(false);
  }

  filterData = (filters) => {
    this.cache.filteredData = filters.tabValue === 'all' ? this.cache.results : this.cache.results.filter(obj => obj.productType === filters.tabValue);
    if (filters.checks.length > 0) {
      this.cache.filteredData = this.cache.filteredData.filter((obj) => {
        if (obj.tagsMap) {
          const match = filters.checks.every(elem => Object.values(obj.tagsMap).indexOf(elem) > -1);
          if (match) {
            return obj;
          }
        }
      });
    }
    return this.cache.filteredData;
  };

  search = () => {
    const { searchTerm } = deparam(window.location.search);
    let queryParams = this.cache.queryParams;
    queryParams = queryParams.charAt(0) === '?' ? queryParams.slice(1, queryParams.length + 1) : queryParams;
    ajaxWrapper.getXhrObj({
      url: this.cache.servletPath,
      method: ajaxMethods.GET,
      cache: true,
      data: queryParams,
      dataType: 'json',
      contentType: 'application/json'
    }).done((data) => {
      if (data.length > 0) {
        this.cache.results = data;
        this.cache.filteredData = data;
        this.renderTitle(data.length, this.cache.resultsTitle, searchTerm);
        console.log('inside search >>>', this.cache.results, this.cache.resultsPerPage); //eslint-disable-line
        this.renderPagination();

        if (data.length > this.cache.resultsPerPage) {
          const currentPage = 1;
          this.renderResults(this.cache.results, currentPage);
        } else {
          this.renderResults(this.cache.results);
        }
        this.cache.$filterChecks.removeAttr('disabled');
      } else {
        this.renderTitle(null, this.cache.noResultsText, null);
        this.cache.$resultsList.empty();
        this.cache.$pagiantion.addClass('d-none');
        // this.cache.$filterChecks.attr('disabled', true);
        // this.cache.$pagiantion.addClass('d-none');
      }
    }).fail(() => {
      this.renderTitle(null, this.cache.noResultsText, null);
      this.cache.$resultsList.empty();
      // this.cache.$filterChecks.attr('disabled', true);
      // this.cache.$pagiantion.addClass('d-none');
    });
  };

  renderTitle = (resultsCount, searchTitle, searchTerm) => {
    console.log('render title>>>>>', resultsCount, searchTitle, searchTerm); //eslint-disable-line
    const data = {
      resultsCount: resultsCount || '',
      searchTitle: searchTitle || 'No results were found',
      searchTerm: searchTerm || ''
    };
    render.fn({
      template: 'searchResultsTitle',
      data: data,
      target: '.js-pw-search-results__title'
    });
  };

  renderResults = (data) => {
    const { searchTerm } = this.cache.filterObj.filterTags;
    // const pagination = this.cache.$pagiantion;
    // const resultsPerPage = this.cache.resultsPerPage;
    const noResultsText = this.cache.noResultsText;
    if (data.length > 0) {
      this.renderTitle(data.length, this.cache.resultsTitle, searchTerm[0]);
      render.fn({
        template: 'searchList',
        data: data,
        target: this.cache.$resultsList
        // beforeRender(data) {
        //   if (data.length > resultsPerPage) {
        //     const totalPages = Math.ceil((+data.length) / resultsPerPage);
        //     this.data = data.slice(((currentPage - 1) * resultsPerPage), ((currentPage - 1) * resultsPerPage + resultsPerPage));
        //     console.log('this data render result>>>', this.data); //eslint-disable-line
        //     pagination.trigger('searchresults.paginate', [{
        //       currentPage,
        //       totalPages
        //     }]);
        //   } else {
        //     this.data = data.slice(0, resultsPerPage);
        //     pagination.addClass('d-none');
        //   }
        // }
      });
    } else {
      this.renderTitle('', noResultsText, '');
    }
  };

  renderFilterTags = (propCheck = false) => {
    const { filterTags } = this.cache.filterObj;
    const joinedFilterTags = [...filterTags.contentType, ...filterTags.theme];
    if (propCheck) {
      joinedFilterTags.forEach(el => {
        $('label[for="' + el + '"] > input').prop('checked', true);
      });
    }
    render.fn({
      template: 'searchFilters',
      data: joinedFilterTags,
      target: '.js-filter-container-chips'
    });
  }

  convertToUrl = (data) => {
    let str = '';
    for (const key in data) {
      if (data[key].length > 0) {
        str += `${key}=${data[key].join(',')}&`;
      }
    }
    return str;
  };

  extractQueryParams = () => {
    const params = deparam(window.location.search);
    this.cache.$searchInput.val(params['searchTerm']);
    console.log('extract query>>>>', params); //eslint-disable-line
    this.cache.queryParams = window.location.search;
    Object.keys(params).map(key => {
      if (Array.isArray(params[key])) {
        this.cache.filterObj.filterTags[key] = params[key];
      } else if ((typeof params[key] === 'string') && (params[key]).indexOf(',') > -1) {
        this.cache.filterObj.filterTags[key] = params[key].split(',');
      } else {
        this.cache.filterObj.filterTags[key].push(params[key]);
      }
      console.log('string>>>>', typeof params[key], typeof params[key] === 'string', (params[key]).indexOf(',') > -1); //eslint-disable-line
    });
    this.renderFilterTags(true);
  }

  pushIntoUrl = () => {
    let url = this.convertToUrl(this.cache.filterObj.filterTags);
    console.log('before url>>>>>', url); //eslint-disable-line
    const lastIndex = url.length - 1;
    if (url.charAt(lastIndex) === '&' && url.charAt(0) === '&') {
      url = url.slice(1, -1);
    } else if (url.charAt(lastIndex) === '&') {
      url = url.slice(0, lastIndex);
    } else if (url.charAt(0) === '&') {
      url = url.slice(1, lastIndex + 1);
    }
    console.log('url>>>>>', url, this.cache.filterObj); //eslint-disable-line
    this.cache.queryParams = url;
    window.history.pushState(null, null, (`?${url}`));
  }

  applyFilters = (toggle = true) => {
    const filteredData = this.filterData(this.cache.filterObj);
    console.log('filterObj>>>', this.cache.filterObj, 'filterData>>>', filteredData); //eslint-disable-line
    // this.renderResults(filteredData, 1);
    this.pushIntoUrl();
    this.search();
    this.renderFilterTags();
    toggle && this.toggleFilterContainer();
  };

  toggleFilterContainer = () => {
    $('.js-pw-search-results__filters', this.root).toggle(500);
    $('.js-pw-search-results__filters + div', this.root).toggle(500);
  };

  renderPagination = () => {
    const currentPage = this.cache.filterObj.filterTags.page[0];
    const paginationData = {
      currentPageNumber: currentPage,
      total: 15,
      prevDisabled: currentPage <= 1 ? true : false,
      prevPage: currentPage - 1,
      nextPage: currentPage + 1,
      nextDisabled: false
    };
    render.fn({
      template: 'searchPagination',
      data: paginationData,
      target: this.cache.$pagiantion
    });
  }
}

export default SearchResults;
