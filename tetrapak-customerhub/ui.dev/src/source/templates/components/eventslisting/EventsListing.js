import $ from 'jquery';
import auth from '../../../scripts/utils/auth';
import { render } from '../../../scripts/utils/render';
import { ajaxMethods, DATE_FORMAT, NO_OF_EVENTS_PER_PAGE, API_MAINTENANCE_EVENTS } from '../../../scripts/utils/constants';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { apiHost } from '../../../scripts/common/common';
import moment from 'moment';

function _renderMaintenanceEvents(...eventsData) {
  let siteFilter = '';
  let lineFilter = '';
  let equipmentFilter = '';
  let selectedFilter = '';
  const [cache, trackAnalytics, onPageLoad, skip] = eventsData;

  const data = {
    top: NO_OF_EVENTS_PER_PAGE
  };
  const $this = this.root.parents('.js-maintenance');
  const $monthsSelector = $this.find('.lightpick__day:not(.is-previous-month):not(.is-next-month)');
  const $todaySelector = $this.find('.is-today');
  let fromDate;
  if ($todaySelector.length === 0) {
    fromDate = moment(new Date($monthsSelector.first().data('time'))).format(DATE_FORMAT);
  } else {
    fromDate = moment(new Date($todaySelector.data('time'))).format(DATE_FORMAT);
  }
  let toDate = moment(new Date($monthsSelector.last().data('time'))).format(DATE_FORMAT);
  const sitenumber = cache.$site.val();
  const linenumber = cache.$line.val();
  const equipmentnumber = cache.$equipment.val();
  const $dateRangeSelector = $this.find('.js-events-date-range-selector');
  const dateRangeArray = $dateRangeSelector.val();
  if (dateRangeArray) {
    const dateRange = dateRangeArray.split(' - ');
    fromDate = moment(dateRange[1]) < moment(dateRange[0]) ? dateRange[1] : dateRange[0];
    toDate = moment(dateRange[1]) > moment(dateRange[0]) ? dateRange[1] : dateRange[0];
  }

  if (sitenumber) {
    data.sitenumber = sitenumber;
    siteFilter = 'site';
  }

  if (linenumber) {
    data.linenumber = linenumber;
    lineFilter = 'line/area';
  }
  if (skip) {
    data.skip = skip;
  }

  if (equipmentnumber) {
    data.equipmentnumber = equipmentnumber;
    equipmentFilter = 'equipment/unit';
  }

  selectedFilter = [siteFilter, lineFilter, equipmentFilter, 'dateschoosen'].join('|');

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
      cache: true,
      data: data
    }).done((data) => {
      if (!data.isError && data.totalRecordsForQuery > NO_OF_EVENTS_PER_PAGE) {
        let currentPage = 1;
        let totalPages = Math.ceil((+data.totalRecordsForQuery) / NO_OF_EVENTS_PER_PAGE);
        if (skip) {
          currentPage = (skip / NO_OF_EVENTS_PER_PAGE) + 1;
        }
        $this.find('.js-pagination').trigger('eventslisting.paginate', [{
          currentPage,
          totalPages
        }]);
      }
      if (data.events.length === 0) {
        cache.isEventNoData = true;
      }
      cache.eventsData = data;
      render.fn({
        template: 'eventsListing',
        target: '.js-maintenance__events',
        data: cache
      }, () => {
        if (!onPageLoad) {
          cache.selectedFilter = selectedFilter;
          const name = cache.navigationSelected ?
            cache.navigationSelected : 'maintenance tab selection';
          trackAnalytics(name);
        }
      });
    }).fail(() => {
      cache.isEventDataError = true;
      render.fn({
        template: 'eventsListing',
        target: '.js-maintenance__events',
        data: cache
      });
    });
  });
}
class EventsListing {
  constructor({ el }) {
    this.root = $(el);
  }

  bindEvents() {
    const $this = this;
    this.root.parents('.js-maintenance').on('renderMaintenance', this, this.renderMaintenanceEvents);
    this.root.parents('.js-maintenance').find('.js-pagination').on('eventslisting.pagenav', (...args) => {
      const [, data] = args;
      const skip = data.pageIndex * NO_OF_EVENTS_PER_PAGE;
      $this.reRenderMaintenanceEvents(skip);
      $this.cache.currentPageIndex = data.pageIndex + 1;
      $this.cache.navigationSelected = 'preventive maintenance';
    });
  }
  renderMaintenanceEvents(...args) {
    let $this = args[0].data;
    [, $this.cache, $this.trackAnalytics] = args;
    return _renderMaintenanceEvents.apply($this, args.slice(1));
  }
  reRenderMaintenanceEvents(skip) {
    return _renderMaintenanceEvents.apply(this, [this.cache, this.trackAnalytics, '', skip]);
  }
  init() {
    this.bindEvents();
  }
}

export default EventsListing;
