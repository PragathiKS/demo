import $ from 'jquery';
import { render } from '../../../scripts/utils/render';
import auth from '../../../scripts/utils/auth';
import { apiHost } from '../../../scripts/common/common';
import { ajaxMethods, API_FINANCIALS_STATEMENTS } from '../../../scripts/utils/constants';

/**
 * Processes order table data
 * @param {object} order Order object
 * @param {string[]} keys List of keys
 * @param {string} orderDetailLink order detail link
 */
function _tableSort(order, keys) {
  const dataObject = {
    row: []
  };
  keys.forEach((key, index) => {
    const value = order[key];
    dataObject.row[index] = {
      key,
      value,
      isRTE: ['contact'].includes(key)
    };
  });
  return dataObject;
}

function _processTableData(data) {
  let keys = [];
  if (Array.isArray(data.statementSummary)) {
    data.summary = data.statementSummary.map(order => {
      keys = (keys.length === 0) ? Object.keys(order) : keys;
      return _tableSort.call(this, order, keys);
    });
    data.summaryHeadings = keys.map(key => ({
      key,
      i18nKey: `cuhu.ordering.${key}`,
      isSortable: ['orderDate'].includes(key),
      sortOrder: 'desc'
    }));
  }

  if (Array.isArray(data.salesOffices)) {
    data.salesOffices.forEach(function (elem, index) {
      data.documentHeadings = keys.map(key => ({
        key,
        i18nKey: `cuhu.ordering.${key}`,
        isSortable: ['orderDate'].includes(key),
        sortOrder: 'desc'
      }));
    })
  }
  debugger; //eslint-disable-line
}

/**
 * Renders table section
 * @param {object} filterParams Selected filter parameters
 */
function _renderTable(filterParams) {
  //const { $filters } = this.cache;
  const $this = this;
  //this.setSearchFields(filterParams);
  //this.root.find('.js-pagination').trigger('ordersearch.pagedisabled');
  auth.getToken(({ data: authData }) => {
    render.fn({
      template: 'financialsSummaryTable',
      target: '.js-financials-summary__table',
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
        return _processTableData.apply($this, [data]);
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
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.renderTable();
  }
}

export default FinancialsStatementSummary;
