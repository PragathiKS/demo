import $ from 'jquery';
import auth from '../../../scripts/utils/auth';
import { _paginate } from './allpayments.paginate';
import { render } from '../../../scripts/utils/render';
import {  _paginationAnalytics } from './allpayments.analytics';
import { getI18n } from '../../../scripts/common/common';
import { getFilterDateRange } from '../../../scripts/utils/dateRange';

class AllPayments {

  constructor({ el }) {
    this.root = el;
    this.roots = $(el);
    this.cache = {};
  }

  initCache() {
    const config = {
      showFields: ['invoiceStatusCode', 'documentDate', 'planningDate', 'companyName', 'companyCode','companyCountry', 'amountInTransactionCurrency', 'withholdingTaxAmmount', 'documentReferenceID', 'supplierName', 'supplier', 'purchasingDocuments'],
      sortableKeys: ['documentDate', 'planningDate', 'companyCode', 'companyCountry', 'amountInTransactionCurrency', 'documentReferenceID', 'supplier'],
      currentPageNumber: 1,
      itemsPerPage: 25,
      activePage: 1,
      skipIndex: 0,
      defaultSortParams: 'documentDate%20desc',
      maxPages: 3,
      i18nkeysMap: {
        'invoiceStatusCode': 'status',
        'documentDate': 'invoiceDate',
        'planningDate': 'dueDate',
        'companyName': 'company',
        'companyCode': 'companyCode',
        'companyCountry': 'country',
        'amountInTransactionCurrency': 'amountIncludingTaxes',
        'withholdingTaxAmmount': 'withHoldingTax',
        'documentReferenceID': 'invoiceNo',
        'supplierName': 'supplier',
        'supplier': 'supplierCode',
        'purchasingDocuments': 'poNo'
      },
      statusMapping: {},
      customisableTableHeaders: [],
      hideColumns: [],
      activeFilterForm: 'invoiceDate',
      $activeFilterBtn: {}
    };
    const selector = {
      paymentApi: this.root.querySelector('.js-payment-api'),
      spinner: this.root.querySelector('.tp-spinner'),
      content: this.root.querySelector('.tp-payment-content'),
      config: this.root.querySelector('.js-all-payments__config'),
      searchResults: this.root.querySelector('.tp-all-payments__search-count'),
      table: this.root.querySelector('.tp-all-payments__table_wrapper'),
      $modal: this.roots.parent().find('.js-filter-modal'),
      allPaymentCustomizeTableAction: this.root.querySelector('.js-all-payments__customise-table-action'),
      headerAction: this.root.querySelector('.tp-all-payments__header-actions'),
      mobileHeadersActions: this.root.querySelector('.js-mobile-header-actions'),
      filterButton: this.root.querySelector('.js-apply-filter-button')
    };
    this.cache = {...config, ...selector};
    this.cache.tableData = [];
    this.cache.activeSortData = null;
    this.cache.i18nKeys = JSON.parse(this.cache.config.textContent);
    this.cache.statusApiUrl = this.root.getAttribute('data-status-api');
    this.cache.authData = {};
  }

  bindEvents() {
    const { table, i18nKeys, allPaymentCustomizeTableAction, headerAction, mobileHeadersActions } =  this.cache;
    const self = this;
    this.cache.customisableTableHeaders = [
      {key:'invoiceStatusCode',option:'status',optionDisplayText:i18nKeys['status'],isChecked:true,index:0},
      {key:'documentDate',option:'invoiceDate',optionDisplayText:i18nKeys['invoiceDate'],isChecked:true,index:1},
      {key:'dueCalculationBaseDate',option:'dueDate',optionDisplayText:i18nKeys['dueDate'],isChecked:true,index:2},
      {key:'companyName',option:'company',optionDisplayText:i18nKeys['company'],isChecked:true,index:3},
      {key:'companyCode',option:'companyCode',optionDisplayText:i18nKeys['companyCode'],isChecked:false,index:4},
      {key:'companyCountry',option:'country',optionDisplayText:i18nKeys['country'],isChecked:false,index:5},
      {key:'amountInTransactionCurrency',option:'amountIncludingTaxes',optionDisplayText:i18nKeys['amountIncludingTaxes'],isChecked:true,index:6},
      {key:'withholdingTaxAmmount',option:'withHoldingTax',optionDisplayText:i18nKeys['withHoldingTax'],isChecked:false,index:7},
      {key:'documentReferenceID',option:'invoiceNo',optionDisplayText:i18nKeys['invoiceNo'],isChecked:true,isDisabled:true,index:8},
      {key:'supplierName',option:'supplier',optionDisplayText:i18nKeys['supplier'],isChecked:false,index:9},
      {key:'supplier',option:'supplierCode',optionDisplayText:i18nKeys['supplierCode'],isChecked:false,index:10},
      {key:'purchasingDocuments',option:'poNo',optionDisplayText:i18nKeys['poNo'],isChecked:false,index:11}
    ];
    if(table) {
      table.addEventListener('click', function(e) {
        if (e.target.closest('.js-page-number') && !e.target.classList.contains('active')) {
          self.paginationAction(e.target);
        }
        else if(e.target.closest('.js-all-payments__table-summary__sort')) {
          self.sortAction(e.target);
        }
        else if(e.target.closest('.tp-all-payments__table-summary__body .tp-all-payments__table-summary__row')){
          const row = e.target.closest('.tp-all-payments__table-summary__row');
          const id = row.querySelector('[data-key="documentReferenceID"]').textContent.trim();
          const paymentDetailsURL = self.cache.paymentApi.getAttribute('data-payment-details-url');
          const url = `${paymentDetailsURL}?documentreferenceid=${id}`;
          window.open(url, '_blank');
        }
      });
    }
    allPaymentCustomizeTableAction.addEventListener('click', () => {
      const groupedFilterOptions = [];
      groupedFilterOptions.push({
        selectAllLabel: i18nKeys['selectAll'],
        options: self.cache.customisableTableHeaders,
        isChecked: !self.cache.customisableTableHeaders.some(filterOption => filterOption.isChecked === false)
      });
      self.renderFilterForm(groupedFilterOptions, { activeForm:'customise-table',header:i18nKeys['columns'],singleButton:true });
      headerAction.classList.remove('show');
    });
    this.roots.on('change', '.js-tp-all-payments-filter-group-checkbox', (e) => {
      const $currentTarget = $(e.target);
      const $currentTargetWrapper = $currentTarget.parents('.tp-all-payments__type-group-option');
      const $checkboxGroupInputs = $currentTargetWrapper.next().find('.tpatom-checkbox__input').not(':disabled');
      $checkboxGroupInputs.each((index, item) => {
        $(item).prop('checked', $currentTarget.is(':checked'));
      });

    });
    this.roots.on('change', '.tp-all-payments-group-filter-options .js-tp-all-payments-filter-checkbox', (e) => {
      const $currentTarget = $(e.target);
      const $thisGroupAllWrapper = $currentTarget.parents('.tp-all-payments-group-filter-options').prev();
      const $thisGroupAllCheckbox = $thisGroupAllWrapper.find('.js-tp-all-payments-filter-group-checkbox');
      if (!$currentTarget.is(':checked')) {
        $thisGroupAllCheckbox.prop('checked', false);
      }
    });

    this.roots.on('click', '.js-apply-filter-button',  () => {
      this.applyFilter();
    });
    mobileHeadersActions.addEventListener('click', () => {
      const isShow = headerAction.classList.contains('show');
      if (isShow) {
        headerAction.classList.remove('show');
      }
      else {
        headerAction.classList.add('show');
      }
    });
  }

  getCheckboxFilterData = () => {
    const $filtersCheckbox = this.roots.find('.js-tp-all-payments-filter-checkbox:not(.js-tp-all-payments-filter-group-checkbox)');
    const filterData = this.cache.customisableTableHeaders;
    if($filtersCheckbox){
      $filtersCheckbox.each(function(index) {
        if ($(this).is(':checked')) {
          filterData[index].isChecked = true;
        } else {
          filterData[index].isChecked = false;
        }
      });
    }
    const showHideFilterFieldList = [];
    if(filterData?.length > 0){
      filterData.forEach(item => {
        const {isChecked,key} = item;
        if(!isChecked){
          showHideFilterFieldList.push(key);
        }
      });
    }
    return { filterData, showHideFilterFieldList };
  }

  applyFilter = () => {
    const { activeFilterForm } = this.cache;
    // if show/hide columns
    if (activeFilterForm === 'customise-table') { // other type of filter change
      const tableData = {
        summary: this.getTableBodyData(),
        summaryHeadings: this.getHeaderData(),
        i18nKeys:this.cache.i18nKeys,
        meta:this.cache.meta
      };
      this.renderPaginationTableData(tableData);
      return;
    }
  }

  renderFilterForm = (data, formDetail, $filterBtn) => {
    const { i18nKeys } = this.cache;
    render.fn({
      template: 'filterForm',
      data: {
        header: formDetail.header,
        formData: data,
        isShowHideColumn: true,
        ...i18nKeys,
        singleButton: formDetail.singleButton === true ? true : false,
        customiseTable: formDetail.activeForm === 'customise-table' ? true : false,
        autoLocatorModal: `${formDetail.activeForm}Overlay`,
        autoLocatorInput: `${formDetail.activeForm}InputBox`,
        autoLocatorCheckbox: `${formDetail.activeForm}FilterCheckboxOverlay`,
        autoLocatorCheckboxText: `${formDetail.activeForm}FilterItemOverlay`
      },
      target: '.tp-all-payments__filter-form',
      hidden: false
    });
    this.cache.activeFilterForm = formDetail.activeForm;
    this.cache.$activeFilterBtn = $filterBtn;
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

  getFilterShowList = () => {
    let showFields = this.cache.showFields;
    const { showHideFilterFieldList } = this.getCheckboxFilterData();
    if(showHideFilterFieldList?.length > 0){
      showFields = showFields.filter( ( item ) => !showHideFilterFieldList.includes( item ) );
    }
    return showFields;
  }

  getTableBodyData = () => {
    const showFields = this.getFilterShowList();
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
    const showFields = this.getFilterShowList();
    return showFields.map(key => ({
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
    const paymentApi = this.cache.paymentApi.getAttribute('data-list-api'),
      dataRange = this.cache.paymentApi.getAttribute('data-date-range');
    const { itemsPerPage, skipIndex, activeSortData, defaultSortParams } = this.cache;

    // TODO: Need to remove this. For testing purpose we add this from date time.
    let apiUrlRequest = `${paymentApi}?skip=${skipIndex}&count=${itemsPerPage}${getFilterDateRange(dataRange)}`;
    //let apiUrlRequest = `${paymentApi}?skip=${skipIndex}&count=${itemsPerPage}`;

    if (activeSortData) {
      apiUrlRequest += `&sort=${activeSortData.sortedByKey} ${activeSortData.sortOrder}`;
    }
    else {
      apiUrlRequest += `&sort=${defaultSortParams}`;
    }

    return apiUrlRequest;
  }

  getStatusName = (statusCode) => {
    const data = this.cache.statusMapping;

    if (statusCode) {
      for (const key in data) {
        if (data[key].includes(statusCode)) {
          return key;
        }
      }
    }

    return '';
  };

  getApiPromise = (authData) => {
    const fetchHeaderOption = {
      method: 'GET',
      contentType: 'application/json',
      headers: {
        'Authorization': `Bearer ${authData.access_token}`,
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    };
    let statusApiPromise = this.cache.statusMapping;

    const paymentApiPromise = fetch(this.getPaymentApiUrl(), fetchHeaderOption).then(resp => resp.json());
    if (Object.keys(this.cache.statusMapping).length === 0) {
      statusApiPromise = fetch(this.cache.statusApiUrl).then(resp => resp.json());
    }

    return [paymentApiPromise, statusApiPromise];
  }

  renderPayment = () => {
    this.showLoader(true);

    auth.getToken(({ data: authData }) => {
      Promise.all(this.getApiPromise(authData))
        .then(response => {
          this.showLoader(false);
          this.cache.tableData = response[0].data;
          this.cache.statusMapping = response[1];

          // Show fields based on requirement ex. render the amount field with currency
          this.cache.tableData = this.cache.tableData.map((item) => ({
            ...item,
            withholdingTaxAmmount: (item.withholdingTaxAmmount) ? `${item.withholdingTaxAmmount  } ${  item.transactionCurrency}` : item.withholdingTaxAmmount,
            amountInTransactionCurrency: (item.amountInTransactionCurrency) ? `${item.amountInTransactionCurrency  } ${  item.transactionCurrency}`: item.amountInTransactionCurrency,
            purchasingDocuments: (item.purchasingDocuments.length > 1) ? getI18n(this.cache.i18nKeys['multiPoNo']):  item.purchasingDocuments,
            invoiceStatusCode: this.getStatusName(item.invoiceStatusCode)
          }));

          this.cache.meta = response[0].meta;
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
      this.cache.searchResults.textContent = `${this.cache.meta.total} ${getI18n(this.cache.i18nKeys['results'])}`;
    }
  }

  renderErrorTemplate = () => {
    render.fn({
      template: 'allpaymentsTable',
      data: { noDataMessage: true, noDataFound: getI18n(this.cache.i18nKeys['fetchError'])},
      target: '.tp-all-payments__table_wrapper',
      hidden: false
    });
  }

  renderPaginationTableData = (list) => {
    const paginationObj = _paginate(list.meta.total, this.cache.activePage, this.cache.itemsPerPage, this.cache.maxPages);

    if (list.summary.length === 0) {
      render.fn({
        template: 'allpaymentsTable',
        data: { noDataMessage: true, noDataFound: getI18n(this.cache.i18nKeys['noDataFound'])},
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
