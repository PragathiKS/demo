import $ from 'jquery';
import { render } from '../../../scripts/utils/render';
import { ajaxMethods } from '../../../scripts/utils/constants';
import { logger } from '../../../scripts/utils/logger';
import 'core-js/features/array/includes';

/**
 * Ensures that data is in same order as keys
 * @param {object} order Data object
 * @param {string[]} activeKeys Headings
 */
function _tableSort(order, activeKeys) {
  const dataArray = [];
  activeKeys.forEach((key, index) => {
    dataArray[index] = {
      key,
      value: order[key]
    };
  });
  return dataArray;
}

/**
 * Processes data before rendering
 * @param {object} data JSON data object
 */
function _processTableData(data) {
  // Update i18n keys
  const { i18nKeys, savedPreferences } = this.cache;
  data.labels = i18nKeys;
  // Activate fields which are enabled for render
  if (Array.isArray(data.orders)) {
    const activeKeys = typeof savedPreferences === 'string' ? savedPreferences.split(',') : [];
    data.orders = data.orders.map(order => {
      const processedOrder = {};
      if (activeKeys.length === 0) {
        activeKeys.push(...Object.keys(order));
        return _tableSort(order, activeKeys);
      }
      Object.keys(order).forEach(key => {
        if (activeKeys.includes(key)) {
          processedOrder[key] = order[key];
        }
      });
      return _tableSort(processedOrder, activeKeys);
    });
    data.orderHeadings = activeKeys.map(key => ({
      key,
      i18nKey: `cuhu.ordering.${key}`,
      isSortable: key === 'orderDate',
      sortOrder: 'asc'
    }));
  }
}

class OrderingCard {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.apiUrl = $('#ordApiUrl').val();
    this.cache.preferencesUrl = $('#ordPreferencesUrl').val();
    this.cache.viewAllOrders = $('#ordAllOrdersLink').val();
    this.cache.savedPreferences = $('#ordSavedPreferences').val();
    try {
      this.cache.i18nKeys = JSON.parse($('#ordI18nKeys').val());
    } catch (e) {
      logger.log(e);
    }
  }
  bindEvents() {
    /* Bind jQuery events here */
  }
  renderTable() {
    logger.log(`Testing template strings`);
    render.fn({
      template: 'orderingCard',
      url: this.cache.apiUrl,
      ajaxConfig: {
        method: ajaxMethods.POST
      },
      beforeRender: (...args) => _processTableData.apply(this, args),
      target: this.root
    });
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.renderTable();
  }
}

export default OrderingCard;
