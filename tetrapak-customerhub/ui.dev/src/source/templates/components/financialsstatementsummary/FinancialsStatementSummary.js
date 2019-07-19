import $ from 'jquery';
import { route } from 'jqueryrouter';
import 'bootstrap';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import auth from '../../../scripts/utils/auth';
import { tableSort, resolveQuery, resolveCurrency } from '../../../scripts/common/common';
import { ajaxMethods, API_FINANCIALS_STATEMENTS } from '../../../scripts/utils/constants';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import { fileWrapper } from '../../../scripts/utils/file';
import { toast } from '../../../scripts/utils/toast';
import { getURL } from '../../../scripts/utils/uri';

/**
 * Fire analytics on Invoice Download
 */
function _trackAnalytics(self, type, data) {
  const $this = self;
  let ob = {
    linkType: 'internal',
    linkSection: 'financials'
  };
  const obKey = 'linkClick';
  const trackingKey = 'linkClicked';
  const { statementOfAccount = '' } = $this.cache.i18nKeys;

  switch (type) {
    case 'downloadPdf': {
      ob.linkParentTitle = statementOfAccount.toLowerCase();
      ob.linkName = 'create pdf';
      break;
    }
    case 'downloadInvoice': {
      ob.linkParentTitle = $(this).parents('table').data('salesOffice').toLowerCase();
      ob.linkName = 'invoice download';
      break;
    }
    case 'downloadExcel': {
      ob.linkParentTitle = statementOfAccount.toLowerCase();
      ob.linkName = 'create excel';
      break;
    }
    case 'documents': {
      ob.linkParentTitle = 'documents';
      ob.linkName = data.toLowerCase();
      break;
    }
    default: {
      break;
    }
  }
  trackAnalytics(ob, obKey, trackingKey, undefined, false);
}

/**
 * Download Invoice
 */
function _downloadInvoice($this) {
  const { downloadInvoice } = $this.cache;
  const documentNumber = $.trim($(this).find('[data-key="documentNumber"]').text());
  auth.getToken(() => {
    fileWrapper({
      extension: 'pdf',
      filename: `${documentNumber}`,
      url: resolveQuery(downloadInvoice, {
        docId: documentNumber
      }),
      method: ajaxMethods.GET
    }).catch(() => {
      const { i18nKeys } = $this.cache;
      toast.render(
        i18nKeys.fileDownloadErrorText,
        i18nKeys.fileDownloadErrorClose
      );
    });
  });
}

function _processTableData(data) {
  let keys = [];
  const { $filtersRoot, currencyFields } = this.cache;
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
        const deleteLocalData = doc.records.length > 0 ? doc.records[0].salesLocalData === '' : true;
        doc.docData = doc.records.map(record => {
          let isClickable = false;
          let dataLink;
          if (deleteLocalData) {
            delete record.salesLocalData;
          }
          delete record.salesOffice;
          if (keys.length === 0) {
            keys = Object.keys(record);
            keys.splice(keys.indexOf('orgAmount'), 1);
            keys.push('orgAmount');
          }
          // Resolve currency for summary section
          keys.forEach(key => {

            if (key === 'documentType' && (record[key] === 'INV' || record[key] === 'CM')) {
              isClickable = true;
              dataLink = record.invoiceReference;
              record['documentNumber'] = `
              <span class="tp-financials-summary__download-invoice">
                ${record['documentNumber']}
                <i class="icon-PDF"></i>
              </span>`;
            }

            if (key === 'invoiceStatus') {
              record[key] = record[key] === 'O' ? 'Open' : record[key] === 'C' ? 'Cleared' : record[key] === 'B' ? 'Both' : record[key];
            }

            if (key === 'documentType') {
              record[key] = record[key] === 'INV' ? 'Invoice' : record[key] === 'CM' ? 'Credit Memo' : record[key] === 'PMT' ? 'Payment' : record[key];
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
  data.dateRange = $filtersRoot.find('.js-financial-statement__date-range').val();
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
          jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token} `);
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
    /* Bind jQuery events here */
    this.root
      .on('click', '.js-financials-summary__documents__row', this, this.downloadInvoice);
    this.root.on('click', '.js-financials-summary__create-pdf', this, function (e) {
      const $this = e.data;
      $this.trackAnalytics($this, 'downloadPdf');
      $this.downloadPdfExcel('pdf', this);
    });
    this.root.on('click', '.js-financials-summary__create-excel', this, function (e) {
      const $this = e.data;
      $this.trackAnalytics($this, 'downloadExcel');
      $this.downloadPdfExcel('excel', this);
    });
    this.root.on('click', '.js-financials-summary__accordion.collapsed', this, function (e) {
      const $this = e.data;
      const documentTitleTotal = $(this).text();
      const documentTitle = documentTitleTotal.substring(0, documentTitleTotal.indexOf('(') - 1);
      $this.trackAnalytics($this, 'documents', documentTitle);
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
    $this.trackAnalytics(this, 'downloadInvoice');
  }
  downloadPdfExcel(type, el) {
    this.root.parents('.js-financials').trigger('financial.filedownload', [type, el]);
  }

  trackAnalytics = (obj, type, data) => _trackAnalytics.call(obj, this, type, data);
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default FinancialsStatementSummary;
