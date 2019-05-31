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
    this.cache.$pagiantion = $('.js-pagination', this.root);
    this.cache.results = [];
    this.cache.totalPages = 0;
  }

  bindEvents() {
    const $this = this;

    this.cache.$pagiantion.on('searchresults.pagenav', (...args) => {
      const [, data] = args;
      console.log(data); // eslint-disable-line
      $this.cache.currentPageIndex = data.pageIndex + 1;
      $this.renderResults(this.cache.results, $this.cache.currentPageIndex);
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
          if (data.length > NO_OF_EVENTS_PER_PAGE) {
            let currentPage = 1;
            this.renderResults(this.cache.results, currentPage);
          } else {
            this.renderResults(this.cache.results);
          }
        }
      });
    }
  };

  renderResults = (data, currentPage) => {
    console.log(this.cache.$pagiantion); // eslint-disable-line
    let pagination = this.cache.$pagiantion;
    render.fn({
      template: 'searchList',
      data: data,
      target: '.js-pw-search-results__results-list',
      beforeRender(data) {
        if (data.length > NO_OF_EVENTS_PER_PAGE) {
          let totalPages = Math.ceil((+data.length) / NO_OF_EVENTS_PER_PAGE);
          this.data = data.slice(((currentPage - 1) * NO_OF_EVENTS_PER_PAGE), ((currentPage - 1) * NO_OF_EVENTS_PER_PAGE + NO_OF_EVENTS_PER_PAGE));
          pagination.trigger('searchresults.paginate', [{
            currentPage,
            totalPages
          }]);
        } else {
          this.data = data.slice(0, NO_OF_EVENTS_PER_PAGE);
        }
      }
    });
  };
}

export default SearchResults;
