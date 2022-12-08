import $ from 'jquery';
import 'bootstrap';
import auth from '../../../scripts/utils/auth';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { ajaxMethods } from '../../../scripts/utils/constants';
import { _paginationAnalytics } from './RebuildingKits.analytics';
import { render } from '../../../scripts/utils/render';
import { getI18n } from '../../../scripts/common/common';
import { _paginate } from './RebuildingKits.paginate';
import { _getFormattedCountryData } from './RebuildingKits.utils';
import { _buildTableRows, _mapHeadings } from './RebuildingKits.table';

function _processKeys(keys, ob) {
  if (keys.length) {
    return keys;
  } else {
    let functionalLocation,
      equipmentDesc,
      serialNumber,
      rkNumber,
      rkDesc,
      implStatus,
      equipmentNumber;
    for (const i in ob) {
      if (i === 'lineCode') {
        functionalLocation = i;
      } else if (i === 'equipmentDesc') {
        equipmentDesc = i;
      } else if (i === 'serialNumber') {
        serialNumber = i;
      } else if (i === 'rkNumber') {
        rkNumber = i;
      } else if (i === 'rkDesc') {
        rkDesc = i;
      } else if (i === 'implStatus') {
        implStatus = i;
      } else if (i === 'equipmentNumber') {
        equipmentNumber = i;
      }

    }
    return [
      functionalLocation,
      equipmentDesc,
      serialNumber,
      rkNumber,
      rkDesc,
      implStatus,
      equipmentNumber
    ];
  }
}

function _processTableData(data) {
  let keys = [];
  if (Array.isArray(data.summary)) {
    data.summary = data.summary.map((summary) => {
      keys = _processKeys(keys, summary);
      this.cache.tableHeaders = keys;
      return _buildTableRows.call(this, summary, keys);
    });
    data.summaryHeadings = _mapHeadings(keys, data.i18nKeys);
  }
  return data;
}

class RebuildingKits {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};

  initCache() {
    this.cache.rkApi = this.root.find('.js-rk-api');
    this.cache.skipIndex = 0;
    this.cache.activePage = 1;
    this.cache.itemsPerPage = 25;
    this.cache.countryData = [];
    this.cache.configJson = this.root.find('.js-rk__config').text();
    this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    this.cache.tableData = [];
    this.cache.customisableTableHeaders = [];
    this.cache.tableHeaders = [];
    this.cache.$spinner = this.root.find('.tp-spinner');
    this.cache.$content = this.root.find('.tp-rk-content');
    this.cache.$searchResults = this.root.find('.tp-rk__search-count');
    this.cache.$pagination = this.root.find('.js-tbl-pagination');
  }

  mapTableColumn = () => {
    const { tableHeaders, customisableTableHeaders } = this.cache;
    for (let i = 0; i < tableHeaders.length; i++) {
      if (customisableTableHeaders[i]) {
        customisableTableHeaders[i] = {
          ...customisableTableHeaders[i],
          colIndex: i
        };
      }
    }
  };

  renderSearchCount = () => {
    this.cache.$searchResults.text(
      `${this.cache.meta.total} ${getI18n(
        this.cache.i18nKeys['searchResults']
      )}`
    );
  };

  renderPaginationTableData = (list) => {
    const paginationObj = _paginate(
      list.meta.total,
      this.cache.activePage,
      this.cache.itemsPerPage,
      3
    );

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
            summary: list.summary,
            paginationObj: paginationObj
          },
          target: '.tp-rk__table_wrapper',
          hidden: false
        },
        () => {
          // this.hideShowColums();
          $(function () {
            $('[data-toggle="tooltip"]').tooltip();
          });
        }
      );
    }
  };

  renderDefaultCountry = () => {
    this.cache.$spinner.removeClass('d-none');
    const countryApi = this.cache.rkApi.data('country-api');
    const rkApi = this.cache.rkApi.data('rklist-api');
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
          this.cache.countryData = _getFormattedCountryData(res.data);
          this.cache.authData = authData;
          const { countryCode } = this.cache.countryData && this.cache.countryData[0];
          const { itemsPerPage } = this.cache;

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
              this.renderSearchCount();
              this.mapTableColumn();
            })
            .fail(() => {
              this.cache.$content.removeClass('d-none');
              this.cache.$spinner.addClass('d-none');
            });
        });
    });
  };

  renderNewPage = ({resetSkip}) => {
    const {itemsPerPage, countryData} = this.cache;
    const rkApi = this.cache.rkApi.data('rklist-api');
    const activeCountry = countryData.filter(e => e.isChecked);
    const countryCode = activeCountry.length ? activeCountry[0].countryCode: '';
    let apiUrlRequest = '';
    const skipIndex = resetSkip ? 0 : this.cache.skipIndex;

    this.cache.$content.addClass('d-none');
    this.cache.$spinner.removeClass('d-none');

    if (resetSkip) {
      // reset indexes when a filter is set/removed
      this.cache.activePage = 1;
      this.cache.skipIndex = 0;
    }
    
    apiUrlRequest = `${rkApi}?skip=${skipIndex}&count=${itemsPerPage}&countrycodes=${countryCode}`;

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
            ...item
          }));
          this.cache.meta = response.meta;
          this.cache.countryData.splice(0, 1, {
            ...this.cache.countryData[0],
            isChecked: true
          });
          const tableData = _processTableData.call(this, {
            summary:this.cache.tableData,
            i18nKeys:this.cache.i18nKeys,
            meta:this.cache.meta
          });
          this.renderPaginationTableData(tableData);
          this.renderSearchCount();
          this.mapTableColumn();
        }).fail(() => {
          this.cache.$content.removeClass('d-none');
          this.cache.$spinner.addClass('d-none');
        });
    });
  }

  bindEvents = () => {
    const $this = this;
    // Pagination
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

    // Redirect to RK Detail Page
    this.root.on('click', '.tp-rk__table-summary__row:not(".tp-rk__table-summary__rowheading")',  (e) => {
      const clickLink = $(e.currentTarget);
      const equipmentNumber = clickLink.find('.tpmol-table__key-equipmentNumber').text();
      const rkNumber = clickLink.find('.tpmol-table__key-rkNumber').text();
      const equipmentDetailsUrl = $this.cache.rkApi.data('rkdetail-page');
      window.open(`${equipmentDetailsUrl}?rkNumber=${rkNumber}&equipment=${equipmentNumber}`, '_blank');
    });
  };

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.renderDefaultCountry();
  }
}

export default RebuildingKits;