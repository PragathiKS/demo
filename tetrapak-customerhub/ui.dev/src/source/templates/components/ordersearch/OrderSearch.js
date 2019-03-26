import $ from 'jquery';
import { router, route } from 'jqueryrouter';
import deparam from 'jquerydeparam';
import Lightpick from 'lightpick';
import 'bootstrap';
import 'core-js/features/array/includes';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import { ajaxMethods, API_ORDER_HISTORY, API_SEARCH, ORDER_HISTORY_ROWS_PER_PAGE } from '../../../scripts/utils/constants';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import { sanitize, apiHost } from '../../../scripts/common/common';
import auth from '../../../scripts/utils/auth';

/**
 * Processes data before rendering
 * @param {object} data JSON data object
 */
function _processOrderSearchData(data) {
  data = $.extend(true, data, this.cache.config);
  const { filterStartDate, filterEndDate } = data.summary;
  data.dateRange = `${filterStartDate} - ${filterEndDate}`;
  return data;
}

/**
 * Returns contact HTML template
 * @param {object} contacts Contacts object
 */
function _processContacts(contacts) {
  return this.cache.contactListTemplate({
    contacts,
    baseClass: 'tp-order-search',
    jsClass: 'js-order-search'
  });
}

/**
 * Processes order table data
 * @param {object} order Order object
 * @param {string[]} keys List of keys
 * @param {string} orderDetailLink order detail link
 */
function _tableSort(order, keys, orderDetailLink) {
  const dataObject = {
    rowLink: `${orderDetailLink}?q=${order['orderNumber']}`,
    row: []
  };
  keys.forEach((key, index) => {
    const value = (key === 'contact') ? _processContacts.call(this, order[key]) : order[key];
    dataObject.row[index] = {
      key,
      value,
      isRTE: ['contact'].includes(key)
    };
  });
  return dataObject;
}

function _processTableData(data) {
  const { orderDetailLink, disabledFields } = this.cache.config;
  let keys = [];
  if (Array.isArray(data.orders)) {
    data.orders = data.orders.map(order => {
      keys = (keys.length === 0) ? Object.keys(order) : keys;
      if (Array.isArray(disabledFields)) {
        keys = keys.filter(key => !disabledFields.includes(key));
      }
      return _tableSort.call(this, order, keys, orderDetailLink);
    });
    data.orderHeadings = keys.map(key => ({
      key,
      i18nKey: `cuhu.ordering.${key}`,
      isSortable: ['orderDate'].includes(key),
      sortOrder: 'desc'
    }));
  }
}

/**
 * Backfills search fields as per query object
 * @param {object} query Current query object
 */
function _setSearchFields(query) {
  const { $dateRange, $deliveryAddress, $search, $orderStatus } = this.cache;
  if ($dateRange && $dateRange.length) {
    $dateRange.val(`${query['orderdate-from']} - ${query['orderdate-to']}`);
  }
  if ($deliveryAddress && $deliveryAddress.length) {
    $deliveryAddress.val(query.deliveryaddress);
  }
  if ($search && $search.length) {
    $search.val(query.search);
  }
  if ($orderStatus && $orderStatus.length) {
    $orderStatus.val(query.orderstatus);
  }
}

/**
 * Renders table section
 * @param {object} filterParams Selected filter parameters
 */
function _renderTable(filterParams) {
  const { $filters } = this.cache;
  const $this = this;
  this.setSearchFields(filterParams);
  this.root.find('.js-pagination').trigger('ordersearch.pagedisabled');
  auth.getToken(({ data: authData }) => {
    render.fn({
      template: 'orderingTable',
      target: '.js-order-search__table',
      url: {
        path: `${apiHost}/${API_ORDER_HISTORY}`,
        data: filterParams
      },
      beforeRender(data) {
        if (!data) {
          this.data = data = {
            isError: true
          };
        }
        return _processTableData.apply($this, [data]);
      },
      ajaxConfig: {
        beforeSend(jqXHR) {
          jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
          jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        method: ajaxMethods.GET,
        cache: true,
        showLoader: true,
        cancellable: true
      }
    }, (data) => {
      if ($filters && $filters.length) {
        $filters.removeClass('d-none');
      }
      if (filterParams && !data.isError && data.totalOrdersForQuery) {
        const { skip } = filterParams;
        let currentPage = 1;
        let totalPages = Math.ceil((+data.totalOrdersForQuery) / ORDER_HISTORY_ROWS_PER_PAGE);
        if (skip) {
          currentPage = (skip / ORDER_HISTORY_ROWS_PER_PAGE) + 1;
        }
        this.root.find('.js-pagination').trigger('ordersearch.paginate', [{
          currentPage,
          totalPages
        }]);
      }
    });
  });
}

/**
 * Render table based on selected filters
 * @param {boolean} isModifiedSearch Flag to check if it's first or modified render
 */
function _setFilters(isModifiedSearch) {
  const filters = this.cache.$filters.serialize();
  const filterProp = deparam(filters);
  if (filterProp.daterange) {
    const [orderdateFrom, orderdateTo] = filterProp.daterange.split(' - ');
    filterProp['orderdate-from'] = orderdateFrom.trim();
    filterProp['orderdate-to'] = orderdateTo.trim();
    delete filterProp.daterange;
  }
  if (!isModifiedSearch) {
    this.cache.defaultParams = filterProp;
  }
  Object.keys(filterProp).forEach(key => {
    if (!filterProp[key]) {
      delete filterProp[key];
    }
  });
  if (!window.location.hash || isModifiedSearch) {
    router.set({
      route: '#/',
      queryString: $.param(filterProp)
    }, !isModifiedSearch);
  } else {
    router.init();
  }
}

/**
 * Fire analytics on search submit
 */
function _trackAnalytics(defaultParam) {
  const formData = defaultParam || deparam(this.cache.$filters.serialize());
  if (!formData.daterange) {
    formData.daterange = `${formData['orderdate-from']} - ${formData['orderdate-to']}`;
  }
  trackAnalytics(`${sanitize(formData.daterange)}|${sanitize(formData.orderstatus)}|${sanitize(formData.deliveryaddress)}|${sanitize(formData.search)}`, 'orders', 'SearchOrders');
}

/**
 * Renders filter section
 */
function _renderFilters() {
  auth.getToken(({ data }) => {
    render.fn({
      template: 'orderSearch',
      url: `${apiHost}/${API_SEARCH}`,
      target: '.js-order-search__form',
      ajaxConfig: {
        beforeSend(jqXHR) {
          jqXHR.setRequestHeader('Authorization', `Bearer ${data.access_token}`);
          jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        method: ajaxMethods.GET,
        cache: true,
        showLoader: true,
        cancellable: true
      },
      beforeRender: (...args) => _processOrderSearchData.apply(this, args)
    }, () => {
      this.initPostCache();
      this.setFilters();
      this.initializeCalendar();
    });
  });
}

/**
 * Sanitize parameters in query object
 * @param {object} query Query object
 */
function _sanitizeQuery(query) {
  Object.keys(query).forEach(key => {
    query[key] = sanitize(query[key]);
  });
  return query;
}

class OrderSearch {
  cache = {};
  constructor({ el }) {
    this.root = $(el);
  }
  initCache() {
    /* Initialize cache here */
    this.cache.configJson = this.root.find('.js-order-search__config').text();
    this.cache.contactListTemplate = render.get('contactList');
    try {
      this.cache.config = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.config = {};
      logger.error(e);
    }
  }
  bindEvents() {
    route((...args) => {
      const [info, , query] = args;
      if (info.hash) {
        this.renderTable(_sanitizeQuery(query));
      }
    });
    this.root
      .on('click', '.js-order-search__submit', () => {
        this.setFilters(true);
        this.trackAnalytics();
      })
      .on('click', '.js-order-search__reset', () => {
        this.resetSearch();
        this.trackAnalytics(this.cache.defaultParams);
      })
      .on('click', '.js-order-search__date-range', () => {
        $('.js-order-search__modal').modal();
      })
      .find('.js-pagination').on('ordersearch.pagenav', (...args) => {
        const [, data] = args;
        const routeQuery = deparam(window.location.hash.substring(2));
        delete routeQuery.skip;
        if (data.pageIndex > 0) {
          routeQuery.skip = data.pageIndex * ORDER_HISTORY_ROWS_PER_PAGE;
        }
        router.set({
          route: '#/',
          queryString: $.param(routeQuery)
        });
      });
  }
  renderFilters() {
    return _renderFilters.apply(this, arguments);
  }
  initializeCalendar() {
    const { $rangeSelector } = this.cache;
    const rangeSelectorEl = $rangeSelector && $rangeSelector.length ? $rangeSelector[0] : null;
    if (rangeSelectorEl) {
      const [startDate, endDate] = $rangeSelector.val().split(' - ');
      // Initialize inline calendar
      this.cache.picker = new Lightpick({
        field: rangeSelectorEl,
        singleDate: false,
        numberOfMonths: 2,
        inline: true,
        maxDate: Date.now(),
        startDate,
        endDate,
        dropdowns: false,
        format: 'YYYY-MM-DD',
        separator: ' - '
      });
    }
  }
  setFilters() {
    return _setFilters.apply(this, arguments);
  }
  setSearchFields() {
    return _setSearchFields.apply(this, arguments);
  }
  resetSearch() {
    const { defaultParams } = this.cache;
    this.setSearchFields($.extend({}, defaultParams));
    router.set({
      route: '#/',
      queryString: $.param(defaultParams)
    });
  }
  renderTable() {
    return _renderTable.apply(this, arguments);
  }
  initPostCache() {
    this.cache.$filters = this.root.find('.js-order-search__filters');
    this.cache.$dateRange = this.root.find('.js-order-search__date-range');
    this.cache.$orderStatus = this.root.find('.js-order-search__order-status');
    this.cache.$deliveryAddress = this.root.find('.js-order-search__delivery-address');
    this.cache.$search = this.root.find('.js-order-search__search-term');
    this.cache.$rangeSelector = this.root.find('.js-range-selector');
  }
  trackAnalytics() {
    return _trackAnalytics.apply(this, arguments);
  }
  init() {
    this.initCache();
    this.bindEvents();
    this.renderFilters();
  }
}

export default OrderSearch;
