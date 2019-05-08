import $ from 'jquery';
import { router, route } from 'jqueryrouter';
import deparam from 'jquerydeparam';
import moment from 'moment';
import Lightpick from 'lightpick';
import 'bootstrap';
import 'core-js/features/array/includes';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import { fileWrapper } from '../../../scripts/utils/file';
import auth from '../../../scripts/utils/auth';
import { ajaxMethods, API_FINANCIAL_SUMMARY, FINANCIAL_DATE_RANGE_PERIOD, DATE_FORMAT, EXT_EXCEL, EXT_PDF } from '../../../scripts/utils/constants';
import { apiHost, resolveQuery } from '../../../scripts/common/common';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import { toast } from '../../../scripts/utils/toast';

function _trackAnalytics(type) {

  const $this = this;
  const { $filterForm } = $this.cache;
  const { statementOfAccount } = $this.cache.i18nKeys || '';

  let ob = {
    linkType: 'internal',
    linkSection: 'financials',
    linkParentTitle: statementOfAccount.toLowerCase()
  };
  const obKey = 'linkClick';
  const trackingKey = 'linkClicked';
  switch (type) {
    case 'reset': {
      ob.linkName = 'reset search';
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
    data.customerData.sort((a, b) => {
      if (a.desc.toUpperCase() < b.desc.toUpperCase()) {
        return -1;
      }
      if (a.desc.toUpperCase() > b.desc.toUpperCase()) {
        return 1;
      }
      return 0;
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
    data.dateRange = data.currentDate = moment(Date.now()).format(DATE_FORMAT);
    const [selectedCustomerData] = data.customerData;
    data.selectedCustomerData = selectedCustomerData;
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
        const [customerKey] = data.customerData;
        const [defaultStatus] = data.status;
        const [docType] = data.documentType;
        defaultQuery.status = defaultStatus.key;
        defaultQuery['document-type'] = docType.key;
        defaultQuery['invoicedate-from'] = moment().format(DATE_FORMAT);
        defaultQuery.customerkey = customerKey.key;
        this.cache.defaultQueryString = $.param(defaultQuery);
        this.initPostCache();
        this.initializeCalendar();
        this.setRoute(true);
      }
    });
  });
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
 * Sets date filter
 * @param {string} status Status
 * @param {string} selectedDate Selected date
 */
function _setDateFilter(status, selectedDate) {
  const $dateSelector = this.root.find('.js-financial-statement__date-range, .js-range-selector');
  if (selectedDate) {
    $dateSelector.val(selectedDate);
    this.initializeCalendar((selectedDate.split(' - ').length > 1));
  } else {
    const endDate = moment(Date.now()).format(DATE_FORMAT);
    if (
      typeof status === 'string'
      && ['open'].includes(status.toLowerCase())
    ) {
      $dateSelector.val(endDate);
      this.initializeCalendar();
    } else {
      const startDate = moment(Date.now() - (FINANCIAL_DATE_RANGE_PERIOD * 24 * 60 * 60 * 1000)).format(DATE_FORMAT);
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
  let dateRange = query['invoicedate-from'];
  if (query['invoicedate-to']) {
    dateRange += ` - ${query['invoicedate-to']}`;
  }
  $findCustomer.val(query.customerkey).trigger('change', [true]);
  $statusField.val(query.status);
  $statusField.find(`option[value="${query.status}"]`).data('selectedDate', dateRange);
  $statusField.trigger('change');
  $filterForm.find('.js-financial-statement__document-type').val(query['document-type']);
  $filterForm.find('.js-financial-statement__document-number').val(query.search);
}

/**
 * Gets current set filters
 */
function _getFilterQuery() {
  const { $filterForm, $findCustomer } = this.cache;
  const filters = $filterForm.serialize();
  const filterProps = deparam(filters);
  if (filterProps.daterange) {
    const [orderdateFrom, orderdateTo] = filterProps.daterange.split(' - ');
    filterProps['invoicedate-from'] = orderdateFrom.trim();
    if (orderdateTo) {
      filterProps['invoicedate-to'] = orderdateTo.trim();
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
  const $this = this;
  const paramsData = {};
  const { $filterForm, $dateRange, data, i18nKeys } = $this.cache;
  const statusDesc = $filterForm.find('.js-financial-statement__status option:selected').text();
  const statusKey = $filterForm.find('.js-financial-statement__status option:selected').val();
  const docTypeDesc = $filterForm.find('.js-financial-statement__document-type option:selected').text();
  const docTypeKey = $filterForm.find('.js-financial-statement__document-type option:selected').val();
  const docNumber = $filterForm.find('.js-financial-statement__document-number').val();
  const dateRangeArray = $dateRange.val().split(' - ');
  paramsData.startDate = dateRangeArray[0];

  if (dateRangeArray.length > 1) {
    paramsData.endDate = dateRangeArray[1];
  }
  paramsData.customerData = data.selectedCustomerData;
  paramsData.status = {
    'key': statusKey,
    'desc': statusDesc
  };
  paramsData.documentType = {
    'key': docTypeKey,
    'desc': docTypeDesc
  };
  paramsData.documentNumber = docNumber;
  auth.getToken(({ data: authData }) => {
    const requestBody = {};
    requestBody.params = JSON.stringify(paramsData);
    requestBody.token = authData.access_token;
    const url = resolveQuery($this.cache.servletUrl, { extnType: type });
    fileWrapper({
      url,
      data: {
        params: JSON.stringify(paramsData),
        token: authData.access_token
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
  const queryObject = deparam(defaultQueryString);
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
    } catch (e) {
      this.cache.i18nKeys = {};
      this.cache.servletUrl = '';
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
  initializeCalendar(isRange) {
    const $this = this;
    const { $rangeSelector, dateConfig, picker } = this.cache;
    const rangeSelectorEl = $rangeSelector && $rangeSelector.length ? $rangeSelector[0] : null;
    if (rangeSelectorEl) {
      const currentConfig = $.extend({}, dateConfig);
      if (isRange) {
        $.extend(currentConfig, {
          field: rangeSelectorEl,
          singleDate: false,
          numberOfMonths: 2,
          separator: ' - ',
          onSelectStart() {
            $this.root.find('.js-calendar').attr('disabled', 'disabled');
          },
          onSelectEnd() {
            $this.root.find('.js-calendar').removeAttr('disabled');
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
      _disableCalendarNext(this);
    }
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
      .on('click', '.js-financial-statement__find-customer', () => {
        $this.trackAnalytics();
      })
      .on('change', '.js-financial-statement__status', (e) => {
        const currentTarget = $(e.target).find('option').eq(e.target.selectedIndex);
        this.setDateFilter(currentTarget.text(), currentTarget.data('selectedDate'));
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
    this.root.parents('.js-financials').on('downloadFinancialPdfExcel', this, this.downloadPdfExcel);
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
    this.syncFields(deparam(defaultQueryString));
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
