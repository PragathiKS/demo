import $ from 'jquery';
import 'bootstrap';
import '@ashwanipahal/paginationjs';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { tableSort } from '../../../scripts/common/common';
import { render } from '../../../scripts/utils/render';
// import auth from '../../../scripts/utils/auth';
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
  // auth.getToken(({ data: authData }) => {
  ajaxWrapper
    .getXhrObj({
      // url: 'https://api-dev.tetrapak.com/mock/installbase/equipments/countries',
      url: 'https://api.jsonbin.io/b/608a94a9acc8d11948f4f028',
      method: ajaxMethods.GET,
      cache: true,
      dataType: 'json',
      contentType: 'application/json',
      // beforeSend(jqXHR) {
      //   jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
      //   jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
      // },
      showLoader: true

    }).then(res => {
      this.cache.countryData = getFormattedData(res.data);
      ajaxWrapper
        .getXhrObj({
          // url: 'https://api-dev.tetrapak.com/mock/installbase/equipments?count=1000',
          url: 'https://api.jsonbin.io/b/6079719c0ed6f819bead5f16',
          method: 'GET',
          contentType: 'application/json',
          dataType: 'json',
          // beforeSend(jqXHR) {
          //   jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
          //   jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
          // },
          showLoader: true
        }).then(response => {
          this.cache.tableData = response.data;
          this.cache.filteredTableData = [...this.cache.tableData];
          this.cache.countryData.splice(0,1,{...this.cache.countryData[0],isChecked:true});
          this.renderFilterForm(this.cache.countryData,{ activeFrom:'country',header:'Country' });
          this.applyFilter();
          this.renderTableData();
          this.renderSearchCount();
          this.mapTableColumn();
        });
    });
  // });

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
  let keyLabel;
  switch (key) {
    case 'countryCode': {
      keyLabel = i18nKeys['country'];
      break;
    }
    case 'siteName': {
      keyLabel = i18nKeys['site'];
      break;
    }
    case 'lineName': {
      keyLabel = i18nKeys['line'];
      break;
    }
    case 'equipmentDescription': {
      keyLabel = i18nKeys['equipmentDescription'];
      break;
    }
    case 'serialNumber': {
      keyLabel = i18nKeys['serialNumber'];
      break;
    }
    default: {
      keyLabel = '';
      break;
    }
  }
  return keyLabel;
}

function _mapHeadings(keys,i18nKeys) {
  return keys.map(key => ({
    key,
    myEquipment:true,
    i18nKey: getKeyMap(key,i18nKeys)
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
    firstText:'first',
    lastText:'last',
    pageNumber: options.isCustomiseTableFilter ? container.pagination('getSelectedPageNum') : 1,
    className: 'paginationjs-theme-blue',
    callback: function(data) {
      render.fn({
        template: 'myEquipmentTable',
        data: {...list,summary:data},
        target: '.tp-my-equipment__table_wrapper',
        hidden: false
      },() => {
        that.hideShowColums();
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
    this.cache.$myEquipmentHeading = this.root.find('.js-my-equipment__heading');
    this.cache.$myEquipmentCustomizeTable = this.root.find('.js-my-equipment__customise-table');
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
    const { $modal, $countryFilterLabel,i18nKeys,$siteFilterLabel,$myEquipmentHeading,$myEquipmentCustomizeTable } = this.cache;
    $countryFilterLabel.text(`${i18nKeys['country']} +`);
    $siteFilterLabel.text(`${i18nKeys['site']} +`);
    $myEquipmentHeading.text(`${i18nKeys['myEquipment']}`);
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
    $myEquipmentCustomizeTable.on('click', () => {
      this.renderFilterForm(this.cache.customisableTableHeaders, { activeFrom:'customise-table',header:'Customise Table',singleButton:false });
      $modal.modal();
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

  renderSearchCount = () => {
    this.cache.$searchResults.text(`${this.cache.filteredTableData.length} ${this.cache.i18nKeys['searchResults']}`);
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
    const { filteredTableData } = this.cache;
    const { tableData } = this.cache;
    if(label === 'Country' && filterValues.length === 0){
      this.cache.filteredTableData = tableData;
      return;
    }
    switch (label) {
      case 'Country':{
        this.cache.filteredTableData = tableData.filter((row) => {
          for(const i in filterValues){
            if(filterValues[i].countryCode === row.countryCode){
              return row;
            }
          }
        });
        break;
      }
      case 'Site':{
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
    const { activeFilterForm,$countryFilterLabel,$siteFilterLabel } = this.cache;
    const $filtersCheckbox = this.root.find('.js-tp-my-equipment-filter-checkbox');
    let filterCount = 0;
    let filterData = [];
    let label;
    let htmlUpdate;
    switch (activeFilterForm) {
      case 'country':{
        filterData = this.cache.countryData;
        label = 'Country';
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
        htmlUpdate.text(`${label} +`);
      }else {
        htmlUpdate.text(`${label}: ${filterCount}`);
      }
    }
    $modal.modal('hide');
  }

  renderFilterForm(data,formDetail) {
    const { i18nKeys } = this.cache;
    render.fn({
      template: 'filterForm',
      data: {formData: data,...i18nKeys,singleButton:formDetail.singleButton === false ? false : true},
      target: '.tp-equipment__filter-form',
      hidden: false
    });
    this.cache.activeFilterForm = formDetail.activeFrom;
    this.updateModalHeader(formDetail.header);
  }

  renderCountryFilters(){
    return _renderCountryFilters.apply(this, arguments);
  }

  updateModalHeader = (header) => {
    const $ModalHeaderSelector = this.root.find('.js-my-equipment__modal-header');
    $ModalHeaderSelector.text(`${header}`);
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.renderCountryFilters();
  }
}

export default MyEquipment;
