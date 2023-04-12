const config = {
  apiSelector: '.js-rk-api',
  configSelector: '.js-rk__config',
  contentSelector: '.tp-filtered-table',
  listApiKey: 'rklist-api',
  countryApiKey: 'country-api',
  detailsApiKey: 'rkdetail-page',
  downloadServletUrlId: '#downloadCsvServletUrl',
  tableConfig: [
    {
      key: 'countryCode',
      displayKey: 'countryName',
      i18nKey: 'country',
      i18nTooltipKey: 'countryToolTip',
      showColumnDisabled: false,
      showColumnByDefault: false,
      showFilterByDefault: true,
      isCountry: true,
      filterType: 'radio',
      apiKey: 'countries',
      queryParam: 'countrycodes'
    },
    {
      key: 'lineCode',
      i18nKey: 'functionalLocation',
      i18nKeyTooltip: 'functionalLocationToolTip',
      showColumnDisabled: false,
      showColumnByDefault: true,
      showFilterByDefault: true,
      filterType: 'checkbox',
      apiKey: 'lines',
      queryParam: 'linecode'
    },
    {
      key: 'equipmentDesc',
      i18nKey: 'equipmentDescription',
      i18nKeyTooltip: 'equipmentDescriptionToolTip',
      showColumnDisabled: false,
      showColumnByDefault: true,
      showFilterByDefault: true,
      filterType: 'text',
      queryParam: 'equipmentdesc'
    },
    {
      key: 'machineSystem',
      i18nKey: 'machineSystem',
      i18nKeyTooltip: 'machineSystemToolTip',
      showColumnDisabled: false,
      showColumnByDefault: false,
      showFilterByDefault: false,
      filterType: 'checkbox',
      apiKey: 'machinesystems',
      queryParam: 'machinesystem'
    },
    {
      key: 'serialNumber',
      i18nKey: 'serialNumber',
      i18nKeyTooltip: 'serialNumberToolTip',
      showColumnDisabled: true,
      showColumnByDefault: true,
      showFilterByDefault: true,
      filterType: 'text',
      queryParam: 'serialnumber'
    },
    {
      key: 'equipmentStatus',
      i18nKey: 'equipmentStatus',
      i18nKeyTooltip: 'equipmentStatusToolTip',
      showColumnDisabled: false,
      showColumnByDefault: true,
      showFilterByDefault: false,
      filterType: 'checkbox',
      apiKey: 'equipmentstatuses',
      queryparam: 'equipmentstatus'
    },
    {
      key: 'rkNumber',
      i18nKey: 'rkNumber',
      i18nKeyTooltip: 'rkNumberToolTip',
      showColumnDisabled: true,
      showColumnByDefault: true,
      showFilterByDefault: true,
      filterType: 'text',
      queryParam: 'rknumber'
    },
    {
      key: 'rkDesc',
      i18nKey: 'rkDesc',
      i18nKeyTooltip: 'rkDescToolTip',
      showColumnDisabled: false,
      showColumnByDefault: false,
      showFilterByDefault: false,
      filterType: 'text',
      queryParam: 'rkdesc'
    },
    {
      key: 'implStatus',
      i18nKey: 'implStatus',
      i18nKeyTooltip: 'implStatusToolTip',
      showColumnDisabled: false,
      showColumnByDefault: true,
      showFilterByDefault: true,
      filterType: 'checkbox',
      apiKey: 'implstatuses',
      queryparam: 'implstatus'
    },
    {
      key: 'implDate',
      i18nKey: 'implDate',
      i18nKeyTooltip: 'implDateToolTip',
      showColumnDisabled: false,
      showColumnByDefault: true,
      showFilterByDefault: false,
      hiddenByDefault: true
    },
    {
      key: 'implStatusDate',
      i18nKey: 'implStatusDate',
      i18nKeyTooltip: 'implStatusDateToolTip',
      showColumnDisabled: false,
      showColumnByDefault: false,
      showFilterByDefault: false
    },
    {
      key: 'rkGeneralNumber',
      i18nKey: 'generalRkNumber',
      i18nKeyTooltip: 'generalRkNumberToolTip',
      showColumnDisabled: false,
      showColumnByDefault: true,
      showFilterByDefault: false,
      filterType: 'text',
      queryParam: 'rkgeneralnumber'
    },
    {
      key: 'rkTypeCode',
      i18nKey: 'rkType',
      i18nKeyTooltip: 'rkTypeToolTip',
      showColumnDisabled: false,
      showColumnByDefault: true,
      showFilterByDefault: true,
      filterType: 'checkbox',
      apiKey: 'rktypes',
      queryparam: 'rktypecode'
    },
    {
      key: 'releaseDate',
      i18nKey: 'releaseDate',
      i18nKeyTooltip: 'releaseDateToolTip',
      showColumnDisabled: false,
      showColumnByDefault: false,
      showFilterByDefault: false,
      filterType: 'date-range',
      queryParam: ['releasedatestart', 'releasedateend']
    },
    {
      key: 'plannedDate',
      i18nKey: 'plannedDate',
      i18nKeyTooltip: 'releaseDateToolTip',
      showColumnDisabled: false,
      showColumnByDefault: false,
      showFilterByDefault: false,
      filterType: 'date-range',
      queryParam: ['planneddatestart', 'planneddateend']
    },
    {
      key: 'implDeadline',
      i18nKey: 'implDeadline',
      i18nKeyTooltip: 'implDeadlineToolTip',
      showColumnDisabled: false,
      showColumnByDefault: false,
      showFilterByDefault: false,
      filterType: 'date-range',
      queryParam: ['impldeadlinestart', 'impldeadlineend']
    },
    {
      key: 'rebuildingKitStatus',
      i18nKey: 'rkStatus',
      i18nKeyTooltip: 'rkStatusToolTip',
      showColumnDisabled: false,
      showColumnByDefault: true,
      showFilterByDefault: true,
      filterType: 'checkbox',
      apiKey: 'rkstatuses',
      queryParam: 'rkstatus'
    },
    {
      key: 'rkHandling',
      i18nKey: 'rkHandling',
      i18nKeyTooltip: 'rkHandlingToolTip',
      showColumnDisabled: false,
      showColumnByDefault: false,
      showFilterByDefault: true,
      filterType: 'checkbox',
      apiKey: 'rkhandlings',
      queryParam: 'rkhandling'
    },
    {
      key: 'order',
      i18nKey: 'order',
      i18nKeyTooltip: 'orderToolTip',
      showColumnDisabled: false,
      showColumnByDefault: false,
      showFilterByDefault: false
    }
  ]
};

export default config;