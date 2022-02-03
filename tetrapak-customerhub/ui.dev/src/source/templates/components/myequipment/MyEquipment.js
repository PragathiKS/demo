import $ from 'jquery';
import 'bootstrap';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { tableSort, getI18n } from '../../../scripts/common/common';
import { render } from '../../../scripts/utils/render';
import auth from '../../../scripts/utils/auth';
import { ajaxMethods } from '../../../scripts/utils/constants';

// https://github.com/interstellarjay/zapfilter
class Zapfilter {
  constructor(dataSet, filters) {
    this.filters = filters || [];
    this.filteredSet = dataSet || [];
    this.filterORBuffer = dataSet || [];
  }

  getAllFilters() {
    return this.filters;
  }

  getTotalFilters() {
    return this.filters.length;
  }

  setFilters(filters) {
    return filters.forEach(f => this.filters.push(f));
  }

  clearFilters() {
    this.filterORBuffer = [];
    this.filteredSet = [];
    return (this.filters = []);
  }

  applySingleFilter(x, f) {
    return f.filter(x, f.onProperty, f.value);
  }

  applyZapFilters(x, f) {
    return (x = this.applySingleFilter(x, f));
  }

  applyORFilters(i, x, f) {
    // Used with this.filterOR(..)
    return (this.filterORBuffer[i] = this.applySingleFilter(x, f));
  }

  combineResultsAndRemoveDuplicates(combinedDataSet) {
    const buffer = combinedDataSet;
    const isolateUniques = buffer.filter(
      (value, index) => buffer.indexOf(value) === index
    );
    const isolateUniquesFlat = isolateUniques.map(x => x[0]);
    return isolateUniquesFlat;
  }

  filterAND(dataSet) {
    // Missing data set
    if (!dataSet) {
      throw new Error('ZapFilter error ===> No data set supplied!');
    }
    // Filter the data
    this.filteredSet = dataSet;
    // Filter the result
    for (let i = 0; i < this.filters.length; i++) {
      this.filteredSet = this.applyZapFilters(
        this.filteredSet,
        this.filters[i]
      );
    }
    // Return the response
    return this.filteredSet;
  }

  filter(dataSet) {
    // Missing data set
    if (!dataSet) {
      throw new Error('ZapFilter error ===> No data set supplied!');
    }
    // Filter the data
    this.filteredSet = dataSet;
    // Filter the result
    for (let i = 0; i < this.filters.length; i++) {
      this.applyORFilters(i, this.filteredSet, this.filters[i]);
    }
    // Return the response
    const flattenedResultsWithPotentialDuplicates = this.filterORBuffer;
    const results = this.combineResultsAndRemoveDuplicates(
      flattenedResultsWithPotentialDuplicates
    );
    return results;
  }

  filterEqualTo(data, property, value) {
    return data.filter(item => {
      if (typeof item[property] === 'string') {
        return (
          item[property].toUpperCase() === value.toUpperCase()
        );
      }
      return item[property] === value;
    });
  }

  filterEqualToArr(data, property, valueArr) {
    return data.filter(item => {
      if (typeof item[property] === 'string') {
        return (
          valueArr.includes(item[property])
        );
      }
      return valueArr.includes(item[property]);
    });
  }

  filterPartialMatch(data, property, value) {
    return data.filter(item => {
      if (typeof item[property] !== 'string') {
        throw new Error(
          'ZapFilter error ===> filterPartialMatch function must validate string value!'
        );
      }
      // Parse value to uppercase and remove spaces, dashes and underscores.
      const v = value.toUpperCase().replace(/\s|_|-/g, '');
      return RegExp(v, 'g').test(
        item[property].toUpperCase().replace(/\s|_|-/g, '')
      );
    });
  }

  removeDuplicates(dataSet) {
    const flatData = dataSet.flat();
    return this.combineResultsAndRemoveDuplicates(flatData);
  }
}

function paginate(totalItems, currentPage, pageSize, maxPages) {
  // calculate total pages
  const totalPages = Math.ceil(totalItems / pageSize);
  // ensure current page isn't out of range
  if (currentPage < 1) {
    currentPage = 1;
  } else if (currentPage > totalPages) {
    currentPage = totalPages;
  }

  let startPage, endPage;
  if (totalPages <= maxPages) {
    // total pages less than max so show all pages
    startPage = 1;
    endPage = totalPages;
  } else {
    // total pages more than max so calculate start and end pages
    const maxPagesBeforeCurrentPage = Math.floor(maxPages / 2);
    const maxPagesAfterCurrentPage = Math.ceil(maxPages / 2) - 1;
    if (currentPage <= maxPagesBeforeCurrentPage) {
      // current page near the start
      startPage = 1;
      endPage = maxPages;
    } else if (currentPage + maxPagesAfterCurrentPage >= totalPages) {
      // current page near the end
      startPage = totalPages - maxPages + 1;
      endPage = totalPages;
    } else {
      // current page somewhere in the middle
      startPage = currentPage - maxPagesBeforeCurrentPage;
      endPage = currentPage + maxPagesAfterCurrentPage;
    }
  }

  // calculate start and end item indexes
  const startIndex = (currentPage - 1) * pageSize;
  const endIndex = Math.min(startIndex + pageSize - 1, totalItems - 1);

  // create an array of pages
  const allPages = Array.from(Array((endPage + 1) - startPage).keys()).map(i => startPage + i);

  const pagesHbsArr = [];
  allPages.forEach(page => {
    const pageObj = {
      isPage: true,
      isSelected: page === currentPage,
      pageNumber: page,
      skip: (page - 1) * pageSize
    };
    pagesHbsArr.push(pageObj);
  });

  return {
    totalItems: totalItems,
    currentPage: currentPage,
    pageSize: pageSize,
    totalPages: totalPages,
    startPage: startPage,
    endPage: endPage,
    startIndex: startIndex,
    endIndex: endIndex,
    prevDisabled: currentPage === 1,
    nextDisabled: currentPage === totalPages,
    prevPage: currentPage - 1,
    nextPage: currentPage + 1,
    prevSkip: (currentPage - 2) * pageSize,
    nextSkip: currentPage * pageSize,
    lastSkip: (endPage - 1) * pageSize,
    pages: pagesHbsArr
  };
}

function getFormattedData(array){
  array.forEach((item,index) => {
    array[index] = {
      option: item.countryName,
      countryCode:item.countryCode,
      isChecked: index === 0 || item.isChecked ? true: false
    };
  });
  return array;
}

function groupByBusinessType(filterOptionsArr) {
  const businessTypeArr = [];
  const groupedFilterOptions = [];
  // get all unique business types
  filterOptionsArr.forEach((item) => {
    if (businessTypeArr.indexOf(item['businessType']) === -1) {
      businessTypeArr.push(item['businessType']);
    }
  });

  // create an object for each business type with it's corresponding items
  businessTypeArr.forEach((businessType) => {
    const optionsForBusinessType = filterOptionsArr.filter(filterOption => filterOption.businessType === businessType);

    groupedFilterOptions.push({
      businessTypeLabel: businessType,
      options: optionsForBusinessType,
      isChecked: !optionsForBusinessType.some(filterOption => filterOption.isChecked === false)
    });
  });

  return groupedFilterOptions;
}

function getFilterModalData(filterByProperty, array, combinedFiltersObj, filteredTableData) {
  const filterOptionsArr = [];
  const zapFilter = new Zapfilter();
  // if a single filter is used, do not disable any of it's options in the modal
  const isSingleFilterApplied = typeof combinedFiltersObj[filterByProperty] !== 'undefined' &&
                                  Object.keys(combinedFiltersObj).length === 1;

  array.forEach((row) => {
    // do not push duplicates, only unique items
    const isItemPresent = filterOptionsArr.some((o) => o.value === row[filterByProperty]);
    if (!isItemPresent) {
      filterOptionsArr.push({
        value: row[filterByProperty],
        businessType: row['businessType'] ? row['businessType'] : null
      });
    }
  });

  filterOptionsArr.forEach((item,index) => {
    // check if filter option is available for current data set, otherwise mark it disabled
    let itemFilteredTableData = [];
    zapFilter.clearFilters();
    zapFilter.setFilters([{
      filter: zapFilter.filterEqualTo,
      onProperty: filterByProperty,
      value: item.value
    }]);

    itemFilteredTableData = zapFilter.filter(filteredTableData);

    filterOptionsArr[index] = {
      option: item.value,
      isChecked: combinedFiltersObj[filterByProperty] ? combinedFiltersObj[filterByProperty].includes(item.value) : false,
      isDisabled: typeof itemFilteredTableData[0] === 'undefined' && !isSingleFilterApplied,
      businessType: item.businessType
    };
  });

  // sort options alphabetically
  filterOptionsArr.sort(function(a, b) {
    return a.option.localeCompare(b.option);
  });

  if (filterByProperty === 'equipmentType') {
    return groupByBusinessType(filterOptionsArr);
  } else {
    return filterOptionsArr;
  }
}

function _renderCountryFilters() {
  this.cache.$spinner.removeClass('d-none');
  const countryApi = this.cache.equipmentApi.data('country-api');
  const equipmentApi = this.cache.equipmentApi.data('list-api');
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
        this.cache.countryData = getFormattedData(res.data);
        const { countryCode } = this.cache.countryData && this.cache.countryData[0];
        ajaxWrapper
          .getXhrObj({
            url: `${equipmentApi}?skip=0&count=25&version=preview&countrycodes=${countryCode}`,
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
            this.cache.filteredTableData = [...this.cache.tableData];
            this.cache.meta = response.meta;
            this.cache.countryData.splice(0,1,{...this.cache.countryData[0],isChecked:true});
            this.renderFilterForm(this.cache.countryData,{ activeForm:'country',header:this.cache.i18nKeys['country']}, this.cache.$countryFilterLabel);
            this.renderTableData();
            this.renderSearchCount();
            this.mapTableColumn();
          }).fail(() => {
            this.cache.$content.removeClass('d-none');
            this.cache.$spinner.addClass('d-none');
          });
      });
  });

}

function _processKeys(keys, ob) {
  if(keys.length){
    return keys;
  } else {
    let country,equipmentTypeDesc,site,line,serialNumber,equipmentStatus,functionalLocation,siteDescription,location;
    for(const i in ob){
      if(i === 'countryCode'){
        country = i;
      }else if(i === 'siteName'){
        site = i;
      }else if(i === 'lineName'){
        line = i;
      }
      else if(i === 'equipmentTypeDesc'){
        equipmentTypeDesc = i;
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
      else if(i === 'equipmentStatus'){
        equipmentStatus = i;
      }
      else if(i === 'functionalLocation'){
        functionalLocation = i;
      }
    }
    return [country, site, siteDescription, line, equipmentTypeDesc, serialNumber, equipmentStatus, location, functionalLocation];
  }
}

function getKeyMap(key,i18nKeys){
  const headerObj = {};
  switch (key) {
    case 'countryCode': {
      headerObj['keyLabel'] = i18nKeys['country'];
      headerObj['showTooltip'] = i18nKeys['countryToolTip'].trim().length > 0 ? true : false;
      headerObj['tooltipText'] = i18nKeys['countryToolTip'];
      break;
    }
    case 'siteName': {
      headerObj['keyLabel'] = i18nKeys['site'];
      headerObj['showTooltip'] = i18nKeys['siteToolTip'].trim().length > 0 ? true : false;
      headerObj['tooltipText'] = i18nKeys['siteToolTip'];
      break;
    }
    case 'lineName': {
      headerObj['keyLabel'] = i18nKeys['line'];
      headerObj['showTooltip'] = i18nKeys['lineToolTip'].trim().length > 0 ? true : false;
      headerObj['tooltipText'] = i18nKeys['lineToolTip'];
      break;
    }
    case 'equipmentTypeDesc': {
      headerObj['keyLabel'] = i18nKeys['equipmentDescription'];
      headerObj['showTooltip'] = i18nKeys['equipDescToolTip'].trim().length > 0 ? true : false;
      headerObj['tooltipText'] = i18nKeys['equipDescToolTip'];
      break;
    }
    case 'siteDesc': {
      headerObj['keyLabel'] = i18nKeys['siteDescription'];
      headerObj['showTooltip'] = i18nKeys['siteDescToolTip'].trim().length > 0 ? true : false;
      headerObj['tooltipText'] = i18nKeys['siteDescToolTip'];
      break;
    }
    case 'location': {
      headerObj['keyLabel'] = i18nKeys['location'];
      headerObj['showTooltip'] = i18nKeys['locationToolTip'].trim().length > 0 ? true : false;
      headerObj['tooltipText'] = i18nKeys['locationToolTip'];
      break;
    }
    case 'serialNumber': {
      headerObj['keyLabel'] = i18nKeys['serialNumber'];
      headerObj['showTooltip'] = i18nKeys['serialNumToolTip'].trim().length > 0 ? true : false;
      headerObj['tooltipText'] = i18nKeys['serialNumToolTip'];
      break;
    }
    case 'equipmentStatus': {
      headerObj['keyLabel'] = i18nKeys['equipmentStatus'];
      headerObj['showTooltip'] = i18nKeys['equipStatToolTip'].trim().length > 0 ? true : false;
      headerObj['tooltipText'] = i18nKeys['equipStatToolTip'];
      break;
    }
    case 'functionalLocation': {
      headerObj['keyLabel'] = i18nKeys['functionalLocation'];
      headerObj['showTooltip'] = i18nKeys['functionalLocationToolTip'].trim().length > 0 ? true : false;
      headerObj['tooltipText'] = i18nKeys['functionalLocationToolTip'];
      break;
    }
    default: {
      break;
    }
  }
  return headerObj;
}

function _mapHeadings(keys,i18nKeys,activeSortData) {
  const sortByKey = activeSortData && activeSortData.sortedByKey;
  const sortOrder = activeSortData && activeSortData.sortOrder;
  return keys.map(key => ({
    key,
    myEquipment: true,
    isSortable: this.cache.sortableKeys.includes(key),
    isActiveSort: key === sortByKey,
    sortOrder: sortOrder,
    i18nKey: getKeyMap(key,i18nKeys).keyLabel,
    showTooltip: getKeyMap(key,i18nKeys).showTooltip,
    tooltipText: getKeyMap(key,i18nKeys).tooltipText
  }));
}

function _processTableData(data){
  let keys = [];
  if (Array.isArray(data.summary)) {
    data.summary = data.summary.map(summary => {
      keys = _processKeys(keys, summary);
      this.cache.tableHeaders = keys;
      return tableSort.call(this, summary, keys);
    });
    data.summaryHeadings = _mapHeadings.call(this,keys,data.i18nKeys,this.cache.activeSortData);
  }
  return data;
}

function renderPaginationTableData(list) {
  const $this = this;
  const paginationObj = paginate(list.meta.total, this.cache.activePage, this.cache.itemsPerPage, 3);

  if (list.summary.length === 0) {
    render.fn({
      template: 'myEquipmentTable',
      data: { noDataMessage: true, noDataFound: $this.cache.i18nKeys['noDataFound']  },
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
      $this.hideShowColums();
      $(function () {
        $('[data-toggle="tooltip"]').tooltip();
      });
    });
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
    this.cache.$showHideAllFiltersBtn = this.root.find('.js-tp-my-equipment__show-hide-all-button');
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
    this.cache.filteredTableData = [];
    this.cache.hideColumns = []; // column index;
    this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    this.cache.currentPageNumber = 1;
    this.cache.zapFilter = new Zapfilter();
    this.cache.filterModalData = {};
    this.cache.combinedFiltersObj = {};
    this.cache.activeFiltersArr = [];
    this.cache.sortableKeys = ['siteName','lineName','equipmentStatus','serialNumber','functionalLocation','siteDesc','location','equipmentTypeDesc'];
    this.cache.activeSortData = null;
    this.cache.activePage = 1;
    this.cache.skipIndex = 0;
    this.cache.itemsPerPage = 25;
  }
  bindEvents() {
    const {$mobileHeadersActions, $modal,i18nKeys,$myEquipmentCustomizeTableAction } = this.cache;
    this.cache.customisableTableHeaders = [
      {key:'countryCode',option:i18nKeys['country'],isChecked:true,index:0},
      {key:'siteName',option:i18nKeys['site'],isChecked:true,index:1},
      {key:'siteDesc',option:i18nKeys['siteDescription'],isChecked:false,index:2},
      {key:'lineName',option:i18nKeys['line'],isChecked:true,index:3},
      {key:'equipmentTypeDesc',option:i18nKeys['equipmentDescription'],isChecked:true,index:4},
      {key:'serialNumber',option:i18nKeys['serialNumber'],isChecked:true,index:5},
      {key:'equipmentStatus',option:i18nKeys['equipmentStatus'],isChecked:true,index:6},
      {key:'location',option:i18nKeys['location'],isChecked:false,index:7},
      {key:'functionalLocation',option:i18nKeys['functionalLocation'],isChecked:false,index:8}
    ];

    this.cache.$countryFilterLabel.on('click', () => {
      const formDetail = { activeForm:'country',header:i18nKeys['country'] };
      this.renderFilterForm(this.cache.countryData, formDetail, this.cache.$countryFilterLabel);
      $modal.modal();
    });

    this.cache.$customerFilterLabel.on('click', () => {
      const formDetail = {activeForm:'customer',header:i18nKeys['customer']};
      this.cache.filterModalData['customer'] = getFilterModalData('customer', [...this.cache.tableData], this.cache.combinedFiltersObj, [...this.cache.filteredTableData]);
      this.renderFilterForm(this.cache.filterModalData['customer'], formDetail, this.cache.$customerFilterLabel);
      $modal.modal();
    });

    this.cache.$lineFilterLabel.on('click', () => {
      const formDetail = {activeForm:'lineName',header:i18nKeys['line']};
      this.cache.filterModalData['lineName'] = getFilterModalData('lineName', [...this.cache.tableData], this.cache.combinedFiltersObj, [...this.cache.filteredTableData]);
      this.renderFilterForm(this.cache.filterModalData['lineName'], formDetail, this.cache.$lineFilterLabel);
      $modal.modal();
    });

    this.cache.$statusFilterLabel.on('click', () => {
      const formDetail = {activeForm:'equipmentStatus',header:i18nKeys['equipmentStatus']};
      this.cache.filterModalData['equipmentStatus'] = getFilterModalData('equipmentStatus', [...this.cache.tableData], this.cache.combinedFiltersObj, [...this.cache.filteredTableData]);
      this.renderFilterForm(this.cache.filterModalData['equipmentStatus'], formDetail, this.cache.$statusFilterLabel);
      $modal.modal();
    });

    this.cache.$equipmentDescFilterLabel.on('click', () => {
      const formDetail = {activeForm:'equipmentTypeDesc', header:i18nKeys['equipmentDescription'], isTextInput: true};
      const activeEquipTypeDesc = this.cache.combinedFiltersObj['equipmentTypeDesc'] ? this.cache.combinedFiltersObj['equipmentTypeDesc'] : '';
      this.renderFilterForm(activeEquipTypeDesc, formDetail, this.cache.$equipmentDescFilterLabel);
      $modal.modal();
    });

    this.cache.$equipmentTypeFilterLabel.on('click', () => {
      const formDetail = {activeForm:'equipmentType',header:i18nKeys['equipmentType']};
      this.cache.filterModalData['equipmentType'] = getFilterModalData('equipmentType', [...this.cache.tableData], this.cache.combinedFiltersObj, [...this.cache.filteredTableData]);
      this.renderFilterForm(this.cache.filterModalData['equipmentType'], formDetail, this.cache.$equipmentTypeFilterLabel);
      $modal.modal();
    });

    this.cache.$serialNumberFilterLabel.on('click', () => {
      const formDetail = {activeForm:'serialNumber',header:i18nKeys['serialNumber'], isTextInput: true};
      const activeSerialNum = this.cache.combinedFiltersObj['serialNumber'] ? this.cache.combinedFiltersObj['serialNumber'] : '';
      this.renderFilterForm(activeSerialNum, formDetail, this.cache.$serialNumberFilterLabel);
      $modal.modal();
    });

    this.cache.$functionalLocFilterLabel.on('click', () => {
      const formDetail = {activeForm:'functionalLocation',header:i18nKeys['functionalLocation'], isTextInput: true};
      const activeSerialNum = this.cache.combinedFiltersObj['functionalLocation'] ? this.cache.combinedFiltersObj['functionalLocation'] : '';
      this.renderFilterForm(activeSerialNum, formDetail, this.cache.$functionalLocFilterLabel);
      $modal.modal();
    });

    $myEquipmentCustomizeTableAction.on('click', () => {
      this.renderFilterForm(this.cache.customisableTableHeaders, { activeForm:'customise-table',header:i18nKeys['customizeTable'],singleButton:false });
      $('.tp-my-equipment__header-actions').removeClass('show');
      $modal.modal();
    });

    $mobileHeadersActions.on('click', () => {
      if($('.tp-my-equipment__header-actions').hasClass('show')){
        $('.tp-my-equipment__header-actions').removeClass('show');
      } else {
        $('.tp-my-equipment__header-actions').addClass('show');
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

    this.root.on('click', '.js-tp-my-equipment__remove-all-button',  () => {
      this.deleteAllFilters();
    });

    this.cache.$showHideAllFiltersBtn.on('click', () => {
      this.showHideAllFilters();
    });

    this.root.on('click', '.js-my-equipment__table-summary__sort',  (e) => {
      const $tHeadBtn = $(e.currentTarget).parent();
      this.sortTableByKey($tHeadBtn);
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
      const $currentTargetWrapper = $currentTarget.parents('.tp-my-equipment__type-group-option');
      const $checkboxGroupInputs = $currentTargetWrapper.next().find('.tpatom-checkbox__input').not(':disabled');
      $checkboxGroupInputs.each((index, item) => {
        $(item).prop('checked', $currentTarget.is(':checked'));
      });
    });

    this.root.on('change', '.tp-my-equipment-group-filter-options .js-tp-my-equipment-filter-checkbox', (e) => {
      const $currentTarget = $(e.target);
      const $thisGroupAllWrapper = $currentTarget.parents('.tp-my-equipment-group-filter-options').prev();
      const $thisGroupAllCheckbox = $thisGroupAllWrapper.find('.js-tp-my-equipment-filter-group-checkbox');
      if (!$currentTarget.is(':checked')) {
        $thisGroupAllCheckbox.prop('checked', false);
      }
    });

    this.root.on('click', '.js-page-number',  (e) => {
      const $btn = $(e.currentTarget);
      const $pagination = $btn.parents('.js-tbl-pagination');

      if (!$btn.hasClass('active')) {
        // stop pointer events until new page is rendered, in order to not send multiple AJAX calls
        $pagination.addClass('pagination-lock');
        this.cache.activePage = $btn.data('page-number');
        this.cache.skipIndex = $btn.data('skip');
        this.renderNewPage();
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

  renderSearchCount = () => {
    this.cache.$searchResults.text(`${this.cache.meta.total} ${getI18n(this.cache.i18nKeys['searchResults'])}`);
  }

  renderTableData = (label,filterValues) => {
    this.updateTable(label,filterValues);
    if(!label || label !== this.cache.i18nKeys['country']){
      renderPaginationTableData.call(
        this,
        _processTableData.call(this, {summary:this.cache.filteredTableData,i18nKeys:this.cache.i18nKeys,meta:this.cache.meta}),
        {isCustomiseTableFilter :label === 'customise-table' ? true : false });
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

  updateTable = (label,filterValues) => {
    const { i18nKeys, $countryFilterLabel } = this.cache;
    const { tableData } = this.cache;

    if (label === i18nKeys['country'] && filterValues.length === 0) {
      this.cache.filteredTableData = tableData;
      return;
    }

    // Country filter change
    if (label === i18nKeys['country'] && filterValues.length > 0) {
      const equipmentApi = this.cache.equipmentApi.data('list-api');
      this.cache.$spinner.removeClass('d-none');
      this.cache.$content.addClass('d-none');
      auth.getToken(({ data: authData }) => {
        ajaxWrapper
          .getXhrObj({
            url: `${equipmentApi}?skip=0&count=25&version=preview&countrycodes=${filterValues[0].countryCode}`,
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
            this.cache.filteredTableData = [...this.cache.tableData];
            this.deleteAllFilters();
            renderPaginationTableData.call(
              this,
              _processTableData.call(this, {summary:this.cache.filteredTableData,i18nKeys:this.cache.i18nKeys,meta:this.cache.meta}),
              {isCustomiseTableFilter :label === 'customise-table' ? true : false });
            this.renderSearchCount();
            this.updateFilterCountValue(label,1,$countryFilterLabel);
          }).fail(() => {
            this.cache.$spinner.addClass('d-none');
            this.cache.$content.removeClass('d-none');
          });
      });
    }

    if (label !== i18nKeys['country']) {
      this.cache.zapFilter.setFilters(this.cache.activeFiltersArr);
      this.cache.filteredTableData = this.cache.zapFilter.filterAND(this.cache.tableData);
    }
  }

  applyFilter = (options) => {
    const { activeFilterForm, $activeFilterBtn, i18nKeys } = this.cache;
    const $filtersCheckbox = this.root.find('.js-tp-my-equipment-filter-checkbox:not(.js-tp-my-equipment-filter-group-checkbox)');
    const $freeTextFilterInput = this.root.find('.js-tp-my-equipment-filter-input');
    let filterCount = 0;
    let filterData = [];
    let label;

    switch (activeFilterForm) {
      case 'country':{
        filterData = this.cache.countryData;
        $filtersCheckbox.each(function(index) {
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
        this.updateActiveFiltersData();
        label = i18nKeys['customer'];
        break;
      }
      case 'lineName': {
        filterCount = this.addCombinedFilter(activeFilterForm, $filtersCheckbox);
        this.updateActiveFiltersData();
        label = i18nKeys['line'];
        break;
      }
      case 'equipmentStatus': {
        filterCount = this.addCombinedFilter(activeFilterForm, $filtersCheckbox);
        this.updateActiveFiltersData();
        label = i18nKeys['equipmentStatus'];
        break;
      }
      case 'equipmentType': {
        filterCount = this.addCombinedFilter(activeFilterForm, $filtersCheckbox);
        this.updateActiveFiltersData();
        label = i18nKeys['equipmentType'];
        break;
      }
      case 'serialNumber': {
        this.cache.combinedFiltersObj['serialNumber'] = $freeTextFilterInput.val();
        this.updateActiveFiltersData();
        filterCount = $freeTextFilterInput.val() !== '' ? 1 : 0;
        label = i18nKeys['serialNumber'];
        break;
      }
      case 'equipmentTypeDesc': {
        this.cache.combinedFiltersObj['equipmentTypeDesc'] = $freeTextFilterInput.val();
        this.updateActiveFiltersData();
        filterCount = $freeTextFilterInput.val() !== '' ? 1 : 0;
        label = i18nKeys['equipmentDescription'];
        break;
      }
      case 'functionalLocation': {
        this.cache.combinedFiltersObj['functionalLocation'] = $freeTextFilterInput.val();
        this.updateActiveFiltersData();
        filterCount = $freeTextFilterInput.val() !== '' ? 1 : 0;
        label = i18nKeys['functionalLocation'];
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
      }
      filterCount = null;
      this.updateActiveFiltersData();
    }

    if ($activeFilterBtn) {
      if (filterCount) {
        $activeFilterBtn.addClass('active');
      } else {
        $activeFilterBtn.removeClass('active');
      }
    }

    if (label === i18nKeys['country']) {
      const countryFilterVal = filterData.filter((option) => {
        if (option.isChecked) {
          return option;
        }
      });
      this.renderTableData(label,countryFilterVal);
    } else {
      this.renderTableData(label);
      this.renderSearchCount();
      this.updateFilterCountValue(label,filterCount,$activeFilterBtn);
    }

    this.cache.$modal.modal('hide');
  }

  deleteAllFilters = () => {
    const $filterBtns = this.root.find('.tp-my-equipment__filter-button:not(.tp-my-equipment__country-button-filter)');
    this.cache.combinedFiltersObj = {};
    this.cache.activeSortData = null;
    this.updateActiveFiltersData();

    $filterBtns.each((index, item) => {
      const initialLabel = $(item).data('label');
      $(item).removeClass('active');
      $(item).text(initialLabel);
    });

    this.renderTableData(null,[]);
    this.renderSearchCount();
  }

  sortTableByKey = ($tHeadBtn) => {
    const sortedByKey = $tHeadBtn.data('key');
    const currentSortOrder = this.cache.activeSortData ? this.cache.activeSortData.sortOrder : '';
    this.cache.activeSortData = {
      'sortedByKey': sortedByKey,
      'sortOrder': currentSortOrder === 'asc' ? 'desc' : 'asc'
    };
    this.renderNewPage();
  }

  showHideAllFilters = () => {
    const $allBtnFilters = this.root.find('.tp-my-equipment__filter-button-all');
    const showLabel = this.cache.$showHideAllFiltersBtn.data('show-label');
    const hideLabel = this.cache.$showHideAllFiltersBtn.data('hide-label');
    const $label = this.cache.$showHideAllFiltersBtn.find('.tpatom-btn__text');
    const currentLabel = $.trim($label.text());
    $allBtnFilters.toggle();
    $label.text(currentLabel === showLabel ? hideLabel : showLabel);
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

  renderFilterForm(data, formDetail, $filterBtn) {
    const { i18nKeys } = this.cache;
    if (formDetail.header === i18nKeys['country']) {
      data.forEach((item,index) => {
        data[index]= {
          ...item,
          isCountry:true
        };
      });
    }

    render.fn({
      template: 'filterForm',
      data: {
        header: formDetail.header,
        formData: data,
        isEquipmentType: formDetail.header === i18nKeys['equipmentType'],
        ...i18nKeys,
        singleButton: formDetail.singleButton === false ? false : true,
        customiseTable: formDetail.activeForm === 'customise-table' ? true : false,
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

  renderCountryFilters(){
    return _renderCountryFilters.apply(this, arguments);
  }

  // maps all filters from combinedFiltersObj into an array that zapFilter uses
  updateActiveFiltersData() {
    const combinedFiltersObj = this.cache.combinedFiltersObj;
    this.cache.zapFilter.clearFilters();
    this.cache.activeFiltersArr = [];

    Object.keys(combinedFiltersObj).forEach(key => {
      if (typeof combinedFiltersObj[key] === 'string') {
        this.cache.activeFiltersArr.push({
          filter: this.cache.zapFilter.filterPartialMatch,
          onProperty: key,
          value: combinedFiltersObj[key]
        });
      } else {
        this.cache.activeFiltersArr.push({
          filter: this.cache.zapFilter.filterEqualToArr,
          onProperty: key,
          value: combinedFiltersObj[key]
        });
      }
    });
  }

  renderNewPage() {
    const {itemsPerPage, skipIndex, countryData, activeSortData} = this.cache;
    const equipmentApi = this.cache.equipmentApi.data('list-api');
    const activeCountry = countryData.filter(e => e.isChecked);
    const countryCode = activeCountry[0].countryCode;
    let apiUrlRequest = '';

    this.cache.$content.addClass('d-none');
    this.cache.$spinner.removeClass('d-none');

    if (activeSortData) {
      apiUrlRequest = `${equipmentApi}?skip=${skipIndex}&count=${itemsPerPage}&version=preview&countrycodes=${countryCode}&sortby=${activeSortData.sortedByKey.toLowerCase()}&sortdirection=${activeSortData.sortOrder}`;
    } else {
      apiUrlRequest = `${equipmentApi}?skip=${skipIndex}&count=${itemsPerPage}&version=preview&countrycodes=${countryCode}`;
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
          this.cache.filteredTableData = [...this.cache.tableData];
          this.cache.meta = response.meta;
          this.renderTableData();
          this.renderSearchCount();
          this.mapTableColumn();
        }).fail(() => {
          this.cache.$content.removeClass('d-none');
          this.cache.$spinner.addClass('d-none');
        });
    });
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.renderCountryFilters();
  }
}

export default MyEquipment;
