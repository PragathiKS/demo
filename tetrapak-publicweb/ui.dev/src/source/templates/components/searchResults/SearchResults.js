import $ from 'jquery';
import deparam from 'jquerydeparam';

class SearchResults {
  cache = {};

  initCache() {
    this.cache.$searchBoxToggle = $('.js-tp-pw-header__search-box-toggle');
    this.cache.searchResultsPath = this.cache.$searchBoxToggle.data('resultsPath');
    this.cache.searchRootPath = this.cache.$searchBoxToggle.data('rootPath');
  }

  bindEvents() {
    /* Bind jQuery events here */
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
      //TODO Use ajaxWrapper instead of $.ajax and set async to true
      $.ajax({ // eslint-disable-line
        url: '/bin/tetrapak/pw-search',
        type: 'get',
        async: false,
        data: {
          fulltextSearchTerm: params.q,
          searchResultsPath: this.cache.searchResultsPath,
          searchRootPath: this.cache.searchRootPath
        },
        success: function (data) {
          //TODO on success render search results hbs template using render.js
          var totalResults = data.length;
          $('.js-pw-search-results__results-count').append('<h4>' + totalResults + 'results found.</h4>');
          $.each(data, function (i, obj) {
            $('.js-pw-search-results').append('<p><a href=\'' + obj.path + '.html\' >' + obj.title + '</a></p>');
          });

        }
      });
    }

  }
}

export default SearchResults;
