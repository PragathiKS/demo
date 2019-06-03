import $ from 'jquery';
import deparam from 'jquerydeparam';
import {render} from '../../../scripts/utils/render';
import {ajaxWrapper} from '../../../scripts/utils/ajax';
import {ajaxMethods, API_SEARCH_RESULTS, NO_OF_EVENTS_PER_PAGE} from '../../../scripts/utils/constants';

class SearchResults {

  constructor({el}) {
    this.root = $(el);
  }

  cache = {};

  initCache() {
    this.cache.$searchBoxToggle = $('.js-tp-pw-header__search-box-toggle');
    this.cache.searchResultsPath = this.cache.$searchBoxToggle.data('resultsPath');
    this.cache.searchRootPath = this.cache.$searchBoxToggle.data('rootPath');
    this.cache.resultsPerPage = this.root.data('resultsPerPage') || NO_OF_EVENTS_PER_PAGE;
    this.cache.$searchInput = $('.js-pw-search-input', this.root);
    this.cache.$tabs = $('.js-pw-search-results__tabs', this.root);
    this.cache.$searchResultsTitle = $('.js-pw-search-results__title', this.root);
    this.cache.resultsTitle = this.cache.$searchResultsTitle.data('resultsTitle');
    this.cache.noResultsText = this.cache.$searchResultsTitle.data('noResultsText');
    this.cache.resultsCounter = '0';
    this.cache.tabButtons = $('.js-pw-search-results_tab', this.root);
    this.cache.$pagiantion = $('.js-pagination', this.root);
    this.cache.searchTerm = '';
    this.cache.results = [];
    this.cache.totalPages = 0;
  }

  bindEvents() {
    const $this = this;
    this.cache.$pagiantion.on('searchresults.pagenav', (...args) => {
      const [, data] = args;
      $this.cache.currentPageIndex = data.pageIndex + 1;
      $this.renderResults(this.cache.results, $this.cache.currentPageIndex);
    });

    this.cache.tabButtons.on('click', (e) => {
      e.preventDefault();
      this.cache.tabButtons.removeClass('tpatom-button--group-item--active');
      let $this = $(e.target);
      let filterValue = $this.data('custom');
      let filteredData = filterValue === 'all' ? this.cache.results : this.cache.results.filter(i => i.productType === filterValue);
      this.renderResults(filteredData, 1);
      $this.addClass('tpatom-button--group-item--active');
    });
    this.cache.$searchInput.keyup((e) => {
      if (e.keyCode === 13) {
        let $this = $(e.target);
        let params = deparam(window.location.search);
        params.q = $this.val();
        window.history.pushState(null, null, ('?q=' + params.q));
        this.search();
      }
    });

  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.search();
  }

  search = () => {
    let params = deparam(window.location.search);
    if (params.q) {
      this.cache.searchTerm = params.q;
      ajaxWrapper.getXhrObj({
        url: API_SEARCH_RESULTS,
        method: ajaxMethods.GET,
        cache: true,
        data: {
          fulltextSearchTerm: params.q,
          searchResultsPath: this.cache.searchResultsPath,
          searchRootPath: this.cache.searchRootPath
        },
        dataType: 'json',
        contentType: 'application/json'
      }).done((data) => {
        if (data.length > 0) {
          this.cache.results = data;
          this.renderTitle(data.length, this.cache.resultsTitle, params.q);
          if (data.length > this.cache.resultsPerPage) {
            let currentPage = 1;
            this.renderResults(this.cache.results, currentPage);
          } else {
            this.renderResults(this.cache.results);
          }
        } else {
          this.renderTitle(null, this.cache.noResultsText, null);
          this.cache.$tabs.addClass('d-none');
        }
      });
    }
  };

  renderTitle = (resultsCount, searchTitle, searchTerm) => {
    let data = {
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

  renderResults = (data, currentPage) => {
    let pagination = this.cache.$pagiantion;
    let resultsPerPage = this.cache.resultsPerPage;
    render.fn({
      template: 'searchList',
      data: data,
      target: '.js-pw-search-results__results-list',
      beforeRender(data) {
        if (data || data.length > 0) {
          if (data.length > resultsPerPage) {
            let totalPages = Math.ceil((+data.length) / resultsPerPage);
            this.data = data.slice(((currentPage - 1) * resultsPerPage), ((currentPage - 1) * resultsPerPage + resultsPerPage));
            pagination.trigger('searchresults.paginate', [{
              currentPage,
              totalPages
            }]);
          } else {
            this.data = data.slice(0, resultsPerPage);
            pagination.addClass('d-none');
          }
        }
      }
    });
  };
}

export default SearchResults;
