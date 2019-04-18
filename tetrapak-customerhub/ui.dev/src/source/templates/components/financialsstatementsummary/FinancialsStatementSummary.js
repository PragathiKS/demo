import $ from 'jquery';
import { route } from 'jqueryrouter';
import 'bootstrap';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import auth from '../../../scripts/utils/auth';
import { apiHost, tableSort } from '../../../scripts/common/common';
import { ajaxMethods, API_FINANCIALS_STATEMENTS } from '../../../scripts/utils/constants';
import { trackAnalytics } from '../../../scripts/utils/analytics';

/**
 * Fire analytics on Invoice Download
 */
function _trackAnalytics(type) {
  // Get selected preferences
  const $this = $(this);
  const analyticsData = {};
  switch (type) {
    case 'downloadPdf': {
      analyticsData.customername = this.cache.$findCustomer.find('.js-financial-statement__find-customer option:selected').text();
      analyticsData.createPDF = 'true';
      trackAnalytics(analyticsData, 'financial', 'statementinvoice');
      break;
    }
    case 'downloadExcel': {
      analyticsData.customername = this.cache.$findCustomer.find('.js-financial-statement__find-customer option:selected').text();
      analyticsData.createExcel = 'true';
      trackAnalytics(analyticsData, 'financial', 'statementinvoice');
      break;
    }
    default: {
      const [statementHeader] = $('[data-target="#' + $this.parents('.js-financials-summary__table').attr('id') + '"]').find('.js-financials-summary__accordion__text').text().split('(');
      analyticsData.statementheader = $.trim(statementHeader);
      analyticsData.statementnumber = $.trim($this.find('[data-key=documentNumber]').text());
      trackAnalytics(analyticsData, 'financial', 'statementinvoice');
    }
  }
}

/**
 * Download Invoice
 */
function _downloadInvoice() {
  window.open($(this).attr('href'), '_blank');
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
        //path: '/apps/settings/wcm/designs/customerhub/jsonData/financialsStatementSummary.json', //Mock JSON
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
    });
    this.root.on('click', '.js-financials-summary__create-excel', () => {
      this.trackAnalytics(this, 'downloadExcel');
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
    $this.trackAnalytics(this);
  }
  trackAnalytics = (obj, type) => _trackAnalytics.call(obj, type);
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default FinancialsStatementSummary;
