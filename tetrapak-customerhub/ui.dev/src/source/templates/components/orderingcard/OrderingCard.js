import $ from 'jquery';
import 'bootstrap';
import { render } from '../../../scripts/utils/render';
import { ajaxMethods, API_ORDER_HISTORY, ORDER_HISTORY_ROWS_PER_PAGE, MOCK_URL } from '../../../scripts/utils/constants'; // eslint-disable-line
import { logger } from '../../../scripts/utils/logger';
import 'core-js/features/array/includes';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import auth from '../../../scripts/utils/auth';
import { apiHost } from '../../../scripts/common/common'; // eslint-disable-line

/**
 * Fire analytics on search submit
 */
function _trackAnalytics() {
  const { title = '' } = this.cache.i18nKeys;
  let ob = {
    linkType: 'internal',
    linkSection: 'dashboard',
    linkParentTitle: title.toLowerCase(),
    linkName: 'order list item'
  };
  const obKey = 'linkClick';
  const trackingKey = 'linkClicked';
  trackAnalytics(ob, obKey, trackingKey, undefined, false);
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
 * @param {string} orderDetailLink Order Detail link
 * @param {string} viewAllOrders
 */
function _tableSort(order, activeKeys, orderDetailLink, viewAllOrders) {
  const dataObject = {
    rowLink: `${orderDetailLink}?q=${order.orderNumber}&orderType=${order.orderType}&p=${encodeURIComponent(`${viewAllOrders}`)}`,
    row: []
  };
  activeKeys.forEach((key, index) => {
    let value = '';
    if (key === 'contact') {
      value = _processContacts.call(this, order[key]);
    } else {
      value = order[key];
    }
    dataObject.row[index] = {
      key,
      value,
      isRTE: ['contact'].includes(key)
    };
  });
  return dataObject;
}

/**
 * Processes data before rendering
 * @param {object} data JSON data object
 */
function _processTableData(data) {
  // Update i18n keys
  const {
    i18nKeys,
    savedPreferences,
    viewAllOrders,
    orderDetailLink,
    defaultFields,
    enabledFieldList
  } = this.cache;
  this.cache.tableData = $.extend(true, {}, data);
  data.labels = i18nKeys;
  // Activate fields which are enabled for render
  if (Array.isArray(data.orders)) {
    const preferenceList = typeof savedPreferences === 'string' ? savedPreferences.split(',') : [];
    const defaultFieldList = typeof defaultFields === 'string' ? defaultFields.split(',') : [];
    defaultFieldList.forEach(key => {
      if (!preferenceList.includes(key)) {
        preferenceList.push(key);
      }
    });
    const activeKeys = enabledFieldList.filter(key => preferenceList.includes(key));
    let allUnchecked = false;
    if (activeKeys.length === 0) {
      allUnchecked = true;
      activeKeys.push(...enabledFieldList);
    }
    data.orders = data.orders.map(order => _tableSort.call(this, order, activeKeys, orderDetailLink, viewAllOrders));
    data.orderHeadings = activeKeys.map(key => ({
      key,
      i18nKey: `cuhu.ordering.${key}`,
      sortOrder: 'desc'
    }));
    data.settingOptions = enabledFieldList.map(key => ({
      key,
      i18nKey: `cuhu.ordering.${key}`,
      isChecked: (activeKeys.includes(key) && !allUnchecked),
      isMandatory: defaultFieldList.includes(key)
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

/**
 * Opens order detail page for current order
 */
function _openOrderDetails() {
  const currentTarget = $(this);
  window.open(currentTarget.attr('href'), '_self');
}

/**
 * Stops event propagation of parent element in child context
 * @param {object} e Event object
 */
function _stopEvtProp(e) {
  e.stopPropagation();
}

function _saveSettings() {
  // Get selected preferences
  const $modalPreference = this.root.find('.js-ordering-card__modal-preference');
  let selectedFields = $.map($modalPreference.find('input:checked'), function (el) {
    return $(el).val();
  });
  if (selectedFields.length === 0) {
    // Assume all fields were selected
    selectedFields = $.map($modalPreference.find('input'), function (el) {
      return $(el).val();
    });
  }
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
      this.root.find('.js-ordering-card__save-error').addClass('d-none');
      this.root.find('.js-ordering-card__modal').modal('hide');
    } else {
      this.root.find('.js-ordering-card__save-error').removeClass('d-none');
    }
  }).fail(() => {
    this.root.find('.js-ordering-card__save-error').removeClass('d-none');
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
    this.cache.orderDetailLink = $('#ordDetailLink').val();
    this.cache.defaultFields = $('#ordDefaultFields').val();
    this.cache.enabledFields = $('#ordEnabledFields').val();
    this.cache.disabledFieldList = [];
    if (typeof this.cache.enabledFields === 'string') {
      this.cache.enabledFieldList = this.cache.enabledFields.split(',');
    }
    this.cache.savedPreferences = $('#ordSavedPreferences').val();
    this.cache.contactListTemplate = render.get('contactList');
    try {
      this.cache.i18nKeys = JSON.parse($('#ordI18nKeys').val());
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.log(e);
    }
  }
  bindEvents() {
    /* Bind jQuery events here */
    this.root
      .on('click', '.js-ordering-card__settings', this.openSettingsPanel)
      .on('click', '.js-ordering-card__modal-save', this.saveSettings)
      .on('click', '.js-ordering-card__row', this, function (e) {
        const $this = e.data;
        $this.openOrderDetails.apply(this, arguments);
        $this.trackAnalytics();
      })
      .on('click', '.js-ordering-card__row a', this.stopEvtProp); // Stops event propagation of order detail for links inside table row
  }
  renderTable(config) {
    const $this = this;
    if (typeof config === 'undefined') {
      config = {
        template: 'orderingCard',
        url: {
          //path: `${apiHost}/${API_ORDER_HISTORY}`,
          path: `${MOCK_URL}/orderingCardData.json`,
          data: {
            top: ORDER_HISTORY_ROWS_PER_PAGE
          }
        },
        ajaxConfig: {
          method: ajaxMethods.GET,
          showLoader: true,
          cancellable: true
        },
        beforeRender(data) {
          if (!data) {
            this.data = data = {
              isError: true
            };
          }
          return _processTableData.apply($this, [data]);
        },
        target: this.root
      };
    }
    if (config.url) {
      // Fetch API token and render table
      auth.getToken(({ data }) => {
        config.ajaxConfig.beforeSend = (jqXHR) => {
          jqXHR.setRequestHeader('Authorization', `Bearer ${data.access_token}`);
          jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        };
        config.ajaxConfig.cache = true;
        render.fn(config);
      });
    } else {
      render.fn(config);
    }
  }
  openSettingsPanel = (...args) => _openSettingsPanel.apply(this, args);
  saveSettings = (...args) => _saveSettings.apply(this, args);
  stopEvtProp = (...args) => _stopEvtProp.apply(this, args);
  trackAnalytics = () => _trackAnalytics.call(this);
  openOrderDetails(...args) {
    return _openOrderDetails.apply(this, args);
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.renderTable();
  }
}

export default OrderingCard;
