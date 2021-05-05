/* eslint-disable no-console */
import $ from 'jquery';
import 'bootstrap';
// import { getURL } from '../../../scripts/utils/uri';
// import { ajaxMethods,MY_EQUIPMENT_COUNTRY } from '../../../scripts/utils/constants';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { tableSort } from '../../../scripts/common/common';
import { render } from '../../../scripts/utils/render';

function getFormattedData(array){
  array.forEach((item,index) => {
    array[index] = {
      option: item.countryName,
      isChecked: index === 0 || item.isChecked ? true: false
    };
  });
  return array;
}

function _renderCountryFilters() {
  ajaxWrapper
    .getXhrObj({
      // url: getURL(MY_EQUIPMENT_COUNTRY),
      // method: ajaxMethods.GET,
      // cache: true,
      // dataType: 'json',
      // contentType: 'application/json'
      // url: 'https://api.jsonbin.io/b/6079719c0ed6f819bead5f16',
      url: 'https://api.jsonbin.io/b/608a94a9acc8d11948f4f028',
      method: 'GET',
      contentType: 'application/json',
      dataType: 'json'
    }).then(res => {
      this.cache.countryData = getFormattedData(res.data);
      ajaxWrapper
        .getXhrObj({
          url: 'https://api.jsonbin.io/b/6079719c0ed6f819bead5f16',
          method: 'GET',
          contentType: 'application/json',
          dataType: 'json'
        }).then(response => {
          this.cache.tableData = response.data;
          this.cache.filteredTableData = [...this.cache.tableData];
          this.cache.countryData.splice(0,1,{...this.cache.countryData[0],isChecked:true});
          this.renderFilterForm(this.cache.countryData,{ activeFrom:'country',header:'Country' });
          this.applyFilter();
          this.renderTableData();
          this.renderSearchCount();
        });
    });
}

function _processKeys(keys, ob) {
  // return keys.length === 0 ? Object.keys(ob) : keys;
  if(keys.length){
    return keys;
  }else {
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
    i18nKey: getKeyMap(key,i18nKeys) // TODO : add i18n keys
  }));
}



function _processTableData(data){
  let keys = [];
  if (Array.isArray(data.summary)) {
    data.summary = data.summary.map(summary => {
      keys = _processKeys(keys, summary);
      return tableSort.call(this, summary, keys);
    });
    data.summaryHeadings = _mapHeadings(keys,data.i18nKeys);
  }
  return data;
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
    this.cache.configJson = this.root.find('.js-my-equipment__config').text();
    this.cache.countryData = [];
    this.cache.siteData = [{option:'site 1',isChecked:true},{option:'site 2',isChecked:false},{option:'site 3',isChecked:true},{option:'site 4',isChecked:true}];
    this.cache.activeFilterForm = 'country';
    this.cache.tableData = [];
    this.cache.filteredTableData = [];
    this.cache.i18nKeys = JSON.parse(this.cache.configJson);
  }
  bindEvents() {
    const { $modal, siteData } = this.cache;
    console.log('this.cache.i18nKeys>>>>>',this.cache.i18nKeys);
    this.cache.$countryFilterLabel.on('click', () => {
      this.renderFilterForm(this.cache.countryData,{ activeFrom:'country',header:'Country' });
      $modal.modal();
    });
    this.cache.$siteFilterLabel.on('click', () => {
      this.renderFilterForm(siteData, { activeFrom:'site',header:'Sites' });
      $modal.modal();
    });

    this.root.on('click', '.js-close-btn',  () => {
      $modal.modal('hide');
    });

    this.root.on('click', '.js-apply-filter-button',  () => {
      this.applyFilter();
    });
  }

  renderSearchCount = () => {
    this.cache.$searchResults.text(`${this.cache.filteredTableData.length} search results`);
  }

  renderTableData = () => {
    render.fn({
      template: 'myEquipmentTable',
      data: _processTableData.call(this, {summary:this.cache.tableData,i18nKeys:this.cache.i18nKeys}),
      target: '.tp-my-equipment__table_wrapper',
      hidden: false
    });
  }


  applyFilter =() => {
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
      default: {
        filterData = [];
        break;
      }
    }
    $filtersCheckbox.each(function(index){
      if($(this).is(':checked')){
        filterCount++;
        filterData[index].isChecked = true;
      }else {
        filterData[index].isChecked = false;
      }
    });
    if(filterCount){
      htmlUpdate.addClass('active');
    }
    this.updateFilterCountValue(label,filterCount,htmlUpdate);
  }

  updateFilterCountValue = (label,filterCount,htmlUpdate) => {
    const { $modal } = this.cache;
    if(!filterCount){
      htmlUpdate.text(`${label} +`);
    }else {
      htmlUpdate.text(`${label}: ${filterCount}`);
    }
    $modal.modal('hide');
  }

  renderFilterForm(data,formDetail) {
    render.fn({
      template: 'filterForm',
      data: data,
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
