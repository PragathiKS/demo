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
      queryParam: 'countrycodes',
      hiddenByDefault: false
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
      queryParam: 'linecode',
      hiddenByDefault: false
    },
    {
      key: 'equipmentDesc',
      i18nKey: 'equipmentDescription',
      i18nKeyTooltip: 'equipmentDescriptionToolTip',
      showColumnDisabled: false,
      showColumnByDefault: true,
      showFilterByDefault: true,
      filterType: 'text',
      queryParam: 'equipmentdesc',
      hiddenByDefault: false
    },
    {
      key: 'serialNumber',
      i18nKey: 'serialNumber',
      i18nKeyTooltip: 'serialNumberToolTip',
      showColumnDisabled: true,
      showColumnByDefault: true,
      showFilterByDefault: true,
      filterType: 'text',
      queryParam: 'serialnumber',
      hiddenByDefault: false
    },
    {
      key: 'rkNumber',
      i18nKey: 'rkNumber',
      i18nKeyTooltip: 'rkNumberToolTip',
      showColumnDisabled: true,
      showColumnByDefault: true,
      showFilterByDefault: true,
      filterType: 'text',
      queryParam: 'rknumber',
      hiddenByDefault: false
    },
    {
      key: 'rkDesc',
      i18nKey: 'rkDesc',
      i18nKeyTooltip: 'rkDescToolTip',
      showColumnDisabled: false,
      showColumnByDefault: true,
      showFilterByDefault: true,
      filterType: 'text',
      queryParam: 'rkdesc',
      hiddenByDefault: false
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
      queryParam: 'implstatus',
      hiddenByDefault: false
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
      queryParam: 'machinesystem',
      hiddenByDefault: true
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
      queryParam: 'equipmentstatus',
      hiddenByDefault: true
    },
    {
      key: 'rkTypeCode',
      i18nKey: 'rkType',
      i18nKeyTooltip: 'rkTypeToolTip',
      showColumnDisabled: false,
      showColumnByDefault: false,
      showFilterByDefault: true,
      filterType: 'checkbox',
      apiKey: 'rktypes',
      queryParam: 'rktypecode',
      hiddenByDefault: false
    },
    {
      key: 'rebuildingKitStatus',
      i18nKey: 'rkStatus',
      i18nKeyTooltip: 'rkStatusToolTip',
      showColumnDisabled: false,
      showColumnByDefault: false,
      showFilterByDefault: true,
      filterType: 'checkbox',
      apiKey: 'rkstatuses',
      queryParam: 'rkstatus',
      hiddenByDefault: false
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
      queryParam: 'rkhandling',
      hiddenByDefault: false
    },
    {
      key: 'implDate',
      i18nKey: 'implDate',
      i18nKeyTooltip: 'implDateToolTip',
      showColumnDisabled: false,
      showColumnByDefault: false,
      showFilterByDefault: false,
      hiddenByDefault: true
    },
    {
      key: 'implStatusDate',
      i18nKey: 'implStatusDate',
      i18nKeyTooltip: 'implStatusDateToolTip',
      showColumnDisabled: false,
      showColumnByDefault: false,
      showFilterByDefault: false,
      hiddenByDefault: true
    },
    {
      key: 'implDeadline',
      i18nKey: 'implDeadline',
      i18nKeyTooltip: 'implDeadlineToolTip',
      showColumnDisabled: false,
      showColumnByDefault: false,
      showFilterByDefault: false,
      filterType: 'date-range',
      queryParam: ['impldeadlinestart', 'impldeadlineend'],
      hiddenByDefault: true
    },
    {
      key: 'plannedDate',
      i18nKey: 'plannedDate',
      i18nKeyTooltip: 'releaseDateToolTip',
      showColumnDisabled: false,
      showColumnByDefault: false,
      showFilterByDefault: false,
      filterType: 'date-range',
      queryParam: ['planneddatestart', 'planneddateend'],
      hiddenByDefault: true
    },
    {
      key: 'releaseDate',
      i18nKey: 'releaseDate',
      i18nKeyTooltip: 'releaseDateToolTip',
      showColumnDisabled: false,
      showColumnByDefault: false,
      showFilterByDefault: false,
      filterType: 'date-range',
      queryParam: ['releasedatestart', 'releasedateend'],
      hiddenByDefault: true
    },
    {
      key: 'rkGeneralNumber',
      i18nKey: 'generalRkNumber',
      i18nKeyTooltip: 'generalRkNumberToolTip',
      showColumnDisabled: false,
      showColumnByDefault: false,
      showFilterByDefault: false,
      filterType: 'text',
      queryParam: 'rkgeneralnumber',
      hiddenByDefault: true
    },
    {
      key: 'order',
      i18nKey: 'order',
      i18nKeyTooltip: 'orderToolTip',
      showColumnDisabled: false,
      showColumnByDefault: false,
      showFilterByDefault: false,
      hiddenByDefault: true
    }
  ]
};
export default config;