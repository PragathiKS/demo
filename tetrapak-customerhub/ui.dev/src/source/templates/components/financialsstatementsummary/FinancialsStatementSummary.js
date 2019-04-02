import $ from 'jquery';
import 'bootstrap';
import { render } from '../../../scripts/utils/render';
import auth from '../../../scripts/utils/auth';
//import { apiHost } from '../../../scripts/common/common';
//import { ajaxMethods, API_FINANCIALS_STATEMENTS } from '../../../scripts/utils/constants';
import { ajaxMethods } from '../../../scripts/utils/constants';

/**
 * Processes financials table data
 * @param {object} data data object
 * @param {string[]} keys List of keys
 */
function _tableSort(data, keys) {
  const dataObject = {
    row: []
  };
  keys.forEach((key, index) => {
    const value = data[key];
    dataObject.row[index] = {
      key,
      value,
      isRTE: [''].includes(key)
    };
  });
  return dataObject;
}

function _processTableData(data) {
  let keys = [];
  if (Array.isArray(data.statementSummary)) {
    data.summary = data.statementSummary.map(summary => {
      keys = (keys.length === 0) ? Object.keys(summary) : keys;
      return _tableSort.call(this, summary, keys);
    });
    data.summaryHeadings = keys.map(key => ({
      key,
      i18nKey: `cuhu.financials.${key}`
    }));
  }

  if (Array.isArray(data.documents)) {
    data.documents.forEach((document, index) => {
      document.title = `${document.title} (${document.records.length})`;
      document.documnetID = `#document${index}`;
      document.documentData = document.records.map(record => {
        keys = Object.keys(record);
        return _tableSort.call(this, record, keys);
      });
      document.documentHeadings = keys.map(key => ({
        key,
        i18nKey: `cuhu.financials.${key}`
      }));
    });
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
    /**
     * If component is added more than once please use "this.root" hook to prevent overlap issues.
     * Example:
     * this.cache.$submitBtn = this.root.find('.js-submit-btn');
     */
  }
  bindEvents() {
    /* Bind jQuery events here */
    /**
     * Example:
     * const { $submitBtn } = this.cache;
     * $submitBtn.on('click', () => { ... });
     */
  }
  renderTable() {
    return _renderTable.apply(this, arguments);
  }
  processTableData(data) {
    return _processTableData.apply(this, data);
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.renderTable();
  }
}

export default FinancialsStatementSummary;
