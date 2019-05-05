import $ from 'jquery';
import { route } from 'jqueryrouter';
import 'bootstrap';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import auth from '../../../scripts/utils/auth';
import { apiHost, tableSort } from '../../../scripts/common/common';
import { ajaxMethods, API_FINANCIALS_STATEMENTS, API_FINANCIALS_INVOICE } from '../../../scripts/utils/constants';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import { fileWrapper } from '../../../scripts/utils/file';
import { toast } from '../../../scripts/utils/toast';

/**
 * Fire analytics on Invoice Download
 */
function _trackAnalytics(type) {
  let ob = {
    linkType: 'internal',
    linkSection: 'financials'
  };
  const obKey = 'linkClick';
  const trackingKey = 'linkClicked';
  switch (type) {
    case 'downloadPdf': {
      ob.linkParentTitle = 'statement of accounts';
      ob.linkName = 'create pdf';
      break;
    }
    case 'downloadInvoice': {
      ob.linkParentTitle = 'packaging';
      ob.linkName = 'invoice download';
      break;
    }
    case 'downloadExcel': {
      ob.linkParentTitle = 'statement of accounts';
      ob.linkName = 'create excel';
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
  const documentNumber = $.trim($(this).find('[data-key="documentNumber"]').text());
  auth.getToken(({ data: authData }) => {
    fileWrapper({
      extension: 'pdf',
      filename: `${documentNumber}`,
      url: `${apiHost}/${API_FINANCIALS_INVOICE}/${documentNumber}`,
      method: ajaxMethods.GET,
      beforeSend(jqXHR) {
        jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
      }
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
  const { $filtersRoot } = this.cache;
  if (Array.isArray(data.summary)) {
    data.summary = data.summary.map(summary => {
      keys = (keys.length === 0) ? Object.keys(summary) : keys;
      return tableSort.call(this, summary, keys);
    });
    data.summaryHeadings = keys.map(key => ({
      key,
      i18nKey: `cuhu.financials.${key}`
    }));
  }
  if (Array.isArray(data.documents) && data.documents.length > 0) {
    keys.length = 0;
    data.documents.forEach((doc, index) => {
      doc.title = `${doc.salesOffice} (${doc.records.length})`;
      doc.docId = `#document${index}`;
      doc.docData = doc.records.map(record => {
        delete record.salesOffice;
        if (keys.length === 0) {
          keys = Object.keys(record);
          keys.splice(keys.indexOf('orgAmount'), 1);
          keys.push('orgAmount');
        }
        return tableSort.call(this, record, keys, record.invoiceReference);
      });
      doc.docHeadings = keys.map(key => ({
        key,
        i18nKey: `cuhu.financials.${key}`
      }));
    });
  } else {
    data.noData = true;
  }
  data.dateRange = $filtersRoot.find('.js-financial-statement__date-range').val();
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
        path: `${apiHost}/${API_FINANCIALS_STATEMENTS}`,
        data: filterParams
      },
      beforeRender(data) {
        if (!data) {
          this.data = data = {
            isError: true
          };
        }
        data = $.extend(true, data, $this.cache.i18nKeys);
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
    this.cache.$filtersRoot = this.root.parent().find('.js-financial-statement');
    this.cache.$findCustomer = this.root.parent().find('.js-financial-statement__select-customer-dropdown');
    this.cache.configJson = this.cache.$filtersRoot.find('.js-financial-statement__config').text();
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
      .on('click', '.js-financials-summary__documents__row', this, this.downloadInvoice);
    this.root.on('click', '.js-financials-summary__create-pdf', () => {
      this.trackAnalytics(this, 'downloadPdf');
      this.downloadPdfExcel('pdf');
    });
    this.root.on('click', '.js-financials-summary__create-excel', () => {
      this.trackAnalytics(this, 'downloadExcel');
      this.downloadPdfExcel('excel');
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

    _downloadInvoice.call(this);
    $this.trackAnalytics(this, 'downloadInvoice');
  }
  downloadPdfExcel(type) {
    this.root.parents('.js-financials').trigger('downloadFinancialPdfExcel', [type]);
  }

  trackAnalytics = (obj, type) => _trackAnalytics.call(obj, type);
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default FinancialsStatementSummary;
