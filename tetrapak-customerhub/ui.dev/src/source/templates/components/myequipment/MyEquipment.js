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
          console.log('response>>>',response);
          this.cache.tableData = response.data;
          this.renderTableData();
        });
    });
}

function _processKeys(keys, ob) {
  return keys.length === 0 ? Object.keys(ob) : keys;
}

function _mapHeadings(keys) {
  return keys.map(key => ({
    key,
    i18nKey: `${key}` // TODO : add i18n keys
  }));
}

function _processTableData(data){
  let keys = [];
  if (Array.isArray(data.summary)) {
    data.summary = data.summary.map(summary => {
      keys = _processKeys(keys, summary);
      return tableSort.call(this, summary, keys);
    });
    data.summaryHeadings = _mapHeadings(keys);
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
    this.cache.$countryFilterLabel = this.root.find('.tp-my-equipment-country-button-filter');
    this.cache.$siteFilterLabel = this.root.find('.tp-my-equipment-site-button-filter');
    this.cache.countryData = [];
    this.cache.siteData = [{option:'site 1',isChecked:true},{option:'site 2',isChecked:false},{option:'site 3',isChecked:true},{option:'site 4',isChecked:true}];
    this.cache.activeFilterForm = 'country';
    this.cache.tableData = [];
  }
  bindEvents() {
    const { $modal, siteData } = this.cache;
    this.cache.$countryFilterLabel.on('click', () => {
      this.renderFilterForm(this.cache.countryData,{ activeFrom:'country',header:'Countries' });
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

  renderTableData = () => {
    render.fn({
      template: 'myEquipmentTable',
      data: _processTableData.call(this, {summary:this.cache.tableData}),
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
    this.updateFilterCountValue(label,filterCount,htmlUpdate);
  }

  updateFilterCountValue = (label,filterCount,htmlUpdate) => {
    const { $modal } = this.cache;
    htmlUpdate.text(`${label}: ${filterCount}`);
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
    const $ModalHeaderSelector = this.root.find('.js-my-equipment-filter');
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
