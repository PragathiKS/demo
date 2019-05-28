import $ from 'jquery';
import { router, route } from 'jqueryrouter';
import deparam from 'jquerydeparam';
import moment from 'moment';
import Lightpick from 'lightpick';
import 'bootstrap';
import 'core-js/features/array/includes';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import { ajaxMethods, ORDER_HISTORY_ROWS_PER_PAGE, DATE_FORMAT } from '../../../scripts/utils/constants';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import { sanitize, getI18n } from '../../../scripts/common/common';
//import auth from '../../../scripts/utils/auth';

/**
 * Disables calendar next button if visible months has current month
 * @param {object} $this Current class object
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
 * Processes data before rendering
 * @param {object} data JSON data object
 */
function _processOrderSearchData(self, data) {
  if (!data) {
    data = {
      isError: true
    };
  }
  data = $.extend(true, data, self.cache.config);
  if (!data.isError) {
    const { filterStartDate, filterEndDate } = data.summary;
    data.dateRange = `${filterStartDate} - ${filterEndDate}`;
  }
  this.data = data;
  return data;
}

/**
 * Returns contact HTML template
 * @param {object} contacts Contacts object
 */
function _processContacts(contacts) {
  return this.cache.contactListTemplate({
    contacts,
    baseClass: 'tp-order-search',
    jsClass: 'js-order-search'
  });
}

/**
 * Processes order table data
 * @param {object} order Order object
 * @param {string[]} keys List of keys
 * @param {string} orderDetailLink order detail link
 */
function _tableSort(order, keys, orderDetailLink) {
  const dataObject = {
    rowLink: `${orderDetailLink}?q=${order.orderNumber}&orderType=${order.orderType}&p=${encodeURIComponent(window.location.href)}`,
    row: []
  };
  keys.forEach((key, index) => {
    const value = (key === 'contact') ? _processContacts.call(this, order[key]) : order[key];
    dataObject.row[index] = {
      key,
      value,
      isRTE: ['contact'].includes(key)
    };
  });
  return dataObject;
}

function _processTableData(data) {
  const { orderDetailLink, enabledFields } = this.cache.config;
  if (Array.isArray(data.orders)) {
    data.orders = data.orders.map(order => _tableSort.call(this, order, enabledFields, orderDetailLink));
    data.orderHeadings = enabledFields.map(key => ({
      key,
      i18nKey: `cuhu.ordering.${key}`
    }));
  }
}

/**
 * Backfills search fields as per query object
 * @param {object} query Current query object
 */
function _setSearchFields(query) {
  const { $dateRange, $rangeSelector, $deliveryAddress, $search, $orderStatus } = this.cache;
  const currentRange = `${query['orderdate-from']} - ${query['orderdate-to']}`;
  if ($dateRange && $dateRange.length) {
    $dateRange.val(currentRange);
  }
  if ($rangeSelector && $rangeSelector.length) {
    $rangeSelector.val(currentRange);
  }
  if ($deliveryAddress && $deliveryAddress.length) {
    $deliveryAddress.val(query.deliveryaddress);
  }
  if ($search && $search.length) {
    $search.val(query.search);
  }
  if ($orderStatus && $orderStatus.length) {
    $orderStatus.val(query.orderstatus);
  }
}

/**
 * Renders table section
 * @param {object} filterParams Selected filter parameters
 */
function _renderTable(filterParams) {
  const { $filters, config } = this.cache;
  const $this = this;
  this.setSearchFields(filterParams);
  this.root.find('.js-pagination').trigger('ordersearch.pagedisabled');
  // auth.getToken(({ data: authData }) => {
  render.fn({
    template: 'orderingTable',
    target: '.js-order-search__table',
    url: {
      path: `/apps/settings/wcm/designs/customerhub/jsonData/orderSearchSummary.json`,
      data: $.extend(filterParams, {
        top: ORDER_HISTORY_ROWS_PER_PAGE
      })
    },
    beforeRender(data) {
      if (!data) {
        this.data = data = {
          isError: true
        };
      }
      $.extend(data, {
        labels: {
          dataError: config.dataErrorI18n,
          noData: config.noDataI18n
        }
      });
      return _processTableData.apply($this, [data]);
    },
    ajaxConfig: {
      beforeSend(jqXHR) {
        //jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
        jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
      },
      method: ajaxMethods.GET,
      cache: true,
      showLoader: true,
      cancellable: true
    }
  }, (data) => {
    if (
      $filters
      && $filters.length
      && !data.isError
    ) {
      $filters.removeClass('d-none');
    }
    if (filterParams && !data.isError && data.totalOrdersForQuery) {
      const { skip } = filterParams;
      let currentPage = 1;
      let totalPages = Math.ceil((+data.totalOrdersForQuery) / ORDER_HISTORY_ROWS_PER_PAGE);
      if (skip) {
        currentPage = (skip / ORDER_HISTORY_ROWS_PER_PAGE) + 1;
      }
      this.root.find('.js-pagination').trigger('ordersearch.paginate', [{
        currentPage,
        totalPages
      }]);
    }
  });
  //});
}

/**
 * Render table based on selected filters
 * @param {boolean} isModifiedSearch Flag to check if it's first or modified render
 */
function _setFilters(isModifiedSearch) {
  const filters = this.cache.$filters.serialize();
  const filterProp = deparam(filters);
  if (filterProp.daterange) {
    const [orderdateFrom, orderdateTo] = filterProp.daterange.split(' - ');
    filterProp['orderdate-from'] = orderdateFrom.trim();
    filterProp['orderdate-to'] = orderdateTo.trim();
    delete filterProp.daterange;
  }
  if (!isModifiedSearch) {
    this.cache.defaultParams = filterProp;
  }
  Object.keys(filterProp).forEach(key => {
    if (!filterProp[key]) {
      delete filterProp[key];
    }
  });
  if (!window.location.hash || isModifiedSearch) {
    router.set({
      route: '#/',
      queryString: $.param(filterProp)
    }, !isModifiedSearch);
  } else {
    router.init();
  }
}

/**
 * Fire analytics on search submit
 */
function _trackAnalytics(type) {
  const $this = this;
  const pageNo = $this.root.find('.js-page-number.active').data('pageNumber') || 1;
  const { resetButtonTextI18n = '', searchButtonTextI18n = '' } = $this.cache.config;

  let ob = {
    linkType: 'internal',
    linkSection: 'order history'
  };

  const obKey = 'linkClick';
  const trackingKey = 'linkClicked';
  switch (type) {
    case 'reset': {
      ob.linkParentTitle = '';
      ob.linkName = getI18n(resetButtonTextI18n).toLowerCase();
      break;
    }
    case 'orderList': {
      ob.linkParentTitle = 'order history';
      ob.linkName = 'ordersSearchResultClick';
      ob.linkSelection = `tetrapak order number|${pageNo}`;
      break;
    }
    case 'search': {
      const formData = deparam(this.cache.$filters.serialize());
      if (!formData.daterange) {
        formData.daterange = `${formData['orderdate-from']} - ${formData['orderdate-to']}`;
      }
      const deliveryAddressChoosen = formData.deliveryaddress ? 'deliveryaddresschoosen' : '';
      let orderStatusText = '';
      if (formData.orderstatus) {
        orderStatusText = this.cache.$orderStatus.find('option').filter(`[value="${formData.orderstatus}"]`).text();
      } else {
        orderStatusText = '';
      }

      ob.linkParentTitle = '';
      ob.linkName = getI18n(searchButtonTextI18n).toLowerCase();
      ob.linkSelection = `DatesChoosen|${sanitize(orderStatusText)}|${deliveryAddressChoosen}|${sanitize(formData.search)}`;
      break;
    }
    default: {
      break;
    }
  }
  trackAnalytics(ob, obKey, trackingKey, undefined, false);
}

/**
 * Renders filter section
 */
function _renderFilters() {
  const self = this;
  //auth.getToken(({ data }) => {
  render.fn({
    template: 'orderSearch',
    url: `/apps/settings/wcm/designs/customerhub/jsonData/orderSearchSummary.json`,
    target: '.js-order-search__form',
    ajaxConfig: {
      beforeSend(jqXHR) {
        //jqXHR.setRequestHeader('Authorization', `Bearer ${data.access_token}`);
        jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
      },
      method: ajaxMethods.GET,
      cache: true,
      showLoader: true,
      cancellable: true
    },
    beforeRender(...args) {
      return _processOrderSearchData.apply(this, [self, ...args]);
    }
  }, () => {
    this.initPostCache();
    this.setFilters();
    this.initializeCalendar();
  });
  //});
}

/**
 * Sanitize parameters in query object
 * @param {object} query Query object
 */
function _sanitizeQuery(query) {
  Object.keys(query).forEach(key => {
    query[key] = sanitize(query[key]);
  });
  return query;
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

class OrderSearch {
  cache = {};
  constructor({ el }) {
    this.root = $(el);
  }
  initCache() {
    /* Initialize cache here */
    this.cache.configJson = this.root.find('.js-order-search__config').text();
    this.cache.contactListTemplate = render.get('contactList');
    try {
      this.cache.config = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.config = {};
      logger.error(e);
    }
  }
  bindEvents() {
    route((...args) => {
      const [info, , query] = args;
      if (info.hash) {
        this.renderTable(_sanitizeQuery(query));
      }
    });
    this.root
      .on('click', '.js-order-search__submit', () => {
        this.setFilters(true);
        this.trackAnalytics('search');
      })
      .on('click', '.js-order-search__reset', () => {
        this.resetSearch();
        this.trackAnalytics('reset');
      })
      .on('click', '.js-order-search__date-range', () => {
        this.openRangeSelector();
      })
      .on('click', '.js-calendar', () => {
        this.submitDateRange();
      })
      .on('click', '.js-calendar-nav', this, this.navigateCalendar)
      .on('click', '.js-ordering-card__row', this, function (e) {
        const $this = e.data;
        $this.openOrderDetails.apply(this, arguments);
        $this.trackAnalytics('orderList');
      })
      .on('click', '.js-ordering-card__row a', this.stopEvtProp)
      .find('.js-pagination').on('ordersearch.pagenav', (...args) => {
        const [, data] = args;
        const routeQuery = deparam(window.location.hash.substring(2));
        delete routeQuery.skip;
        if (data.pageIndex > 0) {
          routeQuery.skip = data.pageIndex * ORDER_HISTORY_ROWS_PER_PAGE;
        }
        router.set({
          route: '#/',
          queryString: $.param(routeQuery)
        });
      });
  }
  openOrderDetails() {
    return _openOrderDetails.apply(this, arguments);
  }
  stopEvtProp() {
    return _stopEvtProp.apply(this, arguments);
  }
  renderFilters() {
    return _renderFilters.apply(this, arguments);
  }
  initializeCalendar(reset) {
    const $this = this;
    const { $rangeSelector } = this.cache;
    const rangeSelectorEl = $rangeSelector && $rangeSelector.length ? $rangeSelector[0] : null;
    if (rangeSelectorEl) {
      // Initialize inline calendar
      const { picker } = this.cache;
      if (picker && reset) {
        picker.destroy();
      }
      this.cache.picker = new Lightpick({
        field: rangeSelectorEl,
        singleDate: false,
        numberOfMonths: 2,
        inline: true,
        maxDate: Date.now(),
        dropdowns: false,
        format: DATE_FORMAT,
        separator: ' - ',
        onSelectStart() {
          $this.root.find('.js-calendar').attr('disabled', 'disabled');
        },
        onSelectEnd() {
          $this.root.find('.js-calendar').removeAttr('disabled');
        }
      });
      _disableCalendarNext(this);
    }
  }
  openRangeSelector() {
    this.cache.$modal.modal('show');
  }
  submitDateRange() {
    const { $dateRange, $rangeSelector, $modal } = this.cache;
    $dateRange.val($rangeSelector.val());
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
  setFilters() {
    return _setFilters.apply(this, arguments);
  }
  setSearchFields() {
    return _setSearchFields.apply(this, arguments);
  }
  resetSearch() {
    const { defaultParams } = this.cache;
    if (defaultParams.daterange) {
      delete defaultParams.daterange;
    }
    this.setSearchFields($.extend({}, defaultParams));
    this.initializeCalendar(true);
    router.set({
      route: '#/',
      queryString: $.param(defaultParams)
    });
  }
  renderTable() {
    return _renderTable.apply(this, arguments);
  }
  initPostCache() {
    this.cache.$filters = this.root.find('.js-order-search__filters');
    this.cache.$dateRange = this.root.find('.js-order-search__date-range');
    this.cache.$orderStatus = this.root.find('.js-order-search__order-status');
    this.cache.$deliveryAddress = this.root.find('.js-order-search__delivery-address');
    this.cache.$search = this.root.find('.js-order-search__search-term');
    this.cache.$rangeSelector = this.root.find('.js-range-selector');
    this.cache.$modal = this.root.find('.js-cal-cont__modal');
  }

  trackAnalytics() {
    return _trackAnalytics.apply(this, arguments);
  }

  init() {
    this.initCache();
    this.bindEvents();
    this.renderFilters();
  }
}

export default OrderSearch;
