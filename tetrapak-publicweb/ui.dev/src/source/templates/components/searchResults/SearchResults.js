import $ from 'jquery';
import deparam from 'deparam.js';
import { render } from '../../../scripts/utils/render';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { ajaxMethods, API_SEARCH_RESULTS, NO_OF_EVENTS_PER_PAGE } from '../../../scripts/utils/constants';

class SearchResults {

  constructor({ el }) {
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
    this.cache.$filterChecks = $('.js-pw-search-results-filter-check', this.root);
    this.cache.$resultsList = $('.js-pw-search-results__results-list', this.root);
    this.cache.$searchResultsTitle = $('.js-pw-search-results__title', this.root);
    this.cache.resultsTitle = this.cache.$searchResultsTitle.data('resultsTitle');
    this.cache.noFiltersMatches = this.cache.$searchResultsTitle.data('noFiltersMatches');
    this.cache.noResultsText = this.cache.$searchResultsTitle.data('noResultsText');
    this.cache.resultsCounter = '0';
    this.cache.tabButtons = $('.js-pw-search-results_tab', this.root);
    this.cache.$pagiantion = $('.js-pagination', this.root);
    this.cache.searchTerm = '';
    this.cache.results = [];
    this.cache.filteredData = [];
    this.cache.filterObj = { tabValue: 'all', checks: [] };
    this.cache.totalPages = 0;
  }

  bindEvents() {
    const $this = this;
    this.cache.$pagiantion.on('searchresults.pagenav', (...args) => {
      const [, data] = args;
      $this.cache.currentPageIndex = data.pageIndex + 1;
      $this.renderResults(this.cache.filteredData, $this.cache.currentPageIndex);
    });

    this.cache.tabButtons.on('click', (e) => {
      e.preventDefault();
      this.cache.tabButtons.removeClass('tpatom-button--group-item--active');
      const $this = $(e.target);
      this.cache.filterObj.tabValue = $this.data('custom');
      const filteredData = this.filterData(this.cache.filterObj);
      this.renderResults(filteredData, 1);
      $this.addClass('tpatom-button--group-item--active');
    });

    this.cache.$filterChecks.change((e) => {
      const $this = $(e.target);
      this.cache.filterObj.checks = this.cache.filterObj.checks || [];
      if ($this[0].checked) {
        this.cache.filterObj.checks.push($this.val());
      } else {
        this.cache.filterObj.checks = this.cache.filterObj.checks.filter(item => item !== $this.val());
      }
      const filteredData = this.filterData(this.cache.filterObj);
      this.renderResults(filteredData, 1);
    });

    this.cache.$searchInput.keyup((e) => {
      if (e.keyCode === 13) {
        const $this = $(e.target);
        const params = deparam(window.location.search);
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
    const params = deparam(window.location.search);
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
          this.cache.filteredData = data;
          this.cache.filterObj = { tabValue: 'all', checks: [] };
          this.renderTitle(data.length, this.cache.resultsTitle, params.q);
          if (data.length > this.cache.resultsPerPage) {
            const currentPage = 1;
            this.renderResults(this.cache.results, currentPage);
          } else {
            this.renderResults(this.cache.results);
          }
          this.cache.$tabs.removeClass('d-none');
          this.cache.$filterChecks.removeAttr('disabled');
        } else {
          this.renderTitle(null, this.cache.noResultsText, null);
          this.cache.$resultsList.empty();
          this.cache.$tabs.addClass('d-none');
          this.cache.$filterChecks.attr('disabled', true);
          this.cache.$pagiantion.addClass('d-none');
        }
        this.cache.$filterChecks.prop('checked', false);
      });
    }
  };

  renderTitle = (resultsCount, searchTitle, searchTerm) => {
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

  renderResults = (data, currentPage) => {
    const pagination = this.cache.$pagiantion;
    const resultsPerPage = this.cache.resultsPerPage;
    const noFilterMatches = this.cache.noFiltersMatches;
    render.fn({
      template: 'searchList',
      data: data,
      target: this.cache.$resultsList,
      beforeRender(data) {
        if (data.length > 0) {
          if (data.length > resultsPerPage) {
            const totalPages = Math.ceil((+data.length) / resultsPerPage);
            this.data = data.slice(((currentPage - 1) * resultsPerPage), ((currentPage - 1) * resultsPerPage + resultsPerPage));
            pagination.trigger('searchresults.paginate', [{
              currentPage,
              totalPages
            }]);
          } else {
            this.data = data.slice(0, resultsPerPage);
            pagination.addClass('d-none');
          }
        } else {
          data.noFiltersMatches = noFilterMatches;
        }
      }
    });
  };
}

export default SearchResults;
