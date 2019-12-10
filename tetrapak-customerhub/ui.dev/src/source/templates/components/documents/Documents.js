import $ from 'jquery';
import 'bootstrap';
import auth from '../../../scripts/utils/auth';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import { ajaxMethods, API_DOCUMENTS_FILTERS, API_DOCUMENTS_SEARCH } from '../../../scripts/utils/constants';
import { getI18n } from '../../../scripts/common/common';
import { getURL } from '../../../scripts/utils/uri';
import { trackAnalytics } from '../../../scripts/utils/analytics';

/**
 * Fire analytics on click of
 * filters and document links
 */
function _trackAnalytics(name, obj) {
  const analyticsData = {
    linkType: 'internal',
    linkSection: 'installed equipment-documents',
    linkName: 'documents tab selection',
    linkParentTitle: 'documents tab'
  };
  const { equipmentresultscount } = this.cache;

  switch (name) {
    case 'site': {
      analyticsData.linkSelection = 'site';
      analyticsData.equipmentresultscount = equipmentresultscount;
      break;
    }
    case 'line': {
      analyticsData.linkSelection = 'line/area';
      analyticsData.equipmentresultscount = equipmentresultscount;
      break;
    }
    case 'document': {
      const linkName = obj.find('.tpatom-link__text').text();
      const linkParentTitle = `${obj.data('equipment')}-${obj.data('doc-type')}`;
      analyticsData.linkType = 'external';
      analyticsData.linkName = linkName;
      analyticsData.linkParentTitle = linkParentTitle;
      break;
    }
    default: {
      break;
    }
  }

  trackAnalytics(analyticsData, 'linkClick', 'linkClicked', undefined, false);
}

/**
 * Process Documents Data
 * @param {object} documentsData JSON data object for equipments documents
 */
function _processDocumentsData(documentsData) {
  const { equipments, results, i18nKeys } = documentsData;

  if (Array.isArray(equipments) && Array.isArray(results)) {
    results.forEach((result, resultIndex) => {
      result.docTypes.forEach((docType, docIndex) => {
        docType.docId = `#document${resultIndex}${docIndex}`;
      });
    });

    equipments.forEach(equipment => {
      equipment.documents = results.filter(document => equipment.serialNo === document.serial)[0];
      equipment.titleWithCount = `${equipment.title} (${equipment.documents.docCount} ${getI18n(i18nKeys.documentLabel)})`;
    });

    documentsData.equipments = equipments.filter(equipment => equipment.documents.docCount > 0);
  }
}

/**
 * Fetch, Process and Render Documents
 * @param {object} equipmentData JSON data object for filtered equipments
 */
function _renderDocuments(equipmentData) {
  const $this = this;
  const allEquipments = equipmentData.equipments.map((option) => option.serialNo).join(',');
  auth.getToken(({ data: authData }) => {
    render.fn({
      template: 'documentsFilteringTable',
      url: {
        path: getURL(API_DOCUMENTS_SEARCH),
        data: {
          'serial': allEquipments
        }
      },
      target: '.js-documents__equipments',
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
          data.i18nKeys = equipmentData.i18nKeys;
        } else if (data.totalDocuments === 0) {
          data.noData = true;
          data.i18nKeys = equipmentData.i18nKeys;
        } else {
          $.extend(data, equipmentData);
          $this.processDocumentsData(data);

          const { name } = $this.cache;
          if (name) {
            $this.cache.equipmentresultscount = data.equipments.length;
            $this.trackAnalytics(name);
          }
        }
      }
    });
  });
}


/**
 * Renders Equipment Filter
 * @param {object} data JSON data object for selected site
 */
function _renderEquipmentFilters(data = this.cache.filteredData) {
  const { i18nKeys, $line, techPubHost } = this.cache;
  const lineVal = $line.val();
  let lines = [];

  data.equipmentData = {};
  data.equipmentData.equipments = [];

  if (lineVal === '') {
    lines = data.lines;
  } else {
    lines = data.lines.filter(line => line.lineNumber === lineVal);
  }

  data.equipmentData.i18nKeys = i18nKeys;
  data.equipmentData.techPubHost = techPubHost;
  data.equipmentData.selectedFilter = `${this.selectedLine},${this.selectedSite}`;

  lines.forEach(line => {
    data.equipmentData.equipments.push(...line.equipments.map((equipment, index) => ({
      key: equipment.equipmentNumber,
      serialNo: equipment.serialNumber,
      title: equipment.equipmentName,
      equipmentId: `#equipment${index}`
    })));
  });

  this.renderDocuments(data.equipmentData);
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
        path: getURL(API_DOCUMENTS_FILTERS)
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
        const { i18nKeys } = $this.cache;

        if (!data) {
          this.data = data = {
            isError: true,
            i18nKeys
          };
        } else {
          data.i18nKeys = i18nKeys;

          $this.processSiteData(data);
          $this.cache.data = data;
        }
      }
    }, (data) => {
      if (!data.isError && !data.noData) {
        $this.initPostCache();
        $this.processLineData();
        // fetching the default selected site and line
        $this.selectedSite = $this.cache.filteredData.customerName;
        $this.selectedLine = $this.cache.filteredData.lines[0].lineDesc;

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
    this.cache.configJson = this.root.find('.js-documents__config').text();
    try {
      this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
    this.cache.techPubHost = this.root.find('.js-tech-pub-host').val();
  }

  bindEvents() {
    const self = this;
    this.root
      .on('change', '.js-documents-filtering__site', function () {
        const siteAndLineRecords = self.cache.data;
        const matchedSite = siteAndLineRecords.sites.filter(site => site.key === $(this).val());
        self.selectedSite = matchedSite[0].desc;
        const matchedLine = siteAndLineRecords.installations.filter(site => site.customerNumber === $(this).val());
        self.selectedLine = matchedLine[0].lines[0].lineDesc;

        self.cache.name = 'site';

        self.processLineData();

      })
      .on('change', '.js-documents-filtering__line', function () {
        const filteredLines = self.cache.filteredData.lines;
        const matchedLine = filteredLines.filter(line => line.lineNumber === $(this).val());
        self.selectedLine = matchedLine[0].lineDesc;

        self.cache.name = 'line';

        self.renderEquipmentFilters();
      })
      .on('click', '.js-documents__document', function () {
        self.trackAnalytics('document', $(this));
      });
  }

  renderEquipmentFilters = (data) => _renderEquipmentFilters.call(this, data);
  processLineData = () => _processLineData.call(this);
  renderLineFilters = (data) => _renderLineFilters.call(this, data);
  processSiteData = (...arg) => _processSiteData.apply(this, arg);
  renderSiteFilters = () => _renderSiteFilters.call(this);
  renderDocuments = (data) => _renderDocuments.call(this, data);
  processDocumentsData = (data) => _processDocumentsData.call(this, data);
  trackAnalytics = (name, obj) => _trackAnalytics.call(this, name, obj);

  init() {
    this.initCache();
    this.bindEvents();
    this.renderSiteFilters();
  }
}

export default Documents;
