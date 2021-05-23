import $ from 'jquery';
import 'bootstrap';
import '@ashwanipahal/paginationjs';
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

function getFormattedSiteData(array){
  const sitesFlatArray = [];
  array.forEach((row) => {
    if(sitesFlatArray.indexOf(row.siteName) === -1){
      sitesFlatArray.push(row.siteName);
    }
  });
  sitesFlatArray.forEach((item,index) => {
    sitesFlatArray[index] = {
      option: item,
      isChecked: false
    };
  });
  return sitesFlatArray;
}

function _renderCountryFilters() {
  auth.getToken(({ data: authData }) => {
    ajaxWrapper
      .getXhrObj({
        url: 'https://api-mig.tetrapak.com/mock/installbase/equipments/countries',
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
        ajaxWrapper
          .getXhrObj({
            url: 'https://api-mig.tetrapak.com/mock/installbase/equipments',
            method: 'GET',
            contentType: 'application/json',
            dataType: 'json',
            beforeSend(jqXHR) {
              jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
              jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            },
            showLoader: true
          }).then(response => {
            this.cache.tableData = response.data;
            this.cache.filteredTableData = [...this.cache.tableData];
            this.cache.countryData.splice(0,1,{...this.cache.countryData[0],isChecked:true});
            this.renderFilterForm(this.cache.countryData,{ activeFrom:'country',header:this.cache.i18nKeys['country'] });
            this.applyFilter();
            this.renderTableData();
            this.renderSearchCount();
            this.mapTableColumn();
          });
      });
  });

}

function _processKeys(keys, ob) {
  if(keys.length){
    return keys;
  } else {
    let country, description,site,line,serialNumber;
    for(const i in ob){
      if(i === 'countryCode'){
        country = i;
      }else if(i === 'siteName'){
        site = i;
      }else if(i === 'lineName'){
        line = i;
      }
      else if(i === 'equipmentDescription'){
        description = i;
      }
      else if(i === 'serialNumber'){
        serialNumber = i;
      }
    }
    return [country, site,line, description,serialNumber];
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
    case 'equipmentDescription': {
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
    pageNumber: options.isCustomiseTableFilter ? container.pagination('getSelectedPageNum') : 1,
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

class MyEquipment {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$modal = this.root.parent().find('.js-filter-modal');
    this.cache.$countryFilterLabel = this.root.find('.tp-my-equipment__country-button-filter');
    this.cache.$siteFilterLabel = this.root.find('.tp-my-equipment__site-button-filter');
    this.cache.$searchResults = this.root.find('.tp-my-equipment__search-count');
    this.cache.$myEquipmentCustomizeTableAction = this.root.find('.js-my-equipment__customise-table-action');
    this.cache.$mobileHeadersActions = this.root.find('.js-mobile-header-actions');
    this.cache.configJson = this.root.find('.js-my-equipment__config').text();
    this.cache.countryData = [];
    this.cache.siteData = [];
    this.cache.activeFilterForm = 'country';
    this.cache.tableData = [];
    this.cache.customisableTableHeaders = [];
    this.cache.tableHeaders = [];
    this.cache.filteredTableData = [];
    this.cache.hideColumns = []; // column index;
    this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    this.cache.currentPageNumber = 1;

  }
  bindEvents() {
    const {$mobileHeadersActions, $modal,i18nKeys,$siteFilterLabel,$myEquipmentCustomizeTableAction } = this.cache;
    $siteFilterLabel.text(`${i18nKeys['site']} +`);
    this.cache.customisableTableHeaders = [{key:'countryCode',option:i18nKeys['country'],isChecked:true},
      {key:'siteName',option:i18nKeys['site'],isChecked:true},
      {key:'equipmentDescription',option:i18nKeys['equipmentDescription'],isChecked:true},
      {key:'serialNumber',option:i18nKeys['serialNumber'],isChecked:true}];
    this.cache.$countryFilterLabel.on('click', () => {
      this.renderFilterForm(this.cache.countryData,{ activeFrom:'country',header:i18nKeys['country'] });
      $modal.modal();
    });
    this.cache.$siteFilterLabel.on('click', () => {
      this.cache.siteData = getFormattedSiteData([...this.cache.filteredTableData]);
      this.renderFilterForm(this.cache.siteData, { activeFrom:'site',header:i18nKeys['site'] });
      $modal.modal();
    });
    $myEquipmentCustomizeTableAction.on('click', () => {
      this.renderFilterForm(this.cache.customisableTableHeaders, { activeFrom:'customise-table',header:i18nKeys['customizeTable'],singleButton:false });
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

    this.root.on('click', '.js-close-btn',  () => {
      $modal.modal('hide');
    });

    this.root.on('click', '.js-apply-filter-button',  () => {
      this.applyFilter();
    });

    this.root.on('click', '.js-tp-my-equipment__remove-button',  () => {
      this.applyFilter({removeFilter:true});
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
    let gotToFirstButton = `<div class="pagination-icon-wrapper icon-left"><i class="icon icon-pagination left icon-Right_new"></i><i class="icon icon-pagination left icon-Right_new"></i><span>${getI18n(i18nKeys['first'])}</span></div>`;
    let gotToLastButton = `<div class="pagination-icon-wrapper icon-right"><i class="icon icon-pagination icon-Right_new"></i><i class="icon icon-pagination icon-Right_new"></i><span>${getI18n(i18nKeys['last'])}</span></div>`;
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
    renderPaginationTableData.call(
      this,
      _processTableData.call(this, {summary:this.cache.filteredTableData,i18nKeys:this.cache.i18nKeys}),
      {isCustomiseTableFilter :label === 'customise-table' ? true : false });
  }

  hideShowColums = () => {
    const { customisableTableHeaders } = this.cache;
    for(const i in customisableTableHeaders){
      if(!customisableTableHeaders[i].isChecked){
        $(`.js-my-equipment__table-summary__cellheading--${i}`).addClass('hide');
        $(`.js-my-equipment__table-summary__cell--${i}`).addClass('hide');
      } else {
        $(`.js-my-equipment__table-summary__cellheading--${i}`).removeClass('hide');
        $(`.js-my-equipment__table-summary__cell--${i}`).removeClass('hide');
      }
    }
  }

  updateTable = (label,filterValues) => {
    const { filteredTableData, i18nKeys } = this.cache;
    const { tableData } = this.cache;
    if(label === i18nKeys['country'] && filterValues.length === 0){
      this.cache.filteredTableData = tableData;
      return;
    }
    switch (label) {
      case i18nKeys['country']:{
        this.cache.filteredTableData = tableData.filter((row) => {
          for(const i in filterValues){
            if(filterValues[i].countryCode === row.countryCode){
              return row;
            }
          }
        });
        break;
      }
      case i18nKeys['site']:{
        this.cache.filteredTableData = filteredTableData.filter((row) => {
          for(const i in filterValues){
            if(filterValues[i].option === row.siteName){
              return row;
            }
          }
        });
        break;
      }
      default: {
        this.cache.filteredTableData= filteredTableData;
        break;
      }
    }
  }


  applyFilter =(options) => {
    const { activeFilterForm,$countryFilterLabel,$siteFilterLabel,i18nKeys } = this.cache;
    const $filtersCheckbox = this.root.find('.js-tp-my-equipment-filter-checkbox');
    let filterCount = 0;
    let filterData = [];
    let label;
    let htmlUpdate;
    switch (activeFilterForm) {
      case 'country':{
        filterData = this.cache.countryData;
        label = i18nKeys['country'];
        htmlUpdate= $countryFilterLabel;
        break;
      }
      case 'site':{
        filterData = this.cache.siteData;
        label = 'Site';
        htmlUpdate= $siteFilterLabel;
        break;
      }
      case 'customise-table':{
        filterData = this.cache.customisableTableHeaders;
        label = 'customise-table';
        break;
      }
      default: {
        break;
      }
    }
    if(options && options.removeFilter){
      $filtersCheckbox.each(function(index){
        $(this).prop('checked',false);
        filterData[index].isChecked = false;
      });
    }else {
      $filtersCheckbox.each(function(index){
        if($(this).is(':checked')){
          filterCount++;
          filterData[index].isChecked = true;
        }else {
          filterData[index].isChecked = false;
        }
      });
    }

    if(htmlUpdate){
      if(filterCount){
        htmlUpdate.addClass('active');
      }else{
        htmlUpdate.removeClass('active');
      }
    }

    const filterValues = filterData.filter((option) => {
      if(option.isChecked){
        return option;
      }
    });

    this.renderTableData(label,filterValues);
    this.renderSearchCount();
    this.updateFilterCountValue(label,filterCount,htmlUpdate);
  }

  updateFilterCountValue = (label,filterCount,htmlUpdate) => {
    const { $modal } = this.cache;
    if(htmlUpdate){
      if(!filterCount){
        htmlUpdate.text(`${getI18n(label)} +`);
      }else {
        htmlUpdate.text(`${getI18n(label)}: ${filterCount}`);
      }
    }
    $modal.modal('hide');
  }

  renderFilterForm(data,formDetail) {
    const { i18nKeys } = this.cache;
    render.fn({
      template: 'filterForm',
      data: {header:formDetail.header, formData: data,...i18nKeys,singleButton:formDetail.singleButton === false ? false : true},
      target: '.tp-equipment__filter-form',
      hidden: false
    });
    this.cache.activeFilterForm = formDetail.activeFrom;
  }

  renderCountryFilters(){
    return _renderCountryFilters.apply(this, arguments);
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.renderCountryFilters();
  }
}

export default MyEquipment;
