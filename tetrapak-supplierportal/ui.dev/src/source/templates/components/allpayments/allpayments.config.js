const config = {
  showFields: [
    'invoiceStatusCode',
    'documentDate',
    'planningDate',
    'companyName',
    'companyCode',
    'companyCountry',
    'amountInTransactionCurrency',
    'withholdingTaxAmmount',
    'documentReferenceID',
    'supplierName',
    'supplier',
    'purchasingDocuments'
  ],
  sortableKeys: [
    'documentDate',
    'planningDate',
    'companyCode',
    'companyCountry',
    'amountInTransactionCurrency',
    'documentReferenceID',
    'supplier'
  ],
  defaultSortParams: 'documentDate%20desc',
  currentPageNumber: 1,
  itemsPerPage: 10,
  activePage: 1,
  skipIndex: 0,
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
  toolTipkeysMap: {
    'invoiceStatusCode': 'statusTooltip',
    'documentDate': 'invoiceDateTooltip',
    'planningDate': 'dueDateTooltip',
    'companyName': 'companyTooltip',
    'companyCode': 'companyCodeTooltip',
    'companyCountry': 'countryTooltip',
    'amountInTransactionCurrency': 'amountIncludingTaxesTooltip',
    'withholdingTaxAmmount': 'withHoldingTaxTooltip',
    'documentReferenceID': 'invoiceNoTooltip',
    'supplierName': 'supplierTooltip',
    'supplier': 'supplierCodeTooltip',
    'purchasingDocuments': 'poNoTooltip'
  }
};

const filterConfig = {
  queryParamMapping: {
    'invoiceDates': ['fromdatetime', 'todatetime'],
    'countries': ['companycountry'],
    'companyCodes': ['companycode'],
    'documentReferenceIDs': ['documentreferenceid'],
    'invoiceStatuses': ['status']
  },
  filterModal: {
    'invoiceDates': [],
    'invoiceStatuses':[],
    'countries': [],
    'companyCodes': [],
    'documentReferenceIDs': []
  },
  filterModalDisableItemsKeys: [
    'companyCodes', 'countries'
  ],
  keyMapping: {
    'documentReferenceIDs': 'documentReferenceID',
    'invoiceStatuses': 'status',
    'companyCodes': 'companyCode',
    'countries': 'country',
    'other': 'other',
    'last90Days': 'last90Days',
    'posted': 'confirmed',
    'inprocess': 'other'
  }
};

export default {...config, ...filterConfig};
