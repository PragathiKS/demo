import $ from 'jquery';
import { route } from 'jqueryrouter';
import 'bootstrap';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import auth from '../../../scripts/utils/auth';
import { tableSort, resolveQuery, resolveCurrency, getI18n, hasOwn } from '../../../scripts/common/common';
import { ajaxMethods, API_FINANCIALS_STATEMENTS, EVT_FINANCIAL_ERROR, EVT_FINANCIAL_FILTERS, EVT_FINANCIAL_ANALYTICS, SOA_FORM_LOAD_MSG, EVT_FINANCIAL_FILEDOWNLOAD, CURRENCY_FIELDS, SOA_DOCUMENT_FIELDS } from '../../../scripts/utils/constants';
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
function _trackValues(type, linkName, el, linkSelection) {
  const { statementOfAccount = '' } = this.cache.i18nKeys;
  linkName = $.trim(linkName).toLowerCase();
  const ob = {
    linkType: 'internal',
    linkSection: 'financials',
    linkName
  };

  switch (type) {
    case 'downloadPdf':
    case 'downloadExcel':
    case 'reset':
      ob.linkParentTitle = $.trim(getI18n(statementOfAccount)).toLowerCase();
      break;
    case 'search':
      ob.linkParentTitle = $.trim(getI18n(statementOfAccount)).toLowerCase();
      ob.linkSelection = linkSelection;
      break;
    case 'downloadInvoice':
      ob.linkParentTitle = $.trim($(el).parents('table').data('salesOffice')).toLowerCase();
      break;
    case 'documents':
      ob.linkParentTitle = 'documents';
      break;
    default: break;
  }
  trackAnalytics(ob, 'linkClick', 'linkClicked', undefined, false);
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
        self.trackValues.apply(self, ['downloadInvoice', 'invoice download', this]);
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

function _processKeys(keys, ob) {
  return keys.length === 0 ? Object.keys(ob) : keys;
}

function _mapCurrency(keys, ob) {
  keys.forEach(key => {
    if (CURRENCY_FIELDS.includes(key)) {
      ob[key] = resolveCurrency(ob[key], ob.currency);
    }
  });
}

function _mapHeadings(keys) {
  return keys.map(key => ({
    key,
    i18nKey: `cuhu.financials.${key}`
  }));
}

function _isTypePayment(record) {
  return record.documentType === 'PMT';
}

function _hasDownloadableInvoice(record) {
  const hasOutputIndication = hasOwn(record, 'outputIndication');
  return (
    (hasOutputIndication && !!$.trim(record.outputIndication))
    || !hasOutputIndication
  ) && !_isTypePayment(record);
}

function _processTableData(data) {
  let keys = [];
  const { $filtersRoot, documentType, status } = this.cache;
  if (Array.isArray(data.summary)) {
    data.summary = data.summary.map(summary => {
      keys = _processKeys(keys, summary);
      _mapCurrency(keys, summary);
      return tableSort.call(this, summary, keys);
    });
    data.summaryHeadings = _mapHeadings(keys);
  }
  if (Array.isArray(data.documents)) {
    data.documents = data.documents.filter(doc => doc.records && doc.records.length);
    if (data.documents.length === 0) {
      data.noData = true;
    } else {
      data.documents.forEach((doc, index) => {
        doc.title = `${doc.salesOffice} (${doc.records.length})`;
        doc.docId = `#document${index}`;
        doc.totalAmount = resolveCurrency(doc.totalAmount, doc.currency);
        keys = [...SOA_DOCUMENT_FIELDS];
        if (Array.isArray(doc.records) && doc.records[0] && !doc.records[0].salesLocalData) {
          keys.splice(keys.indexOf('salesLocalData'), 1);
        }
        doc.docData = doc.records.map(record => {
          const isClickable = _hasDownloadableInvoice(record);
          let dataLink;
          if (isClickable) {
            dataLink = record.invoiceReference;
            record.documentNumber = render.get('downloadInvoice')({
              documentNumber: record.documentNumber
            });
          }
          if (Array.isArray(status)) {
            const statusMatch = status.find(obj => obj.key === record.invoiceStatus);
            record.invoiceStatus = statusMatch ? statusMatch.desc : record.invoiceStatus;
          }

          if (Array.isArray(documentType)) {
            const documentTypeMatch = documentType.find(obj => obj.key === record.documentType);
            record.documentType = documentTypeMatch ? documentTypeMatch.desc : record.documentType;
          }
          // Resolve currency for summary section
          _mapCurrency(keys, record);
          return tableSort.call(this, record, keys, dataLink, isClickable, ['documentNumber']);
        });
        doc.docHeadings = keys.map(key => ({
          key,
          i18nKey: `cuhu.financials.${key}`
        }));
      });
    }
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
function _renderTable(filterParams, config) {
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
        const { i18nKeys } = $this.cache;
        if (!data) {
          const errorResult = this.xhr.responseJSON;
          const apiErrorCode = errorResult && errorResult.apiErrorCode ? errorResult.apiErrorCode : 'default';
          const severity = errorResult && errorResult.apiErrorSeverity ? errorResult.apiErrorSeverity : 'error';
          const { apiErrorCodes } = i18nKeys;
          this.data = data = {
            isError: true,
            errorText: apiErrorCodes ? apiErrorCodes[apiErrorCode] : 'cuhu.error.message',
            severe: severity !== 'info'
          };
        }
        data = $.extend(true, data, i18nKeys);
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
        showLoader: true
      }
    }, (data) => {
      const { $parentRoot, $filtersRoot } = this.cache;
      const linkName = config && config.isClick
        ? $.trim(config.linkText).toLowerCase()
        : SOA_FORM_LOAD_MSG;
      const $filterSection = $filtersRoot.find('.js-financial-statement__filter-section');
      if (!$filterSection.hasClass('has-error')) {
        $filterSection.removeClass('d-none');
      }
      if (!data.isError) {
        if (
          !isIOS()
          && config
          && config.isClick
        ) {
          this.trackValues.apply(this, [config.type, linkName, null, config.linkSelection]);
        }
      } else {
        const { statementOfAccount = '' } = this.cache.i18nKeys;
        $parentRoot.trigger(EVT_FINANCIAL_ERROR, [
          $.trim(getI18n(statementOfAccount)).toLowerCase(),
          linkName,
          $.trim(data.errorText).toLowerCase()
        ]);
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
    try {
      this.cache.i18nKeys = JSON.parse(this.cache.configJson);
      this.cache.downloadInvoice = this.cache.$parentRoot.find('#downloadInvoice').val();
    } catch (e) {
      this.cache.i18nKeys = {};
      this.cache.downloadInvoice = '';
      logger.error(e);
    }
  }
  bindEvents() {
    this.root
      .on('click', '.js-financials-summary__documents__row', this, this.downloadInvoice)
      .on('click', '.js-financials-summary__create-pdf,.js-financials-summary__create-excel', this, function (e) {
        const self = e.data;
        const $this = $(this);
        const downloadType = $this.data('downloadType');
        const trackingType = $this.data('trackingType');
        const linkName = getI18n($this.data('linkName'));
        if (isIOS()) {
          self.trackValues.apply(self, [trackingType, linkName]);
        }
        self.downloadPdfExcel(downloadType, trackingType, linkName, this);
      })
      .on('click', '.js-financials-summary__accordion.collapsed', this, function (e) {
        const self = e.data;
        const documentTitleTotal = $(this).text();
        const documentTitle = documentTitleTotal.substring(0, documentTitleTotal.indexOf('(') - 1);
        self.trackValues.apply(self, ['documents', documentTitle]);
      });
    this.cache.$parentRoot
      .on(EVT_FINANCIAL_FILTERS, this, function (...args) {
        const [e, status, documentType] = args;
        const self = e.data;
        self.cache.status = status;
        self.cache.documentType = documentType;
      })
      .on(EVT_FINANCIAL_ERROR, (...args) => {
        const [, linkParentTitle, linkName, errorMessage] = args;
        this.trackErrors(linkParentTitle, linkName, errorMessage);
      })
      .on(EVT_FINANCIAL_ANALYTICS, (...args) => {
        const [, type, linkName, el] = args;
        this.trackValues.apply(this, [type, linkName, el]);
      });

    route((...args) => {
      const [config, , query] = args;
      if (config.hash) {
        this.renderTable(query, config);
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
      $this.trackValues.apply($this, ['downloadInvoice', 'invoice download', this]);
    }
  }
  downloadPdfExcel(type, trackingType, linkName, el) {
    this.root.parents('.js-financials').trigger(EVT_FINANCIAL_FILEDOWNLOAD, [type, trackingType, linkName, el]);
  }
  trackValues() {
    return _trackValues.apply(this, arguments);
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
