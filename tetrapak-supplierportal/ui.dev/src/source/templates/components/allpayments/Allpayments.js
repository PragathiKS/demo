import auth from '../../../scripts/utils/auth';
import { _paginate } from './allpayments.paginate';
import { render } from '../../../scripts/utils/render';
import {  _paginationAnalytics } from './allpayments.analytics';

class AllPayments {

  constructor({ el }) {
    this.root = el;
    this.cache = {};
  }

  initCache() {
    const config = {
      showFields: ['documentDate', 'dueCalculationBaseDate', 'companyName', 'companyCode','companyCountry', 'amountInTransactionCurrency', 'withholdingTaxAmmount', 'invoiceStatusCode', 'documentReferenceID', 'supplierName', 'supplier', 'purchasingDocuments'],
      sortableKeys: ['documentDate', 'dueCalculationBaseDate', 'companyName', 'companyCode', 'companyCountry', 'amountInTransactionCurrency', 'withholdingTaxAmmount', 'invoiceStatusCode', 'documentReferenceID', 'supplierName', 'supplier'],
      currentPageNumber: 1,
      itemsPerPage: 10,
      activePage: 1,
      skipIndex: 0,
      defaultSortParams: 'documentReferenceID%20asc',
      maxPages: 3,
      i18nkeysMap: {
        'documentDate': 'invoiceDate',
        'dueCalculationBaseDate': 'dueDate',
        'companyName': 'company',
        'companyCode': 'companyCode',
        'companyCountry': 'country',
        'amountInTransactionCurrency': 'amountIncludingTaxes',
        'withholdingTaxAmmount': 'withHoldingTax',
        'invoiceStatusCode': 'status',
        'documentReferenceID': 'invoiceNo',
        'supplierName': 'supplier',
        'supplier': 'supplierCode',
        'purchasingDocuments': 'poNo'
      },
      statusMapping: {
        'In Process': [0,1,2,3,4,5,6,7,11,12,13,14,19,20,21,22,23,24,25,26,27,28,29,30,34,40,41,42,44,45,46,50,51,52,53,54,55,56,61,62,63,64,65,66,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,96,99],
        'Posted' : [15,18,31,32,33,97,98],
        'Rejected': [8,9,10,16,17]}
    };
    const selector = {
      paymentApi: this.root.querySelector('.js-payment-api'),
      spinner: this.root.querySelector('.tp-spinner'),
      content: this.root.querySelector('.tp-payment-content'),
      config: this.root.querySelector('.js-all-payments__config'),
      searchResults: this.root.querySelector('.tp-all-payments__search-count'),
      table: this.root.querySelector('.tp-all-payments__table_wrapper')
    };
    this.cache = {...config, ...selector};
    this.cache.tableData = [];
    this.cache.activeSortData = null;
    this.cache.i18nKeys = JSON.parse(this.cache.config.textContent);
    this.cache.authData = {};
  }

  bindEvents() {
    if(this.cache.table) {
      const self = this;
      this.cache.table.addEventListener('click', function(e) {
        if (e.target.closest('.js-page-number') && !e.target.classList.contains('active')) {
          self.paginationAction(e.target);
        }
        else if(e.target.closest('.js-all-payments__table-summary__sort')) {
          self.sortAction(e.target);
        }
      });
    }
  }

  sortAction = (target) => {
    const sortedByKey = target.closest('.js-all-payments__table-summary__sort').getAttribute('data-key');
    let sortOrder = 'asc';

    // same table header clicked, change from asc to desc
    if (this.cache.activeSortData && this.cache.activeSortData.sortedByKey === sortedByKey) {
      sortOrder = this.cache.activeSortData.sortOrder === 'asc' ? 'desc' : 'asc';
    }

    this.cache.activeSortData = {
      'sortedByKey': sortedByKey,
      'sortOrder': sortOrder
    };
    this.cache.activePage = 1;
    this.cache.skipIndex = 0;
    this.renderPayment();
  }

  paginationAction = (target) => {
    const pageNumDiv = target.closest('.js-page-number'),
      paginationContainer = target.closest('.js-tbl-pagination');

    if (paginationContainer) {
      paginationContainer.classList.add('pagination-lock');
      this.cache.activePage = Number(pageNumDiv.getAttribute('data-page-number'));
      this.cache.skipIndex = Number(pageNumDiv.getAttribute('data-skip'));
      this.renderPayment();
      _paginationAnalytics(pageNumDiv);
    }
  }

  getTableBodyData = () => {
    const showFields = this.cache.showFields;
    const data = this.cache.tableData.map((summary) => {
      const dataObject = {
        row: []
      };

      showFields.forEach((val, index) => {
        dataObject.row[index] = {
          key: val,
          value: summary[val]
        };
      });

      return dataObject;
    });

    return data;
  };


  getHeaderData = () => {
    const sortByKey = this.cache.activeSortData && this.cache.activeSortData.sortedByKey;
    const sortOrder = this.cache.activeSortData && this.cache.activeSortData.sortOrder;

    return this.cache.showFields.map(key => ({
      key,
      myEquipment: true,
      isSortable: this.cache.sortableKeys.includes(key),
      isActiveSort: key === sortByKey,
      sortOrder: sortOrder,
      i18nKey: this.cache.i18nKeys[this.cache.i18nkeysMap[key]]
    }));
  }

  showLoader = (isShow) => {
    if (isShow) {
      this.cache.spinner.classList.remove('d-none');
      this.cache.content.classList.add('d-none');
    }
    else {
      this.cache.spinner.classList.add('d-none');
      this.cache.content.classList.remove('d-none');
    }
  }

  getPaymentApiUrl = () => {
    const paymentApi = this.cache.paymentApi.getAttribute('data-list-api');
    const { itemsPerPage, skipIndex, activeSortData, defaultSortParams } = this.cache;

    // TODO: Need to remove this. For testing purpose we add this from date time.
    let apiUrlRequest = `${paymentApi}?skip=${skipIndex}&count=${itemsPerPage}&fromdatetime=2023-07-01T00:00:00&todatetime=2023-07-30T00:00:00`;
    //let apiUrlRequest = `${paymentApi}?skip=${skipIndex}&count=${itemsPerPage}`;

    if (activeSortData) {
      apiUrlRequest += `&sort=${activeSortData.sortedByKey.toLowerCase()} ${activeSortData.sortOrder}`;
    } else {
      apiUrlRequest += `&sort=${defaultSortParams}`;
    }
    return apiUrlRequest;
  }

  getStatusName = (code) => {
    const data =this.cache.statusMapping;

    for (const key in data) {
      if (data[key].includes(code)) {
        return key;
      }
    }
    return '';
  };

  renderPayment = () => {
    this.showLoader(true);

    auth.getToken(({ data: authData }) => {
      fetch(this.getPaymentApiUrl(), {
        method: 'GET',
        contentType: 'application/json',
        headers: {
          'Authorization': `Bearer ${authData.access_token}`,
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      })
        .then(response => response.json())
        .then(response => {
          this.showLoader(false);
          this.cache.tableData = response.data;

          // Show fields based on requirement ex. render the amount field with currency
          this.cache.tableData = this.cache.tableData.map((item) => ({
            ...item,
            withholdingTaxAmmount: (item.withholdingTaxAmmount) ? `${item.withholdingTaxAmmount  } ${  item.transactionCurrency}` : item.withholdingTaxAmmount,
            amountInTransactionCurrency: (item.amountInTransactionCurrency) ? `${item.amountInTransactionCurrency  } ${  item.transactionCurrency}`: item.amountInTransactionCurrency,
            purchasingDocuments: (item.purchasingDocuments.length > 1) ? this.cache.i18nKeys['multipono']:  item.purchasingDocuments,
            invoiceStatusCode: this.getStatusName(Number(item.invoiceStatusCode))
          }));

          this.cache.meta = response.meta;
          const tableData = {
            summary: this.getTableBodyData(),
            summaryHeadings: this.getHeaderData(),
            i18nKeys:this.cache.i18nKeys,
            meta:this.cache.meta
          };

          this.renderPaginationTableData(tableData);
          this.renderSearchCount();
        })
        .catch(() => {
          this.showLoader(false);
          this.renderErrorTemplate();
        });
    });
  }

  renderSearchCount = () => {
    if (this.cache.meta) {
      this.cache.searchResults.textContent = `${this.cache.meta.total} ${this.cache.i18nKeys['results']}`;
    }
  }

  renderErrorTemplate = () => {
    render.fn({
      template: 'allpaymentsTable',
      data: { noDataMessage: true, noDataFound: this.cache.i18nKeys['fetchError']},
      target: '.tp-all-payments__table_wrapper',
      hidden: false
    });
  }

  renderPaginationTableData = (list) => {
    const paginationObj = _paginate(list.meta.total, this.cache.activePage, this.cache.itemsPerPage, this.cache.maxPages);

    if (list.summary.length === 0) {
      render.fn({
        template: 'allpaymentsTable',
        data: { noDataMessage: true, noDataFound: this.cache.i18nKeys['noDataFound']},
        target: '.tp-all-payments__table_wrapper',
        hidden: false
      });
    }
    else {
      render.fn({
        template: 'allpaymentsTable',
        data: {...list, summary: list.summary, paginationObj: paginationObj },
        target: '.tp-all-payments__table_wrapper',
        hidden: false
      });
    }
  }

  init() {
    this.initCache();
    this.renderPayment();
    this.bindEvents();
  }
}

export default AllPayments;
