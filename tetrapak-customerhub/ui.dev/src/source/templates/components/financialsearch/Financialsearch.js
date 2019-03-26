import $ from 'jquery';
import 'core-js/features/array/includes';
import { render } from '../../../scripts/utils/render';
import { ajaxMethods } from '../../../scripts/utils/constants';
import { logger } from '../../../scripts/utils/logger';

function _processOrderSearchData(data) {
  data.customerData.sort((a, b) => {
    if (a.desc.toUpperCase() < b.desc.toUpperCase()) { return -1; }
    if (a.desc.toUpperCase() > b.desc.toUpperCase()) { return 1; }
    return 0;
  });
  data = $.extend(true, data, this.cache.config);
  const [selectedCustomerData] = data.customerData;
  data.selectedCustomerData = selectedCustomerData;
  this.cache.data = data;
  return data;
}

function _renderAddressDetail() {
  this.root.find('.tp-financial-search__account-number').text(this.cache.data.selectedCustomerData.info.acountNo);
  this.root.find('.tp-financial-search__address-title').text(this.cache.data.selectedCustomerData.info.title);
  this.root.find('.tp-financial-search__address-detail').text(this.cache.data.selectedCustomerData.info.address);
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
  render.fn({
    template: 'financialSearch',
    url: `../../apps/settings/wcm/designs/customerhub/jsonData/financialSearch.json`,
    target: '.js-financial-search__select-customer-dropdown',
    ajaxConfig: {
      method: ajaxMethods.GET,
      cache: true,
      showLoader: true,
      cancellable: true
    },
    beforeRender: (...args) => _processOrderSearchData.apply(this, args)
  }, () => {
    _renderAddressDetail.apply(this);
  });

}
class FinancialSearch {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};

  initCache() {
    this.cache.configJson = this.root.find('.js-financial-search__config').text();
    try {
      this.cache.config = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.config = {};
      logger.error(e);
    }
  }

  renderFilters() {
    return _renderFilters.apply(this, arguments);
  }

  bindEvents() {
    this.root
      .on('change', '.js-financial-search__find-customer', (e) => {
        this.setSelectedCustomer(e.target.value);
      });
  }
  setSelectedCustomer(data) {
    _setSelectedCustomer.apply(this, [data]);
  }
  init() {
    this.initCache();
    this.bindEvents();
    this.renderFilters();
  }
}

export default FinancialSearch;
