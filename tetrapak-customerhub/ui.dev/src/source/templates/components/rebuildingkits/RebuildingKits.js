/* eslint-disable no-console */
import $ from 'jquery';
import 'bootstrap';
import auth from '../../../scripts/utils/auth';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { ajaxMethods } from '../../../scripts/utils/constants';
import { render } from '../../../scripts/utils/render';
// import { getI18n } from '../../../scripts/common/common';
import { _getFormattedCountryData } from './RebuildingKits.utils';
import { _buildTableRows, _mapHeadings } from './RebuildingKits.table';

function _processKeys(keys, ob) {
  if(keys.length){
    return keys;
  } else {
    let line, description, serialNumber, rkNumber, rkDescription, implStatus;
    for(const i in ob){
      if(i === 'line') {
        line = i;
      }
      else if(i === 'description'){
        description = i;
      }
      else if(i === 'serialNumber'){
        serialNumber = i;
      }
      else if(i === 'rkNumber'){
        rkNumber = i;
      }
      else if(i === 'rkDescription'){
        rkDescription = i;
      }
      else if(i === 'implStatus'){
        implStatus = i;
      }
    }
    return [line, description, serialNumber, rkNumber, rkDescription, implStatus];
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

class RebuildingKits {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};

  initCache() {
    this.cache.authData = {};
    this.cache.rkApi = this.root.find('.js-rk-api');
    this.cache.itemsPerPage = 25;
    this.cache.countryData = [];
    this.cache.configJson = this.root.find('.js-rk__config').text();
    this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    this.cache.$spinner = this.root.find('.tp-spinner');
    this.cache.$content = this.root.find('.tp-rk-content');
  }
  
  renderPaginationTableData = (list) => {
    console.log('Hiren Parmar Load Default Table >>>', list);
    /*
    const paginationObj = _paginate(
      list.meta.total,
      this.cache.activePage,
      this.cache.itemsPerPage,
      3
    );
    */

    if (list.summary.length === 0) {
      render.fn({
        template: 'rebuildingkitsTable',
        data: {
          noDataMessage: true,
          noDataFound: this.cache.i18nKeys['noDataFound']
        },
        target: '.tp-rk__table_wrapper',
        hidden: false
      });
    } else {
      render.fn(
        {
          template: 'rebuildingkitsTable',
          data: {
            ...list,
            summary: list.summary
            // paginationObj: paginationObj
          },
          target: '.tp-rk__table_wrapper',
          hidden: false
        },
        () => {
          // this.hideShowColums();
          // $(function () {
          //   $('[data-toggle='tooltip']').tooltip();
          // });
        }
      );
    }
  };

  renderDefaultCountry = () => {
    this.cache.$spinner.removeClass('d-none');
    const countryApi = this.cache.rkApi.data('country-api');
    const rkApi = this.cache.rkApi.data('rklist-api');
    console.log('Hiren Parmar - Default Country >>>', countryApi, rkApi);
    auth.getToken(({ data: authData }) => {
      ajaxWrapper
        .getXhrObj({
          url: countryApi,
          method: ajaxMethods.GET,
          cache: true,
          dataType: 'json',
          contentType: 'application/json',
          beforeSend(jqXHR) {
            jqXHR.setRequestHeader(
              'Authorization',
              `Bearer ${authData.access_token}`
            );
            jqXHR.setRequestHeader(
              'Content-Type',
              'application/x-www-form-urlencoded'
            );
          },
          showLoader: true
        })
        .then((res) => {
          console.log('Hiren Parmar - CountryData >>>', res);
          this.cache.countryData = _getFormattedCountryData(res.data);
          this.cache.authData = authData;
          const { countryCode } =
            this.cache.countryData && this.cache.countryData[0];
          const { itemsPerPage } = this.cache;

          // this.getAllAvailableFilterVals(['country'], true);

          ajaxWrapper
            .getXhrObj({
              url: `${rkApi}?skip=0&count=${itemsPerPage}&countrycodes=${countryCode}`,
              method: 'GET',
              contentType: 'application/json',
              dataType: 'json',
              beforeSend(jqXHR) {
                jqXHR.setRequestHeader(
                  'Authorization',
                  `Bearer ${authData.access_token}`
                );
                jqXHR.setRequestHeader(
                  'Content-Type',
                  'application/x-www-form-urlencoded'
                );
              },
              showLoader: true
            })
            .then((response) => {
              console.log('Hiren Parmar - RK List API >>>', response);
              this.cache.$spinner.addClass('d-none');
              this.cache.$content.removeClass('d-none');
              this.cache.tableData = response.data;
              this.cache.tableData = this.cache.tableData.map((item) => ({
                ...item
              }));
              this.cache.meta = response.meta;
              this.cache.countryData.splice(0, 1, {
                ...this.cache.countryData[0],
                isChecked: true
              });
              const tableData = _processTableData.call(this, {
                summary: this.cache.tableData,
                i18nKeys: this.cache.i18nKeys,
                meta: this.cache.meta
              });
              this.renderPaginationTableData(tableData);
              // this.renderSearchCount();
              this.mapTableColumn();
            })
            .fail(() => {
              this.cache.$content.removeClass('d-none');
              this.cache.$spinner.addClass('d-none');
              console.log('Hiren Parmar - Failed >>>', res);
            });
        });
    });
  };

  init() {
    /* Mandatory method */
    this.initCache();
    this.renderDefaultCountry();
    /*
    this.bindEvents();
    */
  }
}

export default RebuildingKits;
