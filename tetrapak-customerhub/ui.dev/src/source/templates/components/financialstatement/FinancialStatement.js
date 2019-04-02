import $ from 'jquery';
import moment from 'moment';
import 'core-js/features/array/includes';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import auth from '../../../scripts/utils/auth';
import { apiHost } from '../../../scripts/common/common';
import { ajaxMethods, API_FINANCIAL_SUMMARY, FINANCIAL_DATE_RANGE_PERIOD, DATE_FORMAT } from '../../../scripts/utils/constants';

function _processFinancialStatementData(data) {
  data.customerData.sort((a, b) => {
    if (a.desc.toUpperCase() < b.desc.toUpperCase()) {
      return -1;
    }
    if (a.desc.toUpperCase() > b.desc.toUpperCase()) {
      return 1;
    }
    return 0;
  });
  data = $.extend(true, data, this.cache.i18nKeys);
  // Resolve i18n keys for calendar modal
  data.selectDatesI18n = data.selectDates;
  data.closeBtnI18n = data.closeBtn;
  data.setDatesBtnI18n = data.setDates;
  data.dateRangeLabelI18n = data.selectDateRangeLabel;
  // Remove duplicate properties
  delete data.selectDates;
  delete data.closeBtn;
  delete data.setDates;
  data.dateRange = data.currentDate = moment(Date.now()).format(DATE_FORMAT);
  const [selectedCustomerData] = data.customerData;
  data.selectedCustomerData = selectedCustomerData;
  this.cache.data = data;
  return data;
}

function _renderAddressDetail() {
  render.fn({
    template: 'financialAddressDetail',
    target: '.tp-financial-statement__customer-detail',
    data: this.cache.data
  });
}

function _setSelectedCustomer(key) {
  this.cache.data.customerData.forEach(item => {
    if (item.key === key) {
      this.cache.data.selectedCustomerData = item;
    }
  });
  _renderAddressDetail.apply(this);
}

function _renderFilters() {
  auth.getToken(({ data: authData }) => {
    render.fn({
      template: 'financialStatement',
      url: {
        path: `${apiHost}/${API_FINANCIAL_SUMMARY}`
      },
      target: '.js-financial-statement__select-customer-dropdown',
      ajaxConfig: {
        method: ajaxMethods.GET,
        beforeSend(jqXHR) {
          jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
          jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        cache: true,
        showLoader: true,
        cancellable: true
      },
      beforeRender: (...args) => _processFinancialStatementData.apply(this, args)
    });
  });
}

function _setDateFilter(status) {
  const $dateSelector = this.root.find('.js-financial-statement__date-range, .js-range-selector');
  const endDate = moment(Date.now()).format(DATE_FORMAT);
  if (
    typeof status === 'string'
    && ['open'].includes(status.toLowerCase())
  ) {
    $dateSelector.val(endDate);
  } else {
    const startDate = moment(Date.now() - (FINANCIAL_DATE_RANGE_PERIOD * 24 * 60 * 60 * 1000)).format(DATE_FORMAT);
    $dateSelector.val(`${startDate} - ${endDate}`);
  }
}

class FinancialStatement {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};

  initCache() {
    this.cache.configJson = this.root.find('.js-financial-statement__config').text();
    try {
      this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
  }

  renderFilters() {
    return _renderFilters.apply(this, arguments);
  }

  bindEvents() {
    this.root
      .on('change', '.js-financial-statement__find-customer', (e) => {
        this.setSelectedCustomer(e.target.value);
      })
      .on('change', '.js-financial-statement__status', (e) => {
        this.setDateFilter($(e.target).find('option').eq(e.target.selectedIndex).text());
      });
  }
  setDateFilter() {
    return _setDateFilter.apply(this, arguments);
  }
  setSelectedCustomer() {
    return _setSelectedCustomer.apply(this, arguments);
  }
  init() {
    this.initCache();
    this.bindEvents();
    this.renderFilters();
  }
}

export default FinancialStatement;
