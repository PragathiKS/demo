import $ from 'jquery';
import 'bootstrap';
import '@ashwanipahal/paginationjs';
import Zapfilter from 'zapfilter';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { tableSort,isMobile, getI18n } from '../../../scripts/common/common';
import { render } from '../../../scripts/utils/render';
import auth from '../../../scripts/utils/auth';
import { ajaxMethods } from '../../../scripts/utils/constants';

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

function getFilterModalData(filterByProperty, array, combinedFiltersObj, filteredTableData) {
  const filterOptionsArr = [];
  const zapFilter = new Zapfilter();

  array.forEach((row) => {
    if (filterOptionsArr.indexOf(row[filterByProperty]) === -1) {
      filterOptionsArr.push(row[filterByProperty]);
    }
  });

  filterOptionsArr.forEach((item,index) => {
    // check if filter option is available for current data set, otherwise mark it disabled
    let itemFilteredTableData = [];
    zapFilter.clearFilters();
    zapFilter.setFilters([{
      filter: zapFilter.filterEqualTo,
      onProperty: filterByProperty,
      value: item
    }]);

    itemFilteredTableData = zapFilter.filter(filteredTableData);

    filterOptionsArr[index] = {
      option: item,
      isChecked: combinedFiltersObj[filterByProperty] ? combinedFiltersObj[filterByProperty].includes(item) : false,
      isDisabled: typeof itemFilteredTableData[0] === 'undefined'
    };
  });

  // sort options alphabetically
  filterOptionsArr.sort(function(a, b) {
    return a.option.localeCompare(b.option);
  });

  return filterOptionsArr;
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
            url: `${equipmentApi}?countrycode=${countryCode}`,
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
            this.cache.countryData.splice(0,1,{...this.cache.countryData[0],isChecked:true});
            this.renderFilterForm(this.cache.countryData,{ activeForm:'country',header:this.cache.i18nKeys['country']}, this.cache.$countryFilterLabel);
            this.applyFilter();
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
    let country, description,site,line,serialNumber,equipmentStatus,functionalLocation;
    for(const i in ob){
      if(i === 'countryCode'){
        country = i;
      }else if(i === 'siteName'){
        site = i;
      }else if(i === 'lineName'){
        line = i;
      }
      else if(i === 'equipmentTypeDesc'){
        description = i;
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
    return [country, site, line, description, serialNumber, equipmentStatus, functionalLocation];
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

function _mapHeadings(keys,i18nKeys) {
  return keys.map(key => ({
    key,
    myEquipment:true,
    i18nKey: getKeyMap(key,i18nKeys).keyLabel,
    showTooltip:getKeyMap(key,i18nKeys).showTooltip,
    tooltipText:getKeyMap(key,i18nKeys).tooltipText
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
    data.summaryHeadings = _mapHeadings.call(this,keys,data.i18nKeys);
  }
  return data;
}

function renderPaginationTableData(list,options) {
  const that = this;
  const container = $('#pagination-container');
  if(list.summary.length === 0) {
    render.fn({
      template: 'myEquipmentTable',
      data: { noDataMessage:true, noDataFound :that.cache.i18nKeys['noDataFound']  },
      target: '.tp-my-equipment__table_wrapper',
      hidden: false
    });
    container.pagination('destroy');
  }
  else {
    container.pagination({
      dataSource: list.summary,
      showFirst:true,
      showLast:true,
      showFirstOnEllipsisShow: false,
      showLastOnEllipsisShow:false,
      firstText:'',
      lastText:'',
      pageRange:1,
      pageSize: 25,
      pageNumber: (options.isCustomiseTableFilter && container.pagination('getSelectedPageNum')) ? container.pagination('getSelectedPageNum') : 1,
      className: 'paginationjs-theme-tetrapak',
      callback: function(data) {
        render.fn({
          template: 'myEquipmentTable',
          data: {...list,summary:data},
          target: '.tp-my-equipment__table_wrapper',
          hidden: false
        },() => {
          that.hideShowColums();
          that.insertFirstAndLastElement();
          $(function () {
            $('[data-toggle="tooltip"]').tooltip();
          });
        });

      }
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
    this.cache.$siteFilterLabel = this.root.find('.tp-my-equipment__site-button-filter');
    this.cache.$customerFilterLabel = this.root.find('.tp-my-equipment__customer-button-filter');
    this.cache.$lineFilterLabel = this.root.find('.tp-my-equipment__line-button-filter');
    this.cache.$statusFilterLabel = this.root.find('.tp-my-equipment__status-button-filter');
    this.cache.$serialNumberFilterLabel = this.root.find('.tp-my-equipment__serial-button-filter');
    this.cache.$equipmentDescFilterLabel = this.root.find('.tp-my-equipment__equip-desc-button-filter');
    this.cache.$functionalLocFilterLabel = this.root.find('.tp-my-equipment__functional-loc-button-filter');
    this.cache.$searchResults = this.root.find('.tp-my-equipment__search-count');
    this.cache.$myEquipmentCustomizeTableAction = this.root.find('.js-my-equipment__customise-table-action');
    this.cache.$mobileHeadersActions = this.root.find('.js-mobile-header-actions');
    this.cache.$showHideAllFiltersBtn = this.root.find('.js-tp-my-equipment__show-hide-all-button');
    this.cache.configJson = this.root.find('.js-my-equipment__config').text();
    this.cache.$spinner = this.root.find('.tp-spinner');
    this.cache.$content = this.root.find('.tp-equipment-content');
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
  }
  bindEvents() {
    const {$mobileHeadersActions, $modal,i18nKeys,$myEquipmentCustomizeTableAction } = this.cache;
    this.cache.customisableTableHeaders = [{key:'countryCode',option:i18nKeys['country'],isChecked:true,index:0},
      {key:'siteName',option:i18nKeys['site'],isChecked:true,index:1},
      {key:'equipmentTypeDesc',option:i18nKeys['equipmentDescription'],isChecked:true,index:3},
      {key:'serialNumber',option:i18nKeys['serialNumber'],isChecked:true,index:4}];

    this.cache.$countryFilterLabel.on('click', () => {
      const formDetail = { activeForm:'country',header:i18nKeys['country'] };
      this.renderFilterForm(this.cache.countryData, formDetail, this.cache.$countryFilterLabel);
      $modal.modal();
    });

    this.cache.$siteFilterLabel.on('click', () => {
      const formDetail = {activeForm:'siteName',header:i18nKeys['site']};
      this.cache.filterModalData['siteName'] = getFilterModalData('siteName', [...this.cache.tableData], this.cache.combinedFiltersObj, [...this.cache.filteredTableData]);
      this.renderFilterForm(this.cache.filterModalData['siteName'], formDetail, this.cache.$siteFilterLabel);
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

    this.root.on('click', '.js-tp-my-equipment__remove-button',  () => {
      this.applyFilter({removeFilter:true});
    });

    this.root.on('click', '.js-tp-my-equipment__remove-all-button',  () => {
      this.deleteAllFilters();
    });

    this.cache.$showHideAllFiltersBtn.on('click', () => {
      this.showHideAllFilters();
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

  insertFirstAndLastElement = () => {
    const { i18nKeys } = this.cache;
    let gotToFirstButton = `<div class="pagination-icon-wrapper icon-left"><i class="icon icon-pagination left icon-Right_new"></i><i class="icon icon-pagination left icon-Right_new"></i><span class="first">${getI18n(i18nKeys['first'])}</span></div>`;
    let gotToLastButton = `<div class="pagination-icon-wrapper icon-right"><i class="icon icon-pagination icon-Right_new"></i><i class="icon icon-pagination icon-Right_new"></i><span class="last">${getI18n(i18nKeys['last'])}</span></div>`;
    if(isMobile()){
      gotToFirstButton = ('<div class="pagination-icon-wrapper icon-left"><i class="icon icon-pagination left icon-Right_new"></i><i class="icon icon-pagination left icon-Right_new"></i></div>');
      gotToLastButton = '<div class="pagination-icon-wrapper icon-right"><i class="icon icon-pagination icon-Right_new"></i><i class="icon icon-pagination icon-Right_new"></i></div>';
    }

    $('.paginationjs-first > a').not('.paginationjs-page > a').prepend(gotToFirstButton);
    $('.paginationjs-last > a').not('.paginationjs-page > a').prepend(gotToLastButton);
    $('.paginationjs-next > a').replaceWith('<a><i class="icon icon-pagination left icon-Right_new"></i></a>');
    $('.paginationjs-prev > a').replaceWith('<a><i class="icon icon-pagination icon-Right_new"></i></a>');
  }

  renderSearchCount = () => {
    this.cache.$searchResults.text(`${this.cache.filteredTableData.length} ${getI18n(this.cache.i18nKeys['searchResults'])}`);
  }

  renderTableData = (label,filterValues) => {
    this.updateTable(label,filterValues);
    if(!label || label !== this.cache.i18nKeys['country']){
      renderPaginationTableData.call(
        this,
        _processTableData.call(this, {summary:this.cache.filteredTableData,i18nKeys:this.cache.i18nKeys}),
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
            url: `${equipmentApi}?countrycode=${filterValues[0].countryCode}`,
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
            this.cache.filteredTableData = response.data;
            this.cache.filteredTableData = this.cache.filteredTableData.map((item) => ({
              ...item,
              equipmentStatus: item.equipmentStatus || ''
            }));
            renderPaginationTableData.call(
              this,
              _processTableData.call(this, {summary:this.cache.filteredTableData,i18nKeys:this.cache.i18nKeys}),
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
    const $filtersCheckbox = this.root.find('.js-tp-my-equipment-filter-checkbox');
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
      case 'siteName': {
        filterCount = this.addCombinedFilter(activeFilterForm, $filtersCheckbox);
        this.updateActiveFiltersData();
        label = i18nKeys['site'];
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
    this.updateActiveFiltersData();

    $filterBtns.each((index, item) => {
      const initialLabel = $(item).data('label');
      $(item).removeClass('active');
      $(item).text(initialLabel);
    });

    this.renderTableData(null,[]);
    this.renderSearchCount();
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
          filter: this.filterEqualToArr,
          onProperty: key,
          value: combinedFiltersObj[key]
        });
      }
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

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.renderCountryFilters();
  }
}

export default MyEquipment;
