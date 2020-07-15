import $ from 'jquery';
import deparam from 'deparam.js';
import { saveAs } from 'file-saver';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import { render } from '../../../scripts/utils/render';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { ajaxMethods } from '../../../scripts/utils/constants';
import { getI18n } from '../../../scripts/common/common';

class Searchresults {
  constructor({ el }) {
    this.root = $(el);
  }

  cache = {};

  initCache() {
    this.cache.$clearBtn = this.root.find('.js-search-close');
    this.cache.$searchBtn = this.root.find('.js-search-results');
    this.cache.$filterBoxToggleAction = this.root.find('.js-search-filter-toggle');
    this.cache.$searchBoxToggle = $('.js-tp-pw-header__search-box-toggle');
    this.cache.$applyFilterBtn = this.root.find('.js-apply-filter');
    this.cache.$filterChecks = $('.js-pw-search-results-filter-check', this.root);
    this.cache.$searchInput = $('.js-pw-search-input', this.root);
    this.cache.servletPath = this.root.data('servlet');
    this.cache.$filterRemoveBtn = this.root.find('.js-filter-remove');
    this.cache.$spinner = this.root.find('.pw-search-results__spinner');
    this.cache.$searchResultLink = this.root.find('.pw-search-results__results-list__item__title-link');
    this.cache.results = [];
    this.cache.queryParams = '';
    this.cache.$resultsList = $('.js-pw-search-results__results-list', this.root);
    this.cache.$searchResultsTitle = $('.js-pw-search-results__title', this.root);
    this.cache.resultsTitle = this.cache.$searchResultsTitle.data('resultsTitle');
    this.cache.noResultsText = this.cache.$searchResultsTitle.data('noResultsText');
    this.cache.emptyFieldText = this.cache.$searchResultsTitle.data('emptyField');
    this.cache.resultSearchTermText = this.cache.$searchResultsTitle.data('resultSearchTerm');

    this.cache.$pagination = $('.js-pagination', this.root);
    this.cache.searchParams = { 'searchTerm': '', 'contentType': {}, 'theme': {}, 'page': 1 };
    this.cache.totalPages = 0;
    this.cache.totalResultCount = 0;
  }

  bindEvents() {
    const { $clearBtn, $searchInput, $filterBoxToggleAction, $searchBtn, $filterRemoveBtn, $applyFilterBtn, $pagination } = this.cache;
    $applyFilterBtn.on('click', this.applyFilters);
    $filterBoxToggleAction.on('click', this.toggleFilterContainer);
    $('.js-filter-container-chips').on('click', $filterRemoveBtn, this.removeFilter);
    $(window).on('popstate', this.windowPopStateHandler);
    $pagination.on('click', '.js-page-number', this.renderPaginationResult);

    $('.pw-search-results__results').on('click', '.js-asset-download', function(e) {
      e.preventDefault();
      const $target = $(this).closest('.js-asset-download');
      const assetUrl = $target.attr('href');
      const extension = $target.data('assetExtension');
      saveAs(assetUrl, `download.${extension}`);
    });

    this.root.on('click', '.pw-search-results__results-list__item__title-link', this.searchResultLinkAnalytics);

    this.cache.$filterChecks.change((e) => {
      const $this = $(e.target);
      const category = $this.data('category');
      const id = $this.attr('id');
      const value = $this.val();
      if ($this[0].checked) {
        this.cache.searchParams[category][id] = value;
      } else {
        delete this.cache.searchParams[category][id];
      }
    });

    $clearBtn.on('click', function () {
      $searchInput.val('');
      $(this).closest('.pw-search-bar__icons__close').removeClass('d-flex');
    });

    $searchBtn.on('click', this.searchBtnHandler);

    $searchInput.keyup((e) => {
      const $input = $(e.target);
      const searchVal = $input.val().trim();
      (searchVal !== '') ? $('.pw-search-bar__icons__close').addClass('d-flex') : $('.pw-search-bar__icons__close').removeClass('d-flex');
      if (e.keyCode === 13) {
        this.searchBtnHandler();
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
    this.cache.searchParams = {'searchTerm': '', 'contentType': {}, 'theme': {}, 'page': 1 };
    this.cache.$filterChecks.prop('checked', false);
    this.extractQueryParams();
    this.search();
  }

  searchBtnHandler = () => {
    const { searchParams } = this.cache;
    const searchVal = this.cache.$searchInput.val().trim();

    const { contentType, theme } = this.cache.searchParams;

    let joinedFilterTags = { ...contentType, ...theme };
    joinedFilterTags = $.isEmptyObject(joinedFilterTags) ? [] : joinedFilterTags;

    // do the analytics call 
    const searchObj = {
      searchTerm : searchVal, 
      searchFilters : Object.values(joinedFilterTags).join(',')
    };

    const eventObj = {
      eventType : 'internalsearch',
      event : 'Search'
    };

    const linkClickObject = {
      linkType: 'internal',
      linkSection: 'Hyperlink click',
      linkParentTitle: '',
      linkName: 'Search'
    };
      
    trackAnalytics(searchObj, 'search', 'internalsearch', undefined, false, eventObj, linkClickObject);

    if (searchVal === '') {
      searchParams['searchTerm'] = '';
      if ($.isEmptyObject(searchParams['contentType']) && $.isEmptyObject(searchParams['theme'])) {
        this.renderTitle('', this.cache.emptyFieldText, '', true);
        this.cache.$resultsList.empty();
        this.cache.$pagination.addClass('d-none');
      } else {
        searchParams['page'] = 1;
        this.pushIntoUrl();
        this.search();
      }
    } else {
      searchParams['searchTerm'] = searchVal;
      searchParams['page'] = 1;
      this.pushIntoUrl();
      this.search();
    }
  }

  removeFilter = e => {
    const $target = $(e.target);
    if($target.hasClass('js-filter-remove')) {
      const filter = $target.closest('.filter-tag').data('filter');
      const category = $(`#${filter}`).data('category');
      $(`#${filter}`).prop('checked', false);
      delete this.cache.searchParams[category][filter];
      this.applyFilters(false);
    }
  }

  search = () => {
    this.cache.$spinner.removeClass('d-none');
    const { searchTerm, contentType, theme } = this.cache.searchParams;
    if (searchTerm === '' && $.isEmptyObject(contentType) && $.isEmptyObject(theme)) {
      this.renderTitle('', this.cache.emptyFieldText, '', true);
      this.cache.$resultsList.empty();
      this.cache.$spinner.addClass('d-none');
      this.cache.$pagination.addClass('d-none');
    } else {
      const { searchTerm } = deparam(window.location.search);
      this.cache.$filterChecks.attr('disabled', true);
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
        this.cache.$spinner.addClass('d-none');
        this.cache.$filterChecks.removeAttr('disabled');
        
        if (data.totalResults > 0) {

          let joinedFilterTags = { ...contentType, ...theme };
          joinedFilterTags = $.isEmptyObject(joinedFilterTags) ? [] : joinedFilterTags;
          const searchfiltersString = Object.values(joinedFilterTags).join(',');

          // do the analytics call 
          const searchObj = {
            searchTerm : searchTerm, 
            searchResults: data.totalResults,
            searchFilters : searchfiltersString
          };

          const eventObj = {  
            eventType : 'internalsearch',
            event : 'Search'
          };

          trackAnalytics(searchObj, 'search', 'internalsearch', undefined, false, eventObj);

          this.cache.totalResultCount = data.totalResults;
          this.cache.results = data.searchResults;
          this.cache.totalPages = data.totalPages;
          const title = searchTerm ? this.cache.resultSearchTermText : this.cache.resultsTitle;
          this.renderTitle(data.totalResults, title, searchTerm);
          this.renderResults(data.searchResults);
          if (data.totalPages > 1) {
            this.renderPagination();
            this.cache.$pagination.removeClass('d-none');
          } else {
            this.cache.$pagination.addClass('d-none');
          }
        } else {
          this.cache.$filterChecks.removeAttr('disabled');
          this.renderTitle(null, this.cache.noResultsText, null);
          this.cache.$resultsList.empty();
          this.cache.$pagination.addClass('d-none');
        }
      }).fail(() => {
        this.cache.$spinner.addClass('d-none');
        this.renderTitle(null, this.cache.noResultsText, null);
        this.cache.$filterChecks.removeAttr('disabled');
        this.cache.$resultsList.empty();
        this.cache.$pagination.addClass('d-none');
      });
    }
  };

  renderTitle = (resultsCount, searchTitle, searchTerm, emptySearch = false) => {
    if (resultsCount) {
      $('.js-filter-title', this.root).removeClass('no-border');
    } else {
      $('.js-filter-title', this.root).addClass('no-border');
    }
    const data = {
      resultsCount: resultsCount || '',
      searchTitle: searchTitle || '',
      searchTerm: searchTerm || '',
      emptySearch
    };
    render.fn({
      template: 'searchResultsTitle',
      data: data,
      target: '.js-pw-search-results__title'
    });
  };

  renderResults = (data) => {
    const noResultsText = this.cache.noResultsText;
    if (data.length > 0) {
      const modifiedData = data.map(res => {
        res['typeI18n'] = getI18n(res['type']);
        res['assetTypeI18n'] = res['assetType'] === 'video' ? getI18n('pw.searchResults.video') : res['assetType'] === 'image' ? getI18n('pw.searchResults.image') : getI18n('pw.searchResults.document');
        res['sizeTypeI18n'] = getI18n(res['sizeType']);
        return res;
      });
      render.fn({
        template: 'searchList',
        data: modifiedData,
        target: this.cache.$resultsList
      });
    } else {
      this.renderTitle('', noResultsText, '');
    }
  };

  renderFilterTags = () => {
    const { contentType, theme, searchTerm } = this.cache.searchParams;
    let joinedFilterTags = { ...contentType, ...theme };
    joinedFilterTags = $.isEmptyObject(joinedFilterTags) ? [] : joinedFilterTags;

    // do the analytics call 
    const searchObj = {
      searchTerm : searchTerm, 
      searchFilters : Object.values(joinedFilterTags).join(',')
    };

    const eventObj = {
      eventType : 'internalsearch',
      event : 'Search'
    };

    const linkClickObject = {
      linkType: 'internal',
      linkSection: 'Hyperlink click',
      linkParentTitle: '',
      linkName: 'Search'
    };

    trackAnalytics(searchObj, 'search', 'internalsearch', undefined, false, eventObj, linkClickObject);
    
    render.fn({
      template: 'searchFilters',
      data: joinedFilterTags,
      target: '.js-filter-container-chips'
    });
  }

  searchResultLinkAnalytics = () => {
    const { contentType, theme, searchTerm } = this.cache.searchParams;
    let joinedFilterTags = { ...contentType, ...theme };
    joinedFilterTags = $.isEmptyObject(joinedFilterTags) ? [] : joinedFilterTags;

    // do the analytics call 
    const searchObj = {
      searchTerm : searchTerm, 
      searchResults: this.cache.totalResultCount,
      searchFilters : Object.values(joinedFilterTags).join(',')
    };

    const eventObj = {
      eventType : 'internalsearch',
      event : 'Search'
    };

    const linkClickObject = {
      linkType: 'internal',
      linkSection: 'Hyperlink click',
      linkParentTitle: '',
      linkName: 'Search'
    };

    trackAnalytics(searchObj, 'search', 'linkClick', undefined, false, eventObj, linkClickObject);
  }

  convertToUrl = (data) => {
    let str = '';
    for (const key in data) {
      if (typeof data[key] === 'object') {
        const finalArr = [];
        Object.keys(data[key]).forEach(el => finalArr.push(el));
        if (finalArr.length > 0) {
          str += `${key}=${finalArr.join(',')}&`;
        }
      } else if (data[key]) {
        str += `${key}=${data[key]}&`;
      }
    }
    return str;
  };

  extractQueryParams = () => {
    const params = deparam(window.location.search);
    this.cache.$searchInput.val(params['searchTerm']);
    this.cache.queryParams = window.location.search;
    Object.keys(params).map(key => {
      if ((key === 'contentType' || key === 'theme')) {
        if (params[key].indexOf(',') > -1) {
          const extractParams = params[key].split(',');
          extractParams.forEach(el => {
            this.cache.searchParams[key][el] = $(`#${el}`).val();
            $(`#${el}`).prop('checked', true);
          });
        } else {
          const filterKey = params[key];
          this.cache.searchParams[key][filterKey] = $(`#${filterKey}`).val();
          $(`#${filterKey}`).prop('checked', true);
        }
      } else {
        this.cache.searchParams[key] = params[key];
      }
    });
    this.renderFilterTags();
  }

  pushIntoUrl = () => {
    let url = this.convertToUrl(this.cache.searchParams);
    const lastIndex = url.length - 1;
    if (url.charAt(lastIndex) === '&' && url.charAt(0) === '&') {
      url = url.slice(1, -1);
    } else if (url.charAt(lastIndex) === '&') {
      url = url.slice(0, lastIndex);
    } else if (url.charAt(0) === '&') {
      url = url.slice(1, lastIndex + 1);
    }
    this.cache.queryParams = url;
    window.history.pushState(null, null, (`?${url}`));
  }

  applyFilters = (toggle = true) => {
    this.cache.searchParams['page'] = 1;
    this.pushIntoUrl();
    this.search();
    this.renderFilterTags();
    toggle && this.toggleFilterContainer();
  };

  toggleFilterContainer = () => {
    const $filterHeading = $('.pw-search-results__filter-container__heading', this.root);
    $filterHeading.hasClass('light') && $filterHeading.removeClass('light');

    $('.js-pw-search-results__filters', this.root).toggle(500, () => {
      if (!$filterHeading.hasClass('light') && !$('.js-pw-search-results__filters').is(':visible')) {
        $filterHeading.addClass('light');
      }
    });
    $('.js-pw-search-results__filters + div', this.root).toggle(500);
  };

  renderPaginationResult = e => {
    const $this = $(e.target).closest('.js-page-number');
    const pageNumber = $this.data('pageNumber');
    this.cache.searchParams.page = pageNumber;
    this.pushIntoUrl();
    $('html, body').animate({
      scrollTop: 0
    }, 500);
    this.search();
  }

  renderPagination = () => {
    const currentPage = this.cache.searchParams.page;
    const totalPages = this.cache.totalPages;
    const paginationText = this.cache.$pagination.data('paginationText');
    const paginationData = {
      paginationText,
      currentPageNumber: currentPage,
      total: totalPages,
      prevDisabled: currentPage <= 1 ? true : false,
      prevPage: currentPage - 1,
      nextPage: currentPage + 1,
      nextDisabled: currentPage >= totalPages ? true : false
    };
    if (currentPage <= totalPages) {
      render.fn({
        template: 'searchPagination',
        data: paginationData,
        target: this.cache.$pagination
      });
    }
  }
}

export default Searchresults;
