import $ from 'jquery';
import moment from 'moment';
import Lightpick from 'lightpick';
import 'bootstrap';
import 'core-js/features/array/includes';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import auth from '../../../scripts/utils/auth';
import { ajaxMethods, API_FINANCIAL_SUMMARY, FINANCIAL_DATE_RANGE_PERIOD, DATE_FORMAT } from '../../../scripts/utils/constants';
import { apiHost } from '../../../scripts/common/common';

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
        this.initPostCache();
        this.initializeCalendar();
        this.root.trigger(this.cache.summaryRenderEvent);
      }
    });
  });
}

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
    this.cache.summaryRenderEvent = 'financialSummary.render';
  }
  initPostCache() {
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
          selectForward: true,
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
    this.root
      .on('change', '.js-financial-statement__find-customer', (e) => {
        this.setSelectedCustomer(e.target.value);
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
      .on('reset', '.js-financial-statement__filters', () => {
        setTimeout(() => {
          const { $status } = this.cache;
          $status.find('option').each(function () {
            $(this).removeData();
          });
          this.populateResults();
        }, 0);
      });
  }
  openDateSelector() {
    this.cache.$modal.modal('show');
  }
  setDateFilter() {
    return _setDateFilter.apply(this, arguments);
  }
  setSelectedCustomer() {
    $('.js-financial-statement__reset').trigger('click');
    const { $dateRange, $rangeSelector } = this.cache;
    $rangeSelector.val($dateRange.val());
    this.initializeCalendar();
    return _setSelectedCustomer.apply(this, arguments);
  }
  populateResults = () => {
    this.root.trigger(this.cache.summaryRenderEvent);
  }
  init() {
    this.initCache();
    this.bindEvents();
    this.renderFilters();
  }
}

export default FinancialStatement;
