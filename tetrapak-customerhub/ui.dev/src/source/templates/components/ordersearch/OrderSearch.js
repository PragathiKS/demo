import $ from 'jquery';
import { router, route } from 'jqueryrouter';
import deparam from 'jquerydeparam';
import 'core-js/features/array/includes';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import { ajaxMethods } from '../../../scripts/utils/constants';
import { trackAnalytics } from '../../../scripts/utils/analytics';

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
  const { orderDetailLink } = this.cache.config;
  let keys = [];
  if (Array.isArray(data.orders)) {
    data.orders = data.orders.map(order => {
      keys = (keys.length === 0) ? Object.keys(order) : keys;
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
 * Renders table section
 * @param {object} filterParams Selected filter parameters
 */
function _renderTable(filterParams) {
  const { config, $filters } = this.cache;
  const $this = this;
  render.fn({
    template: 'orderingTable',
    target: '.js-order-search__table',
    url: {
      path: config.ordersApiURL,
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
      method: ajaxMethods.GET
    }
  }, () => {
    if ($filters && $filters.length) {
      $filters.removeClass('d-none');
    }
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
      route: '#/orders',
      queryString: $.param(filterProp)
    }, true);
  } else {
    router.init();
  }
}

/**
 * Fire analytics on search submit
 */
function _trackAnalytics() {
  const formData = deparam(this.cache.$filters.serialize());
  trackAnalytics(`${formData.daterange}|${formData.orderstatus}|${formData.deliveryaddress}|${formData.search}`, 'SearchOrders');
}

/**
 * Renders filter section
 */
function _renderFilters() {
  const { config } = this.cache;
  render.fn({
    template: 'orderSearch',
    url: config.searchApiURL,
    target: '.js-order-search__form',
    ajaxConfig: {
      method: ajaxMethods.GET
    },
    beforeRender: (...args) => _processOrderSearchData.apply(this, args)
  }, () => {
    this.initPostCache();
    this.setFilters();
  });
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
        this.renderTable(query);
      }
    });
    this.root.on('click', '.js-order-search__submit', () => {
      this.setFilters(true);
      this.trackAnalytics();
    });
  }
  renderFilters() {
    return _renderFilters.apply(this, arguments);
  }
  setFilters() {
    return _setFilters.apply(this, arguments);
  }
  renderTable() {
    return _renderTable.apply(this, arguments);
  }
  initPostCache() {
    this.cache.$filters = $('.js-order-search__filters');
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
