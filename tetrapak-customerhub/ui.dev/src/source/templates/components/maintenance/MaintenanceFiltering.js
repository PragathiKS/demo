import $ from 'jquery';
import auth from '../../../scripts/utils/auth';
import { render } from '../../../scripts/utils/render';
import { ajaxMethods, API_MAINTENANCE_FILTERS } from '../../../scripts/utils/constants';
import { apiHost } from '../../../scripts/common/common';
import { logger } from '../../../scripts/utils/logger';


/**
 * Process Sites Data
 */
function _processSiteData(data) {
  if (Array.isArray(data.installations)) {
    data.sites = data.installations.map(site => ({
      'key': site.customerNumber,
      'desc': site.customerName
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
  let data = this.cache.data;
  if (Array.isArray(data.installations)) {
    data = data.installations.filter(site => site.customerNumber === siteVal);
    if (data.length > 0) {
      data = this.cache.filteredData = data[0];
      this.renderLineFilter(data);
    } else {
      data = {};
    }
  }

  render.fn({
    template: 'maintenanceContact',
    data,
    target: '.js-maintenance-filtering__contact'
  });
}

/**
 * Renders Line Filter
 */
function _renderLineFilter(data = this.cache.filteredData) {
  if (Array.isArray(data.lines)) {
    data.linesRecords = {};
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

    this.renderEquipmentFilter(data);
  }
}

/**
 * Renders Equipment Filter
 */
function _renderEquipmentFilter(data = this.cache.filteredData) {
  let lineVal = this.cache.$line.val(),
    equipmentRecords;
  data.equipmentRecords = {};
  data.equipmentRecords.options = [];
  if (lineVal === '') {
    equipmentRecords = data.lines;
  } else {
    equipmentRecords = data.lines.filter(line => line.lineNumber === lineVal);
  }

  equipmentRecords.forEach(equipment => {
    data.equipmentRecords.options = data.equipmentRecords.options.concat(equipment.equipments.map(equipment => ({
      'key': equipment.equipmentNumber,
      'desc': equipment.equipmentName
    })));
  });

  data.equipmentRecords.options.unshift({ 'key': '', 'desc': this.cache.data.i18nKeys.allOptionText });

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

          $this.processSiteData(data);
          $this.cache.data = data;
        }
      }
    }, (data) => {
      if (!data.isError && !data.noData) {
        $this.initPostCache();
        $this.renderMaintenanceContact();
      }
    });
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
    this.cache.$line = this.root.find('.js-maintenance-filtering__line');
    this.cache.$equipment = this.root.find('.js-maintenance-filtering__equipment');
  }
  bindEvents() {
    /* Bind jQuery events here */
    this.root
      .on('change', '.js-maintenance-filtering__site', () => {
        this.renderMaintenanceContact();
      })
      .on('change', '.js-maintenance-filtering__line', () => {
        this.renderEquipmentFilter();
      });
  }
  renderMaintenanceFilters = () => _renderMaintenanceFilters.call(this);
  processSiteData = (...arg) => _processSiteData.apply(this, arg);
  renderMaintenanceContact = () => _renderMaintenanceContact.call(this);
  renderLineFilter = (data) => _renderLineFilter.call(this, data);
  renderEquipmentFilter = (data) => _renderEquipmentFilter.call(this, data);
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.renderMaintenanceFilters();
  }
}

export default MaintenanceFiltering;
