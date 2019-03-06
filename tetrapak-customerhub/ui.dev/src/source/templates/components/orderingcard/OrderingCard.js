import $ from 'jquery';
import 'bootstrap';
import { render } from '../../../scripts/utils/render';
import { ajaxMethods } from '../../../scripts/utils/constants';
import { logger } from '../../../scripts/utils/logger';
import 'core-js/features/array/includes';
import { ajaxWrapper } from '../../../scripts/utils/ajax';

/**
 * Caches available keys from data
 * @param {string[]} availableKeys Available keys
 * @param {string[]} orderKeys Order keys
 */
function _setAvailableKeys(availableKeys, orderKeys) {
  if (availableKeys.length === 0) {
    availableKeys.push(...orderKeys);
  } else {
    orderKeys.forEach(key => {
      if (!availableKeys.includes(key)) {
        availableKeys.push(key);
      }
    });
  }
}

/**
 * Returns formatted contacts HTML
 * @param {object[]} contacts List of contacts
 */
function _processContacts(contacts) {
  // Get contact list template file
  return this.cache.contactListTemplate({
    contacts,
    baseClass: 'tp-ordering-card',
    jsClass: 'js-ordering-card'
  });
}

/**
 * Ensures that data is in same order as keys
 * @param {object} order Data object
 * @param {string[]} activeKeys Headings
 */
function _tableSort(order, activeKeys) {
  const dataArray = [];
  activeKeys.forEach((key, index) => {
    let value = '';
    if (key === 'contact') {
      value = _processContacts.call(this, order[key]);
    } else {
      value = order[key];
    }
    dataArray[index] = {
      key,
      value: value,
      isRTE: ['contact'].includes(key)
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
  const { i18nKeys, savedPreferences, availableKeys = [], viewAllOrders } = this.cache;
  this.cache.availableKeys = availableKeys;
  this.cache.tableData = $.extend(true, {}, data);
  data.labels = i18nKeys;
  // Activate fields which are enabled for render
  if (Array.isArray(data.orders)) {
    let activeKeys = typeof savedPreferences === 'string' ? savedPreferences.split(',') : [];
    activeKeys = activeKeys.filter(key => key);
    data.orders = data.orders.map(order => {
      const processedOrder = {};
      const orderKeys = Object.keys(order);
      if (availableKeys.length === 0) {
        _setAvailableKeys(availableKeys, orderKeys);
      }
      if (activeKeys.length === 0) {
        activeKeys.push(...orderKeys);
        return _tableSort.call(this, order, activeKeys);
      }
      orderKeys.forEach(key => {
        if (activeKeys.includes(key)) {
          processedOrder[key] = order[key];
        }
      });
      return _tableSort.call(this, processedOrder, activeKeys);
    });
    data.orderHeadings = activeKeys.map(key => ({
      key,
      i18nKey: `cuhu.ordering.${key}`,
      isSortable: ['orderDate'].includes(key),
      sortOrder: 'desc'
    }));
    data.settingOptions = availableKeys.map(key => ({
      key,
      i18nKey: `cuhu.ordering.${key}`,
      isChecked: activeKeys.includes(key),
      isMandatory: ['orderNumber', 'poNumber', 'orderDate'].includes(key)
    }));
  }
  data.viewAllOrders = viewAllOrders;
  return data;
}

/**
 * Opens settings overlay panel
 */
function _openSettingsPanel() {
  this.root.find('.js-ordering-card__modal').modal();
}

function _saveSettings() {
  // Get selected preferences
  const selectedFields = $.map(this.root.find('.js-ordering-card__modal-preference').find('input:checked'), function (el) {
    return $(el).val();
  });
  ajaxWrapper.getXhrObj({
    url: this.cache.preferencesUrl,
    data: {
      fields: selectedFields.join(',')
    },
    method: ajaxMethods.POST
  }).done((data) => {
    if (data.status === 'success') {
      this.cache.savedPreferences = selectedFields.join(',');
      this.renderTable({
        template: 'orderingTable',
        data: _processTableData.call(this, this.cache.tableData),
        target: this.root.find('.js-ordering-card__tablewrapper')
      });
      this.root.find('.js-ordering-card__modal').modal('hide');
    }
  });
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
    this.cache.contactListTemplate = render.get('contactList');
    try {
      this.cache.i18nKeys = JSON.parse($('#ordI18nKeys').val());
    } catch (e) {
      logger.log(e);
    }
  }
  bindEvents() {
    /* Bind jQuery events here */
    this.root
      .on('click', '.js-ordering-card__settings', this.openSettingsPanel)
      .on('click', '.js-ordering-card__modal-save', this.saveSettings);
  }
  renderTable(config) {
    if (typeof config === 'undefined') {
      config = {
        template: 'orderingCard',
        url: this.cache.apiUrl,
        ajaxConfig: {
          method: ajaxMethods.POST
        },
        beforeRender: (...args) => _processTableData.apply(this, args),
        target: this.root
      };
    }
    render.fn(config);
  }
  openSettingsPanel = (...args) => _openSettingsPanel.apply(this, args);
  saveSettings = (...args) => _saveSettings.apply(this, args);
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.renderTable();
  }
}

export default OrderingCard;
