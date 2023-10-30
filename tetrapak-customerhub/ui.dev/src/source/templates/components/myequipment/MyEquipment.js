import $ from 'jquery';
import 'bootstrap';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { getI18n } from '../../../scripts/common/common';
import { render } from '../../../scripts/utils/render';
import auth from '../../../scripts/utils/auth';
import  filters  from '../../../scripts/utils/filters';
import { ajaxMethods,EQ_TYPE,EQ_FILTERS } from '../../../scripts/utils/constants';
import {  _addFilterAnalytics, _removeFilterAnalytics, _paginationAnalytics, _customizeTableBtnAnalytics, _addShowHideFilterAnalytics, _removeAllFiltersAnalytics, _trackEquipmentLinkClick } from './MyEquipment.analytics';
import file from '../../../scripts/utils/file';
import { _paginate } from './MyEquipment.paginate';
import { _remapFilterProperty, _buildQueryUrl, _getFormattedCountryData, _remapFilterOptionKey } from './MyEquipment.utils';
import { _buildTableRows, _groupByBusinessType, _mapHeadings } from './MyEquipment.table';

function _processKeys(keys, ob) {
  if(keys.length){
    return keys;
  } else {
    let country,equipmentName,site,serialNumber,equipmentStatusDesc,functionalLocation,siteDescription,location;
    for(const i in ob){
      if(i === 'countryCode'){
        country = i;
      }else if(i === 'site') {
        site = i;
      }
      else if(i === 'equipmentName'){
        equipmentName = i;
      }
      else if(i === 'siteDesc'){
        siteDescription = i;
      }
      else if(i === 'location'){
        location = i;
      }
      else if(i === 'serialNumber'){
        serialNumber = i;
      }
      else if(i === 'equipmentStatusDesc'){
        equipmentStatusDesc = i;
      }
      else if(i === 'functionalLocation'){
        functionalLocation = i;
      }
    }
    return [country, site, siteDescription, functionalLocation, equipmentName, serialNumber, equipmentStatusDesc, location];
  }
}

function _processTableData(data){
  let keys = [];
  const { activeSortData, sortableKeys } = this.cache;
  if (Array.isArray(data.summary)) {
    data.summary = data.summary.map(summary => {
      keys = _processKeys(keys, summary);
      this.cache.tableHeaders = keys;
      return _buildTableRows.call(this, summary, keys);
    });
    data.summaryHeadings = _mapHeadings(keys,data.i18nKeys,activeSortData,sortableKeys);
  }
  return data;
}

export function _limitFilterSelection($modal) {
  const $countWrapper = $modal.find('.js-tp-my-equipment__filter-count');
  const maxItems = parseInt($countWrapper.data('max-filters'), 10);
  const $currentCountTxt = $modal.find('.js-tp-my-equipment__filter-count-current');
  const $currentCountError = $modal.find('.js-tp-my-equipment__filter-count-err');
  const checkedItemsNo = $modal.find('.js-tp-my-equipment-filter-checkbox:not(.js-tp-my-equipment-filter-group-checkbox)').filter(':checked').length;
  const $applyBtn = $modal.find('.js-apply-filter-button');

  if (!maxItems) {
    return;
  }

  if (checkedItemsNo <= maxItems) {
    $currentCountTxt.text(checkedItemsNo);
    $currentCountError.attr('hidden', 'hidden');
    $countWrapper.removeAttr('hidden');
    $applyBtn.removeAttr('disabled');
  } else {
    $countWrapper.attr('hidden', 'hidden');
    $currentCountError.removeAttr('hidden');
    $applyBtn.attr('disabled', 'disabled');
  }
}

class MyEquipment {
  constructor({ el }) {
    this.root = $(el);
  }

  cache = {};

  initCache() {
    this.cache.$modal = this.root.parent().find('.js-filter-modal');
    this.cache.$countryFilterLabel = this.root.find('.tp-my-equipment__country-button-filter');
    this.cache.$customerFilterLabel = this.root.find('.tp-my-equipment__customer-button-filter');
    this.cache.$lineFilterLabel = this.root.find('.tp-my-equipment__line-button-filter');
    this.cache.$statusFilterLabel = this.root.find('.tp-my-equipment__status-button-filter');
    this.cache.$serialNumberFilterLabel = this.root.find('.tp-my-equipment__serial-button-filter');
    this.cache.$equipmentDescFilterLabel = this.root.find('.tp-my-equipment__equip-desc-button-filter');
    this.cache.$equipmentTypeFilterLabel = this.root.find('.tp-my-equipment__equip-type-button-filter');
    this.cache.$functionalLocFilterLabel = this.root.find('.tp-my-equipment__functional-loc-button-filter');
    this.cache.$searchResults = this.root.find('.tp-my-equipment__search-count');
    this.cache.$myEquipmentCustomizeTableAction = this.root.find('.js-my-equipment__customise-table-action');
    this.cache.$mobileHeadersActions = this.root.find('.js-mobile-header-actions');
    this.cache.$removeAllFiltersBtn = this.root.find('.js-tp-my-equipment__remove-all-button');
    this.cache.configJson = this.root.find('.js-my-equipment__config').text();
    this.cache.$spinner = this.root.find('.tp-spinner');
    this.cache.$content = this.root.find('.tp-equipment-content');
    this.cache.$pagination = this.root.find('.js-tbl-pagination');
    this.cache.equipmentApi = this.root.find('.js-equipment-api');
    this.cache.countryData = [];
    this.cache.activeFilterForm = 'country';
    this.cache.$activeFilterBtn = {};
    this.cache.tableData = [];
    this.cache.customisableTableHeaders = [];
    this.cache.tableHeaders = [];
    this.cache.hideColumns = []; // column index;
    this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    this.cache.currentPageNumber = 1;
    this.cache.filterModalData = {};
    this.cache.combinedFiltersObj = {};
    this.cache.equipmentListFiltersObj={};
    this.cache.sortableKeys = ['site','lineCode','equipmentStatusDesc','serialNumber','functionalLocation','siteDesc','location','equipmentName'];
    this.cache.activeSortData = null;
    this.cache.activePage = 1;
    this.cache.skipIndex = 0;
    this.cache.itemsPerPage = 25;
    this.cache.authData = {};
    // holds all possible filter values for the modals, WITHOUT any active filters
    // populated at initial page load
    this.cache.allApiFilterValsObj = {
      'statuses': [],
      'types': [],
      'customers': [],
      'lines': []
    };
    // holds all possible filter values for the modals, WITH active filters configured
    // populated every time a filter is set
    this.cache.currentApiFilterValsObj = {
      'statuses': [],
      'types': [],
      'customers': [],
      'lines': []
    };
    this.cache.downloadservletUrl = this.root.find('#downloadExcelServletUrl').val();
    this.cache.defaultSortParams = 'functionallocation%20asc,position%20asc,serialnumber%20asc';
  }

  bindEvents() {
    const {$mobileHeadersActions, $modal,i18nKeys,$myEquipmentCustomizeTableAction } = this.cache;
    this.cache.customisableTableHeaders = [
      {key:'countryCode',option:'countryCode',optionDisplayText:i18nKeys['country'],isChecked:true,index:0},
      {key:'site',option:'site',optionDisplayText:i18nKeys['site'],isChecked:true,index:1},
      {key:'siteDesc',option:'siteDesc',optionDisplayText:i18nKeys['siteDescription'],isChecked:false,index:2},
      {key:'functionalLocation',option:'functionalLocation',optionDisplayText:i18nKeys['functionalLocation'],isChecked:true,index:3},
      {key:'equipmentStatusDesc',option:'equipmentStatusDesc',optionDisplayText:i18nKeys['equipmentStatus'],isChecked:true,index:6},
      {key:'location',option:'location',optionDisplayText:i18nKeys['location'],isChecked:false,index:7}
    ];

    this.cache.$countryFilterLabel.on('click', () => {
      const formDetail = { activeForm:'country',header:i18nKeys['country'], singleButton: true, isRadio: true, radioGroupName: 'countryRadio' };
      this.renderFilterForm(this.cache.countryData, formDetail, this.cache.$countryFilterLabel);
      $modal.modal();
    });

    this.cache.$customerFilterLabel.on('click', () => {
      const formDetail = {activeForm:'customer',header:i18nKeys['customer'],maxFiltersSelection:10};
      this.cache.filterModalData['customer'] = this.getFilterModalData('customer');
      this.renderFilterForm(this.cache.filterModalData['customer'], formDetail, this.cache.$customerFilterLabel);
      $modal.modal();
    });

    this.cache.$lineFilterLabel.on('click', () => {
      const formDetail = {activeForm:'lineCode',header:i18nKeys['line'],maxFiltersSelection:10};
      this.cache.filterModalData['lineCode'] = this.getFilterModalData('lineCode');
      this.renderFilterForm(this.cache.filterModalData['lineCode'], formDetail, this.cache.$lineFilterLabel);
      $modal.modal();
    });

    this.cache.$statusFilterLabel.on('click', () => {
      const formDetail = {activeForm:'equipmentStatusDesc',header:i18nKeys['equipmentStatus'],maxFiltersSelection:10};
      this.cache.filterModalData['equipmentStatusDesc'] = this.getFilterModalData('equipmentStatusDesc');
      this.renderFilterForm(this.cache.filterModalData['equipmentStatusDesc'], formDetail, this.cache.$statusFilterLabel);
      $modal.modal();
    });

    this.cache.$equipmentDescFilterLabel.on('click', () => {
      const formDetail = {activeForm:'equipmentName', header:i18nKeys['equipmentDescription'], isTextInput: true};
      const activeEquipTypeDesc = this.cache.combinedFiltersObj['equipmentName'] ? this.cache.combinedFiltersObj['equipmentName'] : '';
      this.renderFilterForm(activeEquipTypeDesc, formDetail, this.cache.$equipmentDescFilterLabel);
      $modal.modal();
    });

    this.cache.$equipmentTypeFilterLabel.on('click', () => {
      const formDetail = {activeForm:'equipmentType',header:i18nKeys['equipmentType'],maxFiltersSelection:20};
      this.cache.filterModalData['equipmentType'] = this.getFilterModalData('equipmentType');
      this.renderFilterForm(this.cache.filterModalData['equipmentType'], formDetail, this.cache.$equipmentTypeFilterLabel);
      $modal.modal();
    });

    this.cache.$serialNumberFilterLabel.on('click', () => {
      const formDetail = {activeForm:'serialNumber',header:i18nKeys['serialNumber'], isTextInput: true};
      const activeSerialNum = this.cache.combinedFiltersObj['serialNumber'] ? this.cache.combinedFiltersObj['serialNumber'] : '';
      this.renderFilterForm(activeSerialNum, formDetail, this.cache.$serialNumberFilterLabel);
      $modal.modal();
    });

    $myEquipmentCustomizeTableAction.on('click', () => {
      this.renderFilterForm(this.cache.customisableTableHeaders, { activeForm:'customise-table',header:i18nKeys['customizeTable'],singleButton:true });
      $('.tp-my-equipment__header-actions').removeClass('show');
      $modal.modal();
      _customizeTableBtnAnalytics($myEquipmentCustomizeTableAction);
    });

    $mobileHeadersActions.on('click', () => {
      if($('.tp-my-equipment__header-actions').hasClass('show')){
        $('.tp-my-equipment__header-actions').removeClass('show');
      } else {
        $('.tp-my-equipment__header-actions').addClass('show');
      }
    });

    $('body').on('click', (e) => {
      const $actionBtn = e.target;
      if(!$($actionBtn).hasClass('icon-Three_Dot')){
        if($('.tp-my-equipment__header-actions').hasClass('show')){
          $('.tp-my-equipment__header-actions').removeClass('show');
        }
      }
    });

    this.root.on('change', '.country-equipment-list',  function() {
      $('input[type="checkbox"]').not(this).prop('checked', false);
    });

    this.root.on('click', '.js-close-btn',  () => {
      $modal.modal('hide');
    });

    this.root.on('click', '.js-apply-filter-button',  () => {
      this.applyFilter();
    });

    this.root.on('keydown', '.js-tp-my-equipment-filter-input',  (e) => {
      const currentKeyCode = e.keyCode || e.which;
      if (currentKeyCode === 13) {
        this.applyFilter();
      }
    });

    this.root.on('click', '.js-tp-my-equipment__remove-button',  () => {
      this.applyFilter({removeFilter:true});
    });

    this.root.on('click', '.js-my-equipment__export-excel-action',  () => {
      this.downloadExcel();
    });

    this.cache.$removeAllFiltersBtn.on('click', () => {
      this.deleteAllFilters();
      this.toggleRemoveAllFilters(false);
      this.getAllAvailableFilterVals(['statuses', 'types', 'lines', 'customers'], true);
    });

    this.root.on('click', '.js-my-equipment__table-summary__sort',  (e) => {
      const $tHeadBtn = $(e.currentTarget).parent();
      this.sortTableByKey($tHeadBtn);
    });
    this.root.on('click', '.js-my-equipment__table-summary__row',  (e) => {
      const id = $(e.currentTarget).attr('href');
      const equipmentDetailsUrl = this.cache.equipmentApi.data('equip-details-url');
      const url = `${equipmentDetailsUrl}?id=${id}`;
      const $linkName = `${$(e.currentTarget).find('td').eq(4).text().trim()  }-${  id}`;

      filters.setFilterChipInLocalStorage(EQ_TYPE,this.cache.combinedFiltersObj,this.getActiveCountryCode());

      _trackEquipmentLinkClick($linkName);
      window.open(url, '_self');

    });

    $modal.on('shown.bs.modal', () => {
      const $freeTextSearchInput = $modal.find('.js-tp-my-equipment-filter-input');
      if ($freeTextSearchInput.length) {
        $freeTextSearchInput.focus();
      }
    });

    // functionality to check/uncheck all inputs in a specific category
    // Equipment Type filter - All Packaging , All processing
    this.root.on('change', '.js-tp-my-equipment-filter-group-checkbox', (e) => {
      const $currentTarget = $(e.target);
      const $modal = $currentTarget.parents('.tp-my-equipment__modal-content');
      const $currentTargetWrapper = $currentTarget.parents('.tp-my-equipment__type-group-option');
      const $checkboxGroupInputs = $currentTargetWrapper.next().find('.tpatom-checkbox__input').not(':disabled');
      $checkboxGroupInputs.each((index, item) => {
        $(item).prop('checked', $currentTarget.is(':checked'));
      });
      _limitFilterSelection($modal);
    });

    this.root.on('change', '.tp-my-equipment-group-filter-options .js-tp-my-equipment-filter-checkbox', (e) => {
      const $currentTarget = $(e.target);
      const $thisGroupAllWrapper = $currentTarget.parents('.tp-my-equipment-group-filter-options').prev();
      const $thisGroupAllCheckbox = $thisGroupAllWrapper.find('.js-tp-my-equipment-filter-group-checkbox');
      if (!$currentTarget.is(':checked')) {
        $thisGroupAllCheckbox.prop('checked', false);
      }
    });

    // Limit selection of checkbox filters to a max number
    this.root.on('change', '.tp-my-equipment-filter-options .js-tp-my-equipment-filter-checkbox', (e) => {
      const $currentTarget = $(e.target);
      const $modal = $currentTarget.parents('.tp-my-equipment__modal-content');
      _limitFilterSelection($modal);
    });

    this.root.on('click', '.js-page-number',  (e) => {
      const $btn = $(e.currentTarget);
      const $pagination = $btn.parents('.js-tbl-pagination');

      if (!$btn.hasClass('active')) {
        // stop pointer events until new page is rendered, in order to not send multiple AJAX calls
        $pagination.addClass('pagination-lock');
        this.cache.activePage = $btn.data('page-number');
        this.cache.skipIndex = $btn.data('skip');
        this.renderNewPage({'resetSkip': false});
        _paginationAnalytics($btn);
      }
    });
  }

  mapTableColumn = () => {
    const { tableHeaders,customisableTableHeaders } = this.cache;
    for(let i=0;i<tableHeaders.length;i++) {
      if(customisableTableHeaders[i]){
        customisableTableHeaders[i] = {
          ...customisableTableHeaders[i],
          colIndex:i
        };
      }
    }
  }

  downloadExcel = () => {
    auth.getToken(() => {
      const url = this.cache.downloadservletUrl;
      file.get({
        extension: 'csv',
        url: `${url}?countrycodes=${this.getActiveCountryCode()}`,
        method: ajaxMethods.GET
      });
    });
  }

  getActiveCountryCode = () => {
    const { countryData } = this.cache;
    const activeCountry = countryData.filter(e => e.isChecked);
    if (activeCountry.length > 0) {
      return activeCountry[0].countryCode;
    }
  //  return 'DE';//activeCountry[0].countryCode;
  }

  getAllAvailableFilterVals(filterValuesArr, newCountry, appliedFilter) {
    const equipmentApi = this.cache.equipmentApi.data('list-api');
    const { combinedFiltersObj } = this.cache;

    filterValuesArr.forEach(filterVal => {
      const appliedFilterApiKey = _remapFilterProperty(appliedFilter);

      let apiUrlRequest = `${equipmentApi}/${filterVal}?countrycodes=${this.getActiveCountryCode()}`;

      if (!newCountry) {
        apiUrlRequest += `&${_buildQueryUrl(combinedFiltersObj, filterVal)}`;
      }

      // for lines and customers, pass custom max count value
      if (filterVal === 'customers') {
        apiUrlRequest += '&count=1500';
      }

      if (filterVal === 'lines') {
        apiUrlRequest += '&count=7000';
      }
      auth.getToken(({ data: authData }) => {
        ajaxWrapper
          .getXhrObj({
            url: apiUrlRequest,
            method: 'GET',
            contentType: 'application/json',
            dataType: 'json',
            beforeSend(jqXHR) {
              jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
              jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            },
            showLoader: true
          }).then(res => {
            if (newCountry) {
              this.cache.allApiFilterValsObj[filterVal] = res.data;
            } else {
              this.cache.currentApiFilterValsObj[filterVal] = res.data;
              if (appliedFilterApiKey !== filterVal) {
                this.checkActiveFilterSets(filterVal, res.data);
              }
            }
          });
      });
    });
  }

  renderSearchCount = () => {
    if (this.cache.meta) {
      this.cache.$searchResults.text(`${this.cache.meta.total} ${getI18n(this.cache.i18nKeys['searchResults'])}`);
    }
  }

  renderPaginationTableData = (list) => {
    if (list.meta) {
      const paginationObj = _paginate(list.meta.total, this.cache.activePage, this.cache.itemsPerPage, 3);

      if (list.summary.length === 0) {
        render.fn({
          template: 'myEquipmentTable',
          data: { noDataMessage: true, noDataFound: this.cache.i18nKeys['noDataFound']  },
          target: '.tp-my-equipment__table_wrapper',
          hidden: false
        });
      }
      else {
        render.fn({
          template: 'myEquipmentTable',
          data: {...list, summary: list.summary, paginationObj: paginationObj },
          target: '.tp-my-equipment__table_wrapper',
          hidden: false
        },() => {
          this.hideShowColums();
          $(function () {
            $('[data-toggle="tooltip"]').tooltip();
          });
        });
      }
    }
  }

  hideShowColums = () => {
    const { customisableTableHeaders } = this.cache;
    for(const i in customisableTableHeaders){
      if(!customisableTableHeaders[i].isChecked){
        $(`.js-my-equipment__table-summary__cellheading--${customisableTableHeaders[i].index}`).addClass('hide');
        $(`.js-my-equipment__table-summary__cell--${customisableTableHeaders[i].index}`).addClass('hide');
      } else {
        $(`.js-my-equipment__table-summary__cellheading--${customisableTableHeaders[i].index}`).removeClass('hide');
        $(`.js-my-equipment__table-summary__cell--${customisableTableHeaders[i].index}`).removeClass('hide');
      }
    }
  }

  renderNewCountry = (label, analyticsAction) => {
    const { $countryFilterLabel, itemsPerPage } = this.cache;
    const equipmentApi = this.cache.equipmentApi.data('list-api');
    this.cache.$spinner.removeClass('d-none');
    this.cache.$content.addClass('d-none');

    auth.getToken(({ data: authData }) => {
      this.getAllAvailableFilterVals(['statuses', 'types', 'lines', 'customers'], true);
      ajaxWrapper
        .getXhrObj({
          url: `${equipmentApi}?skip=0&count=${itemsPerPage}&countrycodes=${this.getActiveCountryCode()}`,
          method: 'GET',
          contentType: 'application/json',
          dataType: 'json',
          beforeSend(jqXHR) {
            jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
            jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
          },
          showLoader: true
        }).then(response => {
          this.cache.$spinner.addClass('d-none');
          this.cache.$content.removeClass('d-none');
          this.cache.tableData = response.data;
          this.cache.meta = response.meta;
          this.cache.activePage = 1;
          this.cache.tableData = this.cache.tableData.map((item) => ({
            ...item,
            equipmentStatus: item.equipmentStatus || ''
          }));
          this.deleteAllFilters();
          const tableData = _processTableData.call(this, {summary:this.cache.tableData,i18nKeys:this.cache.i18nKeys,meta:this.cache.meta});
          this.renderPaginationTableData(tableData);
          this.renderSearchCount();
          this.updateFilterCountValue(label,1,$countryFilterLabel);

          if (analyticsAction && analyticsAction.action === 'addedFilter' && response.meta) {
            _addFilterAnalytics(analyticsAction.targetFilter, response.meta.total, analyticsAction.items);
          }
        }).fail(() => {
          this.cache.$spinner.addClass('d-none');
          this.cache.$content.removeClass('d-none');
        });
    });
  }

  getFilterModalData(filterByProperty) {
    const { combinedFiltersObj, allApiFilterValsObj, currentApiFilterValsObj } = this.cache;
    let alphabeticalSortKey;
    let optionDisplayTextKey;
    let optionValueKey;
    const filterOptionsArr = [];
    let filterOptionsDatasource = [];
    const allAvailableApiFilterCheckboxes = [...allApiFilterValsObj[_remapFilterProperty(filterByProperty)]];
    const currentSelectionApiFilterCheckboxes = [...currentApiFilterValsObj[_remapFilterProperty(filterByProperty)]];

    // customer uses 'customerNumber' for filtering (as values), but should display and sort by 'customer' in table
    switch (filterByProperty) {
      case 'customer':
        optionDisplayTextKey = 'customer';
        optionValueKey = 'customerNumber';
        alphabeticalSortKey = 'optionDisplayText';
        break;
      case 'equipmentStatusDesc':
        optionDisplayTextKey = 'equipmentStatusDesc';
        optionValueKey = 'equipmentStatus';
        alphabeticalSortKey = 'optionDisplayText';
        break;
      case 'equipmentType':
        optionDisplayTextKey = 'equipmentTypeDesc';
        optionValueKey = 'equipmentType';
        alphabeticalSortKey = 'optionDisplayText';
        break;
      default:
        optionDisplayTextKey = filterByProperty;
        optionValueKey = filterByProperty;
        alphabeticalSortKey = 'option';
    }

    // if only single filter is used, or no filters are set -> display all possible filter values
    if (Object.keys(combinedFiltersObj).length === 0) {
      filterOptionsDatasource = allAvailableApiFilterCheckboxes;
    } else {
      // if multiple filters are set, only display available filter options
      filterOptionsDatasource = currentSelectionApiFilterCheckboxes;
    }

    filterOptionsDatasource.forEach((row) => {
      filterOptionsArr.push({
        option: row[optionValueKey],
        optionDisplayText: row[optionDisplayTextKey],
        isChecked: combinedFiltersObj[filterByProperty] ? combinedFiltersObj[filterByProperty].includes(row[optionValueKey]) : false,
        businessType: row['businessType'] ? row['businessType'] : null
      });
    });

    // sort options alphabetically
    filterOptionsArr.sort(function(a, b) {
      return a[alphabeticalSortKey].localeCompare(b[alphabeticalSortKey]);
    });

    if (filterByProperty === 'equipmentType') {
      return _groupByBusinessType(filterOptionsArr);
    } else {
      return filterOptionsArr;
    }
  }

  toggleRemoveAllFilters = (show) => {
    if (show && Object.keys(this.cache.combinedFiltersObj).length > 0) {
      this.cache.$removeAllFiltersBtn.removeAttr('hidden');
    } else {
      this.cache.$removeAllFiltersBtn.attr('hidden', 'hidden');
    }
  }

  updateFilterBtnCount = (filterProperty, filterCount) => {
    let label;
    let $btnElem;
    const { i18nKeys } = this.cache;

    switch (filterProperty) {
      case 'customer': {
        label = i18nKeys['customer'];
        $btnElem = this.cache.$customerFilterLabel;
        break;
      }
      case 'lineCode': {
        label = i18nKeys['line'];
        $btnElem = this.cache.$lineFilterLabel;
        break;
      }
      case 'equipmentStatusDesc': {
        label = i18nKeys['equipmentStatus'];
        $btnElem = this.cache.$statusFilterLabel;
        break;
      }
      case 'equipmentType': {
        label = i18nKeys['equipmentType'];
        $btnElem = this.cache.$equipmentTypeFilterLabel;
        break;
      }
      default: {
        break;
      }
    }

    $btnElem.text(`${getI18n(label)}: ${filterCount}`);
  }

  // Checks filter set of active filter, uf combination of filters is used
  // e.g 4 Customers selected -> 4 Statuses available
  // -> user selects only 1 status -> not all 4 Customers might be part of selection anymore
  checkActiveFilterSets = (filterVal) => {
    const { combinedFiltersObj, currentApiFilterValsObj } = this.cache;

    // no active filters
    if (Object.keys(combinedFiltersObj).length === 0) {
      return;
    }
    Object.keys(combinedFiltersObj).forEach(enabledFilter => {
      if (combinedFiltersObj[enabledFilter].length && filterVal === _remapFilterProperty(enabledFilter)) {
        const filterPropertyRemap = _remapFilterProperty(enabledFilter);
        const activeFilterItemsArr = currentApiFilterValsObj[filterPropertyRemap];

        const availableFiltersInDataSet = activeFilterItemsArr.map(item => item[_remapFilterOptionKey(enabledFilter)]);

        // if number of items differ between what's selected and what's available,
        // e.g. 4 Customers previously selected, but after adding a new filter only 2 Customers are now available
        // refresh filter buttons and their count
        if (combinedFiltersObj[enabledFilter].length > availableFiltersInDataSet.length) {
          combinedFiltersObj[enabledFilter] = availableFiltersInDataSet;
          this.updateFilterBtnCount(enabledFilter, availableFiltersInDataSet.length);
        }
      }
    });
  }

  applyFilter = (options) => {
    const { activeFilterForm, $activeFilterBtn, i18nKeys } = this.cache;
    const $filtersCheckbox = this.root.find('.js-tp-my-equipment-filter-checkbox:not(.js-tp-my-equipment-filter-group-checkbox)');
    const $filtersRadio = this.root.find('.js-tp-my-equipment-filter-radio');
    const $freeTextFilterInput = this.root.find('.js-tp-my-equipment-filter-input');
    let filterCount = 0;
    let filterData = [];
    let label;
    let analyticsAction = {};

    switch (activeFilterForm) {
      case 'country':{
        filterData = this.cache.countryData;
        $filtersRadio.each(function(index) {
          if ($(this).is(':checked')) {
            filterCount++;
            filterData[index].isChecked = true;
          } else {
            filterData[index].isChecked = false;
          }
        });

        label = i18nKeys['country'];
        break;
      }
      case 'customer': {
        filterCount = this.addCombinedFilter(activeFilterForm, $filtersCheckbox);
        label = i18nKeys['customer'];
        break;
      }
      case 'lineCode': {
        filterCount = this.addCombinedFilter(activeFilterForm, $filtersCheckbox);
        label = i18nKeys['line'];
        break;
      }
      case 'equipmentStatusDesc': {
        filterCount = this.addCombinedFilter(activeFilterForm, $filtersCheckbox);
        label = i18nKeys['equipmentStatus'];
        break;
      }
      case 'equipmentType': {
        filterCount = this.addCombinedFilter(activeFilterForm, $filtersCheckbox);
        label = i18nKeys['equipmentType'];
        break;
      }
      case 'serialNumber': {
        this.cache.combinedFiltersObj['serialNumber'] = $freeTextFilterInput.val();
        filterCount = $freeTextFilterInput.val() !== '' ? 1 : 0;
        label = i18nKeys['serialNumber'];
        break;
      }
      case 'equipmentName': {
        this.cache.combinedFiltersObj['equipmentName'] = $freeTextFilterInput.val();
        filterCount = $freeTextFilterInput.val() !== '' ? 1 : 0;
        label = i18nKeys['equipmentDescription'];
        break;
      }
      case 'customise-table':{
        filterData = this.cache.customisableTableHeaders;
        $filtersCheckbox.each(function(index) {
          if ($(this).is(':checked')) {
            filterData[index].isChecked = true;
          } else {
            filterData[index].isChecked = false;
          }
        });

        label = 'customise-table';
        break;
      }
      default: {
        break;
      }
    }

    // if Remove all button pressed in current modal
    if (options && options.removeFilter) {
      if (this.cache.combinedFiltersObj[activeFilterForm]) {
        delete this.cache.combinedFiltersObj[activeFilterForm];
        filters.setFilterChipInLocalStorage(EQ_TYPE,this.cache.combinedFiltersObj,this.getActiveCountryCode());
      }
      filterCount = null;
    }

    if ($activeFilterBtn) {
      if (filterCount) {
        $activeFilterBtn.addClass('active');
      } else {
        $activeFilterBtn.removeClass('active');
      }
    }

    analyticsAction = {
      action: options && options.removeFilter ? 'removedFilter' : 'addedFilter',
      targetFilter: $activeFilterBtn,
      items: this.cache.combinedFiltersObj[activeFilterForm]
    };

    // if Country filter change
    if (activeFilterForm === 'country') {
      analyticsAction.items = this.getActiveCountryCode();
      this.renderNewCountry(label, analyticsAction);
      return;
    }

    // if show/hide columns
    if (activeFilterForm === 'customise-table') { // other type of filter change
      const tableData = _processTableData.call(this, {summary:this.cache.tableData,i18nKeys:this.cache.i18nKeys,meta:this.cache.meta});
      this.renderPaginationTableData(tableData);
      this.cache.$modal.modal('hide');
      _addShowHideFilterAnalytics(filterData);
      return;
    }

    // All other filters
    this.updateFilterCountValue(label,filterCount,$activeFilterBtn);
    this.renderNewPage({'resetSkip': true, analyticsAction});
    this.getAllAvailableFilterVals(['statuses', 'types', 'lines', 'customers'], false, activeFilterForm);
    this.cache.$modal.modal('hide');
    this.toggleRemoveAllFilters(true);
  }

  deleteAllFilters = () => {
    const $filterBtns = this.root.find('.tp-my-equipment__filter-button:not(.tp-my-equipment__country-button-filter)');
    // if no filters are active
    if (Object.keys(this.cache.combinedFiltersObj).length === 0) {
      return;
    }

    const analyticsAction = {
      action: 'removedAllFilters',
      targetFilter: null,
      items: this.cache.combinedFiltersObj
    };

    this.cache.combinedFiltersObj = {};
    this.cache.currentApiFilterValsObj = {
      'statuses': [],
      'types': [],
      'customers': [],
      'lines': []
    };

    this.cache.activeSortData = null;

    filters.emptyFilterChipInLocalStorage(EQ_TYPE);

    $filterBtns.each((index, item) => {
      const initialLabel = $(item).data('label');
      $(item).removeClass('active');
      $(item).text(initialLabel);
    });

    this.renderNewPage({'resetSkip': true, analyticsAction});
  }

  sortTableByKey = ($tHeadBtn) => {
    const sortedByKey = $tHeadBtn.data('key');
    let sortOrder = 'asc';

    // same table header clicked, change from asc to desc
    if (this.cache.activeSortData && this.cache.activeSortData.sortedByKey === sortedByKey) {
      sortOrder = this.cache.activeSortData.sortOrder === 'asc' ? 'desc' : 'asc';
    }

    this.cache.activeSortData = {
      'sortedByKey': sortedByKey,
      'sortOrder': sortOrder,
      'sendPosition': sortedByKey === 'functionalLocation'
    };
    this.renderNewPage({'resetSkip': true});
  }

  addCombinedFilter = (activeFilterForm, $filtersCheckbox) => {
    if ($filtersCheckbox.filter(':checked').length > 0) {
      this.cache.combinedFiltersObj[activeFilterForm] = [];
      $filtersCheckbox.each((index, item) => {
        if ($(item).is(':checked')) {
          this.cache.combinedFiltersObj[activeFilterForm].push($(item).val());
        }
      });
      return this.cache.combinedFiltersObj[activeFilterForm].length;
    } else {
      delete this.cache.combinedFiltersObj[activeFilterForm];
      return 0;
    }
  }

  updateFilterCountValue = (label,filterCount,htmlUpdate) => {
    const { $modal } = this.cache;

    if (htmlUpdate) {
      if (!filterCount) {
        htmlUpdate.text(`${getI18n(label)} +`);
      } else {
        htmlUpdate.text(`${getI18n(label)}: ${filterCount}`);
      }
    }
    $modal.modal('hide');
  }

  renderFilterForm = (data, formDetail, $filterBtn) => {
    const { i18nKeys } = this.cache;
    let selectedItemsNo = 0;

    if (formDetail.header === i18nKeys['country']) {
      data.forEach((item,index) => {
        data[index]= {
          ...item,
          optionDisplayText: item.option,
          isCountry:true
        };
      });
    }

    // if checkbox type filter
    if (formDetail.maxFiltersSelection) {
      // Equipment Type has different data format, due to being grouped in Packaging or Processing
      if (formDetail.header === i18nKeys['equipmentType']) {
        data.forEach(dataItem => {
          const selectedItems = dataItem.options.filter(item => item.isChecked).length;
          selectedItemsNo += selectedItems;
        });
      } else {
        selectedItemsNo = data.filter(item => item.isChecked).length;
      }
    }

    render.fn({
      template: 'filterForm',
      data: {
        header: formDetail.header,
        formData: data,
        maxItemsNo: formDetail.maxFiltersSelection ? formDetail.maxFiltersSelection : null,
        selectedItemsNo: selectedItemsNo ? selectedItemsNo : 0,
        isEquipmentType: formDetail.header === i18nKeys['equipmentType'],
        ...i18nKeys,
        singleButton: formDetail.singleButton === true ? true : false,
        customiseTable: formDetail.activeForm === 'customise-table' ? true : false,
        isRadio: formDetail.isRadio === true ? true : false,
        radioGroupName: formDetail.radioGroupName,
        isTextInput: formDetail.isTextInput,
        autoLocatorModal: `${formDetail.activeForm}Overlay`,
        autoLocatorInput: `${formDetail.activeForm}InputBox`,
        autoLocatorCheckbox: `${formDetail.activeForm}FilterCheckboxOverlay`,
        autoLocatorCheckboxText: `${formDetail.activeForm}FilterItemOverlay`
      },
      target: '.tp-equipment__filter-form',
      hidden: false
    });
    this.cache.activeFilterForm = formDetail.activeForm;
    this.cache.$activeFilterBtn = $filterBtn;
  }

  renderNewPage = ({resetSkip, analyticsAction}) => {
    const {itemsPerPage, activeSortData, combinedFiltersObj} = this.cache; //countryData
    const equipmentApi = this.cache.equipmentApi.data('list-api');
    //const activeCountry = countryData.filter(e => e.isChecked);
    const countryCode = this.getActiveCountryCode();
    let apiUrlRequest = '';
    const filtersQuery = _buildQueryUrl(combinedFiltersObj);
    const skipIndex = resetSkip ? 0 : this.cache.skipIndex;

    this.cache.$content.addClass('d-none');
    this.cache.$spinner.removeClass('d-none');

    if (resetSkip) {
      // reset indexes when a filter is set/removed
      this.cache.activePage = 1;
      this.cache.skipIndex = 0;
    }

    apiUrlRequest = `${equipmentApi}?skip=${skipIndex}&count=${itemsPerPage}&countrycodes=${countryCode}`;

    if (filtersQuery) {
      apiUrlRequest += `&${filtersQuery}`;
    }

    if (activeSortData) {
      let sortingParam = `${activeSortData.sortedByKey.toLowerCase()} ${activeSortData.sortOrder}`;

      // SMAR-25942 if sorting by functionalLocation, send position parameter as well
      if (activeSortData.sendPosition) {
        sortingParam = `${activeSortData.sortedByKey.toLowerCase()} ${activeSortData.sortOrder},position`;
      }

      apiUrlRequest += `&sort=${sortingParam}`;
    } else {
      apiUrlRequest += `&sort=${this.cache.defaultSortParams}`;
    }

    auth.getToken(({ data: authData }) => {
      ajaxWrapper
        .getXhrObj({
          url: apiUrlRequest,
          method: 'GET',
          contentType: 'application/json',
          dataType: 'json',
          beforeSend(jqXHR) {
            jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
            jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
          },
          showLoader: true
        }).then(response => {
          this.cache.$spinner.addClass('d-none');
          this.cache.$content.removeClass('d-none');
          this.cache.tableData = response.data;
          this.cache.tableData = this.cache.tableData.map((item) => ({
            ...item,
            equipmentStatus: item.equipmentStatus || ''
          }));
          this.cache.meta = response.meta;
          const tableData = _processTableData.call(this, {summary:this.cache.tableData,i18nKeys:this.cache.i18nKeys,meta:this.cache.meta});
          this.renderPaginationTableData(tableData);
          this.renderSearchCount();
          this.mapTableColumn();

          if (analyticsAction && analyticsAction.action === 'removedFilter' && response.meta) {
            _removeFilterAnalytics(analyticsAction.targetFilter, response.meta.total);
          }

          if (analyticsAction && analyticsAction.action === 'removedAllFilters' && response.meta) {
            _removeAllFiltersAnalytics(analyticsAction.items, response.meta.total);
          }

          if (analyticsAction && analyticsAction.action === 'addedFilter' && response.meta) {
            _addFilterAnalytics(analyticsAction.targetFilter, response.meta.total, analyticsAction.items);
          }
        }).fail(() => {
          this.cache.$content.removeClass('d-none');
          this.cache.$spinner.addClass('d-none');
        });
    });
  }

  renderDefaultCountry = () => {
    this.cache.$spinner.removeClass('d-none');
    const countryApi = this.cache.equipmentApi.data('country-api');
    const equipmentApi = this.cache.equipmentApi.data('list-api');
    let apiUrlRequest = '';
    auth.getToken(({ data: authData }) => {
      ajaxWrapper
        .getXhrObj({
          url: countryApi,
          method: ajaxMethods.GET,
          cache: true,
          dataType: 'json',
          contentType: 'application/json',
          beforeSend(jqXHR) {
            jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
            jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
          },
          showLoader: true
        }).then(res => {
          this.cache.countryData = _getFormattedCountryData(res.data);
          this.cache.authData = authData;
          let { countryCode } = this.cache.countryData && this.cache.countryData[0];
          const { itemsPerPage } = this.cache;

          this.cache.combinedFiltersObj=filters.getFiltersValueFromLocalStorage(EQ_TYPE);
          if(this.cache.combinedFiltersObj!==null){
            if (Object.keys(this.cache.combinedFiltersObj).length) {

              countryCode=this.cache.combinedFiltersObj[EQ_FILTERS.COUNTRY] || '';

              if(this.cache.combinedFiltersObj[EQ_FILTERS.COUNTRY]){
                const filterData = this.cache.countryData;
                for (var index in filterData) {
                  if (filterData[index].countryCode===countryCode) {
                    filterData[index].isChecked = true;
                  } else {
                    filterData[index].isChecked = false;
                  }
                }

                this.cache.$countryFilterLabel.addClass('active');
                if (this.cache.$equipmentDescFilterLabel) {
                  this.cache.$countryFilterLabel.text(`${getI18n(this.cache.i18nKeys['country'])}: ${1}`);
                }
              }

              if(this.cache.combinedFiltersObj[EQ_FILTERS.EQUIPMENTNAME]){
                this.cache.$equipmentDescFilterLabel.addClass('active');
                if (this.cache.$equipmentDescFilterLabel) {
                  this.cache.$equipmentDescFilterLabel.text(`${getI18n(this.cache.i18nKeys['equipmentDescription'])}: ${1}`);
                }
              }

              if(this.cache.combinedFiltersObj[EQ_FILTERS.SERIALNUMBER]){
                this.cache.$serialNumberFilterLabel.addClass('active');
                if (this.cache.$serialNumberFilterLabel) {
                  this.cache.$serialNumberFilterLabel.text(`${getI18n(this.cache.i18nKeys['serialNumber'])}: ${1}`);
                }
              }
              this.getAllAvailableFilterVals(['statuses', 'types', 'lines', 'customers'], false);
            }
            else
            {
              this.getAllAvailableFilterVals(['statuses', 'types', 'lines', 'customers'], true);
            }
            this.toggleRemoveAllFilters(true);
          }
          else {
            this.getAllAvailableFilterVals(['statuses', 'types', 'lines', 'customers'], true);
          }

          const filtersQuery = _buildQueryUrl(this.cache.combinedFiltersObj);

          apiUrlRequest = `${equipmentApi}?skip=0&count=${itemsPerPage}&countrycodes=${countryCode}`;

          if (filtersQuery) {
            apiUrlRequest += `&${filtersQuery}`;
          }

          apiUrlRequest += `&sort=${this.cache.defaultSortParams}`;

          ajaxWrapper
            .getXhrObj({
              url: apiUrlRequest,
              method: 'GET',
              contentType: 'application/json',
              dataType: 'json',
              beforeSend(jqXHR) {
                jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
                jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
              },
              showLoader: true
            }).then(response => {
              this.cache.$spinner.addClass('d-none');
              this.cache.$content.removeClass('d-none');
              this.cache.tableData = response.data;
              this.cache.tableData = this.cache.tableData.map((item) => ({
                ...item,
                equipmentStatus: item.equipmentStatus || ''
              }));
              this.cache.meta = response.meta;
              this.cache.countryData.splice(0,1,{...this.cache.countryData[0],isChecked:true});
              const tableData = _processTableData.call(this, {summary:this.cache.tableData,i18nKeys:this.cache.i18nKeys,meta:this.cache.meta});
              this.renderPaginationTableData(tableData);
              this.renderSearchCount();
              this.mapTableColumn();
            }).fail(() => {
              this.cache.$content.removeClass('d-none');
              this.cache.$spinner.addClass('d-none');
            });
        });
    });
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.renderDefaultCountry();
  }
}

export default MyEquipment;
