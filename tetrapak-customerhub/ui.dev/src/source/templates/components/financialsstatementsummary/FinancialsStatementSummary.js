import $ from 'jquery';
import { route } from 'jqueryrouter';
import 'bootstrap';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import auth from '../../../scripts/utils/auth';
import { tableSort, resolveQuery, resolveCurrency, getI18n } from '../../../scripts/common/common';
import { ajaxMethods, API_FINANCIALS_STATEMENTS } from '../../../scripts/utils/constants';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import { toast } from '../../../scripts/utils/toast';
import { getURL } from '../../../scripts/utils/uri';
import { isIOS } from '../../../scripts/utils/browserDetect';
import file from '../../../scripts/utils/file';

/**
 * Error tracking
 * @param {string} type
 */
function _trackErrors(linkParentTitle, linkName, errorMessage) {
  linkParentTitle = linkParentTitle.toLowerCase();
  linkName = linkName.toLowerCase();
  errorMessage = errorMessage.toLowerCase();
  const obj = {
    linkType: 'internal',
    linkSection: 'financials-error',
    linkParentTitle,
    linkName,
    errorMessage
  };
  trackAnalytics(obj, 'linkClick', 'linkClicked', undefined, false);
}

/**
 * Fire analytics on Invoice Download
 * @param {object} self
 * @param {string} type
 * @param {object} data
 */
function _trackAnalytics(type, linkName, el) {
  const obKey = 'linkClick';
  const trackingKey = 'linkClicked';
  const { statementOfAccount = '' } = this.cache.i18nKeys;
  linkName = $.trim(linkName).toLowerCase();
  const ob = {
    linkType: 'internal',
    linkSection: 'financials',
    linkName
  };

  switch (type) {
    case 'downloadPdf': {
      ob.linkParentTitle = $.trim(getI18n(statementOfAccount)).toLowerCase();
      break;
    }
    case 'downloadInvoice': {
      ob.linkParentTitle = $.trim($(el).parents('table').data('salesOffice')).toLowerCase();
      break;
    }
    case 'downloadExcel': {
      ob.linkParentTitle = $.trim(getI18n(statementOfAccount)).toLowerCase();
      break;
    }
    case 'documents': {
      ob.linkParentTitle = 'documents';
      break;
    }
    default: break;
  }
  trackAnalytics(ob, obKey, trackingKey, undefined, false);
}

/**
 * Download Invoice
 */
function _downloadInvoice(self) {
  const { downloadInvoice } = self.cache;
  const $this = $(this);
  const documentNumber = $.trim($this.find('[data-key="documentNumber"]').text());
  const linkParentTitle = $this.parents('.js-financials-summary__table').data('sectionTitle');
  auth.getToken(() => {
    file.get({
      extension: 'pdf',
      filename: `${documentNumber}`,
      url: resolveQuery(downloadInvoice, {
        docId: documentNumber
      }),
      method: ajaxMethods.GET
    }).then(() => {
      if (!isIOS()) {
        self.trackAnalytics.apply(self, ['downloadInvoice', 'invoice download', this]);
      }
    }).catch(() => {
      const { i18nKeys } = self.cache;
      toast.render(
        i18nKeys.fileDownloadErrorText,
        i18nKeys.fileDownloadErrorClose
      );
      self.trackErrors(linkParentTitle, 'invoice download', getI18n(i18nKeys.fileDownloadErrorText));
    });
  });
}

function _processTableData(data) {
  let keys = [];
  const { $filtersRoot, currencyFields, documentType, status } = this.cache;
  if (Array.isArray(data.summary)) {
    data.summary = data.summary.map(summary => {
      keys = (keys.length === 0) ? Object.keys(summary) : keys;
      // Resolve currency for summary section
      keys.forEach(key => {
        if (currencyFields.includes(key)) {
          summary[key] = resolveCurrency(summary[key], summary.currency);
        }
      });
      return tableSort.call(this, summary, keys);
    });
    data.summaryHeadings = keys.map(key => ({
      key,
      i18nKey: `cuhu.financials.${key}`
    }));
  }
  if (Array.isArray(data.documents) && data.documents.length > 0) {
    keys.length = 0;
    data.documents = data.documents.filter(doc => doc.records && doc.records.length);
    if (!data.documents.length) {
      data.noData = true;
    } else {
      data.documents.forEach((doc, index) => {
        doc.title = `${doc.salesOffice} (${doc.records.length})`;
        doc.docId = `#document${index}`;
        doc.totalAmount = resolveCurrency(doc.totalAmount, doc.currency);
        const deleteLocalData = Array.isArray(doc.records) && doc.records[0] && !doc.records[0].salesLocalData;
        doc.docData = doc.records.map(record => {
          let isClickable = false;
          let dataLink;
          if (deleteLocalData) {
            delete record.salesLocalData;
          }
          delete record.salesOffice; // Hide sales office
          if (keys.length === 0) {
            keys = Object.keys(record);
            keys.splice(keys.indexOf('orgAmount'), 1);
            keys.push('orgAmount');
          }
          // Resolve currency for summary section
          keys.forEach(key => {
            if (key === 'documentType' && record[key] !== 'PMT') {
              isClickable = true;
              dataLink = record.invoiceReference;
              record.documentNumber = render.get('downloadInvoice')({
                documentNumber: record.documentNumber
              });
            }

            if (key === 'invoiceStatus' && Array.isArray(status)) {
              const statusMatch = status.find(obj => obj.key === record[key]);
              record[key] = statusMatch ? statusMatch.desc : record[key];
            }

            if (key === 'documentType' && Array.isArray(documentType)) {
              const documentTypeMatch = documentType.find(obj => obj.key === record[key]);
              record[key] = documentTypeMatch ? documentTypeMatch.desc : record[key];
            }
            if (currencyFields.includes(key)) {
              record[key] = resolveCurrency(record[key], record.currency);
            }
          });
          return tableSort.call(this, record, keys, dataLink, isClickable, ['documentNumber']);
        });
        doc.docHeadings = keys.map(key => ({
          key,
          i18nKey: `cuhu.financials.${key}`
        }));
      });
    }
  } else {
    data.noData = true;
  }
  data.dateRange = $filtersRoot.find('.js-financial-statement__date-range-input').val();
}

/**
 * Returns query parameter object as per API requirements
 * @param {object} filterParams Route query parameters
 */
function _getRequestParams(filterParams) {
  const queryParam = $.extend({}, filterParams);
  queryParam.customernumber = queryParam.customerkey;
  delete queryParam.customerkey;
  queryParam['invoice-status'] = queryParam.status;
  delete queryParam.status;
  if (queryParam['invoicedate-to']) {
    delete queryParam['soa-date'];
  } else {
    delete queryParam['invoicedate-from'];
    delete queryParam['invoicedate-to'];
  }
  return queryParam;
}

/**
 * Renders table section
 * @param {object} filterParams Selected filter parameters
 */
function _renderTable(filterParams) {
  const $this = this;
  auth.getToken(({ data: authData }) => {
    render.fn({
      template: 'financialsSummaryTable',
      target: '.js-financials-summary',
      url: {
        path: getURL(API_FINANCIALS_STATEMENTS),
        data: _getRequestParams(filterParams)
      },
      beforeRender(data) {
        if (!data) {
          this.data = data = {
            isError: true
          };
        }
        data = $.extend(true, data, $this.cache.i18nKeys);
        data.params = filterParams;
        return $this.processTableData(data);
      },
      ajaxConfig: {
        beforeSend(jqXHR) {
          jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
          jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        method: ajaxMethods.GET,
        cache: true,
        showLoader: true,
        cancellable: true
      }
    }, (data) => {
      if (!data.isError) {
        const { $filtersRoot } = this.cache;
        $filtersRoot.find('.js-financial-statement__filter-section').removeClass('d-none');
      }
    });
  });
}

class FinancialsStatementSummary {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    /* Initialize selector cache here */
    this.cache.$parentRoot = this.root.parent();
    this.cache.$filtersRoot = this.cache.$parentRoot.find('.js-financial-statement');
    this.cache.$findCustomer = this.cache.$parentRoot.find('.js-financial-statement__select-customer-dropdown');
    this.cache.configJson = this.cache.$filtersRoot.find('.js-financial-statement__config').text();
    this.cache.currencyFields = [
      'current',
      'ninty',
      'nintyPlus',
      'overdue',
      'sixty',
      'thirty',
      'total',
      'orgAmount',
      'remAmount',
      'totalAmount'
    ];
    try {
      this.cache.i18nKeys = JSON.parse(this.cache.configJson);
      this.cache.downloadInvoice = this.cache.$parentRoot.find('#downloadInvoice').val();
    } catch (e) {
      this.cache.i18nKeys = {};
      this.cache.downloadInvoice = '';
      this.cache.currencyFields = [];
      logger.error(e);
    }
  }
  bindEvents() {
    this.root
      .on('click', '.js-financials-summary__documents__row', this, this.downloadInvoice)
      .on('click', '.js-financials-summary__create-pdf,.js-financials-summary__create-excel', this, function (e) {
        const self = e.data;
        const downloadType = $(this).data('downloadType');
        const trackingType = downloadType === 'pdf' ? 'downloadPdf' : 'downloadExcel';
        const linkName = downloadType === 'pdf' ? 'create pdf' : 'create excel';
        if (isIOS()) {
          self.trackAnalytics.apply(self, [trackingType, linkName]);
        }
        self.downloadPdfExcel(downloadType, this);
      })
      .on('click', '.js-financials-summary__accordion.collapsed', this, function (e) {
        const self = e.data;
        const documentTitleTotal = $(this).text();
        const documentTitle = documentTitleTotal.substring(0, documentTitleTotal.indexOf('(') - 1);
        self.trackAnalytics.apply(self, ['documents', documentTitle]);
      });
    this.cache.$parentRoot
      .on('financial.filters', this, function (...args) {
        const [e, status, documentType] = args;
        const self = e.data;
        self.cache.status = status;
        self.cache.documentType = documentType;
      })
      .on('financial.error', (...args) => {
        const [, linkParentTitle, linkName, errorMessage] = args;
        this.trackErrors(linkParentTitle, linkName, errorMessage);
      })
      .on('financial.analytics', (...args) => {
        const [, type, linkName, el] = args;
        this.trackAnalytics.apply(this, [type, linkName, el]);
      });

    route((...args) => {
      const [config, , query] = args;
      if (config.hash) {
        this.renderTable(query);
      }
    });
  }
  renderTable() {
    _renderTable.apply(this, arguments);
  }
  processTableData() {
    return _processTableData.apply(this, arguments);
  }
  downloadInvoice(e) {
    const $this = e.data;
    _downloadInvoice.call(this, $this);
    if (isIOS()) {
      $this.trackAnalytics.apply($this, ['downloadInvoice', 'invoice download', this]);
    }
  }
  downloadPdfExcel(type, el) {
    this.root.parents('.js-financials').trigger('financial.filedownload', [type, el]);
  }
  trackAnalytics() {
    return _trackAnalytics.apply(this, arguments);
  }
  trackErrors() {
    return _trackErrors.apply(this, arguments);
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default FinancialsStatementSummary;
