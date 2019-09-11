import $ from 'jquery';
import { router, route } from 'jqueryrouter';
import deparam from 'deparam.js';
import moment from 'moment';
import Lightpick from 'lightpick';
import 'bootstrap';
import 'core-js/features/array/includes';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import auth from '../../../scripts/utils/auth';
import { ajaxMethods, FINANCIAL_DATE_RANGE_PERIOD, DATE_FORMAT, EXT_EXCEL, EXT_PDF, DATE_RANGE_SEPARATOR, API_FINANCIAL_SUMMARY, DATE_RANGE_REGEX, dateTypes, DATE_REGEX, documentTypes, EVT_FINANCIAL_ERROR, EVT_FINANCIAL_ANALYTICS, EVT_FINANCIAL_FILTERS } from '../../../scripts/utils/constants';
import { resolveQuery, isMobileMode, getI18n } from '../../../scripts/common/common';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import { toast } from '../../../scripts/utils/toast';
import { $body } from '../../../scripts/utils/commonSelectors';
import { getURL } from '../../../scripts/utils/uri';
import { isIOS } from '../../../scripts/utils/browserDetect';
import file from '../../../scripts/utils/file';

/**
 * Returns type of date
 * @param {string} date Input date
 */
function _getDateRangeType(date) {
  return (DATE_RANGE_REGEX).test(date) ? dateTypes.RANGE : dateTypes.DATE;
}

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
  const { $status, $docType } = $this.cache;
  const { statementOfAccount = '' } = $this.cache.i18nKeys;
  let ob = {
    linkType: 'internal',
    linkSection: 'financials',
    linkParentTitle: $.trim(getI18n(statementOfAccount).toLowerCase())
  };
  const obKey = 'linkClick';
  const trackingKey = 'linkClicked';
  switch (type) {
    case 'reset': {
      ob.linkName = 'reset';
      break;
    }
    case 'search': {
      const status = $.trim($status.text());
      const docType = $.trim($docType.text()).toLowerCase();
      ob.linkName = 'search statement';
      ob.linkSelection = `customer name|${status}|dates choosen|${docType}|document number`;
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
  this.cache.statusList = data.status;
  const { documentTypeAll = 'cuhu.documenttype.all' } = this.cache.i18nKeys;
  const defaultDocumentTypes = Object.keys(documentTypes).map(key => ({
    key,
    desc: documentTypes[key]
  }));
  const allKey = {
    key: '',
    desc: documentTypeAll
  };
  const { documentType = [] } = data;
  data.documentType = [allKey].concat(documentType);
  this.cache.documentTypeList = [allKey].concat(defaultDocumentTypes);
  this.root.parents('.js-financials').trigger(EVT_FINANCIAL_FILTERS, [data.status, this.cache.documentTypeList]);
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
        data.dateRangeType = dateTypes.DATE;
        data.currentDatePlaceholder = data.datePlaceholder;
        if ((`${selectedStatus.key}`).toUpperCase() !== 'O') {
          data.dateRange = `${_getStartDate()} - ${data.currentDate}`;
          data.dateRangeType = dateTypes.RANGE;
          data.currentDatePlaceholder = data.dateRangePlaceholder;
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
        defaultQuery.customerkey = customerNumber ? customerNumber.key : '';
        this.cache.defaultQueryString = $.param(defaultQuery);
        this.initPostCache();
        this.initializeCalendar();
        this.setRoute(true);
      } else {
        const { statementOfAccount = '' } = this.cache.i18nKeys;
        this.root.trigger(EVT_FINANCIAL_ERROR, [
          $.trim(getI18n(statementOfAccount)).toLowerCase(),
          'financial search form load',
          $.trim(getI18n('cuhu.error.message')).toLowerCase()
        ]);
        this.root.find('.js-financial-statement__filter-section').removeClass('d-none');
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
  const $dateSelector = this.root.find('.js-financial-statement__date-range-input, .js-range-selector');
  const i18nKeys = this.cache.i18nKeys;
  if (selectedDate) {
    const dateRangeType = _getDateRangeType(selectedDate);
    const currentDatePlaceholder = dateRangeType === dateTypes.RANGE ? 'dateRangePlaceholder' : 'datePlaceholder';
    $dateSelector.attr('data-date-range-type', dateRangeType).data('dateRangeType', dateRangeType);
    $dateSelector.attr('placeholder', i18nKeys[currentDatePlaceholder]);
    $dateSelector.val(selectedDate).trigger('input');
    this.initializeCalendar((selectedDate.split(DATE_RANGE_SEPARATOR).length > 1));
  } else {
    const endDate = moment(Date.now()).format(DATE_FORMAT);
    if (
      typeof status === 'string'
      && ['O'].includes(status.toUpperCase())
    ) {
      $dateSelector.attr('data-date-range-type', dateTypes.DATE).data('dateRangeType', dateTypes.DATE);
      $dateSelector.attr('placeholder', i18nKeys.datePlaceholder);
      $dateSelector.val(endDate).trigger('input');
      this.initializeCalendar();
    } else {
      const startDate = _getStartDate();
      $dateSelector.attr('data-date-range-type', dateTypes.RANGE).data('dateRangeType', dateTypes.RANGE);
      $dateSelector.attr('placeholder', i18nKeys.dateRangePlaceholder);
      $dateSelector.val(`${startDate} - ${endDate}`).trigger('input');
      this.initializeCalendar(true);
    }
  }
}

/**
 * Backfills filter form with query data for consistency
 * @param {object} query Query object
 */
function _syncFields(query) {
  const { $filterForm, $findCustomer, $docType } = this.cache;
  const $statusField = $filterForm.find('.js-financial-statement__status');

  let dateRange = query['soa-date'];
  if (
    query['invoicedate-from']
    && query['invoicedate-to']
  ) {
    dateRange = `${query['invoicedate-from']} - ${query['invoicedate-to']}`;
  }
  $findCustomer.customSelect(query.customerkey).trigger('dropdown.change', [true]);
  $statusField.customSelect(query.status);
  $statusField.parents('.js-custom-dropdown')
    .find(`li>a[data-key="${query.status}"]`)
    .attr('data-selected-date', dateRange)
    .data('selectedDate', dateRange);
  $statusField.trigger('dropdown.change');
  $docType.customSelect(query['document-type'] || '');
  $filterForm.find('.js-financial-statement__document-number').val(query['document-number']);
}

/**
 * Gets current set filters
 */
function _getFilterQuery() {
  const { $filterForm, $findCustomer, $status, $docType } = this.cache;
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
  filterProps.customerkey = $findCustomer.customSelect();
  filterProps.status = $status.customSelect();
  filterProps['document-type'] = $docType.customSelect();
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
      queryString: this.getFilterQuery(),
      data: {
        isClick: !isInit
      }
    }, isInit);
  }
}
function _downloadPdfExcel(...args) {
  const [, type, el] = args;
  const $el = $(el);
  $el.attr('disabled', 'disabled');
  const paramsData = {};
  const { $filterForm, data, i18nKeys, $docType, $soaTitle } = this.cache;
  const statusDesc = $filterForm.find(`.js-financial-statement__status option[value="${$el.data('status')}"]`).text();
  const statusKey = $el.data('status');
  const docTypeDesc = $docType.text().trim();
  const docTypeKey = $el.data('documentType');
  const docNumber = $el.data('search');
  const soaTitle = $soaTitle.text();
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
    desc: getI18n(docTypeDesc)
  };
  paramsData.documentNumber = docNumber;
  paramsData.statusList = this.cache.statusList;
  paramsData.documentTypeList = this.cache.documentTypeList;
  auth.getToken(() => {
    const url = resolveQuery(this.cache.servletUrl, { extnType: type });
    file.get({
      url,
      data: {
        params: JSON.stringify(paramsData)
      },
      extension: _getExtension(type)
    }).then(() => {
      $el.removeAttr('disabled');
      if (!isIOS()) {
        this.root.trigger(EVT_FINANCIAL_ANALYTICS, ['downloadInvoice', 'invoice download', $el]);
      }
    }).catch(() => {
      toast.render(
        i18nKeys.fileDownloadErrorText,
        i18nKeys.fileDownloadErrorClose
      );
      $el.removeAttr('disabled');
      this.root.trigger(EVT_FINANCIAL_ERROR, [$.trim(soaTitle), $.trim($el.text()), getI18n(i18nKeys.fileDownloadErrorText)]);
    });
  });
}

function _getDefaultQueryString() {
  const { defaultQueryString, $findCustomer } = this.cache;
  const queryObject = deparam(defaultQueryString, false);
  // Retain current customer address selection
  queryObject.customerkey = $findCustomer.customSelect();
  return $.param(queryObject);
}

/**
 * Checks if entered date or date range is valid
 */
function _validateDateRange(e) {
  const ref = e.data;
  const $this = $(this);
  const { $dateRangePicker, $errorMsg } = ref.cache;
  const currentValue = $.trim($this.val());
  const currentType = $this.data('dateRangeType');
  const testRegex = currentType === dateTypes.RANGE ? DATE_RANGE_REGEX : DATE_REGEX;
  const dateRangeParts = currentValue.split(DATE_RANGE_SEPARATOR);
  let result = true;
  if (testRegex.test(currentValue)) {
    dateRangeParts.forEach(part => {
      if (!moment(part.trim()).isValid()) {
        result = result && false;
      }
    });
  } else {
    result = false;
  }
  const fn = result ? 'removeClass' : 'addClass';
  $this[fn]('has-error');
  $dateRangePicker[fn]('has-error');
  $errorMsg[result ? 'addClass' : 'removeClass']('d-none');
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
    this.cache.$dateRange = this.root.find('.js-financial-statement__date-range-input');
    this.cache.$dateRangePicker = this.root.find('.js-financial-statement__date-range');
    this.cache.$errorMsg = this.root.find('.js-financial-statement__date-range-error');
    this.cache.$rangeSelector = this.root.find('.js-range-selector');
    this.cache.$modal = this.root.find('.js-cal-cont__modal');
    this.cache.$status = this.root.find('.js-financial-statement__status');
    this.cache.$docType = this.root.find('.js-financial-statement__document-type');
    this.cache.$soaTitle = this.root.find('.js-financial-statement__select-customer-heading');
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
    const rangeSelectorValue = $rangeSelector.val();
    $dateRange.val(rangeSelectorValue).trigger('input');
    $status.find('option')
      .eq($status.prop('selectedIndex'))
      .attr('data-selected-date', rangeSelectorValue)
      .data('selectedDate', rangeSelectorValue);
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
      .on('dropdown.change', '.js-financial-statement__find-customer', function () {
        const [, noReset] = arguments;
        $this.setSelectedCustomer($(this).data('key'), noReset);
      })
      .on('dropdown.change', '.js-financial-statement__status', function () {
        const self = $(this);
        const currentTarget = self.parents('.js-custom-dropdown').find(`li>a[data-key="${self.data('key')}"]`);
        $this.setDateFilter(self.customSelect(), currentTarget.data('selectedDate'));
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
      })
      .on('input', '.js-financial-statement__date-range-input', this, this.validateDateRange);
    this.root.parents('.js-financials').on('financial.filedownload', this, this.downloadPdfExcel);
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
    const { $dateRange } = this.cache;
    if ($dateRange.hasClass('has-error')) {
      $dateRange.focus();
      return;
    }
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
    $status.parents('.js-custom-dropdown').find('.js-custom-dropdown-li').each(function () {
      $(this).removeData('selectedDate').removeAttr('data-selected-date');
    });
    this.syncFields(deparam(defaultQueryString, false));
    router.set({
      route: '#/',
      queryString: defaultQueryString
    });
  }
  trackAnalytics = (type) => _trackAnalytics.call(this, type);
  validateDateRange() {
    return _validateDateRange.apply(this, arguments);
  }
  init() {
    this.initCache();
    this.bindEvents();
    this.renderFilters();
  }
}

export default FinancialStatement;
