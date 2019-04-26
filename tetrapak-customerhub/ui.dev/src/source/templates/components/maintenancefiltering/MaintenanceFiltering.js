import $ from 'jquery';
import auth from '../../../scripts/utils/auth';
import { render } from '../../../scripts/utils/render';
import { ajaxMethods, API_MAINTENANCE_FILTERS } from '../../../scripts/utils/constants';
import { apiHost } from '../../../scripts/common/common';
import { logger } from '../../../scripts/utils/logger';


/**
 * Process Parts Data
 */
function _processFiltersData(data) {
  if (Array.isArray(data.installations)) {
    data.sites = data.installations.map((site) => ({
      'key': site.customerNumber,
      'desc': site.customerName
    }));
  } else {
    data.noData = true;
  }
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
        path: `${apiHost}/${API_MAINTENANCE_FILTERS}`,
        data: {
        }
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

          $this.processFiltersData(data);
          $this.cache.data = data;
        }
      }
    }, (data) => {
      if (!data.isError) {
        $this.initPostCache();
        $this.renderMaintenanceContact();
      }
    });
  });
}

/**
 * Renders contact Addresses
 */
function _renderMaintenanceContact() {
  const siteVal = this.cache.$site.val();
  let data = this.cache.data;
  if (Array.isArray(data.installations)) {
    data = data.installations.filter((site) => site.customerNumber === siteVal);
    if (data.length > 0) {
      data = data[0];
      if (Array.isArray(data.lines)) {
        data.linesRecords = {};
        //data.linesRecords.options = [{ 'key': '', 'desc': this.cache.data.i18nKeys.allOptionText }];
        data.linesRecords.options = data.lines.map((line) => ({
          'key': line.lineNumber,
          'desc': line.lineDesc
        }));
        data.linesRecords.options.unshift({ 'key': '', 'desc': this.cache.data.i18nKeys.allOptionText });

        render.fn({
          template: 'options',
          data: data.linesRecords,
          target: '.js-maintenance-filtering__line'
        });
      }
    } else {
      data = {};
    }
  }

  render.fn({
    template: 'maintenanceContact',
    data,
    target: '.js-maintenance-filtering__contact',
    beforeRender(data) {
      logger.log(data);
    }
  });
}

class MaintenanceFiltering {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    /* Initialize selector cache here */
    this.cache.configJson = this.root.find('.js-maintenance-filtering__config').text();
    try {
      this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
  }
  initPostCache() {
    this.cache.$site = this.root.find('.js-maintenance-filtering__site');
  }
  bindEvents() {
    /* Bind jQuery events here */
    this.root
      .on('change', '.js-maintenance-filtering__site', () => {
        this.renderMaintenanceContact();
      });
  }
  renderMaintenanceFilters = () => _renderMaintenanceFilters.call(this);
  processFiltersData = (...arg) => _processFiltersData.apply(this, arg);
  renderMaintenanceContact = () => _renderMaintenanceContact.call(this);
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.renderMaintenanceFilters();
  }
}

export default MaintenanceFiltering;
