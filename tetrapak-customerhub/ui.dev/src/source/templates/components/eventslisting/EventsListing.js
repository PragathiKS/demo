import $ from 'jquery';
import auth from '../../../scripts/utils/auth';
import { render } from '../../../scripts/utils/render';
import { ajaxMethods, API_MAINTENANCE_EVENTS, DATE_FORMAT, NO_OF_EVENTS_PER_PAGE } from '../../../scripts/utils/constants';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { apiHost } from '../../../scripts/common/common';
import moment from 'moment';

function _renderMaintenanceEvents() {
  const $this = this;
  const data = {
    top: NO_OF_EVENTS_PER_PAGE
  };
  const monthsSelector = $(this).find('.lightpick__day:not(.is-previous-month):not(.is-next-month)');
  const todaySelector = $(this).find('.is-today');
  let fromDate;
  if (todaySelector.length === 0) {
    fromDate = moment(new Date($(monthsSelector).first().data('time'))).format(DATE_FORMAT);
  } else {
    fromDate = moment(new Date($(todaySelector).data('time'))).format(DATE_FORMAT);
  }
  let toDate = moment(new Date($(monthsSelector).last().data('time'))).format(DATE_FORMAT);
  const sitenumber = this.cache.$site.val();
  const linenumber = this.cache.$line.val();
  const equipmentnumber = this.cache.$equipment.val();
  const dateRangeSelector = $(this).find('.js-events-date-range-selector');
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
          this.cache.isEventNoData = true;
        }
        this.cache.data = data;
      }
      render.fn({
        template: 'eventsListing',
        target: '.js-maintenance__events',
        data: $this.cache
      }, () => {
        $this.trackEventAnalytics($this.cache.filterSelected);
      });
    });
  });
}
class EventsListing {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};

  bindEvents() {
    this.root.parents('.js-maintenance').on('renderMaintenance', this, this.renderMaintenanceEvents);
  }
  renderMaintenanceEvents(...args) {
    this.cache = args[1];
    this.trackEventAnalytics = args[2];
    _renderMaintenanceEvents.call(this);
  }

  init() {
    this.bindEvents();
  }
}

export default EventsListing;
