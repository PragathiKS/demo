import $ from 'jquery';
import { router, route } from 'jqueryrouter';
import deparam from 'deparam.js';
import moment from 'moment';
import Lightpick from 'lightpick';
import 'bootstrap';
import 'core-js/features/array/includes';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import { fileWrapper } from '../../../scripts/utils/file';
import auth from '../../../scripts/utils/auth';
import { ajaxMethods, API_FINANCIAL_SUMMARY, FINANCIAL_DATE_RANGE_PERIOD, DATE_FORMAT, EXT_EXCEL, EXT_PDF, DATE_RANGE_SEPARATOR } from '../../../scripts/utils/constants';
import { resolveQuery, isMobileMode } from '../../../scripts/common/common';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import { toast } from '../../../scripts/utils/toast';
import { getURL } from '../../../scripts/utils/uri';
import { $body } from '../../../scripts/utils/commonSelectors';

/**
 * Returns formatted start date by substracting FINANCIAL_DATE_RANGE_PERIOD from current date
 */
function _getStartDate() {
  return moment(Date.now() - (FINANCIAL_DATE_RANGE_PERIOD * 24 * 60 * 60 * 1000)).format(DATE_FORMAT);
}

/**
 * Disables calendar next button
 * @param {object} $this Class context
 */
function _disableCalendarNext($this) {
  // Check if current visible months contain current month
  const currentMonth = moment().month();
  const currentYear = moment().year();
  const visibleMonths = $.map($this.root.find('.lightpick__select-months'), el => +$(el).val());
  const visibleYears = $.map($this.root.find('.lightpick__select-years'), el => +$(el).val());
  if (
    visibleMonths.includes(currentMonth)
    && visibleYears.includes(currentYear)
  ) {
    $this.root.find('.js-calendar-next').attr('disabled', 'disabled');
  } else {
    $this.root.find('.js-calendar-next').removeAttr('disabled');
  }
}

/**
 * Initializes calendar
 * @param {boolean} isRange Switch for range selector
 */
function _initializeCalendar(isRange) {
  const { $rangeSelector, dateConfig, picker } = this.cache;
  const rangeSelectorEl = $rangeSelector && $rangeSelector.length ? $rangeSelector[0] : null;
  if (rangeSelectorEl) {
    const currentConfig = $.extend({}, dateConfig);
    if (isRange) {
      $.extend(currentConfig, {
        field: rangeSelectorEl,
        singleDate: false,
        numberOfMonths: (isMobileMode() ? 1 : 2),
        separator: DATE_RANGE_SEPARATOR,
        onSelectStart: () => {
          this.root.find('.js-calendar').attr('disabled', 'disabled');
        },
        onSelectEnd: () => {
          this.root.find('.js-calendar').removeAttr('disabled');
        }
      });
    } else {
      $.extend(currentConfig, {
        field: rangeSelectorEl
      });
    }
    if (picker) {
      picker.destroy();
    }
    this.cache.picker = new Lightpick(currentConfig);
    if (isRange && $rangeSelector.length) {
      const [, endDate] = $rangeSelector.val().split(DATE_RANGE_SEPARATOR);
      this.cache.picker.gotoDate(endDate);
    }
    _disableCalendarNext(this);
    $(window).off('media.changed').on('media.changed', () => {
      this.initializeCalendar(isRange);
    });
  }
}

function _trackAnalytics(type) {
  const $this = this;
  const { $filterForm } = $this.cache;
  const { statementOfAccount = '' } = $this.cache.i18nKeys;
  let ob = {
    linkType: 'internal',
    linkSection: 'financials',
    linkParentTitle: statementOfAccount.toLowerCase()
  };
  const obKey = 'linkClick';
  const trackingKey = 'linkClicked';
  switch (type) {
    case 'reset': {
      ob.linkName = 'reset';
      break;
    }
    case 'search': {
      const status = $filterForm.find('.js-financial-statement__status option:selected').text().toLowerCase() || '';
      const docType = $filterForm.find('.js-financial-statement__document-type option:selected').text().toLowerCase() || '';
      const docNumber = $filterForm.find('.js-financial-statement__document-number').val().toLowerCase() || '';

      ob.linkName = 'search statement';
      ob.linkSelection = `customer name|${status}|dates choosen|${docType}|${docNumber}`;
      break;
    }
    default: {
      break;
    }
  }
  trackAnalytics(ob, obKey, trackingKey, undefined, false);
}

/**
 * Processes financial data
 * @param {object} data Financial data
 */
function _processFinancialStatementData(data) {
  data = $.extend(true, data, this.cache.i18nKeys);
  if (!data.isError) {
    if (!data.customerData) {
      data.isError = true;
    } else {
      data.customerData.sort((a, b) => {
        if (a && typeof a.customerName === 'string') {
          a = a.customerName.toUpperCase();
        }
        if (b && typeof b.customerName === 'string') {
          b = b.customerName.toUpperCase();
        }
        if (a < b) {
          return -1;
        }
        if (a > b) {
          return 1;
        }
        return 0;
      });
      try {
        data.customerData.forEach(customerOb => {
          customerOb.key = customerOb.customerNumber;
          customerOb.desc = `${customerOb.customerNumber} - ${customerOb.customerName} - ${customerOb.info.city}`;
        });
        // Resolve i18n keys for calendar modal
        data.selectDatesI18n = data.selectDates;
        data.closeBtnI18n = data.closeBtn;
        data.setDatesBtnI18n = data.setDates;
        data.dateRangeLabelI18n = data.selectDateRangeLabel;
        // Remove duplicate properties
        delete data.selectDates;
        delete data.closeBtn;
        delete data.setDates;
        const [selectedStatus] = data.status;
        data.dateRange = data.currentDate = moment(Date.now()).format(DATE_FORMAT);
        if ((`${selectedStatus.key}`).toUpperCase() !== 'O') {
          data.dateRange = `${_getStartDate()} - ${data.currentDate}`;
        }
        const [selectedCustomerData] = data.customerData;
        data.selectedCustomerData = selectedCustomerData;
      } catch (e) {
        data.isError = true;
      }
    }
  }
  this.cache.data = data;
  return data;
}

/**
 * Renders address details
 */
function _renderAddressDetail() {
  render.fn({
    template: 'financialAddressDetail',
    target: '.js-financial-statement__customer-detail',
    data: this.cache.data
  });
}

/**
 * Sets selected customer
 * @param {string} key Key
 */
function _setSelectedCustomer(key, noReset) {
  this.cache.data.customerData.forEach(item => {
    if (item.key === key) {
      this.cache.data.selectedCustomerData = item;
    }
  });
  _renderAddressDetail.apply(this);
  if (!noReset) {
    this.resetFilters();
  }
}

/**
 * Renders filters
 */
function _renderFilters() {
  const $this = this;
  const { tempCustomerList } = this.cache;
  auth.getToken(({ data: authData }) => {
    const url = {
      path: getURL(API_FINANCIAL_SUMMARY)
    };
    if (
      typeof tempCustomerList === 'string'
      && tempCustomerList.trim().length
    ) {
      url.data = `customernumber=${tempCustomerList.trim()}`;
    }
    render.fn({
      template: 'financialStatement',
      url,
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
      beforeRender(data) {
        if (!data) {
          this.data = data = {
            isError: true
          };
        }
        return _processFinancialStatementData.apply($this, [data]);
      }
    }, (data) => {
      if (!data.isError && Array.isArray(data.customerData)) {
        const defaultQuery = {};
        const [customerNumber] = data.customerData;
        const [defaultStatus] = data.status;
        const [docType] = data.documentType;
        defaultQuery.status = defaultStatus.key;
        defaultQuery['document-type'] = docType.key;
        defaultQuery['soa-date'] = moment().format(DATE_FORMAT);
        if ((`${defaultQuery.status}`).toUpperCase() !== 'O') {
          defaultQuery['invoicedate-from'] = _getStartDate();
          defaultQuery['invoicedate-to'] = moment().format(DATE_FORMAT);
        }
        defaultQuery.customerkey = customerNumber.key;
        this.cache.defaultQueryString = $.param(defaultQuery);
        this.initPostCache();
        this.initializeCalendar();
        this.setRoute(true);
      }
    });
  });
}

/**
 * Sets date filter
 * @param {string} status Status
 * @param {string} selectedDate Selected date
 */
function _setDateFilter(status, selectedDate) {
  const $dateSelector = this.root.find('.js-financial-statement__date-range, .js-range-selector');
  if (selectedDate) {
    $dateSelector.val(selectedDate);
    this.initializeCalendar((selectedDate.split(DATE_RANGE_SEPARATOR).length > 1));
  } else {
    const endDate = moment(Date.now()).format(DATE_FORMAT);
    if (
      typeof status === 'string'
      && ['O'].includes(status.toUpperCase())
    ) {
      $dateSelector.val(endDate);
      this.initializeCalendar();
    } else {
      const startDate = _getStartDate();
      $dateSelector.val(`${startDate} - ${endDate}`);
      this.initializeCalendar(true);
    }
  }
}

/**
 * Backfills filter form with query data for consistency
 * @param {object} query Query object
 */
function _syncFields(query) {
  const { $filterForm, $findCustomer } = this.cache;
  const $statusField = $filterForm.find('.js-financial-statement__status');

  let dateRange = query['soa-date'];
  if (
    query['invoicedate-from']
    && query['invoicedate-to']
  ) {
    dateRange = `${query['invoicedate-from']} - ${query['invoicedate-to']}`;
  }
  $findCustomer.val(query.customerkey).trigger('change', [true]);
  $statusField.val(query.status);
  $statusField.find(`option[value="${query.status}"]`).data('selectedDate', dateRange);
  $statusField.trigger('change');
  $filterForm.find('.js-financial-statement__document-type').val(query['document-type']);
  $filterForm.find('.js-financial-statement__document-number').val(query['document-number']);
}

/**
 * Gets current set filters
 */
function _getFilterQuery() {
  const { $filterForm, $findCustomer } = this.cache;
  const filters = $filterForm.serialize();
  const filterProps = deparam(filters, false);
  if (filterProps.daterange) {
    const [invoiceDateFrom, invoiceDateTo] = filterProps.daterange.split(DATE_RANGE_SEPARATOR);
    filterProps['soa-date'] = invoiceDateFrom.trim();
    if (invoiceDateTo) {
      filterProps['invoicedate-from'] = invoiceDateFrom.trim();
      filterProps['soa-date'] = filterProps['invoicedate-to'] = invoiceDateTo.trim();
    }
    delete filterProps.daterange;
  }
  filterProps.customerkey = $findCustomer.val();
  Object.keys(filterProps).forEach(key => {
    if (!filterProps[key]) {
      delete filterProps[key];
    }
  });
  const returnQueryString = `?${$.param(filterProps)}`;
  return returnQueryString;
}

/**
 * Returns extension based on file type
 * @param {string} type File type
 */
function _getExtension(type) {
  if (type === 'excel') {
    return EXT_EXCEL;
  }
  return EXT_PDF;
}

/**
 * Sets current filter route
 * @param {boolean} isInit Initialize flag
 */
function _setRoute(isInit = false) {
  if (window.location.hash && isInit) {
    router.init();
  } else {
    router.set({
      route: '#/',
      queryString: this.getFilterQuery()
    }, isInit);
  }
}
function _downloadPdfExcel(...args) {
  const [, type, el] = args;
  const $el = $(el);
  $el.attr('disabled', 'disabled');
  const paramsData = {};
  const { $filterForm, data, i18nKeys } = this.cache;
  const statusDesc = $filterForm.find(`.js-financial-statement__status option[value="${$el.data('status')}"]`).text();
  const statusKey = $el.data('status');
  const docTypeDesc = $filterForm.find(`.js-financial-statement__document-type option[value="${$el.data('documentType')}"]`).text();
  const docTypeKey = $el.data('documentType');
  const docNumber = $el.data('search');
  paramsData.soaDate = paramsData.startDate = $el.data('soaDate');
  if ($el.data('invoiceDateTo')) {
    paramsData.startDate = $el.data('invoiceDateFrom');
    paramsData.endDate = $el.data('invoiceDateTo');
  }
  paramsData.customerData = data.selectedCustomerData;
  paramsData.status = {
    key: statusKey,
    desc: statusDesc
  };
  paramsData.documentType = {
    key: docTypeKey,
    desc: docTypeDesc
  };
  paramsData.documentNumber = docNumber;
  auth.getToken(() => {
    const url = resolveQuery(this.cache.servletUrl, { extnType: type });
    fileWrapper({
      url,
      data: {
        params: JSON.stringify(paramsData)
      },
      extension: _getExtension(type)
    }).then(() => {
      $el.removeAttr('disabled');
    }).catch(() => {
      toast.render(
        i18nKeys.fileDownloadErrorText,
        i18nKeys.fileDownloadErrorClose
      );
      $el.removeAttr('disabled');
    });
  });
}

function _getDefaultQueryString() {
  const { defaultQueryString, $findCustomer } = this.cache;
  const queryObject = deparam(defaultQueryString, false);
  // Retain current customer address selection
  queryObject.customerkey = $findCustomer.val();
  return $.param(queryObject);
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
      this.cache.servletUrl = this.root.find('#downloadPdfExcelServletUrl').val();
      this.cache.tempCustomerList = $body.find('#tempCustomerList').val();
    } catch (e) {
      this.cache.i18nKeys = {};
      this.cache.servletUrl = '';
      this.cache.tempCustomerList = '';
      logger.error(e);
    }
  }
  initPostCache() {
    this.cache.$filterForm = this.root.find('.js-financial-statement__filters');
    this.cache.$findCustomer = this.root.find('.js-financial-statement__find-customer');
    this.cache.$dateRange = this.root.find('.js-financial-statement__date-range');
    this.cache.$rangeSelector = this.root.find('.js-range-selector');
    this.cache.$modal = this.root.find('.js-cal-cont__modal');
    this.cache.$status = this.root.find('.js-financial-statement__status');
    this.cache.dateConfig = {
      singleDate: true,
      numberOfMonths: 1,
      skip: 1,
      inline: true,
      maxDate: Date.now(),
      dropdowns: false,
      format: DATE_FORMAT
    };
  }
  submitDateRange() {
    const { $dateRange, $rangeSelector, $modal, $status } = this.cache;
    $dateRange.val($rangeSelector.val());
    $status.find('option').eq($status.prop('selectedIndex')).data('selectedDate', $rangeSelector.val());
    $modal.modal('hide');
  }
  navigateCalendar(e) {
    const $this = e.data;
    const action = $(this).data('action');
    const $defaultCalendarNavBtn = $this.root.find(`.lightpick__${action}`);
    if ($defaultCalendarNavBtn.length) {
      let evt = document.createEvent('MouseEvents');
      evt.initEvent('mousedown', true, true);
      $defaultCalendarNavBtn[0].dispatchEvent(evt); // JavaScript mousedown event
      _disableCalendarNext($this);
    }
  }
  initializeCalendar() {
    return _initializeCalendar.apply(this, arguments);
  }
  renderFilters() {
    return _renderFilters.apply(this, arguments);
  }
  bindEvents() {
    const $this = this;
    route((...args) => {
      const [config, , query] = args;
      if (config.hash) {
        this.syncFields(query);
      }
    });
    this.root
      .on('change', '.js-financial-statement__find-customer', function () {
        const [, noReset] = arguments;
        $this.setSelectedCustomer($(this).val(), noReset);
      })
      .on('change', '.js-financial-statement__status', (e) => {
        const currentTarget = $(e.target).find('option').eq(e.target.selectedIndex);
        this.setDateFilter(currentTarget.val(), currentTarget.data('selectedDate'));
      })
      .on('click', '.js-financial-statement__date-range', () => {
        this.openDateSelector();
      })
      .on('click', '.js-calendar', () => {
        this.submitDateRange();
      })
      .on('click', '.js-calendar-nav', this, this.navigateCalendar)
      .on('click', '.js-financial-statement__submit', this.populateResults)
      .on('click', '.js-financial-statement__reset', () => {
        this.resetFilters();
        this.trackAnalytics('reset');
      });
    this.root.parents('.js-financials').on('financial.filedownload', this, this.downloadPdfExcel);
    $(document).on('click', '#downloadPdf', function () {
      window.open($(this).attr('href'), '_blank');
    });
  }
  openDateSelector() {
    this.cache.$modal.modal('show');
  }
  downloadPdfExcel(...args) {
    const [e] = args;
    _downloadPdfExcel.apply(e.data, args);
  }
  setDateFilter() {
    return _setDateFilter.apply(this, arguments);
  }
  setSelectedCustomer() {
    return _setSelectedCustomer.apply(this, arguments);
  }
  populateResults = () => {
    this.trackAnalytics('search');
    this.setRoute();
  }
  getFilterQuery() {
    return _getFilterQuery.apply(this, arguments);
  }
  setRoute() {
    return _setRoute.apply(this, arguments);
  }
  syncFields() {
    return _syncFields.apply(this, arguments);
  }
  resetFilters() {
    const { $status } = this.cache;
    const defaultQueryString = _getDefaultQueryString.apply(this);
    $status.find('option').each(function () {
      $(this).removeData();
    });
    this.syncFields(deparam(defaultQueryString, false));
    router.set({
      route: '#/',
      queryString: defaultQueryString
    });
  }
  trackAnalytics = (type) => _trackAnalytics.call(this, type);
  init() {
    this.initCache();
    this.bindEvents();
    this.renderFilters();
  }
}

export default FinancialStatement;
