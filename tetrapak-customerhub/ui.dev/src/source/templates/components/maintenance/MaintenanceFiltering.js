import $ from 'jquery';
import auth from '../../../scripts/utils/auth';
import { render } from '../../../scripts/utils/render';
import { ajaxMethods, API_MAINTENANCE_FILTERS, API_MAINTENANCE_EVENTS, DATE_FORMAT, DATE_RANGE_SEPARATOR } from '../../../scripts/utils/constants';
import { isDesktopMode, isMobileMode } from '../../../scripts/common/common';
import { logger } from '../../../scripts/utils/logger';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import Lightpick from 'lightpick';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { getDatesBetweenDateRange } from '../../../scripts/utils/dateUtils';
import moment from 'moment';
import { getURL } from '../../../scripts/utils/uri';

/**
 * Fire analytics on click of
 * filters, contact and calender
 */
function _trackAnalytics(name, type) {
  const analyticsData = {
    linkType: 'internal',
    linkSection: 'installed equipment-maintenance',
    linkName: name
  };
  const { eventsData, currentPageIndex } = this.cache;

  switch (name) {
    case 'email':
    case 'phone': {
      analyticsData.linkParentTitle = `contact-${type}`;
      break;
    }
    case 'site': {
      analyticsData.linkName = 'maintenance tab selection';
      analyticsData.linkSelection = 'site';
      analyticsData.linkParentTitle = 'maintenance tab';
      analyticsData.maintenanceResultsCount = eventsData.totalRecordsForQuery;
      break;
    }
    case 'line': {
      analyticsData.linkName = 'maintenance tab selection';
      analyticsData.linkSelection = 'line/area';
      analyticsData.linkParentTitle = 'maintenance tab';
      analyticsData.maintenanceResultsCount = eventsData.totalRecordsForQuery;
      break;
    }
    case 'equipment': {
      analyticsData.linkName = 'maintenance tab selection';
      analyticsData.linkSelection = 'equipment/unit';
      analyticsData.linkParentTitle = 'maintenance tab';
      analyticsData.maintenanceResultsCount = eventsData.totalRecordsForQuery;
      break;
    }
    case 'date': {
      analyticsData.linkName = 'maintenance tab selection';
      analyticsData.linkSelection = 'dateschoosen';
      analyticsData.linkParentTitle = 'maintenance schedule';
      analyticsData.maintenanceResultsCount = eventsData.totalRecordsForQuery;
      break;
    }
    case 'left arrow':
    case 'right arrow': {
      analyticsData.linkSelection = 'dateschoosen';
      analyticsData.linkParentTitle = 'maintenance schedule';
      analyticsData.maintenanceResultsCount = eventsData.totalRecordsForQuery;
      break;
    }
    case 'pagination': {
      analyticsData.linkParentTitle = 'maintenance events';
      analyticsData.linkName = 'pagination click';
      analyticsData.maintenanceEventPagination = currentPageIndex;
      break;
    }
    default: {
      break;
    }
  }

  trackAnalytics(analyticsData, 'linkClick', 'linkClicked', undefined, false);
}

/**
 * Process Sites Data
 * @param {object} data JSON data object
 */
function _processSiteData(data) {
  if (Array.isArray(data.installations)) {
    data.sites = data.installations.map(site => ({
      key: site.customerNumber,
      desc: site.customerName
    }));
  } else {
    data.noData = true;
  }
}

/**
 * Renders contact Addresses
 */
function _renderMaintenanceContact() {
  const siteVal = this.cache.$site.val();
  let { data } = this.cache;
  if (Array.isArray(data.installations)) {
    const [filteredData] = data.installations.filter(site => site.customerNumber === siteVal);
    if (filteredData) {
      data = this.cache.filteredData = filteredData;
      this.renderLineFilter(filteredData);
    } else {
      data = {};
    }
  }

  render.fn({
    template: 'maintenanceContact',
    target: '.js-maintenance-filtering__contact',
    data
  });
}

/**
 * Renders Line Filter
 * @param {object} data JSON data object for selected site
 */
function _renderLineFilter(data = this.cache.filteredData) {
  if (Array.isArray(data.lines)) {
    data.linesRecords = {};
    data.linesRecords.options = data.lines.map((line) => ({
      key: line.lineNumber,
      desc: line.lineDesc
    }));

    const { options } = data.linesRecords;

    if (options.length > 1) {
      const { i18nKeys } = this.cache;
      options.unshift({ 'key': '', 'desc': i18nKeys.allOptionText });
    }

    render.fn({
      template: 'options',
      data: data.linesRecords,
      target: '.js-maintenance-filtering__line'
    });

    this.renderEquipmentFilter(data);
  }
}

/**
 * Renders Equipment Filter
 * @param {object} data JSON data object for selected site
 */
function _renderEquipmentFilter(data = this.cache.filteredData) {
  const lineVal = this.cache.$line.val();
  let equipmentRecords;

  data.equipmentRecords = {};
  data.equipmentRecords.options = [];

  if (lineVal === '') {
    equipmentRecords = data.lines;
  } else {
    equipmentRecords = data.lines.filter(line => line.lineNumber === lineVal);
  }

  equipmentRecords.forEach(equipment => {
    data.equipmentRecords.options.push(...equipment.equipments.map(equipment => ({
      key: equipment.equipmentNumber,
      desc: equipment.equipmentName
    })));
  });

  const { options } = data.equipmentRecords;

  if (options.length > 1) {
    const { i18nKeys } = this.cache;
    options.unshift({ 'key': '', 'desc': i18nKeys.allOptionText });
  }

  render.fn({
    template: 'options',
    data: data.equipmentRecords,
    target: '.js-maintenance-filtering__equipment'
  });
}


/**
 * Renders Maintenance Filters
 */
function _renderMaintenanceFilters() {
  const $this = this;
  auth.getToken(({ data: authData }) => {
    render.fn({
      template: 'maintenanceFiltering',
      url: {
        path: getURL(API_MAINTENANCE_FILTERS)
      },
      target: '.js-maintenance-filtering__filters',
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
        } else {
          const { i18nKeys } = $this.cache;
          data.i18nKeys = i18nKeys;

          $this.processSiteData(data);
          $this.cache.data = data;
        }
      }
    }, (data) => {
      if (!data.isError && !data.noData) {
        this.root.find('.js-maintenance-filtering__filters').addClass('tp-maintenance-filtering__filters');
        this.initPostCache();
        this.renderMaintenanceContact();
        this.renderCalendar(true);
      }
    });
  });
}

/**
 * Renders dots on calendar
 * @param {object} data Calendar events data
 */
function _renderDots(data) {
  const eventsDateArrayFinal = [];
  let eventsDateArray = [];
  data.events.forEach(function (item) {
    const start = new Date(item.plannedStart);
    const end = new Date(item.plannedFinish);
    const datearray = getDatesBetweenDateRange(start, end);
    eventsDateArray = [...eventsDateArray, ...datearray];
  });
  eventsDateArray.forEach(function (date) {
    const formattedDate = moment(date).format(DATE_FORMAT);
    if (!eventsDateArrayFinal.includes(formattedDate)) {
      eventsDateArrayFinal.push(formattedDate);
    }
  });
  const detachedMonths = this.root.find('.lightpick__months').detach();
  const $allDays = $(detachedMonths).find('.lightpick__day:not(.is-previous-month):not(.is-next-month)');
  $allDays.each(function () {
    const date = moment(new Date($(this).data('time'))).format(DATE_FORMAT);
    if (eventsDateArrayFinal.includes(date)) {
      $(this).append(`<span class='lightpick__dot'></span>`);
    }
  });
  this.root.find('.lightpick__inner').append(detachedMonths);
}

/**
 * Render Dots on Calendar
 */
function _renderCalendarEventsDot() {
  const siteVal = this.cache.$site.val();
  const $dateRange = this.root.find('.lightpick__day:not(.is-previous-month):not(.is-next-month)');
  const startDate = moment(new Date($dateRange.first().data('time'))).format(DATE_FORMAT);
  const endDate = moment(new Date($dateRange.last().data('time'))).format(DATE_FORMAT);
  auth.getToken(({ data: authData }) => {
    ajaxWrapper.getXhrObj({
      url: getURL(API_MAINTENANCE_EVENTS),
      method: ajaxMethods.GET,
      beforeSend(jqXHR) {
        jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
        jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
      },
      cache: true,
      data: {
        'sitenumber': siteVal,
        'from-date': startDate,
        'to-date': endDate
      }
    }).done((data) => {
      this.cache.dotsData = this.cache.eventsData = data;
      this.renderDots(data);
    });
  });
}


class MaintenanceFiltering {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  /**
  * Initialize selector cache on component load
  */
  initCache() {
    this.cache.configJson = this.root.find('.js-maintenance-filtering__config').text();
    try {
      this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
  }
  /**
  * Initialize selector cache after filters rendering
  */
  initPostCache() {
    this.cache.$site = this.root.find('.js-maintenance-filtering__site');
    this.cache.$line = this.root.find('.js-maintenance-filtering__line');
    this.cache.$equipment = this.root.find('.js-maintenance-filtering__equipment');
  }
  triggerMaintenanceEvents() {
    this.root.parents('.js-maintenance').trigger('maintenance.render', [this.cache, this.trackAnalytics]);
  }
  bindEvents() {
    const self = this;
    this.root
      .on('change', '.js-maintenance-filtering__site', () => {
        this.renderMaintenanceContact();
        this.renderCalendar();
        this.trackAnalytics('site');
      })
      .on('change', '.js-maintenance-filtering__line', () => {
        this.renderEquipmentFilter();
        this.renderCalendar();
        this.trackAnalytics('line');
      })
      .on('change', '.js-maintenance-filtering__equipment', () => {
        this.renderCalendar();
        this.trackAnalytics('equipment');
      })
      .on('click', '.js-maintenance-filtering__contact-mail', function () {
        self.trackAnalytics('email', $(this).data('type').toLowerCase());
      })
      .on('click', '.js-maintenance-filtering__contact-phone', function () {
        self.trackAnalytics('phone', $(this).data('type').toLowerCase());
      })
      .on('click', '.js-maintenance-filtering__calendar-wrapper .js-calendar-nav', this, this.navigateCalendar);
  }
  renderCalendar(pageLoad, selectedDateRange = '', noRender) {
    const { i18nKeys } = this.cache;
    render.fn({
      template: 'maintenanceCalendar',
      target: '.js-maintenance-filtering__calendar-wrapper',
      data: {
        ...i18nKeys,
        selectedDateRange
      }
    }, () => {
      this.cache.$calendarNavCont = this.root.find('.js-cal-cont__calendar-nav');
      const $maintenancecalendar = this.root.find('.js-events-date-range-selector');
      const calendarField = $maintenancecalendar[0];
      const { picker } = this.cache;
      if (picker) {
        picker.destroy();
      }
      this.cache.picker = new Lightpick({
        field: calendarField,
        singleDate: false,
        numberOfMonths: (isMobileMode() ? 1 : 4),
        numberOfColumns: (isMobileMode() ? 1 : 2),
        inline: true,
        dropdowns: false,
        format: DATE_FORMAT,
        separator: DATE_RANGE_SEPARATOR,
        onSelectStart: () => {
          this.cache.$calendarNavCont.addClass('js-disable-data-call');
        },
        onSelectEnd: () => {
          this.cache.$calendarNavCont.removeClass('js-disable-data-call');
          this.triggerMaintenanceEvents();
          this.trackAnalytics('date');
        }
      });
      this.wrapCalendar();
      if (!noRender) {
        this.renderCalendarEventsDot();
        this.triggerMaintenanceEvents(pageLoad);
      }
      $(window).off('media.changed').on('media.changed', () => {
        this.renderCalendar(pageLoad, $maintenancecalendar.val(), true);
        this.renderDots(this.cache.dotsData);
      });
    });

  }
  wrapCalendar() {
    const $calendarMonthsCont = this.root.find('.lightpick__months');
    if (
      isDesktopMode()
      && $calendarMonthsCont.length
    ) {
      const $months = $calendarMonthsCont.find('section.lightpick__month');
      if ($months.length === 4) {
        const leftMonthsContainer = $($months[0]).add($months[1]);
        const rightMonthsContainer = $($months[2]).add($months[3]);
        leftMonthsContainer.wrapAll('<div></div>');
        rightMonthsContainer.wrapAll('<div></div>');
      }
    }
  }
  navigateCalendar(e) {
    const $this = e.data;
    const action = $(this).data('action');
    const $defaultCalendarNavBtn = $this.root.find(`.lightpick__${action}`);

    const analyticsName = (action === 'previous-action') ? 'left arrow' : 'right arrow';
    $this.trackAnalytics(analyticsName);
    if ($defaultCalendarNavBtn.length) {
      const evt = document.createEvent('MouseEvents');
      evt.initEvent('mousedown', true, true);
      $defaultCalendarNavBtn[0].dispatchEvent(evt); // JavaScript mousedown event
    }
    $this.wrapCalendar();
    $this.renderCalendarEventsDot();
    if ($this.root.find('.js-disable-data-call').length === 0) {
      $this.triggerMaintenanceEvents();
    }
  }
  renderMaintenanceFilters = () => _renderMaintenanceFilters.call(this);
  renderCalendarEventsDot = () => _renderCalendarEventsDot.call(this);
  renderDots = (...args) => _renderDots.apply(this, args);
  processSiteData = (...arg) => _processSiteData.apply(this, arg);
  renderMaintenanceContact = () => _renderMaintenanceContact.call(this);
  renderLineFilter = (data) => _renderLineFilter.call(this, data);
  renderEquipmentFilter = (data) => _renderEquipmentFilter.call(this, data);
  trackAnalytics = (name, type) => _trackAnalytics.call(this, name, type);
  init() {
    this.initCache();
    this.bindEvents();
    this.renderMaintenanceFilters();
  }
}

export default MaintenanceFiltering;