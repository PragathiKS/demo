import $ from 'jquery';
import { router, route } from 'jqueryrouter';
import deparam from 'jquerydeparam';
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
 * Render table based on selected filters
 */
function _renderTable() {
  this.cache.$filters = $('.js-order-search__filters');
  const filters = this.cache.$filters.serialize();
  const filterProp = deparam(filters);
  if (filterProp.daterange) {
    const [orderdateFrom, orderdateTo] = filterProp.daterange.split(' - ');
    filterProp['orderdate-from'] = orderdateFrom.trim();
    filterProp['orderdate-to'] = orderdateTo.trim();
    delete filterProp.daterange;
  }
  this.cache.defaultParams = filterProp;
  Object.keys(filterProp).forEach(key => {
    if (!filterProp[key]) {
      delete filterProp[key];
    }
  });
  router.set({
    route: '#/orders',
    queryString: $.param(filterProp)
  }, true);
}

/**
 * Fire analytics on search submit
 */
function _trackAnalytics() {
  const { $dateRange, $status, $address, $searchInput } = this.cache;
  let analyticsData = `${$dateRange.val()}|${$status.val()}|${$address.val()}|${$searchInput.val()}`;
  trackAnalytics(analyticsData, 'SearchOrders');
}

/**
 * Renders filter section
 */
function _renderFilters() {
  const { config } = this.cache;
  render.fn({
    template: 'orderSearch',
    url: config.apiURL,
    target: '.js-order-search__form',
    ajaxConfig: {
      method: ajaxMethods.POST
    },
    beforeRender: (...args) => _processOrderSearchData.apply(this, args)
  }, () => {
    this.renderTable();
    this.initPostCache();
    this.bindPostEvents();
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
        logger.log(query);
      }
    });
  }
  renderFilters() {
    return _renderFilters.apply(this, arguments);
  }
  renderTable() {
    return _renderTable.apply(this, arguments);
  }
  initPostCache() {
    this.cache.$dateRange = this.root.find('.js-order-search__date-range');
    this.cache.$status = this.root.find('.js-order-search__order-status');
    this.cache.$address = this.root.find('.js-order-search__delivery-address');
    this.cache.$searchInput = this.root.find('.js-order-search__search-term');
    this.cache.$submitButton = this.root.find('.js-order-search__submit');
  }
  bindPostEvents() {
    this.cache.$submitButton.on('click', this.trackAnalytics);
  }
  trackAnalytics = () => _trackAnalytics.call(this);
  init() {
    this.initCache();
    this.bindEvents();
    this.renderFilters();
  }
}

export default OrderSearch;
