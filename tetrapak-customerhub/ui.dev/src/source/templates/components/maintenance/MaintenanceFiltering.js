import $ from 'jquery';
import auth from '../../../scripts/utils/auth';
import { render } from '../../../scripts/utils/render';
import { ajaxMethods, API_MAINTENANCE_FILTERS, API_MAINTENANCE_EVENTS, DATE_FORMAT } from '../../../scripts/utils/constants';
import { apiHost, isDesktopMode } from '../../../scripts/common/common';
import { logger } from '../../../scripts/utils/logger';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import Lightpick from 'lightpick';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { getDatesBetweenDateRange } from '../../../scripts/utils/dateUtils';
import moment from 'moment';


/**
 * Fire analytics on Packaging, Processing
 * mail/contact link click
 */
function _trackAnalytics(type, name) {
  const analyticsData = {
    linkType: 'internal',
    linkSection: 'installed equipment-maintenance'
  };

  // creating linkParentTitle/linkName as per the name or type received
  analyticsData.linkParentTitle = `contact-${type}`;
  analyticsData.linkName = `${name}`;

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
        path: `${apiHost}/${API_MAINTENANCE_FILTERS}`
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
        $this.initPostCache();
        $this.renderMaintenanceContact();
        this.renderCalendar();
        $this.renderCalendarEventsDot();
        $this.renderMaintenanceEvents();

      }
    });
  });
}
/**
 * Render Dots on Calendar
 */
function _renderCalendarEventsDot() {
  const siteVal = this.cache.$site.val();
  const dateRange = this.root.find('.lightpick__day:not(.is-previous-month):not(.is-next-month)');
  let startDate = moment(new Date($(dateRange).first().data('time'))).format(DATE_FORMAT);
  let endDate = moment(new Date($(dateRange).last().data('time'))).format(DATE_FORMAT);
  let eventsDateArrayFinal = [];
  auth.getToken(({ data: authData }) => {
    ajaxWrapper.getXhrObj({
      url: `${apiHost}/${API_MAINTENANCE_EVENTS}`,
      method: ajaxMethods.GET,
      beforeSend(jqXHR) {
        jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
        jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
      },
      data: {
        'sitenumber': siteVal,
        'from-date': startDate,
        'to-date': endDate
      }
    }).done((data) => {
      let eventsDateArray = [];
      data.events.forEach(function (item) {
        let start = new Date(item.plannedStart);
        let end = new Date(item.plannedFinish);
        let datearray = getDatesBetweenDateRange(start, end);
        eventsDateArray = [...eventsDateArray, ...datearray];
      });
      eventsDateArray.forEach(function (date) {
        let formattedDate = moment(date).format(DATE_FORMAT);
        if (!eventsDateArrayFinal.includes(formattedDate)) {
          eventsDateArrayFinal.push(formattedDate);
        }
      });
      const detachedMonths = this.root.find('.lightpick__months').detach();
      const allDays = $(detachedMonths).find('.lightpick__day:not(.is-previous-month):not(.is-next-month)');
      allDays.each(function () {
        const date = moment(new Date($(this).data('time'))).format(DATE_FORMAT);
        if (eventsDateArrayFinal.includes(date)) {
          $(this).append(`<span class='lightpick__dot'></span>`);
        }
      });
      this.root.find('.lightpick__inner').append(detachedMonths);
    });
  });
}

function _renderMaintenanceEvents() {
  const $this = this;
  const data = {
    top: 12
  };
  const monthsSelector = this.root.find('.lightpick__day:not(.is-previous-month):not(.is-next-month)');
  const todaySelector = this.root.find('.is-today');
  let fromDate = moment(new Date($(todaySelector).data('time'))).format(DATE_FORMAT);
  let toDate = moment(new Date($(monthsSelector).last().data('time'))).format(DATE_FORMAT);
  const sitenumber = this.cache.$site.val() ? this.cache.$site.val() : null;
  const linenumber = this.cache.$line.val() ? this.cache.$line.val() : null;
  const equipmentnumber = this.cache.$equipment.val() ? this.cache.$equipment.val() : null;
  const dateRangeSelector = this.root.find('.js-events-date-range-selector');
  const dateRangeArray = dateRangeSelector.val() ? dateRangeSelector.val() : null;
  if (dateRangeArray) {
    const dateRange = dateRangeArray.split(' - ');
    fromDate = moment(dateRange[1]) < moment(dateRange[0]) ? dateRange[1] : dateRange[0];
    toDate = moment(dateRange[1]) > moment(dateRange[0]) ? dateRange[1] : dateRange[0];
  }
  if (sitenumber) {
    data.sitenumber = sitenumber;
  }
  if (linenumber) {
    data.linenumber = linenumber;
  }
  if (equipmentnumber) {
    data.equipmentnumber = equipmentnumber;
  }
  if (fromDate) {
    data['from-date'] = fromDate;
  }
  if (toDate) {
    data['to-date'] = toDate;
  }

  auth.getToken(({ data: authData }) => {
    ajaxWrapper.getXhrObj({
      url: `${apiHost}/${API_MAINTENANCE_EVENTS}`,
      method: ajaxMethods.GET,
      beforeSend(jqXHR) {
        jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
        jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
      },
      data: data
    }).done((data) => {
      if (!data) {
        this.cache.isEventDataError = true;
      }
      else {
        if (data.events.length === 0) {
          $this.cache.isEventNoData = true;
        }
        $this.cache.data = data;
      }
      render.fn({
        template: 'maintenanceevents',
        target: '.js-maintenance__events',
        data: $this.cache
      });
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
  bindEvents() {
    const self = this;
    this.root
      .on('change', '.js-maintenance-filtering__site', () => {
        this.renderMaintenanceContact();
        this.renderMaintenanceEvents();
      })
      .on('change', '.js-maintenance-filtering__line', () => {
        this.renderEquipmentFilter();
        this.renderMaintenanceEvents();
      })
      .on('change', '.js-maintenance-filtering__equipment', () => {
        this.renderMaintenanceEvents();
      })
      .on('click', '.js-maintenance-filtering__contact-mail', function () {
        self.trackAnalytics($(this).data('type').toLowerCase(), 'email');
      })
      .on('click', '.js-maintenance-filtering__contact-phone', function () {
        self.trackAnalytics($(this).data('type').toLowerCase(), 'phone');
      });
    this.root.on('click', '.js-maintenance-filtering__calendar-wrapper .js-calendar-nav', this, this.navigateCalendar);
  }
  renderCalendar() {
    const $this = this;
    render.fn({
      template: 'maintenanceCalendar',
      target: '.js-maintenance-filtering__calendar-wrapper',
      data: this.cache.i18nKeys
    }, () => {
      const maintenancecalendar = this.root.find('.js-events-date-range-selector');
      const calendarField = maintenancecalendar[0];
      const { picker } = this.cache;
      if (picker) {
        picker.destroy();
      }
      this.cache.picker = new Lightpick({
        field: calendarField,
        singleDate: false,
        numberOfMonths: 4,
        numberOfColumns: 2,
        inline: true,
        dropdowns: false,
        format: DATE_FORMAT,
        separator: ' - ',
        onSelectEnd() {
          $this.renderMaintenanceEvents();
        }
      });
      this.wrapCalendar();
    });

  }
  wrapCalendar() {
    const calendarMonthsCont = this.root.find('.lightpick__months');
    if (
      isDesktopMode()
      && calendarMonthsCont.length
    ) {
      const months = calendarMonthsCont.find('section.lightpick__month');
      if (months.length === 4) {
        const leftMonthsContainer = $(months[0]).add(months[1]);
        const rightMonthsContainer = $(months[2]).add(months[3]);
        leftMonthsContainer.wrapAll('<div></div>');
        rightMonthsContainer.wrapAll('<div></div>');
      }
    }
  }
  navigateCalendar(e) {
    const $this = e.data;
    const action = $(this).data('action');
    const $defaultCalendarNavBtn = $this.root.find(`.lightpick__${action}`);
    if ($defaultCalendarNavBtn.length) {
      let evt = document.createEvent('MouseEvents');
      evt.initEvent('mousedown', true, true);
      $defaultCalendarNavBtn[0].dispatchEvent(evt); // JavaScript mousedown event
    }
    $this.wrapCalendar();
    $this.renderCalendarEventsDot();
  }
  renderMaintenanceEvents = () => _renderMaintenanceEvents.call(this);
  renderMaintenanceFilters = () => _renderMaintenanceFilters.call(this);
  renderCalendarEventsDot = () => _renderCalendarEventsDot.call(this);
  processSiteData = (...arg) => _processSiteData.apply(this, arg);
  renderMaintenanceContact = () => _renderMaintenanceContact.call(this);
  renderLineFilter = (data) => _renderLineFilter.call(this, data);
  renderEquipmentFilter = (data) => _renderEquipmentFilter.call(this, data);
  trackAnalytics = (type, name) => _trackAnalytics.call(this, type, name);
  init() {
    this.initCache();
    this.bindEvents();
    this.renderMaintenanceFilters();
  }
}

export default MaintenanceFiltering;
