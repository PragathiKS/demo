import $ from 'jquery';
import 'bootstrap';
import auth from '../../../scripts/utils/auth';
import file from '../../../scripts/utils/file';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { ajaxMethods } from '../../../scripts/utils/constants';
import { _paginationAnalytics, _customizeTableBtnAnalytics } from './RebuildingKits.analytics';
import { render } from '../../../scripts/utils/render';
import { getI18n } from '../../../scripts/common/common';
import { _paginate } from './RebuildingKits.paginate';
import { _getFormattedCountryData } from './RebuildingKits.utils';
import { _buildTableRows, _mapHeadings } from './RebuildingKits.table';
import {
  RK_COUNTRY_CODE,
  RK_LINE_CODE,
  RK_EQ_DESC,
  RK_MACHINE_SYSTEM,
  RK_SERIAL_NUMBER,
  RK_EQ_STATUS,
  RK_NUMBER,
  RK_DESC,
  RK_IMPL_STATUS,
  RK_IMPL_DATE,
  RK_IMPL_STATUS_DATE,
  RK_GENERAL_NUMBER,
  RK_TYPE_CODE,
  RK_RELEASE_DATE,
  RK_IMPL_DEADLINE,
  RK_STATUS,
  RK_HANDLING,
  RK_ORDER,
  RK_I18N_COUNTRY_CODE,
  RK_I18N_LINE_CODE,
  RK_I18N_EQ_DESC,
  RK_I18N_MACHINE_SYSTEM,
  RK_I18N_SERIAL_NUMBER,
  RK_I18N_EQ_STATUS,
  RK_I18N_NUMBER,
  RK_I18N_DESC,
  RK_I18N_IMPL_STATUS,
  RK_I18N_IMPL_DATE,
  RK_I18N_IMPL_STATUS_DATE,
  RK_I18N_GENERAL_NUMBER,
  RK_I18N_TYPE_CODE,
  RK_I18N_RELEASE_DATE,
  RK_I18N_IMPL_DEADLINE,
  RK_I18N_STATUS,
  RK_I18N_HANDLING,
  RK_I18N_ORDER,
  RK_PROPERTY_KEYS
} from './constants';

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
    this.cache.tableHeaders = RK_PROPERTY_KEYS;
    this.cache.$spinner = this.root.find('.tp-spinner');
    this.cache.$content = this.root.find('.tp-rk-content');
    this.cache.$searchResults = this.root.find('.tp-rk__search-count');
    this.cache.$pagination = this.root.find('.js-tbl-pagination');
    this.cache.$modal = this.root.parent().find('.js-filter-modal');
    this.cache.filterModalData = {};
    this.cache.$rkCustomizeTableAction = this.root.find('.js-rk__customise-table-action');
    this.cache.activeFilterForm = 'country';
    this.cache.combinedFiltersObj = {};
    this.cache.downloadservletUrl = this.root.find('#downloadCsvServletUrl').val();
  }

  processTableData = (data) => {
    if (Array.isArray(data.summary)) {
      data.summary = data.summary.map((summary) => _buildTableRows.call(this, summary, this.cache.tableHeaders));
      data.summaryHeadings = _mapHeadings(this.cache.tableHeaders, data.i18nKeys);
    }
    return data;
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
          this.hideShowColums();
          $(function () {
            $('[data-toggle="tooltip"]').tooltip();
          });
        }
      );
    }
  };

  hideShowColums = () => {
    const { customisableTableHeaders } = this.cache;
    for(const i in customisableTableHeaders){
      if(!customisableTableHeaders[i].isChecked){
        $(`.js-rk__table-summary__cellheading--${customisableTableHeaders[i].index}`).addClass('hide');
        $(`.js-rk__table-summary__cell--${customisableTableHeaders[i].index}`).addClass('hide');
      } else {
        $(`.js-rk__table-summary__cellheading--${customisableTableHeaders[i].index}`).removeClass('hide');
        $(`.js-rk__table-summary__cell--${customisableTableHeaders[i].index}`).removeClass('hide');
      }
    }
  }

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
              url: `${rkApi}?skip=0&count=${itemsPerPage}&countrycodes=${countryCode}&sort=lineCode asc,rkGeneralNumber asc,position asc`,
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
              const tableData = this.processTableData({
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
      // Reset indexes when a filter is set/removed
      this.cache.activePage = 1;
      this.cache.skipIndex = 0;
    }

    apiUrlRequest = `${rkApi}?skip=${skipIndex}&count=${itemsPerPage}&countrycodes=${countryCode}&sort=lineCode asc,rkGeneralNumber asc,position asc`;
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
          const tableData = this.processTableData({
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

  renderFilterForm = (data, formDetail, $filterBtn) => {
    const { i18nKeys } = this.cache;
    const selectedItemsNo = 0;

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
      target: '.tp-rk__filter-form',
      hidden: false
    });
    this.cache.activeFilterForm = formDetail.activeForm;
    this.cache.$activeFilterBtn = $filterBtn;
  }

  applyFilter = (options) => {
    const { activeFilterForm, $activeFilterBtn } = this.cache;
    const $filtersCheckbox = this.root.find('.js-tp-my-equipment-filter-checkbox:not(.js-tp-my-equipment-filter-group-checkbox)');
    const filterCount = 0;
    let filterData = [];
    let analyticsAction = {};

    switch (activeFilterForm) {
      case 'customise-table':{
        filterData = this.cache.customisableTableHeaders;
        $filtersCheckbox.each(function(index) {
          if ($(this).is(':checked')) {
            filterData[index].isChecked = true;
          } else {
            filterData[index].isChecked = false;
          }
        });
        break;
      }
      default: {
        break;
      }
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

    switch (activeFilterForm) {
      case 'customise-table': {
        const tableData = this.processTableData({ summary:this.cache.tableData,i18nKeys:this.cache.i18nKeys,meta:this.cache.meta });
        this.renderPaginationTableData(tableData);
        this.cache.$modal.modal('hide');
        break;
      }
      default: {
        this.renderNewPage({'resetSkip': true, analyticsAction});
        this.cache.$modal.modal('hide');
        break;
      }
    }
  }

  downloadCsv = () => {
    auth.getToken(() => {
      const url = this.cache.downloadservletUrl;
      file.get({
        extension: 'csv',
        url,
        method: ajaxMethods.GET
      });
    });
  }

  bindEvents = () => {
    const $this = this;
    const {$modal,i18nKeys,$rkCustomizeTableAction } = this.cache;

    this.cache.customisableTableHeaders = [
      {key:RK_COUNTRY_CODE,option:RK_COUNTRY_CODE,optionDisplayText:this.cache.i18nKeys[RK_I18N_COUNTRY_CODE],isChecked:true,index:0,isDisabled:false},
      {key:RK_LINE_CODE,option:RK_LINE_CODE,optionDisplayText:this.cache.i18nKeys[RK_I18N_LINE_CODE],isChecked:true,index:1,isDisabled:false},
      {key:RK_EQ_DESC,option:RK_EQ_DESC,optionDisplayText:this.cache.i18nKeys[RK_I18N_EQ_DESC],isChecked:true,index:2,isDisabled:false},
      {key:RK_MACHINE_SYSTEM,option:RK_MACHINE_SYSTEM,optionDisplayText:this.cache.i18nKeys[RK_I18N_MACHINE_SYSTEM],isChecked:true,index:3,isDisabled:false},
      {key:RK_SERIAL_NUMBER,option:RK_SERIAL_NUMBER,optionDisplayText:this.cache.i18nKeys[RK_I18N_SERIAL_NUMBER],isChecked:true,index:4,isDisabled:true},
      {key:RK_EQ_STATUS,option:RK_EQ_STATUS,optionDisplayText:this.cache.i18nKeys[RK_I18N_EQ_STATUS],isChecked:false,index:5,isDisabled:false},
      {key:RK_NUMBER,option:RK_NUMBER,optionDisplayText:this.cache.i18nKeys[RK_I18N_NUMBER],isChecked:true,index:6,isDisabled:true},
      {key:RK_DESC,option:RK_DESC,optionDisplayText:this.cache.i18nKeys[RK_I18N_DESC],isChecked:true,index:7,isDisabled:false},
      {key:RK_IMPL_STATUS,option:RK_IMPL_STATUS,optionDisplayText:this.cache.i18nKeys[RK_I18N_IMPL_STATUS],isChecked:true,index:8,isDisabled:false},
      {key:RK_IMPL_DATE,option:RK_IMPL_DATE,optionDisplayText:this.cache.i18nKeys[RK_I18N_IMPL_DATE],isChecked:false,index:9,isDisabled:false},
      {key:RK_IMPL_STATUS_DATE,option:RK_IMPL_STATUS_DATE,optionDisplayText:this.cache.i18nKeys[RK_I18N_IMPL_STATUS_DATE],isChecked:false,index:10,isDisabled:false},
      {key:RK_GENERAL_NUMBER,option:RK_GENERAL_NUMBER,optionDisplayText:this.cache.i18nKeys[RK_I18N_GENERAL_NUMBER],isChecked:false,index:11,isDisabled:false},
      {key:RK_TYPE_CODE,option:RK_TYPE_CODE,optionDisplayText:this.cache.i18nKeys[RK_I18N_TYPE_CODE],isChecked:false,index:12,isDisabled:false},
      {key:RK_RELEASE_DATE,option:RK_RELEASE_DATE,optionDisplayText:this.cache.i18nKeys[RK_I18N_RELEASE_DATE],isChecked:false,index:13,isDisabled:false},
      {key:RK_IMPL_DEADLINE,option:RK_IMPL_DEADLINE,optionDisplayText:this.cache.i18nKeys[RK_I18N_IMPL_DEADLINE],isChecked:false,index:14,isDisabled:false},
      {key:RK_STATUS,option:RK_STATUS,optionDisplayText:this.cache.i18nKeys[RK_I18N_STATUS],isChecked:false,index:15,isDisabled:false},
      {key:RK_HANDLING,option:RK_HANDLING,optionDisplayText:this.cache.i18nKeys[RK_I18N_HANDLING],isChecked:false,index:16,isDisabled:false},
      {key:RK_ORDER,option:RK_ORDER,optionDisplayText:this.cache.i18nKeys[RK_I18N_ORDER],isChecked:false,index:17,isDisabled:false}
    ];

    // Show/Hide Columns
    $rkCustomizeTableAction.on('click', () => {
      this.renderFilterForm(this.cache.customisableTableHeaders, { activeForm:'customise-table',header:i18nKeys['customizeTable'],singleButton:true });
      $('.tp-rk__header-actions').removeClass('show');
      $modal.modal();
      _customizeTableBtnAnalytics($rkCustomizeTableAction);
    });

    // Apply filters
    this.root.on('click', '.js-apply-filter-button',  () => {
      this.applyFilter();
    });

    // Close modal
    this.root.on('click', '.js-close-btn',  () => {
      $modal.modal('hide');
    });

    // Pagination
    this.root.on('click', '.js-page-number',  (e) => {
      const $btn = $(e.currentTarget);
      const $pagination = $btn.parents('.js-tbl-pagination');

      if (!$btn.hasClass('active')) {
        // Stop pointer events until new page is rendered, in order to not send multiple AJAX calls
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

    // Download CSV
    this.root.on('click', '.js-rk__export-csv-action',  () => {
      this.downloadCsv();
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
