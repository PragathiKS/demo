import $ from 'jquery';
import { route } from 'jqueryrouter';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import { ajaxMethods } from '../../../scripts/utils/constants';
import routing from '../../../scripts/utils/routing';
import { trackAnalytics } from '../../../scripts/utils/analytics';

/**
 * Processes data before rendering
 * @param {object} data JSON data object
 */
function _processOrderSearchData(data) {
  data = $.extend(true, data, this.cache.config);
  data.dateRange = `${data.summary.filterStartDate} - ${data.summary.filterEndDate}`;
  return data;
}

/**
 * Fire analytics on search submit
 */
function _trackAnalytics() {
  const { $dateRange, $status, $address, $searchInput } = this.cache;
  let analyticsData = `${$dateRange.val()}|${$status.val()}|${$address.val()}|${$searchInput.val()}`;
  trackAnalytics(analyticsData, 'SearchOrders');
}

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
      const [info] = args;
      if (info.hash) {
        this.renderFilters();
      }
    });
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
  renderFilters = (...args) => _renderFilters.apply(this, args);
  trackAnalytics = () => _trackAnalytics.call(this);
  init() {
    this.initCache();
    this.bindEvents();
    routing.push('OrderSearch');
  }
}

export default OrderSearch;
