import $ from 'jquery';
import auth from '../../../scripts/utils/auth';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import { ajaxMethods, API_MAINTENANCE_FILTERS } from '../../../scripts/utils/constants';
import { apiHost } from '../../../scripts/common/common';


/**
 * Renders Equipment Filter
 * @param {object} data JSON data object for selected site
 */
function _renderEquipmentFilters(data = this.cache.filteredData) {
  const { i18nKeys, $line } = this.cache;
  const lineVal = $line.val();
  let equipmentRecords = [];

  data.equipmentRecords = {};
  data.equipmentRecords.options = [];

  if (lineVal === '') {
    equipmentRecords = data.lines;
  } else {
    equipmentRecords = data.lines.filter(line => line.lineNumber === lineVal);
  }

  data.equipmentRecords.i18nKeys = i18nKeys;
  data.equipmentRecords.seletedFilter = `site,line`;
  equipmentRecords.forEach(equipment => {
    data.equipmentRecords.options.push(...equipment.equipments.map((equipment, index) => ({
      key: equipment.equipmentNumber,
      desc: equipment.equipmentName,
      docId: `#document${index}`
    })));
  });

  render.fn({
    template: 'documentsFilteringTable',
    data: data.equipmentRecords,
    target: '.js-documents__equipments'
  });
}
/**
 * Filter the line values
 */
function _processLineData() {
  const siteVal = this.cache.$site.val();
  let { data } = this.cache;
  if (Array.isArray(data.installations)) {
    const [filteredData] = data.installations.filter(site => site.customerNumber === siteVal);
    if (filteredData) {
      data = this.cache.filteredData = filteredData;
      this.renderLineFilters(filteredData);
    } else {
      data = {};
    }
  }
}
/**
 * Renders Line Filter
 * @param {object} data JSON data object for selected site
 */
function _renderLineFilters(data = this.cache.filteredData) {
  if (Array.isArray(data.lines)) {
    data.linesRecords = {};
    data.linesRecords.options = data.lines.map((line) => ({
      key: line.lineNumber,
      desc: line.lineDesc
    }));

    render.fn({
      template: 'options',
      data: data.linesRecords,
      target: '.js-documents-filtering__line'
    });

    this.renderEquipmentFilters(data);
  }
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
 * To fetch all the sites
 */
function _renderSiteFilters() {
  const $this = this;
  auth.getToken(({ data: authData }) => {
    render.fn({
      template: 'documentsFiltering',
      url: {
        path: `${apiHost}/${API_MAINTENANCE_FILTERS}`
      },
      target: '.js-documents__installments',
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
        $this.processLineData();
        $this.renderEquipmentFilters();
      }
    });
  });
}

class Documents {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  /**
  * Initialize selector cache after filters rendering
  */
  initPostCache() {
    this.cache.$site = this.root.find('.js-documents-filtering__site');
    this.cache.$line = this.root.find('.js-documents-filtering__line');
  }
  /**
  * Initialize selector cache on component load
  */
  initCache() {
    /* Initialize selector cache here */
    this.cache.configJson = this.root.find('.js-documents__config').text();
    try {
      this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
  }
  bindEvents() {
    /* Bind jQuery events here */
    this.root
      .on('change', '.js-documents-filtering__site', () => {
        logger.log('site', $(this));
        this.processLineData();
      })
      .on('change', '.js-documents-filtering__line', () => {
        this.renderEquipmentFilters();
      });
  }
  renderEquipmentFilters = (data) => _renderEquipmentFilters.call(this, data);
  processLineData = () => _processLineData.call(this);
  renderLineFilters = (data) => _renderLineFilters.call(this, data);
  processSiteData = (...arg) => _processSiteData.apply(this, arg);
  renderSiteFilters = () => _renderSiteFilters.call(this);
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.renderSiteFilters();
  }
}

export default Documents;
