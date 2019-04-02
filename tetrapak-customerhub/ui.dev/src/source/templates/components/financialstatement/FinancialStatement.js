import $ from 'jquery';
import 'core-js/features/array/includes';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import auth from '../../../scripts/utils/auth';
import { apiHost } from '../../../scripts/common/common';
import { ajaxMethods, API_FINANCIAL_SUMMARY } from '../../../scripts/utils/constants';



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
      });
  }
  setSelectedCustomer() {
    _setSelectedCustomer.apply(this, arguments);
  }
  init() {
    this.initCache();
    this.bindEvents();
    this.renderFilters();
  }
}

export default FinancialStatement;
