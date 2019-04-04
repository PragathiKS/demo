import $ from 'jquery';
import 'bootstrap';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import auth from '../../../scripts/utils/auth';
import { tableSort } from '../../../scripts/common/common';
//import { apiHost } from '../../../scripts/common/common';
//import { ajaxMethods, API_FINANCIALS_STATEMENTS } from '../../../scripts/utils/constants';
import { ajaxMethods } from '../../../scripts/utils/constants';
import deparam from 'jquerydeparam';

/**
 * Download Invoice
 */
function _downloadInvoice() {
  window.open($(this).attr('href'), '_blank');
}

/**
 * Get filters data
 */
function _getFilters() {
  const filters = $('.js-financial-statement__filters').serialize();
  const filterProp = deparam(filters);
  if (filterProp.daterange) {
    const [orderdateFrom, orderdateTo] = filterProp.daterange.split(' - ');
    filterProp['invoicedate-from'] = orderdateFrom.trim();
    if (orderdateTo) {
      filterProp['invoicedate-to'] = orderdateTo.trim();
    }
    delete filterProp.daterange;
  }
  filterProp['customerkey'] = $('.js-financial-statement__find-customer').val();

  Object.keys(filterProp).forEach(key => {
    if (!filterProp[key]) {
      delete filterProp[key];
    }
  });

  return $.param(filterProp);
}

function _processTableData(data) {
  let keys = [];
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

  if (data.summaryHeadingI18n) {
    data.summaryHeadingI18n = `${data.summaryHeadingI18n} : ${$('.js-financial-statement__date-range').val()}`;
  }
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
        //path: `${apiHost}/${API_FINANCIALS_STATEMENTS}`,
        path: '/apps/settings/wcm/designs/customerhub/jsonData/financialsStatementSummary.json',
        data: filterParams
      },
      beforeRender(data) {
        if (!data) {
          this.data = data = {
            isError: true
          };
        }
        data = $.extend(true, data, $this.cache.i18nKeys);
        return $this.processTableData([data]);
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
    this.cache.configJson = $('.js-financial-statement__config').text();
    this.cache.$filtersRoot = $('.js-financial-statement');
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
      .on('click', '.js-financials-summary__documents__row', this.downloadInvoice);
    this.cache.$filtersRoot.on('financialSummary.render', this.renderTable);
  }
  renderTable = () => {
    const filterParams = this.getFilters();
    _renderTable.apply(this, [filterParams]);
  }
  processTableData(data) {
    return _processTableData.apply(this, data);
  }
  downloadInvoice() {
    return _downloadInvoice.call(this);
  }
  getFilters = () => _getFilters.apply(this);
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default FinancialsStatementSummary;
