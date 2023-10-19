const config = {
  showFields: [
    'invoiceStatusCode',
    'documentDate',
    'dueCalculationBaseDate',
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
    'dueCalculationBaseDate',
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
  }
};

const filterConfig = {
  queryParamMapping: {
    'invoiceDates': ['fromdatetime', 'todatetime'],
    'countries': ['companycountry'],
    'companyCodes': ['companycode'],
    'documentReferenceIDs': ['documentreferenceid']
  },
  filterModal: {
    'invoiceDates': [],
    'countries': [],
    'companyCodes': [],
    'documentReferenceIDs': []
  },
  keyMapping: {
    'documentReferenceIDs': 'documentReferenceID',
    'companyCodes': 'companyCode',
    'countries': 'country',
    'other': 'other',
    'last90Days': 'last90Days'
  }
};

export default {...config, ...filterConfig};
