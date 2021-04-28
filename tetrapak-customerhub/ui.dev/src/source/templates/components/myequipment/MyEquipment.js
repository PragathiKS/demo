/* eslint-disable no-console */
import $ from 'jquery';
import 'bootstrap';
import { render } from '../../../scripts/utils/render';

class MyEquipment {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$modal = this.root.parent().find('.js-filter-modal');
    this.cache.$countryFilterLabel = this.root.find('.tp-equipment-country-button-filter');
    this.cache.$siteFilterLabel = this.root.find('.tp-equipment-site-button-filter');
    this.cache.countryData = [{option:'filter 1',isChecked:true},{option:'filter 2',isChecked:false},{option:'filter 3',isChecked:true},{option:'filter 4',isChecked:false}];
    this.cache.siteData = [{option:'site 1',isChecked:true},{option:'site 2',isChecked:false},{option:'site 3',isChecked:true},{option:'site 4',isChecked:true}];
    this.cache.activeFilterForm = 'country';
  }
  bindEvents() {
    const { $modal,countryData, siteData } = this.cache;
    this.cache.$countryFilterLabel.on('click', () => {

      this.renderFilterForm(countryData,{ activeFrom:'country' });
      $modal.modal();
    });
    this.cache.$siteFilterLabel.on('click', () => {
      this.renderFilterForm(siteData, { activeFrom:'site' });
      $modal.modal();
    });

    this.root.on('click', '.js-close-btn',  () => {
      $modal.modal('hide');
    });

    this.root.on('click', '.js-apply-filter-button',  () => {
      this.applyFilter();
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
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default MyEquipment;
