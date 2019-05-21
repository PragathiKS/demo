import $ from 'jquery';
import 'bootstrap';
import auth from '../../../scripts/utils/auth';
import { render } from '../../../scripts/utils/render';
import { ajaxMethods, API_MAINTENANCE_EVENTS } from '../../../scripts/utils/constants';
import { apiHost } from '../../../scripts/common/common';
import { logger } from '../../../scripts/utils/logger';
import { trackAnalytics } from '../../../scripts/utils/analytics';


/**
 * Fire analytics on click of
 * maintenance card event
 */
function _trackAnalytics() {
  const { maintenanceHeading } = this.cache.i18nKeys;

  const analyticsData = {
    linkType: 'internal',
    linkSection: 'dashboard',
    linkName: 'maintenanceListItem',
    linkParentTitle: maintenanceHeading
      ? maintenanceHeading.toLowerCase()
      : 'maintenance service events'
  };

  trackAnalytics(analyticsData, 'linkClick', 'linkClicked', undefined, false);
}

/**
 * Renders Maintenance Events
 */
function _renderMaintenanceEvents() {
  const $this = this;
  auth.getToken(({ data: authData }) => {
    render.fn({
      template: 'maintenanceCardEvents',
      url: {
        path: `${apiHost}/${API_MAINTENANCE_EVENTS}`
      },
      target: '.js-maintenance-card__events',
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
          if (!Array.isArray(data.events) || data.events.length === 0) {
            data.noData = true;
          }

          const { i18nKeys, viewAllLink } = $this.cache;
          data.i18nKeys = i18nKeys;
          data.viewAllLink = viewAllLink;
        }
      }
    }, (data) => {
      if (!data.isError && !data.noData) {
        $this.root.find('.js-maintenance-card__events-detail').html($('#Event0').html());
      }
    });
  });
}

class MaintenanceCard {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.configJson = this.root.find('.js-maintenance-card__config').text();
    try {
      this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
    this.cache.viewAllLink = this.root.find('.js-view-all-link').val();
  }
  bindEvents() {
    const $this = this;
    this.root.on('click', '.js-maintenance-card__event', function () {
      let detailTargetEle = $(this).data('target');
      $this.root.find('.js-maintenance-card__events-detail').html($(detailTargetEle).html());
      $this.trackAnalytics();
    });
  }
  renderMaintenanceEvents = () => _renderMaintenanceEvents.call(this);
  trackAnalytics = () => _trackAnalytics.call(this);
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.renderMaintenanceEvents();
  }
}

export default MaintenanceCard;
